package com.riyadhbank.Async;

import android.app.Activity;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.CustomDialog;
import com.riyadhbank.Utility.Interface;
import com.riyadhbank.Utility.GlobalClass;

import org.json.JSONObject;

public class SignUpService extends AsyncTask<String, String, String> {

    CustomDialog customDialog;
    Activity activity;
    Interface.OnSignUp onSignUp;
    String Result;
    String FName, LName, Email, MobileNumber, EmployeeId, Password, DeviceId;

    public SignUpService(Activity activity, Interface.OnSignUp onSignUp, String FName, String LName, String email, String mobileNumber, String employeeId, String password) {
        this.activity = activity;
        this.onSignUp = onSignUp;
        this.FName = FName;
        this.LName = LName;
        Email = email;
        MobileNumber = mobileNumber;
        EmployeeId = employeeId;
        Password = password;
        DeviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
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
                    .add(Constants.action, Constants.register)
                    .add(Constants.firstname, FName)
                    .add(Constants.lastname, LName)
                    .add(Constants.displayname, FName + " " + LName)
                    .add(Constants.email, Email)
                    .add(Constants.phonenumber, MobileNumber)
                    .add(Constants.employeeid, EmployeeId)
                    .add(Constants.password, Password)
                    .add(Constants.notificationtoken, "11111")
                    .add(Constants.udid, DeviceId)
                    .add(Constants.appid, GlobalClass.Riyadh);

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

       // Log.e("AA_SSS", s + " -- ");

        try {
            if (!s.equals("Error")) {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("status") == Constants.Success) {
                    onSignUp.onSignUp(jsonObject);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.check_empty), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getResources().getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
        }
    }

}
