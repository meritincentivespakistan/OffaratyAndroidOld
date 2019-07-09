package com.riyadhbank.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.Modal.LocationModal;
import com.riyadhbank.R;
import com.riyadhbank.Utility.GlobalClass;

import java.util.ArrayList;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    Activity activity;
    ArrayList<LocationModal> locationArrayList;

    public LocationListAdapter(Activity activity, ArrayList<LocationModal> locationArrayList) {
        this.activity = activity;
        this.locationArrayList = locationArrayList;
    }

    @Override
    public LocationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_map_list, parent, false);
        return new LocationListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationListAdapter.ViewHolder holder, final int position) {

        if (position % 2 == 0) {
            holder.mainLout.setBackgroundColor(activity.getResources().getColor(R.color.white));
        } else {
            holder.mainLout.setBackgroundColor(activity.getResources().getColor(R.color.greyTint));
        }

        holder.txtAddress.setText(locationArrayList.get(position).getAddress());
       // holder.txtLocation.setText(locationArrayList.get(position).getLocationname());
        holder.txtArea.setText(locationArrayList.get(position).getPhone());
        holder.txtKm.setText(locationArrayList.get(position).getDistanceinkm() + " " + activity.getResources().getString(R.string.km
        ));

        holder.btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double latitude = Double.parseDouble(locationArrayList.get(position).getLatitude());
                double longitude = Double.parseDouble(locationArrayList.get(position).getLongitude());
                String label = activity.getResources().getString(R.string.app_name);
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                try {
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent1);
                } catch (Exception e) {
                    Log.e("DirectionError", e.toString());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return locationArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLout;
        TextView txtAddress,txtLocation,txtArea,txtKm,btnDirection;

        public ViewHolder(View view) {
            super(view);
            mainLout = (LinearLayout) view.findViewById(R.id.mainLout);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txtLocation = (TextView) view.findViewById(R.id.txtLocation);
            txtArea = (TextView) view.findViewById(R.id.txtArea);
            txtKm = (TextView) view.findViewById(R.id.txtKm);
            btnDirection = (TextView) view.findViewById(R.id.btnDirection);
        }
    }

}
