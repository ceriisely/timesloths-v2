package com.example.obberertest.timesloth_sm10;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by Obberer Test on 10/29/2017.
 */

public class Room extends ArrayList<Parcelable> implements Parcelable{
    int Id;
    String Name;
    String Image_path;
    String Background_image_path;
    String Floor_plan_image_path;
    int Start_time;
    int End_time;
    int Slot_size;
    boolean Status;
    int RoomStatus;
    ArrayList<Schedule> Slots = new ArrayList<Schedule>();
    ArrayList<Schedule> RoomSchedule = new ArrayList<Schedule>(); // "00:00:00-08:00:00"
    int RoomIdStatus;

    public Room(JSONObject room){

        try {
            JSONObject room_data = room.getJSONObject("roomData");
            this.Id = room_data.getInt("resourceId");
            this.Name = room_data.getString("name");
            this.Image_path = room_data.getString("image_name_path");
            this.Background_image_path = room_data.getString("background_image_path");
            this.Floor_plan_image_path = room_data.getString("floor_plan_image_path");
            this.RoomStatus = 0; //Available;
            this.Slot_size = room_data.getInt("slot_size");
            if(Slot_size == 0){
                Slot_size = 30;
            }
            for(int i=0;i<(24*60);i+=this.Slot_size){
                String start_time = String.format("%02d:%02d:00",(int) (i / 60),(int) (i%60));
                String end_time = String.format("%02d:%02d:00",(int) ((i + this.Slot_size) / 60),(int) ((i + this.Slot_size)%60));
                Slots.add(new Schedule(start_time, end_time));
            }
            JSONArray block_slots = room_data.getJSONArray("blocked_slots");
            for(int i=0;i<block_slots.length();i++){
                JSONObject block = block_slots.getJSONObject(i);
                blockSlot(block.getString("start_time"), block.getString("end_time"));
            }
            JSONArray room_schedule = room.getJSONArray("RoomSchedule");
            for(int i=0;i<room_schedule.length();i++){
                JSONObject schedule = room_schedule.getJSONObject(i);
                addSchedule(schedule);
            }
            int count_status = 0;
            boolean check_start_time = false;
            boolean check_end_time = false;
            for(int i=0;i<Slots.size();i++){
                if(Slots.get(i).Status && !Slots.get(i).Free){
                    //Log.d(this.Name + " Schedule",Slots.get(i).Start_time + "-" + Slots.get(i).End_time);
                }
                if(!Slots.get(i).Status){
                    count_status++;
                }
                if (!check_start_time && Slots.get(i).Status){
                    check_start_time = true;
                    Start_time = Slots.get(i).Start_timestamp;
                }
                if (!check_end_time && check_start_time && Slots.get(i).Status){
                    End_time = Slots.get(i).End_timestamp;
                }
                if (check_start_time && !Slots.get(i).Status){
                    check_end_time = true;
                }
            }
            if(count_status == Slots.size()){
                this.Status = false;
            }else {
                this.Status = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Room(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Image_path = in.readString();
        Background_image_path = in.readString();
        Floor_plan_image_path = in.readString();
        Start_time = in.readInt();
        End_time = in.readInt();
        Slot_size = in.readInt();
        Status = in.readByte() != 0;
        in.readTypedList(Slots, Schedule.CREATOR);
        in.readTypedList(RoomSchedule, Schedule.CREATOR);
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public Room() {

    }

    void blockSlot(String start_time, String end_time){
        if(end_time.equals("00:00:00")){
            end_time = "24:00:00";
        }
        int start_timestamp = (Integer.parseInt(start_time.substring(0,2))*60) + (Integer.parseInt(start_time.substring(3,5)));
        int end_timestamp = (Integer.parseInt(end_time.substring(0,2))*60) + (Integer.parseInt(end_time.substring(3,5)));
        for(int j=0;j<Slots.size();j++){
            if(Slots.get(j).Start_timestamp >= start_timestamp && Slots.get(j).End_timestamp <= end_timestamp){
                Slots.get(j).Status = false;
                Slots.get(j).Free = true;
            }
        }
    }

    void addSchedule(JSONObject schedule){
        String start_time = null;
        String end_time = null;
        try {
            start_time = (schedule.getString("start_date_time")) + ":00";
            end_time = (schedule.getString("end_date_time")) + ":00";
            Log.d(this.Name + " Schedule",start_time + "-" + end_time);
            RoomSchedule.add(new Schedule(start_time, end_time, schedule));
            Collections.sort(RoomSchedule, Schedule.StartTimestampCompare);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i=0;i<Slots.size();i++){
            if(start_time.equals(Slots.get(i).Start_time)){
                for(int j=i;j<Slots.size();j++){
                    Slots.get(j).Schedule_detail = schedule.toString();
                    Slots.get(j).Free = false;
                    if(end_time.equals(Slots.get(j).End_time)){
                        return;
                    }
                }
            }
        }
    }

    void updateSlot(){
        ArrayList<Integer> list_slot = new ArrayList<Integer>();
        int room_schedule_size = this.RoomSchedule.size();
        int slot_size = this.Slots.size();
        for (int i=0;i<room_schedule_size;i++){
            if(i < this.RoomSchedule.size()){
                Schedule room_schedule = this.RoomSchedule.get(i);
                for (int j=0;j<slot_size;j++){
                    Schedule schedule = this.Slots.get(j);
                    if (room_schedule.Start_timestamp <= schedule.Start_timestamp && room_schedule.End_timestamp >= schedule.End_timestamp){
                        list_slot.add(j);
                        //this.Slots.get(i).Free = true;
                    }
                }
            }
        }
        for (int i=0;i<this.Slots.size();i++){
            for (int j=0;j<list_slot.size();j++){
                int index = list_slot.get(j);
                if(i == index){
                    this.Slots.get(i).Free = false;
                    break;
                }else if(j == list_slot.size() - 1){
                    this.Slots.get(i).Free = true;
                }
            }
        }
    }

    Schedule getSlot_fromTime(String start_time, String end_time){
        for(int i=0;i<Slots.size();i++){
            if(start_time.equals(Slots.get(i).Start_time) && end_time.equals(Slots.get(i).End_time)){
                return Slots.get(i);
            }
        }
        return null;
    }

    public static void changeRoomName(View view_main){
        view_main.findViewById(R.id.Room_name);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Id);
        parcel.writeString(Name);
        parcel.writeString(Image_path);
        parcel.writeString(Background_image_path);
        parcel.writeString(Floor_plan_image_path);
        parcel.writeInt(Start_time);
        parcel.writeInt(End_time);
        parcel.writeInt(Slot_size);
        parcel.writeByte((byte) (Status ? 1 : 0));
        parcel.writeTypedList(Slots);
        parcel.writeTypedList(RoomSchedule);
    }

    int getIndexScheduleFromIdStatus(){
        if (this.RoomIdStatus == 0){
            return -1;
        }else {
            for (int i = 0; i < this.RoomSchedule.size(); i++) {
                Schedule schedule = this.RoomSchedule.get(i);
                try {
                    JSONObject json = new JSONObject(schedule.Schedule_detail);
                    if (json.getInt("reservation_instance_id") == this.RoomIdStatus) {
                        return i;
                    }
                } catch (JSONException e) {

                }
            }
        }
        return -1;
    }
}
