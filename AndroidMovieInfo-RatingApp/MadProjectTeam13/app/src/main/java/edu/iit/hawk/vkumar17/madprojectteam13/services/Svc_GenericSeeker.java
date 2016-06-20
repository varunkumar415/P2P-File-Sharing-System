package edu.iit.hawk.vkumar17.madprojectteam13.services;

import java.net.URLEncoder;
import java.util.ArrayList;


public abstract class Svc_GenericSeeker<E> {

    protected static final String BASE_URL = "http://api.themoviedb.org/3/";
    protected static final String API_KEY = "968f12643279971a28aa66d13d1453ad";

    protected Svc_HttpRetriever svcHttpRetriever = new Svc_HttpRetriever();

    public abstract ArrayList<E> find(String query);
    public abstract ArrayList<E> find(String query, int maxResults);

    public abstract String retrieveSearchMethodPath();

    protected String constructSearchUrl(String query) {
        StringBuffer sb = new StringBuffer();
        sb.append(BASE_URL);
        sb.append(retrieveSearchMethodPath());
        sb.append("?api_key=");
        sb.append(API_KEY);
        sb.append("&query=");
        sb.append(URLEncoder.encode(query));
        return sb.toString();
    }

    public ArrayList<E> retrieveFirstResults(ArrayList<E> list, int maxResults) {
        ArrayList<E> newList = new ArrayList<E>();
        int count = Math.min(list.size(), maxResults);
        for (int i=0; i<count; i++) {
            newList.add(list.get(i));
        }
        return newList;
    }

}
