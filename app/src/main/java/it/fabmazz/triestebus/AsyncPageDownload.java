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
import android.util.Log;
import it.fabmazz.triestebus.backend.NetworkTools;
import it.fabmazz.triestebus.model.DownloadResult;
import it.fabmazz.triestebus.model.PageParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.annotation.Documented;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsyncPageDownload<PageParser> extends AsyncTask<String,DownloadResult,String> {
    @Override
    protected String doInBackground(String... strings) {

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
        String responseString = null;
        try {
            Log.d("AsyncPageDownloader","Downloaded data");
            int num;
            responseString = resp.body().string();
            Document doc = Jsoup.parse(responseString);
            Element table = doc.select("table.tblLocalizeDetails").first();
            Elements rows = table.select("tr");
            for (num = 1; num < rows.size();num++){
                //This way we should avoid the first row which is the header
                Elements cols = rows.get(num).select("td");
                if(cols.size()>0) {
                    String linea = cols.get(0).text();
                    String direzione = cols.get(2).text();
                    //if (linea != null) Log.d("AsyncPageDownloader", "Found something: " + linea);
                    Log.d("AsyncPageDownloader", "Found something: " + linea);
                    //Skip the line if there is no info
                    if ((linea == null || linea.isEmpty()) && (direzione == null || direzione.isEmpty())) continue;

                } else Log.w("AsyncPageDownloader","ERROR: size = 0"+cols.text());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }


        return null; //TODO change this once we have a good parser
    }

    @Override
    protected void onPostExecute(String stringtoParse) {
        super.onPostExecute(stringtoParse);
    }

    @Override
    protected void onProgressUpdate(DownloadResult... values) {
        super.onProgressUpdate(values);
    }
}
