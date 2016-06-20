package edu.iit.hawk.vkumar17.madprojectteam13.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Model_Review {

    private String id;
    private String author;
    private String content;

    public Model_Review() {

    }

    public Model_Review(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.author = trailer.getString("author");
        this.content = trailer.getString("content");
    }

    public String getId() { return id; }

    public String getAuthor() { return author; }

    public String getContent() { return content; }
}
