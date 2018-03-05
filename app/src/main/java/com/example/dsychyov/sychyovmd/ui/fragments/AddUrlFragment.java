package com.example.dsychyov.sychyovmd.ui.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.dsychyov.sychyovmd.R;
import com.example.dsychyov.sychyovmd.async_tasks.launcher.InsertDesktopApp;
import com.example.dsychyov.sychyovmd.viewmodel.DesktopApp;

public class AddUrlFragment extends DialogFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_add_url, container, false);

        view.findViewById(R.id.add_url_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                EditText urlView = view.findViewById(R.id.url_input);
                EditText nameView = view.findViewById(R.id.name_input);
                String url = urlView.getText().toString();
                String name = nameView.getText().toString();

                boolean errorAdded = false;
                boolean validUrl;

                try {
                    Uri.parse(url);
                    validUrl = true;
                } catch (Exception e1) {
                    validUrl = false;
                }

                if(!validUrl) {
                    urlView.setError(getResources().getString(R.string.invalid_url));
                    errorAdded = true;
                }

                if(name.length() == 0) {
                    nameView.setError(getResources().getString(R.string.invalid_name));
                    errorAdded = true;
                }

                if(errorAdded) {
                    return;
                }

                new InsertDesktopApp(name, url, DesktopApp.Type.URI).execute();
                dismiss();
            }
        });

        view.findViewById(R.id.cancel_add_url_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
