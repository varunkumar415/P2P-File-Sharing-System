package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class Welcome_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome__screen);

        MediaPlayer objPlayer = MediaPlayer.create(getApplicationContext(), R.raw.speech);
        objPlayer.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        return super.onTouchEvent(event);

    }

}
