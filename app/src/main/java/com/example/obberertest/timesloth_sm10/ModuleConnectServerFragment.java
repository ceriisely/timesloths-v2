package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleConnectServerFragment extends Fragment {

    static String TAG = "ModuleConnectServerFragment";

    private ConnectActivity Connect_activity;
    private View View_main;
    boolean isConnected = false;

    public ModuleConnectServerFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConnectServerFragment"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConnectServerFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleConnectServerFragment(ConnectActivity connectactivity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConnectServerFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectactivity", String.valueOf(connectactivity)));

        Connect_activity = connectactivity;
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConnectServerFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectactivity", String.valueOf(connectactivity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_connect_server, container, false);
        return View_main;
    }

    public boolean isConnectedToServer(String url) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "boolean", "isConnectedToServer") + " - " + TAG_MODIFIED.tagArgument("String", "url", url));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "boolean", "isConnectedToServer") + " - " + TAG_MODIFIED.tagArgument("String", "url", url));
        try{
            URL myUrl = new URL("http://" + url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(15000);
            connection.connect();
            isConnected = true;
            //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "boolean", "isConnectedToServer") + " - " + TAG_MODIFIED.tagReturn("boolean", "true"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "boolean", "isConnectedToServer") + " - " + TAG_MODIFIED.tagReturn("boolean", "true"));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            SiteData.writeFile(Connect_activity, TAG + " | isConnectedToServer " + e.getMessage());
            isConnected = false;
            //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "boolean", "isConnectedToServer") + " - " + TAG_MODIFIED.tagReturn("boolean", "false"));
            //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "boolean", "isConnectedToServer") + " - " + TAG_MODIFIED.tagReturn("boolean", "false"));
            return false;
        }
    }

}
