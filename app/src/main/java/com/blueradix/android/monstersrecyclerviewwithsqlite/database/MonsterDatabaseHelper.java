package com.blueradix.android.monstersrecyclerviewwithsqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Helper class where we will write all operations related to the database
 */
public class MonsterDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = MonsterDatabaseHelper.class.getName();

    private static MonsterDatabaseHelper mInstance = null;
    private final Context context;

    //create database constants
    private static final String DATABASE_NAME = "monster.db";
    private static final Integer DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "monster";

    //create constants for the table's column name
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_DESCRIPTION = "DESCRIPTION";
    private static final String COL_SCARINESS = "SCARINESS";
    private static final String COL_IMAGE = "IMAGE";
    private static final String COL_VOTES = "VOTES";
    private static final String COL_STARS = "STARS";

    //create sql statements initial version
    private static final String CREATE_TABLE_ST = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT, " +
            COL_DESCRIPTION + " TEXT, " +
            COL_SCARINESS + " INTEGER, " +
            COL_IMAGE + " TEXT)";

    // Keep this SQL statement updated with the latest version of the table
    private static final String CREATE_TABLE_ST_UP_TO_DATE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAME + " TEXT, " +
            COL_DESCRIPTION + " TEXT, " +
            COL_SCARINESS + " INTEGER, " +
            COL_IMAGE + " TEXT, " +
            COL_VOTES + " INTEGER DEFAULT 0, " +
            COL_STARS + " INTEGER DEFAULT 0 )";


    private static final String DROP_TABLE_ST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String GET_ALL_ST = "SELECT * FROM " + TABLE_NAME;
    //adding more queries
    private static final String GET_LAST_INSERTED_ID = "SELECT SEQ FROM SQLITE_SEQUENCE WHERE NAME = ?";
    private static final String GET_MONSTER_BY_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + "= ?";
    private static final String UPDATE_MONSTER_VOTES = "UPDATE " + TABLE_NAME + " SET " + COL_STARS + " = " + COL_STARS + " + ? " + ", " + COL_VOTES + " = " + COL_VOTES + " + 1" + " WHERE " + COL_ID + "= ?";

    public static synchronized MonsterDatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new MonsterDatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Set the new version of the database (newVersion specified in the constant DATABASE_VERSION),
     * if the database new version is greater than the
     * database version stored inside of the database( oldVersion, written there when the db was created ) then
     * the onUpgrade method will be called.
     *
     * @param context provides access to the Activity resources
     */
    public MonsterDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    /**
     * this method gets executed only if the database does not exists
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE_ST_UP_TO_DATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.

        for (int i = oldVersion; i < newVersion; ++i) {
            String migrationFileName = String.format("from_%d_to_%d.sql", i, (i + 1));
            migrationFileName = "databaseFiles/scripts/" + migrationFileName;
            Log.d(TAG, "Looking for migration file: " +  migrationFileName);
            readAndExecuteSQLScript(sqLiteDatabase, context, migrationFileName);
        }

    }

    /**
     * Add a monster to the database
     * @param name          Monster's name
     * @param description   Monster's description
     * @param scariness     Monster's scariness level
     * @return      if it succeeded, the autogenerated id (primary key) of the recently added monster
     *              otherwise -1
     */
    public Long insert(String name, String description, Integer scariness) {
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

        return result;
    }

    /**
     * @return  A cursor of all monsters in the table called monster.
     */
    public Cursor getAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery(GET_ALL_ST, null);
    }

    /**
     * Update a monster record in the database
     * @param id            Primary key of the monster
     * @param name          New Monster's name
     * @param description   New Monster's description
     * @param scariness     New Monster's Scariness level
     * @return      true is the monster record in the database was updated, otherwise false.
     */
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

    /**
     * Delete a monster from the database
     * @param id    Monster's primary key
     * @return      true if the monster was deleted, otherwise false
     */
    public boolean delete(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int numOfAffectedRows = db.delete(TABLE_NAME, "ID = ?", new String[]{id.toString()});
        return numOfAffectedRows != -1;
    }

    /**
     * @return an autogenerated image name
     */
    private String getRandomImageName() {
        Random ran = new Random();

        int value = ran.nextInt(30) + 1;
        return "ic_monster_" + value;
    }

    /**
     * @return a list of all monsters from the database table called monster
     */
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
                Integer votes = cursor.getInt(5);
                Integer stars = cursor.getInt(6);

                monster = new Monster(id, name, description, scariness, imageFileName, votes, stars);
                monsters.add(monster);
            }
        }
        cursor.close();
        return monsters;

    }

    private Cursor getSequenceCursor(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(GET_LAST_INSERTED_ID, new String[]{tableName});
    }

    /**
     * return the last autogenerated id (primary key)in a table, given the table name
     * @param tableName     name of the table in which we want to know the last autogenerated id
     * @return  the last autogenerated id (primary key)in the table @tableName
     */
    public Long getLastInsertedIdInTable(String tableName){
        Long lastId = -999L;
        Cursor cursor = getSequenceCursor(tableName);

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                lastId = cursor.getLong(0);
            }
        }
        cursor.close();
        return lastId;
    }

    /**
     * return A monster from the database given its primary key (id)
     * @param id    Primary key of the monster
     * @return      Monster object
     */
    public Monster getMonster(Long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Monster monster = null;

        Cursor cursor = db.rawQuery(GET_MONSTER_BY_ID, new String[]{id.toString()});

        if(cursor.getCount() > 0)
            while (cursor.moveToNext()){
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                Integer scariness = cursor.getInt(3);
                String imageFileName = cursor.getString(4);
                Integer votes = cursor.getInt(5);
                Integer stars = cursor.getInt(6);

                monster = new Monster(id, name, description, scariness, imageFileName, votes, stars);

            }
        cursor.close();
        return monster;

    }


    public boolean rateMonster(Long id, Integer stars){
        SQLiteDatabase db = this.getWritableDatabase();
        /*
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_VOTES, COL_VOTES + "+1");
        contentValues.put(COL_STARS, COL_STARS + "+" + stars.toString());

        int numRowsUpdated = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id.toString()});
        return numRowsUpdated > 0;
        */

        db.execSQL(UPDATE_MONSTER_VOTES, new String[]{ stars.toString(), id.toString()});

        return true;
    }

    /**
     * reads and execute an upgrade sql file stored in assets
     * @param db        database reference
     * @param ctx       current context
     * @param fileName  file to execute. It MUST have the following format: file_X_to_Y.sql
     *                  where X is the old version of the database
     *                        Y is the new version of the database
     */
    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name " + fileName + " is empty");
            return;
        }

        Log.d(TAG, "Script " + fileName + " found. Executing...");
        AssetManager assetManager = ctx.getAssets();
        BufferedReader reader = null;
        InputStream is = null;
        InputStreamReader isr = null;
        try {
            is = assetManager.open(fileName);
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    isr.close();
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }

    }

    /**
     * Executes every line inside of a file
     * @param db        database reference
     * @param reader    reader used to read lines
     * @throws IOException
     */
    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }
}
