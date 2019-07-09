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
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class ChangePasswordService extends AsyncTask<String, String, String> {

    CustomDialog customDialog;
    Activity activity;
    String Result;
    Interface.OnChangePassword onChangePassword;
    String Password,UserId;

    public ChangePasswordService(Activity activity, Interface.OnChangePassword onChangePassword, String password, String userId) {
        this.activity = activity;
        this.onChangePassword = onChangePassword;
        Password = password;
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
                    .add(Constants.action, Constants.changepassword)
                    .add(Constants.password, Password)
                    .add(Constants.userid, UserId);

            Request request = Bridge
                    .post(Constants.URL + "/" + Constants.UserProcess)
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
                    onChangePassword.onChangePassword(jsonObject);
                } else {
                    Toast.makeText(activity, jsonObject.get("msg").toString(), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }


}
