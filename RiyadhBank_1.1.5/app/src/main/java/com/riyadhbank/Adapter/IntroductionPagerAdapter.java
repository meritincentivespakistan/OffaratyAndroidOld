package com.riyadhbank.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.riyadhbank.Modal.IntroductionModal;
import com.riyadhbank.R;

import java.util.ArrayList;

public class IntroductionPagerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    Activity activity;
    ArrayList<IntroductionModal> arrayList;
    ImageView mainImg;
    TextView txtTitle, txtSubTitle;

    public IntroductionPagerAdapter(Activity activity, ArrayList<IntroductionModal> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.raw_introduction, container,
                false);


        mainImg = (ImageView) viewLayout.findViewById(R.id.mainImg);
        txtTitle = (TextView) viewLayout.findViewById(R.id.txtTitle);
        txtSubTitle = (TextView) viewLayout.findViewById(R.id.txtSubTitle);

        Glide.with(activity)
                .load(arrayList.get(position).getImage())
                .into(mainImg);

        txtTitle.setText(arrayList.get(position).getTitle());
        txtSubTitle.setText(arrayList.get(position).getSubtitle());

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(View v, Object object) {
        return v == ((RelativeLayout) object);
    }

}
