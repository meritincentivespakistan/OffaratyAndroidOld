package com.riyadhbank.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.riyadhbank.Activity.SearchActivity;
import com.riyadhbank.Adapter.FeaturedDealsAdapter;
import com.riyadhbank.Adapter.HomeCategoryAdapter;
import com.riyadhbank.Adapter.ImageSliderAdapter;
import com.riyadhbank.Async.DashboardService;
import com.riyadhbank.Async.FavouriteService;
import com.riyadhbank.Async.WelcomeMessageService;
import com.riyadhbank.Custom.AutoScrollViewPager;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.Modal.OfferModal;
import com.riyadhbank.Modal.SliderModal;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements Interface.OnWelcomeMsg, Interface.OnDashboard,
        Interface.OnClickHomeOrCategoryFavourite, Interface.OnFavourite,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    View view;
    ImageView back, search;
    TextView txtTitle;
    String LangTitle, LangText, Language, UserId;
    NestedScrollView scrollLout;
    ArrayList<SliderModal> sliderArrayList;
    AutoScrollViewPager imageSlider;
    ImageSliderAdapter imageSliderAdapter;

    RecyclerView categoryList;
    ArrayList<CategoryModal> categoryArrayList;
    HomeCategoryAdapter homeCategoryAdapter;

    ArrayList<OfferModal> offerArrayList;
    RecyclerView featureDeals;
    FeaturedDealsAdapter dealsAdapter;
    GoogleApiClient googleApiClient;
    Location myLocation;
    LocationManager locationManager;
    String latitude = "24.649139", longitude = "46.715085";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        FindById();

        back.setVisibility(View.GONE);
        txtTitle.setText(getActivity().getResources().getString(R.string.offaraty));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SearchActivity.class);
                startActivity(i);
            }
        });

        if (GlobalClass.languageCode.equals("") || GlobalClass.languageCode.equals("en")) {
            LangTitle = "welcometitleenglish";
            LangText = "welcomeenglish";
            Language = "english";
        } else {
            LangTitle = "welcometitlearabic";
            LangText = "welcomearabic";
            Language = "arabic";
        }

        if (GlobalClass.isWelcomDialogShow.equals("0")) {
            if (GlobalClass.isNetworkConnected(getActivity())) {
                new WelcomeMessageService(getActivity(), HomeFragment.this, LangTitle, LangText).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

        SharedPreferences shared = getActivity().getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(AppIndex.API).addApi(LocationServices.API).build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        return view;
    }

    String WelcomeTitle, WelcomeDesc;

    @Override
    public void onWelcomeMsg(JSONObject jsonObject) {
        try {
            JSONObject object = new JSONObject(jsonObject.get("msg").toString());
            WelcomeTitle = object.get("title").toString();
            WelcomeDesc = object.get("text").toString();
            welcomeDialog(WelcomeTitle, WelcomeDesc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDashboard(JSONObject jsonObject) {

        try {

            scrollLout.setVisibility(View.VISIBLE);

            //region Set Slider Data
            JSONObject sliderObject = new JSONObject(jsonObject.get("slider").toString());
            JSONArray sliderArray = sliderObject.getJSONArray("slidercontent");

            sliderArrayList = new ArrayList<>();
            for (int i = 0; i < sliderArray.length(); i++) {
                JSONObject sliderInnerObject = sliderArray.getJSONObject(i);
                SliderModal sliderModal = new SliderModal();
                sliderModal.setSr(sliderInnerObject.getString("sr"));
                sliderModal.setImage(sliderInnerObject.getString("image"));
                sliderModal.setAppid(sliderInnerObject.getString("appid"));
                sliderModal.setLanguage(sliderInnerObject.getString("language"));
                sliderModal.setUrl(sliderInnerObject.has("url") ? sliderInnerObject.getString("url") : "");
                sliderArrayList.add(sliderModal);
            }

            imageSliderAdapter = new ImageSliderAdapter(getActivity(), sliderArrayList, imageSlider);
            imageSlider.setAdapter(imageSliderAdapter);
            imageSlider.startAutoScroll();
            imageSlider.setInterval(6000);
            imageSlider.setCycle(true);
            imageSlider.setStopScrollWhenTouch(true);
            //endregion

            //region Set Category Data
            JSONObject categoryObject = new JSONObject(jsonObject.get("category").toString());
            JSONArray categoryArray = categoryObject.getJSONArray("categorycontent");

            categoryArrayList = new ArrayList<>();
            for (int i = 0; i < categoryArray.length(); i++) {
                JSONObject categoryInnerObject = categoryArray.getJSONObject(i);
                CategoryModal categoryModal = new CategoryModal();
                categoryModal.setId(categoryInnerObject.getString("id"));
                categoryModal.setName(categoryInnerObject.getString("name"));
                categoryModal.setImage(categoryInnerObject.getString("image"));
                categoryArrayList.add(categoryModal);
            }

            homeCategoryAdapter = new HomeCategoryAdapter(getActivity(), categoryArrayList);
            categoryList.setAdapter(homeCategoryAdapter);
            //endregion

            //region Set Feature Offer Data
            JSONObject offerObject = new JSONObject(jsonObject.get("offer").toString());
            JSONArray offerArray = offerObject.getJSONArray("offercontent");

            offerArrayList = new ArrayList<>();
            for (int i = 0; i < offerArray.length(); i++) {
                JSONObject offerInnerObject = offerArray.getJSONObject(i);
                OfferModal offerModal = new OfferModal();
                offerModal.setId(offerInnerObject.getString("id"));
                offerModal.setOffertitle(offerInnerObject.getString("offertitle"));
                offerModal.setDiscount(offerInnerObject.getString("discount"));
                offerModal.setImage(offerInnerObject.getString("image"));
                offerModal.setSelected(offerInnerObject.getString("selected"));
                offerModal.setBusinessname(offerInnerObject.getString("businessname"));
                offerArrayList.add(offerModal);
            }

            dealsAdapter = new FeaturedDealsAdapter(getActivity(), offerArrayList, GlobalClass.HOME, HomeFragment.this);
            featureDeals.setAdapter(dealsAdapter);
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String isSelected;

    @Override
    public void onClickHomeOrCategoryFavourite(OfferModal offerModal, int position) {
        if (offerModal.getSelected().equals("1")) {
            isSelected = "0";
        } else {
            isSelected = "1";
        }
        if (GlobalClass.isNetworkConnected(getActivity())) {
            new FavouriteService(getActivity(), HomeFragment.this, UserId, offerModal.getId(), isSelected, position).execute();
        } else {
            Toast.makeText(getActivity(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavourite(int position) {
        offerArrayList.get(position).setSelected(isSelected);
        dealsAdapter.notifyItemChanged(position);
    }

    private void welcomeDialog(String WelcomeMessageTitle, String WelcomeMessageText) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.welcome_dialog);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView txtWelcomeTitle = (TextView) dialog.findViewById(R.id.txtWelcomeTitle);
        TextView txtWelcomeText = (TextView) dialog.findViewById(R.id.txtWelcomeText);
        txtWelcomeTitle.setText(WelcomeMessageTitle);
        txtWelcomeText.setText(WelcomeMessageText);

        TextView btnCancel = (TextView) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        GlobalClass.isWelcomDialogShow = "1";
    }

    private void FindById() {
        back = (ImageView) view.findViewById(R.id.back);
        search = (ImageView) view.findViewById(R.id.search);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        scrollLout = (NestedScrollView) view.findViewById(R.id.scrollLout);
        imageSlider = (AutoScrollViewPager) view.findViewById(R.id.imageSlider);

        featureDeals = (RecyclerView) view.findViewById(R.id.featureDeals);
        featureDeals.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));

        categoryList = (RecyclerView) view.findViewById(R.id.categoryList);
        categoryList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void onPause() {
        super.onPause();
        imageSlider.stopAutoScroll();
    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            try {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                latitude = String.valueOf(myLocation.getLatitude());
                longitude = String.valueOf(myLocation.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            CallDahsboardService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            try {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                latitude = String.valueOf(myLocation.getLatitude());
                longitude = String.valueOf(myLocation.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CallDahsboardService();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkPermissions();
    }

    @Override
    public void onConnectionSuspended(int i) {
        CallDahsboardService();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        CallDahsboardService();
    }

    private void CallDahsboardService() {
        Log.e("AA_S", latitude + " -- " + longitude);
        if (GlobalClass.isNetworkConnected(getActivity())) {
            new DashboardService(getActivity(), HomeFragment.this, Language, UserId, latitude, longitude).execute();
        } else {
            Toast.makeText(getActivity(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

}

