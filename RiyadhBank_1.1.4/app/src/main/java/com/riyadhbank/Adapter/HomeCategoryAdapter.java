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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.riyadhbank.Activity.CategoryWiseOfferActivity;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.R;

import java.util.ArrayList;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    Activity activity;
    ArrayList<CategoryModal> categoryModals;
    private int lastPosition = -1;

    public HomeCategoryAdapter(Activity activity, ArrayList<CategoryModal> categoryModals) {
        this.activity = activity;
        this.categoryModals = categoryModals;
    }

    @Override
    public HomeCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_category, parent, false);
        return new HomeCategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeCategoryAdapter.ViewHolder holder, final int position) {

        Glide.with(activity)
                .load(categoryModals.get(position).getImage())
                .into(holder.img);

        holder.text.setText(categoryModals.get(position).getName());

        holder.mainLout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, CategoryWiseOfferActivity.class);
                i.putExtra("CategoryId", categoryModals.get(position).getId());
                i.putExtra("CategoryTitle", categoryModals.get(position).getName());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView text;
        RelativeLayout mainLout;

        public ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            text = (TextView) view.findViewById(R.id.text);
            mainLout = (RelativeLayout) view.findViewById(R.id.mainLout);
        }
    }

}
