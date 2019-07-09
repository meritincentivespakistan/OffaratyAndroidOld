package com.riyadhbank.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riyadhbank.Async.ChangePasswordService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements Interface.OnChangePassword {

    ImageView back;
    TextView txtTitle;
    LinearLayout chandLout,lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_profile,img_logout;
    TextView txt_profile,txt_logout, btnChangePassword;
    EditText edtNewPass, edtConPass;
    String isFrom, UserId,  ConPass, Password;
    LinearLayout footerMainLout;
    FrameLayout footerChand;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        FindById();

        SharedPreferences prefs = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = prefs.getString(Constants.LanguageCode, "");

        isFrom = getIntent().getStringExtra("isFrom");

        if (isFrom.equals("OTPVerification")) {
            UserId = getIntent().getStringExtra(Constants.userid);
            footerMainLout.setVisibility(View.GONE);
            footerChand.setVisibility(View.GONE);
        } else {
            SharedPreferences shared = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
            Password = shared.getString(Constants.password, "");
            UserId = shared.getString(Constants.userid, "");
            footerMainLout.setVisibility(View.VISIBLE);
            footerChand.setVisibility(View.VISIBLE);
        }

        if (GlobalClass.languageCode.equals("en")) {
            edtNewPass.setGravity(Gravity.LEFT);
            edtConPass.setGravity(Gravity.LEFT);
        } else {
            edtNewPass.setGravity(Gravity.RIGHT);
            edtConPass.setGravity(Gravity.RIGHT);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtTitle.setText(getResources().getString(R.string.change_password));

        img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_select));
        txt_profile.setTextColor(getResources().getColor(R.color.themeColor));

        clickMenuType();

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Password = edtNewPass.getText().toString().trim();
                ConPass = edtConPass.getText().toString().trim();

                if (Password.equals("")) {
                    GlobalClass.showToastMessage(ChangePasswordActivity.this, getResources().getString(R.string.insert_password));
                } else if (ConPass.equals("")) {
                    GlobalClass.showToastMessage(ChangePasswordActivity.this, getResources().getString(R.string.insert_con_password));
                } else if (!Password.equals(ConPass)) {
                    GlobalClass.showToastMessage(ChangePasswordActivity.this, getResources().getString(R.string.con_password_validation));
                } else {
                    if (GlobalClass.isNetworkConnected(ChangePasswordActivity.this)) {
                        new ChangePasswordService(ChangePasswordActivity.this, ChangePasswordActivity.this,
                                Password, UserId).execute();
                    } else {
                        GlobalClass.showToastMessage(ChangePasswordActivity.this, getResources().getString(R.string.internet_msg));
                    }
                }

            }
        });

    }

    @Override
    public void onChangePassword(JSONObject jsonObject) {
        try {
            if (isFrom.equals("OTPVerification")) {
                Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                edtNewPass.setText("");
                edtConPass.setText("");
                GlobalClass.showToastMessage(this, jsonObject.getString("msg").toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FindById() {
        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        img_profile = (ImageView) findViewById(R.id.img_profile);
        txt_profile = (TextView) findViewById(R.id.txt_profile);

        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        btnChangePassword = (TextView) findViewById(R.id.btnChangePassword);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

        edtNewPass = (EditText) findViewById(R.id.edtNewPass);
        edtConPass = (EditText) findViewById(R.id.edtConPass);

        footerMainLout = (LinearLayout) findViewById(R.id.footerMainLout);
        footerChand = (FrameLayout) findViewById(R.id.footerChand);

    }

    private void clickMenuType() {

        lout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.HOME);
            }
        });

        lout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.PROFILE);
            }
        });

        lout_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.FAVOURITE);
            }
        });

        lout_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.ABOUT_US);
            }
        });

        lout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_logout.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout_select));
                txt_logout.setTextColor(getResources().getColor(R.color.themeColor));
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
                txt_profile.setTextColor(getResources().getColor(R.color.grey));
                GlobalClass.logOutUser(ChangePasswordActivity.this, GlobalClass.PROFILE, img_logout, txt_logout, img_profile, txt_profile, chandLout);
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(ChangePasswordActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

}
