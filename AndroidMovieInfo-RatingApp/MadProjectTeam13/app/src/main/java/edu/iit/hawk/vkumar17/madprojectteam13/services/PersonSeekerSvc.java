package edu.iit.hawk.vkumar17.madprojectteam13.services;

import android.util.Log;

import java.util.ArrayList;

import edu.iit.hawk.vkumar17.madprojectteam13.model.Model_Person;
import edu.iit.hawk.vkumar17.madprojectteam13.utils.JSONUtils;


public class PersonSeekerSvc extends Svc_GenericSeeker<Model_Person> {

    private static final String PERSON_SEARCH_PATH = "search/person";

    @Override
    public ArrayList<Model_Person> find(String query) {
        return retrievePersonsList(query);
    }

    @Override
    public ArrayList<Model_Person> find(String query, int maxResults) {
        return retrieveFirstResults(retrievePersonsList(query),maxResults);
    }

    private ArrayList<Model_Person> retrievePersonsList(String query){
        String url = constructSearchUrl(query);
        String response = svcHttpRetriever.retrieve(url);
        Log.d(getClass().getSimpleName(), response);
        return JSONUtils.parsePersonResponse(response);
    }

    @Override
    public String retrieveSearchMethodPath() {
        return PERSON_SEARCH_PATH;
    }
}
