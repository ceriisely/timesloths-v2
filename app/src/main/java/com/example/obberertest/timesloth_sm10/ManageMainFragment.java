package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageMainFragment extends Fragment {

    String TAG = "ManageMainFragment";

    private ManageMainFragment Context;
    private MainActivity Main_activity;
    private View View_main;
    JSONObject UserDetailByRFID;
    boolean AddReservation;
    private Thread time_thread;
    private Thread refresh_thread;
    private Thread update_slot_thread;
    private Thread all_room_thread;
    private static int Connection_fail = 0;

    public ManageMainFragment() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ManageMainFragment"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ManageMainFragment"));
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ManageMainFragment(MainActivity context) {
        Context = this;
        Main_activity = context;
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ManageMainFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ManageMainFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_manage_main, container, false);
        runThread();
        return View_main;
    }

    private void runThread(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "runThread"));

        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(Main_activity.Site_data.TimeRefresh);
                        Main_activity.runOnUiThread(new Runnable() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void run() {
                                if (Main_activity.manage_main_fragment.isAdded()) {
                                    new FeedAsynTask_getRoomInfoByRoomID().execute();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | time_thread " + e.getMessage());
                    Connection_fail++;
                }
            }
        };
        time_thread.start();

        refresh_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Main_activity.manage_main_fragment.isAdded()) {
                                    updateRoomStatus(Main_activity.Main_Room);
                                    updateScheduleValid();
                                    if (Main_activity.module_default_main_fragment.isAdded()) {
                                        Main_activity.module_default_main_fragment.updateUIStatus();
                                        Main_activity.module_default_main_fragment.updateUIScheduleValid();
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | refresh_thread " + e.getMessage());
                    Connection_fail++;
                }
            }
        };
        refresh_thread.start();

        update_slot_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Main_activity.manage_main_fragment.isAdded()) {
                                    Main_activity.Main_Room.updateSlot();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | update_slot_thread " + e.getMessage());
                    Connection_fail++;
                }
            }
        };
        update_slot_thread.start();

        all_room_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(5 * 60000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Main_activity.manage_main_fragment.isAdded()) {
                                    new FeedAsynTaskAllRoom().execute();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | all_room_thread " + e.getMessage());
                    Connection_fail++;
                }
            }
        };
        all_room_thread.start();
    }

    private void updateRoomStatus(Room main_room){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "updateRoomStatus") + " - " + TAG_MODIFIED.tagArgument("Room", "main_room", String.valueOf(main_room)));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "updateRoomStatus") + " - " + TAG_MODIFIED.tagArgument("Room", "main_room", String.valueOf(main_room)));
        try {
            int timestamp = SiteData.getTimeDeivceTimestamp();
            //Log.d(TAG, "main_room schedule " + String.valueOf(main_room.RoomSchedule.size()));
            if (main_room.RoomSchedule.size() == 0) {
                main_room.RoomStatus = 0;
                main_room.RoomIdStatus = 0;
                return;
            }
            for(int i=0;i<main_room.RoomSchedule.size();i++) {
                Schedule schedule = main_room.RoomSchedule.get(i);
                JSONObject json = new JSONObject(schedule.Schedule_detail);
                if (timestamp >= schedule.Start_timestamp && timestamp < schedule.End_timestamp) {
                    boolean checked_in = json.getBoolean("checked_in");
                    if (checked_in) {
                        main_room.RoomStatus = 2; // Busy;
                        main_room.RoomIdStatus = json.getInt("reservation_instance_id");
                    } else if (timestamp < schedule.Start_timestamp + 30) {
                        main_room.RoomStatus = 1; //wait
                        main_room.RoomIdStatus = json.getInt("reservation_instance_id");
                    } else if (timestamp >= schedule.Start_timestamp + 30) {
                        main_room.RoomStatus = 1;
                        main_room.RoomIdStatus = json.getInt("reservation_instance_id");
                        if (!Main_activity.module_loader_fragment.Loader.isShown()){
                            new FeedAsynTaskUpdateReservation().execute();
                        }
                        // delete schedule
                    }
                    return;
                } else if (timestamp < schedule.Start_timestamp) {
                    main_room.RoomStatus = 0;
                    main_room.RoomIdStatus = json.getInt("reservation_instance_id");
                    return;
                }
            }
            main_room.RoomStatus = 0;
            main_room.RoomIdStatus = 0;
        } catch (JSONException e) {
            e.printStackTrace();
            SiteData.writeFile(Main_activity, TAG + " | updateRoomStatus " + e.getMessage());
            Connection_fail++;
        }
    }

    class FeedAsynTask_getRoomInfoByRoomID extends AsyncTask<String, Void, String> {
        String feed = "FeedAsynTask_getRoomInfoByRoomID";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=get&key=reservation_by_resourceid_date&resourceid=" + String.valueOf(Main_activity.Main_Room.Id) + "&date=" + Main_activity.Site_data.Server_date;
            //String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=get&key=reservation_by_resourceid_date&resourceid=" + String.valueOf(Main_activity.Main_Room.Id) + "&date=" + "2017-12-22";
            Log.d("OkHttp url getRoomInfoRoomID", url);
            //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url getRoomInfoRoomID " + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                client.cancel("TAG");
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTask_getRoomInfoByRoomID doInBackground " + e.getMessage());
                Connection_fail++;
            }
            return "";
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //Log.d("OkHttp get getRoomInfoRoomID",s);
            if(!s.equals("")){
                getMainRoomInfoByRoomID(s);
            }
        }
    }

    @SuppressLint("LongLogTag")
    void getMainRoomInfoByRoomID(String string_json){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "getMainRoomInfoByRoomID"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "getMainRoomInfoByRoomID"));
        if (string_json == null) return;
        try {
            JSONObject json = new JSONObject(string_json);
            JSONArray room_schedule = json.getJSONArray("RoomSchedule");
            Log.d("room_schedule.length()", String.valueOf(room_schedule.length()));
            if (room_schedule.length() == 0) {

            }
            for (int i=0;i<room_schedule.length();i++){
                JSONObject schedule = room_schedule.getJSONObject(i);
                Log.d("size Main_activity.Main_Room.RoomSchedule.size()", String.valueOf(Main_activity.Main_Room.RoomSchedule.size()));
                int size = Main_activity.Main_Room.RoomSchedule.size();
                if(size == 0){
                    String start_time = (schedule.getString("start_date_time")) + ":00";
                    String end_time = (schedule.getString("end_date_time")) + ":00";
                    Main_activity.Main_Room.RoomSchedule.add(new Schedule(start_time, end_time, schedule));
                    for (int j=0;j<Main_activity.Main_Room.RoomSchedule.size();j++){
                        Schedule main_schedule = Main_activity.Main_Room.RoomSchedule.get(j);
                        if (main_schedule.Start_time == start_time){
                            //Main_activity.ScheduleValid.add(main_schedule);
                            break;
                        }
                    }
                    //Collections.sort(Main_activity.ScheduleValid, Schedule.StartTimestampCompare);
                    Collections.sort(Main_activity.Main_Room.RoomSchedule, Schedule.StartTimestampCompare);
                }else {
                    for (int j = 0; j < size; j++) {
                        Log.d("Main_activity.Main_Room.RoomSchedule.size()", String.valueOf(Main_activity.Main_Room.RoomSchedule.size()));
                        //Log.d("getRoomInfoByRoomID", String.valueOf(j));
                        JSONObject main_schedule = new JSONObject(Main_activity.Main_Room.RoomSchedule.get(j).Schedule_detail);
                        if (Objects.equals(schedule.getString("reference_number"), main_schedule.getString("reference_number"))) {
                            Main_activity.Main_Room.RoomSchedule.get(j).Schedule_detail = schedule.toString();
                            Main_activity.Main_Room.RoomSchedule.get(j).updateSchedule();
                            if (Main_activity.Main_Room.Status) {
                                Main_activity.module_default_main_fragment.updateScheduleBar();
                            }
                            //Collections.sort(Main_activity.ScheduleValid, Schedule.StartTimestampCompare);
                            Collections.sort(Main_activity.Main_Room.RoomSchedule, Schedule.StartTimestampCompare);
                            break;
                        } else if (j == Main_activity.Main_Room.RoomSchedule.size() - 1) {
                            String start_time = (schedule.getString("start_date_time")) + ":00";
                            String end_time = (schedule.getString("end_date_time")) + ":00";
                            Main_activity.Main_Room.RoomSchedule.add(new Schedule(start_time, end_time, schedule));
                            for (int k=0;k<Main_activity.Main_Room.RoomSchedule.size();k++){
                                Schedule _schedule = Main_activity.Main_Room.RoomSchedule.get(k);
                                if (_schedule.Start_time == start_time){
                                    //Main_activity.ScheduleValid.add(_schedule);
                                    break;
                                }
                            }
                            //Collections.sort(Main_activity.ScheduleValid, Schedule.StartTimestampCompare);
                            Collections.sort(Main_activity.Main_Room.RoomSchedule, Schedule.StartTimestampCompare);
                        }
                    }
                }
            }
            //Log.d("size del Main_activity.Main_Room.RoomSchedule.size()", String.valueOf(Main_activity.Main_Room.RoomSchedule.size()));
            ArrayList<Integer> indexSchedule = new ArrayList<Integer>();
            int size = Main_activity.Main_Room.RoomSchedule.size();
            for (int i=0;i<size;i++){
                JSONObject main_schedule = new JSONObject(Main_activity.Main_Room.RoomSchedule.get(i).Schedule_detail);
                if (room_schedule.length() == 0) {
                    indexSchedule.add(i);
                    continue;
                }
                for (int j=0;j<room_schedule.length();j++){
                    JSONObject schedule = room_schedule.getJSONObject(j);
                    Log.d(main_schedule.getString("reference_number"), schedule.getString("reference_number"));
                    if (Objects.equals(main_schedule.getString("reference_number"), schedule.getString("reference_number"))){
                        break;
                    }
                    if (j == room_schedule.length() - 1){
                        indexSchedule.add(i);
                        Log.d("add indexSchedule", String.valueOf(i));
                    }
                }
            }
            for (int i=0;i<indexSchedule.size();i++){
                //Log.d("indexSchedule", String.valueOf(indexSchedule.get(i)));
                int num = indexSchedule.get(i);
                JSONObject schedule = new JSONObject(Main_activity.Main_Room.RoomSchedule.get(num).Schedule_detail);
                String start_time = (schedule.getString("start_date_time")) + ":00";
                String end_time = (schedule.getString("end_date_time")) + ":00";
                String reference = schedule.getString("reference_number");
                int start_timestamp = SiteData.getTimestamp(start_time);
                int end_timestamp = SiteData.getTimestamp(end_time);
                Log.d(TAG, "start_time " + start_timestamp);
                Log.d(TAG, "end_timestamp " + end_timestamp);
                for (int j=0;j<Main_activity.Main_Room.Slots.size();j++) {
                    if (Main_activity.Main_Room.Slots.get(j).Start_timestamp >= start_timestamp && Main_activity.Main_Room.Slots.get(j).End_timestamp <= end_timestamp) {
                        Main_activity.Main_Room.Slots.get(j).Free = true;
                    }
                }
                //Main_activity.Main_Room.deleteSchedule(num);
                Main_activity.Main_Room.RoomSchedule.remove(num);
                //int valid_size = Main_activity.ScheduleValid.size();
//                for (int j=0;j<valid_size;j++){
//                    JSONObject valid_schedule = new JSONObject(Main_activity.ScheduleValid.get(j).Schedule_detail);
//                    if (Objects.equals(valid_schedule.getString("reference_number"), reference)){
//                        //Main_activity.ScheduleValid.remove(j);
//                        break;
//                    }
//                }

            }
            //Main_activity.ScheduleValid = Main_activity.Main_Room.RoomSchedule;
            //Log.d("schedule info", String.valueOf(room_schedule.length()));
        } catch (JSONException e) {
            e.printStackTrace();
            SiteData.writeFile(Main_activity, TAG + " | getMainRoomInfoByRoomID " + e.getMessage());
            Connection_fail++;
        }
    }

    void updateScheduleValid(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateScheduleValid"));
//        SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateScheduleValid"));
//        int timestamp = SiteData.getTimeDeivceTimestamp();
//        ArrayList<Schedule> all_schedule = Main_activity.ScheduleValid;
//        for(int i=0;i<all_schedule.size();i++){
//            Schedule schedule = all_schedule.get(i);
//            if(timestamp >= schedule.Start_timestamp){
//                Main_activity.ScheduleValid.remove(i);
//            }
//        }
    }

    @SuppressLint("StaticFieldLeak")
    class FeedAsynTaskCheckedIn extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTaskCheckedIn";
        String referenceNumber;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            Main_activity.module_loader_fragment.showLoader();
            int index = Main_activity.Main_Room.getIndexScheduleFromIdStatus();
            try {
                JSONObject json = new JSONObject(Main_activity.Main_Room.RoomSchedule.get(index).Schedule_detail);
                referenceNumber = json.getString("reference_number");
            } catch (JSONException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskCheckedIn onPreExecute " + e.getMessage());
                Connection_fail++;
            }
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=set&key=checkin_reservation&refnum=" + referenceNumber;
            //Log.d("OkHttp url ChecedIn", url);
            //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url ChecedIn " + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskCheckedIn doInBackground " + e.getMessage());
                Connection_fail++;
            }
            return "";
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            try {
                if (!Objects.equals(s, "")) {
                    Log.d(TAG, "OkHttp get checked in | " + s);
                    try {
                        JSONObject json = new JSONObject(s);
                        boolean status = json.getBoolean("status");
                        if (status) {
                            Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.check_in_successfully));
                            new FeedAsynTask_getRoomInfoByRoomID().execute();
                        } else {
                            Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.check_in_fail));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskCheckedIn onPostExecute " + e.getMessage());
                        Connection_fail++;
                        Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.check_in_fail));
                    }
                } else {
                    Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.check_in_fail));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskCheckedIn onPostExecute " + e.getMessage());
                Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.check_in_fail));
            }
            Main_activity.module_loader_fragment.hideLoader();
            //Log.d("OkHttp get ChecedIn",s);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class FeedAsynTaskGetUserDetailByRFID extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTaskGetUserDetailByRFID";
        String rfidNumber;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            Main_activity.module_loader_fragment.showLoader();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            //rfidNumber = "0008731202";
            rfidNumber = ModuleReadRFIDFragment.RFID;
            if (!rfidNumber.equals("")) {
                String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=get&key=user_by_rfid&rfid=" + rfidNumber;
                Log.d("OkHttp url GetUserDetailByRFID", url);
                //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url GetUserDetailByRFID " + url);
                com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
                com.squareup.okhttp.Response response = null;
                String result;
                try {
                    response = client.newCall(request).execute();
                    result = response.body().string();
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskGetUserDetailByRFID doInBackground " + e.getMessage());
                    Connection_fail++;
                }
            }
            return "";
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            ModuleReadRFIDFragment.RFID = "";
            EditText input_rfid = ModuleReadRFIDFragment.View_main.findViewById(R.id.edittext_input_rfid);
            input_rfid.setText("");
            if(s.equals("")){
                Main_activity.module_loader_fragment.hideLoader();
                return;
            }
            //Log.d("OkHttp get GetUserDetailByRFID",s);
            try {
                //Main_activity.module_frame_alert_popup_fragment.Alert(rfidNumber);
                JSONObject json = new JSONObject(s);
                if (!json.getBoolean("status")){
                    Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.rfid_error));
                    Main_activity.module_loader_fragment.hideLoader();
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskGetUserDetailByRFID onPostExecute " + e.getMessage());
                Connection_fail++;
                Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.rfid_error));
                Main_activity.module_loader_fragment.hideLoader();
                return;
            }
            try {
                UserDetailByRFID = new JSONObject(s);
                Main_activity.module_loader_fragment.hideLoader();
                switch (Main_activity.Main_Room.RoomStatus){
                    case 0:{ //available
                        Main_activity.getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_back, Main_activity.module_frame_use_now_fragment, "TAG").commitAllowingStateLoss();
                        //Main_activity.module_frame_use_now_fragment.showFrame();
                        break;
                    }
                    case 1:{ //occupied
                        new FeedAsynTaskCheckedIn().execute();
                        break;
                    }
                    case 2:{ //busy
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskGetUserDetailByRFID onPostExecute " + e.getMessage());
                Connection_fail++;
                Main_activity.module_frame_alert_popup_fragment.Alert("RFID_Error");
                Main_activity.module_loader_fragment.hideLoader();
                return;
            }
        }
    }

    String getNameUserByRFID(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "String", "getNameUserByRFID"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "String", "getNameUserByRFID"));
        String name = null;
        try {
            name = UserDetailByRFID.getString("fullname");
        } catch (JSONException e) {
            e.printStackTrace();
            SiteData.writeFile(Main_activity, TAG + " | getNameUserByRFID " + e.getMessage());
            Connection_fail++;
        }
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "String", "getNameUserByRFID") + " - " + TAG_MODIFIED.tagReturn("String", name));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "String", "getNameUserByRFID") + " - " + TAG_MODIFIED.tagReturn("String", name));
        return name;
    }

    String getUserIdByRFID(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "String", "getUserIdByRFID"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "String", "getUserIdByRFID"));
        String id = null;
        try {
            id = UserDetailByRFID.getString("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
            SiteData.writeFile(Main_activity, TAG + " | getUserIdByRFID " + e.getMessage());
            Connection_fail++;
        }
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "String", "getUserIdByRFID") + " - " + TAG_MODIFIED.tagReturn("String", id));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "String", "getUserIdByRFID") + " - " + TAG_MODIFIED.tagReturn("String", id));
        return id;
    }

    @SuppressLint("StaticFieldLeak")
    class FeedAsynTaskUpdateReservation extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTaskUpdateReservation";
        String referenceNumber;
        String endTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            Main_activity.module_loader_fragment.showLoader();
            if (Main_activity.module_frame_extend_fragment.isAdded()){
                endTime = Main_activity.module_frame_extend_fragment.End_time_extend.substring(0, 5);
            }else if(Main_activity.module_frame_end_now_fragment.isAdded()){
                endTime = Main_activity.Site_data.getTimeString24(SiteData.getTimeDeivceTimestamp());
            }else if(Main_activity.Main_Room.RoomStatus == 1){ //occupied over 30 minutes
                endTime = Main_activity.Site_data.getTimeString24(SiteData.getTimeDeivceTimestamp());
            }
            int index = Main_activity.Main_Room.getIndexScheduleFromIdStatus();
            try {
                if(index >= 0) {
                    JSONObject json = new JSONObject(Main_activity.Main_Room.RoomSchedule.get(index).Schedule_detail);
                    referenceNumber = json.getString("reference_number");
                }else {
                    referenceNumber = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskUpdateReservation onPreExecute " + e.getMessage());
                Connection_fail++;
            }
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=set&key=update_reservation_endtime&refnum=" + referenceNumber + "&endtime=" + endTime;
            Log.d("OkHttp url UpdateReservation", url);
            //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url UpdateReservation " + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                client.cancel("TAG");
                return result;
            } catch (SocketException e){
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskUpdateReservation doInBackground " + e.getMessage());
                Connection_fail++;
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskUpdateReservation doInBackground " + e.getMessage());
                Connection_fail++;
            }
            return "";
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            try {
                if (!Objects.equals(s, "")) {
                    try {
                        JSONObject json = new JSONObject(s);
                        boolean status = json.getBoolean("status");
                        if (status) {
                            if (Main_activity.module_frame_end_now_fragment.isAdded()) {
                                ModuleAlertPopupFragment.Alert(getResources().getString(R.string.end_current_reservation));
                            } else if (Main_activity.module_frame_extend_fragment.isAdded()) {
                                ModuleAlertPopupFragment.Alert(getResources().getString(R.string.extend_current_reservation));
                            }
                            new FeedAsynTask_getRoomInfoByRoomID().execute();
                        } else {
                            if (Main_activity.module_frame_end_now_fragment.isAdded()) {
                                ModuleAlertPopupFragment.Alert(getResources().getString(R.string.cannot_end_current_reservation));
                            } else if (Main_activity.module_frame_extend_fragment.isAdded()) {
                                ModuleAlertPopupFragment.Alert(getResources().getString(R.string.cannot_extend_current_reservation));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskUpdateReservation onPostExecute " + e.getMessage());
                        Connection_fail++;
                        if (Main_activity.module_frame_end_now_fragment.isAdded()) {
                            ModuleAlertPopupFragment.Alert(getResources().getString(R.string.cannot_end_current_reservation));
                        } else if (Main_activity.module_frame_extend_fragment.isAdded()) {
                            ModuleAlertPopupFragment.Alert(getResources().getString(R.string.cannot_extend_current_reservation));
                        }
                    }
                }
                if (Main_activity.module_frame_end_now_fragment.isAdded()) {
                    Main_activity.getFragmentManager().beginTransaction().remove(Main_activity.module_frame_end_now_fragment).commit();
                } else if (Main_activity.module_frame_extend_fragment.isAdded()) {
                    Main_activity.getFragmentManager().beginTransaction().remove(Main_activity.module_frame_extend_fragment).commit();
                }
                Main_activity.module_default_main_fragment.hidepreviewScheduleTime();
            } catch (NullPointerException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskUpdateReservation onPostExecute " + e.getMessage());
                if (Main_activity.module_frame_end_now_fragment.isAdded()) {
                    Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cannot_end_current_reservation));
                } else if (Main_activity.module_frame_extend_fragment.isAdded()) {
                    Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cannot_extend_current_reservation));
                }
            }
            Main_activity.module_loader_fragment.hideLoader();
            //Log.d("OkHttp get UpdateReservation",s);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class FeedAsynTaskDeleteReservation extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTaskUpdateReservation";
        String referenceNumber;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            int index;
//            switch (Main_activity.Main_Room.RoomStatus){
//                case 0:{ //available
//                    break;
//                }
//                case 1:{ //occupied
//                    break;
//                }
//                case 2:{ //busy
//
//                    break;
//                }
//            }
            index = Main_activity.Main_Room.getIndexScheduleFromIdStatus();
            try {
                JSONObject json = new JSONObject(Main_activity.Main_Room.RoomSchedule.get(index).Schedule_detail);
                referenceNumber = json.getString("reference_number");
            } catch (JSONException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskDeleteReservation onPreExecute " + e.getMessage());
                Connection_fail++;
            }
            Main_activity.module_loader_fragment.showLoader();
//            for (int i=0;i<Main_activity.Main_Room.RoomSchedule.size();i++){
//                Schedule schedule = Main_activity.Main_Room.RoomSchedule.get(i);
//                try {
//                    JSONObject json = new JSONObject(schedule.Schedule_detail);
//                    int id = json.getInt("reservation_instance_id");
//                    if(id == Main_activity.Main_Room.RoomIdStatus){
//                        referenceNumber = json.getString("reference_number");
//                        break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " +  TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=set&key=delete_reservation&refnum=" + referenceNumber;
            Log.d("OkHttp url DeleteReservation", url);
            //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url DeleteReservation " + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskDeleteReservation doInBackground " + e.getMessage());
                Connection_fail++;
            }
            return "";
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            try {
                if (!Objects.equals(s, "")) {
                    try {
                        JSONObject json = new JSONObject(s);
                        boolean status = json.getBoolean("status");
                        if (status) {
                            Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cancel_now_successfully));
                            new FeedAsynTask_getRoomInfoByRoomID().execute();
                            Main_activity.getFragmentManager().beginTransaction().remove(Main_activity.module_frame_cancel_now_fragment).commit();
                        } else {
                            Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cancel_now_fail));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskDeleteReservation onPostExecute " + e.getMessage());
                        Connection_fail++;
                        Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cancel_now_fail));
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskDeleteReservation onPostExecute " + e.getMessage());
                Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.cancel_now_fail));
            }
            Main_activity.module_loader_fragment.hideLoader();
            //Log.d("OkHttp get DeleteReservation",s);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class FeedAsynTaskReserveRoomNow extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTaskReserveRoomNow";
        String title;
        String organize;
        String time;
        String rfid;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=set&key=add_reservation_now&title=" + title + "&des=" + organize + "&resourceid=" + String.valueOf(Main_activity.Main_Room.Id) + "&endtime=" + time + "&userId=" + rfid + "&checkin=true";
            Log.d("OkHttp url ReserveRoomNow", url);
            //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url ReserveRoomNow " + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskReserveRoomNow doInBackground " + e.getMessage());
                Connection_fail++;
            }
            return result;
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //Log.d("OkHttp get ReserveRoomNow",s);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class FeedAsynTaskAddReservation extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTaskAddReservation";
        String title;
        String organize;
        String startTime; //16:00
        String endTime; //12:00
        String date; //2014-12-01
        String userId;
        String checked_in;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            Main_activity.module_loader_fragment.showLoader();
            title = Main_activity.module_frame_use_now_fragment.getTextSubject();
            organize = getNameUserByRFID();
            startTime = Main_activity.module_frame_use_now_fragment.Start_time_usenow.substring(0, 5);
            if (Main_activity.Site_data.getTimeDeivceTimestamp() < Main_activity.Site_data.getTimestamp(startTime)){
                checked_in = "false";//false
            }else {
                checked_in = "true";
            }
            endTime = Main_activity.module_frame_use_now_fragment.End_time_usenow.substring(0, 5);
            date = Main_activity.Site_data.Server_date;
            userId = getUserIdByRFID();
        }

        @SuppressLint("LongLogTag")
        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=set&key=add_reservation&title=" + title + "&des=" + organize + "&resourceid=" + String.valueOf(Main_activity.Main_Room.Id) + "&starttime=" + startTime + "&endtime=" + endTime + "&date=" + date + "&checkin=" + checked_in + "&userId=" + userId;
            Log.d("OkHttp url AddReservation", url);
            //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url AddReservation " + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskAddReservation doInBackground " + e.getMessage());
                Connection_fail++;
            }
            return "";
        }

        @SuppressLint("LongLogTag")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            try {
                if (!Objects.equals(s, "")) {
                    Log.d(TAG, "OkHttp get AddReservation | " + s);
                    try {
                        JSONObject json = new JSONObject(s);
                        boolean status = json.getBoolean("status");
                        if (status) {
                            Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.add_reservation_successfully));
                            new FeedAsynTask_getRoomInfoByRoomID().execute();
                        } else {
                            Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.add_reservation_fail));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskAddReservation onPostExecute " + e.getMessage());
                        Connection_fail++;
                        Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.add_reservation_fail));
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskAddReservation onPostExecute " + e.getMessage());
                Main_activity.module_frame_alert_popup_fragment.Alert(getResources().getString(R.string.add_reservation_fail));
            }
            Main_activity.module_loader_fragment.hideLoader();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class FeedAsynTaskAllRoom extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTaskAllRoom";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ModuleLoaderFragment.showLoader();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
        }

        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
            String url = "http://" + Main_activity.Site_data.Server_address + Main_activity.Site_data.Path + "api/services.php?action=get&key=all_room_reservation_by_date&date=" + Main_activity.Site_data.Server_date;
            //String url = "http://" + Connect_main.Site_data.Server_address + Connect_main.Site_data.Path + "api/services.php?action=get&key=all_room_reservation_by_date&date=2018-01-05";
            Log.d("OkHttp url all room", url);
            //SiteData.writeFile(Main_activity, TAG + " | " + "OkHttp url all room " + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskAllRoom doInBackground " + e.getMessage());
                Connection_fail++;
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            if (!Objects.equals(s, "")) {
                Log.d("OkHttp get all room", s);
                try {
                    JSONObject all_room_resource = new JSONObject(s);
                    JSONArray all_room = all_room_resource.getJSONArray("resources");
                    for (int i = 0; i < all_room.length(); i++) {
                        JSONObject room_detail = all_room.getJSONObject(i);
                        JSONObject room_data = room_detail.getJSONObject("roomData");
                        String name = room_data.getString("name");
                        for (int j=0;j<Main_activity.All_Room.size();j++){
                            Room _room = Main_activity.All_Room.get(j);
                            String _name = _room.Name;
                            if (Objects.equals(name, _name)){
                                getRoomInfoByRoomID(room_detail, _room);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | FeedAsynTaskAllRoom onPostExecute " + e.getMessage());
                    Connection_fail++;
                }
            }
            try {
                getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_all_room, Main_activity.module_find_all_room_fragment, "TAG").commit();
            } catch (IllegalStateException e){
                getFragmentManager().beginTransaction().add(R.id.fragment_main_activity_container_all_room, Main_activity.module_find_all_room_fragment, "TAG").commitAllowingStateLoss();
                SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_find_all_room", "Button") + " - " + e.getMessage());
            }
            ModuleLoaderFragment.hideLoader();
        }
    }

    @SuppressLint("LongLogTag")
    void getRoomInfoByRoomID(JSONObject json, Room main_room){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "getRoomInfoByRoomID"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "getRoomInfoByRoomID"));
        if (json == null) return;
        try {
            JSONArray room_schedule = json.getJSONArray("RoomSchedule");
            //Log.d("room_schedule.length()", String.valueOf(room_schedule.length()));
            for (int i=0;i<room_schedule.length();i++){
                JSONObject schedule = room_schedule.getJSONObject(i);
                Log.d("size Main_activity.Main_Room.RoomSchedule.size()", String.valueOf(main_room.RoomSchedule.size()));
                int size = main_room.RoomSchedule.size();
                if(size == 0){
                    String start_time = (schedule.getString("start_date_time")) + ":00";
                    String end_time = (schedule.getString("end_date_time")) + ":00";
                    main_room.RoomSchedule.add(new Schedule(start_time, end_time, schedule));
                    Collections.sort(main_room.RoomSchedule, Schedule.StartTimestampCompare);
                }else {
                    for (int j = 0; j < size; j++) {
                        Log.d("Main_activity.Main_Room.RoomSchedule.size()", String.valueOf(main_room.RoomSchedule.size()));
                        //Log.d("getRoomInfoByRoomID", String.valueOf(j));
                        JSONObject main_schedule = new JSONObject(main_room.RoomSchedule.get(j).Schedule_detail);
                        if (Objects.equals(schedule.getString("reference_number"), main_schedule.getString("reference_number"))) {
                            main_room.RoomSchedule.get(j).Schedule_detail = schedule.toString();
                            main_room.RoomSchedule.get(j).updateSchedule();
                            Collections.sort(main_room.RoomSchedule, Schedule.StartTimestampCompare);
                            break;
                        } else if (j == main_room.RoomSchedule.size() - 1) {
                            String start_time = (schedule.getString("start_date_time")) + ":00";
                            String end_time = (schedule.getString("end_date_time")) + ":00";
                            main_room.RoomSchedule.add(new Schedule(start_time, end_time, schedule));
                            Collections.sort(main_room.RoomSchedule, Schedule.StartTimestampCompare);
                        }
                    }
                }
            }
            //Log.d("size del Main_activity.Main_Room.RoomSchedule.size()", String.valueOf(main_room.RoomSchedule.size()));
            ArrayList<Integer> indexSchedule = new ArrayList<Integer>();
            int size = main_room.RoomSchedule.size();
            for (int i=0;i<size;i++){
                JSONObject main_schedule = new JSONObject(main_room.RoomSchedule.get(i).Schedule_detail);
                if (room_schedule.length() == 0) {
                    indexSchedule.add(i);
                    continue;
                }
                for (int j=0;j<room_schedule.length();j++){
                    JSONObject schedule = room_schedule.getJSONObject(j);
                    //Log.d(main_schedule.getString("reference_number"), schedule.getString("reference_number"));
                    if (Objects.equals(main_schedule.getString("reference_number"), schedule.getString("reference_number"))){
                        break;
                    }
                    if (j == room_schedule.length() - 1){
                        indexSchedule.add(i);
                        //Log.d("add indexSchedule", String.valueOf(i));
                    }
                }
            }
            for (int i=0;i<indexSchedule.size();i++){
                int num = indexSchedule.get(i);
                main_room.RoomSchedule.remove(num);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            SiteData.writeFile(Main_activity, TAG + " | getRoomInfoByRoomID " + e.getMessage());
            Connection_fail++;
        }
        updateRoomStatus(main_room);
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
        SiteData.stopThread(refresh_thread);
        SiteData.stopThread(update_slot_thread);
        SiteData.stopThread(all_room_thread);
    }
}
