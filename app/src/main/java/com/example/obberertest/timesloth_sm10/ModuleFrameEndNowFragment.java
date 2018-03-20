package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ModuleFrameEndNowFragment extends Fragment {

    static String TAG = "ModuleFrameEndNowFragment";

    private MainActivity Main_activity;
    private View View_main;
    LinearLayout frame_busy_endnow;

    @SuppressLint("ValidFragment")
    public ModuleFrameEndNowFragment(MainActivity main_activity) {
        // Required empty public constructor
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleFrameEndNowFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));

        Main_activity = main_activity;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleFrameEndNowFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_frame_end_now, container, false);
//        frame_busy_endnow = View_main.findViewById(R.id.frame_busy_endnow);
//        frame_busy_endnow.setVisibility(View.INVISIBLE);
        bindButton();
        return View_main;
    }

    private void bindButton(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button btn_busy_endnow_cancel = View_main.findViewById(R.id.btn_busy_endnow_cancel);
        btn_busy_endnow_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "cancel");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_busy_endnow_cancel", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_busy_endnow_cancel", "Button"));
                Main_activity.getFragmentManager().beginTransaction().remove(Main_activity.module_frame_end_now_fragment).commit();
                //frame_busy_endnow.setVisibility(View.INVISIBLE);
            }
        });

        Button btn_busy_endnow_ok = View_main.findViewById(R.id.btn_busy_endnow_ok);
        btn_busy_endnow_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "ok");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_busy_endnow_ok", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_busy_endnow_ok", "Button"));
                Main_activity.manage_main_fragment.new FeedAsynTaskUpdateReservation().execute();
                //new FeedAsyncTaskExtend (endnow)
            }
        });
    }

    void updateLanguage(){
        TextView text_title_end_now = View_main.findViewById(R.id.text_title_end_now);
        text_title_end_now.setText(getResources().getString(R.string.end_now));

        TextView text_end_now_detail = View_main.findViewById(R.id.text_end_now_detail);
        text_end_now_detail.setText(getResources().getString(R.string.end_now_detail));

        Button btn_busy_endnow_ok = View_main.findViewById(R.id.btn_busy_endnow_ok);
        btn_busy_endnow_ok.setText(getResources().getString(R.string.ok));

        Button btn_busy_endnow_cancel = View_main.findViewById(R.id.btn_busy_endnow_cancel);
        btn_busy_endnow_cancel.setText(getResources().getString(R.string.cancel));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStart"));
//
//        LinearLayout frame_busy_endnow = View_main.findViewById(R.id.frame_busy_endnow);
//        frame_busy_endnow.setFocusable(true);
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

//        LinearLayout frame_busy_endnow = View_main.findViewById(R.id.frame_busy_endnow);
//        frame_busy_endnow.setFocusable(false);
    }
}
