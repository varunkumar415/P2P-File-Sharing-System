package edu.iit.hawk.vkumar17.madprojectteam13.model;

import java.util.Comparator;


public class ActorFilmComparator implements Comparator<Model_Actor_Film>{

    @Override
    public int compare(Model_Actor_Film f1, Model_Actor_Film f2) {

        if (f1.getDate().before(f2.getDate())) {
            return -1;
        } else if (f1.getDate().after(f2.getDate())) {
            return 1;
        } else {
            return 0;
        }
    }

}
