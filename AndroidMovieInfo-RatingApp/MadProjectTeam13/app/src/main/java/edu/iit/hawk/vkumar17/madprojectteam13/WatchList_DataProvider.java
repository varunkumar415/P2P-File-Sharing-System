package edu.iit.hawk.vkumar17.madprojectteam13;

/**
 * Created by Hari on 4/8/2016.
 */
public class WatchList_DataProvider {
    private String name;
    private String des;
    private String dvd;
    private String cd1;
    private String bluray;
    private String usrname;

    public WatchList_DataProvider(String name, String des, String dvd, String cd, String bluray)
    {
        this.name =name;
        this.des=des;
        this.dvd=dvd;
        this.cd1=cd;
        this.bluray=bluray;

    }
    public WatchList_DataProvider(String name, String des,String username)
    {
        this.name =name;
        this.des=des;
        this.usrname=username;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDvd() {
        return dvd;
    }

    public void setDvd(String dvd) {
        this.dvd = dvd;
    }

    public String getCd() {
        return dvd;
    }

    public void setCd(String cd) {
        this.cd1 = cd;
    }

    public String getBluray() {
        return bluray;
    }

    public void setBluray(String bluray) {
        this.bluray = bluray;
    }

    public String getUsrName() {
        return usrname;
    }

    public void setUsrName(String name) {
        this.usrname = usrname;
    }

}