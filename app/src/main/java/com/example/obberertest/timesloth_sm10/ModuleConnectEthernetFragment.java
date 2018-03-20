package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ModuleConnectEthernetFragment extends Fragment {

    static String TAG = "ModuleConfigWiFiFragment";

    ConnectActivity Connect_activity;
    private View View_main;
    boolean isConnected = false;

    public ModuleConnectEthernetFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConnectEthernetFragment"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConnectEthernetFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleConnectEthernetFragment(ConnectActivity connectActivity) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConnectEthernetFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectActivity", String.valueOf(connectActivity)));
        Connect_activity = connectActivity;
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConnectEthernetFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectActivity", String.valueOf(connectActivity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_connect_ethernet, container, false);
        return View_main;
    }

    boolean isConnectedEthernet() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "boolean", "isConnectedEthernet"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "boolean", "isConnectedEthernet"));
        ConnectivityManager conMgr =  (ConnectivityManager)Connect_activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        isConnected = netInfo != null;
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "boolean", "isConnectedEthernet") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(isConnected)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "boolean", "isConnectedEthernet") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(isConnected)));
        return isConnected;
    }

}
