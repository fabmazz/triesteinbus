/*
 * TriesteinBus, module app
 * Copyright (c) 2017 Fabio Mazza
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.fabmazz.triestebus;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import it.fabmazz.triestebus.backend.NetworkTools;
import it.fabmazz.triestebus.fragments.ResultListFragment;
import it.fabmazz.triestebus.model.DownloadResult;
import it.fabmazz.triestebus.model.PageParser;
import it.fabmazz.triestebus.model.Stop;
import it.fabmazz.triestebus.ui_components.PalinaAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.lang.ref.WeakReference;

public class AsyncPageDownload<P extends PageParser> extends AsyncTask<String,DownloadResult,String> {
    private static final String DEBUG_TAG = "AsyncPageDownloader";
    private P parser;
    private WeakReference<AppCompatActivity> reference;

    public AsyncPageDownload(P parser, WeakReference<AppCompatActivity> c) {
        this.parser = parser;
        this.reference = c;
    }

    @Override
    protected String doInBackground(String... strings) {

        String responseString = null;

        OkHttpClient theClient = NetworkTools.getClient();
        Request request = new Request.Builder().url(strings[0]).get().build();
        Response resp;
        try {
            resp = theClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(DownloadResult.CONNECTION_ERROR);
            return null;
        }
        if (!resp.isSuccessful()) {
            publishProgress(DownloadResult.GENERIC_ERROR);
            return null;
        }
        /*
        HttpURLConnection urlConnection;
        StringBuilder result = null;
        URL url = null;
        int connectionCode = 0;
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            connectionCode = urlConnection.getResponseCode();
        } catch(IOException e) {
            publishProgress(DownloadResult.CONNECTION_ERROR);
            e.printStackTrace();
            return null;
        }
        if (connectionCode == HttpsURLConnection.HTTP_OK || connectionCode == HttpURLConnection.HTTP_OK)
            try {
                InputStream in = new BufferedInputStream(
                        urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));
                result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
            } catch (Exception e) {
                Log.e("asyncwget", e.getMessage());
            } finally {
                urlConnection.disconnect();
            }
            if (result == null) {
                publishProgress(DownloadResult.NULL_STRING);
                return null;
            }
        else{
            publishProgress(DownloadResult.GENERIC_ERROR);
            Log.d("PageDownload","Status code different than 200: "+connectionCode);
            }

        **/
        publishProgress(DownloadResult.DONE);
        try{
            responseString = resp.body().string();
            parser.parseFromString(responseString);

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            publishProgress(DownloadResult.SERVER_ERROR);
        }

        return parser.getRelatedFragmentType();
    }

    @Override
    protected void onPostExecute(String fragmentType) {
        AppCompatActivity ac = reference.get();

        if(ac != null){
            Stop stop = parser.getCreatedStop();
            FragmentManager framan = ac.getSupportFragmentManager();
            FragmentTransaction ft = framan.beginTransaction();
            ResultListFragment fragment = ResultListFragment.newInstance(fragmentType);
            PalinaAdapter adapter = new PalinaAdapter(ac,stop);
            Log.d(DEBUG_TAG,"Adapter has "+adapter.getCount()+" elements");
            fragment.setListAdapter(adapter);
            fragment.setTextViewMessage(stop.getLocation());
            ft.replace(R.id.centralFrameLayout,fragment,stop.ID);
            ft.addToBackStack("Fragment_"+stop.ID);
            ft.commit();

            SwipeRefreshLayout srl = (SwipeRefreshLayout) ac.findViewById(R.id.swipeRefreshLayout);
            srl.setRefreshing(false);

            Log.d(DEBUG_TAG,"Fragment created");
        }
    }

    @Override
    protected void onProgressUpdate(DownloadResult... values) {
        super.onProgressUpdate(values);

    }

}
