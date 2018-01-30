package it.fabmazz.triestebus.model;

import android.support.v4.app.Fragment;
import it.fabmazz.triestebus.fragments.FragmentKind;

public abstract class PageParser<Result> {
    final int RESULT_OK = 0;
    final int GENERIC_ERROR = 1;
    public abstract void parseFromString(String strToParse);
    public abstract FragmentKind getRelatedFragmentType();
    public abstract Result getFinalResult();
}
