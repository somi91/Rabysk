package com.uslive.rabyks.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by marezina on 30.6.2015.
 */
public class JsonManager {
    private JSONArray jsonArray;
    private JSONObject jsonObject;

    public JsonManager(JSONArray array){
        jsonArray = array;
    }

    public JSONObject FindObjectById(int id) {
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject json = jsonArray.getJSONObject(i);
                if(json.getInt("objectId") == id) {
                    return json;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
