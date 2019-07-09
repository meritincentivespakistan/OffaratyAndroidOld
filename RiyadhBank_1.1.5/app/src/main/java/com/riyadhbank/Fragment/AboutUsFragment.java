package com.riyadhbank.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riyadhbank.Activity.AboutUsActivity;
import com.riyadhbank.Activity.ContactUsActivity;
import com.riyadhbank.R;

public class AboutUsFragment extends Fragment {

    ImageView back;
    TextView txtTitle, btnAboutUs, btnSupport;
    RelativeLayout mainLout;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_us, container, false);

        FindById();

        back.setVisibility(View.GONE);
        txtTitle.setText(getActivity().getResources().getString(R.string.about_us));

        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(i);
            }
        });

        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void FindById() {

        back = (ImageView) view.findViewById(R.id.back);

        mainLout = (RelativeLayout) view.findViewById(R.id.mainLout);

        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        btnAboutUs = (TextView) view.findViewById(R.id.btnAboutUs);
        btnSupport = (TextView) view.findViewById(R.id.btnSupport);

    }

}
