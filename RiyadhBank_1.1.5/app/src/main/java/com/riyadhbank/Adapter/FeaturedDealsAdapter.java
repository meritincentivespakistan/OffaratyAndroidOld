package com.riyadhbank.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.riyadhbank.Activity.OfferDetailActivity;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.Modal.FeatureDealsModal;
import com.riyadhbank.Modal.OfferModal;
import com.riyadhbank.R;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import java.util.ArrayList;

public class FeaturedDealsAdapter extends RecyclerView.Adapter<FeaturedDealsAdapter.ViewHolder> {

    Activity activity;
    ArrayList<OfferModal> offerModals;
    String type;
    Interface.OnClickHomeOrCategoryFavourite onClickHomeOrCategoryFavourite;
    private int lastPosition = -1;

    public FeaturedDealsAdapter(Activity activity, ArrayList<OfferModal> offerModals, String type, Interface.OnClickHomeOrCategoryFavourite onClickHomeOrCategoryFavourite) {
        this.activity = activity;
        this.offerModals = offerModals;
        this.type = type;
        this.onClickHomeOrCategoryFavourite = onClickHomeOrCategoryFavourite;
    }

    @Override
    public FeaturedDealsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_feature_deals, parent, false);
        return new FeaturedDealsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeaturedDealsAdapter.ViewHolder holder, final int position) {

        setAnimation(holder.itemView, position);

        if (GlobalClass.languageCode.equals("en") || GlobalClass.languageCode.equals("")) {
            holder.dicountLout.setRotation(0);
        } else {
            holder.dicountLout.setRotation(180);
        }

        Glide.with(activity)
                .load(offerModals.get(position).getImage())
                .into(holder.offerImg);

        holder.txtDiscount.setText(offerModals.get(position).getDiscount() + " " + activity.getResources().getString(R.string.off));
        holder.offerCompName.setText(offerModals.get(position).getBusinessname());
        holder.offerTitle.setText(offerModals.get(position).getOffertitle());

        if (offerModals.get(position).getSelected().equals("1")) {
            holder.imgFavourite.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart_full));
        } else {
            holder.imgFavourite.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
        }

        holder.favLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickHomeOrCategoryFavourite.onClickHomeOrCategoryFavourite(offerModals.get(position), position);
            }
        });

        holder.mainLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, OfferDetailActivity.class);
                i.putExtra("isFrom", type);
                i.putExtra("OfferId", offerModals.get(position).getId());
                i.putExtra("OfferTitle", offerModals.get(position).getOffertitle());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return offerModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView offerImg, imgFavourite;
        TextView offerCompName,offerTitle, txtDiscount;
        RelativeLayout mainLout;
        View dicountLout;
        LinearLayout favLout;

        public ViewHolder(View view) {
            super(view);
            dicountLout = (View) view.findViewById(R.id.dicountLout);
            offerImg = (ImageView) view.findViewById(R.id.offerImg);
            imgFavourite = (ImageView) view.findViewById(R.id.imgFavourite);
            offerCompName = (TextView) view.findViewById(R.id.offerCompName);
            offerTitle = (TextView) view.findViewById(R.id.offerTitle);
            txtDiscount = (TextView) view.findViewById(R.id.txtDiscount);
            mainLout = (RelativeLayout) view.findViewById(R.id.mainLout);
            favLout = (LinearLayout) view.findViewById(R.id.favLout);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slide_from_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
