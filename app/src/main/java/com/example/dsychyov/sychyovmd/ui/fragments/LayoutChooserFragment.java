package com.example.dsychyov.sychyovmd.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.dsychyov.sychyovmd.R;

public class LayoutChooserFragment extends Fragment {
    private boolean isDense;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isDense = preferences.getBoolean(getResources().getString(R.string.launcher_layout_dense_key), false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_chooser, container, false);

        setActiveRadioButton(view);
        initializeRadioButtonsListeners(view);

        return view;
    }

    private void initializeRadioButtonsListeners(@NonNull final View fragmentView) {
        View.OnClickListener denseLayoutListener = newRadioButtonListenerInstance(fragmentView, true);
        View.OnClickListener standardLayoutListener = newRadioButtonListenerInstance(fragmentView, false);

        fragmentView.findViewById(R.id.standard_layout_radio_button).setOnClickListener(standardLayoutListener);
        fragmentView.findViewById(R.id.standard_layout_radio_button_wrapper).setOnClickListener(standardLayoutListener);
        fragmentView.findViewById(R.id.dense_layout_radio_button).setOnClickListener(denseLayoutListener);
        fragmentView.findViewById(R.id.dense_layout_radio_button_wrapper).setOnClickListener(denseLayoutListener);
    }

    private void setActiveRadioButton(View view) {
        RadioButton standardLayoutRadioButton = getStandardLayoutRadioButton(view);
        RadioButton denseLayoutRadioButton = getDenseLayoutRadioButton(view);

        denseLayoutRadioButton.setChecked(isDense);
        standardLayoutRadioButton.setChecked(!isDense);
    }

    private RadioButton getStandardLayoutRadioButton(@NonNull final View view){
        return view.findViewById(R.id.standard_layout_radio_button);
    }

    private RadioButton getDenseLayoutRadioButton(@NonNull final View view){
        return view.findViewById(R.id.dense_layout_radio_button);
    }

    private View.OnClickListener newRadioButtonListenerInstance(@NonNull final View fragmentView, final boolean newIsDense) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDense == newIsDense) {
                    return;
                }

                isDense = newIsDense;
                setActiveRadioButton(fragmentView);
                changePreferences();
            }
        };
    }

    private void changePreferences() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.launcher_layout_dense_key), isDense);
        editor.apply();
    }
}
