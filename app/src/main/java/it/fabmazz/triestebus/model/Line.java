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

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Line model for the app
 */
public class Line {
    private String direction;
    private String number;
    private String description;
    @Nullable private ArrayList<Integer> arrivalTimes;
    private static final String ARRIVING_STRING = "IN TRANSITO";
    private static final Integer ARRIVING_STRING_VALUE = 0;

    public Line(String name, String direction) {
        this.direction = direction;
        this.number = name;
    }

    public Line(String direction, String name, String description,@Nullable ArrayList<Integer> arrivalTimes) {
        this.direction = direction;
        this.number = name;
        this.description = description;
        this.arrivalTimes = arrivalTimes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArrivalTimes(ArrayList<Integer> arrivalTimes) {
        this.arrivalTimes = arrivalTimes;
    }

    public String getDirection() {
        return direction;
    }

    public String getName() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Integer> getArrivalTimes() {
        return arrivalTimes;
    }
    public String printArrivalTimesWith(CharSequence charSequence){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getArrivalTimesCount(); i++ ){
            if (i!=0) sb.append(charSequence);
            sb.append(getStringOfTime(arrivalTimes.get(i)));
        }
        return sb.toString();
    }
    public int getArrivalTimesCount(){
        return arrivalTimes.size();
    }
    public static Integer getNumericalTime(String timeS){
        Integer t;
        if(timeS.trim().equals(ARRIVING_STRING)) t = ARRIVING_STRING_VALUE;
        else {
            t = Integer.parseInt(timeS);
        }
        return t;
    }
    public static String getStringOfTime(Integer time){
        String s;
        if(time.equals(ARRIVING_STRING_VALUE)) s = ARRIVING_STRING;
        else {
            s = Integer.toString(time);
        }
        return s;
    }
    /**
     *  Check if is the same "tratta"
     * 	Many "tratta"s may have the same number but different direction
     * 	The direction is the important stuff
     */
    public boolean isTheSameAs(Line lt){
        if(number.equals(lt.getName()) && direction.equals(lt.getDirection())) return true;
        else return false;
    }
}
