package edu.iit.hawk.vkumar17.madprojectteam13;

import android.app.Application;

/**
 * Created by Varun on 4/27/2016.
 */
public class SessionApplication extends Application {
    private static String username;
    private static String password;

    @Override
    public void onCreate() {
        super.onCreate();
        username="";
        password="";
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionApplication.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        SessionApplication.password = password;
    }

}
