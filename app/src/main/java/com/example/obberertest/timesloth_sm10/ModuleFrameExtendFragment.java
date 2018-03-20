package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ModuleFrameExtendFragment extends Fragment {

    static String TAG = "ModuleFrameExtendFragment";

    private MainActivity Main_activity;
    private View View_main;
    LinearLayout frame_busy_extend;
    String End_time_extend;

    @SuppressLint("ValidFragment")
    public ModuleFrameExtendFragment(MainActivity main_activity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleFrameExtendFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));

        Main_activity = main_activity;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleFrameExtendFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_frame_extend, container, false);
//        frame_busy_extend = View_main.findViewById(R.id.frame_busy_extend);
//        frame_busy_extend.setVisibility(View.INVISIBLE);
        bindButton();
        setupFrame();
        return View_main;
    }

    private void bindButton(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button btn_busy_extend_cancel = View_main.findViewById(R.id.btn_busy_extend_cancel);
        btn_busy_extend_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "cancel");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_busy_extend_cancel", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_busy_extend_cancel", "Button"));
                Main_activity.module_default_main_fragment.hidepreviewScheduleTime();
                getFragmentManager().beginTransaction().remove(Main_activity.module_frame_extend_fragment).commit();
                //frame_busy_extend.setVisibility(View.INVISIBLE);

            }
        });

        Button btn_busy_extend_ok = View_main.findViewById(R.id.btn_busy_extend_ok);
        btn_busy_extend_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "ok");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_busy_extend_ok", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_busy_extend_ok", "Button"));
                Main_activity.manage_main_fragment.new FeedAsynTaskUpdateReservation().execute();
                //new FeedAsyncTaskExtend
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void setupFrame(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "showFrame"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "showFrame"));
        int index = Main_activity.Main_Room.getIndexScheduleFromIdStatus();
        Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(index);
        //Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(1);
        if((index + 1) < Main_activity.Main_Room.RoomSchedule.size()){
            Schedule _schedule = Main_activity.Main_Room.RoomSchedule.get(index+1);
            if (schedule.End_timestamp == _schedule.Start_timestamp){
                Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cannot_extend));
                getFragmentManager().beginTransaction().remove(Main_activity.module_frame_extend_fragment).commit();
                return;
            }
        }
        if (schedule.End_timestamp == Main_activity.Main_Room.End_time) {
            Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cannot_extend_endday));
            getFragmentManager().beginTransaction().remove(Main_activity.module_frame_extend_fragment).commit();
            return;
        }
        ArrayList<Integer> Start_slot = new ArrayList<Integer>();
        boolean found_slot = false;
        for (int i=0;i<Main_activity.Main_Room.Slots.size();i++){
            Schedule slot = Main_activity.Main_Room.Slots.get(i);
            if (schedule != null) {
                if (slot.Status && slot.Free && slot.Start_timestamp >= schedule.End_timestamp) {
                    Start_slot.add(i);
                    found_slot = true;
                    //Log.d("Slot Free", slot.Start_time + " - " + slot.End_time);
                }else if (found_slot){
                    break;
                }
            }
        }
        NumberPicker picker_extend_time = View_main.findViewById(R.id.picker_extend_time);
        picker_extend_time.setMinValue(0);
        picker_extend_time.setMaxValue(0);
        String[] time;
        int[] timestamp;
        if (Start_slot.size() > 0) {
            time = new String[Start_slot.size()];
            timestamp = new int[Start_slot.size()];
            for (int i = 0; i < Start_slot.size(); i++) {
                Schedule slot = Main_activity.Main_Room.Slots.get(Start_slot.get(i));
                time[i] = slot.End_time.substring(0, 5);
                timestamp[i] = slot.End_timestamp;
            }
        }else {
            time = new String[1];
            timestamp = new int[1];
            time[0] = schedule.End_time.substring(0, 5);
            timestamp[0] = schedule.End_timestamp;
        }
        picker_extend_time.setValue(0);
        picker_extend_time.setDisplayedValues(time);
        if (Start_slot.size() == 0){
            picker_extend_time.setMaxValue(0);
        }else {
            picker_extend_time.setMaxValue(Start_slot.size()-1);
        }
        bindNumberPicker(schedule.End_timestamp, timestamp, time);
        End_time_extend = time[0];
        //frame_busy_extend.setVisibility(View.VISIBLE);
        Main_activity.module_default_main_fragment.previewScheduleTime(schedule.End_timestamp, timestamp[0]);
        TextView text = View_main.findViewById(R.id.text_extend_time);
        text.setText(getResources().getString(R.string.extending) + " " + Main_activity.Site_data.getTimeRemain(Main_activity, schedule.End_timestamp, timestamp[0]));
        //text.setText("Extending " + Main_activity.Site_data.getTimeRemain(Main_activity, schedule.End_timestamp, timestamp[0]));

    }

    void bindNumberPicker(final int start_time, final int[] time, final String[] time_strings){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "bindNumberPicker") + " - " + TAG_MODIFIED.tagArgument("int", "start_time", String.valueOf(start_time)) + " - " + TAG_MODIFIED.tagArgument("int[]", "time", Arrays.toString(time)) + " - " + TAG_MODIFIED.tagArgument("String[]", "time_strings", Arrays.toString(time_strings)));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "bindNumberPicker") + " - " + TAG_MODIFIED.tagArgument("int", "start_time", String.valueOf(start_time)) + " - " + TAG_MODIFIED.tagArgument("int[]", "time", Arrays.toString(time)) + " - " + TAG_MODIFIED.tagArgument("String[]", "time_strings", Arrays.toString(time_strings)));
        NumberPicker time_extend = View_main.findViewById(R.id.picker_extend_time);
        final TextView text = View_main.findViewById(R.id.text_extend_time);
        time_extend.setOnLongPressUpdateInterval(5000);
        time_extend.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Main_activity.module_default_main_fragment.previewScheduleTime(start_time, time[newVal]);
                String remain = Main_activity.Site_data.getTimeRemain(Main_activity, start_time, time[newVal]);
                //Log.d("time[newVal]", time_strings[newVal]);
                End_time_extend = time_strings[newVal];
                //String End_time_extend = time[newVal];
                if (remain.equals("")){
                    //text.setText(getResources().getString(R.string.can));
                    text.setText("Can not Extending.");
                }else {
                    text.setText(getResources().getString(R.string.extending) + " " + remain);
                    //text.setText("Extending " + remain);
                }
            }


        });
    }

    void updateLanguage(){
        TextView text_title_extend = View_main.findViewById(R.id.text_title_extend);
        text_title_extend.setText(getResources().getString(R.string.extend_head));

        TextView text_new_ending = View_main.findViewById(R.id.text_new_ending);
        text_new_ending.setText(getResources().getString(R.string.new_ending));

        Button btn_busy_extend_ok = View_main.findViewById(R.id.btn_busy_extend_ok);
        btn_busy_extend_ok.setText(getResources().getString(R.string.ok));

        Button btn_busy_extend_cancel = View_main.findViewById(R.id.btn_busy_extend_cancel);
        btn_busy_extend_cancel.setText(getResources().getString(R.string.cancel));

        TextView text = View_main.findViewById(R.id.text_extend_time);
        String string = text.getText().toString();
        string = string.replace("Extending", getResources().getString(R.string.extending));
        string = string.replace("เพิ่มเวลา", getResources().getString(R.string.extending));
        string = string.replace("Minutes", getResources().getString(R.string.minutes));
        string = string.replace("นาที", getResources().getString(R.string.minutes));
        string = string.replace("Hr", getResources().getString(R.string.hr));
        string = string.replace("ชม.", getResources().getString(R.string.hr));
        text.setText(string);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStart"));

//        LinearLayout frame_busy_extend = View_main.findViewById(R.id.frame_busy_extend);
//        frame_busy_extend.setFocusable(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onResume"));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onPause"));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStop"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onDestroy"));

//        LinearLayout frame_busy_extend = View_main.findViewById(R.id.frame_busy_extend);
//        frame_busy_extend.setFocusable(false);
    }
}
