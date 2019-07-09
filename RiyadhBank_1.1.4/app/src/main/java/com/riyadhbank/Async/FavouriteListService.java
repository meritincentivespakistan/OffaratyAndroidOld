package com.riyadhbank.Async;

import android.app.Activity;
import android.os.AsyncTask;
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

public class FavouriteListService extends AsyncTask<String, String, String> {

    CustomDialog customDialog;
    Activity activity;
    Interface.OnFavouriteList onFavouriteList;
    String Result;
    String Lang, UserId;

    public FavouriteListService(Activity activity, Interface.OnFavouriteList onFavouriteList, String lang, String userId) {
        this.activity = activity;
        this.onFavouriteList = onFavouriteList;
        Lang = lang;
        UserId = userId;
    }

    @Override
    protected void onPreExecute() {
        customDialog = new CustomDialog(activity);
        customDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            Form form = new Form()
                    .add(Constants.action, Constants.myfavourite)
                    .add(Constants.language, Lang)
                    .add(Constants.userid, UserId)
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
                onFavouriteList.onFavouriteList(s);
            }else {
                Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

}
