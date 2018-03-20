package com.example.obberertest.timesloth_sm10;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Obberer Test on 10/29/2017.
 */

class SiteData extends ArrayList<Parcelable> implements Parcelable{
    String Server_address;
    String Version = "Version 2.0 Build 1802121251";
    String Server_date; // 2016-08-13
    int Room_id;
    String Path;
    static String Password;
    String UnitTime;
    int TimeRefresh;
    static String Language;
    private static String LogFilePath = "/Android/data/com.example.obberertest.bankrecord/files";
    private static String LogFileName = "/log.txt";

    public SiteData(ConnectActivity connectActivity, String server_address, int room_id, String path, String language, int time_refresh, String unit_time) {
        this.Server_address = server_address;
        this.Room_id = room_id;
        this.Path = path;
        Language = language;
        this.TimeRefresh = time_refresh;
        this.UnitTime = unit_time;
    }

    private SiteData(Parcel in) {
        Server_address = in.readString();
        Server_date = in.readString();
        Path = in.readString();
        Password = in.readString();
        UnitTime = in.readString();
        Room_id = in.readInt();
        TimeRefresh = in.readInt();
    }

    public static final Creator<SiteData> CREATOR = new Creator<SiteData>() {
        @Override
        public SiteData createFromParcel(Parcel in) {
            return new SiteData(in);
        }

        @Override
        public SiteData[] newArray(int size) {
            return new SiteData[size];
        }
    };

    void setServer_date(String date){
        this.Server_date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Server_address);
        parcel.writeString(Server_date);
        parcel.writeString(Path);
        parcel.writeString(Password);
        parcel.writeString(UnitTime);
        parcel.writeInt(Room_id);
        parcel.writeInt(TimeRefresh);
    }

    static int getTimeDeivceTimestamp(){
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String Date = simpledateformat.format(calander.getTime());

        int hr = Integer.parseInt(Date.substring(11, 13));
        int min = Integer.parseInt(Date.substring(14, 16));

        return (hr * 60) + min;
        //return (9*60) + 0;
    }

    static String getDateDeviceTimestamp(String type){
        Calendar calander = Calendar.getInstance();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String Date = simpledateformat.format(calander.getTime());
        String date = null;
        switch (type){
            case "dd mmm yyyy": {
                date = Date.substring(0, 2) + " " + getStringMonth(Date.substring(3, 5)) + " " + Date.substring(6, 10);
                break;
            }
            default:break;
        }

        return date;
    }

    static String getStringMonth(String month){
        int m = Integer.parseInt(month);
        switch (m){
            case 1: {
                return Objects.equals(Language, "en") ? "JAN" : "ม.ค.";
            }
            case 2: {
                return Objects.equals(Language, "en") ? "FEB" : "ก.พ.";
            }
            case 3: {
                return Objects.equals(Language, "en") ? "MAR" : "มี.ค.";
            }
            case 4: {
                return Objects.equals(Language, "en") ? "APR" : "เม.ย.";
            }
            case 5: {
                return Objects.equals(Language, "en") ? "MAY" : "พ.ค.";
            }
            case 6: {
                return Objects.equals(Language, "en") ? "JUN" : "มิ.ย.";
            }
            case 7: {
                return Objects.equals(Language, "en") ? "JUL" : "ก.ค.";
            }
            case 8: {
                return Objects.equals(Language, "en") ? "AUG" : "ส.ค.";
            }
            case 9: {
                return Objects.equals(Language, "en") ? "SEP" : "ก.ย.";
            }
            case 10: {
                return Objects.equals(Language, "en") ? "OCT" : "ต.ค.";
            }
            case 11: {
                return Objects.equals(Language, "en") ? "NOV" : "พ.ย.";
            }
            case 12: {
                return Objects.equals(Language, "en") ? "DEC" : "ธ.ค.";
            }
            default: break;
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    String getTimeString(int timestamp){
        switch (this.UnitTime){
            case "12": {
                int hr = timestamp / 60;
                int min = timestamp % 60;
                if(hr == 12){
                    return String.format("%02d:%02d PM", 12, min);
                }else if(hr > 12){
                    return String.format("%02d:%02d PM", hr%12, min);
                }else if(hr < 12){
                    return String.format("%02d:%02d AM", hr, min);
                }else if(hr == 24 || hr == 0){
                    return String.format("%02d:%02d AM", 12, min);
                }
                break;
            }
            case "24": {
                int hr = timestamp / 60;
                int min = timestamp % 60;
                return String.format("%02d:%02d", hr, min);
            }
            default: break;
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    String getTimeString(int hr, int min){
        switch (this.UnitTime){
            case "12": {
                if(hr == 12){
                    return String.format("%02d:%02d PM", 12, min);
                }else if(hr > 12){
                    return String.format("%02d:%02d PM", hr%12, min);
                }else if(hr < 12){
                    return String.format("%02d:%02d AM", hr, min);
                }else if(hr == 24 || hr == 0){
                    return String.format("%02d:%02d AM", 12, min);
                }
                break;
            }
            case "24": {
                return String.format("%02d:%02d", hr, min);
            }
            default: break;
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    String getTimeString(int value, String type){
        if(type.equals("hr")) {
            switch (this.UnitTime) {
                case "12": {
                    if (value == 12) {
                        return String.format("%d PM", 12);
                    } else if (value > 12) {
                        return String.format("%d PM", value%12);
                    } else if (value < 12) {
                        return String.format("%d AM", value);
                    } else if (value == 24 || value == 0) {
                        return String.format("%d AM", 12);
                    }
                    break;
                }
                case "24": {
                    return String.format("%d", value);
                }
                default:
                    break;
            }
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    String getTimeString24(int timestamp){
        String hr = String.format("%02d",timestamp/60);
        String min = String.format("%02d",timestamp%60);
        return hr + ":" + min;
    }

    String getTimeRemain(Context context, int start_timestamp, int end_timestamp){
        int remain = end_timestamp - start_timestamp;
        String time = "";
        if(remain < 0) {
            remain = remain*-1;
        }
        if ((remain / 60) > 0){
            time = time + (String.valueOf(remain/60) + " " + context.getResources().getString(R.string.hr) + " ");
        }
        if (remain%60 > 1) {
            time = time + (String.valueOf(remain % 60) + " " + context.getResources().getString(R.string.minutes));
        }else if (remain%60 == 1){
            time = time + (String.valueOf(remain%60) + " " + context.getResources().getString(R.string.minute));
        }
        return time;
    }

    String getTimeRemain(String start_time, String end_time){
        int start_timestamp = (Integer.parseInt(start_time.substring(0,2)) * 60) + Integer.parseInt(start_time.substring(3,5));
        int end_timestamp = (Integer.parseInt(end_time.substring(0,2)) * 60) + Integer.parseInt(end_time.substring(3,5));
        int remain = end_timestamp - start_timestamp;
        String time = "";
        if(remain < 0) {
            remain = remain*-1;
        }
        if ((remain / 60) > 0){
            time = time + (String.valueOf(remain/60) + " Hr ");
        }
        if (remain%60 > 1) {
            time = time + (String.valueOf(remain % 60) + " Minutes");
        }else if (remain%60 == 1){
            time = time + (String.valueOf(remain%60) + " Minute");
        }
        Log.d("getTimeRemain", time);
        return time;
    }

    static int getTimestamp(String time){
        int hr = Integer.parseInt(time.substring(0,2));
        int min = Integer.parseInt(time.substring(3,5));
        return hr * 60 + min;
    }

    String getBaseServer(){
        String server = this.Server_address;
        StringBuilder base_server = new StringBuilder();
        for (int i=0;i<server.length();i++){
            if(server.charAt(i) == '/'){
                return base_server.toString();
            }else {
                base_server.append(server.charAt(i));
            }
        }
        return "";
    }

    static void playSound(Context context, String i){
        MediaPlayer _a = null;
        switch (i) {
            case "ok"
                    : _a = MediaPlayer.create(context, R.raw.ok);
                break;
            case "cancel"
                    : _a = MediaPlayer.create(context, R.raw.cancel);
                break;
            case "allroom"
                    : _a = MediaPlayer.create(context, R.raw.allroom);
                break;
            case "transition"
                    : _a = MediaPlayer.create(context, R.raw.transition);
                break;
            case "popup"
                    : _a = MediaPlayer.create(context, R.raw.popup);
                break;
            case "touch"
                    : _a = MediaPlayer.create(context, R.raw.touch);
                break;
            case "error"
                    : _a = MediaPlayer.create(context, R.raw.error);
                break;
            default:break;
        }
        if (_a != null) {
            _a.start();
        }
    }

    static String getDeviceIPAddress(boolean useIPv4) {
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagArgument("boolean", "useIPv4", String.valueOf(useIPv4)));
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
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                // drop ip6 port suffix
                                int delim = sAddr.indexOf('%');
                                //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf(delim < 0 ? sAddr : sAddr.substring(0, delim))));
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "String", "getDeviceIPAddress") + " - " + TAG_MODIFIED.tagReturn("String", String.valueOf("")));
        return "";
    }

    public static void stopThread(Thread myService){
        if(myService != null){
            myService.interrupt();
            myService = null;
        }
    }

    static void writeFile(Context context, String data){
        File backupPath = Environment.getExternalStorageDirectory();

        backupPath = new File(backupPath.getPath() + LogFilePath);
        FileOutputStream fos;

        Time now = new Time();
        now.setToNow();

        try {
            if(!backupPath.exists()) {
                backupPath.mkdirs();
                fos = new FileOutputStream(backupPath.getPath() + LogFileName, true);
                fos.write(("Created on " + now.toString().subSequence(0, 15)
                        + "\nDevice name: " + Build.MODEL
                        + "\nAndroid Version " + Build.VERSION.SDK_INT
                        + "\n")
                        .getBytes());
                fos.close();
            }
            fos = new FileOutputStream(backupPath.getPath() + LogFileName, true); // append true
            String text = now.toString().subSequence(0, 15) + ": " + data;
            fos.write(text.getBytes());
            fos.write("\n".getBytes());
            fos.flush();
            fos.close();

        } catch (IOException e) {

            e.printStackTrace();

            AlertDialog.Builder delmessagebuilder = new AlertDialog.Builder(context);

            delmessagebuilder.setCancelable(false);

            delmessagebuilder.setMessage("File Access Error");

            delmessagebuilder.setNeutralButton("Okay", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });

            delmessagebuilder.create().show();
        }
    }
}
