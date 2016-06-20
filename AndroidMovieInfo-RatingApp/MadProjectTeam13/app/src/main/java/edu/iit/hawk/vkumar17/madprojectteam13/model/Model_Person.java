package edu.iit.hawk.vkumar17.madprojectteam13.model;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;


public class Model_Person implements Serializable{

    public String score;
    public String popularity;
    public String name;
    public String id;
    public String biography;
    public String url;
    public String version;
    public String lastModifiedAt;
    public ArrayList<Image> imagesList;
    public String profilePath;

}