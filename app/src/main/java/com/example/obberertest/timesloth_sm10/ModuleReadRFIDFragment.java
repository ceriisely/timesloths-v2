package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleReadRFIDFragment extends Fragment {

    private String TAG = "ModuleReadRFIDFragment";
    private MainActivity Main_activity;
    static View View_main;
    private Thread rfid_thread;
    static String RFID;

    public ModuleReadRFIDFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ModuleReadRFIDFragment(MainActivity mainActivity) {
        Main_activity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View_main = inflater.inflate(R.layout.fragment_module_read_rfid, container, false);
        bindView();
        EditText input_rfid = View_main.findViewById(R.id.edittext_input_rfid);
        runThread();
        return View_main;
    }

    private void runThread(){
        final EditText input_rfid = View_main.findViewById(R.id.edittext_input_rfid);
        rfid_thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        Main_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FrameLayout rootView_back = (FrameLayout) Main_activity.findViewById(R.id.fragment_main_activity_container_back);
                                    FrameLayout rootView_front = (FrameLayout) Main_activity.findViewById(R.id.fragment_main_activity_container_front);
                                    int childViewCount_back = rootView_back.getChildCount();
                                    int childViewCount_front = rootView_front.getChildCount();
                                    if (childViewCount_back == 1 && childViewCount_front == 1) {
                                        input_rfid.requestFocus();
                                        try {
                                            InputMethodManager imm = (InputMethodManager) Main_activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            if (imm != null) {
                                                imm.hideSoftInputFromWindow(input_rfid.getWindowToken(), 0);
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                            SiteData.writeFile(Main_activity, TAG + " | rfid_thread " + e.getMessage());
                                        }
                                    } else {
                                        input_rfid.clearFocus();
                                    }
//                                    for (int i=0; i<childViewCount;i++){
//                                        View workWithMe = rootView.getChildAt(i);
//                                    }
                                } catch (ClassCastException | NullPointerException e){
                                    //Not a viewGroup here
                                    SiteData.writeFile(Main_activity, TAG + " | rfid_thread " + e.getMessage());
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | rfid_thread " + e.getMessage());
                    //Connection_fail++;
                }
            }
        };
        rfid_thread.start();
    }

    private void bindView() {
        EditText input_rfid = View_main.findViewById(R.id.edittext_input_rfid);

        input_rfid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.d(TAG, String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Log.d(TAG, "afterTextChanged " + editable.toString());
                //editable.clear();
                if (editable.toString().length() == 10){
                    Log.d(TAG, "afterTextChanged " + editable.toString());
                    RFID = editable.toString();
                    Main_activity.manage_main_fragment.new FeedAsynTaskGetUserDetailByRFID().execute();
                    editable.clear();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SiteData.stopThread(rfid_thread);
    }
}
