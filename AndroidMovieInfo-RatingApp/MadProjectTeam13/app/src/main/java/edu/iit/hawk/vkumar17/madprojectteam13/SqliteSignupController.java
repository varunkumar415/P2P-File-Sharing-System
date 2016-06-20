package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Varun on 4/27/2016.
 */

public class SqliteSignupController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    public String table_name="user_details";
    public String col1="name";
    public String col2="email";
    public String col3="password";
    String[] myStringArray = new String[2];



    public SqliteSignupController(Context applicationcontext) {
        super(applicationcontext, "signup.db", null, 3);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE user_details ( name TEXT, email TEXT,password TEXT,CONSTRAINT user_pk PRIMARY KEY (email))";
        database.execSQL(query);
        Log.d(LOGCAT, "Table Created");
    }
    //
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS user_details";
        database.execSQL(query);
        onCreate(database);
    }

    public long insertuser(String name,String email, String password) {

        long status;
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);

        status= database.insert("user_details", null, values);
        return status;


    }
    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        return db.rawQuery("select uname as _id,email from user_details", null);
    }


    public String validate_user(String email,String Password )
    {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT email FROM user_details WHERE " + col2 + "=? AND password=?", new String[]{email,Password});
            //Cursor c = db.rawQuery("SELECT email FROM user_details WHERE " + col2 + "=? AND" +col3+"=?", new String[]{email,Password});
            c.moveToFirst();
            return c.getString(c.getColumnIndex("email"));
        }
        catch (Exception e){
            return null;
        }


    }

    /*
    public String[] validate_email_and_send_email(String email){

        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT uname as un,password as pwd FROM user_details WHERE " + col6 + "=?", new String[]{email});
            c.moveToFirst();
            myStringArray[0]= c.getString(c.getColumnIndex("un"));
            myStringArray[1]=c.getString(c.getColumnIndex("pwd"));
            return myStringArray;
        }
        catch (Exception e)
        {
            myStringArray=null;
            return myStringArray;
        }



    }
    */

    public void cleardb()
    {   SQLiteDatabase database = this.getWritableDatabase();
        String query="delete from user_details";
        database.execSQL(query);
    }


}
