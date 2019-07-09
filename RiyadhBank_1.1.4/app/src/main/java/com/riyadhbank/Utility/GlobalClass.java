package com.riyadhbank.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riyadhbank.Activity.ChangePasswordActivity;
import com.riyadhbank.Activity.LoginActivity;
import com.riyadhbank.Activity.MainActivity;
import com.riyadhbank.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class GlobalClass {

    public static String Riyadh = "Riyadh";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String languageCode = "";
    public static String isWelcomDialogShow = "0";

    public static String HOME = "home";
    public static String PROFILE = "profile";
    public static String FAVOURITE = "favourite";
    public static String ABOUT_US = "about_us";
    public static String LOGOUT = "logout";

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void showToastMessage(Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
    }

    //region Change Language
    public static void changeLanguage(Activity activity, String Language_Code) {

        Locale locale = new Locale(Language_Code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config, null);

        SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.LanguagePref, MODE_PRIVATE).edit();
        editor.putString(Constants.LanguageCode, Language_Code);
        editor.commit();
    }
    //endregion

    //region Logout Dialog
    public static void logOutUser(final Activity activity, final String pageType, final ImageView img, final TextView txt, final ImageView changeImg, final TextView changeTxt, final LinearLayout chandLout) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.offaraty));
        alertDialog.setMessage(activity.getResources().getString(R.string.logout_msg));

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

                img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_logout));
                txt.setTextColor(activity.getResources().getColor(R.color.grey));
                chandLout.setBackground(activity.getResources().getDrawable(R.drawable.icn_chand_grey));

                if (pageType.equals(GlobalClass.HOME)) {
                    changeImg.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_home_select));
                } else if (pageType.equals(GlobalClass.PROFILE)) {
                    changeImg.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_profile_select));
                } else if (pageType.equals(GlobalClass.FAVOURITE)) {
                    chandLout.setBackground(activity.getResources().getDrawable(R.drawable.icn_chand_theme));
                    changeImg.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_favourite_select));
                } else if (pageType.equals(GlobalClass.ABOUT_US)) {
                    changeImg.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_about_us_select));
                }

                changeTxt.setTextColor(activity.getResources().getColor(R.color.themeColor));
            }
        });

        alertDialog.setPositiveButton(activity.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.UserDetailPref, MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                dialog.cancel();
                Intent i = new Intent(activity, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(i);
            }
        });

        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel_), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();

    }
    //endregion

}


