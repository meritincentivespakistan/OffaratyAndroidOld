package com.riyadhbank.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.UserVerificationService;
import com.riyadhbank.Async.VerifyOtpService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.Interface;
import com.riyadhbank.Utility.GlobalClass;

import org.json.JSONException;
import org.json.JSONObject;

public class OtpVerificationActivity extends AppCompatActivity implements Interface.OnUserVerification, Interface.OnVerifyOTP {

    ImageView back;
    TextView txtTitle;
    TextView btnSubmit;
    EditText edtOne, edtTwo, edtThree, edtFour, edtFive, edtSix;
    String OTP;
    String UserId, Email, PhoneNumber, EmpId, redirectOtp;
    String isFrom, Fname, Lname, Name, NotificationToken, Password, DeviceId, AppId, Lat, Long, UserRole;
    LinearLayout otpLout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        FindViewById();

        showOTPDialog();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTitle.setText(getResources().getString(R.string.otp_verification));

        isFrom = getIntent().getStringExtra("isFrom");
        UserId = getIntent().getStringExtra(Constants.userid);

        SharedPreferences shared = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = shared.getString(Constants.LanguageCode, "en");
        if (GlobalClass.languageCode.equals("ar")) {
            otpLout.setRotation(180);
            edtOne.setRotation(180);
            edtTwo.setRotation(180);
            edtThree.setRotation(180);
            edtFour.setRotation(180);
            edtFive.setRotation(180);
            edtSix.setRotation(180);
        }

        if (isFrom.equals("SignUp")) {
            redirectOtp = getIntent().getStringExtra(Constants.OTP);
            Email = getIntent().getStringExtra(Constants.email);
            PhoneNumber = getIntent().getStringExtra(Constants.phonenumber);
            EmpId = getIntent().getStringExtra(Constants.employeeid);
            Fname = getIntent().getStringExtra(Constants.firstname);
            Lname = getIntent().getStringExtra(Constants.lastname);
            Name = getIntent().getStringExtra(Constants.displayname);
            NotificationToken = getIntent().getStringExtra(Constants.notificationtoken);
            Password = getIntent().getStringExtra(Constants.password);
            DeviceId = getIntent().getStringExtra(Constants.udid);
            AppId = getIntent().getStringExtra(Constants.appid);
            Lat = getIntent().getStringExtra(Constants.lat);
            Long = getIntent().getStringExtra(Constants.longi);
            UserRole = getIntent().getStringExtra(Constants.userrole);
        } else {
            PhoneNumber = getIntent().getStringExtra(Constants.phonenumber);
        }

        // Log.e("AA_S", redirectOtp);

        //region Otp Edittext
        setEdtRoundBackground(edtOne, false);
        setEdtRoundBackground(edtTwo, false);
        setEdtRoundBackground(edtThree, false);
        setEdtRoundBackground(edtFour, false);
        setEdtRoundBackground(edtFive, false);
        setEdtRoundBackground(edtSix, false);

        edtOne.addTextChangedListener(new CustomTextWatcher(edtOne));
        edtTwo.addTextChangedListener(new CustomTextWatcher(edtTwo));
        edtThree.addTextChangedListener(new CustomTextWatcher(edtThree));
        edtFour.addTextChangedListener(new CustomTextWatcher(edtFour));
        edtFive.addTextChangedListener(new CustomTextWatcher(edtFive));
        edtSix.addTextChangedListener(new CustomTextWatcher(edtSix));

        edtOne.setOnKeyListener(new CustomKeyListener(edtOne));
        edtTwo.setOnKeyListener(new CustomKeyListener(edtTwo));
        edtThree.setOnKeyListener(new CustomKeyListener(edtThree));
        edtFour.setOnKeyListener(new CustomKeyListener(edtFour));
        edtFive.setOnKeyListener(new CustomKeyListener(edtFive));
        edtSix.setOnKeyListener(new CustomKeyListener(edtSix));
        //endregion

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String One = edtOne.getText().toString().trim();
                String Two = edtTwo.getText().toString().trim();
                String Three = edtThree.getText().toString().trim();
                String Four = edtFour.getText().toString().trim();
                String Five = edtFive.getText().toString().trim();
                String Six = edtSix.getText().toString().trim();

                OTP = One + Two + Three + Four + Five + Six;

                if (OTP.equals("")) {
                    Toast.makeText(OtpVerificationActivity.this, getResources().getString(R.string.insert_otp), Toast.LENGTH_SHORT).show();
                } else {
                    if (GlobalClass.isNetworkConnected(OtpVerificationActivity.this)) {
                        new VerifyOtpService(OtpVerificationActivity.this, OtpVerificationActivity.this, PhoneNumber, OTP).execute();
                    } else {
                        Toast.makeText(OtpVerificationActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onVerifyOTP(JSONObject jsonObject) {
        if (GlobalClass.isNetworkConnected(OtpVerificationActivity.this)) {
            if (isFrom.equals("SignUp")) {
                new UserVerificationService(OtpVerificationActivity.this, OtpVerificationActivity.this, UserId, Email, PhoneNumber, EmpId).execute();
            } else {
                Intent i = new Intent(OtpVerificationActivity.this, ChangePasswordActivity.class);
                i.putExtra("isFrom", "OTPVerification");
                i.putExtra(Constants.userid, UserId);
                startActivity(i);
            }
        } else {
            Toast.makeText(OtpVerificationActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUserVerification(JSONObject jsonObject) {

        SharedPreferences.Editor editor = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE).edit();
        editor.putString(Constants.userid, UserId);
        editor.putString(Constants.email, Email);
        editor.putString(Constants.firstname, Fname);
        editor.putString(Constants.lastname, Lname);
        editor.putString(Constants.displayname, Name);
        editor.putString(Constants.phonenumber, PhoneNumber);
        editor.putString(Constants.employeeid, EmpId);
        editor.putString(Constants.password, Password);
        editor.putString(Constants.notificationtoken, NotificationToken);
        editor.putString(Constants.udid, DeviceId);
        editor.putString(Constants.appid, AppId);
        editor.putString(Constants.lat, Lat);
        editor.putString(Constants.longi, Long);
        editor.putString(Constants.userrole, UserRole);
        editor.putString(Constants.OTP, redirectOtp);
        editor.apply();

        Intent i = new Intent(this, IntroductionActivity.class);
        i.putExtra("isFrom", GlobalClass.HOME);
        startActivity(i);
        finish();

    }

    //region Otp Edittext Event

    private class CustomTextWatcher implements TextWatcher {

        EditText editText;

        public CustomTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().isEmpty()) {
                switch (editText.getId()) {
                    case R.id.edtOne:
                        setEdtRoundBackground(edtOne, true);
                        edtTwo.requestFocus();
                        break;
                    case R.id.edtTwo:
                        edtThree.requestFocus();
                        setEdtRoundBackground(edtTwo, true);
                        break;
                    case R.id.edtThree:
                        edtFour.requestFocus();
                        setEdtRoundBackground(edtThree, true);
                        break;
                    case R.id.edtFour:
                        edtFive.requestFocus();
                        setEdtRoundBackground(edtFour, true);
                        break;
                    case R.id.edtFive:
                        edtSix.requestFocus();
                        setEdtRoundBackground(edtFive, true);
                        break;
                    case R.id.edtSix:
                        setEdtRoundBackground(edtSix, true);
                        break;
                }
            }
        }
    }

    private class CustomKeyListener implements View.OnKeyListener {

        EditText editText;

        public CustomKeyListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                switch (editText.getId()) {
                    case R.id.edtOne:
                        setEdtRoundBackground(edtOne, false);
                        edtOne.setText("");
                        break;
                    case R.id.edtTwo:
                        edtOne.requestFocus();
                        setEdtRoundBackground(edtTwo, false);
                        edtTwo.setText("");
                        break;
                    case R.id.edtThree:
                        edtTwo.requestFocus();
                        setEdtRoundBackground(edtThree, false);
                        edtThree.setText("");
                        break;
                    case R.id.edtFour:
                        edtThree.requestFocus();
                        setEdtRoundBackground(edtFour, false);
                        edtFour.setText("");
                        break;
                    case R.id.edtFive:
                        edtFour.requestFocus();
                        setEdtRoundBackground(edtFive, false);
                        edtFive.setText("");
                        break;
                    case R.id.edtSix:
                        edtFive.requestFocus();
                        setEdtRoundBackground(edtSix, false);
                        edtSix.setText("");
                        break;
                }
            }
            return false;
        }
    }

    private void setEdtRoundBackground(EditText edtTxt, boolean isEnable) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(getResources().getDimension(R.dimen.D_5dp));
        shape.setColor(isEnable ? getResources().getColor(R.color.themeColor) : getResources().getColor(R.color.grey));
        edtTxt.setBackground(shape);
    }

    //endregion

    private void FindViewById() {

        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        edtOne = (EditText) findViewById(R.id.edtOne);
        edtTwo = (EditText) findViewById(R.id.edtTwo);
        edtThree = (EditText) findViewById(R.id.edtThree);
        edtFour = (EditText) findViewById(R.id.edtFour);
        edtFive = (EditText) findViewById(R.id.edtFive);
        edtSix = (EditText) findViewById(R.id.edtSix);
        btnSubmit = (TextView) findViewById(R.id.btnSubmit);
        otpLout = (LinearLayout) findViewById(R.id.otpLout);

    }

    private void showOTPDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.otp_verification));
        alertDialog.setMessage(getResources().getString(R.string.otp_send_msg));

        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
