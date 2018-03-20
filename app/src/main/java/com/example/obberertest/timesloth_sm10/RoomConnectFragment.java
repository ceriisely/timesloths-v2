package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoomConnectFragment extends Fragment {

    static String TAG = "RoomConnectFragment";

    private ConnectActivity Connect_activity;
    private RoomConnectFragment Context;
    private View View_main;
    public boolean Finish;
    boolean isConnected = false;

    public RoomConnectFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "RoomConnectFragment"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "RoomConnectFragment"));
    }

    @SuppressLint("ValidFragment")
    public RoomConnectFragment(ConnectActivity _connectactivity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "RoomConnectFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)));
        Context = this;
        Connect_activity = _connectactivity;
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "RoomConnectFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)));
        this.Finish = false;
    }

    public void updateRoom(ConnectActivity _connectactivity){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "updateRoom") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "updateRoom") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)));
        Connect_activity = _connectactivity;
        this.Finish = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_room_connect, container, false);
        bindButton();
        bindSetting();
        return View_main;
    }

    private void bindButton() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindButton"));
        Button btn_save = View_main.findViewById(R.id.btn_connect_setting_save);
        Button btn_ipconfig = View_main.findViewById(R.id.btn_connect_setting_ipconfig);

        btn_save.setOnClickListener(view -> {
            SiteData.playSound(Connect_activity, "ok");
            //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_connect_setting_save", "Button"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_connect_setting_save", "Button"));
            //NumberPicker room = View_main.findViewById(R.id.numberpicker_connect_setting_select_room);
            EditText room_number = View_main.findViewById(R.id.edittext_number_room);
            EditText server_address = View_main.findViewById(R.id.edittext_connect_setting_serveraddress);
//                if(Connect_activity.All_Room.size() > 0) {
//                    Connect_activity.Site_data.Room_id = Connect_activity.All_Room.get(room.getValue()).Id;
//                }
            Connect_activity.Site_data.Room_id = Integer.parseInt(room_number.getText().toString());
            Connect_activity.Site_data.Server_address = server_address.getText().toString();
            //Button save = Connect_main.findViewById(R.id.Connect_btn_setting);
            //save.performClick();
            Connect_activity.Room_connect_fragment.updateRoom(Connect_activity);
            Connect_activity.Site_connect_fragment.updateSiteData(Connect_activity, Connect_activity.Site_data.Server_address, Connect_activity.Site_data.Room_id, Connect_activity.Site_data.Path);
            Connect_activity.Network_diag_fragment.startConnectNetwork();
            try {
                Connect_activity.getFragmentManager().beginTransaction().remove(Connect_activity.Room_connect_fragment).commit();
            } catch (IllegalStateException e) {
                SiteData.writeFile(Connect_activity, TAG + " | btn_connect_setting_save " + e.getMessage());
                Connect_activity.getFragmentManager().beginTransaction().remove(Connect_activity.Room_connect_fragment).commitAllowingStateLoss();
            }
        });

        btn_ipconfig.setOnClickListener(view -> {
            SiteData.playSound(Connect_activity, "ok");
            //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_connect_setting_ipconfig", "Button"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_connect_setting_ipconfig", "Button"));
            //Intent intent = new Intent(Connect_activity, NetworkSettingActivity.class);
            //startActivity(intent);
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            Connect_activity.onDestroy();
            //startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        });
    }

    class FeedAsynTask extends AsyncTask<String, Void, String> {

        String feed = "FeedAsynTask";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPreExecute"));
        }

        @Override
        protected String doInBackground(String... strings) {
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "String", "doInBackground"));
            Connect_activity.All_Room.removeAll(Connect_activity.All_Room);
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
            String url = "http://" + Connect_activity.Site_data.Server_address + Connect_activity.Site_data.Path + "api/services.php?action=get&key=all_room_reservation_by_date&date=" + Connect_activity.Site_data.Server_date;
            //String url = "http://" + Connect_main.Site_data.Server_address + Connect_main.Site_data.Path + "api/services.php?action=get&key=all_room_reservation_by_date&date=2018-01-05";
            //Log.d("OkHttp url all room", url);
            //SiteData.writeFile(Connect_activity, TAG + " | " + "OkHttp url all room" + url);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();
            com.squareup.okhttp.Response response = null;
            String result = null;
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Connect_activity, TAG + " | FeedAsynTask doInBackground " + e.getMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            if (!Objects.equals(s, "")) {
                Log.d("OkHttp get all room", s);
                try {
                    JSONObject all_room_resource = new JSONObject(s);
                    JSONArray all_room = all_room_resource.getJSONArray("resources");
                    for (int i = 0; i < all_room.length(); i++) {
                        JSONObject room_detail = all_room.getJSONObject(i);
                        JSONObject room_data = room_detail.getJSONObject("roomData");
                        String resource_type = room_data.getString("resource_type_id"); //check type of room
                        if(Objects.equals(resource_type, "1")){
                            Connect_activity.All_Room.add(new Room(room_detail));
                        }
                    }
                    Finish = true;
                    isConnected = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Connect_activity, TAG + " | FeedAsynTask onPostExecute " + e.getMessage());
                }
                //Connect_main.new FeedAsynTask().execute();
            }
        }
    }

    private void bindSetting(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindSetting"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindSetting"));
        //int maxValue = Connect_activity.All_Room.size();
        //NumberPicker select_room = View_main.findViewById(R.id.numberpicker_connect_setting_select_room);
        EditText room_number = View_main.findViewById(R.id.edittext_number_room);
        EditText server_address = View_main.findViewById(R.id.edittext_connect_setting_serveraddress);

        server_address.setText(Connect_activity.Site_data.Server_address);
        room_number.setText(String.valueOf(Connect_activity.Site_data.Room_id));

    }

}
