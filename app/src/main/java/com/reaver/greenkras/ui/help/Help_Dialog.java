package com.reaver.greenkras.ui.help;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.reaver.greenkras.R;



public class Help_Dialog extends DialogFragment {
    private String Name, Disc;
    private int Photo;

    Help_Dialog(String Name, Integer Photo, String Disc){
        this.Name = Name;
        this.Photo = Photo;
        this.Disc = Disc;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState ) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_help, null);
        TextView name = view.findViewById(R.id.nameObject);
        TextView dis = view.findViewById(R.id.disObject);
        ImageView image = view.findViewById(R.id.imageObject);
        ImageButton close = view.findViewById(R.id.close_help);
        name.setText(Name);
        image.setImageResource(Photo);
        dis.setText(Disc);

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
