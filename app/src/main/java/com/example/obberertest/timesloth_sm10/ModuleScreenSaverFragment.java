package com.example.obberertest.timesloth_sm10;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleScreenSaverFragment extends Fragment {

    static String TAG = "ModuleScreenSaverFragment";
    private View View_main;

    public ModuleScreenSaverFragment() {
        // Required empty public constructor
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleScreenSaverFragment"));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_screen_saver, container, false);
        return View_main;
    }

}
