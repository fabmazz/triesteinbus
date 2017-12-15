/*
	BusTO (backend components)
    Copyright (C) 2016 Ludovico Pavesi

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.fabmazz.triestebus.ui_components;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import it.fabmazz.triestebus.R;
import it.fabmazz.triestebus.model.Line;
import it.fabmazz.triestebus.model.Stop;



/**
 * This once was a ListView Adapter for BusLine[].
 *
 * Thanks to Framentos developers for the guide:
 * http://www.framentos.com/en/android-tutorial/2012/07/16/listview-in-android-using-custom-listadapter-and-viewcache/#
 *
 * @author Valerio Bozzolan
 * @author Ludovico Pavesi
 */
public class PalinaAdapter extends ArrayAdapter<Line> {
    private LayoutInflater li;
    private static int row_layout = R.layout.entry_bus_line_passage;
    private static final int busBg = R.drawable.bus_icon_background;
    private static final int busIcon = R.drawable.bus_white;
    //private static final int cityIcon = R.drawable.city;

    // hey look, a pattern!
    private class ViewHolder {
        TextView rowStopIcon;
        TextView rowRouteDestination;
        TextView rowRouteTimetable;
    }

    public PalinaAdapter(Context context, Stop p) {
        super(context, row_layout, p.getLinesStoppingHere());
        li = LayoutInflater.from(context);
    }

    /**
     * Some parts taken from the AdapterBusLines class.<br>
     * Some parts inspired by these enlightening tutorials:<br>
     * http://www.simplesoft.it/android/guida-agli-adapter-e-le-listview-in-android.html<br>
     * https://www.codeofaninja.com/2013/09/android-viewholder-pattern-example.html<br>
     * And some other bits and bobs TIRATI FUORI DAL NULLA CON L'INTUIZIONE INTELLETTUALE PERCHÉ
     * SEMBRA CHE NESSUNO ABBIA LA MINIMA IDEA DI COME FUNZIONA UN ADAPTER SU ANDROID.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;

        if(convertView == null) {
            // INFLATE!
            // setting a parent here is not supported and causes a fatal exception, apparently.
            convertView = li.inflate(row_layout, null);

            // STORE TEXTVIEWS!
            vh = new ViewHolder();
            vh.rowStopIcon = (TextView) convertView.findViewById(R.id.routeID);
            vh.rowRouteDestination = (TextView) convertView.findViewById(R.id.routeDestination);
            vh.rowRouteTimetable = (TextView) convertView.findViewById(R.id.routesThatStopHere);

            // STORE VIEWHOLDER IN\ON\OVER\UNDER\ABOVE\BESIDE THE VIEW!
            convertView.setTag(vh);
        } else {
            // RECOVER THIS STUFF!
            vh = (ViewHolder) convertView.getTag();
        }

        Line line = getItem(position);
        vh.rowStopIcon.setText(line.getName());
        if(line.getDirection().length() == 0) {
            vh.rowRouteDestination.setVisibility(View.GONE);
        } else {
            // View Holder Pattern(R) renders each element from a previous one: if the other one had an invisible rowRouteDestination, we need to make it visible.
            vh.rowRouteDestination.setVisibility(View.VISIBLE);
            vh.rowRouteDestination.setText(line.getDirection());
        }


        // convertView could contain another background, reset it
        vh.rowStopIcon.setBackgroundResource(busBg);
        vh.rowRouteDestination.setCompoundDrawablesWithIntrinsicBounds(busIcon, 0, 0, 0);

        if(line.getArrivalTimesCount() == 0) {
            vh.rowRouteTimetable.setText(R.string.no_passages);
        } else {
           String resultString = line.printArrivalTimesWith(" ");
            vh.rowRouteTimetable.setText(resultString);
        }
        return convertView;
    }
}
