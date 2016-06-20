package edu.iit.hawk.vkumar17.madprojectteam13;

/**
 * Created by Hari on 4/7/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class WatchList_UserDbHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="USER_INFO.DB";
    private static final int DATABASE_VERSION=5;
    private static final String user_name="user_email";

    private static final String CREATE_QUERY="CREATE TABLE "+ WatchList_Userinfo.NewUserInfo.TABLE_NAME+"("+ WatchList_Userinfo.NewUserInfo.REVIEW_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            WatchList_Userinfo.NewUserInfo.USER_NAME+" TEXT,"+ WatchList_Userinfo.NewUserInfo.USER_MOB+" TEXT, "
            + WatchList_Userinfo.NewUserInfo.USER_DVD+" TEXT,"+ WatchList_Userinfo.NewUserInfo.USER_CD+" TEXT,"+ WatchList_Userinfo.NewUserInfo.USER_Bluray+" TEXT,"+user_name+" TEXT);";

    public WatchList_UserDbHandler(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DATABASE OPERATION", "Database created / opened.....");


    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATION", "Table create..."+CREATE_QUERY);
    }
    public void addInformation(String name,String des,String ch1,String ch2,String ch3, String username, SQLiteDatabase db){
            db = this.getWritableDatabase();
            ContentValues contentValue = new ContentValues();
            contentValue.put(WatchList_Userinfo.NewUserInfo.USER_NAME, name);
            contentValue.put(WatchList_Userinfo.NewUserInfo.USER_MOB, des);
            contentValue.put(WatchList_Userinfo.NewUserInfo.USER_DVD, ch1);
            contentValue.put(WatchList_Userinfo.NewUserInfo.USER_CD, ch2);
            contentValue.put(WatchList_Userinfo.NewUserInfo.USER_Bluray, ch3);
            contentValue.put(user_name, username);

            //contentValue.put(WatchList_Userinfo.NewUserInfo.USER_EMAIL,email);

            db.insert(WatchList_Userinfo.NewUserInfo.TABLE_NAME, null, contentValue);
            Log.e("DATABASE OPERATION", "row is inserted");


    }

    public Cursor getInformation(SQLiteDatabase db){
        String currentUserName= SessionApplication.getUsername();
        Cursor cursor;
        //String[] projections={ WatchList_Userinfo.NewUserInfo.USER_NAME, WatchList_Userinfo.NewUserInfo.USER_MOB, WatchList_Userinfo.NewUserInfo.USER_DVD, WatchList_Userinfo.NewUserInfo.USER_CD, WatchList_Userinfo.NewUserInfo.USER_Bluray};
        //cursor= db.query(WatchList_Userinfo.NewUserInfo.TABLE_NAME +" WHERE " + user_name + " = " + currentUserName, projections, null, null, null, null, null);
        //String retq="SELECT * FROM "+WatchList_Userinfo.NewUserInfo.TABLE_NAME+"WHERE " + user_name + " = " +  ;
        //cursor= db.rawQuery(retq, null);
        cursor = db.rawQuery("SELECT user_name,user_mob,user_dvd,user_cd,user_bluray FROM USER_INFO WHERE " + user_name + "=? ", new String[]{currentUserName});

        //cursor = db.rawQuery("SELECT " + WatchList_Userinfo.NewUserInfo.USER_NAME + WatchList_Userinfo.NewUserInfo.USER_MOB + WatchList_Userinfo.NewUserInfo.USER_DVD + WatchList_Userinfo.NewUserInfo.USER_CD + WatchList_Userinfo.NewUserInfo.USER_Bluray + " FROM user_info WHERE " + user_name + "=?" , new String[]{currentUserName});
        return cursor;
    }

    public Cursor getallInformation(SQLiteDatabase db){
        //String currentUserName=SessionApplication.getUsername();
        Cursor cursor;
        String[] projections={ WatchList_Userinfo.NewUserInfo.USER_NAME, WatchList_Userinfo.NewUserInfo.USER_MOB, user_name};
        cursor= db.query(WatchList_Userinfo.NewUserInfo.TABLE_NAME, projections, null, null, null, null, null);
        //String retq="SELECT * FROM "+WatchList_Userinfo.NewUserInfo.TABLE_NAME+"WHERE " + user_name + " = " +  ;
        //cursor= db.rawQuery(retq, null);
        //cursor = db.rawQuery("SELECT user_name,user_mob,user_dvd,user_cd,user_bluray FROM USER_INFO WHERE " + user_name + "=? ", new String[]{currentUserName});

        //cursor = db.rawQuery("SELECT " + WatchList_Userinfo.NewUserInfo.USER_NAME + WatchList_Userinfo.NewUserInfo.USER_MOB + WatchList_Userinfo.NewUserInfo.USER_DVD + WatchList_Userinfo.NewUserInfo.USER_CD + WatchList_Userinfo.NewUserInfo.USER_Bluray + " FROM user_info WHERE " + user_name + "=?" , new String[]{currentUserName});
        return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
