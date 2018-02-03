package com.example.dsychyov.sychyovmd.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_chooser, container, false);

        setActiveRadioButton(view);
        initializeRadioButtonsListeners(view);

        return view;
    }

    public boolean isDense() {
        return isDense;
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
                isDense = newIsDense;
                setActiveRadioButton(fragmentView);
            }
        };
    }
}
