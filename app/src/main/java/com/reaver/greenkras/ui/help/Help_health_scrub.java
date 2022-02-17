package com.reaver.greenkras.ui.help;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;

import com.reaver.greenkras.R;

public class Help_health_scrub extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState ) {

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.health_scrub_dialog, null);

        ImageButton close = view.findViewById(R.id.close_help);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        AlertDialog.Builder adb = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme)
                .setView(view);

        return adb.create();
    }
}

