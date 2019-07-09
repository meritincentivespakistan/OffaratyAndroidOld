package com.riyadhbank.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Adapter.FavouriteAdapter;
import com.riyadhbank.Async.FavouriteListService;
import com.riyadhbank.Async.FavouriteService;
import com.riyadhbank.Modal.CategoryModal;
import com.riyadhbank.Modal.FavouriteModal;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class FavouriteFragment extends Fragment implements Interface.OnFavouriteList, Interface.OnClickFavourite, Interface.OnFavourite {

    View view;
    ImageView back;
    TextView txtTitle;
    RecyclerView favouriteList;
    ArrayList<FavouriteModal> favouriteArrayList;
    FavouriteAdapter favouriteAdapter;
    String Language, UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        FindById();

        back.setVisibility(View.GONE);
        txtTitle.setText(getActivity().getResources().getString(R.string.my_fav));

        if (GlobalClass.languageCode.equals("") || GlobalClass.languageCode.equals("en")) {
            Language = "english";
        } else {
            Language = "arabic";
        }

        SharedPreferences shared = getActivity().getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");

        if (GlobalClass.isNetworkConnected(getActivity())) {
            new FavouriteListService(getActivity(), FavouriteFragment.this, Language, UserId).execute();
        } else {
            Toast.makeText(getActivity(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onFavouriteList(String json) {
        try {

            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getInt("status") == Constants.Success) {

                JSONArray favouriteArray = jsonObject.getJSONArray("offercontent");
                favouriteArrayList = new ArrayList<>();
                for (int i = 0; i < favouriteArray.length(); i++) {
                    JSONObject offerInnerObject = favouriteArray.getJSONObject(i);
                    FavouriteModal favouriteModal = new FavouriteModal();
                    favouriteModal.setId(offerInnerObject.getString("id"));
                    favouriteModal.setOffertitle(offerInnerObject.getString("offertitle"));
                    favouriteModal.setDiscount(offerInnerObject.getString("discount"));
                    favouriteModal.setBusinessname(offerInnerObject.getString("businessname"));
                    favouriteModal.setImage(offerInnerObject.getString("image"));
                    favouriteModal.setSelected(offerInnerObject.getString("selected"));
                    favouriteArrayList.add(favouriteModal);
                }

                favouriteAdapter = new FavouriteAdapter(getActivity(), favouriteArrayList, GlobalClass.FAVOURITE, FavouriteFragment.this);
                favouriteList.setAdapter(favouriteAdapter);
                favouriteList.setVisibility(View.VISIBLE);

            } else {
                favouriteList.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_favourite), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickFavourite(FavouriteModal favouriteModal, int position) {
        if (GlobalClass.isNetworkConnected(getActivity())) {
            new FavouriteService(getActivity(), this, UserId, favouriteModal.getId(), "0", position).execute();
        } else {
            Toast.makeText(getActivity(), (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFavourite(int position) {
        favouriteArrayList.remove(position);
        favouriteAdapter.notifyDataSetChanged();
    }

    private void FindById() {
        back = (ImageView) view.findViewById(R.id.back);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);

        favouriteList = (RecyclerView) view.findViewById(R.id.favouriteList);
        favouriteList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

    }
}
