package com.riyadhbank.Async;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.CustomDialog;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class FavouriteService extends AsyncTask<String, String, String> {

    CustomDialog customDialog;
    Activity activity;
    Interface.OnFavourite onFavourite;
    String Result;
    String UserId, OfferId, Selected;
    int Position;

    public FavouriteService(Activity activity, Interface.OnFavourite onFavourite, String userId, String offerId, String selected, int Position) {
        this.activity = activity;
        this.onFavourite = onFavourite;
        UserId = userId;
        OfferId = offerId;
        Selected = selected;
        this.Position = Position;
    }

    @Override
    protected void onPreExecute() {
        customDialog = new CustomDialog(activity);
        customDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            Log.e("AA_S", Constants.favouriteselected + " -- " + UserId + " -- " + OfferId + " -- " + Selected + GlobalClass.Riyadh);

            Form form = new Form()
                    .add(Constants.action, Constants.favouriteselected)
                    .add(Constants.userid, UserId)
                    .add(Constants.offerid, OfferId)
                    .add(Constants.selected, Selected)
                    .add(Constants.appid, GlobalClass.Riyadh);

            Request request = Bridge
                    .post(Constants.URL + "/" + Constants.DashBoardProcess)
                    .body(form)
                    .request();

            Response response = request.response();

            if (response.isSuccess()) {
                Result = response.asString();
            } else {
                Result = "Error";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Result = "Error";
        }

        return Result;

    }

    @Override
    protected void onPostExecute(String s) {

        customDialog.dismiss();

        try {
            if (!s.equals("Error")) {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("status") == Constants.Success) {
                    onFavourite.onFavourite(Position);
                }
                //Toast.makeText(activity, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status")) {
                    onFavourite.onFavourite(Position);
                }
                //Toast.makeText(activity, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
            } catch (Exception ee) {
                ee.printStackTrace();
                Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
