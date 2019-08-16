package com.tec.aplicacionapi.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetworkStatus {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetworkInfo =
                connectivityManager.getActiveNetworkInfo();
        return activityNetworkInfo != null && activityNetworkInfo.isConnected();

    }

}
