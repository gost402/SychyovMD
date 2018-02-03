package com.example.dsychyov.sychyovmd.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.dsychyov.sychyovmd.R;

public class ThemeChooserFragment extends Fragment {
    private boolean isDark;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme_chooser, container, false);

        setActiveRadioButton(view);
        initializeRadioButtonsListeners(view);

        return view;
    }

    public boolean isDark() {
        return isDark;
    }

    private void initializeRadioButtonsListeners(@NonNull final View fragmentView) {
        View.OnClickListener darkThemeListener = newRadioButtonListenerInstance(fragmentView, true);
        View.OnClickListener lightThemeListener = newRadioButtonListenerInstance(fragmentView, false);

        fragmentView.findViewById(R.id.light_theme_radio_button_wrapper).setOnClickListener(lightThemeListener);
        fragmentView.findViewById(R.id.light_theme_radio_button).setOnClickListener(lightThemeListener);
        fragmentView.findViewById(R.id.dark_theme_radio_button_wrapper).setOnClickListener(darkThemeListener);
        fragmentView.findViewById(R.id.dark_theme_radio_button).setOnClickListener(darkThemeListener);
    }

    private void setActiveRadioButton(View view) {
        RadioButton lightThemeRadioButton = getLightThemeRadioButton(view);
        RadioButton darkThemeRadioButton = getDarkThemeRadioButton(view);

        darkThemeRadioButton.setChecked(isDark);
        lightThemeRadioButton.setChecked(!isDark);
    }

    private RadioButton getLightThemeRadioButton(@NonNull final View view){
        return view.findViewById(R.id.light_theme_radio_button);
    }

    private RadioButton getDarkThemeRadioButton(@NonNull final View view){
        return view.findViewById(R.id.dark_theme_radio_button);
    }

    private View.OnClickListener newRadioButtonListenerInstance(@NonNull final View fragmentView, final boolean newIsDark) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDark = newIsDark;
                setActiveRadioButton(fragmentView);
            }
        };
    }
}
