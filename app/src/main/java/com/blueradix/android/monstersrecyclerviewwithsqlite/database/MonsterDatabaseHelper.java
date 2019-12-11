package com.blueradix.android.monstersrecyclerviewwithsqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.blueradix.android.monstersrecyclerviewwithsqlite.Entities.Monster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterDatabaseHelper extends SQLiteOpenHelper {

    //create database constants
    private static final String DATABASE_NAME = "monster.db";
    private static final Integer DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "monster";

    //create constants for the table's column name
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_SCARINESS = "SCARINESS";
    private static final String COL_IMAGE = "IMAGE";

    //create sql statements
    private static final String CREATE_TABLE_ST = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT, " +
            COL_DESCRIPTION + " TEXT, " +
            COL_SCARINESS + " INTEGER, " +
            COL_IMAGE + " TEXT)";

    private static final String DROP_TABLE_ST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String GET_ALL_ST = "SELECT * FROM " + TABLE_NAME;



    /**
     * create the database every time this constructor gets called.
     *
     * @param context provides access to the Activity resources
     */
    public MonsterDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //this method gets executed every time getWritableDatabase or getReadableDatabase is called.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_ST);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String name, String description, Integer scariness) {
        //create an instance of SQLITE database
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_DESCRIPTION, description);
        contentValues.put(COL_SCARINESS, scariness);
        //we store the image name, after using
        //long resId = parent.getResources().getIdentifier(arrayOfStrings[position], "drawable", mApplicationContext.getPackageName());
        //you get the Id of the image as drawable, so you can use it in an image view
        //                int resId = getResources().getIdentifier("bomb", "drawable", this.getPackageName());
        //                imageView.setImageResource(resId);
        contentValues.put(COL_IMAGE, getRandomImageName());

        long result = db.insert(TABLE_NAME, null, contentValues);
        //if result -1  insert was not performed, otherwise will have the row ID of the newly inserted row
        return result != -1;
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery(GET_ALL_ST, null);
    }

    public boolean update(Integer id, String name, String description, Integer scariness) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_DESCRIPTION, description);
        contentValues.put(COL_SCARINESS, scariness);

        int numRowsUpdated = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id.toString()});

        return numRowsUpdated != 1;
    }

    public boolean delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int numOfAffectedRows = db.delete(TABLE_NAME, "ID = ?", new String[]{id.toString()});
        return numOfAffectedRows != -1;
    }

    private String getRandomImageName() {
        Random ran = new Random();

        int value = ran.nextInt(30) + 1;
        return "ic_monster_" + value;
    }

    public List<Monster> getMonsters(){
        List<Monster> monsters = new ArrayList<>();
        Cursor cursor = getAll();

        if(cursor.getCount() > 0){

            Monster monster;

            while (cursor.moveToNext()) {
                Long id = cursor.getLong(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Integer scariness = cursor.getInt(3);
                String imageFileName = cursor.getString(4);

                monster = new Monster(id, name, description, scariness, imageFileName);
                monsters.add(monster);
            }
        }
        cursor.close();
        return monsters;

    }

}
