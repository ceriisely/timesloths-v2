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


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ModuleFrameCancelNowFragment extends Fragment {

    static String TAG = "ModuleFrameCancelNowFragment";

    private MainActivity Main_activity;
    private View View_main;
    LinearLayout frame_occupied_cancelnow;

    @SuppressLint("ValidFragment")
    public ModuleFrameCancelNowFragment(MainActivity main_activity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleFrameCancelNowFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));

        Main_activity = main_activity;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleFrameCancelNowFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "main_activity", String.valueOf(main_activity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_frame_cancel_now, container, false);
        //frame_occupied_cancelnow = View_main.findViewById(R.id.frame_occupied_cancelnow);
        //frame_occupied_cancelnow.setVisibility(View.INVISIBLE);
        bindButton();
        return View_main;
    }

    private void bindButton(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button btn_cancelnow_cancel = View_main.findViewById(R.id.btn_occupied_cancelnow_cancel);
        btn_cancelnow_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "cancel");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_occupied_cancelnow_cancel", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_occupied_cancelnow_cancel", "Button"));
                getFragmentManager().beginTransaction().remove(Main_activity.module_frame_cancel_now_fragment).commit();
                //frame_occupied_cancelnow.setVisibility(View.INVISIBLE);
            }
        });

        Button btn_cancelnow_ok = View_main.findViewById(R.id.btn_occupied_cancelnow_ok);
        btn_cancelnow_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "ok");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_occupied_cancelnow_ok", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_occupied_cancelnow_ok", "Button"));
                Main_activity.manage_main_fragment.new FeedAsynTaskDeleteReservation().execute();
                //new FeedAsyncTaskCancelSchedule
            }
        });
    }

    void updateLanguage(){
        Button btn_occupied_cancelnow_ok = View_main.findViewById(R.id.btn_occupied_cancelnow_ok);
        btn_occupied_cancelnow_ok.setText(getResources().getString(R.string.ok));

        Button btn_occupied_cancelnow_cancel = View_main.findViewById(R.id.btn_occupied_cancelnow_cancel);
        btn_occupied_cancelnow_cancel.setText(getResources().getString(R.string.cancel));

        TextView text_cancel_now = View_main.findViewById(R.id.text_cancel_now);
        text_cancel_now.setText(getResources().getString(R.string.cancel_now));

        TextView text_cancel_now_detail = View_main.findViewById(R.id.text_cancel_now_detail);
        text_cancel_now_detail.setText(getResources().getString(R.string.cancel_now_detail));
    }
}
