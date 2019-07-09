package com.riyadhbank.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riyadhbank.R;
import com.riyadhbank.Utility.GlobalClass;

public class RedeemSuccessActivity extends AppCompatActivity {

    ImageView back;
    TextView txtTitle, txt_home, txt_favourite, txt_logout;
    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_home, img_favourite, img_logout;

    String isFrom;
    TextView btnHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem_success);

        FiindById();

        isFrom = getIntent().getStringExtra("isFrom");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTitle.setText(getResources().getString(R.string.success));

        if (isFrom.equals(GlobalClass.HOME)) {
            img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_select));
            txt_home.setTextColor(getResources().getColor(R.color.themeColor));
        } else {
            chandLout.setBackground(getResources().getDrawable(R.drawable.icn_chand_theme));
            img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_select));
            txt_favourite.setTextColor(getResources().getColor(R.color.themeColor));
        }

        clickMenuType();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.HOME);
            }
        });

    }

    private void FiindById() {

        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        img_home = (ImageView) findViewById(R.id.img_home);
        txt_home = (TextView) findViewById(R.id.txt_home);
        img_favourite = (ImageView) findViewById(R.id.img_favourite);
        txt_favourite = (TextView) findViewById(R.id.txt_favourite);
        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

        btnHome = (TextView)findViewById(R.id.btnHome);

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

                if (isFrom.equals(GlobalClass.HOME)) {
                    img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
                    txt_home.setTextColor(getResources().getColor(R.color.grey));
                    GlobalClass.logOutUser(RedeemSuccessActivity.this, GlobalClass.HOME, img_logout, txt_logout, img_home, txt_home, chandLout);
                } else {
                    img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite));
                    txt_favourite.setTextColor(getResources().getColor(R.color.grey));
                    GlobalClass.logOutUser(RedeemSuccessActivity.this, GlobalClass.FAVOURITE, img_logout, txt_logout, img_favourite, txt_favourite, chandLout);
                }
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(RedeemSuccessActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

}
