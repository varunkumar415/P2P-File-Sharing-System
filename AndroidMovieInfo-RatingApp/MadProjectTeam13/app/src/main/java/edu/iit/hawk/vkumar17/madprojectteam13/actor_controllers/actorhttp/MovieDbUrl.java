package edu.iit.hawk.vkumar17.madprojectteam13.actor_controllers.actorhttp;


public class MovieDbUrl {


    private volatile static MovieDbUrl uniqueInstance;

    private final String url = "http://api.themoviedb.org/3/";
    private final String API_KEY = "968f12643279971a28aa66d13d1453ad";

    private MovieDbUrl() {
    }


    public static MovieDbUrl getInstance() {
        if (uniqueInstance == null) {
            synchronized (MovieDbUrl.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new MovieDbUrl();
                }
            }
        }
        return uniqueInstance;
    }

    public String getActorQuery(String nameToSearch){
        return url + "search/person?api_key=" + API_KEY + "&query=" + nameToSearch + "&sort_by=popularity";
    }

    public String getFilmographyQuery(int actorId){
        return url + "person/" + actorId + "?api_key=" + API_KEY + "&append_to_response=credits";
    }

}
