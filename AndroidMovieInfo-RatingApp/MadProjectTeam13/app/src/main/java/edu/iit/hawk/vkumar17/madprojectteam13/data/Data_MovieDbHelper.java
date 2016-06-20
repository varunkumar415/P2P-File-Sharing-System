package edu.iit.hawk.vkumar17.madprojectteam13.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Data_MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public Data_MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + Data_MovieContract.MovieEntry.TABLE_NAME + " (" +
                Data_MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Data_MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                Data_MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                Data_MovieContract.MovieEntry.COLUMN_IMAGE + " TEXT, " +
                Data_MovieContract.MovieEntry.COLUMN_IMAGE2 + " TEXT, " +
                Data_MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                Data_MovieContract.MovieEntry.COLUMN_RATING + " INTEGER, " +
                Data_MovieContract.MovieEntry.COLUMN_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Data_MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
