package com.bezzy.Ui.View.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.bezzy.Ui.View.activity.LoginActivity;
import com.bezzy_application.R;

import org.json.JSONArray;

public class Utility {

    private static AlertDialog topupDialog;

    public static boolean internet_check(Context context){
        //Test for Connection
        ConnectivityManager cmgr=(ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cmgr.getActiveNetworkInfo()!= null && cmgr.getActiveNetworkInfo().isAvailable() && cmgr.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void setLogin(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login", type);
        editor.apply();
    }

    public static String getLogin(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("login", "");
    }

    public static void setSocial(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("google", type);
        editor.apply();
    }

    public static String getSocial(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("google", "");
    }

    public static void setOtpScreen(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("otpscreen", type);
        editor.apply();
    }

    public static String getOtpScreen(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("otpscreen", "");
    }

    public static void setUserId(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId", type);
        editor.apply();
    }

    public static String getUserId(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("userId", "");
    }


    public static void setNotificationStatus(Context mContext, String type) {
        SharedPreferences preferences = mContext.getSharedPreferences("Bezzy", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("status", type);
        editor.apply();
    }

    public static String getNotificationStatus(Context mContext) {
        SharedPreferences memIdPreferences = mContext.getSharedPreferences("Bezzy", 0); // 0 - for private mode
        return memIdPreferences.getString("status", "0");
    }

    public static void displayLoader(Context context){

        customProgressDialog(context);

    }

    public static void hideLoader(Context context){

        customProgressDialog2(context);

    }

    public static void customProgressDialog(Context context){

        ProgressBar imageView;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View v= LayoutInflater.from(context).inflate(R.layout.popup_dialog,null);
        imageView = v.findViewById(R.id.logo_bezzy);

        builder.setView(v);
        builder.setCancelable(false);
        topupDialog=builder.create();
        topupDialog.show();
        topupDialog.getWindow().setLayout(200, 200);
    }

    public static void customProgressDialog2(Context context){
        topupDialog.dismiss();
    }


}
