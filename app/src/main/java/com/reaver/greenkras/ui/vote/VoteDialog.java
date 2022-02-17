package com.reaver.greenkras.ui.vote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.MainActivity;
import com.reaver.greenkras.R;
import com.reaver.greenkras.async.AsyncVotePark;

import java.util.ArrayList;
import java.util.List;


public class VoteDialog extends DialogFragment implements DialogInterface.OnClickListener, OnMapReadyCallback {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private String Name, Cords, User,  Yes, No, CenterX, CenterY;
    private Double lat = 0.0;
    private List<LatLng> plotPolygon = new ArrayList<>();
    private String vote;

    VoteDialog(Context context, String Name, String Cords, String CenterX, String CenterY, String User, String Count, String Yes, String No){
        this.Name = Name;
        this.Cords = Cords;
        this.CenterX = CenterX;
        this.CenterY = CenterY;
        this.User = User;
        this.Yes = Yes;
        this.No = No;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState ) {
        Mapbox.getInstance(requireContext().getApplicationContext(), "pk.eyJ1IjoicmVhdmVyNzE3IiwiYSI6ImNqdm5pb2VwajB2OXA0OGxldWo0bHN6MWIifQ.HQsK7BFinOlmFYEtDQyRVQ");
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.vote_dialog_park, null);
        TextView parkname = view.findViewById(R.id.tvpark_name);
        TextView login = view.findViewById(R.id.tvlogin);
        parkname.setText(Name);
        vote = "";

        String[] split = Cords.split(",");
        for(int i = 0; i<split.length; i++) {
            if ( i % 2 == 0 ){
                lat = Double.parseDouble(split[i]);
            }
            else{
                double lon = Double.parseDouble(split[i]);
                LatLng latLng = new LatLng(lat, lon);
                plotPolygon.add(latLng);}
        }

        login.setText("Добавил пользователь: "+User);
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme)
                .setPositiveButton("За("+Yes+")", this)
                .setNeutralButton("Отмена",this)
                .setNegativeButton("Против("+No+")", this)
                .setView(view);


        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.onStart();

        return adb.create();
    }
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                vote = "true";
                Send();
                Intent intentYes = new Intent(getContext(), MainActivity.class);
                requireContext().startActivity(intentYes);
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                ((Activity)requireContext()).finish();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                vote = "false";
                Send();
                Intent intentNo = new Intent(getContext(), MainActivity.class);
                requireContext().startActivity(intentNo);
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                ((Activity)requireContext()).finish();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                vote = "";
                dismiss();
                break;
        }
    }

    public void Send(){
        AsyncVotePark asyncVotePark = new AsyncVotePark(getContext());
        asyncVotePark.execute(Cords,vote);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        VoteDialog.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {
                        double lat = Double.parseDouble(CenterX);
                        double lon = Double.parseDouble(CenterY);
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(lat,lon))
                                .zoom(15.5)
                                .tilt(0)
                                .build();

                        mapboxMap.addPolygon(new PolygonOptions()
                                .fillColor(Color.argb(20,0,0,0))
                                .addAll(plotPolygon)
                                .alpha(3));
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 300);
                    }
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onStop();
    }
}