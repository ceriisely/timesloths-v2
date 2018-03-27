package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ModuleFindAllRoomFragment extends Fragment {

    static String TAG = "ModuleFindAllRoomFragment";

    private ModuleFindAllRoomFragment Context;
    private MainActivity Main_activity;
    private View View_main;
    private FragmentTransaction fragmentTransaction;
    private Thread time_thread;
    private static int time_count;

    public ModuleFindAllRoomFragment(MainActivity context) {
        // Required empty public constructor
        //.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleFindAllRoomFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));
        Main_activity = context;
        Context = this;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleFindAllRoomFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_find_all_room, container, false);

        bindButton();

        changeTimeDate();

        ImageView background_room = View_main.findViewById(R.id.image_room);
        Main_activity.module_default_main_fragment.changeImageViewFromUrl(Main_activity.Main_Room.Background_image_path, background_room, Main_activity.getApplicationContext());

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        LinearLayout main = View_main.findViewById(R.id.fragment_find_all_room);
        LinearLayout temp = null;

        for (int i=0;i<Main_activity.All_Room.size();i++){
            if (i % 6 == 0){
                temp = new LinearLayout(Main_activity);
                temp.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                temp.setLayoutParams(params);
                temp.setId(findId());
                main.addView(temp);
                ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) temp.getLayoutParams();
                lpimgFooter.rightMargin = 20;
                temp.setLayoutParams(lpimgFooter);
            }
            fragmentTransaction.add(temp.getId(), new ModuleFindRoomDetailFragment(Main_activity, Main_activity.All_Room.get(i)), "TAG");
        }
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            fragmentTransaction.commitAllowingStateLoss();
            SiteData.writeFile(Main_activity, TAG + " | onCreateView " + e.getMessage());
        }
        time_count = 0;
        runThread();
        ModuleLoaderFragment.hideLoader();
        return View_main;
    }

    private void runThread(){
        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                time_count++;
                                if (time_count >= 15) {
                                    Button btn_back = View_main.findViewById(R.id.btn_back);
                                    btn_back.performClick();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | time_thread " + e.getMessage());
                }
            }
        };
        time_thread.start();
    }

    private void changeTimeDate() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "changeTimeDate"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "changeTimeDate"));
        TextView text_time = View_main.findViewById(R.id.text_time);
        text_time.setText(Main_activity.Site_data.getTimeString(SiteData.getTimeDeivceTimestamp()));

        TextView text_date = View_main.findViewById(R.id.text_date);
        text_date.setText(SiteData.getDateDeviceTimestamp("dd mmm yyyy"));
    }

    private void bindButton() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button btn_back = View_main.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "cancel");
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_back", "Button"));
                //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_back", "Button"));
                try {
                    Main_activity.getFragmentManager().beginTransaction().remove(ModuleFindAllRoomFragment.this).commit();
                } catch (IllegalStateException e) {
                    SiteData.writeFile(Main_activity, TAG + " | btn_back " + e.getMessage());
                    Main_activity.getFragmentManager().beginTransaction().remove(ModuleFindAllRoomFragment.this).commitAllowingStateLoss();
                }
            }
        });
    }

    int findId(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "int", "findId"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "int", "findId"));
        int id = 1;
        View v = View_main.findViewById(id);
        while (v != null){
            v = View_main.findViewById(++id);
        }
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "int", "findId") + " - " + TAG_MODIFIED.tagReturn("int", String.valueOf(id)));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "int", "findId") + " - " + TAG_MODIFIED.tagReturn("int", String.valueOf(id)));
        return id;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStart"));
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
        SiteData.stopThread(time_thread);
    }
}
