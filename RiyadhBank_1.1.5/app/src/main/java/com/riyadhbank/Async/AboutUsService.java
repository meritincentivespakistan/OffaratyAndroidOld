package com.riyadhbank.Async;

import android.app.Activity;
import android.os.AsyncTask;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.CustomDialog;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class AboutUsService extends AsyncTask<String, String, String> {

    CustomDialog customDialog;
    Activity activity;
    Interface.OnAboutUs onAboutUs;
    String Result, LangType;

    public AboutUsService(Activity activity, Interface.OnAboutUs onAboutUs, String langType) {
        this.activity = activity;
        this.onAboutUs = onAboutUs;
        LangType = langType;
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
                    .add(Constants.action, Constants.get_content)
                    .add(Constants.key, LangType)
                    .add(Constants.appid, GlobalClass.Riyadh);

            Request request = Bridge
                    .post(Constants.URL + "/" + Constants.ContentProcess)
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
                    onAboutUs.onAboutUs(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
