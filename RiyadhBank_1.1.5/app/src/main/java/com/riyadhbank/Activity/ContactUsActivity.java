package com.riyadhbank.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Async.AskQuestionService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;
import com.riyadhbank.Utility.Interface;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;

public class ContactUsActivity extends AppCompatActivity implements Interface.OnAskQuestion {

    ImageView back;
    TextView txtTitle;
    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_about, img_logout;
    TextView txt_about, txt_logout, btnSend;
    EditText edtQuestion;
    String UserId, Question;
    LinearLayout callLout, emailLout;
    TextView txtMobNo;

    TextView btnWhatsapp, btnCall, btnEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        FindById();

        SharedPreferences pref = getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE);
        GlobalClass.languageCode = pref.getString(Constants.LanguageCode, "");

        if (GlobalClass.languageCode.equals("en")) {
            edtQuestion.setGravity(Gravity.LEFT);
            // txtMobNo.setText("+966563796573");
            btnWhatsapp.setText("+966563796573");
            btnCall.setText("+966563796573");
        } else {
            edtQuestion.setGravity(Gravity.RIGHT);
            // txtMobNo.setText("966563796573+");
            btnWhatsapp.setText("966563796573+");
            btnCall.setText("966563796573+");
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtTitle.setText(getResources().getString(R.string.contact_us));

        img_about.setImageDrawable(getResources().getDrawable(R.drawable.ic_about_us_select));
        txt_about.setTextColor(getResources().getColor(R.color.themeColor));

        SharedPreferences shared = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        UserId = shared.getString(Constants.userid, "");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Question = edtQuestion.getText().toString().trim();

                if (Question.equals("")) {
                    GlobalClass.showToastMessage(ContactUsActivity.this, getResources().getString(R.string.insert_query));
                } else {
                    if (GlobalClass.isNetworkConnected(ContactUsActivity.this)) {
                        new AskQuestionService(ContactUsActivity.this, ContactUsActivity.this,
                                Question, UserId).execute();
                    } else {
                        GlobalClass.showToastMessage(ContactUsActivity.this, getResources().getString(R.string.internet_msg));
                    }
                }

            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.google.android.gm");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@meritincentives.com"});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUsActivity.this, "Gmail have not been installed.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String url = "https://api.whatsapp.com/send?phone=966563796573&text=" + getResources().getString(R.string.whatsappMsg);
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUsActivity.this, "WhatsApp have not been installed.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        clickMenuType();

    }

    private void checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+966563796573"));
            startActivity(callIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if (permissionCall == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+966563796573"));
            startActivity(callIntent);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onAskQuestion(JSONObject jsonObject) {
        try {
            GlobalClass.showToastMessage(this, getResources().getString(R.string.email_sent));
            Question = "";
            edtQuestion.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FindById() {
        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        img_about = (ImageView) findViewById(R.id.img_about);
        txt_about = (TextView) findViewById(R.id.txt_about);

        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        btnSend = (TextView) findViewById(R.id.btnSend);
        edtQuestion = (EditText) findViewById(R.id.edtQuestion);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

        callLout = (LinearLayout) findViewById(R.id.callLout);
        emailLout = (LinearLayout) findViewById(R.id.emailLout);
        txtMobNo = (TextView) findViewById(R.id.txtMobNo);
        btnWhatsapp = (TextView) findViewById(R.id.btnWhatsapp);
        btnCall = (TextView) findViewById(R.id.btnCall);
        btnEmail = (TextView) findViewById(R.id.btnEmail);
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
                img_about.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile));
                txt_about.setTextColor(getResources().getColor(R.color.grey));
                GlobalClass.logOutUser(ContactUsActivity.this, GlobalClass.ABOUT_US, img_logout, txt_logout, img_about, txt_about, chandLout);
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(ContactUsActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }
}
