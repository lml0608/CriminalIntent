package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by liubin on 2017/2/8.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
