package com.reaver.greenkras.ui.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.reaver.greenkras.async.AsyncAddData;
import com.reaver.greenkras.DBHelper;
import com.reaver.greenkras.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
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
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import dmax.dialog.SpotsDialog;

import static java.nio.charset.StandardCharsets.*;

public class MapFragment extends Fragment implements OnMapReadyCallback, PermissionsListener{

    private static final int REQUEST_WEIGHT = 1;
    private static final int REQUEST_ANOTHER_ONE = 2;
    private String res;
    private MapView mapView;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private Location location;
    private String coordix, coordiy, Saved_upload, upload;
    private String Snipet;
    private DBHelper dbHelper;
    private Icon icon;
    private SpotsDialog progressDialog;
    private FloatingActionButton fabme;
    private ImageView mImageView;
    private AutoCompleteTextView searchWindow;
    private Integer visibility = 0;
    private Integer tik = 0;
    private ImageView btnSearch;
    private String text = "";
    private Double parkX, parkY;
    private Integer savePositionCamera = 0;
    private FrameLayout searchBar;
    private ArrayAdapter<?> adapter;
    private Integer MyLvl;
    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(requireContext(), "pk.eyJ1IjoicmVhdmVyNzE3IiwiYSI6ImNqdm5pb2VwajB2OXA0OGxldWo0bHN6MWIifQ.HQsK7BFinOlmFYEtDQyRVQ");
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        dbHelper = new DBHelper(getContext());
        fabme = view.findViewById(R.id.fabme);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        FloatingActionButton fabSearch = view.findViewById(R.id.fabSearch);
        searchWindow = view.findViewById(R.id.acSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        searchBar = view.findViewById(R.id.search_layout);
        searchWindow.setBackgroundColor(Color.argb(80,	93, 166, 82));
        SearchAdapter();
        searchWindow.setAdapter(adapter);

        SharedPreferences photoPref = requireContext().getSharedPreferences("Upload", Context.MODE_PRIVATE);
        upload = photoPref.getString(Saved_upload, "True");

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS6, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int lvlC = cursor.getColumnIndex(DBHelper.KEY_LVL6);
            do {
                String lvl = cursor.getString(lvlC);
                    MyLvl = Integer.parseInt(lvl);

            } while (cursor.moveToNext());
        }
        cursor.close();

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibility==0){
                    SearchAdapter();
                    searchBar.setVisibility(View.VISIBLE);
                    visibility = visibility+1;
                }else{
                    searchBar.setVisibility(View.GONE);
                    visibility = visibility-1;
                }
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetwork()){
                    mapboxMap.clear();
                    savePositionCamera = 1;
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    database.delete(DBHelper.TABLE_CONTACTS, null, null);
                    database.delete(DBHelper.TABLE_CONTACTS2, null, null);
                    database.delete(DBHelper.TABLE_CONTACTS3, null, null);
                    database.delete(DBHelper.TABLE_CONTACTS5, null, null);
                    AsyncMap asyncMap = new AsyncMap();
                    asyncMap.execute();

                }else{
                    Toast.makeText(getContext(),"Проверьте интернет соединение",Toast.LENGTH_LONG).show();
                }
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        } );
        return view;
    }

    private boolean CheckNetwork() {
        ConnectivityManager conMgr = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conMgr != null;
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncMap extends AsyncTask<String,Integer,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new SpotsDialog(requireContext(), R.style.Download);
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String download= "https://greenkras.ru/text.php";
            HttpURLConnection connection;
            BufferedReader reader;
            SQLiteDatabase database = dbHelper.getWritableDatabase();
                try {
                    URL url = new URL (download);
                    connection  = (HttpURLConnection)url.openConnection();
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream, UTF_8));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null) {
                        buffer.append(line);
                    }
                    String finalJson = buffer.toString();
                    JSONObject parentObject = new JSONObject(finalJson);
                    JSONArray parentArray = parentObject.getJSONArray("addtree");
                    JSONObject parentObject2 = new JSONObject(finalJson);
                    JSONArray parentArray2 = parentObject2.getJSONArray("addscrub");
                    JSONObject parentObject3 = new JSONObject(finalJson);
                    JSONArray parentArray3 = parentObject3.getJSONArray("addparterres");
                    JSONObject parentObject4 = new JSONObject(finalJson);
                    JSONArray parentArray4 = parentObject4.getJSONArray("parktop");
                    for(int i=0; i<parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);
                        String idd  = finalObject.getString("links");
                        String parktree  = finalObject.getString("parktree");
                        String cordix  = finalObject.getString("cordix");
                        String cordiy  = finalObject.getString("cordiy");
                        String nameder  = finalObject.getString("nameder");
                        String hp  = finalObject.getString("hp");
                        String sostkron  = finalObject.getString("sostkron");
                        String diamst  = finalObject.getString("diamst");
                        String visder  = finalObject.getString("visder");
                        String diamkron  = finalObject.getString("diamkron");
                        String date  = finalObject.getString("date");
                        String login  = finalObject.getString("login");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.KEY_IDD, idd);
                        contentValues.put(DBHelper.KEY_PARKTREE, parktree);
                        contentValues.put(DBHelper.KEY_CORDIX, cordix);
                        contentValues.put(DBHelper.KEY_CORDIY, cordiy);
                        contentValues.put(DBHelper.KEY_NAMEDER, nameder);
                        contentValues.put(DBHelper.KEY_HP, hp);
                        contentValues.put(DBHelper.KEY_SOSTKRON, sostkron);
                        contentValues.put(DBHelper.KEY_DIAMST, diamst);
                        contentValues.put(DBHelper.KEY_VISDER, visder);
                        contentValues.put(DBHelper.KEY_DIAMKRON, diamkron);
                        contentValues.put(DBHelper.KEY_DATE, date);
                        contentValues.put(DBHelper.KEY_LOGIN, login);
                        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                    }
                    for(int i=0; i<parentArray2.length(); i++) {
                        JSONObject finalObject2 = parentArray2.getJSONObject(i);
                        String idd2  = finalObject2.getString("links");
                        String parkscrub2  = finalObject2.getString("parkscrub");
                        String cordix2  = finalObject2.getString("coordix");
                        String cordiy2 = finalObject2.getString("coordiy");
                        String nameder2  = finalObject2.getString("nameder");
                        String ploshad2  = finalObject2.getString("ploshad");
                        String sost2  = finalObject2.getString("sost");
                        String tip2  = finalObject2.getString("tip");
                        String date2  = finalObject2.getString("date");
                        String login2  = finalObject2.getString("login");
                        ContentValues contentValues2 = new ContentValues();
                        contentValues2.put(DBHelper.KEY_IDD2, idd2);
                        contentValues2.put(DBHelper.KEY_PARKSCRUB2, parkscrub2);
                        contentValues2.put(DBHelper.KEY_CORDIX2, cordix2);
                        contentValues2.put(DBHelper.KEY_CORDIY2, cordiy2);
                        contentValues2.put(DBHelper.KEY_NAMEDER2, nameder2);
                        contentValues2.put(DBHelper.KEY_PLOSHAD2, ploshad2);
                        contentValues2.put(DBHelper.KEY_SOST2, sost2);
                        contentValues2.put(DBHelper.KEY_TIP2, tip2);
                        contentValues2.put(DBHelper.KEY_DATE2, date2);
                        contentValues2.put(DBHelper.KEY_LOGIN2, login2);
                        database.insert(DBHelper.TABLE_CONTACTS2, null, contentValues2);
                    }
                    for(int i=0; i<parentArray3.length(); i++) {
                        JSONObject finalObject3 = parentArray3.getJSONObject(i);
                        String idd3  = finalObject3.getString("links");
                        String parkparterres3  = finalObject3.getString("parkparterres");
                        String cordix3  = finalObject3.getString("coordix");
                        String cordiy3 = finalObject3.getString("coordiy");
                        String nameder3  = finalObject3.getString("nameder");
                        String grouper3  = finalObject3.getString("grouper");
                        String ploshad3  = finalObject3.getString("ploshad");
                        String hp3  = finalObject3.getString("hp");
                        String date3  = finalObject3.getString("date");
                        String login3  = finalObject3.getString("login");
                        ContentValues contentValues3 = new ContentValues();
                        contentValues3.put(DBHelper.KEY_IDD3, idd3);
                        contentValues3.put(DBHelper.KEY_PARKPARTERRES3, parkparterres3);
                        contentValues3.put(DBHelper.KEY_CORDIX3, cordix3);
                        contentValues3.put(DBHelper.KEY_CORDIY3, cordiy3);
                        contentValues3.put(DBHelper.KEY_NAMEDER3, nameder3);
                        contentValues3.put(DBHelper.KEY_GROUPER3, grouper3);
                        contentValues3.put(DBHelper.KEY_PLOSHAD3, ploshad3);
                        contentValues3.put(DBHelper.KEY_HP3, hp3);
                        contentValues3.put(DBHelper.KEY_DATE3, date3);
                        contentValues3.put(DBHelper.KEY_LOGIN3, login3);
                        database.insert(DBHelper.TABLE_CONTACTS3, null, contentValues3);

                    }
                    for(int i=0; i<parentArray4.length(); i++) {
                        JSONObject finalObject4 = parentArray4.getJSONObject(i);
                        String name = finalObject4.getString("name_park");
                        String cords = finalObject4.getString("cords");
                        String x = finalObject4.getString("centerx");
                        String y = finalObject4.getString("centery");
                        ContentValues contentValues4 = new ContentValues();
                        contentValues4.put(DBHelper.KEY_NAMEPARK5, name);
                        contentValues4.put(DBHelper.KEY_PARKCORD5, cords);
                        contentValues4.put(DBHelper.KEY_CENTERX5, x);
                        contentValues4.put(DBHelper.KEY_CENTERY5, y);
                        database.insert(DBHelper.TABLE_CONTACTS5, null, contentValues4);
                    }
                    database.close();
                    dbHelper.close();
                    return "Данные о метках обновлены";
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            onMapReady(mapboxMap);

        }
    }
    private void SearchAdapter(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS5, null, null, null, null, null, null);
        ArrayList<String> array = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int parkName = cursor.getColumnIndex(DBHelper.KEY_NAMEPARK5);
            do {
                String Name = cursor.getString(parkName);
                array.add(Name);

            } while (cursor.moveToNext());
        }
        adapter = new ArrayAdapter<>(requireContext(), R.layout.item_dropdown, array);
        adapter.notifyDataSetChanged();
        cursor.close();
        database.close();
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

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapFragment.this.mapboxMap = mapboxMap;
        mapboxMap.getUiSettings().setCompassMargins(1,450,10,1);
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull final Style style) {

                        if (savePositionCamera==0) {
                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(56.006466, 92.861640))
                                    .zoom(11)
                                    .tilt(20)
                                    .build();
                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 16);
                        }

                        fabme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                enableLocationComponent(style);
                            }
                        });

                        btnSearch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                Objects.requireNonNull(imm).hideSoftInputFromWindow(btnSearch.getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                parkX = 0.0;
                                parkY = 0.0;
                                adapter.notifyDataSetChanged();
                                text = searchWindow.getText().toString();
                                if (!text.equals("")) {
                                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                                    String[] sr = {text};
                                    Cursor X = database.rawQuery("SELECT map4.parkx, map4.parky FROM map4 WHERE (map4.parkname = ?)", sr);
                                    if (X != null) {
                                        if (X.moveToFirst()) {
                                            String str;
                                            do {
                                                for (String cn : X.getColumnNames()) {
                                                    str = X.getString(X.getColumnIndex(cn));
                                                    if (tik == 0) {
                                                        parkX = Double.parseDouble(str);
                                                        tik = tik + 1;
                                                    } else {
                                                        parkY = Double.parseDouble(str);
                                                        tik = 0;
                                                    }
                                                }
                                            } while (X.moveToNext());
                                        }
                                        X.close();
                                        database.close();
                                    }
                                if (parkX>0 && parkY>0){
                                    CameraPosition position = new CameraPosition.Builder()
                                        .target(new LatLng(parkX, parkY))
                                        .zoom(14.5)
                                        .tilt(20)
                                        .build();
                                    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 16);
                                }else
                                    Toast.makeText(getContext(),"Парк не найден",Toast.LENGTH_LONG).show();

                            }else{
                                    Toast.makeText(getContext(),"Вы не ввели название парка",Toast.LENGTH_LONG).show();}
                            }

                        });

                        final SQLiteDatabase database = dbHelper.getWritableDatabase();
                        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            int parktree = cursor.getColumnIndex(DBHelper.KEY_PARKTREE);
                            int cordix = cursor.getColumnIndex(DBHelper.KEY_CORDIX);
                            int cordiy = cursor.getColumnIndex(DBHelper.KEY_CORDIY);
                            int nameder = cursor.getColumnIndex(DBHelper.KEY_NAMEDER);
                            int hp = cursor.getColumnIndex(DBHelper.KEY_HP);
                            int sostkron = cursor.getColumnIndex(DBHelper.KEY_SOSTKRON);
                            int diamst = cursor.getColumnIndex(DBHelper.KEY_DIAMST);
                            int visder = cursor.getColumnIndex(DBHelper.KEY_VISDER);
                            int diamkron = cursor.getColumnIndex(DBHelper.KEY_DIAMKRON);
                            int date = cursor.getColumnIndex(DBHelper.KEY_DATE);
                            int login = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                            do {

                                Snipet = "Парк : " + cursor.getString(parktree) + "\n" +
                                        "Жизненное состояние дерева : " + cursor.getString(hp) + "\n" +
                                        "Состояние кроны : " + cursor.getString(sostkron) + "\n" +
                                        "Диаметр ствола : " + cursor.getString(diamst) + " см\n" +
                                        "Высота дерева : " + cursor.getString(visder) + " см\n" +
                                        "Диаметр кроны : " + cursor.getString(diamkron) + " см\n" +
                                        "Дата добавления : " + cursor.getString(date) + "\n" +
                                        "Добавил пользователь : " + cursor.getString(login);
                                String Health = cursor.getString(hp);
                                String name = cursor.getString(nameder);
                                switch (Health) {
                                    case "Хорошее": {
                                        IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                        Bitmap drawableBitmap = getBitmap(R.drawable.ic_tree_green);
                                        icon = iconFactory.fromBitmap(drawableBitmap);
                                        break;
                                    }
                                    case "Среднее": {
                                        IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                        Bitmap drawableBitmap = getBitmap(R.drawable.ic_tree_yellow);
                                        icon = iconFactory.fromBitmap(drawableBitmap);
                                        break;
                                    }
                                    case "Плохое": {
                                        IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                        Bitmap drawableBitmap = getBitmap(R.drawable.ic_tree_red);
                                        icon = iconFactory.fromBitmap(drawableBitmap);
                                        break;
                                    }
                                    default: {
                                        IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                        Bitmap drawableBitmap = getBitmap(R.drawable.ic_tree_black);
                                        icon = iconFactory.fromBitmap(drawableBitmap);
                                        break;
                                    }
                                }
                                 double x = cursor.getDouble(cordix);
                                 double y = cursor.getDouble(cordiy);
                                mapboxMap.addMarker(new MarkerOptions()

                                        .position(new LatLng(x, y))
                                        .title(name)
                                        .setIcon(icon)
                                        .setSnippet(Snipet));

                            } while (cursor.moveToNext());
                        }
                        cursor.close();
                    final Cursor cursor2 = database.query(DBHelper.TABLE_CONTACTS2, null, null, null, null, null, null);
                    if (cursor2.moveToFirst()) {
                        int parkscrub2 = cursor2.getColumnIndex(DBHelper.KEY_PARKSCRUB2);
                        int cordix2 = cursor2.getColumnIndex(DBHelper.KEY_CORDIX2);
                        int cordiy2 = cursor2.getColumnIndex(DBHelper.KEY_CORDIY2);
                        int nameder2 = cursor2.getColumnIndex(DBHelper.KEY_NAMEDER2);
                        int ploshad2 = cursor2.getColumnIndex(DBHelper.KEY_PLOSHAD2);
                        int sost2 = cursor2.getColumnIndex(DBHelper.KEY_SOST2);
                        int tip2 = cursor2.getColumnIndex(DBHelper.KEY_TIP2);
                        int date2 = cursor2.getColumnIndex(DBHelper.KEY_DATE2);
                        int login2 = cursor2.getColumnIndex(DBHelper.KEY_LOGIN2);
                        do {

                            Snipet = "Парк : "  + cursor2.getString(parkscrub2) + "\n" +
                                    "Жизненное состояние кустарника : " + cursor2.getString(sost2) + "\n" +
                                    "Площадь кустарника : " + cursor2.getString(ploshad2) + " см\n" +
                                    "Тип насаждения : " + cursor2.getString(tip2) + "\n" +
                                    "Дата добавления : " + cursor2.getString(date2) + "\n" +
                                    "Добавил пользователь : " + cursor2.getString(login2);
                            String Health = cursor2.getString(sost2);
                            String name = cursor2.getString(nameder2);
                            switch (Health) {
                                case "Хорошее": {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_scrub_green);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                                case "Среднее": {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_scrub_yellow);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                                case "Плохое": {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_scrub_red);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                                default: {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_scrub_black);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                            }
                            double x = cursor2.getDouble(cordix2);
                            double y = cursor2.getDouble(cordiy2);

                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(x, y))
                                    .title(name)
                                    .setIcon(icon)
                                    .setSnippet(Snipet));

                        } while (cursor2.moveToNext());
                    }
                    cursor2.close();

                    Cursor cursor3 = database.query(DBHelper.TABLE_CONTACTS3, null, null, null, null, null, null);
                    if (cursor3.moveToFirst()) {
                        int parkparterres3 = cursor3.getColumnIndex(DBHelper.KEY_PARKPARTERRES3);
                        int cordix3 = cursor3.getColumnIndex(DBHelper.KEY_CORDIX3);
                        int cordiy3 = cursor3.getColumnIndex(DBHelper.KEY_CORDIY3);
                        int nameder3 = cursor3.getColumnIndex(DBHelper.KEY_NAMEDER3);
                        int grouper3 = cursor3.getColumnIndex(DBHelper.KEY_GROUPER3);
                        int ploshad3 = cursor3.getColumnIndex(DBHelper.KEY_PLOSHAD3);
                        int hp3 = cursor3.getColumnIndex(DBHelper.KEY_HP3);
                        int date3 = cursor3.getColumnIndex(DBHelper.KEY_DATE3);
                        int login3 = cursor3.getColumnIndex(DBHelper.KEY_LOGIN3);

                        do {

                            Snipet = "Парк : " + cursor3.getString(parkparterres3) + "\n" +
                                    "Жизненное состояние цветника : " + cursor3.getString(hp3) + "\n" +
                                    "Площадь цветника : " + cursor3.getString(ploshad3) + " см\n" +
                                    "Тип насаждения : " + cursor3.getString(grouper3) + "\n" +
                                    "Дата добавления : " + cursor3.getString(date3) + "\n" +
                                    "Добавил пользователь : " + cursor3.getString(login3);
                            String Health = cursor3.getString(hp3);
                            String name = cursor3.getString(nameder3);
                            switch (Health) {
                                case "Хорошее": {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_parterres_green);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                                case "Среднее": {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_parterres_yellow);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                                case "Плохое": {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_parterres_red);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                                default: {
                                    IconFactory iconFactory = IconFactory.getInstance(requireContext());
                                    Bitmap drawableBitmap = getBitmap(R.drawable.ic_parterres_black);
                                    icon = iconFactory.fromBitmap(drawableBitmap);
                                    break;
                                }
                            }

                            double x = cursor3.getDouble(cordix3);
                            double y = cursor3.getDouble(cordiy3);

                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(x, y))
                                    .title(name)
                                    .setIcon(icon)
                                    .setSnippet(Snipet));

                        } while (cursor3.moveToNext());
                    }
                    cursor3.close();


                        Cursor cursor5 = database.query(DBHelper.TABLE_CONTACTS5, null, null, null, null, null, null);
                        if (cursor5.moveToFirst()) {
                            int parkcords5 = cursor5.getColumnIndex(DBHelper.KEY_PARKCORD5);
                            do {
                                double lat = 0.0;
                                double lon;
                                LatLng latLng;
                                String Cords = cursor5.getString(parkcords5);
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
                                        .alpha(5));

                            } while (cursor5.moveToNext());
                        }
                        cursor5.close();
                        database.close();



                        mapboxMap.setInfoWindowAdapter(new MapboxMap.InfoWindowAdapter() {
                            TextView title, snipet, link;
                            ImageButton buttonVote, buttonClose;
                            @SuppressLint("InflateParams")
                            @Override
                            public View getInfoWindow(@NonNull final Marker marker) {
                                final View infowindow;
                                infowindow = LayoutInflater.from(getContext()).inflate(R.layout.fragment_infowindow, null);

                                mImageView = infowindow.findViewById(R.id.loadimage);
                                title = infowindow.findViewById(R.id.fragment_title);
                                snipet = infowindow.findViewById(R.id.fragment_snipet);
                                buttonVote = infowindow.findViewById(R.id.fragment_button_vote);
                                buttonClose = infowindow.findViewById(R.id.fragment_button_close);
                                link = infowindow.findViewById(R.id.fragment_link);

                                    mImageView.setVisibility(View.VISIBLE);
                                    double d1 = marker.getPosition().getLatitude();
                                    double d2 = marker.getPosition().getLongitude();
                                    String s1 = Double.toString(d1);
                                    String s2 = Double.toString(d2);
                                    String [] sr = {s1,s2};
                                    String res = "null";
                                    final SQLiteDatabase database = dbHelper.getWritableDatabase();
                                    Cursor c = null;
                                    Cursor tree = database.rawQuery("SELECT map.idd FROM map WHERE (map.cordix = ? AND map.cordiy = ?)", sr);
                                    Cursor scrub = database.rawQuery("SELECT map2.idd FROM map2 WHERE (map2.cordix = ? AND map2.cordiy = ?)", sr);
                                    Cursor parterres = database.rawQuery("SELECT map3.idd FROM map3 WHERE (map3.cordix = ? AND map3.cordiy = ?)", sr);

                                    for (int count = 0; count < 3;) {
                                        if (count == 0) {
                                            c = tree;
                                        }
                                        if (count == 1) {
                                            c = scrub;
                                        }
                                        if (count == 2) {
                                            c = parterres;
                                        }
                                        if (c != null) {
                                            if (c.moveToFirst()) {
                                                String str;
                                                do {
                                                    str = "null";
                                                    for (String cn : c.getColumnNames()) {
                                                        str = c.getString(c.getColumnIndex(cn));
                                                    }
                                                    res = str;
                                                    Log.d("0", res);
                                                } while (c.moveToNext());
                                                break;
                                            }
                                        }
                                        count = count+1;
                                    }
                                assert c != null;
                                c.close();
                                    database.close();
                                    if (upload.equals("True")){
                                        if (!res.equals("https://greenkras.ru/images/nottree.png")){
                                            mImageView.setVisibility(View.VISIBLE);
                                            Picasso.get()
                                                    .load(res)
                                                    .resize(750,1000)
                                                    .error(R.drawable.ic_error)
                                                    .into(mImageView);
                                        }else {
                                            mImageView.setVisibility(View.GONE);
                                        }
                                    } else
                                        mImageView.setVisibility(View.GONE);

                                title.setText(marker.getTitle());


                                buttonClose.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mapboxMap.deselectMarker(marker);
                                    }
                                });

                                snipet.setText(marker.getSnippet());
                                buttonVote.setOnClickListener(new View.OnClickListener() {
                                    @SuppressLint("DefaultLocale")
                                    @Override
                                    public void onClick(View v) {
                                        if (MyLvl>2) {
                                            coordix = String.format("%f", marker.getPosition().getLongitude());
                                            coordiy = String.format("%f", marker.getPosition().getLatitude());
                                            MapDialog fragment = new MapDialog();
                                            fragment.setTargetFragment(MapFragment.this, REQUEST_WEIGHT);
                                            assert getFragmentManager() != null;
                                            fragment.show(getFragmentManager(), fragment.getClass().getName());
                                        }else{
                                            Toast.makeText(getContext(),"Вам необходимо достигнуть 3 уровня",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });



                                mapboxMap.setOnInfoWindowClickListener(new MapboxMap.OnInfoWindowClickListener() {
                                    @Override
                                    public boolean onInfoWindowClick(@NonNull Marker marker) {
                                        return true;
                                    }
                                });
                                return infowindow;

                            }
                        });

                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_WEIGHT:
                    res = data.getStringExtra("res");
                    Send();
                case REQUEST_ANOTHER_ONE:
                    //...
                    break;
                //обработка других requestCode
            }
        }
    }

    public void Send(){
        String method ="Missing";
        savePositionCamera = 1;
        AsyncAddData asyncAddData = new AsyncAddData(requireContext());
        asyncAddData.execute(method, coordix, coordiy, res);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(DBHelper.TABLE_CONTACTS, null, null);
        database.delete(DBHelper.TABLE_CONTACTS2, null, null);
        database.delete(DBHelper.TABLE_CONTACTS3, null, null);
        database.delete(DBHelper.TABLE_CONTACTS5, null, null);
        database.close();
        AsyncMap asyncMap = new AsyncMap();
        asyncMap.execute();
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
}