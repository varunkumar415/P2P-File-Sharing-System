package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class WatchedMovieListActivity extends ActionBarActivity {
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    WatchList_UserDbHandler userDbHelper;
    Cursor cursor;
    WatchList_ListDataAdpter watchListListDataAdpter;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchedmovielistlayout);
        listView=(ListView)findViewById(R.id.reviewList1);
        watchListListDataAdpter =new WatchList_ListDataAdpter(getApplicationContext(), R.layout.watched_row_layout);

        userDbHelper=new WatchList_UserDbHandler(getApplicationContext());
        sqLiteDatabase=userDbHelper.getReadableDatabase();
        cursor=userDbHelper.getInformation(sqLiteDatabase);
        if(cursor.moveToFirst())
        {
            do {
                String name,des,dvd,cd,bluray;

                name= cursor.getString(0);
                des=cursor.getString(1);
                dvd= cursor.getString(2);
                cd=cursor.getString(3);
                bluray=cursor.getString(4);
                WatchList_DataProvider watchListDataProvider =new WatchList_DataProvider(name,des,dvd,cd,bluray);
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