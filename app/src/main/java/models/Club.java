package models;

/**
 * Created by milos on 5/24/2015.
 */
public class Club {
    // private variables
    int _id;
    String _name;
    String _url;
    byte[] _image;
    String _uid;
    String _created_at;

    // Empty constructor
    public Club() {

    }

    // constructor
    public Club(int keyId, String name, String url, byte[] image, String uid, String created_at) {
        this._id = keyId;
        this._name = name;
        this._url = url;
        this._image = image;
        this._uid = uid;
        this._created_at = created_at;

    }

    // constructor
    public Club(String name, String url, byte[] image, String uid, String created_at) {
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

    public String get_url() {
        return _url;
    }

    public void set_url(String url) {
        this._url = url;
    }

    public byte[] get_image() {
        return _image;
    }

    public void set_image(byte[] image) {
        this._image = image;
    }

    public String get_uid() {
        return _uid;
    }

    public void set_uid(String uid) {
        this._uid = uid;
    }

    public String get_created_at() {
        return _created_at;
    }

    public void set_created_at(String created_at) {
        this._created_at = created_at;
    }

}
