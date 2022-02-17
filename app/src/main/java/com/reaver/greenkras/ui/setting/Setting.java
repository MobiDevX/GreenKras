package com.reaver.greenkras.ui.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.messaging.FirebaseMessaging;
import com.reaver.greenkras.MainActivity;
import com.reaver.greenkras.R;

public class Setting extends Fragment {
    private Switch swTheme, swUpload, swUploadPark, swPush;
    private String Saved_theme;
    private String Saved_upload;
    private String Saved_park;
    private String Saved_push;
    private SharedPreferences.Editor themeEditor, uploadEditor, uploadParkEditor, pushEditor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        swTheme = view.findViewById(R.id.switchTheme);
        swUpload = view.findViewById(R.id.switchUpload);
        swUploadPark = view.findViewById(R.id.switchUploadPark);
        swPush = view.findViewById(R.id.switchPush);

        SharedPreferences push = requireContext().getSharedPreferences("Push",Context.MODE_PRIVATE);
        String pushEnable = push.getString(Saved_push, "True");
        pushEditor = push.edit();

        SharedPreferences themePref = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        String theme = themePref.getString(Saved_theme, "False");
        themePref = requireContext().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        themeEditor = themePref.edit();

        SharedPreferences uploadPref = requireContext().getSharedPreferences("Upload", Context.MODE_PRIVATE);
        String upload = uploadPref.getString(Saved_upload, "True");
        uploadPref = requireContext().getSharedPreferences("Upload", Context.MODE_PRIVATE);
        uploadEditor = uploadPref.edit();

        SharedPreferences uploadParkPref = requireContext().getSharedPreferences("UploadPark", Context.MODE_PRIVATE);
        String uploadpark = uploadParkPref.getString(Saved_park, "True");
        uploadParkPref = requireContext().getSharedPreferences("UploadPark", Context.MODE_PRIVATE);
        uploadParkEditor = uploadParkPref.edit();


        swPush.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swPush.isChecked()){
                    FirebaseMessaging.getInstance().subscribeToTopic("APP");
                    pushEditor.putString(Saved_push, "True");
                    pushEditor.apply();
                }
                else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("APP");
                    pushEditor.putString(Saved_push, "False");
                    pushEditor.apply();
                }
            }
        });



        swUpload.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swUpload.isChecked()){
                    uploadEditor.putString(Saved_upload, "True");
                    uploadEditor.apply();
                }
                else {
                    uploadEditor.putString(Saved_upload, "False");
                    uploadEditor.apply();
                }
            }
        });

        swUploadPark.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swUploadPark.isChecked()){
                    uploadParkEditor.putString(Saved_park, "True");
                    uploadParkEditor.apply();
                }
                else {
                    uploadParkEditor.putString(Saved_park, "False");
                    uploadParkEditor.apply();
                }
            }
        });

            swTheme.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (swTheme.isChecked()){
                        themeEditor.putString(Saved_theme, "True").apply();
                        themeEditor.commit();
                    }
                    else {
                        themeEditor.putString(Saved_theme, "False").apply();
                        themeEditor.commit();
                    }
                }
            });
            swTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    requireContext().startActivity(intent);
                    requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    ((Activity)requireContext()).finish();

                }
            });


        if (pushEnable.equals("True")){
            swPush.setChecked(true);
        }else
            swPush.setChecked(false);


        if (theme.equals("True")){
            swTheme.setChecked(true);
        }else
            swTheme.setChecked(false);

        if (upload.equals("True")){
            swUpload.setChecked(true);
        }else
            swUpload.setChecked(false);

        if (uploadpark.equals("True")){
            swUploadPark.setChecked(true);
        }else
            swUploadPark.setChecked(false);


        return view;
    }
}
