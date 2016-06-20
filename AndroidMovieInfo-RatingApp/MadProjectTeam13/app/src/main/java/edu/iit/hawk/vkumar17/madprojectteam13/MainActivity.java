package edu.iit.hawk.vkumar17.madprojectteam13;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actor_activities.Actor_MainActivity;
import edu.iit.hawk.vkumar17.madprojectteam13.ui.Main_movieinfo;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String currentUserName=SessionApplication.getUsername();
        final TextView text = (TextView) findViewById(R.id.username_show);
        text.setText("Welcome "+currentUserName);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void movieinfo(View view)
    {
        Intent intent = new Intent(this, Main_movieinfo.class);
        startActivity(intent);


    }

    public void actorinfo(View view)
    {

        Intent intent = new Intent(this, Actor_MainActivity.class);
        startActivity(intent);


    }

    //-----------------------------------------------------------------
    public void watchlist(View view)
    {
        Intent intent = new Intent(this, WatchList_NoteActivity.class);
        startActivity(intent);


    }

    public void nowPlaying(View view)
    {
        Intent intent = new Intent(this, NowPlayingActivity.class);
        startActivity(intent);


    }




    public void signout(View view)
    {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void popmovies(View view)
    {
        Intent intent = new Intent(this, MostpopmovActivity.class);
        startActivity(intent);


    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
