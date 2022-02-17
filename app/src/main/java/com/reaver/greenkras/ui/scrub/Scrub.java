package com.reaver.greenkras.ui.scrub;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.async.AsyncAddData;
import com.reaver.greenkras.R;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.reaver.greenkras.ui.PhotoDialog;
import com.reaver.greenkras.ui.help.Help_health_scrub;
import com.reaver.greenkras.ui.help.Help_scrub_name;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scrub extends Fragment implements OnMapReadyCallback, PermissionsListener{
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Location location;
    private TextView Scrub_coordi;
    private EditText ET_ploshad;
    private String weight;
    private ImageView ivImage;
    private String Scrub_nameschub, Scrub_hp, Scrub_tip,Scrub_ploshad, Scrub_coordix, Scrub_coordiy;
    private AutoCompleteTextView ScrubComplete;
    private FloatingActionButton fabme;
    private DBHelper dbHelper;
    private ArrayAdapter<?> adapter;


    public Scrub() {

    }

    private boolean CheckNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

    }

        @SuppressLint("ClickableViewAccessibility")
        public View onCreateView(@NonNull LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            Mapbox.getInstance(requireContext().getApplicationContext(), "pk.eyJ1IjoicmVhdmVyNzE3IiwiYSI6ImNqdm5pb2VwajB2OXA0OGxldWo0bHN6MWIifQ.HQsK7BFinOlmFYEtDQyRVQ");
            final View view = inflater.inflate(R.layout.fragment_scrub, container, false);
            dbHelper = new DBHelper(getContext());
            mapView =  view.findViewById(R.id.mapView);
            ImageButton help = view.findViewById(R.id.helpScrub);
            ImageButton helpHealth = view.findViewById(R.id.helpScrubHealth);
            Button SendScrub = view.findViewById(R.id.SendScrub);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
            final ScrollView sv = view.findViewById(R.id.scrollView);
            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            sv.requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            sv.requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return mapView.onTouchEvent(event);
                }
            });
            Scrub_coordi= view.findViewById(R.id.Scrub_coordi);
            ET_ploshad = view.findViewById(R.id.Scrub_ploshad);
            ivImage = view.findViewById(R.id.imageToEncode);
            Scrub_coordi.setText("Чтобы получить координаты изменяемого объекта, кликните по карте в предполагаемом месте его нахождения");
            ScrubComplete = view.findViewById(R.id.autoCompleteTextScrub);
            SearchAdapter();
            ScrubComplete.setAdapter(adapter);
            fabme = view.findViewById(R.id.fabme);
            RadioButton RadioButtonSostScrub1 = view.findViewById(R.id.radioButton);
            RadioButtonSostScrub1.setOnClickListener(radioButtonClickListener1);
            RadioButton RadioButtonSostScrub2 = view.findViewById(R.id.radioButton2);
            RadioButtonSostScrub2.setOnClickListener(radioButtonClickListener1);
            RadioButton RadioButtonSostScrub3 = view.findViewById(R.id.radioButton3);
            RadioButtonSostScrub3.setOnClickListener(radioButtonClickListener1);
            RadioButton RadioButtonTipScrub1 = view.findViewById(R.id.radioButton4);
            RadioButtonTipScrub1.setOnClickListener(radioButtonClickListener2);
            RadioButton RadioButtonTipScrub2 = view.findViewById(R.id.radioButton5);
            RadioButtonTipScrub2.setOnClickListener(radioButtonClickListener2);
            RadioButton RadioButtonTipScrub3 = view.findViewById(R.id.radioButton6);
            RadioButtonTipScrub3.setOnClickListener(radioButtonClickListener2);
            RadioButton RadioButtonTipScrub4 = view.findViewById(R.id.radioButton7);
            RadioButtonTipScrub4.setOnClickListener(radioButtonClickListener2);
            RadioButton RadioButtonTipScrub5 = view.findViewById(R.id.radioButton8);
            RadioButtonTipScrub5.setOnClickListener(radioButtonClickListener2);
            RadioButton RadioButtonTipScrub6 = view.findViewById(R.id.radioButton9);
            RadioButtonTipScrub6.setOnClickListener(radioButtonClickListener2);
            RadioButton RadioButtonTipScrub7 = view.findViewById(R.id.radioButton10);
            RadioButtonTipScrub7.setOnClickListener(radioButtonClickListener2);
            RadioButton RadioButtonTipScrub8 = view.findViewById(R.id.radioButton11);
            RadioButtonTipScrub8.setOnClickListener(radioButtonClickListener2);

            if(Build.VERSION.SDK_INT >= 23){
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }

            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), Help_scrub_name.class);
                    requireContext().startActivity(intent);
                }
            });

            helpHealth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment fragment = new Help_health_scrub();
                    fragment.show(requireFragmentManager(), "custom");
                }
            });

            SendScrub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckNetwork()){
                    Scrub_nameschub = ScrubComplete.getText().toString();
                    Scrub_ploshad = ET_ploshad.getText().toString();
                    //Scrub_idd, Scrub_nameschub, Scrub_hp, Scrub_tip,Scrub_ploshad, Scrub_coordix, Scrub_coordiy, login,
                    if (TextUtils.isEmpty(Scrub_nameschub)|| TextUtils.isEmpty(Scrub_hp)
                    || TextUtils.isEmpty(Scrub_tip) || TextUtils.isEmpty(Scrub_ploshad) || TextUtils.isEmpty(Scrub_coordix)
                    || TextUtils.isEmpty(Scrub_coordiy)){
                        Toast.makeText(getActivity(), "Заполнены не все поля", Toast.LENGTH_LONG).show();
                    }else {
                        Send();
                    }
                    } else{
                        Toast.makeText(getContext(),"Проверьте интернет соединение",Toast.LENGTH_LONG).show();
                    }
                }
            });

            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //SelectImage();
                    int write = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int cam = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA);
                    if ((write == PackageManager.PERMISSION_GRANTED) && (cam == PackageManager.PERMISSION_GRANTED)) {
                        PhotoDialog fragment = new PhotoDialog(weight);
                        fragment.setTargetFragment(Scrub.this, 2);
                        assert getFragmentManager() != null;
                        fragment.show(getFragmentManager(), fragment.getClass().getName());
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 2);
                    }

                }
            });

            return view;
        }

    private void SearchAdapter(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS7, null, null, null, null, null, null);
        ArrayList<String> array = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int name = cursor.getColumnIndex(DBHelper.KEY_NAMESCRUB7);
            do {
                String Name = cursor.getString(name);
                array.add(Name);

            } while (cursor.moveToNext());
        }
        adapter = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, array);
        adapter.notifyDataSetChanged();
        cursor.close();
        database.close();
    }

    @SuppressLint("ExifInterface")
    private Bitmap RotateBitmap(Bitmap bitmap)
    {
         android.media.ExifInterface exifInterface = null;
        try{
            exifInterface = new android.media.ExifInterface(weight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert exifInterface != null;
        int orientation = exifInterface.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION, android.media.ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation){
            case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                weight = data.getStringExtra("1");
                if (weight == null) {
                    ivImage.setImageResource(R.drawable.ic_add);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(weight);
                    Bitmap result = Bitmap.createScaledBitmap(bitmap,
                            170, 230, false);
                    bitmap = RotateBitmap(result);
                    ivImage.setImageBitmap(bitmap);
                }
            }
        }
    }

    private View.OnClickListener radioButtonClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb1 = (RadioButton)v;
            switch (rb1.getId()) {
                case R.id.radioButton: Scrub_hp = "Хорошее";
                    break;
                case R.id.radioButton2: Scrub_hp = "Среднее";
                    break;
                case R.id.radioButton3: Scrub_hp = "Плохое";
                    break;

                default: Scrub_hp = "Нет данных";
                    break;
            }
        }
    };

    private View.OnClickListener radioButtonClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb1 = (RadioButton)v;
            switch (rb1.getId()) {
                case R.id.radioButton4: Scrub_tip = "Стриженные";
                    break;
                case R.id.radioButton5: Scrub_tip = "Солитер";
                    break;
                case R.id.radioButton6: Scrub_tip = "Группа";
                    break;
                case R.id.radioButton7: Scrub_tip = "Смешенная группа";
                    break;
                case R.id.radioButton8: Scrub_tip = "Живая изгородь (стриженная)";
                    break;
                case R.id.radioButton9: Scrub_tip = "Живая изгородь (нестриженная)";
                    break;
                case R.id.radioButton10: Scrub_tip = "Живая изгородь из деревьев и кустарников";
                    break;
                case R.id.radioButton11: Scrub_tip = "Топиари";
                    break;

                   default: Scrub_tip = "Нет данных";
                    break;
            }
        }
    };
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

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        Scrub.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {

                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(56.012410, 92.869002))
                                .zoom(16)
                                .tilt(0)
                                .build();
                        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 16);
                        enableLocationComponent(style);

                        fabme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableLocationComponent(style);
                            }
                        });

                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                mapboxMap.clear();
                                mapboxMap.addMarker(new MarkerOptions()
                                        .position(point)
                                        .title("Измеряемый объект"));
                                Scrub_coordi.setText(String.format("%f %f",point.getLatitude(), point.getLongitude()));
                                Scrub_coordix = String.format("%f", point.getLatitude());
                                Scrub_coordiy = String.format("%f",point.getLongitude());
                                return false;

                            }

                        });
                        mapboxMap.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                            @Override
                            public boolean onInfoWindowClick(@NonNull Marker marker) {
                                mapboxMap.removeMarker(marker);
                                Scrub_coordi.setText("Чтобы получить координаты изменяемого объекта, кликните по карте в предполагаемом месте его нахождения");
                                return false;
                            }
                        });
                        SQLiteDatabase database = dbHelper.getWritableDatabase();
                        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS5, null, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            int parkcords = cursor.getColumnIndex(DBHelper.KEY_PARKCORD5);
                            do {
                                if (cursor.isAfterLast()) {
                                    Toast.makeText(getContext(), "Данные о метках обновлены", Toast.LENGTH_LONG).show();
                                }
                                double lat = 0.0;
                                double lon;
                                LatLng latLng;
                                String Cords = cursor.getString(parkcords);
                                String[] split = Cords.split(",");
                                List<LatLng> plotPolygon = new ArrayList<>();
                                for(int i = 0; i<split.length; i++) {
                                    if ( i % 2 == 0 ){
                                        lat = Double.parseDouble(split[i]);
                                    }
                                    else{
                                        lon = Double.parseDouble(split[i]);
                                        latLng= new LatLng(lat,lon);
                                        plotPolygon.add(latLng);}
                                }

                                mapboxMap.addPolygon(new PolygonOptions()
                                        .fillColor(Color.argb(10,0,0,0))
                                        .addAll(plotPolygon)
                                        .alpha(3));

                            } while (cursor.moveToNext());
                        }
                        cursor.close();

                    }
                });
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle).build());
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING_GPS);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.forceLocationUpdate(location);
            locationComponent.zoomWhileTracking(16);
        } else {
            PermissionsManager permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getActivity(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            super.onDestroyView();
        }
    }

    public void Send(){
        String method ="SendScrub";
        AsyncAddData asyncAddData = new AsyncAddData(getContext());
        asyncAddData.execute(method,Scrub_nameschub,Scrub_ploshad, Scrub_hp,Scrub_tip, Scrub_coordix, Scrub_coordiy, weight);
    }


}