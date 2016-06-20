package edu.iit.hawk.vkumar17.madprojectteam13.services;

import android.util.Log;

import java.util.ArrayList;

import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Movie;
import edu.iit.hawk.vkumar17.madprojectteam13.utils.JSONUtils;

public class MovieSeekerSvc extends Svc_GenericSeeker<Model_Movie> {

    private static final String MOVIE_SEARCH_PATH = "search/movie";

    @Override
    public ArrayList<Model_Movie> find(String query) {
        return retrieveMoviesList(query);
    }

    @Override
    public ArrayList<Model_Movie> find(String query, int maxResults) {
        return retrieveFirstResults(retrieveMoviesList(query), maxResults);
    }

    private ArrayList<Model_Movie> retrieveMoviesList(String query){
        String url = constructSearchUrl(query);
        String response = svcHttpRetriever.retrieve(url);
        if(response == null)
        {
            Log.d(getClass().getSimpleName(), "Network error");
            return null;
        }
        Log.d(getClass().getSimpleName(), response);
        return JSONUtils.parseMovieResponse(response);
    }

    @Override
    public String retrieveSearchMethodPath() {
        return MOVIE_SEARCH_PATH;
    }
}
