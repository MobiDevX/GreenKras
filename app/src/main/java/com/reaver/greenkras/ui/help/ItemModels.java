package com.reaver.greenkras.ui.help;

public class ItemModels {

    private String name;
    private String disc;
    private int image;

    public ItemModels(String name, String disc, int image) {
        this.name = name;
        this.disc = disc;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
