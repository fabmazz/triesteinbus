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

package it.fabmazz.triestebus.model;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import it.fabmazz.triestebus.fragments.ResultListFragment;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SingleStopParser implements PageParser {
    private static final String DEBUG_TAG ="SingleStopParser";
    Stop stop;

    @Override
    public void parseFromString(@NonNull String strToParse) {
        int num;
        Element table,headtable;
        Elements rows,fields;
        //if we got a null string, check first

        Document doc = Jsoup.parse(strToParse);

        headtable = doc.select("table.tblLocalizeHeader").get(1);
        fields = headtable.select("td");
        //Log.d("AsyncPageDownloader","The header contains: "+fields.text());
        stop =  new Stop(fields.get(0).text(),fields.get(1).text());
        table = doc.select("table.tblLocalizeDetails").first();
        rows = table.select("tr");

        ArrayList<Line> linesArray = new ArrayList<>();
        ArrayList<Integer> arrivalTimesList = new ArrayList<>();
        Line line = null;
        for (num = 1; num < rows.size();num++){

            //This way we should avoid the first row which is the header
            Elements cols = rows.get(num).select("td");
            if(cols.size()>2) {
                String linenumber = cols.get(0).text();
                String description = cols.get(1).text();
                String direzione = cols.get(2).text();
                String arrivalTime = cols.get(3).text();
                //if (linenumber != null) Log.d("AsyncPageDownloader", "Found something: " + linenumber);
                if(!linenumber.isEmpty()){
                    //new line found
                    //add previous line to the list
                    if(line != null){
                        line.setArrivalTimes(arrivalTimesList);
                        linesArray.add(line);
                    }
                    //make new line
                    line = new Line(linenumber,description);
                    line.setDirection(direzione);
                    arrivalTimesList = new ArrayList<>();
                    //Log.d(DEBUG_TAG,"Creating new Line");
                } else if(!direzione.isEmpty()){
                    //There is no number, but the direction has changed
                    //New line but with same description and number
                    if(line != null){
                        line.setArrivalTimes(arrivalTimesList);
                        linesArray.add(line);
                    }
                    //It says it can get a NullPointerException, but it's impossible that
                    //the line number is empty and the direction is not in the first passage
                    line.setDirection(direzione);
                    arrivalTimesList = new ArrayList<>();
                    //Log.d(DEBUG_TAG,"Creating new line with same number and description, num = "+num);
                }
                //in every case, we have a new passage to add
                arrivalTimesList.add(Line.getNumericalTime(arrivalTime));
                //IF WE HAVE ARRIVED AT THE LAST ROW
                if (num == rows.size()-1){
                    //Log.d(DEBUG_TAG,"Last row");
                    line.setArrivalTimes(arrivalTimesList);
                    linesArray.add(line);
                }

            } else
                Log.w("AsyncPageDownloader","ERROR: size = 0"+cols.text());
        }
        if (linesArray.size() !=0)
         stop.setLinesStoppingHere(linesArray);
        //TODO: Handle this case gracefully
        else Log.w(DEBUG_TAG, "No lines found; The app will probably crash");
    }

    @Override
    public String getRelatedFragmentType() {
        return ResultListFragment.TYPE_LINES;
    }

    @Override
    public Stop getCreatedStop() {
        return stop;
    }
}
