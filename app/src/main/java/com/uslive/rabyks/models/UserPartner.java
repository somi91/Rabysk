package com.uslive.rabyks.models;

/**
 * Created by ustankovic on 01-07-15.
 */
public class UserPartner {

    private int userId;
    private int partnerId;

    public UserPartner() {
    }

    public UserPartner(int userId, int partnerId) {
        this.userId = userId;
        this.partnerId = partnerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }
}
