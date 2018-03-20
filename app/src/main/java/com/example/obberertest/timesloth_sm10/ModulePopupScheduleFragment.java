package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModulePopupScheduleFragment extends Fragment {

    private String TAG = "ModulePopupScheduleFragment";
    private View ScheduleView;
    private MainActivity Main_activity;
    private String TextTitle;
    private String TextTime;
    private View View_main;
    private Thread Time_Thread;

    public ModulePopupScheduleFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ModulePopupScheduleFragment(MainActivity mainActivity, String time, String title, View view) {
        // Required empty public constructor
        Main_activity = mainActivity;
        TextTime = time;
        TextTitle = title;
        ScheduleView = view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View_main = inflater.inflate(R.layout.fragment_module_popup_schedule, container, false);
        initDetail();
        runThread();
        return View_main;
    }

    private void runThread() {
        Time_Thread = new Thread() {
            @Override
            public void run() {
                try {
                    final int[] count_time = {0};
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (count_time[0] == 10) {
                                    getFragmentManager().beginTransaction().remove(ModulePopupScheduleFragment.this).commit();
                                }
                                count_time[0]++;
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | Time_Thread " + e.getMessage());
                }
            }
        };
        Time_Thread.start();
    }

    private void initDetail() {
        TextView text_time = View_main.findViewById(R.id.text_time);
        TextView text_title = View_main.findViewById(R.id.text_title);

        text_time.setText(TextTime);
        text_title.setText(TextTitle);

        LinearLayout popupbox = View_main.findViewById(R.id.layout_popupbox);
        Log.d("lefttt", String.valueOf(ScheduleView.getX()));
        ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) popupbox.getLayoutParams();

        lpimgFooter.leftMargin = (int) (90 + ScheduleView.getX());
        //lpimgFooter.topMargin = ScheduleView.getTop();
        popupbox.setLayoutParams(lpimgFooter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SiteData.stopThread(Time_Thread);
    }
}
