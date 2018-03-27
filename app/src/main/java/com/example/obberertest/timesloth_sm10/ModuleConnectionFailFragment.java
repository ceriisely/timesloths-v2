package com.example.obberertest.timesloth_sm10;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModuleConnectionFailFragment extends Fragment {

    private static View View_main;
    private static View Popup;

    public ModuleConnectionFailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View_main = inflater.inflate(R.layout.fragment_module_connection_fail, container, false);
        Popup = View_main.findViewById(R.id.popup_box);
        hideLoader();
        return View_main;
    }

    static void hideLoader(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "hideLoader"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "hideLoader"));
        Popup.setVisibility(View.INVISIBLE);
    }

    static void showLoader(){
        //Log.d(TAG, TAG_MODIFIED.tagMethod("", "void", "showLoader"));
        //SiteData.writeFile(Main_activity, TAG + " | " + TAG_MODIFIED.tagMethod("", "void", "showLoader"));
        Popup.setVisibility(View.VISIBLE);
    }
}
