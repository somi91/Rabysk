package com.uslive.rabyks.models;

/**
 * Created by marezina on 8.7.2015.
 */
public class DrawerRow {
    private int imageId;
    private String name;

    public DrawerRow(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
