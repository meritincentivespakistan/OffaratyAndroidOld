package com.riyadhbank.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.AboutUsService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class AboutUsActivity extends AppCompatActivity implements Interface.OnAboutUs {

    ImageView back;
    TextView txtTitle, txt_about, txt_logout;
    LinearLayout chandLout,lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_about, img_logout;
    TextView txtAbout;
    String LangType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        FindById();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTitle.setText(getResources().getString(R.string.about_offaraty));

        SharedPreferences pref = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = pref.getString(Constants.LanguageCode, "");

        if (GlobalClass.languageCode.equals("") || GlobalClass.languageCode.equals("en")) {
            LangType = "aboutenglish";
        } else {
            LangType = "aboutarabic";
        }

        img_about.setImageDrawable(getResources().getDrawable(R.drawable.ic_about_us_select));
        txt_about.setTextColor(getResources().getColor(R.color.themeColor));

        clickMenuType();

        if (GlobalClass.isNetworkConnected(AboutUsActivity.this)) {
            new AboutUsService(AboutUsActivity.this, AboutUsActivity.this, LangType).execute();
        } else {
            Toast.makeText(AboutUsActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }

    }

    private void FindById() {
        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        img_about = (ImageView) findViewById(R.id.img_about);
        txt_about = (TextView) findViewById(R.id.txt_about);

        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        txtAbout = (TextView) findViewById(R.id.txtAbout);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

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
                img_about.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
                txt_about.setTextColor(getResources().getColor(R.color.grey));
                GlobalClass.logOutUser(AboutUsActivity.this, GlobalClass.ABOUT_US, img_logout, txt_logout, img_about, txt_about, chandLout);
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(AboutUsActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

    @Override
    public void onAboutUs(JSONObject jsonObject) {
        try {
            JSONObject object = new JSONObject(jsonObject.get("msg").toString());
            txtAbout.setText(object.get("content").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
