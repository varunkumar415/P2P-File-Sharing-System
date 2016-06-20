package edu.iit.hawk.vkumar17.madprojectteam13.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Model_Actor_Film {

    private String title;
    private String posterPath;
    private String role;

    private Date date;
    private String formattedDate;

    public Model_Actor_Film() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFormattedDate() {

        if (formattedDate == null || formattedDate.equals("null")){
            return "null";
        }

        return formattedDate;
    }

    public void setDate(Date d) {
        date = d;

    }


    public void setFormattedDate(String stringDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date unformatedDate = formatter.parse(stringDate);

            //We use setDate() method so we don't have to repeat code in filmography adapter for every film
            setDate(unformatedDate);

            formattedDate = formatter.format(unformatedDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String dateInString) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date date = formatter.parse(dateInString);

            this.date = date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


}
