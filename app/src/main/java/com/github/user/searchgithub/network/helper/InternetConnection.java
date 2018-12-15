package com.github.user.searchgithub.network.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.github.user.searchgithub.App;


public class InternetConnection {

    public static boolean checkConnection(@NonNull Context context) {
//        return ((ConnectivityManager) context.getSystemService
//                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        ConnectivityManager
                cm = (ConnectivityManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }
}
