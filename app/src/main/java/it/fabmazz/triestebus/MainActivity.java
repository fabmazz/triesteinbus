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

import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import it.fabmazz.triestebus.fragments.FragmentListener;
import it.fabmazz.triestebus.model.PageParser;
import it.fabmazz.triestebus.model.SingleStopParser;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentListener{

    Button searchButton;
    PageParser pp;
    EditText stopEditText;
    private SwipeRefreshLayout swipeRefreshLayout;
    FragmentManager framan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton  = (Button) findViewById(R.id.searchBtn);
        stopEditText = (EditText) findViewById(R.id.numEditText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        framan = getSupportFragmentManager();
        framan.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("MainActivity, TsiBus", "BACK STACK CHANGED");
            }
        });
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.centralFrameLayout);
        frameLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void createFragmentForStop(String ID) {

    }
    public void searchFromButton(View v){
        new AsyncPageDownload<>(new SingleStopParser(),new WeakReference<AppCompatActivity>(this)).execute("https://infomobility.triestetrasporti.it/tst/webapp/index.php?operation=8&point=FER-03002");
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

    }
}
