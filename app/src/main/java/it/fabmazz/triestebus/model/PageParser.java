package it.fabmazz.triestebus.model;

import android.support.v4.app.Fragment;

public interface PageParser {
    void parseFromString(String strToParse);
    String getRelatedFragmentType();
    Stop getCreatedStop();
}
