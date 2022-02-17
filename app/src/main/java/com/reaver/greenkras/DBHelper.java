package com.reaver.greenkras;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "contactMap";
    public static final String TABLE_CONTACTS = "map";
    public static final String TABLE_CONTACTS2 = "map2";
    public static final String TABLE_CONTACTS3 = "map3";
    public static final String TABLE_CONTACTS4 = "parkname";
    public static final String TABLE_CONTACTS5 = "map4";
    public static final String TABLE_CONTACTS6 = "user";
    public static final String TABLE_CONTACTS7 = "scrub";
    public static final String TABLE_CONTACTS8 = "tree";
    public static final String TABLE_CONTACTS9 = "parterres";

    private static final String KEY_ID = "_id";
    public static final String KEY_IDD = "idd";
    public static final String KEY_PARKTREE = "parktree";
    public static final String KEY_CORDIX = "cordix";
    public static final String KEY_CORDIY = "cordiy";
    public static final String KEY_NAMEDER = "nameder";
    public static final String KEY_HP = "hp";
    public static final String KEY_SOSTKRON = "sostkron";
    public static final String KEY_DIAMST = "diamst";
    public static final String KEY_VISDER = "visder";
    public static final String KEY_DIAMKRON = "diamkron";
    public static final String KEY_DATE = "date";
    public static final String KEY_LOGIN = "login";

    private static final String KEY_ID2 = "_id";
    public static final String KEY_IDD2 = "idd";
    public static final String KEY_PARKSCRUB2 = "parkscrub";
    public static final String KEY_CORDIX2 = "cordix";
    public static final String KEY_CORDIY2 = "cordiy";
    public static final String KEY_NAMEDER2 = "nameder";
    public static final String KEY_PLOSHAD2 = "ploshad";
    public static final String KEY_SOST2 = "sost";
    public static final String KEY_TIP2 = "tip";
    public static final String KEY_DATE2 = "date";
    public static final String KEY_LOGIN2 = "login";

    private static final String KEY_ID3 = "_id";
    public static final String KEY_IDD3 = "idd";
    public static final String KEY_PARKPARTERRES3 = "parkparterres";
    public static final String KEY_CORDIX3 = "cordix";
    public static final String KEY_CORDIY3 = "cordiy";
    public static final String KEY_NAMEDER3 = "nameder";
    public static final String KEY_GROUPER3 = "grouper";
    public static final String KEY_PLOSHAD3 = "ploshad";
    public static final String KEY_HP3 = "hp";
    public static final String KEY_DATE3 = "date";
    public static final String KEY_LOGIN3 = "login";

    private static final String KEY_ID4 = "_id";
    public static final String KEY_PARKNAME = "parkname";
    public static final String KEY_PARKCORD = "parkcord";
    public static final String KEY_CENTERX = "centerx";
    public static final String KEY_CENTERY = "centery";
    public static final String KEY_PARKLOGIN = "parklogin";
    public static final String KEY_VOTEYES = "voteyes";
    public static final String KEY_VOTENO = "voteno";

    private static final String KEY_ID5 = "_id";
    public static final String KEY_NAMEPARK5 = "parkname";
    public static final String KEY_PARKCORD5 = "parkcord";
    public static final String KEY_CENTERX5 = "parkx";
    public static final String KEY_CENTERY5 = "parky";

    private static final String KEY_ID6 = "_id";
    public static final String KEY_LOGIN6 = "login";
    public static final String KEY_TITUL6 = "titul";
    public static final String KEY_AVATAR6 = "avatar";
    public static final String KEY_LVL6 = "lvl";
    public static final String KEY_PROC6 = "proc";
    public static final String KEY_OST6 = "ostob";
    public static final String KEY_SCHETDER6 = "schetder";
    public static final String KEY_SCHETKUST6 = "schetkust";
    public static final String KEY_SCHETPART6 = "schetpart";
    public static final String KEY_SCHETPARK6 = "schetpark";
    public static final String KEY_ALLOBJ6 = "allobj";
    public static final String KEY_ALLVOTERS6 = "allvoters";
    public static final String KEY_WARN6 = "warn";

    private static final String KEY_ID7 = "_id";
    public static final String KEY_NAMESCRUB7 = "scrub";

    private static final String KEY_ID8 = "_id";
    public static final String KEY_NAMETREE8 = "tree";

    private static final String KEY_ID9 = "_id";
    public static final String KEY_NAMEPARTERRES9 = "parterres";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key,"  + KEY_IDD + " text," + KEY_PARKTREE + " text," +  KEY_CORDIX + " text," + KEY_CORDIY + " text," +
                KEY_NAMEDER + " text," + KEY_HP + " text," + KEY_SOSTKRON + " text," + KEY_DIAMST + " text," + KEY_VISDER + " text," +
                KEY_DIAMKRON + " text," + KEY_DATE + " text," + KEY_LOGIN + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS2 + "(" + KEY_ID2
                + " integer primary key,"  + KEY_IDD2 + " text," + KEY_PARKSCRUB2 + " text," + KEY_CORDIX2 + " text," + KEY_CORDIY2 + " text," +
                KEY_NAMEDER2 + " text," + KEY_PLOSHAD2 + " text," + KEY_SOST2 + " text," + KEY_TIP2 + " text,"
                + KEY_DATE2 + " text," + KEY_LOGIN2 + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS3 + "(" + KEY_ID3
                + " integer primary key,"  + KEY_IDD3 + " text," + KEY_PARKPARTERRES3 + " text," +  KEY_CORDIX3 + " text," + KEY_CORDIY3 + " text," +
                KEY_NAMEDER3 + " text," + KEY_GROUPER3 + " text," + KEY_PLOSHAD3 + " text," + KEY_HP3 + " text," + KEY_DATE3 + " text," + KEY_LOGIN3 + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS4 + "(" + KEY_ID4
                + " integer primary key,"  + KEY_PARKNAME + " text," +  KEY_PARKCORD + " text," +  KEY_CENTERX + " text," +  KEY_CENTERY + " text,"
                + KEY_PARKLOGIN + " text," + KEY_VOTEYES + " text," + KEY_VOTENO + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS5 + "(" + KEY_ID5
                + " integer primary key," + KEY_NAMEPARK5 + " text," + KEY_PARKCORD5 + " text," + KEY_CENTERX5 + " text," +  KEY_CENTERY5 + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS6 + "(" + KEY_ID6
                + " integer primary key,"  + KEY_LOGIN6 + " text," +  KEY_TITUL6 + " text," +  KEY_AVATAR6 + " text," +  KEY_LVL6 + " text,"
                + KEY_PROC6 + " text," + KEY_OST6 + " text," + KEY_SCHETDER6 + " text," + KEY_SCHETKUST6 + " text," + KEY_SCHETPART6 + " text,"
                + KEY_SCHETPARK6 + " text," + KEY_ALLOBJ6 + " text," + KEY_ALLVOTERS6 + " text," + KEY_WARN6 + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS7 + "(" + KEY_ID7
                + " integer primary key," + KEY_NAMESCRUB7 + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS8 + "(" + KEY_ID8
                + " integer primary key," + KEY_NAMETREE8 + " text" + ")");

        db.execSQL("create table " + TABLE_CONTACTS9 + "(" + KEY_ID9
                + " integer primary key," + KEY_NAMEPARTERRES9 + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        db.execSQL("drop table if exists " + TABLE_CONTACTS2);
        db.execSQL("drop table if exists " + TABLE_CONTACTS3);
        db.execSQL("drop table if exists " + TABLE_CONTACTS4);
        db.execSQL("drop table if exists " + TABLE_CONTACTS5);
        db.execSQL("drop table if exists " + TABLE_CONTACTS6);
        db.execSQL("drop table if exists " + TABLE_CONTACTS7);
        db.execSQL("drop table if exists " + TABLE_CONTACTS8);
        db.execSQL("drop table if exists " + TABLE_CONTACTS9);
        onCreate(db);

    }
}
