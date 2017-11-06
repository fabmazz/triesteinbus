/*
 * TriesteinBus, model part
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
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Stop {
    public final @NonNull String ID;
    private @Nullable
    String name;
    private @Nullable String username;
    public @Nullable String location;
    private @Nullable Double lat;
    private @Nullable Double lon;
    private ArrayList<Line> linesStoppingHere;



    public ArrayList<Line> getLinesStoppingHere() {
        return linesStoppingHere;
    }


    public Stop(@NonNull String ID, Double lat, Double lon) {
        this.ID = ID;
        this.lat = lat;
        this.lon = lon;
    }

    @NonNull
    public String getID() {
        return ID;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getUsername() {
        return username;
    }


    @Nullable
    public Double getLat() {
        return lat;
    }

    @Nullable
    public Double getLon() {
        return lon;
    }
    public String getIDwithFER(){
        return "FER-".concat(ID);
    }

    public Line getLine(int i){
        return linesStoppingHere.get(i);
    }
    public int countLines(){
        return linesStoppingHere.size();
    }
}