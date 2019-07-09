package com.riyadhbank.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riyadhbank.Activity.ChangePasswordActivity;
import com.riyadhbank.Activity.ProfileUpdateActivity;
import com.riyadhbank.R;

public class ProfileFragment extends Fragment {

    ImageView back;
    TextView txtTitle, btnProfileUpdate, btnChangePassword;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        FindById();

        back.setVisibility(View.GONE);
        txtTitle.setText(getActivity().getResources().getString(R.string.profile));


        btnProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ProfileUpdateActivity.class);
                startActivity(i);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
                i.putExtra("isFrom", "ChangePassword");
                startActivity(i);
            }
        });

        return view;
    }

    private void FindById() {

        back = (ImageView) view.findViewById(R.id.back);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        btnProfileUpdate = (TextView) view.findViewById(R.id.btnProfileUpdate);
        btnChangePassword = (TextView) view.findViewById(R.id.btnChangePassword);

    }

}
