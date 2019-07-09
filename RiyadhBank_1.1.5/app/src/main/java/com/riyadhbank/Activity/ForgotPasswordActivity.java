package com.riyadhbank.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.CheckNewUserService;
import com.riyadhbank.Async.ForgotPasswordService;
import com.riyadhbank.Async.GetOtpService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity implements Interface.OnGetOTP, Interface.OnCheckNewUser {

    ImageView back;
    TextView txtTitle, btnForgotPass;
    EditText edtEmail;
    String PhoneNumber;
    JSONObject json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        FindViewById();

        if (GlobalClass.languageCode.equals("en")) {
            edtEmail.setGravity(Gravity.LEFT);
        } else {
            edtEmail.setGravity(Gravity.RIGHT);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtTitle.setText(getResources().getString(R.string.forgot_pass));

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneNumber = edtEmail.getText().toString().trim();

                if (PhoneNumber.equals("")) {
                    Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.insert_email), Toast.LENGTH_SHORT).show();
                } else {
                    if (GlobalClass.isNetworkConnected(ForgotPasswordActivity.this)) {
                        new CheckNewUserService(ForgotPasswordActivity.this, ForgotPasswordActivity.this,
                                PhoneNumber).execute();
                        //new ForgotPasswordService(ForgotPasswordActivity.this, ForgotPasswordActivity.this, Email).execute();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onCheckNewUser(JSONObject jsonObject) {
        try {
            json = jsonObject;
            if (GlobalClass.isNetworkConnected(ForgotPasswordActivity.this)) {
                new GetOtpService(ForgotPasswordActivity.this, ForgotPasswordActivity.this,
                        PhoneNumber).execute();
            } else {
                Toast.makeText(this, getResources().getString(R.string.internet_msg), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetOTP(JSONObject jsonObject) {
        try {
            Log.e("AA_S_F", json.get(Constants.userid).toString());
            Intent i = new Intent(this, OtpVerificationActivity.class);
            i.putExtra(Constants.userid, json.get(Constants.userid).toString());
            i.putExtra(Constants.phonenumber, PhoneNumber);
            i.putExtra("isFrom", "ForgotPassword");
            startActivity(i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FindViewById() {

        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        btnForgotPass = (TextView) findViewById(R.id.btnForgotPass);
        edtEmail = (EditText) findViewById(R.id.edtEmail);

    }

}
