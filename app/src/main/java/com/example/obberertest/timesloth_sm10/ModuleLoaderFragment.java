package com.example.obberertest.timesloth_sm10;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleLoaderFragment extends Fragment {

    static String TAG = "ModuleLoaderFragment";
    private NetworkSettingActivity NetworkSetting_activity;

    MainActivity Main_activity;
    static View View_main;
    ProgressBar Loader;

    public ModuleLoaderFragment() {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleLoaderFragment"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleLoaderFragment"));
    }


    @SuppressLint("ValidFragment")
    public ModuleLoaderFragment(MainActivity context) {
        // Required empty public constructor
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "", "ModuleLoaderFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));

        Main_activity = context;
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "", "ModuleLoaderFragment") + " - " + TAG_MODIFIED.tagArgument("MainActivity", "context", String.valueOf(context)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.d(TAG, TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("public", "View", "onCreateView"));
        View_main = inflater.inflate(R.layout.fragment_module_loader, container, false);
        Loader = View_main.findViewById(R.id.progress_loader_circular);
        hideLoader();
        return View_main;
    }

    static void hideLoader(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "hideLoader"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "hideLoader"));
        ProgressBar loader = View_main.findViewById(R.id.progress_loader_circular);
        loader.setVisibility(View.INVISIBLE);
    }

    static void showLoader(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "showLoader"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "showLoader"));
        ProgressBar loader = View_main.findViewById(R.id.progress_loader_circular);
        loader.setVisibility(View.VISIBLE);
    }

}
