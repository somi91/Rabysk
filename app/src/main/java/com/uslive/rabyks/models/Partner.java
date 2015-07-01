package com.uslive.rabyks.models;

/**
 * Created by milos on 5/31/2015.
 */
public class Partner {
    private int id;
    private String name;
    private String address;
    private String number;
    private String logo_url;
    private byte[] logo_url_bytes;
    private String layout_img_url;
    private int type;
    private String working_hours;
    private Long created_at;
    private Long modified_at;

    public Partner() {

    }

    public Partner(int id, String name, String address, String number, String logo_url, byte[] logo_url_bytes, String layout_img_url, int type, String working_hours, Long created_at, Long modified_at) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.number = number;
        this.logo_url = logo_url;
        this.logo_url_bytes = logo_url_bytes;
        this.layout_img_url = layout_img_url;
        this.type = type;
        this.working_hours = working_hours;
        this.created_at = created_at;
        this.modified_at = modified_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public byte[] getLogo_url_bytes() {
        return logo_url_bytes;
    }

    public String getLayout_img_url() {
        return layout_img_url;
    }

    public void setLayout_img_url(String layout_img_url) {
        this.layout_img_url = layout_img_url;
    }

    public void setLogo_url_bytes(byte[] logo_url_bytes) {
        this.logo_url_bytes = logo_url_bytes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(String working_hours) {
        this.working_hours = working_hours;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public Long getModified_at() {
        return modified_at;
    }

    public void setModified_at(Long modified_at) {
        this.modified_at = modified_at;
    }
}