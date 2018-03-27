package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class SiteConnectFragment extends Fragment {

    static String TAG = "SiteConnectFragment";

    ConnectActivity Connect_activity;
    String Server_address;
    String Server_time;
    String Server_date; // 2016-08-13
    int Room_id;
    String Path;
    String Password;
    int All_room;
    View View_main;
    private SiteConnectFragment Context;
    public boolean Finish;
    boolean isConnected = false;

    @SuppressLint("ValidFragment")
    public SiteConnectFragment(ConnectActivity _connectactivity, String address, int id, String path) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "SiteConnectFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)) + " - " + TAG_MODIFIED.tagArgument("String", "address", String.valueOf(address)) + " - " + TAG_MODIFIED.tagArgument("int", "id", String.valueOf(id)) + " - " + TAG_MODIFIED.tagArgument("String", "path", String.valueOf(path)));
        Connect_activity = _connectactivity;
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "SiteConnectFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)) + " - " + TAG_MODIFIED.tagArgument("String", "address", String.valueOf(address)) + " - " + TAG_MODIFIED.tagArgument("int", "id", String.valueOf(id)) + " - " + TAG_MODIFIED.tagArgument("String", "path", String.valueOf(path)));
        this.Finish = false;
        ChangeDetailSite(address, id, path);
    }

    public void updateSiteData(ConnectActivity _connectactivity, String address, int id, String path){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "updateSiteData") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)) + " - " + TAG_MODIFIED.tagArgument("String", "address", String.valueOf(address)) + " - " + TAG_MODIFIED.tagArgument("int", "id", String.valueOf(id)) + " - " + TAG_MODIFIED.tagArgument("String", "path", String.valueOf(path)));
        Connect_activity = _connectactivity;
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "updateSiteData") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "_connectactivity", String.valueOf(_connectactivity)) + " - " + TAG_MODIFIED.tagArgument("String", "address", String.valueOf(address)) + " - " + TAG_MODIFIED.tagArgument("int", "id", String.valueOf(id)) + " - " + TAG_MODIFIED.tagArgument("String", "path", String.valueOf(path)));
        this.Finish = false;
        ChangeDetailSite(address, id, path);
        //new FeedAsynTask().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_site_connect, container, false);
        Context = this;
        changeConfigUI();
        new FeedAsynTask().execute();
        return View_main;
    }

    public void changeConfigUI(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "changeConfigUI"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "changeConfigUI"));
        if (View_main != null) {
            TextView _server_address = View_main.findViewById(R.id.Server_address);
            _server_address.setText(Connect_activity.Site_data.Server_address);
            TextView _room_id = View_main.findViewById(R.id.Room_name);
            _room_id.setText(String.valueOf(Connect_activity.Site_data.Room_id));
        }
    }

    @SuppressLint("SetTextI18n")
    public void ChangeDetailSite(String _server_address, int id, String _path){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "ChangeDetailSite") + " - " + TAG_MODIFIED.tagArgument("String", "_server_address", _server_address) + " - " + TAG_MODIFIED.tagArgument("int", "id", String.valueOf(id)) + " - " + TAG_MODIFIED.tagArgument("String", "_path", _path));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "ChangeDetailSite") + " - " + TAG_MODIFIED.tagArgument("String", "_server_address", _server_address) + " - " + TAG_MODIFIED.tagArgument("int", "id", String.valueOf(id)) + " - " + TAG_MODIFIED.tagArgument("String", "_path", _path));
        if(!(_server_address == null)){
            Connect_activity.Site_data.Server_address = _server_address;
        }
        if(!(_path == null)){
            Connect_activity.Site_data.Path = _path;
        }
        Connect_activity.Site_data.Room_id = id;
        changeConfigUI();
    }

    @SuppressLint("StaticFieldLeak")
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
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
            String url = "http://" + Connect_activity.Site_data.Server_address + Connect_activity.Site_data.Path + "api/services.php?action=get&key=config";
            Log.d("OkHttp url site config", url);
            //SiteData.writeFile(Connect_activity, TAG + " | " + "OkHttp url site config" + url);
            com.squareup.okhttp.Request GetSiteConfigData = new com.squareup.okhttp.Request.Builder().url(url).build();

            com.squareup.okhttp.Response getsiteconfigdata = null;
            String result_getsiteconfigdata = null;
            try {
                getsiteconfigdata = client.newCall(GetSiteConfigData).execute();
                result_getsiteconfigdata = getsiteconfigdata.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                SiteData.writeFile(Connect_activity, TAG + " | FeedAsynTask doInBackground " + e.getMessage());
            }
            return result_getsiteconfigdata;
        }

        @SuppressLint("ResourceType")
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d(TAG, TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagFeed(feed) + " - " + TAG_MODIFIED.tagMethod("protected", "void", "onPostExecute"));
            if (s != null) {
                Log.d("OkHttp get site config", s);
                ChangeSiteData(s);
                Finish = true;
                isConnected = true;
                Connect_activity.Room_connect_fragment.new FeedAsynTask().execute();
                //Connect_activity.getFragmentManager().beginTransaction().add(Connect_activity.getBaseContext().getResources().getInteger(R.id.fragment_container), Connect_activity.Room_connect_fragment).commit();
                //Connect_activity.getFragmentManager().beginTransaction().remove(Connect_activity.Site_connect_fragment).commit();
                //Connect_main.new FeedAsynTask().execute();
            }else {
                new FeedAsynTask().execute();
            }
        }
    }

    void ChangeSiteData(String json){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "ChangeSiteData") + " - " + TAG_MODIFIED.tagArgument("String", "json", json));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "ChangeSiteData") + " - " + TAG_MODIFIED.tagArgument("String", "json", json));
        String _logo_url, _app_title, _server_time, _password;
        JSONObject _main_object = null;
        try {
            _main_object = new JSONObject(json);
            _logo_url = _main_object.getString("logo_url");
            _app_title = _main_object.getString("app_title");
            _server_time = _main_object.getString("server_time");
            _password = _main_object.getString("password");

            SiteData.Password = _password;

            Log.d("password", _password);
            Log.d("Server_time", _server_time);

            settingTime(_server_time);

        } catch (JSONException e) {
            Log.d("JSON get site config", "JSONException");
            SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "ChangeSiteData") + " - " + e.getMessage());
            //Button btn_setting = Connect_activity.findViewById(R.id.Connect_btn_setting);
            //btn_setting.performClick();
            e.printStackTrace();
        }
    }

    void settingTime(String server_time){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "settingTime") + " - " + TAG_MODIFIED.tagArgument("String", "server_time", server_time));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "settingTime") + " - " + TAG_MODIFIED.tagArgument("String", "server_time", server_time));
        //Log.d("main_Server_time", server_time);
        String year = server_time.substring(0,4);
        String month = server_time.substring(5,7);
        String day = server_time.substring(8,10);
        String hour = server_time.substring(11,13);
        String minute = server_time.substring(14,16);
        String second = server_time.substring(17,19);
        Connect_activity.Site_data.Server_date = year + "-" + month + "-" + day;
        //Connect_main.Room_connect_fragment.new FeedAsynTask().execute();
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command = "date -s "+year+month+day+"."+hour+minute+second+"\n";
            Log.e("command",command);
            os.writeBytes(command);
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "settingTime") + " - " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getFragmentManager().beginTransaction().remove(SiteConnectFragment.this).commit();
    }
}
