package com.reaver.greenkras.ui.poligon;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polygon;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
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
import com.reaver.greenkras.async.AsyncAddData;
import com.reaver.greenkras.R;
import java.util.ArrayList;
import java.util.List;

public class PoligonFragment extends Fragment implements OnMapReadyCallback, PermissionsListener {

    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private Location location;
    private EditText Name_park;
    private String Park_name, Park_polygon;
    private FloatingActionButton fabadd;
    private FloatingActionButton fabme;
    private FloatingActionButton fabsel;
    private FloatingActionButton fabcolor;
    private List<LatLng> plotPolygon = new ArrayList<>();
    private Integer count = 0;
    private Integer color = 1;
    private Polygon polygon;
    private Integer select = 0;



    public PoligonFragment() {

    }

    private boolean CheckNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(requireContext().getApplicationContext(), "pk.eyJ1IjoicmVhdmVyNzE3IiwiYSI6ImNqdm5pb2VwajB2OXA0OGxldWo0bHN6MWIifQ.HQsK7BFinOlmFYEtDQyRVQ");
        View view = inflater.inflate(R.layout.fragment_poligon, container, false);
        mapView = view.findViewById(R.id.mapView);
        DialogFragment tutorial = new DialogPark();
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        fabadd = view.findViewById(R.id.fab_add);
        FloatingActionButton fabrem = view.findViewById(R.id.fab_remove);
        fabme = view.findViewById(R.id.fabme);
        fabsel = view.findViewById(R.id.fab_select);
        Name_park = view.findViewById(R.id.park_name);
        fabcolor = view.findViewById(R.id.fab_color);
        assert getFragmentManager() != null;
        tutorial.show(getFragmentManager(),"Обучение");
        fabrem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plotPolygon = new ArrayList<>();
                mapboxMap.removeAnnotations();
                count = 0;
                color = 1;
                select = 0;
            }
        });

        return view;
    }

    public void Send(){
        String method ="SendPark";
        AsyncAddData asyncAddData = new AsyncAddData(getContext());
        asyncAddData.execute(method,Park_name,Park_polygon);
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

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        PoligonFragment.this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {

                        final CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(56.012410, 92.869002))
                                .zoom(18)
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
                            @Override
                            public boolean onMapClick(@NonNull final LatLng point) {
                                if (select ==0){
                                    if(count < 16){
                                    mapboxMap.addMarker(new MarkerOptions()
                                        .position(point));
                                    plotPolygon.add(point);
                                    count = count + 1;
                                    }
                                    else{
                                        Toast.makeText(getContext(), "Колличество меток не должно превышать 16", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                return false;
                            }



                        });
                        fabadd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CheckNetwork()){
                                Park_name = Name_park.getText().toString();
                                Park_polygon = plotPolygon.toString();
                                if(TextUtils.isEmpty(Park_name) || TextUtils.isEmpty(Park_polygon) || (count <= 2)){
                                    Toast.makeText(getActivity(), "Заполнены не все данные", Toast.LENGTH_SHORT).show();
                                }else {
                                    Send();
                                }
                                }else{
                                    Toast.makeText(getContext(),"Проверьте интернет соединение",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                        new PolygonOptions()
                                .fillColor(Color.rgb(128,128,128))
                                .addAll(plotPolygon)
                                .alpha(3);

                        fabsel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                select = 1;
                                color = 2;
                                polygon = mapboxMap.addPolygon(new PolygonOptions()
                                        .fillColor(Color.rgb(128,128,128))
                                        .addAll(plotPolygon)
                                        .alpha(3));
                                if (plotPolygon.isEmpty())
                                    Toast.makeText(getContext(),"Обведите парк",Toast.LENGTH_LONG).show();
                            }
                        });

                        fabcolor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (color % 2 == 0) {
                                    polygon.remove();
                                    polygon = mapboxMap.addPolygon(new PolygonOptions()
                                            .fillColor(Color.argb(20,0,0,0))
                                            .addAll(plotPolygon)
                                            .alpha(3));
                                    color++;

                                }
                                else if (color == 1){
                                        Toast.makeText(getContext(),"Невозможно изменить стиль разметки", Toast.LENGTH_LONG).show();
                                }else {
                                    polygon.remove();
                                    polygon = mapboxMap.addPolygon(new PolygonOptions()
                                            .fillColor(Color.rgb(128,128,128))
                                            .addAll(plotPolygon)
                                            .alpha(3));
                                     color++;
                                    }
                                }

                        });

                        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                color = 2;
                                mapboxMap.removeMarker(marker);
                                count = count-1;
                                plotPolygon.remove(marker.getPosition());
                                return false;
                            }
                        });

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
            locationComponent.zoomWhileTracking(18);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    @SuppressWarnings({"MissingPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
}

