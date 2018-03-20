package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ModuleFindRoomDetailFragment extends Fragment {

    static String TAG = "ModuleFindRoomDetailFragment";

    private MainActivity Main_activity;
    Room MainRoom;
    private View View_main;

    public ModuleFindRoomDetailFragment(MainActivity context, Room room) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleFindRoomDetailFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)) + " - " + TAG_MODIFIED.tagArgument("Room", "room", String.valueOf(room)));
        Main_activity = context;
        MainRoom = room;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleFindRoomDetailFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)) + " - " + TAG_MODIFIED.tagArgument("Room", "room", String.valueOf(room)));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_find_room_detail, container, false);
        changeStatus();
        return View_main;
    }

    @SuppressLint("SetTextI18n")
    void changeStatus(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "changeStatus"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "changeStatus"));
        ImageView bar_status = View_main.findViewById(R.id.image_bar_status);
        TextView text_room_name = View_main.findViewById(R.id.text_room_name);
        TextView text_status = View_main.findViewById(R.id.text_status);
        TextView text_status_time = View_main.findViewById(R.id.text_status_time);

        text_room_name.setText(MainRoom.Name);

        int index = MainRoom.getIndexScheduleFromIdStatus();
        //Log.d(MainRoom.Name, String.valueOf(MainRoom.RoomStatus));
        //SiteData.writeFile(Main_activity, TAG + " | " + MainRoom.Name + String.valueOf(MainRoom.RoomStatus));
        //Log.d(Main_activity.Main_Room.Name, String.valueOf(Main_activity.Main_Room.RoomStatus));
        //SiteData.writeFile(Main_activity, TAG + " | " + Main_activity.Main_Room.Name + String.valueOf(Main_activity.Main_Room.RoomStatus));
        if (MainRoom.Status) {
            text_room_name.setAlpha(1);
            switch (MainRoom.RoomStatus) {
                case 0: { //available
                    bar_status.setBackgroundColor(Color.parseColor("#53FE02"));
                    text_status.setText(getResources().getString(R.string.available));
                    if (index == -1) {
                        text_status_time.setText(getResources().getString(R.string.until_the_end_of_the_day));
                    } else {
                        Schedule schedule = MainRoom.RoomSchedule.get(index);
                        text_status_time.setText(getResources().getString(R.string.until) + " " + Main_activity.Site_data.getTimeString(schedule.Start_timestamp));
                    }
                    text_status_time.setTextColor(Color.parseColor("#53FE02"));
                    break;
                }
                case 1: { //occupied
                    bar_status.setBackgroundColor(Color.parseColor("#EA0100"));
                    text_status.setText(getResources().getString(R.string.busy));
                    Schedule schedule = MainRoom.RoomSchedule.get(index);
                    for (int i = 0; i < MainRoom.Slots.size(); i++) {
                        Schedule slot = MainRoom.Slots.get(i);
                        if (slot.Status && slot.Free && slot.Start_timestamp >= schedule.End_timestamp) {
                            text_status_time.setText(getResources().getString(R.string.next_available) + " " + Main_activity.Site_data.getTimeString(slot.Start_timestamp));
                            break;
                        }
                        text_status_time.setText(getResources().getString(R.string.not_available_all_day));
                    }
                    break;
                }
                case 2: { //busy
                    bar_status.setBackgroundColor(Color.parseColor("#EA0100"));
                    text_status.setText(getResources().getString(R.string.busy));
                    Schedule schedule = MainRoom.RoomSchedule.get(index);
                    for (int i = 0; i < MainRoom.Slots.size(); i++) {
                        Schedule slot = MainRoom.Slots.get(i);
                        if (slot.Status && slot.Free && slot.Start_timestamp >= schedule.End_timestamp) {
                            text_status_time.setText(getResources().getString(R.string.next_available) + " " + Main_activity.Site_data.getTimeString(slot.Start_timestamp));
                            break;
                        }
                        text_status_time.setText(getResources().getString(R.string.not_available_all_day));
                    }
                    break;
                }
                default:
                    break;
            }
        } else {
            text_room_name.setAlpha(0.5f);
            bar_status.setBackgroundColor(Color.parseColor("#9b9b9b"));
            text_status.setText("");
            text_status_time.setText("");
        }
    }

}
