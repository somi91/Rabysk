package com.uslive.rabyks.models;

import java.sql.Timestamp;

/**
 * Created by milos on 5/31/2015.
 */
public class Message {
    private String id;
    private String user_id;
    private Partner partner;
    private String person_count;
    private Timestamp date_of_reservation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public String getPerson_count() {
        return person_count;
    }

    public void setPerson_count(String person_count) {
        this.person_count = person_count;
    }

    public Timestamp getDate_of_reservation() {
        return date_of_reservation;
    }

    public void setDate_of_reservation(Timestamp date_of_reservation) {
        this.date_of_reservation = date_of_reservation;
    }

}
