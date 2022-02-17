package com.reaver.greenkras.ui.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.reaver.greenkras.R;
import java.util.Objects;

public class MapDialog extends DialogFragment implements DialogInterface.OnClickListener{
    public  String res = "";
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.map_dialog, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(),R.style.MyDialogTheme)
                .setPositiveButton("Да", this)
                .setNegativeButton("Отмена", this)
                .setView(view);
        RadioButton none = view.findViewById(R.id.rbNone);
        RadioButton remove = view.findViewById(R.id.rbRemove);
        RadioButton fine = view.findViewById(R.id.rbFine);
        RadioButton normal = view.findViewById(R.id.rbNormal);
        RadioButton damaged = view.findViewById(R.id.rbDamaged);
        none.setOnClickListener(rbClickListener);
        remove.setOnClickListener(rbClickListener);
        fine.setOnClickListener(rbClickListener);
        normal.setOnClickListener(rbClickListener);
        damaged.setOnClickListener(rbClickListener);

        return adb.create();
    }
    private View.OnClickListener rbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.rbNone: res = "Дерева нет";
                    break;
                case R.id.rbRemove: res = "Погибло/Уничтожено";
                    break;
                case R.id.rbFine: res = "Хорошее";
                    break;
                case R.id.rbNormal: res = "Среднее";
                    break;
                case R.id.rbDamaged: res = "Плохое";
                    break;

            }
        }
    };

    public void onClick(DialogInterface dialog, int which) {
        if (!res.equals("")) {
            String TAG_WEIGHT_SELECTED = "yes";
            switch (which) {
            case Dialog.BUTTON_POSITIVE:
                Intent intent = new Intent();
                intent.putExtra(TAG_WEIGHT_SELECTED, -1);
                intent.putExtra("res",res);
                Objects.requireNonNull(getTargetFragment()).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                dismiss();
                break;
            case Dialog.BUTTON_NEUTRAL:
                dismiss();
                break;
        }
        }else {
            Toast.makeText(getContext(),"Вы ничего не выбрали.\nВаш голос не был засчитан.", Toast.LENGTH_LONG).show();
        }

    }
}
