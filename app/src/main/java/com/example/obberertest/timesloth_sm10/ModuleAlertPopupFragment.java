package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.os.SystemClock.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleAlertPopupFragment extends Fragment {

    String TAG = "ModuleAlertPopupFragment";

    static private MainActivity Main_activity;
    private static View View_main;
    static RelativeLayout popup_box;
    static private Thread time_thread;

    public ModuleAlertPopupFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleAlertPopupFragment(MainActivity main_activity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
        Main_activity = main_activity;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleAlertPopupFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_alert_popup, container, false);
        popup_box = View_main.findViewById(R.id.popup_box);
        popup_box.setVisibility(View.INVISIBLE);
        return View_main;
    }

    static void runThread() {
        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    final int[] count = {0};
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count[0]++;
                                if (count[0] >= 3) {
                                    popup_box.setVisibility(View.INVISIBLE);
                                    SiteData.stopThread(time_thread);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Connection_fail++;
                }
            }
        };
        time_thread.start();
    }

    static void runThread(final MainActivity context) {
        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    final int[] count = {0};
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count[0]++;
                                if (count[0] >= 3) {
                                    popup_box.setVisibility(View.INVISIBLE);
                                    SiteData.stopThread(time_thread);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Connection_fail++;
                }
            }
        };
        time_thread.start();
    }

    static void runThread(final ConnectActivity context) {
        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    final int[] count = {0};
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count[0]++;
                                if (count[0] >= 3) {
                                    popup_box.setVisibility(View.INVISIBLE);
                                    SiteData.stopThread(time_thread);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Connection_fail++;
                }
            }
        };
        time_thread.start();
    }

    static void runThread(final NetworkSettingActivity context) {
        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    final int[] count = {0};
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count[0]++;
                                if (count[0] >= 3) {
                                    popup_box.setVisibility(View.INVISIBLE);
                                    SiteData.stopThread(time_thread);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Connection_fail++;
                }
            }
        };
        time_thread.start();
    }

    static void runThread(final SettingActivity context) {
        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    final int[] count = {0};
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count[0]++;
                                if (count[0] >= 3) {
                                    popup_box.setVisibility(View.INVISIBLE);
                                    SiteData.stopThread(time_thread);
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Connection_fail++;
                }
            }
        };
        time_thread.start();
    }

    static void Alert(String type){
        SiteData.playSound(View_main.getContext(), "popup");
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        popup_box.setVisibility(View.VISIBLE);
        TextView text = View_main.findViewById(R.id.popup_text);
        switch (type){
            case "RFID_Error": {
                text.setText("This card has not been registered to the Time Sloths system. Please contact IT department");
                break;
            }
            case "usenow_text_subject": {
                text.setText("Please enter subject");
                break;
            }
            default: {
                text.setText(type);
                break;
            }
        }
        runThread();
    }

    static void Alert(MainActivity context, String type){
        SiteData.playSound(View_main.getContext(), "popup");
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        popup_box.setVisibility(View.VISIBLE);
        TextView text = View_main.findViewById(R.id.popup_text);
        switch (type){
            case "RFID_Error": {
                text.setText("This card has not been registered to the Time Sloths system. Please contact IT department");
                break;
            }
            case "usenow_text_subject": {
                text.setText("Please enter subject");
                break;
            }
            default: {
                text.setText(type);
                break;
            }
        }
        runThread(context);
    }

    static void Alert(ConnectActivity context, String type){
        SiteData.playSound(View_main.getContext(), "popup");
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        popup_box.setVisibility(View.VISIBLE);
        TextView text = View_main.findViewById(R.id.popup_text);
        switch (type){
            case "RFID_Error": {
                text.setText("This card has not been registered to the Time Sloths system. Please contact IT department");
                break;
            }
            case "usenow_text_subject": {
                text.setText("Please enter subject");
                break;
            }
            default: {
                text.setText(type);
                break;
            }
        }
        runThread(context);
    }

    static void Alert(NetworkSettingActivity context, String type){
        SiteData.playSound(View_main.getContext(), "popup");
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        popup_box.setVisibility(View.VISIBLE);
        TextView text = View_main.findViewById(R.id.popup_text);
        switch (type){
            case "RFID_Error": {
                text.setText("This card has not been registered to the Time Sloths system. Please contact IT department");
                break;
            }
            case "usenow_text_subject": {
                text.setText("Please enter subject");
                break;
            }
            default: {
                text.setText(type);
                break;
            }
        }
        runThread(context);
    }

    static void Alert(SettingActivity context, String type){
        SiteData.playSound(View_main.getContext(), "popup");
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "Alert") + " - " + TAG_MODIFIED.tagArgument("String", "type", type));
        popup_box.setVisibility(View.VISIBLE);
        TextView text = View_main.findViewById(R.id.popup_text);
        switch (type){
            case "RFID_Error": {
                text.setText("This card has not been registered to the Time Sloths system. Please contact IT department");
                break;
            }
            case "usenow_text_subject": {
                text.setText("Please enter subject");
                break;
            }
            default: {
                text.setText(type);
                break;
            }
        }
        runThread(context);
    }
}
