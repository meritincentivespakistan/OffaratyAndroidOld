package com.riyadhbank.Utility;

import com.riyadhbank.Modal.FavouriteModal;
import com.riyadhbank.Modal.OfferModal;

import org.json.JSONObject;

public class Interface {

    public interface OnSignUp {
        void onSignUp(JSONObject jsonObject);
    }

    public interface OnCheckNewUser {
        void onCheckNewUser(JSONObject jsonObject);
    }

    public interface OnLogin {
        void onLogin(JSONObject jsonObject);
    }

    public interface OnUserVerification {
        void onUserVerification(JSONObject jsonObject);
    }

    public interface OnUserProfileUpdate {
        void onUserProfileUpdate(JSONObject jsonObject);
    }

    public interface OnChangePassword {
        void onChangePassword(JSONObject jsonObject);
    }

    public interface OnAskQuestion {
        void onAskQuestion(JSONObject jsonObject);
    }

    public interface OnIntroduction {
        void onIntroduction(JSONObject jsonObject);
    }

    public interface OnForgotPassword {
        void onForgotPassword(JSONObject jsonObject);
    }

    public interface OnAboutUs {
        void onAboutUs(JSONObject jsonObject);
    }

    public interface OnWelcomeMsg {
        void onWelcomeMsg(JSONObject jsonObject);
    }

    public interface OnDashboard {
        void onDashboard(JSONObject jsonObject);
    }

    public interface OnOfferCategory {
        void onOfferCategory(String json);
    }

    public interface OnOfferDetail {
        void onOfferDetail(JSONObject jsonObject);
    }

    public interface OnFavouriteList {
        void onFavouriteList(String json);
    }

    public interface OnClickHomeOrCategoryFavourite{
        void onClickHomeOrCategoryFavourite(OfferModal offerModal, int position);
    }

    public interface OnFavourite{
        void onFavourite(int position);
    }

    public interface OnClickFavourite{
        void onClickFavourite(FavouriteModal favouriteModal, int position);
    }

    public interface OnGetVoucher{
        void onGetVoucher(JSONObject jsonObject);
    }

    public interface OnGetOTP{
        void onGetOTP(JSONObject jsonObject);
    }

    public interface OnVerifyOTP{
        void onVerifyOTP(JSONObject jsonObject);
    }

    public interface OnVersionCheck{
        void onVersionCheck(JSONObject jsonObject);
    }

    public interface OnSearch {
        void onSearch(JSONObject jsonObject);
    }

}
