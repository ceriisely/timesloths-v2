package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ModuleFrameUseNowFragment extends Fragment {

    static String TAG = "ModuleFrameUseNowFragment";

    private MainActivity Main_activity;
    private View View_main;
    LinearLayout frame_available_usenow;
    boolean LockPicker = false;
    String Start_time_usenow;
    String End_time_usenow;

    @SuppressLint("ValidFragment")
    public ModuleFrameUseNowFragment(MainActivity main_activity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleFrameUseNowFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));

        Main_activity = main_activity;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleFrameUseNowFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_frame_use_now, container, false);
        setupFrame();
        bindButton();
        return View_main;
    }

    private void bindButton(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button btn_available_usenow_cancel = View_main.findViewById(R.id.btn_available_usenow_cancel);
        btn_available_usenow_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "cancel");
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_available_usenow_cancel", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_available_usenow_cancel", "Button"));
                //hideFrame();
                EditText subject = View_main.findViewById(R.id.edittext_subject);
                subject.setText("");
                RelativeLayout time_preview = Main_activity.module_default_main_fragment.View_main.findViewById(R.id.time_schedule_preview);
                if (time_preview.getChildCount() > 0){
                    time_preview.removeAllViews();
                }
                Main_activity.getFragmentManager().beginTransaction().remove(Main_activity.module_frame_use_now_fragment).commit();
            }
        });

        Button btn_available_usenow_ok = View_main.findViewById(R.id.btn_available_usenow_ok);
        btn_available_usenow_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "ok");
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_available_usenow_ok", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_available_usenow_ok", "Button"));
                if (getTextSubject().equals("")){
                    ModuleAlertPopupFragment.Alert(getResources().getString(R.string.please_enter_subject));
                }else {
                    Main_activity.manage_main_fragment.new FeedAsynTaskAddReservation().execute();
                    //hideFrame();
                    EditText subject = View_main.findViewById(R.id.edittext_subject);
                    subject.setText("");
                    Main_activity.getFragmentManager().beginTransaction().remove(Main_activity.module_frame_use_now_fragment).commit();
                    RelativeLayout time_preview = Main_activity.module_default_main_fragment.View_main.findViewById(R.id.time_schedule_preview);
                    if (time_preview.getChildCount() > 0){
                        time_preview.removeAllViews();
                    }
                    //Main_activity.module_frame_alert_popup_fragment.Alert(getTextSubject());
                }
                //new FeedAsyncTaskUseNow
            }
        });
    }

    String getTextSubject(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "String", "getTextSubject"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "String", "getTextSubject"));
        EditText subject = View_main.findViewById(R.id.edittext_subject);
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "String", "getTextSubject") + " - " + TAG_MODIFIED.tagReturn("String", subject.getText().toString()));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "String", "getTextSubject") + " - " + TAG_MODIFIED.tagReturn("String", subject.getText().toString()));
        return subject.getText().toString();
    }

    void setupFrame(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "showFrame"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "showFrame"));
        /*EditText*/
        EditText subject = View_main.findViewById(R.id.edittext_subject);
        subject.setText("");
        /*RFID*/
        TextView text_organizer = View_main.findViewById(R.id.text_organizer);
        text_organizer.setText(Main_activity.manage_main_fragment.getNameUserByRFID());
        /*Time*/
        NumberPicker picker_start = View_main.findViewById(R.id.picker_start_time);
        NumberPicker picker_end = View_main.findViewById(R.id.picker_end_time);
        ArrayList<String> start_list = new ArrayList<String>();
        for (int i=0;i<Main_activity.Main_Room.Slots.size();i++){
            Schedule slot = Main_activity.Main_Room.Slots.get(i);
            if (slot.Status && slot.Free && SiteData.getTimeDeivceTimestamp() < slot.End_timestamp){
            //if (slot.Status && slot.Free && (8*60+45) < slot.End_timestamp){
                start_list.add(slot.Start_time.substring(0, 5));
            }
        }
        picker_start.setMinValue(0);
        if (start_list.size() > 0){
            picker_start.setMaxValue(start_list.size()-1);
        }else {
            picker_start.setMaxValue(0);
        }
        String[] start_time = new String[start_list.size()];
        for (int i=0;i<start_list.size();i++){
            start_time[i] = start_list.get(i);
        }
        picker_start.setDisplayedValues(start_time);
        changeEndTimePicker(start_time[0]);
        bindNumberPickerStart(start_time);
    }

    @SuppressLint("LongLogTag")
    void changeEndTimePicker(String start_time) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "changeEndTimePicker") + " - " + TAG_MODIFIED.tagArgument("String", "start_time", start_time));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "changeEndTimePicker") + " - " + TAG_MODIFIED.tagArgument("String", "start_time", start_time));
        NumberPicker picker_end = View_main.findViewById(R.id.picker_end_time);
        String[] end_time = new String[0];
        try {
            ArrayList<String> end_list = new ArrayList<String>();
            boolean found_time = false;
            //Log.d("Main_activity.Main_Room.Slots.size()", String.valueOf(Main_activity.Main_Room.Slots.size()));
            for (int i = 0; i < Main_activity.Main_Room.Slots.size(); i++) {
                Schedule slot = Main_activity.Main_Room.Slots.get(i);
                if (Objects.equals(start_time, slot.Start_time.substring(0, 5))) {
                    found_time = true;
                }
                if (found_time) {
                    if (slot.Status && slot.Free) {
                        end_list.add(slot.End_time.substring(0, 5));
                    } else {
                        break;
                    }
                }
            }
            picker_end.setMinValue(0);
            picker_end.setMaxValue(0);
            end_time = new String[end_list.size()];
            for (int i = 0; i < end_list.size(); i++) {
                end_time[i] = end_list.get(i);
            }
            picker_end.setDisplayedValues(end_time);
            if (end_list.size() > 0) {
                picker_end.setMaxValue(end_list.size() - 1);
            } else {
                picker_end.setMaxValue(0);
            }
        } catch (NumberFormatException e) {
            //Log.e(TAG, e.getMessage(), e);
            SiteData.writeFile(Main_activity, TAG + " | changeEndTimePicker " + e.getMessage());
        }
        //Log.d(String.valueOf(Main_activity.Site_data.getTimestamp(start_time)), String.valueOf(Main_activity.Site_data.getTimestamp(end_time[0])));

        Main_activity.module_default_main_fragment.previewScheduleTime(SiteData.getTimestamp(start_time), SiteData.getTimestamp(end_time[0]));
        Start_time_usenow = start_time;
        End_time_usenow = end_time[0];
        bindNumberPickerEnd(start_time, end_time);
    }

    void bindNumberPickerStart(final String[] start_time){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "bindNumberPickerStart") + " - " + TAG_MODIFIED.tagArgument("String[]", "start_time", Arrays.toString(start_time)));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "bindNumberPickerStart") + " - " + TAG_MODIFIED.tagArgument("String[]", "start_time", Arrays.toString(start_time)));
        NumberPicker picker_start = View_main.findViewById(R.id.picker_start_time);
        picker_start.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                changeEndTimePicker(start_time[newVal]);
                Start_time_usenow = start_time[newVal];
            }
        });
    }

    void bindNumberPickerEnd(final String start_time, final String[] end_time){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "bindNumberPickerEnd") + " - " + TAG_MODIFIED.tagArgument("String", "start_time", start_time) + " - " + TAG_MODIFIED.tagArgument("String[]", "end_time", Arrays.toString(end_time)));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "bindNumberPickerEnd") + " - " + TAG_MODIFIED.tagArgument("String", "start_time", start_time) + " - " + TAG_MODIFIED.tagArgument("String[]", "end_time", Arrays.toString(end_time)));
        NumberPicker picker_end = View_main.findViewById(R.id.picker_end_time);
        picker_end.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Main_activity.module_default_main_fragment.previewScheduleTime(SiteData.getTimestamp(start_time), SiteData.getTimestamp(end_time[newVal]));
                Start_time_usenow = start_time;
                End_time_usenow = end_time[newVal];
            }
        });
    }

    void updateLanguage(){
        TextView text_start_time = View_main.findViewById(R.id.text_start_time);
        text_start_time.setText(getResources().getString(R.string.start_time));

        TextView text_end_time = View_main.findViewById(R.id.text_end_time);
        text_end_time.setText(getResources().getString(R.string.end_time));

        TextView text_subject = View_main.findViewById(R.id.text_subject);
        text_subject.setText(getResources().getString(R.string.subject));

        EditText edittext_subject = View_main.findViewById(R.id.edittext_subject);
        edittext_subject.setHint(getResources().getString(R.string.subject));

        TextView text_title_organizer = View_main.findViewById(R.id.text_title_organizer);
        text_title_organizer.setText(getResources().getString(R.string.organizer));

        Button btn_available_usenow_ok = View_main.findViewById(R.id.btn_available_usenow_ok);
        btn_available_usenow_ok.setText(getResources().getString(R.string.ok));

        Button btn_available_usenow_cancel = View_main.findViewById(R.id.btn_available_usenow_cancel);
        btn_available_usenow_cancel.setText(getResources().getString(R.string.cancel));

        TextView text_usenow = View_main.findViewById(R.id.text_usenow);
        text_usenow.setText(getResources().getString(R.string.use_now));
    }
}
