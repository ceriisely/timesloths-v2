package com.example.obberertest.timesloth_sm10;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Obberer Test on 11/30/2017.
 */

public class Schedule extends ArrayList<Parcelable> implements Parcelable{
    String Start_time;
    String End_time;
    int Start_timestamp;
    int End_timestamp;
    boolean Status;
    boolean Free;
    String Schedule_detail;

    Schedule(String start_time, String end_time){
        this.Start_time = start_time;
        if(end_time.equals("00:00:00")){
            end_time = "24:00:00";
        }
        this.End_time = end_time;
        this.Start_timestamp = (Integer.parseInt(start_time.substring(0,2))*60) + (Integer.parseInt(start_time.substring(3,5)));
        this.End_timestamp = (Integer.parseInt(end_time.substring(0,2))*60) + (Integer.parseInt(end_time.substring(3,5)));
        this.Status = true;
        this.Free = true;
    }

    Schedule(String start_time, String end_time, JSONObject schedule_detail){
        this.Start_time = start_time;
        if(end_time.equals("00:00:00")){
            end_time = "24:00:00";
        }
        this.End_time = end_time;
        this.Start_timestamp = (Integer.parseInt(start_time.substring(0,2))*60) + (Integer.parseInt(start_time.substring(3,5)));
        this.End_timestamp = (Integer.parseInt(end_time.substring(0,2))*60) + (Integer.parseInt(end_time.substring(3,5)));
        this.Status = true;
        this.Schedule_detail = schedule_detail.toString();
        this.Free = false;
    }

    void updateSchedule(){
        try {
            JSONObject json = new JSONObject(this.Schedule_detail);
            this.Start_time = json.getString("start_date_time") + ":00";
            this.End_time = json.getString("end_date_time") + ":00";
            this.Start_timestamp = (Integer.parseInt(this.Start_time.substring(0,2))*60) + (Integer.parseInt(this.Start_time.substring(3,5)));
            this.End_timestamp = (Integer.parseInt(this.End_time.substring(0,2))*60) + (Integer.parseInt(this.End_time.substring(3,5)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Schedule(Parcel in) {
        Start_time = in.readString();
        End_time = in.readString();
        Start_timestamp = in.readInt();
        End_timestamp = in.readInt();
        Status = in.readByte() != 0;
        Free = in.readByte() != 0;
        Schedule_detail = in.readString();
    }

    public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Start_time);
        parcel.writeString(End_time);
        parcel.writeInt(Start_timestamp);
        parcel.writeInt(End_timestamp);
        parcel.writeByte((byte) (Status ? 1 : 0));
        parcel.writeByte((byte) (Free ? 1 : 0));
        parcel.writeString(Schedule_detail);
    }

    /*Comparator for sorting the list by Student Name*/
    public static Comparator<Schedule> StartTimestampCompare = new Comparator<Schedule>() {

        public int compare(Schedule item1, Schedule item2) {
            int start_time1 = item1.Start_timestamp;
            int start_time2 = item2.Start_timestamp;

            //ascending order
            return start_time1 - start_time2;

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
}
