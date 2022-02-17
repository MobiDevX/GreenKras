package com.reaver.greenkras.ui.tree;

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
import com.mapbox.mapboxsdk.storage.Resource;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.async.AsyncAddData;
import com.reaver.greenkras.ui.PhotoDialog;
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
import com.reaver.greenkras.ui.help.Help_health_tree;
import com.reaver.greenkras.ui.help.Help_tree_name;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tree extends Fragment implements OnMapReadyCallback, PermissionsListener {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private Location location;
    private TextView Tree_coordi;
    private String weight;
    private EditText ET_diamst, ET_vis, ET_diamkro;
    private String Tree_hp, Tree_sost, Tree_nameder, Tree_diamst, Tree_vis, Tree_diamkro, Tree_coordix, Tree_coordiy;
    private AutoCompleteTextView TreeComplete;
    private ImageView ivImage;
    private FloatingActionButton fabme;
    private DBHelper dbHelper;
    private ArrayAdapter<?> adapter;

    public Tree() {
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
        final View view = inflater.inflate(R.layout.fragment_tree, container, false);
        dbHelper = new DBHelper(getContext());
        mapView =  view.findViewById(R.id.mapView);
        Button SendTree = view.findViewById(R.id.SendTree);
        mapView.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        Tree_coordi= view.findViewById(R.id.Tree_coordi);
        ImageButton help = view.findViewById(R.id.helpTree);
        ImageButton helpHealth = view.findViewById(R.id.helpTreeHealth);
        ET_diamst = view.findViewById(R.id.Tree_diamst);
        ET_vis = view.findViewById(R.id.Tree_vis);
        ET_diamkro = view.findViewById(R.id.Tree_diamkro);
        Tree_coordi.setText("Чтобы получить координаты изменяемого объекта, кликните по карте в предполагаемом месте его нахождения");
        TreeComplete = view.findViewById(R.id.autoCompleteTextTree);
        SearchAdapter();
        TreeComplete.setAdapter(adapter);
        fabme = view.findViewById(R.id.fabme);
        ivImage =  view.findViewById(R.id.imageToEncode);
        RadioButton RadioButtonSostDer1 = view.findViewById(R.id.radioButton);
        RadioButtonSostDer1.setOnClickListener(radioButtonClickListener1);
        RadioButton RadioButtonSostDer2 = view.findViewById(R.id.radioButton2);
        RadioButtonSostDer2.setOnClickListener(radioButtonClickListener1);
        RadioButton RadioButtonSostDer3 = view.findViewById(R.id.radioButton3);
        RadioButtonSostDer3.setOnClickListener(radioButtonClickListener1);
        RadioButton RadioButtonSostKro1 = view.findViewById(R.id.radioButton4);
        RadioButtonSostKro1.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonSostKro2 = view.findViewById(R.id.radioButton5);
        RadioButtonSostKro2.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonSostKro3 = view.findViewById(R.id.radioButton6);
        RadioButtonSostKro3.setOnClickListener(radioButtonClickListener2);

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);

        }

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Help_tree_name.class);
                requireContext().startActivity(intent);
            }
        });

        helpHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new Help_health_tree();
                fragment.show(requireFragmentManager(), "custom");
            }
        });

        SendTree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String Tree_hp, Tree_sost, Tree_idd, Tree_nameder, Tree_diamst, Tree_vis, Tree_diamkro, Tree_coordix, Tree_coordiy, login, method;
                if (CheckNetwork()){
                Tree_nameder = TreeComplete.getText().toString();
                Tree_diamst = ET_diamst.getText().toString();
                Tree_vis = ET_vis.getText().toString();
                Tree_diamkro = ET_diamkro.getText().toString();
                if (TextUtils.isEmpty(Tree_nameder)|| TextUtils.isEmpty(Tree_diamst)
                        || TextUtils.isEmpty(Tree_diamkro) || TextUtils.isEmpty(Tree_hp) || TextUtils.isEmpty(Tree_sost)
                        || TextUtils.isEmpty(Tree_vis) || TextUtils.isEmpty(Tree_coordix) || TextUtils.isEmpty(Tree_coordiy)){
                    Toast.makeText(getActivity(), "Заполнены не все поля", Toast.LENGTH_LONG).show();

                }else {
                    Send();
                }
                }else{
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
                    fragment.setTargetFragment(Tree.this, 2);
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
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS8, null, null, null, null, null, null);
        ArrayList<String> array = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int name = cursor.getColumnIndex(DBHelper.KEY_NAMETREE8);
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
                case R.id.radioButton: Tree_hp = "Хорошее";
                    break;
                case R.id.radioButton2: Tree_hp = "Среднее";
                    break;
                case R.id.radioButton3: Tree_hp = "Плохое";
                    break;

                default: Tree_hp = "Нет данных";
                    break;
            }
        }
    };

    private View.OnClickListener radioButtonClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb1 = (RadioButton)v;
            switch (rb1.getId()) {
                case R.id.radioButton4: Tree_sost = "Естественное";
                    break;
                case R.id.radioButton5: Tree_sost = "Формировочная обрезка";
                    break;
                case R.id.radioButton6: Tree_sost = "Глубокая обрезка";
                    break;

                default: Tree_sost = "Нет данных";
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
        Tree.this.mapboxMap = mapboxMap;
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
                                Tree_coordi.setText(String.format("%f %f",point.getLatitude(), point.getLongitude()));
                                Tree_coordix = String.format("%f", point.getLatitude());
                                Tree_coordiy = String.format("%f",point.getLongitude());
                                return false;

                            }

                        });
                        mapboxMap.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                            @Override
                            public boolean onInfoWindowClick(@NonNull Marker marker) {
                                mapboxMap.removeMarker(marker);
                                Tree_coordi.setText("Чтобы получить координаты изменяемого объекта, кликните по карте в предполагаемом месте его нахождения");
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
        String method ="SendTree";
        AsyncAddData asyncAddData = new AsyncAddData(getContext());
        asyncAddData.execute(method, Tree_nameder,Tree_hp, Tree_sost,Tree_diamst,Tree_vis,Tree_diamkro, Tree_coordix, Tree_coordiy, weight);
    }


}