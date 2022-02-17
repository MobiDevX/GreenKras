package com.reaver.greenkras.ui.poligon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.reaver.greenkras.R;

public class DialogPark extends DialogFragment implements DialogInterface.OnClickListener {

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme)
                .setPositiveButton(R.string.hide, this)
                .setView(R.layout.fragment_dialog_park);

        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            Toast.makeText(getContext(), "Будьте внимательны :)", Toast.LENGTH_LONG).show();
            dismiss();
        }

    }



}