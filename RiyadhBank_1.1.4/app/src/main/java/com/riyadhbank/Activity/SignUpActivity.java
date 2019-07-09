package com.riyadhbank.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.CheckNewUserService;
import com.riyadhbank.Async.GetOtpService;
import com.riyadhbank.Async.SignUpService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.Interface;
import com.riyadhbank.Utility.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements Interface.OnSignUp, Interface.OnCheckNewUser, Interface.OnGetOTP {

    EditText edtFname, edtLname, edtEmail, edtMobileNumber, edtEmpId, edtPassword, edtConPass;
    TextView btnSignUp, txtLogin;
    String Fname, Lname, Email, MobileNumber, EmpId, Password, ConPass;
    JSONObject userObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FindById();

        SharedPreferences shared = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = shared.getString(Constants.LanguageCode, "");

        if (GlobalClass.languageCode.equals("en")) {
            edtFname.setGravity(Gravity.LEFT);
            edtLname.setGravity(Gravity.LEFT);
            edtEmail.setGravity(Gravity.LEFT);
            edtMobileNumber.setGravity(Gravity.LEFT);
            edtEmpId.setGravity(Gravity.LEFT);
            edtPassword.setGravity(Gravity.LEFT);
            edtConPass.setGravity(Gravity.LEFT);
        } else {
            edtFname.setGravity(Gravity.RIGHT);
            edtLname.setGravity(Gravity.RIGHT);
            edtEmail.setGravity(Gravity.RIGHT);
            edtMobileNumber.setGravity(Gravity.RIGHT);
            edtEmpId.setGravity(Gravity.RIGHT);
            edtPassword.setGravity(Gravity.RIGHT);
            edtConPass.setGravity(Gravity.RIGHT);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fname = edtFname.getText().toString().trim();
                Lname = edtLname.getText().toString().trim();
                Email = edtEmail.getText().toString().trim();
                MobileNumber = edtMobileNumber.getText().toString().trim();
                EmpId = edtEmpId.getText().toString().trim();
                Password = edtPassword.getText().toString().trim();
                ConPass = edtConPass.getText().toString().trim();

                if (Fname.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (Lname.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (Email.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (!Email.matches(GlobalClass.emailPattern)) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (MobileNumber.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (Password.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (ConPass.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (!Password.equals(ConPass)) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else {
                    if (GlobalClass.isNetworkConnected(SignUpActivity.this)) {
                        new CheckNewUserService(SignUpActivity.this, SignUpActivity.this,
                                MobileNumber).execute();
                    } else {
                        showToastMessage(getResources().getString(R.string.internet_msg));
                    }
                }

            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onCheckNewUser(JSONObject jsonObject) {
        if (GlobalClass.isNetworkConnected(SignUpActivity.this)) {
            try {
                EmpId = jsonObject.getString("empid");
                new SignUpService(SignUpActivity.this, SignUpActivity.this, Fname, Lname, Email,
                        MobileNumber, EmpId, Password).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showToastMessage(getResources().getString(R.string.internet_msg));
        }
    }

    @Override
    public void onSignUp(JSONObject jsonObject) {
        try {
            userObject = new JSONObject(jsonObject.get("userdetails").toString());
            if (GlobalClass.isNetworkConnected(SignUpActivity.this)) {
                new GetOtpService(SignUpActivity.this, SignUpActivity.this,
                        MobileNumber).execute();
            } else {
                showToastMessage(getResources().getString(R.string.internet_msg));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetOTP(JSONObject jsonObject) {
        Intent i = new Intent(SignUpActivity.this, OtpVerificationActivity.class);
        i.putExtra("isFrom", "SignUp");
        try {
            i.putExtra(Constants.userid, userObject.get(Constants.userid).toString());
            i.putExtra(Constants.email, userObject.get(Constants.email).toString());
            i.putExtra(Constants.firstname, userObject.get(Constants.firstname).toString());
            i.putExtra(Constants.lastname, userObject.get(Constants.lastname).toString());
            i.putExtra(Constants.displayname, userObject.get(Constants.displayname).toString());
            i.putExtra(Constants.phonenumber, userObject.get(Constants.phonenumber).toString());
            i.putExtra(Constants.employeeid, userObject.get(Constants.employeeid).toString());
            i.putExtra(Constants.notificationtoken, userObject.get(Constants.notificationtoken).toString());
            i.putExtra(Constants.password, userObject.get(Constants.password).toString());
            i.putExtra(Constants.udid, userObject.get(Constants.udid).toString());
            i.putExtra(Constants.appid, userObject.get(Constants.appid).toString());
            i.putExtra(Constants.lat, userObject.get(Constants.lat).toString());
            i.putExtra(Constants.longi, userObject.get(Constants.longi).toString());
            i.putExtra(Constants.userrole, userObject.get(Constants.userrole).toString());
            i.putExtra(Constants.OTP, userObject.get(Constants.OTP).toString());
            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showToastMessage(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    private void FindById() {

        edtFname = (EditText) findViewById(R.id.edtFname);
        edtLname = (EditText) findViewById(R.id.edtLname);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMobileNumber = (EditText) findViewById(R.id.edtMobileNumber);
        edtEmpId = (EditText) findViewById(R.id.edtEmpId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConPass = (EditText) findViewById(R.id.edtConPass);

        btnSignUp = (TextView) findViewById(R.id.btnSignUp);
        txtLogin = (TextView) findViewById(R.id.txtLogin);

    }
}
