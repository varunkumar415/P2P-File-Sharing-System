package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class WatchedAllMovieListActivity extends ActionBarActivity {
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    WatchList_UserDbHandler userDbHelper;
    Cursor cursor;
    WatchList_AllListDataAdpter watchListListDataAdpter;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_watched_all_movie_list);
        }
        catch (Exception ex){ return;}

            listView=(ListView)findViewById(R.id.reviewList1);
        watchListListDataAdpter =new WatchList_AllListDataAdpter(getApplicationContext(), R.layout.watched_allrow_layout);

        userDbHelper=new WatchList_UserDbHandler(getApplicationContext());
        sqLiteDatabase=userDbHelper.getReadableDatabase();
        cursor=userDbHelper.getallInformation(sqLiteDatabase);
        if(cursor.moveToFirst())
        {
            do {
                String name,des,username;

                name= cursor.getString(0);
                des=cursor.getString(1);
                //dvd= cursor.getString(2);
                //cd=cursor.getString(3);
                //bluray=cursor.getString(4);
                username=cursor.getString(2);
                WatchList_DataProvider watchListDataProvider =new WatchList_DataProvider(name,des,username);
                watchListListDataAdpter.add(watchListDataProvider);

            }while (cursor.moveToNext());
        }listView.setAdapter(watchListListDataAdpter);
    }
    public void backFunc(View view){
        Intent intent = new Intent(this, WatchList_NoteActivity.class);
        startActivity(intent);
        finish();
    }
}
