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

import android.util.Log;
import it.fabmazz.triestebus.fragments.FragmentKind;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class should parse the information from the position, but doesn't work yet
 */
public class PositionParser extends PageParser<ArrayList<Stop>> {
    @Override
    public void parseFromString(String strToParse) {
        Document doc = Jsoup.parse(strToParse);
        Element[] scriptTags = doc.select("script").toArray(new Element[0]);
        Pattern p; // Regex try
        p = Pattern.compile("iIndex\\t+=(\\d+);\\n\\t+dLon\\t+=(\\d+.\\d+);\\n\\t+dLat*\\t+=(\\d+.\\d+);");
        Matcher m;
        for (Element script : scriptTags) {
            String str = script.html().replaceAll("\\ +", "");
            m = p.matcher(str);
            while (m.find()) {
                Log.d("AsyncDownload", "Element found");
                Log.d("element", "iIndex " + m.group(1) + ", dLon = " + m.group(2) + ", dLat = " + m.group(3));

            }
            //Log.i("Script",str);

        }
    }

    @Override
    public FragmentKind getRelatedFragmentType() {
        return FragmentKind.STOPS;
    }

    @Override
    public ArrayList<Stop> getFinalResult() {
        return null;
    }

}
