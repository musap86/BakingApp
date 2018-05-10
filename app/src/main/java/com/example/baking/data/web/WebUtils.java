package com.example.baking.data.web;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class WebUtils {
    /**
     * Returns if the user is online or not.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (manager != null) {
            netInfo = manager.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
