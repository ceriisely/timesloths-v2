package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleDefaultMainFragment extends Fragment {

    static String TAG = "ModuleDefaultMainFragment";

    private MainActivity Main_activity;
    private ModuleDefaultMainFragment Context;
    View View_main = null;
    private Thread time_thread;
    private ArrayList<Integer> LeftMarginSlot = new ArrayList<Integer>();

    public ModuleDefaultMainFragment(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleDefaultMainFragment"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleDefaultMainFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleDefaultMainFragment(MainActivity context) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleDefaultMainFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));

        Context = this;
        Main_activity = context;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleDefaultMainFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_default_main, container, false);
        setupRoom();
        bindButton();

        ImageView splash = View_main.findViewById(R.id.myLayout);
        Animation animation = AnimationUtils.loadAnimation(Main_activity.getApplicationContext(), R.anim.move);
        splash.startAnimation(animation);
        return View_main;
    }

    @SuppressLint("ResourceAsColor")
    void setupRoom(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "setupRoom"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "setupRoom"));
        /*Room name*/
        TextView roomname = View_main.findViewById(R.id.text_room_name);
        roomname.setText(Main_activity.Main_Room.Name);
        /*Room Image Background*/
        ImageView background_room = View_main.findViewById(R.id.image_room);
        String myfileurl = Main_activity.Main_Room.Background_image_path;
        changeImageViewFromUrl(myfileurl, background_room, Main_activity.getApplicationContext());
        //Glide.with(Main_activity.getApplicationContext()).load(myfileurl).into(background_room);
        /*Schedule Bar*/
        ArrayList<Integer> count_timestamp = new ArrayList<Integer>();
        for (int i=0;i<Main_activity.Main_Room.Slots.size();i++){
            if(Main_activity.Main_Room.Slots.get(i).Status){
                if(count_timestamp.size() == 0 && Main_activity.Main_Room.Slots.get(i).Start_timestamp % 60 == 0){
                    count_timestamp.add(Main_activity.Main_Room.Slots.get(i).Start_timestamp);
                }
                else if(Main_activity.Main_Room.Slots.get(i).End_timestamp % 60 == 0) {
                    count_timestamp.add(Main_activity.Main_Room.Slots.get(i).End_timestamp);
                }
            }
        }
        /*Time Bar*/
        RelativeLayout time_separate = View_main.findViewById(R.id.time_separate);
        if (time_separate.getChildCount() > 0){
            time_separate.removeAllViews();
        }
        RelativeLayout time_bar = View_main.findViewById(R.id.time_bar);
        time_bar.measure(0 ,0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT);
        for(int i=0;i<count_timestamp.size();i++){
            ImageView separate = new ImageView(Main_activity, null, R.style.TimeSeparate);
            separate.setBackgroundResource(R.color.White);
            separate.setLayoutParams(params);

            time_separate.addView(separate);

            ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) separate.getLayoutParams();
            int size_count_timestamp = count_timestamp.size() - 1;
            if (size_count_timestamp < 1){
                size_count_timestamp = 1;
            }
            LeftMarginSlot.add(((time_bar.getMeasuredWidth() / size_count_timestamp) * i) + i);
            lpimgFooter.leftMargin = ((time_bar.getMeasuredWidth() / size_count_timestamp) * i) + i;
            separate.setLayoutParams(lpimgFooter);
        }
        /*Time Clock Bar*/
        RelativeLayout clock_bar = View_main.findViewById(R.id.time_clock_value);
        if (clock_bar.getChildCount() > 0){
            clock_bar.removeAllViews();
        }
        LinearLayout.LayoutParams clock_bar_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i=0;i<count_timestamp.size();i++){
            TextView clock_value = new TextView(Main_activity);
            clock_value.setText(Main_activity.Site_data.getTimeString(count_timestamp.get(i) / 60, "hr"));
            clock_value.setTextColor(Color.parseColor("#FFFFFF"));
            clock_value.setTextSize(15);
            clock_value.setLayoutParams(clock_bar_params);

            clock_bar.addView(clock_value);

            ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) clock_value.getLayoutParams();
            int size_count_timestamp = count_timestamp.size() - 1;
            if (size_count_timestamp < 1){
                size_count_timestamp = 1;
            }
            lpimgFooter.leftMargin = (time_bar.getMeasuredWidth() / size_count_timestamp) * i;
            clock_value.setLayoutParams(lpimgFooter);
            Log.d(String.valueOf((time_bar.getMeasuredWidth() / size_count_timestamp) * i), String.valueOf(count_timestamp.get(i) / 60));
        }
        /*Arrow Clock*/
        updateTime(count_timestamp);
    }

    void updateTime(final ArrayList<Integer> count_timestamp){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateTime") + " - " + TAG_MODIFIED.tagArgument("ArrayList<Integer>", "count_timestamp", ""));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateTime") + " - " + TAG_MODIFIED.tagArgument("ArrayList<Integer>", "count_timestamp", ""));
        time_thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Main_activity.module_default_main_fragment.isAdded() && SiteData.getTimeDeivceTimestamp() >= Main_activity.Main_Room.Start_time && SiteData.getTimeDeivceTimestamp() < Main_activity.Main_Room.End_time) {
                                    updateTimeArrow(count_timestamp);
                                    updateTimeText(count_timestamp);
                                    updateScheduleBar();
                                    updateUIScheduleValid();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    SiteData.writeFile(Main_activity, TAG + " | time_thread " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };

        time_thread.start();
    }

    void updateTimeText(ArrayList<Integer> timestamp){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateTimeText") + " - " + TAG_MODIFIED.tagArgument("ArrayList<Integer>", "timestamp", ""));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateTimeText") + " - " + TAG_MODIFIED.tagArgument("ArrayList<Integer>", "timestamp", ""));
        RelativeLayout time_bar = View_main.findViewById(R.id.time_bar);
        time_bar.measure(0 ,0);
        int end_hr = timestamp.get(timestamp.size()-1) / 60;
        int end_min = timestamp.get(timestamp.size()-1) % 60;
        int start_hr = timestamp.get(0) / 60;
        int start_min = timestamp.get(0) % 60;
        int clock_timestamp;
        if(SiteData.getTimeDeivceTimestamp() < timestamp.get(timestamp.size()-1)){
            clock_timestamp = ((end_hr - (SiteData.getTimeDeivceTimestamp() / 60) - 1) * 60) + (60 - (SiteData.getTimeDeivceTimestamp() % 60)) + end_min;
        }else {
            clock_timestamp = ((-1) * 60) + (60 - end_min) + end_min;
        }
        int all_minute = ((end_hr - start_hr - 1) * 60) + (60 - start_min) + end_min;
        if (all_minute < 1) {all_minute = 1;}
        int leftMargin = (int) ((all_minute - clock_timestamp) *  time_bar.getMeasuredWidth()) / all_minute;

        TextView text_time = View_main.findViewById(R.id.time_clock_text);
        text_time.setText(Main_activity.Site_data.getTimeString(SiteData.getTimeDeivceTimestamp()));

        ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) text_time.getLayoutParams();
        
        lpimgFooter.leftMargin = leftMargin;
        text_time.setLayoutParams(lpimgFooter);
    }

    void updateTimeArrow(ArrayList<Integer> timestamp){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateTimeArrow") + " - " + TAG_MODIFIED.tagArgument("ArrayList<Integer>", "timestamp", ""));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateTimeArrow") + " - " + TAG_MODIFIED.tagArgument("ArrayList<Integer>", "timestamp", ""));
        RelativeLayout time_bar = View_main.findViewById(R.id.time_bar);
        time_bar.measure(0 ,0);
        int end_hr = timestamp.get(timestamp.size()-1) / 60;
        int end_min = timestamp.get(timestamp.size()-1) % 60;
        int start_hr = timestamp.get(0) / 60;
        int start_min = timestamp.get(0) % 60;
        int clock_timestamp;
        if(SiteData.getTimeDeivceTimestamp() < timestamp.get(timestamp.size()-1)){
            clock_timestamp = ((end_hr - (SiteData.getTimeDeivceTimestamp() / 60) - 1) * 60) + (60 - (SiteData.getTimeDeivceTimestamp() % 60)) + end_min;
        }else {
            clock_timestamp = ((end_hr - (end_hr) - 1) * 60) + (60 - end_min) + end_min;
        }
        int all_minute = ((end_hr - start_hr - 1) * 60) + (60 - start_min) + end_min;

        if (all_minute < 1) {all_minute  = 1;}
        int leftMargin = (int) ((all_minute - clock_timestamp) *  time_bar.getMeasuredWidth()) / all_minute;
        ImageView arrow = View_main.findViewById(R.id.time_arrow_img);

        ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) arrow.getLayoutParams();

        lpimgFooter.leftMargin = leftMargin;
        arrow.setLayoutParams(lpimgFooter);

    }

    void updateScheduleBar(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateScheduleBar"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateScheduleBar"));
        ArrayList<Integer> count_slot = new ArrayList<Integer>();
        for (int i=0;i<Main_activity.Main_Room.Slots.size();i++){
            if(Main_activity.Main_Room.Slots.get(i).Status){
                count_slot.add(i);
            }
        }

        RelativeLayout time_schedule = View_main.findViewById(R.id.time_schedule);
        RelativeLayout time_bar = View_main.findViewById(R.id.time_bar);
        if (time_schedule.getChildCount() > 0){
            time_schedule.removeAllViews();
        }
        time_bar.measure(0,0);
        LinearLayout.LayoutParams schedule_params = new LinearLayout.LayoutParams((time_bar.getMeasuredWidth() / count_slot.size()), ViewGroup.LayoutParams.MATCH_PARENT);

        for (int i=0;i<Main_activity.Main_Room.RoomSchedule.size();i++){
            final Schedule _schedule = Main_activity.Main_Room.RoomSchedule.get(i);
            if(_schedule.Status){
                int count = 0;
                for (int j=0;j<Main_activity.Main_Room.Slots.size();j++){
                    if(Main_activity.Main_Room.Slots.get(j).Start_timestamp >= _schedule.Start_timestamp && Main_activity.Main_Room.Slots.get(j).End_timestamp <= _schedule.End_timestamp){
                        ImageView separate = new ImageView(Main_activity);
                        if(SiteData.getTimeDeivceTimestamp() >= _schedule.End_timestamp){
                            separate.setBackgroundResource(R.color.White);
                            separate.setAlpha(0.5f);
                        }else {
                            separate.setBackgroundResource(R.color.Red);
                        }
                        separate.setLayoutParams(schedule_params);

                        time_schedule.addView(separate);

                        ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) separate.getLayoutParams();

                        int count_size;
                        if (count % 2 != 0) {
                            count_size = time_bar.getMeasuredWidth() / count_slot.size();
                        } else {
                            count_size = 0;
                        }
                        lpimgFooter.leftMargin = LeftMarginSlot.get(count/2) + count_size;
                        separate.setLayoutParams(lpimgFooter);
                        separate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SiteData.playSound(Main_activity, "touch");
                                int start_time = _schedule.Start_timestamp;
                                int end_time = _schedule.End_timestamp;
                                String time = Main_activity.Site_data.getTimeString(start_time) + " - " + Main_activity.Site_data.getTimeString(end_time);
                                String title = null;
                                try {
                                    JSONObject json = new JSONObject(_schedule.Schedule_detail);
                                    title = json.getString("title");
                                } catch (JSONException e) {
                                    SiteData.writeFile(Main_activity, TAG + " | updateScheduleBar " + e.getMessage());
                                    e.printStackTrace();
                                }
                                try {
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_main_activity_container_popupbox, new ModulePopupScheduleFragment(Main_activity, time, title, view)).commit();
                                } catch (IllegalStateException e) {
                                    SiteData.writeFile(Main_activity, TAG + " | updateScheduleBar " + e.getMessage());
                                    getFragmentManager().beginTransaction().replace(R.id.fragment_main_activity_container_popupbox, new ModulePopupScheduleFragment(Main_activity, time, title, view)).commitAllowingStateLoss();
                                }
                            }
                        });
                    }
                    if(Main_activity.Main_Room.Slots.get(j).Status) {count++;}
                }
            }
        }
    }

    void hidepreviewScheduleTime(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "hidepreviewScheduleTime"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "hidepreviewScheduleTime"));
        RelativeLayout time_preview = View_main.findViewById(R.id.time_schedule_preview);
        if (time_preview.getChildCount() > 0){
            time_preview.removeAllViews();
        }
    }

    void previewScheduleTime(int start_timestamp, int end_timestamp){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "previewScheduleTime") + " - " + TAG_MODIFIED.tagArgument("int", "start_timestamp", String.valueOf(start_timestamp)) + " - " + TAG_MODIFIED.tagArgument("int", "end_timestamp", String.valueOf(end_timestamp)));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "previewScheduleTime") + " - " + TAG_MODIFIED.tagArgument("int", "start_timestamp", String.valueOf(start_timestamp)) + " - " + TAG_MODIFIED.tagArgument("int", "end_timestamp", String.valueOf(end_timestamp)));
        ArrayList<Integer> count_slot = new ArrayList<Integer>();
        int start = 0, end = 0, count = 0;
        for (int i=0;i<Main_activity.Main_Room.Slots.size();i++){
            if(Main_activity.Main_Room.Slots.get(i).Status){
                count_slot.add(i);
                count++;
            }
            if (start_timestamp == Main_activity.Main_Room.Slots.get(i).Start_timestamp){
                start = count;
            }
            if (end_timestamp == Main_activity.Main_Room.Slots.get(i).End_timestamp){
                end = count;
            }
        }
        //Log.d("timestamp " + String.valueOf(start_timestamp), String.valueOf(end_timestamp));
        //Log.d("numslot " + String.valueOf(start), String.valueOf(end));
        int numSlot = (end - start) + 1;

        RelativeLayout time_preview = View_main.findViewById(R.id.time_schedule_preview);
        RelativeLayout time_bar = View_main.findViewById(R.id.time_bar);
        if (time_preview.getChildCount() > 0){
            time_preview.removeAllViews();
        }
        time_bar.measure(0,0);
        int width = (int)(time_bar.getMeasuredWidth() / (float)count_slot.size());

        int width_start = width * (start-1);
        int width_end = width * (end);

        LinearLayout.LayoutParams schedule_params = new LinearLayout.LayoutParams(width * (end - start + 1), ViewGroup.LayoutParams.MATCH_PARENT);

        ImageView preview = new ImageView(Main_activity);
        preview.setBackgroundResource(R.color.Blue);
        preview.setLayoutParams(schedule_params);

        time_preview.addView(preview);

        ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) preview.getLayoutParams();

        int count_size;
        if ((start-1) % 2 != 0) {
            count_size = time_bar.getMeasuredWidth() / count_slot.size();
        } else {
            count_size = 0;
        }
        lpimgFooter.leftMargin = LeftMarginSlot.get((start-1)/2) + count_size;
        preview.setLayoutParams(lpimgFooter);

    }

    void changeImageViewFromUrl(String url, ImageView imageView, android.content.Context context){
        Glide.with(context).load(url).into(imageView);
    }

//    @SuppressLint("StaticFieldLeak")
//    public class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
//
//        String feed = "DownLoadImageTask";
//
//        ImageView imageView;
//
//        DownLoadImageTask(ImageView imageView){
//            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("", "", "DownLoadImageTask") + " - " + TAG_MODIFIED.tagArgument("ImageView", "imageView", ""));
//            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("", "", "DownLoadImageTask") + " - " + TAG_MODIFIED.tagArgument("ImageView", "imageView", ""));
//            this.imageView = imageView;
//        }
//
//        /*
//            doInBackground(Params... params)
//                Override this method to perform a computation on a background thread.
//         */
//        protected Bitmap doInBackground(String...urls){
//            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "Bitmap", "doInBackground"));
//            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "Bitmap", "doInBackground"));
//
//            String urlOfImage = urls[0];
//            Bitmap logo = null;
//            try{
//                InputStream is = new URL(urlOfImage).openStream();
//                /*
//                    decodeStream(InputStream is)
//                        Decode an input stream into a bitmap.
//                 */
//                logo = BitmapFactory.decodeStream(is);
//            }catch(Exception e){ // Catch the download exception
//                e.printStackTrace();
//                SiteData.writeFile(Main_activity, TAG + " | DownLoadImageTask doInBackground " + e.getMessage());
//            }
//            return logo;
//        }
//
//        /*
//            onPostExecute(Result result)
//                Runs on the UI thread after doInBackground(Params...).
//         */
//        protected void onPostExecute(Bitmap result){
//            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
//            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
//            //imageView.setImageBitmap(result);
//        }
//    }

    @SuppressLint("ResourceAsColor")
    void updateUIStatus(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateUIStatus"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateUIStatus"));
        RelativeLayout background = View_main.findViewById(R.id.background_status_outside);
        RelativeLayout ui_status_available = View_main.findViewById(R.id.ui_status_available);
        RelativeLayout ui_status_occupied = View_main.findViewById(R.id.ui_status_occupied);
        RelativeLayout ui_status_busy = View_main.findViewById(R.id.ui_status_busy);
        RelativeLayout title_available = View_main.findViewById(R.id.title_available);
        RelativeLayout title_no_available = View_main.findViewById(R.id.title_no_available);
        //Log.d(TAG, "Main_activity.Main_Room.RoomStatus " + String.valueOf(Main_activity.Main_Room.RoomStatus));
        switch (Main_activity.Main_Room.RoomStatus){
            case 0: { //available
                if (ui_status_available.getVisibility() == View.INVISIBLE) {
                    SiteData.playSound(Main_activity, "transition");
                }
                background.setBackgroundColor(Color.parseColor("#53FE02"));
                ui_status_available.setVisibility(View.VISIBLE);
                ui_status_occupied.setVisibility(View.INVISIBLE);
                ui_status_busy.setVisibility(View.INVISIBLE);
                title_available.setVisibility(View.VISIBLE);
                title_no_available.setVisibility(View.INVISIBLE);
                TextView text = View_main.findViewById(R.id.text_title_available);
                text.setText(getResources().getString(R.string.available));
                TextView text_usenow = View_main.findViewById(R.id.text_usenow);
                text_usenow.setText(getResources().getString(R.string.use_now));
                TextView text_tapcard = View_main.findViewById(R.id.text_tapcard_available);
                text_tapcard.setText(getResources().getString(R.string.tap_your_id_card_below));
                break;
            }
            case 1: { //occupied
                if (ui_status_occupied.getVisibility() == View.INVISIBLE) {
                    SiteData.playSound(Main_activity, "transition");
                }
                background.setBackgroundColor(Color.parseColor("#F5D800"));
                ui_status_available.setVisibility(View.INVISIBLE);
                ui_status_occupied.setVisibility(View.VISIBLE);
                ui_status_busy.setVisibility(View.INVISIBLE);
                title_available.setVisibility(View.INVISIBLE);
                title_no_available.setVisibility(View.VISIBLE);
                TextView text_checkin = View_main.findViewById(R.id.text_checkin);
                text_checkin.setText(getResources().getString(R.string.check_in));
                TextView text_tapcard_occupied = View_main.findViewById(R.id.text_tapcard_occupied);
                text_tapcard_occupied.setText(getResources().getString(R.string.tap_your_id_card_below));
                Button btn_cancel = View_main.findViewById(R.id.btn_occupied_cancel);
                btn_cancel.setText(getResources().getString(R.string.cancel));
                updateDetailTitle();
                break;
            }
            case 2: { //busy
                if (ui_status_busy.getVisibility() == View.INVISIBLE) {
                    SiteData.playSound(Main_activity, "transition");
                }
                background.setBackgroundColor(Color.parseColor("#EA0100"));
                ui_status_available.setVisibility(View.INVISIBLE);
                ui_status_occupied.setVisibility(View.INVISIBLE);
                ui_status_busy.setVisibility(View.VISIBLE);
                title_available.setVisibility(View.INVISIBLE);
                title_no_available.setVisibility(View.VISIBLE);
                Button btn_busy_end = View_main.findViewById(R.id.btn_busy_end);
                btn_busy_end.setText(getResources().getString(R.string.end));
                Button btn_busy_extend = View_main.findViewById(R.id.btn_busy_extend);
                btn_busy_extend.setText(getResources().getString(R.string.extend));
                updateDetailTitle();
                break;
            }
            default: break;
        }
        updateTextCenterBar();
    }

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    void updateTextCenterBar(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateTextCenterBar"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateTextCenterBar"));
        TextView text = View_main.findViewById(R.id.text_centerbar_time);
        int index = Main_activity.Main_Room.getIndexScheduleFromIdStatus();

        switch (Main_activity.Main_Room.RoomStatus){
            case 0: { //Available
                if (index == -1){
                    text.setText(getResources().getString(R.string.until_the_end_of_the_day));
                }else {
                    Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(index);
                    text.setText(getResources().getString(R.string.until) + " " + Main_activity.Site_data.getTimeString(schedule.Start_timestamp));
                }
                break;
            }
            case 1: { //Occupied
                Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(index);
                text.setText(getResources().getString(R.string.please_check_in_within) + " " + Main_activity.Site_data.getTimeRemain(Main_activity, schedule.Start_timestamp + 30, SiteData.getTimeDeivceTimestamp()));
                break;
            }
            case 2: { //Busy
                Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(index);
                text.setText(getResources().getString(R.string.finishing_in) + " " + Main_activity.Site_data.getTimeRemain(Main_activity, SiteData.getTimeDeivceTimestamp(), schedule.End_timestamp));
                break;
            }
            default: break;
        }
        updateDetailTitle();
    }

    @SuppressLint("SetTextI18n")
    void updateDetailTitle(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateDetailTitle"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateDetailTitle"));
        for (int i=0;i<Main_activity.Main_Room.RoomSchedule.size();i++){
            Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(i);
            try {
                JSONObject json = new JSONObject(schedule.Schedule_detail);
                int id = json.getInt("reservation_instance_id");
                if(id == Main_activity.Main_Room.RoomIdStatus) {
                    String time = Main_activity.Site_data.getTimeString(schedule.Start_timestamp) + " - " + Main_activity.Site_data.getTimeString(schedule.End_timestamp);

                    String title = json.getString("title");
                    String organizer = json.getJSONObject("user").getString("fname");
                    String phone_number = json.getJSONObject("user").getString("phone");

                    TextView text_time = View_main.findViewById(R.id.text_time_title_no_available);
                    TextView text_title = View_main.findViewById(R.id.text_title_title_no_available);
                    TextView text_organize = View_main.findViewById(R.id.text_organize_title_no_available);

                    text_time.setText(time);
                    text_title.setText(title);
                    if(phone_number.equals("") || phone_number.equals(null) || phone_number.equals("null")){
                        text_organize.setText(organizer);
                    }else {
                        text_organize.setText(organizer + ", " + phone_number);
                    }
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | updateDetailTitle " + e.getMessage());
            }
        }


    }

    @SuppressLint({"SetTextI18n", "LongLogTag"})
    void updateUIScheduleValid(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateUIScheduleValid"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateUIScheduleValid"));
        LinearLayout schedule_valid = View_main.findViewById(R.id.text_schedule_valid);
        if(schedule_valid.getChildCount() > 0){
            schedule_valid.removeAllViews();
        }
        //Log.d("Main_activity.ScheduleValid.size()", String.valueOf(Main_activity.ScheduleValid.size()));
        for (int i=0;i<Main_activity.Main_Room.RoomSchedule.size();i++){
            Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(i);
            if (schedule.Start_timestamp <= SiteData.getTimeDeivceTimestamp()) continue;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout layout_text = new RelativeLayout(Main_activity);
            layout_text.setLayoutParams(params);

            schedule_valid.addView(layout_text);

            String time = Main_activity.Site_data.getTimeString(schedule.Start_timestamp) + " - " + Main_activity.Site_data.getTimeString(schedule.End_timestamp);
            String title = null, organizer = null, phone_number = null;
            try {
                JSONObject json = new JSONObject(schedule.Schedule_detail);
                title = json.getString("title");
                organizer = json.getJSONObject("user").getString("fname");
                phone_number = json.getJSONObject("user").getString("phone");
            } catch (JSONException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | updateUIScheduleValid " + e.getMessage());
            }

            TextView text_time = new TextView(Main_activity);
            text_time.setText(time);
            text_time.setTextSize(20);
            text_time.setTextColor(Color.parseColor("#FFFFFF"));

            layout_text.addView(text_time);

            TextView text_title = new TextView(Main_activity);
            text_title.setText(title);
            text_title.setTextSize(20);
            text_title.setTextColor(Color.parseColor("#FFFFFF"));

            layout_text.addView(text_title);

            ViewGroup.MarginLayoutParams lpimgFooter = (ViewGroup.MarginLayoutParams) text_title.getLayoutParams();

            lpimgFooter.leftMargin = 220;
            text_title.setLayoutParams(lpimgFooter);

            TextView text_organize = new TextView(Main_activity);
            if (phone_number.equals("") || phone_number.equals(null) || phone_number.equals("null")){
                text_organize.setText(organizer);
            }else {
                text_organize.setText(organizer + ", " + phone_number);
            }
            text_organize.setTextSize(20);
            text_organize.setTextColor(Color.parseColor("#F5D800"));

            schedule_valid.addView(text_organize);

            ViewGroup.MarginLayoutParams lpimgFooter_organize = (ViewGroup.MarginLayoutParams) text_organize.getLayoutParams();

            lpimgFooter_organize.leftMargin = 220;
            text_organize.setLayoutParams(lpimgFooter_organize);
        }
    }

    void bindButton(){
        Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "bindButton"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "bindButton"));
        /*occupied*/
        /*-----occupied--Cancel-----*/
        Button btn_occupied_cancel = View_main.findViewById(R.id.btn_occupied_cancel);
        btn_occupied_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "cancel");
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_occupied_cancel", "Button"));
                if (!Main_activity.module_frame_cancel_now_fragment.isAdded()) {
                    getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_back, Main_activity.module_frame_cancel_now_fragment, "TAG").commit();
                }
            }
        });

        /*-----occupied--Checked in-----*/
//        RelativeLayout btn_occupied_checkedin = View_main.findViewById(R.id.btn_occupied_checkedin);
//        btn_occupied_checkedin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SiteData.playSound(Main_activity, "touch");
//                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_occupied_checkedin", "RelativeLayout"));
//                Main_activity.manage_main_fragment.new FeedAsynTaskGetUserDetailByRFID().execute();
//
//            }
//        });

        /*Available*/
//        RelativeLayout btn_available_usenow = View_main.findViewById(R.id.btn_available_usenow);
//        btn_available_usenow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SiteData.playSound(Main_activity, "touch");
//                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_available_usenow", "RelativeLayout"));
//                Main_activity.manage_main_fragment.new FeedAsynTaskGetUserDetailByRFID().execute();
//            }
//        });

        /*Busy*/
        /*-----Busy--End-----*/
        Button btn_busy_end = View_main.findViewById(R.id.btn_busy_end);
        btn_busy_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "touch");
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_busy_end", "Button"));
                if (!Main_activity.module_frame_end_now_fragment.isAdded()) {
                    getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_back, Main_activity.module_frame_end_now_fragment, "TAG").commit();
                }
                //Main_activity.module_frame_end_now_fragment.frame_busy_endnow.setVisibility(View.VISIBLE);
            }
        });

        /*-----Busy--Extend-----*/
        Button btn_busy_extend = View_main.findViewById(R.id.btn_busy_extend);
        btn_busy_extend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "touch");
                //Main_activity.module_frame_extend_fragment.showFrame();
                Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_busy_extend", "Button"));
                if (!Main_activity.module_frame_extend_fragment.isAdded()) {
                    Main_activity.getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_back, Main_activity.module_frame_extend_fragment, "TAG").commit();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStart"));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onStart"));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onResume"));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onResume"));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onPause"));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onPause"));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStop"));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onStop"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onDestroy"));
        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onDestroy"));
        SiteData.stopThread(time_thread);
    }
}
