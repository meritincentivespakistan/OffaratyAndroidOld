package com.riyadhbank.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.VersionCheckService;
import com.riyadhbank.Async.WelcomeMessageService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements Interface.OnVersionCheck {

    private int TIME_OUT = 2000;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences shared = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");

        SharedPreferences pref = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = pref.getString(Constants.LanguageCode, "");

        if (GlobalClass.languageCode.equals("") || GlobalClass.languageCode.equals("en")) {
            GlobalClass.changeLanguage(this, "en");
        } else {
            GlobalClass.changeLanguage(this, "ar");
        }

        if (GlobalClass.isNetworkConnected(this)) {
            new VersionCheckService(this, SplashActivity.this).execute();
        } else {
            Toast.makeText(this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onVersionCheck(JSONObject jsonObject) {
        try {

            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            if (jsonObject.getString("androidversion").equals("1") &&
                    !jsonObject.getString("androidforce").equals(version)) {
                versionDialog();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (UserId.equals("")) {
                            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            i.putExtra("isFrom", GlobalClass.HOME);
                            startActivity(i);
                            finish();
                        }
                    }
                }, TIME_OUT);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void versionDialog() {

        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.version_dialog);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView txtWelcomeTitle = (TextView) dialog.findViewById(R.id.txtWelcomeTitle);
        TextView txtWelcomeText = (TextView) dialog.findViewById(R.id.txtWelcomeText);
        txtWelcomeTitle.setText(getResources().getString(R.string.new_version));
        txtWelcomeText.setText(getResources().getString(R.string.update_available));

        TextView btnUpdate = (TextView) dialog.findViewById(R.id.btnUpdate);
        TextView btnCancel = (TextView) dialog.findViewById(R.id.btnCancel);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateIntent = new Intent("android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                startActivity(updateIntent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            }
        }
    }

}
