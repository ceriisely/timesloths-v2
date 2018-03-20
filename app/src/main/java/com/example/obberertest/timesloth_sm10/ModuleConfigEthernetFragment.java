package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.pow;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleConfigEthernetFragment extends Fragment {

    static String TAG = "ModuleConfigEthernetFragment";

    private NetworkSettingActivity NetworkSetting_activity;
    private View View_main;
    private RadioButton radio_dhcp;
    private RadioButton radio_manually;
    private LinearLayout form_config_dhcp;
    private LinearLayout form_config_manually;
    private String IPAddress;
    private String Subnetmask;
    private String DNS1 = "";
    private String DNS2 = "";

    private WifiManager wifii;
    private DhcpInfo d;
    private String s_dns1;
    private String s_dns2;
    private String s_gateway;
    private String s_ipAddress;
    private String s_leaseDuration;
    private String s_netmask;
    private String s_serverAddress;
    private String Gateway;


    public ModuleConfigEthernetFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment"));
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment"));
    }

    @SuppressLint("ValidFragment")
    public ModuleConfigEthernetFragment(NetworkSettingActivity context) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment") + " - " + TAG_MODIFIED.tagArgument("NetworkSettingActivity", "context", String.valueOf(context)));

        NetworkSetting_activity = context;
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleConfigEthernetFragment") + " - " + TAG_MODIFIED.tagArgument("NetworkSettingActivity", "context", String.valueOf(context)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_config_ethernet, container, false);
        radio_dhcp = View_main.findViewById(R.id.radio_dhcp);
        radio_manually = View_main.findViewById(R.id.radio_manually);
        form_config_dhcp = View_main.findViewById(R.id.form_config_dhcp);
        form_config_manually = View_main.findViewById(R.id.form_config_manually);
        bindView();
        initConfig();
        return View_main;
    }

    private void bindView() {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "bindView"));
        radio_dhcp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SiteData.playSound(NetworkSetting_activity, "touch");
                if (checked){
                    form_config_dhcp.setVisibility(View.VISIBLE);
                    form_config_manually.setVisibility(View.INVISIBLE);
                }
            }
        });
        radio_manually.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                SiteData.playSound(NetworkSetting_activity, "touch");
                if (checked){
                    form_config_manually.setVisibility(View.VISIBLE);
                    form_config_dhcp.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button btn_save = View_main.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                SiteData.playSound(NetworkSetting_activity, "ok");
                //if (radio_manually.isChecked()) {
                    ModuleLoaderFragment.showLoader();
                    setConfigEthernet();
                    ModuleLoaderFragment.hideLoader();
                    ModuleAlertPopupFragment.Alert(NetworkSetting_activity, "Save!!!!!!!");
                //}
            }
        });

        Button btn_back = View_main.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(NetworkSetting_activity, "ok");
                NetworkSetting_activity.onDestroy();
            }
        });
    }

    private void setConfigEthernet() {
        Log.d(TAG, "getIpAddress " + getIpAddress());
        Log.d(TAG, "getSubnetmask " + getSubnetmask());
        Log.d(TAG, "getGateway " + getGateway());
        Log.d(TAG, "getDNS1 " + getDNS1());
        Log.d(TAG, "getDNS2 " + getDNS2());
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            String command = "ifconfig eth0 " + getIpAddress() + " netmask " + getSubnetmask() + "\n";
            Log.e("command",command);
            os.writeBytes(command);
            os.flush();
            command = "route add default gw " + getGateway() + " dev eth0" + "\n";
            Log.e("command",command);
            os.writeBytes(command);
            os.flush();
            command = "setprop net.dns1 " + getDNS1() + "\n";
            Log.e("command",command);
            os.writeBytes(command);
            os.flush();
            command = "setprop net.dns2 " + getDNS2() + "\n";
            Log.e("command",command);
            os.writeBytes(command);
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initConfig() {
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "initConfig"));
        //SiteData.writeFile(NetworkSetting_activity, TAG + " | " + TAG_MODIFIED.tagMethod("private", "void", "initConfig"));
        if(NetworkSetting_activity.isConnected){
            if (NetworkSetting_activity.isConnectedMobile){
                radio_dhcp.setChecked(true);
            }else {
                radio_manually.setChecked(true);
            }
        } else {
            radio_manually.setChecked(true);
            return;
        }
        IPAddress = getDeviceIPAddress(true);
        getDNS();
        Gateway = getDeviceGateway();
        Log.d(TAG, "ip address " + IPAddress);
        Log.d(TAG, "subnet mask " + Subnetmask);
        Log.d(TAG, "gateway " + Gateway);
        Log.d(TAG, "dns1 " + DNS1);
        Log.d(TAG, "dns2 " + DNS2);

        initViewIpAddress(IPAddress);
        initViewSubnet(Subnetmask);
        initViewGateway(Gateway);
        initViewDNS1(DNS1);
        initViewDNS2(DNS2);
    }

    private void initViewIpAddress(String ipAddress) {
        EditText editText_1 = View_main.findViewById(R.id.ip_address_1);
        EditText editText_2 = View_main.findViewById(R.id.ip_address_2);
        EditText editText_3 = View_main.findViewById(R.id.ip_address_3);
        EditText editText_4 = View_main.findViewById(R.id.ip_address_4);

        String[] ip = new String[4];
        int count_ip = 0;
        for (int i=0;i<ipAddress.length();i++) {
            if (ipAddress.charAt(i) == '.') {
                count_ip++;
                ip[count_ip] = "";
                continue;
            }
            if (ip[count_ip] == null) {
                ip[count_ip] = "";
            }
            ip[count_ip] = ip[count_ip].concat(String.valueOf(ipAddress.charAt(i)));
        }

        editText_1.setText(ip[0]);
        editText_2.setText(ip[1]);
        editText_3.setText(ip[2]);
        editText_4.setText(ip[3]);
    }

    private void initViewSubnet(String subnetmask) {
        EditText editText_1 = View_main.findViewById(R.id.subnet_1);
        EditText editText_2 = View_main.findViewById(R.id.subnet_2);
        EditText editText_3 = View_main.findViewById(R.id.subnet_3);
        EditText editText_4 = View_main.findViewById(R.id.subnet_4);

        String[] ip = new String[4];
        int count_ip = 0;
        for (int i=0;i<subnetmask.length();i++) {
            if (subnetmask.charAt(i) == '.') {
                count_ip++;
                ip[count_ip] = "";
                continue;
            }
            if (ip[count_ip] == null) {
                ip[count_ip] = "";
            }
            ip[count_ip] = ip[count_ip].concat(String.valueOf(subnetmask.charAt(i)));
        }

        editText_1.setText(ip[0]);
        editText_2.setText(ip[1]);
        editText_3.setText(ip[2]);
        editText_4.setText(ip[3]);
    }

    private void initViewGateway(String gateway) {
        EditText editText_1 = View_main.findViewById(R.id.gateway_1);
        EditText editText_2 = View_main.findViewById(R.id.gateway_2);
        EditText editText_3 = View_main.findViewById(R.id.gateway_3);
        EditText editText_4 = View_main.findViewById(R.id.gateway_4);

        String[] ip = new String[4];
        int count_ip = 0;
        for (int i=0;i<gateway.length();i++) {
            if (gateway.charAt(i) == '.') {
                count_ip++;
                ip[count_ip] = "";
                continue;
            }
            if (ip[count_ip] == null) {
                ip[count_ip] = "";
            }
            ip[count_ip] = ip[count_ip].concat(String.valueOf(gateway.charAt(i)));
        }

        editText_1.setText(ip[0]);
        editText_2.setText(ip[1]);
        editText_3.setText(ip[2]);
        editText_4.setText(ip[3]);
    }

    private void initViewDNS1(String dns1) {
        EditText editText_1 = View_main.findViewById(R.id.dns1_1);
        EditText editText_2 = View_main.findViewById(R.id.dns1_2);
        EditText editText_3 = View_main.findViewById(R.id.dns1_3);
        EditText editText_4 = View_main.findViewById(R.id.dns1_4);

        String[] ip = new String[4];
        int count_ip = 0;
        for (int i=0;i<dns1.length();i++) {
            if (dns1.charAt(i) == '.') {
                count_ip++;
                ip[count_ip] = "";
                continue;
            }
            if (ip[count_ip] == null) {
                ip[count_ip] = "";
            }
            ip[count_ip] = ip[count_ip].concat(String.valueOf(dns1.charAt(i)));
        }

        editText_1.setText(ip[0]);
        editText_2.setText(ip[1]);
        editText_3.setText(ip[2]);
        editText_4.setText(ip[3]);
    }

    private void initViewDNS2(String dns2) {
        EditText editText_1 = View_main.findViewById(R.id.dns2_1);
        EditText editText_2 = View_main.findViewById(R.id.dns2_2);
        EditText editText_3 = View_main.findViewById(R.id.dns2_3);
        EditText editText_4 = View_main.findViewById(R.id.dns2_4);

        String[] ip = new String[4];
        int count_ip = 0;
        for (int i=0;i<dns2.length();i++) {
            if (dns2.charAt(i) == '.') {
                count_ip++;
                ip[count_ip] = "";
                continue;
            }
            if (ip[count_ip] == null) {
                ip[count_ip] = "";
            }
            ip[count_ip] = ip[count_ip].concat(String.valueOf(dns2.charAt(i)));
        }

        editText_1.setText(ip[0]);
        editText_2.setText(ip[1]);
        editText_3.setText(ip[2]);
        editText_4.setText(ip[3]);
    }

    String getDeviceGateway() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ip route");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        try {
            while(true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) break;
                    if (readLine.contains("default")) {
                        boolean startIp = false;
                        String ip_gateway = "";
                        Log.d(TAG, "gateway " + readLine);
                        for (int i=0;i<readLine.length();i++) {
                            if (!startIp && readLine.charAt(i) >= '0' && readLine.charAt(i) <= '9') {
                                startIp = true;
                            }
                            if (startIp && readLine.charAt(i) == ' ') return ip_gateway;
                            if (startIp) {
                                ip_gateway = ip_gateway.concat(String.valueOf(readLine.charAt(i)));
                            }

                        }
                    }
                    Log.d(TAG, "dns3 " + readLine);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String intToIp(int addr) {
        return  ((addr & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF) + "." +
                ((addr >>>= 8) & 0xFF));
    }

    @SuppressLint("PrivateApi")
    void getDNS(){
        Class<?> SystemProperties = null;
        try {
            SystemProperties = Class.forName("android.os.SystemProperties");
            Method method = SystemProperties.getMethod("get", String.class);
            ArrayList<String> servers = new ArrayList<String>();
            for (String name : new String[] { "net.dns1", "net.dns2", "net.gateway" }) {
                String value = (String) method.invoke(null, name);
                Log.d(TAG, "value " + name + " " + value);
                if (name.equals("net.dns1")) {
                    DNS1 = value;
                } else if (name.equals("net.dns2")) {
                    DNS2 = value;
                }
                //value = (String) method.getName();
                //Log.d(TAG, "value gateway " + value);
                if (value != null && !"".equals(value) && !servers.contains(value))
                    servers.add(value);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public String getDeviceIPAddress(boolean useIPv4) {
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagArgument("boolean", "useIPv4", String.valueOf(useIPv4)));
        int count = 0;
        try {
            List<NetworkInterface> networkInterfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaces) {
                count = 0;
                Log.d(TAG, "displayname " + networkInterface.getDisplayName());
                Log.d(TAG, "interface " + Arrays.toString(new List[]{networkInterface.getInterfaceAddresses()}));
                //Log.d(TAG, "interfaceAddress " + String.valueOf(networkInterface.getNetworkPrefixLength()));
                List<InetAddress> inetAddresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    count++;
                    Log.d(TAG, "inetaddress " + Arrays.toString(new String[]{inetAddress.getHostAddress()}));
                    //Log.d(TAG, "inetaddress ffff" + inetAddress.getCanonicalHostName());
                    if (!inetAddress.isLoopbackAddress()) {
                        String sAddr = inetAddress.getHostAddress().toUpperCase();
                        //String gateway = inetAddress.getHostName().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        //getDeviceSubnetmask(inetAddress, count);
                        if (useIPv4) {
                            if (isIPv4) {
                                //
                                getDeviceSubnetmask(inetAddress, count);
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                // drop ip6 port suffix
                                getDeviceSubnetmask(inetAddress, count);
                                int delim = sAddr.indexOf('%');
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void getDeviceSubnetmask(InetAddress inetAddr, int count){
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "void", "getDeviceSubnetmask") + " - " + TAG_MODIFIED.tagArgument("InetAddress", "inetAddr", String.valueOf(inetAddr)) + " - " + TAG_MODIFIED.tagArgument("int", "count", String.valueOf(count)));
        NetworkInterface temp;
        InetAddress iAddr = null;
        Log.d(TAG, "subnet mask " + "start");
        int _count = 0;
        try {
            temp = NetworkInterface.getByInetAddress(inetAddr);
            Log.d("temp", Arrays.toString(temp.getHardwareAddress()));
            List<InterfaceAddress> addresses = temp.getInterfaceAddresses();
            for (InterfaceAddress inetAddress : addresses){
                _count++;
                if (count == _count){
                    Log.d(TAG, "Subnet mask " + String.valueOf(inetAddress.getNetworkPrefixLength()));
                    subnetByteToIPString(inetAddress.getNetworkPrefixLength());
                }
            }

        } catch (SocketException e) {
            Log.d(TAG, "subnet mask " + "error");
            e.printStackTrace();
        }
        Log.d(TAG, "subnet mask " + "end");
    }

    private void subnetByteToIPString(short networkPrefixLength) {
        Log.d(TAG, TAG_MODIFIED.tagMethod("private", "void", "subnetByteToIPString") + " - " + TAG_MODIFIED.tagArgument("short", "networkPrefixLength", String.valueOf(networkPrefixLength)));
        String ip_string = "";
        int[] ip_bit = new int[32];
        for (int i=0;i<32;i++){
            if (i < networkPrefixLength){
                ip_bit[i] = 1;
            }else {
                ip_bit[i] = 0;
            }
        }
        int value = 0;
        int power_bit = 7;
        for (int i=0;i<32;i++){
            if (i > 0 && i % 8 == 0){
                ip_string += '.';
            }
            value += ip_bit[i] * (pow(2, power_bit));
            power_bit--;
            if (i%8 == 7){
                ip_string += String.valueOf(value);
                value = 0;
                power_bit = 7;
            }
        }
        Subnetmask = ip_string;
    }

    String getIpAddress(){
        EditText editText_1 = View_main.findViewById(R.id.ip_address_1);
        EditText editText_2 = View_main.findViewById(R.id.ip_address_2);
        EditText editText_3 = View_main.findViewById(R.id.ip_address_3);
        EditText editText_4 = View_main.findViewById(R.id.ip_address_4);

        String s_1 = editText_1.getText().toString();
        String s_2 = editText_2.getText().toString();
        String s_3 = editText_3.getText().toString();
        String s_4 = editText_4.getText().toString();

        return s_1 + "." + s_2 + "." + s_3 + "." + s_4;
    }

    String getSubnetmask(){
        EditText editText_1 = View_main.findViewById(R.id.subnet_1);
        EditText editText_2 = View_main.findViewById(R.id.subnet_2);
        EditText editText_3 = View_main.findViewById(R.id.subnet_3);
        EditText editText_4 = View_main.findViewById(R.id.subnet_4);

        String s_1 = editText_1.getText().toString();
        String s_2 = editText_2.getText().toString();
        String s_3 = editText_3.getText().toString();
        String s_4 = editText_4.getText().toString();

        return s_1 + "." + s_2 + "." + s_3 + "." + s_4;
    }

    String getGateway(){
        EditText editText_1 = View_main.findViewById(R.id.gateway_1);
        EditText editText_2 = View_main.findViewById(R.id.gateway_2);
        EditText editText_3 = View_main.findViewById(R.id.gateway_3);
        EditText editText_4 = View_main.findViewById(R.id.gateway_4);

        String s_1 = editText_1.getText().toString();
        String s_2 = editText_2.getText().toString();
        String s_3 = editText_3.getText().toString();
        String s_4 = editText_4.getText().toString();

        return s_1 + "." + s_2 + "." + s_3 + "." + s_4;
    }

    String getDNS1(){
        EditText editText_1 = View_main.findViewById(R.id.dns1_1);
        EditText editText_2 = View_main.findViewById(R.id.dns1_2);
        EditText editText_3 = View_main.findViewById(R.id.dns1_3);
        EditText editText_4 = View_main.findViewById(R.id.dns1_4);

        String s_1 = editText_1.getText().toString();
        String s_2 = editText_2.getText().toString();
        String s_3 = editText_3.getText().toString();
        String s_4 = editText_4.getText().toString();

        return s_1 + "." + s_2 + "." + s_3 + "." + s_4;
    }

    String getDNS2(){
        EditText editText_1 = View_main.findViewById(R.id.dns2_1);
        EditText editText_2 = View_main.findViewById(R.id.dns2_2);
        EditText editText_3 = View_main.findViewById(R.id.dns2_3);
        EditText editText_4 = View_main.findViewById(R.id.dns2_4);

        String s_1 = editText_1.getText().toString();
        String s_2 = editText_2.getText().toString();
        String s_3 = editText_3.getText().toString();
        String s_4 = editText_4.getText().toString();
        if (!Objects.equals(s_1, "")) {
            return s_1 + "." + s_2 + "." + s_3 + "." + s_4;
        } else {
            return "";
        }
    }
}
