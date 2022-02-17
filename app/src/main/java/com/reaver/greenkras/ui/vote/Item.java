package com.reaver.greenkras.ui.vote;

public class Item {
    private int id;
    String parkname;
    String parkcount;
    String parklogin;
    String voteyes;
    String CenterX;
    String CenterY;
    String cords;


    void setCords(String cords) {
        this.cords = cords;
    }

    void setVoteyes(String voteyes) {
        this.voteyes = voteyes;
    }

    void setVoteno(String voteno) {
        this.voteno = voteno;
    }

    void setCenterX(String centerX) {
        this.CenterX = centerX;
    }

    void setCenterY(String centerY) {
        this.CenterY = centerY;
    }

    String voteno;


    public void setId(int id) {
        this.id = id;
    }

    void setParkname(String parkname) {
        this.parkname = parkname;
    }

    void setParkcount(String parkcount) {
        this.parkcount = parkcount;
    }

    void setParklogin(String parklogin) {
        this.parklogin = parklogin;
    }


    public int getId() {
        return id;
    }

    String getParkname() {
        return parkname;
    }

    String getParkcount() {
        return parkcount;
    }

    String getParklogin() {
        return parklogin;
    }

    public Item(int id, String parkname, String parkcount, String parklogin, String voteyes, String voteno) {
        this.id = id;
        this.parkname = parkname;
        this.parkcount = parkcount;
        this.parklogin = parklogin;
        this.voteyes = voteyes;
        this.voteno = voteno;
    }
    Item() {

    }



}
