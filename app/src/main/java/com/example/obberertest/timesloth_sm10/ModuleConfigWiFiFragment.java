package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.thanosfisherman.wifiutils.WifiUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.obberertest.timesloth_sm10.ModuleReadRFIDFragment.View_main;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleConfigWiFiFragment extends Fragment {

    static String TAG = "ModuleConfigWiFiFragment";
    private NetworkSettingActivity Networksetting_activity;
    private WifiManager wifii;
    private DhcpInfo d;
    private String s_dns1;
    private String s_dns2;
    private String s_gateway;
    private String s_ipAddress;
    private String s_leaseDuration;
    private String s_netmask;
    private String s_serverAddress;
    private Thread time_thread;

    public ModuleConfigWiFiFragment() {
        // Required empty public constructor
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConfigWiFiFragment"));
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "initConfig"));
    }

    @SuppressLint("ValidFragment")
    public ModuleConfigWiFiFragment(NetworkSettingActivity networkSettingActivity) {
        // Required empty public constructor
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConfigWiFiFragment"));
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "initConfig"));
        Networksetting_activity = networkSettingActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_config_wi_fi, container, false);
        bindView();
        return View_main;
    }

    void bindView() {
        Switch sw_wifi = View_main.findViewById(R.id.switch_wifi);
        if (NetworkSettingActivity.isConnectedWifi(Networksetting_activity)) {
            sw_wifi.setChecked(true);
            enableWifi();
            initIp();
            showConnectedWifi();
        } else {
            sw_wifi.setChecked(false);
        }
        sw_wifi.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                enableWifi();
                initIp();
                showConnectedWifi();
            } else {
                disableWifi();
            }
        });

        Button btn_back = View_main.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Networksetting_activity, "ok");
                time_thread.interrupt();
                time_thread = null;
                Networksetting_activity.onDestroy();
                //onDestroy();
                getFragmentManager().beginTransaction().remove(ModuleConfigWiFiFragment.this).commit();
            }
        });
    }

    void enableWifi() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command = "svc wifi enable " + "\n";
            Log.e("command",command);
            os.writeBytes(command);
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        RelativeLayout ssid_form = View_main.findViewById(R.id.wifi_ssid_form);
        ssid_form.setVisibility(View.VISIBLE);
        showSSID();

    }

    private void showConnectedWifi(){
        TextView text_ssid = View_main.findViewById(R.id.wifi_ssid_connected);
        time_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        Networksetting_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WifiManager wifiMgr = (WifiManager) Networksetting_activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                assert wifiMgr != null;
                                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                                String name = wifiInfo.getSSID();

                                try {
                                    text_ssid.setVisibility(View.VISIBLE);
                                    text_ssid.setText("Connected: " + name.substring(1, name.length()-1));
                                } catch (NullPointerException e) {
                                    SiteData.stopThread(time_thread);
                                    getFragmentManager().beginTransaction().remove(ModuleConfigWiFiFragment.this).commit();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Networksetting_activity, TAG + " | time_thread " + e.getMessage());
                }
            }
        };
        time_thread.start();
    }

    private void showSSID() {
        WifiUtils.withContext(Networksetting_activity.getApplicationContext()).enableWifi(this::checkResult);
        WifiUtils.withContext(Networksetting_activity.getApplicationContext()).scanWifi(this::getScanResults).start();
    }

    private void checkResult(boolean isSuccess)
    {
        if (isSuccess)
            Toast.makeText(Networksetting_activity, "WIFI ENABLED", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(Networksetting_activity, "COULDN'T ENABLE WIFI", Toast.LENGTH_SHORT).show();
    }

    private void getScanResults(@NonNull final List<ScanResult> results)
    {
        if (results.isEmpty())
        {
            Log.i(TAG, "SCAN RESULTS IT'S EMPTY");
            return;
        }
        for (int i=0;i<results.size();i++) {
            if (results.get(i).capabilities.contains("[WPS]") || results.get(i).capabilities.contains("PSK")) {
                Log.i(TAG, "GOT SCAN RESULTS WPS " + results.get(i).SSID + " " + results.get(i).capabilities);
            } else {
                Log.i(TAG, "GOT SCAN RESULTS " + results.get(i).SSID + " " + results.get(i).capabilities);
                results.remove(i);
                i = 0;
            }
        }
        setAdapterWiFiSSID(results);
        //Log.i(TAG, "GOT SCAN RESULTS " + results);
    }

    private void setAdapterWiFiSSID(List<ScanResult> results){
        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("please select ssid");
        for (ScanResult result : results) {
            if (!result.SSID.equals("")) {
                spinnerArray.add(result.SSID);
            }
        }

        Spinner spinner = View_main.findViewById(R.id.spinner_wifi_ssid);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Networksetting_activity, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) return;
                String ssid = adapterView.getItemAtPosition(position).toString();
                final EditText input = new EditText(Networksetting_activity);
                final TextView text = new TextView(Networksetting_activity);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Networksetting_activity);
                // set title
                alertDialogBuilder.setTitle(ssid);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Connect",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                ModuleLoaderFragment.showLoader();
                                String networkSSID = ssid;
                                WifiConfiguration conf = new WifiConfiguration();
                                conf.SSID = "\"" + networkSSID + "\"";

                                if (results.get(position).capabilities.contains("WPA")) {
                                    String networkPass = input.getText().toString();
                                    conf.preSharedKey = "\"" + networkPass + "\"";
                                } else {
                                    conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                                }

                                WifiManager wifiManager = (WifiManager) Networksetting_activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                assert wifiManager != null;
                                wifiManager.addNetwork(conf);

                                List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                                for( WifiConfiguration i : list ) {
                                    if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                                        wifiManager.disconnect();
                                        wifiManager.enableNetwork(i.networkId, true);
                                        wifiManager.reconnect();
                                        break;
                                    }
                                }
                                ModuleLoaderFragment.hideLoader();
                            }

                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //disable wifi
                                //wifiMan.setWifiEnabled(false);
                            }
                        });
                Log.d(TAG, results.get(position).SSID + " - " + results.get(position).capabilities);
                if (results.get(position).capabilities.contains("PSK")) {
                    text.setText("  Password  :   ");
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    LinearLayout layout = new LinearLayout(Networksetting_activity);
                    input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout.addView(text);
                    layout.addView(input);
                    layout.setLayoutParams(lp);
                    alertDialogBuilder.setView(layout);

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    input.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (editable.toString().length() > 0) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            } else {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }
                        }
                    });
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    alertDialogBuilder.setMessage("Do you want to connect ?");

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void disableWifi() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command = "svc wifi disable " + "\n";
            Log.e("command",command);
            os.writeBytes(command);
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        TextView text_ssid = View_main.findViewById(R.id.wifi_ssid_connected);
        text_ssid.setVisibility(View.INVISIBLE);
        RelativeLayout ssid_form = View_main.findViewById(R.id.wifi_ssid_form);
        ssid_form.setVisibility(View.INVISIBLE);
    }

    void initIp(){
        wifii= (WifiManager) Networksetting_activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert wifii != null;
        d=wifii.getDhcpInfo();

        s_dns1="DNS 1: "+intToIp(d.dns1);
        s_dns2="DNS 2: "+intToIp(d.dns2);
        s_gateway="Default Gateway: "+intToIp(d.gateway);
        s_ipAddress="IP Address: "+intToIp(d.ipAddress);
        s_leaseDuration="Lease Time: "+intToIp(d.leaseDuration);
        s_netmask="Subnet Mask: "+intToIp(d.netmask);
        s_serverAddress="Server IP: "+intToIp(d.serverAddress);

        //dispaly them
        //info= (TextView) findViewById(R.id.infolbl);
        Log.d(TAG, "Network Info "+s_dns1+" "+s_dns2+" "+s_gateway+" "+s_ipAddress+" "+s_leaseDuration+" "+s_netmask+" "+s_serverAddress);
    }

    public String intToIp(int addr) {
        return  ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
