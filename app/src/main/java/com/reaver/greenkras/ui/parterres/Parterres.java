package com.reaver.greenkras.ui.parterres;

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
import com.reaver.greenkras.ui.help.Help_health_parterres;
import com.reaver.greenkras.ui.help.Help_health_scrub;
import com.reaver.greenkras.ui.help.Help_parterres_name;
import com.reaver.greenkras.ui.help.Help_scrub_name;
import com.reaver.greenkras.ui.tree.Tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parterres extends Fragment implements OnMapReadyCallback, PermissionsListener {
    private MapView mapView;
    private MapboxMap mapboxMap;
    private Location location;
    private TextView Parterres_coordi;
    private EditText  ET_ploshad;
    private FloatingActionButton fabme;
    private String weight;
    private ImageView ivImage;
    private DBHelper dbHelper;
    private ArrayAdapter<?> adapter;

    private String Parterres_nameparterres, Parterres_hp, Parterres_tip,Parterres_ploshad, Parterres_coordix, Parterres_coordiy;
    private AutoCompleteTextView ParterresComplete;
    private String[] mParterres = {"Алиссум морской", "Алиссум", "Бархатец отклоненный", "Бархатец прямостоячий", "Бархатец", "Бегония всегдацветущая",
            "Бегония клубневая", "Бегония", "Вербена гибридная", "Вербена", "Календула лекарственная", "Календула", "Капуста декоративная","Капуста",
            "Клещевина обыкновенная", "Кохия веничная", "Кохия",
            "Лобелия эринус", "Лобелия", "Пеларгония плющелистная", "Пеларгония", "Петуния (гибридная мелкоцветковая, крупноцветковая)", "Петуния",
            "Рудбекия волосистая", "Рудбекия",
            "Сальвия блестящая", "Сальвия", "Флокс Друммонда", "Флокс",
            "Циния изящная", "Циния", "Эшшольция калифорнийская", "Эшшольция",
            "Виола Витрокка", "Незабудка гибридная", "Незабудка", "Маргаритка многолетняя", "Маргаритка",
            "Гвоздика турецкая", "Гвоздика", "Колокольчик средний", "Колокольчик",
            "Аконит желтый", "Аконит", "Аквилегия гибридная", "Аквилегия",
            "Астильба гибридная (сорта)", "Астра альпийская",
            "Астра итальянская", "Астра кустарниковая", "Астра", "Вероника колосистая", "Вероника",
            "Василистник водосборолистный", "Василистник", "Герань великолепная", "Герань",
            "Гвоздика перистая", "Гвоздика", "Дербенник иволистный", "Дербенник", "Дельфиниум культурный",
            "Ирис бородатый (высокие сорта, средние сорта)", "Ирис болотный",
            "Ирис сибирский", "Купальница европейская", "Купальница",
            "Колокольчик скученный", "Колокольчик", "Люпин многолистный", "Люпин",
            "Лилейник (сорта и виды)",
            "Манжетка мягкая", "Манжетка",
            "Овсяница сизая", "Овсяница",
            "Пион молочноцветковый", "Пион лекарственный", "Пион тонколистный", "Пион",
            "Рудбекия (блестящая, глянцевитая)", "Синюха голубая", "Синеголовник альпийский",
            "Тысячелистник таволговый обыкновенный", "Флокс метельчатый (сорта)",
            "Хоста (сорта и виды)", "Хризантема корейская", "Хризантема", "Шалфей дубравный", "Шалфей"};

    public Parterres() {

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
        final View view = inflater.inflate(R.layout.fragment_parterres, container, false);
        dbHelper = new DBHelper(getContext());
        mapView =  view.findViewById(R.id.mapView);
        Button SendParterres = view.findViewById(R.id.SendParterres);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        ImageButton help = view.findViewById(R.id.helpParterres);
        ImageButton helpHealth = view.findViewById(R.id.helpParterresHealth);
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
        Parterres_coordi= view.findViewById(R.id.Parterres_coordi);
        ET_ploshad = view.findViewById(R.id.Parterres_ploshad);
        Parterres_coordi.setText("Чтобы получить координаты изменяемого объекта, кликните по карте в предполагаемом месте его нахождения");
        ParterresComplete = view.findViewById(R.id.autoCompleteTextParterres);
        SearchAdapter();
        ParterresComplete.setAdapter(adapter);
        fabme = view.findViewById(R.id.fabme);
        ivImage = view.findViewById(R.id.imageToEncode);
        RadioButton RadioButtonSostParterres1 = view.findViewById(R.id.radioButton);
        RadioButtonSostParterres1.setOnClickListener(radioButtonClickListener1);
        RadioButton RadioButtonSostParterres2 = view.findViewById(R.id.radioButton2);
        RadioButtonSostParterres2.setOnClickListener(radioButtonClickListener1);
        RadioButton RadioButtonSostParterres3 = view.findViewById(R.id.radioButton3);
        RadioButtonSostParterres3.setOnClickListener(radioButtonClickListener1);

        RadioButton RadioButtonTipPreterres1 = view.findViewById(R.id.radioButton4);
        RadioButtonTipPreterres1.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonTipPreterres2 = view.findViewById(R.id.radioButton5);
        RadioButtonTipPreterres2.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonTipPreterres3 = view.findViewById(R.id.radioButton6);
        RadioButtonTipPreterres3.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonTipPreterres4 = view.findViewById(R.id.radioButton7);
        RadioButtonTipPreterres4.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonTipPreterres5 = view.findViewById(R.id.radioButton8);
        RadioButtonTipPreterres5.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonTipPreterres6 = view.findViewById(R.id.radioButton9);
        RadioButtonTipPreterres6.setOnClickListener(radioButtonClickListener2);
        RadioButton RadioButtonTipPreterres7 = view.findViewById(R.id.radioButton10);
        RadioButtonTipPreterres7.setOnClickListener(radioButtonClickListener2);

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Help_parterres_name.class);
                requireContext().startActivity(intent);
            }
        });

        helpHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new Help_health_parterres();
                fragment.show(requireFragmentManager(), "custom");
            }
        });

        SendParterres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parterres_nameparterres = ParterresComplete.getText().toString();
                Parterres_ploshad = ET_ploshad.getText().toString();
                if (CheckNetwork()) {
                    //Scrub_idd, Scrub_nameschub, Scrub_hp, Scrub_tip,Scrub_ploshad, Scrub_coordix, Scrub_coordiy, login,
                    if (TextUtils.isEmpty(Parterres_nameparterres) || TextUtils.isEmpty(Parterres_hp)
                            || TextUtils.isEmpty(Parterres_tip) || TextUtils.isEmpty(Parterres_ploshad) || TextUtils.isEmpty(Parterres_coordix)
                            || TextUtils.isEmpty(Parterres_coordiy)) {
                        Toast.makeText(getActivity(), "Заполнены не все поля", Toast.LENGTH_LONG).show();
                    } else {
                        Send();
                    }
                }else {
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
                    fragment.setTargetFragment(Parterres.this, 2);
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
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS9, null, null, null, null, null, null);
        ArrayList<String> array = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int name = cursor.getColumnIndex(DBHelper.KEY_NAMEPARTERRES9);
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
                case R.id.radioButton: Parterres_hp = "Хорошее";
                    break;
                case R.id.radioButton2: Parterres_hp = "Среднее";
                    break;
                case R.id.radioButton3: Parterres_hp = "Плохое";
                    break;

                default: Parterres_hp = "Нет данных";
                    break;
            }
        }
    };

    private View.OnClickListener radioButtonClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb1 = (RadioButton)v;
            switch (rb1.getId()) {
                case R.id.radioButton4: Parterres_tip = "Клумба";
                    break;
                case R.id.radioButton5: Parterres_tip = "Рабатки";
                    break;
                case R.id.radioButton6: Parterres_tip = "Бордюры";
                    break;
                case R.id.radioButton7: Parterres_tip = "Альпинарии, каменистые сады";
                    break;
                case R.id.radioButton8: Parterres_tip = "Вертикальное озеленение";
                    break;
                case R.id.radioButton9: Parterres_tip = "Миксбордеры";
                    break;
                case R.id.radioButton10: Parterres_tip = "Вазоны";
                    break;

                default: Parterres_tip = "Нет данных";
                    break;
            }
        }
    };

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        Parterres.this.mapboxMap = mapboxMap;
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
                                Parterres_coordi.setText(String.format("%f %f",point.getLatitude(), point.getLongitude()));
                                Parterres_coordix = String.format("%f", point.getLatitude());
                                Parterres_coordiy = String.format("%f",point.getLongitude());
                                return false;

                            }

                        });
                        mapboxMap.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                            @Override
                            public boolean onInfoWindowClick(@NonNull Marker marker) {
                                mapboxMap.removeMarker(marker);
                                Parterres_coordi.setText("Чтобы получить координаты изменяемого объекта, кликните по карте в предполагаемом месте его нахождения");
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

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
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

    public void Send(){
        String method ="SendParterres";
        AsyncAddData asyncAddData = new AsyncAddData(getContext());
        asyncAddData.execute(method, Parterres_nameparterres,Parterres_ploshad,Parterres_hp,Parterres_tip,Parterres_coordix,Parterres_coordiy, weight);
    }

}
