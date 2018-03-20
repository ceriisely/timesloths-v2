package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleSettingFragment extends Fragment {

    static String TAG = "ModuleSettingFragment";

    private MainActivity Main_activity;
    private View View_main;
    RelativeLayout frame_setting;

    public ModuleSettingFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleSettingFragment"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleSettingFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleSettingFragment(MainActivity main_activity) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleSettingFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
        Main_activity = main_activity;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleSettingFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_setting, container, false);
        setupFrame();
        bindView();

        Spinner spinner = View_main.findViewById(R.id.spinner_refresh_rate);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(Main_activity, R.array.refresh_rate_arrays, R.layout.spinner_refresh_rate);
        spinner.setAdapter(adapter);
        spinner.setSelection((Main_activity.Site_data.TimeRefresh / 10000)-1);

//        frame_setting = View_main.findViewById(R.id.frame_setting);
//        frame_setting.setVisibility(View.INVISIBLE);

        return View_main;
    }

    private void bindView(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        Button btn_reboot = View_main.findViewById(R.id.btn_reboot);
        btn_reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "ok");
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_reboot", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_reboot", "Button"));
                Main_activity.getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_front, Main_activity.module_reboot_fragment).commit();
            }
        });

        Button back = View_main.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "ok");
                //Main_activity.updateUi(Main_activity.Main_Room.Status);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Main_activity.getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("time_refresh", Main_activity.Site_data.TimeRefresh);
                editor.putString("unit_time", Main_activity.Site_data.UnitTime);
                editor.apply();
                Main_activity.getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_front, Main_activity.manage_main_fragment, "TAG").commit();
                if(Main_activity.module_default_main_fragment.isAdded()) {
                    Main_activity.module_default_main_fragment.setupRoom();
                }
                getFragmentManager().beginTransaction().remove(ModuleSettingFragment.this).commit();
                //frame_setting.setVisibility(View.INVISIBLE);
            }
        });

        final Button format_24 = View_main.findViewById(R.id.btn_timeformat_24);
        final Button format_12 = View_main.findViewById(R.id.btn_timeformat_12);
        format_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_timeformat_24", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_timeformat_24", "Button"));
                Main_activity.Site_data.UnitTime = "24";
                format_24.setBackgroundResource(R.color.Blue);
                format_12.setBackgroundResource(R.color.Default_btn);
            }
        });
        format_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_timeformat_12", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_timeformat_12", "Button"));
                Main_activity.Site_data.UnitTime = "12";
                format_24.setBackgroundResource(R.color.Default_btn);
                format_12.setBackgroundResource(R.color.Blue);
            }
        });

        Spinner spinner = View_main.findViewById(R.id.spinner_refresh_rate);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("spinner_refresh_rate", "Spinner"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("spinner_refresh_rate", "Spinner"));
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("spinner_refresh_rate", "Spinner") + " - " + "position " + String.valueOf(position)); //position (since 0)
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("spinner_refresh_rate", "Spinner") + " - " + "position " + String.valueOf(position));
                Main_activity.Site_data.TimeRefresh = (position + 1) * 10000;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @SuppressLint("ResourceType")
    void setupFrame(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "showFrame"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "showFrame"));
        Button format_24 = View_main.findViewById(R.id.btn_timeformat_24);
        Button format_12 = View_main.findViewById(R.id.btn_timeformat_12);
        if (Objects.equals(Main_activity.Site_data.UnitTime, "24")){
            format_24.setBackgroundResource(R.color.Blue);
            format_12.setBackgroundResource(R.color.Default_btn);
        }else {
            format_24.setBackgroundResource(R.color.Default_btn);
            format_12.setBackgroundResource(R.color.Blue);
        }
    }

}
