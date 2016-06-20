package edu.iit.hawk.vkumar17.madprojectteam13;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.alertdialogs.Alert_DialogNoInternetConnection;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Moviepop;


public class MostpopmovActivity extends AppCompatActivity implements MostpopmovActivityFragment.Callback {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostpopmov);

        if ( !isNetworkAvailable() ){
            noInternetAlertMessage();
        }

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailActivityFragment(),
                                DetailActivityFragment.TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            //Toast.makeText(getBaseContext(), "Please Check Internet Connection..", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(Model_Moviepop modelMoviepop) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_MOVIE, modelMoviepop);

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DetailActivityFragment.TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivityFragment.DETAIL_MOVIE, modelMoviepop);
            startActivity(intent);
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

}
