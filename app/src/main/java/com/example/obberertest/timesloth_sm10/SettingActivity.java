package com.example.obberertest.timesloth_sm10;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingActivity extends Activity {

    static String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onStart"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onStart"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onResume"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onResume"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onDestroy"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onDestroy"));
        //Here we are clearing back stack fragment entries
        int backStackEntry = getFragmentManager().getBackStackEntryCount();
        Log.d(TAG, "backStackEntry " + String.valueOf(backStackEntry));
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onRestart"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onRestart"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onStop"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onStop"));
    }
}
