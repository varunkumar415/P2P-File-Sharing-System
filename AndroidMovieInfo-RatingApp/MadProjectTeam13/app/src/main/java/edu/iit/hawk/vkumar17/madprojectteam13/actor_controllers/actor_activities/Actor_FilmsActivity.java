package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actor_activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.iit.hawk.vkumar17.madprojectteam13.R;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.adapters.ActorFilmsAdapter;
import edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.transformation.Img_RoundedTransformation;
import edu.iit.hawk.vkumar17.madprojectteam13.model.ActorFilmComparator;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Actor_Film;



public class Actor_FilmsActivity extends AppCompatActivity {

    public static final String TAG = Actor_FilmsActivity.class.getSimpleName();


    @Bind(R.id.actorNameLabel)TextView actorLabel;
    @Bind(R.id.actorProfileImage)ImageView actorPicture;
    @Bind(R.id.filmsRecyclerView)RecyclerView filmsRecyclerView;


    private List<Model_Actor_Film> actorFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actorfilms);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        if (intent != null) {

            String jsonData = intent.getExtras().getString("jsonData");

            getActorFilmsFrom(jsonData);
        }

        gatherRecyclerView();
        setTypeFaces();
    }

    private void getActorFilmsFrom(String jsonData) {

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(jsonData);

            setActorNameAndImage(jsonResponse);

            JSONObject credits = jsonResponse.getJSONObject("credits");
            JSONArray cast = credits.getJSONArray("cast");

            int dataSize = cast.length();

            if (dataSize == 0) {
                displayNotFoundNotification();
            }

            actorFilms = new ArrayList<>(dataSize);

            for (int i = 0; i < dataSize; i++) {

                JSONObject jsonFilm = cast.getJSONObject(i);

                Model_Actor_Film actorFilm = new Model_Actor_Film();

                String dateString = jsonFilm.getString("release_date");

                if (dateString != null && !dateString.equals("null")) {

                    actorFilm.setTitle(jsonFilm.getString("title"));
                    actorFilm.setPosterPath(jsonFilm.getString("poster_path"));
                    actorFilm.setRole(jsonFilm.getString("character"));
                    actorFilm.setFormattedDate(dateString);

                    actorFilms.add(actorFilm);
                }
            }

            Collections.sort(actorFilms, new ActorFilmComparator());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setActorNameAndImage(JSONObject jsonResponse) throws JSONException {
        actorLabel.setText(jsonResponse.getString("name"));
        Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w185" + jsonResponse.getString("profile_path"))
                .transform(new Img_RoundedTransformation(20, 5))
                .error(R.drawable.noprofile)
                .into(actorPicture);
    }


    private void gatherRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.filmsRecyclerView);
        ActorFilmsAdapter adapter = new ActorFilmsAdapter(Actor_FilmsActivity.this, actorFilms);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Actor_FilmsActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
    }

    private void setTypeFaces() {
        Typeface latoBlack = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        actorLabel.setTypeface(latoBlack);

    }

    private void displayNotFoundNotification() {
        Toast.makeText(this,
                "Sorry we couldn't find anything, Please try again",
                Toast.LENGTH_SHORT).
                show();
        finish();
    }

}

