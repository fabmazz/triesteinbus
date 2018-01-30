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

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO
                //Temporary Fix
                stopRotatingRefresh();
            }
        });

        stopEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchFromMainEditText(v);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void createFragmentForStop(String ID) {

    }
    public void searchFromMainEditText(View v){
        String stop = String.valueOf(stopEditText.getText());
        if (stop.isEmpty() || stop.length() < 4){
            Toast.makeText(this, "Enter more than 4 characters", Toast.LENGTH_SHORT).show();
        } else {
            new AsyncPageDownload<>(new SingleStopParser(), new WeakReference<AppCompatActivity>(this))
                    .execute("https://infomobility.triestetrasporti.it/tst/webapp/index.php?operation=8&point=FER-"+stop);
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        hideKeyboard();
    }

    /**
     * Methods from BusTO
     * (Thanks to Valerio Bozzolan and Ludovico Pavesi)
      */
    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(stopEditText, InputMethodManager.SHOW_IMPLICIT);
        Log.d("BUSTO Keyboard", "Shown");
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private synchronized void stopRotatingRefresh(){
        swipeRefreshLayout.setRefreshing(false);
    }
}
