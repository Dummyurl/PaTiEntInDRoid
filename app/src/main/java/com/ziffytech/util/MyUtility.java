package com.ziffytech.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by Karshima on 1/2/2017.
 */

public class MyUtility {
    public static String INTERNET_ERROR = "Check Internet Connection";
    public static String DATA_ERROR = "Failed to get Data";

    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    public static boolean isConnected(Context context) {

        if (context != null) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = cm.getActiveNetworkInfo();

            if (netinfo != null && netinfo.isConnectedOrConnecting()) {
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                    return true;
                else return false;
            } else
                return false;
        } else return false;
    }

    public static boolean isConnectedd(Context context, View view) {

        if (context != null) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = cm.getActiveNetworkInfo();

            if (netinfo != null && netinfo.isConnectedOrConnecting()) {
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) {
                    view.setVisibility(View.VISIBLE);
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    public static void internetProblem(View parent) {

      /*  parent.setVisibility(View.GONE);
        checkconn.setVisibility(View.VISIBLE);*/

        Snackbar.make(parent, INTERNET_ERROR, Snackbar.LENGTH_LONG).show();
    }

    public static void internetProblemm(View parent, View checkconn) {

            parent.setVisibility(View.GONE);
            checkconn.setVisibility(View.VISIBLE);



        //Snackbar.make(parent,INTERNET_ERROR, Snackbar.LENGTH_LONG).show();
    }

    public static void dataNotFound(View parent, View notFound) {

        parent.setVisibility(View.GONE);
        notFound.setVisibility(View.VISIBLE);

        //Snackbar.make(parent,INTERNET_ERROR, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnack(View view, String msg) {

        //  Snackbar.make(layout,"Hi",Snackbar.LENGTH_SHORT).show();

        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        //  Snackbar.make(view, "Snackbar", Snackbar.LENGTH_LONG)
        //.setAction("Action", null).show();

    }


    public static void showToast(String msg, Context context) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(View view, Context context) {

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showAlertMessage(Context context,String msg){

        AlertDialog.Builder ad=new AlertDialog.Builder(context);
        ad.setMessage(msg);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog=ad.create();
        dialog.show();
    }

}
