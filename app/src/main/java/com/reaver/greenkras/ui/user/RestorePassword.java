package com.reaver.greenkras.ui.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.reaver.greenkras.async.AsyncAuthorization;
import com.reaver.greenkras.R;


public class RestorePassword extends DialogFragment {
    private EditText ET_email_restore;
    private String email;
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.restore, null);
        final AlertDialog adb = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.restore, null)
                .setNeutralButton(R.string.cancel, null)
                .setView(view)
                .create();

        ET_email_restore = view.findViewById(R.id.ET_email_restore);

        adb.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button p = adb.getButton(AlertDialog.BUTTON_POSITIVE);
                Button n = adb.getButton(AlertDialog.BUTTON_NEUTRAL);

                p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CheckNetwork()){
                            email = ET_email_restore.getText().toString();
                            if(TextUtils.isEmpty(email)){
                                Toast.makeText(getContext(), "Поле E-mail не может быть пустым", Toast.LENGTH_LONG).show();
                            }else{
                                Send();
                                dismiss();
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"Проверьте интернет соединение", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                n.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        });
        return adb;

    }

    private boolean CheckNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }

    public void Send(){
        String method ="Restore";
        AsyncAuthorization asyncAuthorization = new AsyncAuthorization(getContext());
        asyncAuthorization.execute(method, email);
    }
}
