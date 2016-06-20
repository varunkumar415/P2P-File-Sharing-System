package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.Context;
import android.database.Cursor;

import edu.iit.hawk.vkumar17.madprojectteam13.data.Data_MovieContract;

public class Utility {

    public static int isFavorited(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                Data_MovieContract.MovieEntry.CONTENT_URI,
                null,
                Data_MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[] { Integer.toString(id) },   // selectionArgs
                null
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }

    public static String buildImageUrl(int width, String fileName) {
        return "http://image.tmdb.org/t/p/w" + Integer.toString(width) + fileName;
    }
}
