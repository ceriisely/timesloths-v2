package com.example.obberertest.timesloth_sm10;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class AboutActivity extends Activity {

    static String TAG = "AboutActivity";

    private SiteData Site_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        setContentView(R.layout.activity_about);

        ArrayList<SiteData> site_data = this.getIntent().getParcelableArrayListExtra("Site_data");
        Site_data = site_data.get(0);

        View decor_View = getWindow().getDecorView();
        int ui_Options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decor_View.setSystemUiVisibility(ui_Options);


        bindView();
    }

    private void bindView(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        Button btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(AboutActivity.this, "cancel");
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_back", "Button"));
                //SiteData.writeFile(AboutActivity.this, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_back", "Button"));
                finish();
            }
        });
    }

    private void updateView(){
        TextView text = findViewById(R.id.text_detail);
        String ip =  SiteData.getDeviceIPAddress(true);
        text.setText(String.format("Licensed to Jaspal Company Limited\n%s\nServer Address %s\nNode IP Address %s\n\nAll Right Reserved", Site_data.Version, Site_data.Server_address, ip));
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

        updateView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onStop"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onStop"));
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
        //Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onRestart"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onRestart"));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getAction() == KeyEvent.KEYCODE_ENTER)){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        }
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            assert w != null;
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);


            }
        }
        return ret;
    }
}
