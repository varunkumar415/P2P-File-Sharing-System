package edu.iit.hawk.vkumar17.madprojectteam13.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Movie;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Person;
import edu.iit.hawk.vkumar17.madprojectteam13.services.MovieSeekerSvc;
import edu.iit.hawk.vkumar17.madprojectteam13.services.PersonSeekerSvc;
import edu.iit.hawk.vkumar17.madprojectteam13.services.Svc_GenericSeeker;


public class Main_movieinfo extends Activity {


    EditText searchText;
    Button searchButton;
   // RadioGroup searchRadioGroup;
    //RadioButton movieRadioButton;
   // RadioButton personRadioButton;

    private Svc_GenericSeeker<Model_Movie> movieSeeker = new MovieSeekerSvc();
    private Svc_GenericSeeker<Model_Person> personSeeker = new PersonSeekerSvc();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_movieinfo);

        findAllViewsById();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchText.getText().toString().trim();

                if(query.equals(""))
                    Toast.makeText(getBaseContext(), "Enter a Movie Name", Toast.LENGTH_LONG).show();
                else{
                    performSearch(query);
                }
            }
        });

        searchText.setOnFocusChangeListener(new DefaultTextOnFocusChangeListener(getString(R.string.search)));
    }

    private void findAllViewsById() {
        searchText = (EditText)findViewById(R.id.search_edit_text);
        searchButton = (Button)findViewById(R.id.search_button);
        //searchRadioGroup = (RadioGroup)findViewById(R.id.search_radio_group);
      //  movieRadioButton = (RadioButton) findViewById(R.id.philm_radio_button);
      //  personRadioButton = (RadioButton) findViewById(R.id.philmi_star_radio_button);
    }



    private void performSearch(String query) {

        progressDialog = ProgressDialog.show(Main_movieinfo.this,
                "Please wait...", "Retrieving data...", true, true);

       // if (movieRadioButton.isChecked()) {
            MovieSearchTask task = new MovieSearchTask();
            task.execute(query);
            progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
      //  }
      /*  else if (personRadioButton.isChecked()) {
            PersonSearchTask task = new PersonSearchTask();
            task.execute(query);
            progressDialog.setOnCancelListener(new CancelTaskOnCancelListener(task));
        }*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DefaultTextOnFocusChangeListener implements View.OnFocusChangeListener
    {
        String defaultText;
        public DefaultTextOnFocusChangeListener(String defaultText)
        {
            this.defaultText = defaultText;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {

            if(view instanceof EditText){
                EditText v = (EditText)view;
                String sText = v.getText().toString().trim();
                if(hasFocus){
                    if(sText.equals(defaultText)){
                        v.setText("");
                        v.setTextColor(Color.parseColor("#00FF00"));
                    }
                }
                else{
                    if(sText.equals("")){
                        v.setText(defaultText);
                        v.setTextColor(Color.parseColor("#efefef"));
                    }
                }
            }
        }
    }

    // it will call serverice
    private class MovieSearchTask extends AsyncTask<String, Void, ArrayList<Model_Movie>> {

        @Override
        protected ArrayList<Model_Movie> doInBackground(String... strings) {
            String query = strings[0];
            return movieSeeker.find(query);
        }

        @Override
        protected void onPostExecute(final ArrayList<Model_Movie> modelMovieList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog != null) {
                     progressDialog.dismiss();
                        progressDialog = null;
                    }
                    if(modelMovieList != null) {
                        Intent intent = new Intent(Main_movieinfo.this, MoviesListActivity.class);
                        intent.putExtra("movies", modelMovieList);
                        startActivity(intent);
                    }
                    else{

                        Toast.makeText(getBaseContext(), "Please check Internet conncetivity", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private class PersonSearchTask extends AsyncTask<String, Void, ArrayList<Model_Person>> {

        @Override
        protected ArrayList<Model_Person> doInBackground(String... strings) {
            String query = strings[0];
            return personSeeker.find(query);
        }

        @Override
        protected void onPostExecute(final ArrayList<Model_Person> modelPersonList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    if(modelPersonList != null) {
                        Intent intent = new Intent(Main_movieinfo.this, PersonListActivity.class);
                        intent.putExtra("persons", modelPersonList);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private class CancelTaskOnCancelListener implements DialogInterface.OnCancelListener{

        private AsyncTask<?,?,?> task;

        private CancelTaskOnCancelListener(AsyncTask<?,?,?> task){
            this.task = task;
        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if(task != null){
                task.cancel(true);
            }
        }
    }
}


