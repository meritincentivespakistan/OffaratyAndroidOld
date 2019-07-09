package com.riyadhbank.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.riyadhbank.Activity.WebviewActivity;
import com.riyadhbank.Custom.AutoScrollViewPager;
import com.riyadhbank.Modal.SliderModal;
import com.riyadhbank.R;

import java.util.ArrayList;

public class ImageSliderAdapter extends PagerAdapter {

    Activity activity;
    ArrayList<SliderModal> sliderModals;
    AutoScrollViewPager imageSlider;

    public ImageSliderAdapter(Activity activity, ArrayList<SliderModal> sliderModals, AutoScrollViewPager imageSlider) {
        this.activity = activity;
        this.sliderModals = sliderModals;
        this.imageSlider = imageSlider;
    }

    @Override
    public int getCount() {
        return sliderModals.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.raw_image_slider, container, false);

        final ImageView imgSlider = (ImageView) view.findViewById(R.id.imgSlider);

        imgSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sliderModals.get(position).getUrl().equals("")) {
                    Intent i = new Intent(activity, WebviewActivity.class);
                    i.putExtra("URL", sliderModals.get(position).getUrl());
                    activity.startActivity(i);
                }
            }
        });

        Glide.with(activity)
                .load(sliderModals.get(position).getImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        ViewGroup.LayoutParams params = imgSlider.getLayoutParams();
                        params.height = (int) (imgSlider.getWidth() / 2.25);
                        imgSlider.setLayoutParams(params);

                        ViewGroup.LayoutParams params1 = imageSlider.getLayoutParams();
                        params1.height = (int) (imageSlider.getWidth() / 2.25);
                        imageSlider.setLayoutParams(params1);

                       /* Log.e("AA_S", imgSlider.getWidth() + " -- " + (int) (imgSlider.getWidth() / 2.25) + " -- " +
                                imageSlider.getWidth() + " ** " + (int) (imageSlider.getWidth() / 2.25));*/

                        return false;
                    }
                })
                .into(imgSlider);

        container.addView(view);

        return view;

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}