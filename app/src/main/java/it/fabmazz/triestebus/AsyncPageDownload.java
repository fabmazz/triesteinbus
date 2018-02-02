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

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import it.fabmazz.triestebus.backend.NetworkTools;
import it.fabmazz.triestebus.fragments.FragmentKind;
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

/**
 * Class to download stuff from the internet and display it
 * Might be nice to move all the UI part inside another component
 *
 */
public class AsyncPageDownload extends AsyncTask<String,DownloadResult,FragmentKind> {
    private static final String DEBUG_TAG = "AsyncPageDownloader";
    private PageParser parser;
    private WeakReference<AppCompatActivity> reference;
    public AsyncPageDownload(PageParser p, WeakReference<AppCompatActivity> c) {
        this.parser = p;
        this.reference = c;
    }

    @Override
    protected FragmentKind doInBackground(String... strings) {

        String responseString;

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
        try{
            responseString = resp.body().string();
            parser.parseFromString(responseString);

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            publishProgress(DownloadResult.SERVER_ERROR);
        }
        publishProgress(DownloadResult.DONE);

        return parser.getRelatedFragmentType();
    }

    @Override
    protected void onPostExecute(FragmentKind fragmentKind) {
        AppCompatActivity ac = reference.get();

        if(ac != null && fragmentKind != null){
            SwipeRefreshLayout srl = (SwipeRefreshLayout) ac.findViewById(R.id.swipeRefreshLayout);
            srl.setRefreshing(false);
            String listFragmentType;
            switch (fragmentKind){
                case STOPS:
                    listFragmentType = ResultListFragment.TYPE_STOPS;
                case ARRIVALS:
                    listFragmentType = ResultListFragment.TYPE_LINES;
                    Stop stop = (Stop) parser.getFinalResult();
                    if(stop.countLines() == 0){
                        Log.d(DEBUG_TAG,"Adapter has no elements");
                        showToastAndDie("No passages for this stop");
                    }else {
                        PalinaAdapter adapter = new PalinaAdapter(ac, stop);
                        ResultListFragment fragment = ResultListFragment.newInstance(listFragmentType);
                        fragment.setListAdapter(adapter);
                        fragment.setTextViewMessage(stop.getLocation());
                        addNewFragment(fragment,stop.ID,ac);
                    }
            }
            Log.d(DEBUG_TAG, "Fragment created");


        }
    }

    @Override
    protected void onProgressUpdate(DownloadResult... values) {
        DownloadResult res = values[0];
        switch(res){
            case NULL_STRING:
               showToastAndDie("Null string inserted");
                break;
            case SERVER_ERROR:
                showToastAndDie("Server error");
                break;
            case CONNECTION_ERROR:
                showToastAndDie("Connection error");
                break;
            case GENERIC_ERROR:
                showToastAndDie("An error occured");
                break;
            case DONE:
                Log.d(DEBUG_TAG,"Finished downloading");
            default:

        }



    }
    private void showToastAndDie(String text){
        AppCompatActivity act = reference.get();
        if(act!=null) {
            SwipeRefreshLayout srl = (SwipeRefreshLayout) act.findViewById(R.id.swipeRefreshLayout);
            srl.setRefreshing(false);
            Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
        }
        cancel(true);
    }

    /**
     * Shortcut method to add new Fragment
     * @param fragment the fragment to add
     * @param tag   the fragment tag
     * @param act   the activity to add it to, already "unwrapped"
     */

    protected void addNewFragment(Fragment fragment, String tag, AppCompatActivity act){
        FragmentManager fm = act.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.centralFrameLayout,fragment,tag);
        ft.addToBackStack("state"+tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        ft.commit();
    }
}
