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

import java.util.ArrayList;

/**
 * Line model for the app
 */
public class Line {
    private String direction;
    private String name;
    private String description;
    private ArrayList<String> arrivalTimes;

    public Line(String direction, String name) {
        this.direction = direction;
        this.name = name;
    }

    public Line(String direction, String name, String description, ArrayList<String> arrivalTimes) {
        this.direction = direction;
        this.name = name;
        this.description = description;
        this.arrivalTimes = arrivalTimes;
    }

    public String getDirection() {
        return direction;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getArrivalTimes() {
        return arrivalTimes;
    }
    public String printArrivalTimesWith(CharSequence charSequence){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getArrivalTimesCount(); i++ ){
            if (i!=0) sb.append(charSequence);
            sb.append(arrivalTimes.get(i));
        }
        return sb.toString();
    }
    public int getArrivalTimesCount(){
        return arrivalTimes.size();
    }
}
