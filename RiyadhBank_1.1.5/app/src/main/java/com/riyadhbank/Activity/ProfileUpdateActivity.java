package com.riyadhbank.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.UpdateProfileService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;

public class ProfileUpdateActivity extends AppCompatActivity implements Interface.OnUserProfileUpdate {

    ImageView back;
    TextView txtTitle, btnProfileUpdate;
    LinearLayout chandLout,lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_profile,img_logout;
    TextView txt_profile,txt_logout;
    EditText edtFname, edtLname, edtName, edtEmail, edtEmpId, edtMobileNumber;
    String UserId, Fname, Lname, Name, Email, EmpId, MobileNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        FindById();

        SharedPreferences prefs = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = prefs.getString(Constants.LanguageCode, "");

        if (GlobalClass.languageCode.equals("en")) {
            edtFname.setGravity(Gravity.LEFT);
            edtLname.setGravity(Gravity.LEFT);
            edtName.setGravity(Gravity.LEFT);
            edtEmail.setGravity(Gravity.LEFT);
            edtEmpId.setGravity(Gravity.LEFT);
            edtMobileNumber.setGravity(Gravity.LEFT);
        } else {
            edtFname.setGravity(Gravity.RIGHT);
            edtLname.setGravity(Gravity.RIGHT);
            edtName.setGravity(Gravity.RIGHT);
            edtEmail.setGravity(Gravity.RIGHT);
            edtEmpId.setGravity(Gravity.RIGHT);
            edtMobileNumber.setGravity(Gravity.RIGHT);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtTitle.setText(getResources().getString(R.string.profile_update));

        img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_select));
        txt_profile.setTextColor(getResources().getColor(R.color.themeColor));

        clickMenuType();

        SharedPreferences shared = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");
        Fname = shared.getString(Constants.firstname, "");
        Lname = shared.getString(Constants.lastname, "");
        Name = shared.getString(Constants.displayname, "");
        Email = shared.getString(Constants.email, "");
        EmpId = shared.getString(Constants.employeeid, "");
        MobileNumber = shared.getString(Constants.phonenumber, "");

        edtFname.setText(Fname);
        edtLname.setText(Lname);
        edtName.setText(Name);
        edtEmail.setText(Email);
        edtEmpId.setText(EmpId);
        edtMobileNumber.setText(MobileNumber);

        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fname = edtFname.getText().toString().trim();
                Lname = edtLname.getText().toString().trim();
                Name = edtName.getText().toString().trim();
                Email = edtEmail.getText().toString().trim();
                EmpId = edtEmpId.getText().toString().trim();
                MobileNumber = edtMobileNumber.getText().toString().trim();

                if (Fname.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (Lname.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (Name.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (Email.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (EmpId.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else if (MobileNumber.equals("")) {
                    showToastMessage(getResources().getString(R.string.check_empty));
                } else {
                    if (GlobalClass.isNetworkConnected(ProfileUpdateActivity.this)) {
                        new UpdateProfileService(ProfileUpdateActivity.this, ProfileUpdateActivity.this,
                                UserId, Fname, Lname, Name, Email, EmpId, MobileNumber).execute();
                    } else {
                        showToastMessage(getResources().getString(R.string.internet_msg));
                    }
                }

            }
        });

        edtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edtMobileNumber.requestFocus();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onUserProfileUpdate(JSONObject jsonObject) {
        try {

            JSONObject userObject = new JSONObject(jsonObject.get("userdetails").toString());

            SharedPreferences.Editor editor = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE).edit();
            editor.putString(Constants.userid, userObject.get(Constants.userid).toString());
            editor.putString(Constants.email, userObject.get(Constants.email).toString());
            editor.putString(Constants.firstname, userObject.get(Constants.firstname).toString());
            editor.putString(Constants.lastname, userObject.get(Constants.lastname).toString());
            editor.putString(Constants.displayname, userObject.get(Constants.displayname).toString());
            editor.putString(Constants.phonenumber, userObject.get(Constants.phonenumber).toString());
            editor.putString(Constants.employeeid, userObject.get(Constants.employeeid).toString());
            editor.putString(Constants.notificationtoken, userObject.get(Constants.notificationtoken).toString());
            editor.putString(Constants.password, userObject.get(Constants.password).toString());
            editor.putString(Constants.udid, userObject.get(Constants.udid).toString());
            editor.putString(Constants.appid, userObject.get(Constants.appid).toString());
            editor.putString(Constants.lat, userObject.get(Constants.lat).toString());
            editor.putString(Constants.longi, userObject.get(Constants.longi).toString());
            editor.putString(Constants.userrole, userObject.get(Constants.userrole).toString());
            editor.apply();

            showToastMessage(jsonObject.getString("msg").toLowerCase());
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToastMessage(String Message) {
        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
    }

    private void FindById() {
        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        img_profile = (ImageView) findViewById(R.id.img_profile);
        txt_profile = (TextView) findViewById(R.id.txt_profile);

        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

        edtFname = (EditText) findViewById(R.id.edtFname);
        edtLname = (EditText) findViewById(R.id.edtLname);
        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtEmpId = (EditText) findViewById(R.id.edtEmpId);
        edtMobileNumber = (EditText) findViewById(R.id.edtMobileNumber);

        btnProfileUpdate = (TextView) findViewById(R.id.btnProfileUpdate);

    }

    private void clickMenuType() {

        lout_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.HOME);
            }
        });

        lout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.PROFILE);
            }
        });

        lout_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.FAVOURITE);
            }
        });

        lout_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateMenu(GlobalClass.ABOUT_US);
            }
        });

        lout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_logout.setImageDrawable(getResources().getDrawable(R.drawable.ic_logout_select));
                txt_logout.setTextColor(getResources().getColor(R.color.themeColor));
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
                txt_profile.setTextColor(getResources().getColor(R.color.grey));
                GlobalClass.logOutUser(ProfileUpdateActivity.this, GlobalClass.PROFILE, img_logout, txt_logout, img_profile, txt_profile, chandLout);
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(ProfileUpdateActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

}
