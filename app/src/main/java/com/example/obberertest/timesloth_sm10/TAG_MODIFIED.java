package com.example.obberertest.timesloth_sm10;

import android.content.Context;

/**
 * Created by Obberer Test on 2/10/2018.
 */

class TAG_MODIFIED {

    static String tagMethod(String modifier, String return_type, String name){
        return "method [" + modifier + "] [" + return_type + "] " + name;
    }

    static String tagDevice(Context context){
        return context.getPackageName();
    }

    static String tagArgument(String type, String name, String value){
        return "argument [name=" + name + "] [type=" + type + "] [value=" + value + "]";
    }

    static String tagReturn(String type, String value){
        return "return [type=" + type + "] [value=" + value + "]";
    }

    static String tagOnClick(String id, String view){
        return "onCLick [id=" + id + "] [view=" + view + "]";
    }

    static String tagFeed(String name){
        return "Feed [name=" + name + "]";
    }

}
