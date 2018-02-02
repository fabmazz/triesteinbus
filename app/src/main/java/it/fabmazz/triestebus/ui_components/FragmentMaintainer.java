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

package it.fabmazz.triestebus.ui_components;

import android.support.v4.app.FragmentManager;
import it.fabmazz.triestebus.fragments.FragmentKind;
import java.util.EnumMap;

public class FragmentMaintainer {
    private FragmentManager fm;
    private static FragmentMaintainer INSTANCE;
    private EnumMap<FragmentKind,String> currentShowingFragments;
    public FragmentMaintainer(FragmentManager framan){
        //Let's use an empty constructor for the moment
        currentShowingFragments =  new EnumMap<>(FragmentKind.class);
        fm = framan;
    }
    /*
    public void linkFragmentManager(FragmentManager framan){
        fm = framan;
    }
    */
    public String getCurrentFragmentKey (FragmentKind fk){
        return currentShowingFragments.get(fk);
    }
}
