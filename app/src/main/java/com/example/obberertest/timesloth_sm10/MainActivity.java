package com.example.obberertest.timesloth_sm10;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import android.support.v4.content.WakefulBroadcastReceiver;

public class MainActivity extends Activity {

    String TAG = "MainActivity";

    SiteData Site_data;
    ArrayList<Room> All_Room = new ArrayList<Room>();
    private FragmentTransaction fragmentTransaction;
    private MainActivity Context;
    Room Main_Room;
    ModuleDefaultMainFragment module_default_main_fragment;
    ManageMainFragment manage_main_fragment;
    ModuleFrameUseNowFragment module_frame_use_now_fragment;
    ModuleFrameExtendFragment module_frame_extend_fragment;
    ModuleFrameEndNowFragment module_frame_end_now_fragment;
    ModuleFrameCancelNowFragment module_frame_cancel_now_fragment;
    ModuleAlertPopupFragment module_frame_alert_popup_fragment;
    ModuleSettingFragment module_setting_fragment;
    ModuleFindAllRoomFragment module_find_all_room_fragment;
    ModuleLoaderFragment module_loader_fragment;
    ModulePasswordFragment module_password_fragment;
    private int timestamp_restart = 15;
    private DevicePolicyManager deviceManger;
    private ActivityManager activityManager;
    private ComponentName compName;
    private Thread time_thread;
    private static PowerManager pm;
    private static PowerManager.WakeLock wakeLock;
    private static PowerManager.WakeLock sleepLock;
    ModuleRebootFragment module_reboot_fragment;
    ModuleReadRFIDFragment module_read_rfid_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onCreate"));
        setContentView(R.layout.activity_main);
        Context = this;
        pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        assert pm != null;
        //final PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        sleepLock = pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK), "TAG");
//        View decor_View = getWindow().getDecorView();
//
//        int ui_Options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//
//        decor_View.setSystemUiVisibility(ui_Options);

        ArrayList<SiteData> site_data = this.getIntent().getParcelableArrayListExtra("Site_data");
        Site_data = site_data.get(0);

        ArrayList<Room> main_room = this.getIntent().getParcelableArrayListExtra("Main_Room");
        Main_Room = main_room.get(0);

        All_Room = this.getIntent().getParcelableArrayListExtra("All_Room");
        Log.d("Room Status", String.valueOf(Main_Room.Status));
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        module_loader_fragment = new ModuleLoaderFragment(Context);
        fragmentTransaction.add(R.id.fragment_main_activity_container_loader, module_loader_fragment, "TAG");

        module_frame_alert_popup_fragment = new ModuleAlertPopupFragment(Context);
        fragmentTransaction.add(R.id.fragment_main_activity_container_loader, module_frame_alert_popup_fragment, "TAG");

        module_setting_fragment = new ModuleSettingFragment(Context);
        //fragmentTransaction.add(R.id.fragment_main_activity_container_front, module_setting_fragment, "TAG");

        module_read_rfid_fragment = new ModuleReadRFIDFragment(Context);
        fragmentTransaction.add(R.id.fragment_main_activity_container_rfid, module_read_rfid_fragment, "TAG");

        manage_main_fragment = new ManageMainFragment(Context);
        module_default_main_fragment = new ModuleDefaultMainFragment(Context);
        module_frame_use_now_fragment = new ModuleFrameUseNowFragment(Context);
        module_frame_extend_fragment = new ModuleFrameExtendFragment(Context);
        module_frame_end_now_fragment = new ModuleFrameEndNowFragment(Context);
        module_frame_cancel_now_fragment = new ModuleFrameCancelNowFragment(Context);
        module_find_all_room_fragment = new ModuleFindAllRoomFragment(Context);
        module_password_fragment = new ModulePasswordFragment(Context);
        module_reboot_fragment = new ModuleRebootFragment(Context);

        updateUi(Main_Room.Status);
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e){
            fragmentTransaction.commitAllowingStateLoss();
            SiteData.writeFile(MainActivity.this, TAG + " | onCreate " + e.getMessage());
        }

        bindButton();
        runThread();

        if (android.os.Build.VERSION.SDK_INT > 19) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    void updateUi(boolean room_status){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateUi") + " - " + TAG_MODIFIED.tagArgument("boolean", "room_status", String.valueOf(room_status)));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateUi") + " - " + TAG_MODIFIED.tagArgument("boolean", "room_status", String.valueOf(room_status)));
        if (room_status) {
            try {
                getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_front, manage_main_fragment, "TAG").commit();
                getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_back, module_default_main_fragment, "TAG").commit();
            }catch (IllegalStateException e){
                getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_front, manage_main_fragment, "TAG").commitAllowingStateLoss();
                getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_back, module_default_main_fragment, "TAG").commitAllowingStateLoss();
                SiteData.writeFile(MainActivity.this, TAG + " | updateUi " + String.valueOf(room_status) + " " + e.getMessage());
            }

        }else {
            try {
                if (manage_main_fragment.isAdded()) {
                    getFragmentManager().beginTransaction().remove(manage_main_fragment).commit();
                }
                if (module_default_main_fragment.isAdded()) {
                    getFragmentManager().beginTransaction().remove(module_default_main_fragment).commit();
                }
            } catch (IllegalStateException e){
                SiteData.writeFile(MainActivity.this, TAG + " | updateUi " + String.valueOf(room_status) + " " + e.getMessage());
                if (manage_main_fragment.isAdded()) {
                    getFragmentManager().beginTransaction().remove(manage_main_fragment).commitAllowingStateLoss();
                }
                if (module_default_main_fragment.isAdded()) {
                    getFragmentManager().beginTransaction().remove(module_default_main_fragment).commitAllowingStateLoss();
                }
            }
        }
    }

    private void bindButton(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(this, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button setting = findViewById(R.id.btn_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Context, "touch");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_setting", "Button"));
                //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_setting", "Button"));
                try {
                    getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_front, module_password_fragment).commit();
                    //updateUi(false);
                } catch (IllegalStateException e){
                    getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_front, module_password_fragment).commitAllowingStateLoss();
                    SiteData.writeFile(MainActivity.this, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_setting", "Button") + " - " + e.getMessage());
                    //updateUi(false);
                }
            }
        });

        Button about = findViewById(R.id.btn_about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Context, "touch");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_about", "Button"));
                //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_about", "Button"));
                Intent intent = new Intent(Context, AboutActivity.class);

                ArrayList<SiteData> site_data = new ArrayList<SiteData>();
                site_data.add(Site_data);

                intent.putParcelableArrayListExtra("Site_data", site_data);
                startActivity(intent);
            }
        });

        Button find_room = findViewById(R.id.btn_find_all_room);
        if (!Main_Room.Status){
            find_room.setVisibility(View.INVISIBLE);
        }
        find_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Context, "allroom");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_find_all_room", "Button"));
                //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_find_all_room", "Button"));
                manage_main_fragment.new FeedAsynTaskAllRoom().execute();
            }
        });

        final Button change_language = findViewById(R.id.btn_change_language);
        final Button btn_find_all_room = this.findViewById(R.id.btn_find_all_room);
        if (Objects.equals(SiteData.Language, "en")){
            change_language.setBackgroundResource(R.drawable.btn_eng);
            btn_find_all_room.setBackgroundResource(R.drawable.findroom_eng);
        }else if(Objects.equals(SiteData.Language, "th")) {
            change_language.setBackgroundResource(R.drawable.btn_thai);
            btn_find_all_room.setBackgroundResource(R.drawable.findroom_th);
        }
        change_language.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                SiteData.playSound(Context, "touch");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_change_language", "Button"));
                //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_change_language", "Button"));
                if (Objects.equals(SiteData.Language, "en")){
                    change_language.setBackgroundResource(R.drawable.btn_thai);
                    btn_find_all_room.setBackgroundResource(R.drawable.findroom_th);
                    SiteData.Language = "th";
                }else if(Objects.equals(SiteData.Language, "th")){
                    change_language.setBackgroundResource(R.drawable.btn_eng);
                    btn_find_all_room.setBackgroundResource(R.drawable.findroom_eng);
                    SiteData.Language = "en";
                }
                updateTextLanguage(SiteData.Language);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("language", SiteData.Language);
                editor.apply();
            }
        });
    }

    private void updateTextLanguage(String language){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "updateTextLanguage") + " - " + TAG_MODIFIED.tagArgument("String", "language", language));
        //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "updateTextLanguage") + " - " + TAG_MODIFIED.tagArgument("String", "language", language));
        Context context = LocaleHelper.setLocale(MainActivity.this, language);
        Resources resources = context.getResources();

        //change_language.setText(resources.getString(R.string.lang));

        if (module_frame_cancel_now_fragment.isAdded()){
            module_frame_cancel_now_fragment.updateLanguage();
        }
        if (module_frame_use_now_fragment.isAdded()){
            module_frame_use_now_fragment.updateLanguage();
        }
        if (module_frame_extend_fragment.isAdded()){
            module_frame_extend_fragment.updateLanguage();
        }
        if (module_frame_end_now_fragment.isAdded()){
            module_frame_end_now_fragment.updateLanguage();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
        //Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "attachBaseContext"));
        //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "attachBaseContext"));
    }

    private void runThread(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "runThread"));
        //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "runThread"));
        time_thread = new Thread() {
            @SuppressLint("WakelockTimeout")
            @Override
            public void run() {
                try {
                    final boolean[] check_time = {false};
                    wakeLock.acquire();
                    while (!isInterrupted()) {
                        Thread.sleep(5000);
                        runOnUiThread(new Runnable() {
                            @SuppressLint("WakelockTimeout")
                            @Override
                            public void run() {
                                //Log.d(TAG, "SiteData.getTimeDeivceTimestamp() " + String.valueOf(SiteData.getTimeDeivceTimestamp()) + " | Main_Room.Start_time " + String.valueOf(Main_Room.Start_time) + " | Main_Room.End_time " + String.valueOf(Main_Room.End_time));
                                if (SiteData.getTimeDeivceTimestamp() < (Main_Room.Start_time) || SiteData.getTimeDeivceTimestamp() > Main_Room.End_time){
                                //if (!check_time[0] && SiteData.getTimeDeivceTimestamp() < (23*60+57) || SiteData.getTimeDeivceTimestamp() > (22*60+47)){
                                    if ((getFragmentManager().findFragmentById(R.id.fragment_main_activity_container_back) != null)) {
                                        Button find_room = findViewById(R.id.btn_find_all_room);
                                        find_room.setVisibility(View.INVISIBLE);
                                        updateUi(false);
                                    }
                                    if (wakeLock.isHeld()) {
                                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, 15000);
                                        wakeLock.release();
                                        Log.d(TAG, "wakeLock release");
                                    }
                                    if (!sleepLock.isHeld()){
                                        sleepLock.acquire();
                                        Log.d(TAG, "sleepLock acquire");
                                    }
                                } else if(SiteData.getTimeDeivceTimestamp() >= (Main_Room.Start_time) && SiteData.getTimeDeivceTimestamp() <= Main_Room.End_time){
                                    if (sleepLock.isHeld()){
                                        sleepLock.release();
                                        Log.d(TAG, "sleepLock release");
                                    }
                                    if (!wakeLock.isHeld()) {
                                        wakeLock.acquire();
                                        Log.d(TAG, "wakeLock acquire");
                                    }
                                    if((getFragmentManager().findFragmentById(R.id.fragment_main_activity_container_back) == null)) {
                                        Button find_room = findViewById(R.id.btn_find_all_room);
                                        find_room.setVisibility(View.VISIBLE);
                                        updateUi(true);
                                    }
                                }
                                if (SiteData.getTimeDeivceTimestamp() == timestamp_restart){
                                    try {
                                        sleep(30000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                        SiteData.writeFile(MainActivity.this, TAG + " | time_thread " + e.getMessage());
                                    }
                                    Intent intent = new Intent(Context, ConnectActivity.class);
                                    startActivity(intent);
//                                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
//                                    if (i != null) {
//                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    }
//                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(MainActivity.this, TAG + " | time_thread " + e.getMessage());
                }
            }
        };
        time_thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onStart"));
        SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onStart"));
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onResume"));
        SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onResume"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
        SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onPause"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onDestroy"));
        SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onDestroy"));
        SiteData.stopThread(time_thread);
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
        SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onRestart"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, TAG_MODIFIED.tagMethod("protected", "void", "onStop"));
        SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onStop"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
        //SiteData.writeFile(Context, TAG + " | " + TAG_MODIFIED.tagMethod("protected", "void", "onSaveInstanceState"));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);
        if((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getAction() == KeyEvent.KEYCODE_ENTER)){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            try {
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            } catch (NullPointerException e) {
                e.printStackTrace();
                SiteData.writeFile(MainActivity.this, TAG + " | dispatchTouchEvent " + e.getMessage());
                return false;
            }
        }
        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            assert w != null;
            try {
                w.getLocationOnScreen(scrcoords);
            } catch (NullPointerException e){
                e.printStackTrace();
                SiteData.writeFile(MainActivity.this, TAG + " | dispatchTouchEvent " + e.getMessage());
                return false;
            }
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
