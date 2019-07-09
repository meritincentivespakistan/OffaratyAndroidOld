package com.riyadhbank.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.riyadhbank.Async.RedeemVoucherService;
import com.riyadhbank.R;
import com.riyadhbank.Utility.Constants;
import com.riyadhbank.Utility.GlobalClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetVoucherActivity extends AppCompatActivity {

    ImageView back;
    TextView txtTitle, txt_home, txt_favourite, txt_logout;
    LinearLayout chandLout, lout_home, lout_profile, lout_about_us, lout_logout, lout_favourite;
    ImageView img_home, img_favourite, img_logout;

    TextView offerTitle, expiryDate, txtCouponCode, btnUseVoucher, txtSponsor;
    ImageView imgSponsor;
    String isFrom, CouponCode, SponsorImage, OfferTitle, ExpiryDate, SponsorText, Username, ShareMsg;
    ImageView imgWhatsApp, imgEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_voucher);

        FiindById();

        SharedPreferences shared = getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE);
        Username = shared.getString(Constants.displayname, "");

        isFrom = getIntent().getStringExtra("isFrom");
        CouponCode = getIntent().getStringExtra("CouponCode");
        SponsorImage = getIntent().getStringExtra("SponsorImage");
        OfferTitle = getIntent().getStringExtra("OfferTitle");
        ExpiryDate = getIntent().getStringExtra("ExpiryDate");
        SponsorText = getIntent().getStringExtra("SponsorText");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtTitle.setText(getResources().getString(R.string.voucher));

        if (isFrom.equals(GlobalClass.HOME)) {
            img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home_select));
            txt_home.setTextColor(getResources().getColor(R.color.themeColor));
        } else {
            chandLout.setBackground(getResources().getDrawable(R.drawable.icn_chand_theme));
            img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite_select));
            txt_favourite.setTextColor(getResources().getColor(R.color.themeColor));
        }

        clickMenuType();

        offerTitle.setText(OfferTitle);
        expiryDate.setText(ExpiryDate);
        txtCouponCode.setText(CouponCode);
        txtSponsor.setText(SponsorText);
        Glide.with(this)
                .load(SponsorImage)
                .into(imgSponsor);

        btnUseVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redeemDialog();
            }
        });

        ShareMsg = getResources().getString(R.string.receive_gift) + " " + Username + "\n\n" +
                getResources().getString(R.string.voucher_code_is) + " " + CouponCode;

        imgWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, ShareMsg);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(GetVoucherActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        imgEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.google.android.gm");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.receive_gift) + " " + Username);
                sendIntent.putExtra(Intent.EXTRA_TEXT, ShareMsg);
                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(GetVoucherActivity.this, "Gmail have not been installed.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void FiindById() {

        back = (ImageView) findViewById(R.id.back);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        img_home = (ImageView) findViewById(R.id.img_home);
        txt_home = (TextView) findViewById(R.id.txt_home);
        img_favourite = (ImageView) findViewById(R.id.img_favourite);
        txt_favourite = (TextView) findViewById(R.id.txt_favourite);
        img_logout = (ImageView) findViewById(R.id.img_logout);
        txt_logout = (TextView) findViewById(R.id.txt_logout);

        chandLout = (LinearLayout) findViewById(R.id.chandLout);
        lout_home = (LinearLayout) findViewById(R.id.lout_home);
        lout_profile = (LinearLayout) findViewById(R.id.lout_profile);
        lout_about_us = (LinearLayout) findViewById(R.id.lout_about_us);
        lout_logout = (LinearLayout) findViewById(R.id.lout_logout);
        lout_favourite = (LinearLayout) findViewById(R.id.lout_favourite);

        offerTitle = (TextView) findViewById(R.id.offerTitle);
        expiryDate = (TextView) findViewById(R.id.expiryDate);
        txtCouponCode = (TextView) findViewById(R.id.txtCouponCode);
        btnUseVoucher = (TextView) findViewById(R.id.btnUseVoucher);
        imgSponsor = (ImageView) findViewById(R.id.imgSponsor);
        txtSponsor = (TextView) findViewById(R.id.txtSponsor);
        imgWhatsApp = (ImageView) findViewById(R.id.imgWhatsApp);
        imgEmail = (ImageView) findViewById(R.id.imgEmail);

    }

    private void redeemDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_redeem_voucher);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final EditText edtPinCode = (EditText) dialog.findViewById(R.id.edtPinCode);
        LinearLayout btnClose = (LinearLayout) dialog.findViewById(R.id.btnClose);
        TextView btnSubmit = (TextView) dialog.findViewById(R.id.btnSubmit);

        if (GlobalClass.languageCode.equals("en")) {
            edtPinCode.setGravity(Gravity.LEFT);
        } else {
            edtPinCode.setGravity(Gravity.RIGHT);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String MerchantPin = edtPinCode.getText().toString().trim();

                if (MerchantPin.equals("")) {
                    Toast.makeText(GetVoucherActivity.this, getResources().getString(R.string.insert_pin), Toast.LENGTH_SHORT).show();
                } else {
                    if (GlobalClass.isNetworkConnected(GetVoucherActivity.this)) {
                        new RedeemVoucherService(GetVoucherActivity.this, CouponCode, MerchantPin, isFrom).execute();
                    } else {
                        Toast.makeText(GetVoucherActivity.this, (getResources().getString(R.string.internet_msg)), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

                if (isFrom.equals(GlobalClass.HOME)) {
                    img_home.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
                    txt_home.setTextColor(getResources().getColor(R.color.grey));
                    GlobalClass.logOutUser(GetVoucherActivity.this, GlobalClass.HOME, img_logout, txt_logout, img_home, txt_home, chandLout);
                } else {
                    img_favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favourite));
                    txt_favourite.setTextColor(getResources().getColor(R.color.grey));
                    GlobalClass.logOutUser(GetVoucherActivity.this, GlobalClass.FAVOURITE, img_logout, txt_logout, img_favourite, txt_favourite, chandLout);
                }
            }
        });

    }

    private void navigateMenu(String type) {
        Intent i = new Intent(GetVoucherActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isFrom", type);
        startActivity(i);
    }

}
