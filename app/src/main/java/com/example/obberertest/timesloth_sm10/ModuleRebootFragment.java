package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleRebootFragment extends Fragment {

    private String TAG = "ModuleRebootFragment";
    private MainActivity Main_activity;
    private View View_main;

    public ModuleRebootFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ModuleRebootFragment(MainActivity mainActivity){
        Main_activity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View_main = inflater.inflate(R.layout.fragment_module_reboot, container, false);
        bindView();
        return View_main;
    }

    private void bindView(){
        Button btn_yes = View_main.findViewById(R.id.btn_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "ok");
                Process proc = null;
                try {
                    proc = Runtime.getRuntime().exec(new String[]{ "su", "-c", "reboot" });
                } catch (IOException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | btn_yes " + e.getMessage());
                }
                try {
                    if (proc != null) {
                        proc.waitFor();
                    }
                } catch (InterruptedException | NullPointerException e) {
                    e.printStackTrace();
                    SiteData.writeFile(Main_activity, TAG + " | btn_yes " + e.getMessage());
                }
            }
        });

        Button btn_no = View_main.findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiteData.playSound(Main_activity, "cancel");
                Main_activity.getFragmentManager().beginTransaction().remove(ModuleRebootFragment.this).commit();
            }
        });
    }
}
