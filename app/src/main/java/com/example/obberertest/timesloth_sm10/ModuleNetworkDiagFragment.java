package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleNetworkDiagFragment extends Fragment {

    String TAG = "ModuleNetworkDiagFragment";

    private ConnectActivity Connect_activity;
    View View_main = null;
    private boolean isConnectedSiteData = false;
    public Thread main_thread;
    private Thread time_thread;
    public boolean isConnectSite = true;

    public ModuleNetworkDiagFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleNetworkDiagFragment"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleNetworkDiagFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleNetworkDiagFragment(ConnectActivity connectActivity) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleNetworkDiagFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectActivity", String.valueOf(connectActivity)));

        Connect_activity = connectActivity;
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleNetworkDiagFragment") + " - " + TAG_MODIFIED.tagArgument("ConnectActivity", "connectActivity", String.valueOf(connectActivity)));
        Connect_activity.Connect_ethernet_fragment = new ModuleConnectEthernetFragment(connectActivity);
        Connect_activity.Check_invalid_ip_fragment = new ModuleCheckInvalidIpFragment(connectActivity);
        Connect_activity.Connect_server_fragment = new ModuleConnectServerFragment(connectActivity);
        Connect_activity.Site_connect_fragment = new SiteConnectFragment(connectActivity, Connect_activity.Site_data.Server_address, Connect_activity.Site_data.Room_id, Connect_activity.Site_data.Path);
        Connect_activity.Room_connect_fragment = new RoomConnectFragment(connectActivity);
    }


    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_network_diag, container, false);
        bindView();
        return View_main;
    }

    private void bindView() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        Button btn_retry = View_main.findViewById(R.id.btn_retry_connection);
        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, TAG_MODIFIED.tagOnClick("btn_retry_connection", "Button"));
                SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagOnClick("btn_retry_connection", "Button"));
                startConnectNetwork();
            }
        });
    }

    private boolean isConnectNetworkState(int state){
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagArgument("int", "state", String.valueOf(state)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagArgument("int", "state", String.valueOf(state)));
        boolean result;
        switch (state){
            case 1  :   {
                result = Connect_activity.Connect_ethernet_fragment.isConnectedEthernet();
                break;
            }
            case 2  :   {
                result = Connect_activity.Check_invalid_ip_fragment.isInvalidIp();
                break;
            }
            case 3  :   {
                result = Connect_activity.Connect_server_fragment.isConnectedToServer(Connect_activity.Site_data.getBaseServer());
                break;
            }
            case 4  :   {
                int count_time = 0;
//                if (Connect_activity.Site_connect_fragment.isAdded()){
//                    try {
//                        Connect_activity.getFragmentManager().beginTransaction().remove(Connect_activity.Site_connect_fragment).commit();
//                    } catch (IllegalStateException e) {
//                        Connect_activity.getFragmentManager().beginTransaction().remove(Connect_activity.Site_connect_fragment).commitAllowingStateLoss();
//                        Log.d(TAG, " | isConnectNetworkState " + e.getMessage());
//                        SiteData.writeFile(Connect_activity, TAG + " | isConnectNetworkState " + e.getMessage());
//                        return false;
//                    }
//                }
                if (Connect_activity.Site_connect_fragment.isAdded()){
                    try {
                        Connect_activity.getFragmentManager().beginTransaction().remove(Connect_activity.Site_connect_fragment).commit();
                    } catch (IllegalStateException e) {
                        Connect_activity.getFragmentManager().beginTransaction().remove(Connect_activity.Site_connect_fragment).commitAllowingStateLoss();
                        Log.d(TAG, " | isConnectNetworkState " + e.getMessage());
                        SiteData.writeFile(Connect_activity, TAG + " | isConnectNetworkState " + e.getMessage());
                        return false;
                    }
                } else {
                    try {
                        Connect_activity.getFragmentManager().beginTransaction().add(R.id.fragment_container, Connect_activity.Site_connect_fragment).commit();
                    } catch (IllegalStateException e) {
                        //Connect_activity.getFragmentManager().beginTransaction().add(R.id.fragment_container, Connect_activity.Site_connect_fragment).commitAllowingStateLoss();
                        Log.d(TAG, " | isConnectNetworkState " + e.getMessage());
                        SiteData.writeFile(Connect_activity, TAG + " | isConnectNetworkState " + e.getMessage());
                        return false;
                    }
                }
                while (isConnectSite){
                    if (count_time > 60){
                        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
                        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
                        return false;
                    }
//                    if(Connect_activity.Site_connect_fragment.Finish){
//                        break;
//                    }
                    try {
                        sleep(1000);
                        try {
                            if (Connect_activity.Site_connect_fragment.isAdded()) {
                                getFragmentManager().beginTransaction().remove(Connect_activity.Site_connect_fragment).commit();
                            }
                            if (Connect_activity.Room_connect_fragment.isAdded()) {
                                getFragmentManager().beginTransaction().remove(Connect_activity.Room_connect_fragment).commit();
                            }
                        } catch (IllegalStateException e) {
                            if (Connect_activity.Site_connect_fragment.isAdded()) {
                                getFragmentManager().beginTransaction().remove(Connect_activity.Site_connect_fragment).commitAllowingStateLoss();
                            }
                            if (Connect_activity.Room_connect_fragment.isAdded()) {
                                getFragmentManager().beginTransaction().remove(Connect_activity.Room_connect_fragment).commitAllowingStateLoss();
                            }
                        }
                        count_time++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
                        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + e.getMessage());
                        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
                        return false;
                    }
                }
//                count_time = 0;
//                Connect_activity.Room_connect_fragment.new FeedAsynTask().execute();
//                while (true){
//                    if (count_time > 10){
//                        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
//                        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
//                        return false;
//                    }
//                    if(Connect_activity.Room_connect_fragment.Finish && !Connect_activity.isConnectNetworkAll){
//                        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(true)));
//                        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(true)));
//                        return true;
//                    }
//                    try {
//                        sleep(1000);
//                        count_time++;
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + e.getMessage());
//                        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
//                        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(false)));
//                        return false;
//                    }
//                }
            }
            case 5  :   {
                Connect_activity.isConnectNetworkAll = true;
                Connect_activity.finishConnectNetwork();
                Connect_activity.IntentToMainActivity();
                result = true;
                break;
            }
            default: {
                result = false;
                break;
            }
        }
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(result)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "boolean", "isConnectNetworkState") + " - " + TAG_MODIFIED.tagReturn("boolean", String.valueOf(result)));
        return result;
    }

    void startConnectNetwork() {
        Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "updateBeforeConnectionState"));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "updateBeforeConnectionState"));
        Button btn_retry = View_main.findViewById(R.id.btn_retry_connection);
        btn_retry.setVisibility(View.INVISIBLE);

        time_thread = new Thread(){
            @Override
            public void run() {
                try {
                    int time_sleep = 0;
                    final TextView text_loading = View_main.findViewById(R.id.text_loading_connection);
                    while (true) {
                        if (time_sleep++ % 20 == 0) {
                            Connect_activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text_loading.setText(". ");
                                }
                            });
                        }
                        sleep(100);
                        final String text = text_loading.getText().toString() + ". ";
                        Connect_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_loading.setText(text);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Connect_activity, TAG + " | time_thread " + e.getMessage());
                }
            }
        };
        main_thread = new Thread(){
            @Override
            public void run() {
                int count_connect = 0;
                final int[] state = {0};
                Connect_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateSuccessState(state[0]++);
                        updateBeforeConnectionState(state[0]);
                    }
                });
                try {
                    int time_sleep = 0;
                    final TextView text_loading = View_main.findViewById(R.id.text_loading_connection);
                    while (time_sleep < 100) {
                        if (time_sleep++ % 20 == 0) {
                            Connect_activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    text_loading.setText(". ");
                                }
                            });
                        }
                        sleep(100);
                        final String text = text_loading.getText().toString() + ". ";
                        Connect_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                text_loading.setText(text);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Connect_activity, TAG + " | main_thread " + e.getMessage());
                }
                try {
                    if (!time_thread.isAlive()) {
                        time_thread.start();
                    }
                    while (true) {
                        if (isConnectNetworkState(state[0])) {
                            Connect_activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateSuccessState(state[0]++);
                                    updateBeforeConnectionState(state[0]);
                                }
                            });
                            count_connect = 0;
                            sleep(5000);
                            if (state[0] > 5){
                                break;
                            }
                        } else {
                            count_connect++;
                        }
                        if (count_connect > 5) {
                            stopConnectNetwork(false);
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Connect_activity, TAG + " | main_thread " + e.getMessage());
                }
            }
        };
        main_thread.start();
    }

    private void updateBeforeConnectionState(int state){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "updateBeforeConnectionState") + " - " + TAG_MODIFIED.tagArgument("int", "state", String.valueOf(state)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "updateBeforeConnectionState") + " - " + TAG_MODIFIED.tagArgument("int", "state", String.valueOf(state)));
        TextView text_detail = View_main.findViewById(R.id.text_detail_connection);
        switch (state){
            case 1  :   {
                text_detail.setText("Connecting ethernet");
                break;
            }
            case 2  :   {
                text_detail.setText("Checking IP address");
                break;
            }
            case 3  :   {
                text_detail.setText("Connecting to server " + Connect_activity.Site_data.getBaseServer());
                break;
            }
            case 4  :   {
                text_detail.setText("Preparing");
                break;
            }
            case 5  :   {
                text_detail.setText("Connecting to server as Room " + Connect_activity.Site_data.Room_id);
                break;
            }
            default: break;
        }
    }

    private void updateSuccessState(int state) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "updateSuccessState") + " - " + TAG_MODIFIED.tagArgument("int", "state", String.valueOf(state)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "updateSuccessState") + " - " + TAG_MODIFIED.tagArgument("int", "state", String.valueOf(state)));
        RelativeLayout _layout = null;
        switch (state){
            case 0: {
                _layout = View_main.findViewById(R.id.network_diag_box_1);
                _layout.setBackground(getResources().getDrawable(R.drawable.network_diag_box));
                _layout = View_main.findViewById(R.id.network_diag_box_2);
                _layout.setBackground(getResources().getDrawable(R.drawable.network_diag_box));
                _layout = View_main.findViewById(R.id.network_diag_box_3);
                _layout.setBackground(getResources().getDrawable(R.drawable.network_diag_box));
                _layout = View_main.findViewById(R.id.network_diag_box_4);
                _layout.setBackground(getResources().getDrawable(R.drawable.network_diag_box));
                return;
            }
            case 1: {
                _layout = View_main.findViewById(R.id.network_diag_box_1);
                break;
            }
            case 2: {
                _layout = View_main.findViewById(R.id.network_diag_box_2);
                break;
            }
            case 3: {
                _layout = View_main.findViewById(R.id.network_diag_box_3);
                break;
            }
            case 4: {
                _layout = View_main.findViewById(R.id.network_diag_box_4);
                break;
            }
            default: {
                return;
            }
        }
        if (isAdded() && _layout != null){
            _layout.setBackground(getResources().getDrawable(R.drawable.network_diag_box_success));
        }
    }

    @SuppressLint("LongLogTag")
    void stopConnectNetwork(boolean isBtnSetting){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "stopConnectNetwork") + " - " + TAG_MODIFIED.tagArgument("boolean", "isBtnSetting", String.valueOf(isBtnSetting)));
        //SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "stopConnectNetwork") + " - " + TAG_MODIFIED.tagArgument("boolean", "isBtnSetting", String.valueOf(isBtnSetting)));
        if (!isBtnSetting){
            main_thread.interrupt();
            time_thread.interrupt();
        }else {
            while (main_thread.isAlive()) {
                main_thread.interrupt();
            }
            while (time_thread.isAlive()) {
                time_thread.interrupt();
            }
        }
        if (!isBtnSetting){
            Connect_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Button btn_retry = View_main.findViewById(R.id.btn_retry_connection);
                    btn_retry.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStart"));
        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onStart"));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onResume"));
        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onResume"));
        startConnectNetwork();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onStop"));
        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onStop"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onDestroy"));
        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onDestroy"));
        SiteData.stopThread(main_thread);
        SiteData.stopThread(time_thread);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "onPause"));
        SiteData.writeFile(Connect_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "void", "onPause"));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }
}
