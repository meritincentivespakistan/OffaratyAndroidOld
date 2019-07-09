package com.riyadhbank.Async;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.Form;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class WelcomeMessageService extends AsyncTask<String, String, String> {

    Activity activity;
    Interface.OnWelcomeMsg onWelcomeMsg;
    String Result, LangTitle, LangText;

    public WelcomeMessageService(Activity activity,Interface.OnWelcomeMsg onWelcomeMsg, String LangTitle,String LangText) {
        this.activity = activity;
        this.onWelcomeMsg = onWelcomeMsg;
        this.LangTitle = LangTitle;
        this.LangText = LangText;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            Form form = new Form()
                    .add(Constants.action, Constants.get_contentwelcome)
                    .add(Constants.title, LangTitle)
                    .add(Constants.text, LangText)
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

        try {
            if (!s.equals("Error")) {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getInt("status") == Constants.Success) {
                    onWelcomeMsg.onWelcomeMsg(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
