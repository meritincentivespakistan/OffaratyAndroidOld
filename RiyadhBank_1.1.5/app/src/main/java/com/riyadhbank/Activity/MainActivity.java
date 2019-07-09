package com.riyadhbank.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riyadhbank.Fragment.AboutUsFragment;
import com.riyadhbank.Fragment.FavouriteFragment;
import com.riyadhbank.Fragment.HomeFragment;
import com.riyadhbank.Fragment.ProfileFragment;
import com.riyadhbank.R;
import com.riyadhbank.Utility.GlobalClass;

public class MainActivity extends AppCompatActivity {

    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_home, img_profile, img_about, img_logout, img_favourite;
    TextView txt_home, txt_profile, txt_about, txt_logout, txt_favourite;
    Fragment fragment;
    FrameLayout lout_frame;
    String isFrom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindById();

        isFrom = getIntent().getStringExtra("isFrom");

        if (isFrom.equals(GlobalClass.HOME)) {
            selectMenuType(GlobalClass.HOME);
        } else if (isFrom.equals(GlobalClass.PROFILE)) {
            selectMenuType(GlobalClass.PROFILE);
        } else if (isFrom.equals(GlobalClass.FAVOURITE)) {
            selectMenuType(GlobalClass.FAVOURITE);
        } else if (isFrom.equals(GlobalClass.ABOUT_US)) {
            selectMenuType(GlobalClass.ABOUT_US);
        }

        lout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMenuType(GlobalClass.HOME);
            }
        });

        lout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMenuType(GlobalClass.PROFILE);
            }
        });

        lout_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMenuType(GlobalClass.FAVOURITE);
            }
        });

        lout_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMenuType(GlobalClass.ABOUT_US);
            }
        });

        lout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMenuType(GlobalClass.LOGOUT);
            }
        });

    }


    private void selectMenuType(String pos) {

        chandLout.setBackground(getResources().getDrawable(R.drawable.icn_chand_grey));
        img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
        img_about.setImageDrawable(getResources().getDrawable(R.drawable.ic_about_us));
        img_logout.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout));
        img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite));

        txt_home.setTextColor(getResources().getColor(R.color.grey));
        txt_profile.setTextColor(getResources().getColor(R.color.grey));
        txt_about.setTextColor(getResources().getColor(R.color.grey));
        txt_logout.setTextColor(getResources().getColor(R.color.grey));
        txt_favourite.setTextColor(getResources().getColor(R.color.grey));

        if (pos.equals(GlobalClass.HOME)) {
            fragment = new HomeFragment();
            img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_select));
            txt_home.setTextColor(getResources().getColor(R.color.themeColor));
        } else if (pos.equals(GlobalClass.PROFILE)) {
            fragment = new ProfileFragment();
            img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_select));
            txt_profile.setTextColor(getResources().getColor(R.color.themeColor));
        } else if (pos.equals(GlobalClass.FAVOURITE)) {
            fragment = new FavouriteFragment();
            chandLout.setBackground(getResources().getDrawable(R.drawable.icn_chand_theme));
            img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_select));
            txt_favourite.setTextColor(getResources().getColor(R.color.themeColor));
        } else if (pos.equals(GlobalClass.ABOUT_US)) {
            fragment = new AboutUsFragment();
            img_about.setImageDrawable(getResources().getDrawable(R.drawable.ic_about_us_select));
            txt_about.setTextColor(getResources().getColor(R.color.themeColor));
        } else if (pos.equals(GlobalClass.LOGOUT)) {

            img_logout.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout_select));
            txt_logout.setTextColor(getResources().getColor(R.color.themeColor));

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.lout_frame);

            if (fragment instanceof HomeFragment) {
                GlobalClass.logOutUser(MainActivity.this, GlobalClass.HOME, img_logout, txt_logout, img_home, txt_home, chandLout);
            } else if (fragment instanceof ProfileFragment) {
                GlobalClass.logOutUser(MainActivity.this, GlobalClass.PROFILE, img_logout, txt_logout, img_profile, txt_profile, chandLout);
            } else if (fragment instanceof FavouriteFragment) {
                GlobalClass.logOutUser(MainActivity.this, GlobalClass.FAVOURITE, img_logout, txt_logout, img_favourite, txt_favourite, chandLout);
            } else if (fragment instanceof AboutUsFragment) {
                GlobalClass.logOutUser(MainActivity.this, GlobalClass.ABOUT_US, img_logout, txt_logout, img_about, txt_about, chandLout);
            }
        }

        if (!pos.equals(GlobalClass.LOGOUT)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.lout_frame, fragment).commit();
        }

    }

    private void FindById() {

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

        img_home = (ImageView) findViewById(R.id.img_home);
        img_profile = (ImageView) findViewById(R.id.img_profile);
        img_about = (ImageView) findViewById(R.id.img_about);
        img_logout = (ImageView) findViewById(R.id.img_logout);
        img_favourite = (ImageView) findViewById(R.id.img_favourite);

        txt_home = (TextView) findViewById(R.id.txt_home);
        txt_profile = (TextView) findViewById(R.id.txt_profile);
        txt_about = (TextView) findViewById(R.id.txt_about);
        txt_logout = (TextView) findViewById(R.id.txt_logout);
        txt_favourite = (TextView) findViewById(R.id.txt_favourite);
        lout_frame = (FrameLayout) findViewById(R.id.lout_frame);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
