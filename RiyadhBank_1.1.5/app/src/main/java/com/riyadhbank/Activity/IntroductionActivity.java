package com.riyadhbank.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Adapter.IntroductionPagerAdapter;
import com.riyadhbank.Async.IntroductionService;
import com.riyadhbank.Modal.IntroductionModal;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IntroductionActivity extends AppCompatActivity implements Interface.OnIntroduction {

    ViewPager viewPager;
    ImageView[] bottomBars;
    LinearLayout Layout_bars;
    IntroductionPagerAdapter pagerAdapter;
    ArrayList<IntroductionModal> introductionModals;
    TextView btnNext;
    String LangType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Layout_bars = (LinearLayout) findViewById(R.id.Layout_bars);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btnNext = (TextView) findViewById(R.id.btnNext);

        SharedPreferences shared = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = shared.getString(Constants.LanguageCode, "en");

        if(GlobalClass.languageCode.equals("en")){
            LangType = "english";
        }else {
            LangType = "arabic";
        }

        if (GlobalClass.isNetworkConnected(IntroductionActivity.this)) {
            new IntroductionService(IntroductionActivity.this, IntroductionActivity.this, LangType).execute();
        } else {
            Toast.makeText(IntroductionActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onIntroduction(JSONObject jsonObject) {

        try {

            JSONArray jsonArray = new JSONArray(jsonObject.get("tutorial").toString());

            if (jsonArray.length() > 0) {

                introductionModals = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    IntroductionModal modal = new IntroductionModal();
                    JSONObject object = new JSONObject(jsonArray.get(i).toString());
                    modal.setSr(object.get("sr").toString());
                    modal.setImage(object.get("image").toString());
                    modal.setTitle(object.get("title").toString());
                    modal.setSubtitle(object.get("subtitle").toString());
                    modal.setAppid(object.get("appid").toString());
                    modal.setLanguage(object.get("language").toString());
                    introductionModals.add(modal);
                }

                selectedDots(0);

                pagerAdapter = new IntroductionPagerAdapter(IntroductionActivity.this, introductionModals);
                viewPager.setAdapter(pagerAdapter);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {

                    }

                    @Override
                    public void onPageSelected(int i) {

                        selectedDots(i);
                        if (i == introductionModals.size() - 1) {
                            btnNext.setVisibility(View.VISIBLE);
                        }else {
                            btnNext.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {

                    }
                });

                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(IntroductionActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.putExtra("isFrom", GlobalClass.HOME);
                        startActivity(i);
                        finish();
                    }
                });

            } else {
                GlobalClass.showToastMessage(IntroductionActivity.this, getResources().getString(R.string.please_try_again));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void selectedDots(int pos) {

        bottomBars = new ImageView[introductionModals.size()];

        Layout_bars.removeAllViews();
        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new ImageView(this);
            bottomBars[i].setImageDrawable(getResources().getDrawable(R.drawable.deactivate));
            Layout_bars.addView(bottomBars[i]);
        }

        if (bottomBars.length > 0)
            bottomBars[pos].setImageDrawable(getResources().getDrawable(R.drawable.activate));

    }
}
