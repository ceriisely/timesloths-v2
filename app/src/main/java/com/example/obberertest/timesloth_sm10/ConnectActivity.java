package com.example.obberertest.timesloth_sm10;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class ConnectActivity extends Activity {

    String TAG = "ConnectActivity";

    private String server_address;
    private int room_id;
    private String path;
    public SiteData Site_data;
    public Room Room_info;
    public ArrayList<Room> All_Room = new ArrayList<Room>();
    private FragmentTransaction fragmentTransaction;
    private ConnectActivity Context;
    public Boolean FeedAsync = true;
    private Room Main_Room;
    RoomConnectFragment Room_connect_fragment;
    SiteConnectFragment Site_connect_fragment;
    ModuleConnectEthernetFragment Connect_ethernet_fragment;
    ModuleCheckInvalidIpFragment Check_invalid_ip_fragment;
    ModuleConnectServerFragment Connect_server_fragment;
    ModuleNetworkDiagFragment Network_diag_fragment;
    boolean isConnectNetworkAll = false;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_connect);
        Context = this;
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(android.content.Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
        Settings.System.putInt(getContentResolver(),Settings.System.SCREEN_OFF_TIMEOUT, 3600000);
        wakeLock.release();

        bindButton();
    }

    void finishConnectNetwork(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "finishConnectNetwork"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "finishConnectNetwork"));
        isConnectNetworkAll = true;
    }

    private void bindButton(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button btn_setting = findViewById(R.id.Connect_btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(ConnectActivity.this, "touch");
                Log.d(TAG, TAG_MODIFIED.tagOnClick("Connect_btn_setting", "Button"));
                //SiteData.writeFile(ConnectActivity.this, TAG + " | " + TAG_MODIFIED.tagOnClick("Connect_btn_setting", "Button"));
                if(Network_diag_fragment.isAdded()){
                    if(!Room_connect_fragment.isAdded()) {
                        try {
                            getFragmentManager().beginTransaction().add(R.id.fragment_container, Room_connect_fragment).commit();
                        } catch (IllegalStateException e) {
                            getFragmentManager().beginTransaction().add(R.id.fragment_container, Room_connect_fragment).commitAllowingStateLoss();
                            SiteData.writeFile(ConnectActivity.this, TAG + " | " + TAG_MODIFIED.tagOnClick("Connect_btn_setting", "Button") + " - " + e.getMessage());
                        }
                    }
                    Network_diag_fragment.stopConnectNetwork(true);
                }
            }
        });
    }

    private void getDataShared() {
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "getDataShared"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "getDataShared"));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        server_address = prefs.getString("server_address", "room.jaspal.co.th/admin/timesloth");
        path = prefs.getString("path", "/");
        room_id = prefs.getInt("room_id", 1);
        String language = prefs.getString("language", "en");
        int time_refresh = prefs.getInt("time_refresh", 30000);
        String unit_time = prefs.getString("unit_time", "24");
        Site_data = new SiteData(this, server_address, room_id, path, language, time_refresh, unit_time);
    }

    void IntentToMainActivity(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "onStart"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "onStart"));
        for(int i=0;i<All_Room.size();i++){
            if(All_Room.get(i).Status){
                Log.d("open",All_Room.get(i).Name);
            }else {
                Log.d("close",All_Room.get(i).Name);
            }
        }
        for(int i=0;i<All_Room.size();i++){
            Log.d(All_Room.get(i).Name, String.valueOf(All_Room.get(i).Status));
            if(All_Room.get(i).Id == Site_data.Room_id){
                Main_Room = All_Room.get(i);
                break;
            }
        }

        Intent intent = new Intent(Context, MainActivity.class);
        ArrayList<SiteData> site_data = new ArrayList<SiteData>();
        site_data.add(Site_data);

        ArrayList<Room> main_room = new ArrayList<Room>();
        main_room.add(Main_Room);

        Log.d("main_room", String.valueOf(main_room.get(0).Slots.size()));

        intent.putParcelableArrayListExtra("Site_data", site_data);
        intent.putParcelableArrayListExtra("All_Room", All_Room);
        intent.putParcelableArrayListExtra("Main_Room", main_room);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("server_address", Site_data.Server_address);
        editor.putString("path", Site_data.Path);
        editor.putInt("room_id", Site_data.Room_id);
        editor.putString("language", SiteData.Language);
        editor.putInt("time_refresh", Site_data.TimeRefresh);
        editor.putString("unit_time", Site_data.UnitTime);
        editor.apply();

        startActivity(intent);
        finish();
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
        getDataShared();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Network_diag_fragment = new ModuleNetworkDiagFragment(this);
        fragmentTransaction.add(R.id.fragment_container, Network_diag_fragment);
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            fragmentTransaction.commitAllowingStateLoss();
            SiteData.writeFile(ConnectActivity.this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onResume") + " - " + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
        SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
    }

    @SuppressLint("NewApi")
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
        if (Room_connect_fragment.isAdded()) {
            getFragmentManager().beginTransaction().remove(Room_connect_fragment).commit();
        }
        if (Network_diag_fragment.isAdded()) {
            getFragmentManager().beginTransaction().remove(Network_diag_fragment).commit();
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
