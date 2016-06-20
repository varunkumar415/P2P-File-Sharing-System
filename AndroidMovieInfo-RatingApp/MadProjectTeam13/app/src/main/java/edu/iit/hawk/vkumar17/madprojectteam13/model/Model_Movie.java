package edu.iit.hawk.vkumar17.madprojectteam13.model;

import java.io.Serializable;
import java.util.ArrayList;


public class Model_Movie implements Serializable{

    private final String BASE_URL = "http://image.tmdb.org/t/p/";
    private final String SIZE = "w92"; //['w92', 'w154', 'w185', 'w342', 'w500', 'original']
    private final String POSTER_SIZE = "w600";
    private final String SLASH = "/";

    public String score;
    public String popularity;
    public boolean translated;
    public boolean adult;
    public String language;
    public String originalName;
    public String name;
    public String type;
    public String id;
    public String imdbId;
    public String url;
    public String votes;
    public String rating;
    public String certification;
    public String overview;
    public String released;
    public String version;
    public String lastModifiedAt;
    public ArrayList<Image> imagesList;
    public String posterPath;

    public String retrieveThumbnail() {
        if (imagesList!=null && !imagesList.isEmpty()) {
            for (Image movieImage : imagesList) {
                if (movieImage.size.equalsIgnoreCase(Image.SIZE_THUMB) &&
                        movieImage.type.equalsIgnoreCase(Image.TYPE_POSTER)) {
                    return movieImage.url;
                }
            }
        }
        return null;
    }

    public String retrieveThumbnailUrl(){
        if(posterPath !=null && !posterPath.isEmpty())
            return BASE_URL + SIZE + posterPath;

        return null;
    }

    public String retrievePosterUrl(){
        if(posterPath !=null && !posterPath.isEmpty())
            return BASE_URL + POSTER_SIZE + posterPath;

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Model_Movie [name=");
        builder.append(name);
        builder.append("] - ");
        builder.append(rating);
        return builder.toString();
    }
}
