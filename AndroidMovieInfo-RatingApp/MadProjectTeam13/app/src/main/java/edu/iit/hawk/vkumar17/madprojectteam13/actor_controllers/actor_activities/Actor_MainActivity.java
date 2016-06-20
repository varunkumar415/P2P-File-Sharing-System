package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actor_activities;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actorhttp.AsyncDownloader;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actorhttp.MovieDbUrl;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.alertdialogs.Alert_DialogNoInternetConnection;

public class Actor_MainActivity extends AppCompatActivity {

    private static final String TAG = Actor_MainActivity.class.getSimpleName();

    @Bind(R.id.nameEditText)EditText nameLabel;
    @Bind(R.id.search_button)Button searchButton;
    //@Bind(R.id.poweredTextView)TextView poweredBy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actormain);

        ButterKnife.bind(this);

        if ( !isNetworkAvailable() ){
            noInternetAlertMessage();
        }

        setTypeFaces();
    }


    @OnClick(R.id.search_button)
    public void searchButtonClick() {

        String actorNameToSearch = nameLabel.getText().toString();
        if(actorNameToSearch==null)
        {
            Toast.makeText(getBaseContext(), "Enter an Actor Name", Toast.LENGTH_LONG).show();
        }

        if(actorNameToSearch.length() >= 2){

            MovieDbUrl url = MovieDbUrl.getInstance();
            String getActorHttpMethod = url.getActorQuery(actorNameToSearch);

            AsyncDownloader downloader = new AsyncDownloader(this, new Actors_Activity().getClass());
            downloader.execute(getActorHttpMethod);

        } else {
            Toast.makeText(this, "Invalid actor/actress name, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void noInternetAlertMessage() {
        Alert_DialogNoInternetConnection dialog = new Alert_DialogNoInternetConnection();
        dialog.show(getFragmentManager(), "no_internet_error_dialog");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void setTypeFaces() {
        Typeface latoBlack = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        nameLabel.setTypeface(latoBlack);
        searchButton.setTypeface(latoBlack);
       // poweredBy.setTypeface(latoBlack);
    }
}
