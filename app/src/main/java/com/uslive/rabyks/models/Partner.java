package com.uslive.rabyks.models;

/**
 * Created by milos on 5/31/2015.
 */
public class Partner {
    private int partner_id;
    private String name;
    private String socket_url;
    private String number;
    private String address;
    private String imageUrl;
    private Long created_at;

    public Partner() {

    }

    public Partner(int partner_id, String name, String socket_url, String number, String address, String imageUrl) {
        this.partner_id = partner_id;
        this.name = name;
        this.socket_url = socket_url;
        this.number = number;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public int getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(int partner_id) {
        this.partner_id = partner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocket_url() {
        return socket_url;
    }

    public void setSocket_url(String socket_url) {
        this.socket_url = socket_url;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }
}
