package com.example.coloridentifierapplication.ColorIdentity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";

    public static final String TABLE_COLOR = "ColorTable";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "Color";
    public static final String COLUMN_RGB = "RGB";
    public static final String COLUMN_HEX = "HEX";

    private static final String DATABASE_NAME = "color.db";
    private static final int DATABASE_VERSION = 1;
    List<Color> colorList = new ArrayList<>();

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_COLOR + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_RGB + " TEXT, " +
            COLUMN_HEX + " TEXT );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLOR);
        onCreate(db);
    }

    public void addColor(Color color) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, color.getName());
        values.put(COLUMN_RGB, color.getRgb());
        values.put(COLUMN_HEX, color.getHex());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_COLOR, null, values);
        db.close();
    }

    public boolean deleteColor(Color color) {
        SQLiteDatabase db = this.getWritableDatabase();
        int nrows = db.delete(TABLE_COLOR, COLUMN_ID + " = ?", new String[] {color.getId()});
        db.close();
        return nrows != 0;
    }

    private Color cursorToColor(Cursor cursor)
    {
        Color color = new Color();
        color.setId(cursor.getString(0));
        color.setName(cursor.getString(1));
        color.setRgb(cursor.getString(2));
        color.setHex(cursor.getString(3));
        return color;
    }

    public boolean findColor(String hex) {
        String query = "Select * FROM " + TABLE_COLOR + " WHERE " + COLUMN_HEX + " =  \"" + hex + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Color color=null;
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            color = cursorToColor(cursor);
            cursor.close();
            return false;
        }
        db.close();
        return true;
    }

    public ArrayList<Color> findAllColors() {
        ArrayList<Color> colors = new ArrayList<Color>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_COLOR,
                new String[]{COLUMN_ID,COLUMN_NAME,COLUMN_RGB,COLUMN_HEX}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Color color = cursorToColor(cursor);
            colors.add(color);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        db.close();
        return colors;
    }

    public List<Color> getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String columns[] = {COLUMN_ID,COLUMN_NAME,COLUMN_RGB,COLUMN_HEX};
        Cursor cursor = db.query(TABLE_COLOR,columns,null,null,null, null, null);
        while(cursor.moveToNext()){
            int index = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            String id = cursor.getString(index);
            int index1 = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            String name = cursor.getString(index1);
            int index2 = cursor.getColumnIndex(DatabaseHelper.COLUMN_RGB);
            String rgb = cursor.getString(index2);
            int index3 = cursor.getColumnIndex(DatabaseHelper.COLUMN_HEX);
            String hex = cursor.getString(index3);
            Color color = new Color(id, name, rgb, hex);
            colorList.add(color);
        }
        cursor.close();
        db.close();
        return colorList;
    }
}
