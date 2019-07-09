package com.riyadhbank.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.riyadhbank.Activity.OfferDetailActivity;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.Modal.FavouriteModal;
import com.riyadhbank.R;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    Activity activity;
    ArrayList<FavouriteModal> favouriteModals;
    String type;
    Interface.OnClickFavourite onClickFavourite;
    private int lastPosition = -1;

    public FavouriteAdapter(Activity activity, ArrayList<FavouriteModal> favouriteModals, String type, Interface.OnClickFavourite onClickFavourite) {
        this.activity = activity;
        this.favouriteModals = favouriteModals;
        this.type = type;
        this.onClickFavourite = onClickFavourite;
    }

    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_favourite_list, parent, false);
        return new FavouriteAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.ViewHolder holder, final int position) {

        setAnimation(holder.itemView, position);

        if (GlobalClass.languageCode.equals("en") || GlobalClass.languageCode.equals("")) {
            holder.dicountLout.setRotation(0);
        } else {
            holder.dicountLout.setRotation(180);
        }

        Glide.with(activity)
                .load(favouriteModals.get(position).getImage())
                .into(holder.offerImg);

        holder.txtDiscount.setText(favouriteModals.get(position).getDiscount() + " " + activity.getResources().getString(R.string.off));
        holder.txtBussinessName.setText(favouriteModals.get(position).getBusinessname());
        holder.txtOffetTitle.setText(favouriteModals.get(position).getOffertitle());

        if (favouriteModals.get(position).getSelected().equals("1")) {
            holder.imgFavourite.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart_full));
        } else {
            holder.imgFavourite.setImageDrawable(activity.getResources().getDrawable(R.drawable.heart));
        }

        holder.favLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickFavourite.onClickFavourite(favouriteModals.get(position), position);
            }
        });

        holder.mainLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, OfferDetailActivity.class);
                i.putExtra("isFrom", type);
                i.putExtra("OfferId", favouriteModals.get(position).getId());
                i.putExtra("OfferTitle", favouriteModals.get(position).getOffertitle());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouriteModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView offerImg, imgFavourite;
        TextView txtBussinessName, txtOffetTitle, txtDiscount;
        RelativeLayout mainLout;
        View dicountLout;
        LinearLayout favLout;

        public ViewHolder(View view) {
            super(view);

            dicountLout = (View) view.findViewById(R.id.dicountLout);
            offerImg = (ImageView) view.findViewById(R.id.offerImg);
            imgFavourite = (ImageView) view.findViewById(R.id.imgFavourite);
            txtOffetTitle = (TextView) view.findViewById(R.id.txtOffetTitle);
            txtBussinessName = (TextView) view.findViewById(R.id.txtBussinessName);
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
