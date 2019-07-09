package com.riyadhbank.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.riyadhbank.Async.GetVoucherService;
import com.riyadhbank.Async.OfferDetailService;
import com.riyadhbank.Fragment.LocationListFragment;
import com.riyadhbank.Fragment.LocationMapFragment;
import com.riyadhbank.Modal.LocationModal;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfferDetailActivity extends AppCompatActivity implements Interface.OnOfferDetail, Interface.OnGetVoucher {

    TabLayout tabs;
    ViewPager locationPager;

    ScrollView scrollView;
    ExpandableLayout aboutExpand, tcExpand, locationExpand;
    ImageView imgAboutPlus, imgTCPlus, imgLocationPlus;
    TextView txtOfferAbout, txtOfferTC, btnGetNow;
    String isFrom, OfferId, OfferTitle, Language, UserId, ExpiryDate;

    ImageView back;
    TextView txtTitle, txt_home, txt_favourite, txt_logout;
    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_home, img_favourite, img_logout;

    ImageView offerImg, btnShare;
    TextView offerName, offerDetail, expiryDate, saveMoney;
    ArrayList<LocationModal> locationArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        FindById();

        isFrom = getIntent().getStringExtra("isFrom");
        OfferId = getIntent().getStringExtra("OfferId");
        OfferTitle = getIntent().getStringExtra("OfferTitle");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTitle.setText(OfferTitle);

        if (GlobalClass.languageCode.equals("") || GlobalClass.languageCode.equals("en")) {
            Language = "english";
        } else {
            Language = "arabic";
        }

        SharedPreferences shared = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");

        //Log.e("AA_S", UserId + " -- " + OfferId);

        if (GlobalClass.isNetworkConnected(OfferDetailActivity.this)) {
            new OfferDetailService(OfferDetailActivity.this, OfferDetailActivity.this, Language, UserId, OfferId).execute();
        } else {
            Toast.makeText(OfferDetailActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }

        imgAboutPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrCollapse(aboutExpand, imgAboutPlus);
            }
        });

        imgTCPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrCollapse(tcExpand, imgTCPlus);
            }
        });

        imgLocationPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandOrCollapse(locationExpand, imgLocationPlus);
            }
        });

        if (isFrom.equals(GlobalClass.HOME)) {
            img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_select));
            txt_home.setTextColor(getResources().getColor(R.color.themeColor));
        } else {
            chandLout.setBackground(getResources().getDrawable(R.drawable.icn_chand_theme));
            img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_select));
            txt_favourite.setTextColor(getResources().getColor(R.color.themeColor));
        }

        clickMenuType();

        btnGetNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GlobalClass.isNetworkConnected(OfferDetailActivity.this)) {
                    new GetVoucherService(OfferDetailActivity.this, OfferDetailActivity.this, OfferId,Language,UserId).execute();
                } else {
                    Toast.makeText(OfferDetailActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onGetVoucher(JSONObject jsonObject) {

        try {
            Intent i = new Intent(OfferDetailActivity.this, GetVoucherActivity.class);
            i.putExtra("isFrom", isFrom);
            i.putExtra("CouponCode", jsonObject.getString("coupencode"));
            i.putExtra("SponsorImage", jsonObject.getString("sponsorimage"));
            i.putExtra("OfferTitle", jsonObject.getString("title"));
            i.putExtra("ExpiryDate", jsonObject.getString("enddate"));
            i.putExtra("SponsorText", jsonObject.getString("printmsg"));
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onOfferDetail(JSONObject jsonObject) {

        try {

            scrollView.setVisibility(View.VISIBLE);

            JSONArray offerArray = jsonObject.getJSONArray("offercontent");
            final JSONObject offerObject = offerArray.getJSONObject(0);

            Glide.with(this)
                    .load(offerObject.getString("image"))
                    .into(offerImg);

            offerName.setText(offerObject.getString("name"));
            offerDetail.setText(offerObject.getString("offerdetail"));
            ExpiryDate = offerObject.getString("valid");
            expiryDate.setText(getResources().getString(R.string.valid_till) + " " + ExpiryDate);
            saveMoney.setText(getResources().getString(R.string.estimate_saving) + " " + offerObject.getString("estimatedsaving"));

            txtOfferAbout.setText(offerObject.getString("branddescription"));
            txtOfferTC.setText(offerObject.getString("termscondition"));

            JSONArray locationArray = offerObject.getJSONArray("location");
            locationArrayList = new ArrayList<>();
            for (int i = 0; i < locationArray.length(); i++) {
                JSONObject innerObject = locationArray.getJSONObject(i);
                LocationModal locationModal = new LocationModal();
                locationModal.setLatitude(innerObject.getString("latitude"));
                locationModal.setLongitude(innerObject.getString("longitude"));
                locationModal.setAddress(innerObject.getString("address"));
                locationModal.setPhone(innerObject.getString("phone"));
                locationModal.setLocationname(innerObject.getString("locationname"));
                locationModal.setAreaname(innerObject.getString("areaname"));
                locationModal.setDistanceinkm(innerObject.getString("distanceinkm"));
                locationArrayList.add(locationModal);
            }

            tabs.setupWithViewPager(locationPager);
            setupViewPager(locationPager);
            locationPager.setOffscreenPageLimit(1);

            btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String shareMsg = getResources().getString(R.string.thanks_app_txt) + "\n\n" +
                                offerObject.getString("name") + "\n\n" + offerObject.getString("offerdetail") + "\n\n" +
                                getResources().getString(R.string.offer_valid_till) + " " + offerObject.getString("valid") + "\n\n" +
                                getResources().getString(R.string.estimate_saving) + " " + offerObject.getString("estimatedsaving") + "\n\n" +
                                getResources().getString(R.string.thank_you);

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMsg);
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void expandOrCollapse(ExpandableLayout layout, ImageView imageView) {
        if (layout.isExpanded()) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.icn_plus));
            layout.collapse();
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.icn_minus));
            layout.expand();
        }
    }

    private void FindById() {

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

        tabs = (TabLayout) findViewById(R.id.tabs);
        locationPager = (ViewPager) findViewById(R.id.locationPager);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        aboutExpand = (ExpandableLayout) findViewById(R.id.aboutExpand);
        imgAboutPlus = (ImageView) findViewById(R.id.imgAboutPlus);
        txtOfferAbout = (TextView) findViewById(R.id.txtOfferAbout);

        tcExpand = (ExpandableLayout) findViewById(R.id.tcExpand);
        imgTCPlus = (ImageView) findViewById(R.id.imgTCPlus);
        txtOfferTC = (TextView) findViewById(R.id.txtOfferTC);
        btnGetNow = (TextView) findViewById(R.id.btnGetNow);

        locationExpand = (ExpandableLayout) findViewById(R.id.locationExpand);
        imgLocationPlus = (ImageView) findViewById(R.id.imgLocationPlus);

        btnShare = (ImageView) findViewById(R.id.btnShare);
        offerImg = (ImageView) findViewById(R.id.offerImg);
        offerName = (TextView) findViewById(R.id.offerName);
        offerDetail = (TextView) findViewById(R.id.offerDetail);
        expiryDate = (TextView) findViewById(R.id.expiryDate);
        saveMoney = (TextView) findViewById(R.id.saveMoney);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LocationListFragment(locationArrayList), getResources().getString(R.string.list_view));
        adapter.addFragment(new LocationMapFragment(scrollView, locationArrayList), getResources().getString(R.string.map_view));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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
                    GlobalClass.logOutUser(OfferDetailActivity.this, GlobalClass.HOME, img_logout, txt_logout, img_home, txt_home, chandLout);
                } else {
                    img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite));
                    txt_favourite.setTextColor(getResources().getColor(R.color.grey));
                    GlobalClass.logOutUser(OfferDetailActivity.this, GlobalClass.FAVOURITE, img_logout, txt_logout, img_favourite, txt_favourite, chandLout);
                }
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(OfferDetailActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

}
