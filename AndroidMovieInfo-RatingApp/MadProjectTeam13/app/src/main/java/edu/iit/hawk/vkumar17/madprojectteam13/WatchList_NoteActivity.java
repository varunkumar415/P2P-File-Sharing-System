package edu.iit.hawk.vkumar17.madprojectteam13;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hari on 4/7/2016.
 */
public class WatchList_NoteActivity extends Activity{

    EditText MovieName,Description,EmailAddress;
    CheckBox cb1, cb2, cb3;
    String checked1="No",checked2="No",checked3="No";
    Context context=this;
    WatchList_UserDbHandler userDbHelper;
    SQLiteDatabase sqLiteDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist_note);
        MovieName=(EditText)findViewById(R.id.edit1);
        Description=(EditText)findViewById(R.id.edit2);
        // EmailAddress=(EditText)findViewById(R.id.edit3);
        cb1 = (CheckBox)findViewById(R.id.text2);
        cb2 = (CheckBox)findViewById(R.id.text);
        cb3 = (CheckBox)findViewById(R.id.textView4);

        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked1 = "Yes";
                }else{
                    checked1 = "No";
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked2 = "Yes";
                }else{
                    checked2 = "No";
                }
            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    checked3 = "Yes";
                }else{
                    checked3 = "No";
                }
            }
        });



    }

    public void addContant(View view) {
        String movie_name = MovieName.getText().toString();
        String description = Description.getText().toString();
        String ch1 = checked1;
        String ch2 = checked2;
        String ch3 = checked3;
        //String viewed=EmailAddress.getText().toString();
        userDbHelper = new WatchList_UserDbHandler(context);
        sqLiteDatabase = userDbHelper.getWritableDatabase();

        String currentUserName= SessionApplication.getUsername();

        if (movie_name.isEmpty() || description.isEmpty()) {
            Toast.makeText(getBaseContext(), "Movie/Description fields should not be empty", Toast.LENGTH_LONG).show();
        } else {
            userDbHelper.addInformation(movie_name, description, ch1, ch2, ch3, currentUserName, sqLiteDatabase);
            Toast.makeText(getBaseContext(), "Data saved", Toast.LENGTH_LONG).show();
            userDbHelper.close();
        }

    }

    public void historyFunc(View view){
        Intent intent = new Intent(this, WatchedMovieListActivity.class);
        startActivity(intent);
        finish();
    }

    public void allhistoryFunc(View view){
        Intent intent = new Intent(this, WatchedAllMovieListActivity.class);
        startActivity(intent);
        finish();
    }


}
