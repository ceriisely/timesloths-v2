package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleCheckInvalidIpFragment extends Fragment {

    static String TAG = "ModuleCheckInvalidIpFragment";
    //private static Context Connect_activity;

    private static ConnectActivity Connect_activity;
    private View View_main;
    boolean isConnected = false;

    public ModuleCheckInvalidIpFragment() {
        // Required empty public constructor
        //.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleCheckInvalidIpFragment"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleCheckInvalidIpFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleCheckInvalidIpFragment(ConnectActivity connectactivity) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleCheckInvalidIpFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectactivity", String.valueOf(connectactivity)));
        Connect_activity = connectactivity;
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleCheckInvalidIpFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectactivity", String.valueOf(connectactivity)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_check_invalid_ip, container, false);
        return View_main;
    }

    boolean isInvalidIp() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "boolean", "isInvalidIp"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "boolean", "isInvalidIp"));
        isConnected = validate(getDeviceIPAddress(true));

        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "boolean", "isInvalidIp") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(isConnected)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "boolean", "isInvalidIp") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(isConnected)));
        return isConnected;
    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "boolean", "validate") + " - " + TAG_MODIFIED.tagArgument("String", "ip", String.valueOf(ip)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public static", "boolean", "validate") + " - " + TAG_MODIFIED.tagArgument("String", "ip", String.valueOf(ip)));
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public static", "boolean", "validate") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(PATTERN.matcher(ip).matches())));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public static", "boolean", "validate") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(PATTERN.matcher(ip).matches())));
        return PATTERN.matcher(ip).matches();
    }

    public String getDeviceIPAddress(boolean useIPv4) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagArgument("boolean", "useIPv4", String.valueOf(useIPv4)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagArgument("boolean", "useIPv4", String.valueOf(useIPv4)));
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        String sAddr = inetAddress.getHostAddress().toUpperCase();
                        //String gateway = inetAddress.getHostName().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4) {
                                //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf(sAddr)));
                                //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf(sAddr)));
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                // drop ip6 port suffix
                                int delim = sAddr.indexOf('%');
                                //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf(delim < 0 ? sAddr : sAddr.substring(0, delim))));
                                //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf(delim < 0 ? sAddr : sAddr.substring(0, delim))));
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            SiteData.writeFile(Connect_activity, TAG + " | getDeviceIPAddress " + e.getMessage());
        }
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf("")));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf("")));
        return "";
    }

}
