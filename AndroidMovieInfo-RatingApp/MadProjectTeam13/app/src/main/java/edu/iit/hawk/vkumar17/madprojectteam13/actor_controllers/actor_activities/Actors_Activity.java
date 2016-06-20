package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actor_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.adapters.ActorsAdapter;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Actor;


public class Actors_Activity extends AppCompatActivity {

    private static final String TAG = Actors_Activity.class.getSimpleName();

    private List<Model_Actor> modelActors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actors);

        Intent intent = getIntent();

        if (intent != null) {

            String jsonData = intent.getExtras().getString("jsonData");
            {
                if(jsonData==null)
                {
                    Toast.makeText(getBaseContext(), "Please Check Internet connection..", Toast.LENGTH_LONG).show();

                    return;
                }
            }
            getActorsFrom(jsonData);
        }

        populateRecyclerView();

    }

    private void getActorsFrom(String rawJSON) {
        JSONObject results = null;

        try {
            results = new JSONObject(rawJSON);
            JSONArray data = results.getJSONArray("results");

            int dataSize = data.length();

            if (dataSize == 0) {
                showNotFoundNotification();
                super.onBackPressed();
            }

            modelActors = new ArrayList<>(dataSize);

            for (int i = 0; i < dataSize; i++) {
                JSONObject jsonActor = data.getJSONObject(i);

                Model_Actor tempModelActor = new Model_Actor();
                tempModelActor.setId(jsonActor.getInt("id"));
                tempModelActor.setName(jsonActor.getString("name"));
                tempModelActor.setPicturePath(jsonActor.getString("profile_path"));

                JSONArray tempArray = jsonActor.getJSONArray("known_for");
                JSONObject knownFor = tempArray.getJSONObject(0);

                if (knownFor.getString("media_type").equals("movie")) {
                    tempModelActor.setKnownForFilm(knownFor.getString("original_title"));

                }

                modelActors.add(tempModelActor);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    private void populateRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.actorsRecyclerView);
        ActorsAdapter adapter = new ActorsAdapter(Actors_Activity.this, modelActors);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Actors_Activity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
    }

    private void showNotFoundNotification() {
        Toast.makeText(this,
                "Sorry we couldn't find anything, please try again",
                Toast.LENGTH_SHORT).
                show();
    }
}
