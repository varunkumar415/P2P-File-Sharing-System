package edu.iit.hawk.vkumar17.madprojectteam13.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Movie;
import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Person;


public class JSONUtils {

    public static ArrayList<Model_Person> parsePersonResponse(String jsonStr) {

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray results = jsonObject.getJSONArray("results");

            ArrayList<Model_Person> modelPersons = new ArrayList<Model_Person>();

            for(int i = 0; i < results.length(); i++){
                JSONObject result = results.getJSONObject(i);
                Model_Person modelPerson = new Model_Person();

                //modelPerson.adult = result.getBoolean("adult");
                modelPerson.name = result.getString("name");
                modelPerson.id = result.getString("id");
                modelPerson.popularity = result.getString("popularity");
                modelPerson.profilePath = result.getString("profile_path");
                modelPersons.add(modelPerson);
            }

            return modelPersons;
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;

    }

    public static ArrayList<Model_Movie> parseMovieResponse(String jsonStr) {
        try{
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray results = jsonObject.getJSONArray("results");

            ArrayList<Model_Movie> movies = new ArrayList<Model_Movie>();

            for(int i = 0; i < results.length(); i++){
                JSONObject result = results.getJSONObject(i);
                Model_Movie modelMovie = new Model_Movie();
                modelMovie.rating = result.getString("vote_average");
                modelMovie.adult = result.getBoolean("adult");
                modelMovie.id = result.getString("id");
                modelMovie.name = result.getString("title");
                modelMovie.originalName = result.getString("original_title");
                modelMovie.popularity = result.getString("popularity");
                modelMovie.released = result.getString("release_date");
                modelMovie.votes = result.getString("vote_count");
                modelMovie.posterPath = result.getString("poster_path");
                movies.add(modelMovie);
            }

            return movies;

        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
