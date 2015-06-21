package com.uslive.rabyks.models;

/**
 * Created by milos on 5/24/2015.
 */
public class Club {
    // private variables
    int _id;
    String _name;
    int _url;
    byte[] _image;
    int _uid;
    Long _created_at;

    // Empty constructor
    public Club() {

    }

    // constructor
    public Club(int keyId, String name, int url, byte[] image, int uid, Long created_at) {
        this._id = keyId;
        this._name = name;
        this._url = url;
        this._image = image;
        this._uid = uid;
        this._created_at = created_at;

    }

    // constructor
    public Club(String name, int url, byte[] image, int uid, Long created_at) {
        this._name = name;
        this._url = url;
        this._image = image;
        this._uid = uid;
        this._created_at = created_at;
    }

    // getting ID
    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String name) {
        this._name = name;
    }

    public int get_url() {
        return _url;
    }

    public void set_url(int url) {
        this._url = url;
    }

    public byte[] get_image() {
        return _image;
    }

    public void set_image(byte[] image) {
        this._image = image;
    }

    public int get_uid() {
        return _uid;
    }

    public void set_uid(int uid) {
        this._uid = uid;
    }

    public Long get_created_at() {
        return _created_at;
    }

    public void set_created_at(Long created_at) {
        this._created_at = created_at;
    }

}
