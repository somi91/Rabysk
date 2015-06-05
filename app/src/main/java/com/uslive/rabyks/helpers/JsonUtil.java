package com.uslive.rabyks.helpers;

import com.uslive.rabyks.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by milos on 5/31/2015.
 */
public class JsonUtil {

    public static String toJson(Message message){

        try {
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("id", message.getId());
            jsonMessage.put("user_id", message.getUser_id());
            jsonMessage.put("person_count", message.getPerson_count());
            jsonMessage.put("date_of_reservation", message.getDate_of_reservation());

            JSONObject jsonPartner = new JSONObject();
            jsonPartner.put("partner_id", message.getPartner().getPartner_id());
            jsonPartner.put("name", message.getPartner().getName());
            jsonPartner.put("number", message.getPartner().getNumber());
            jsonPartner.put("address", message.getPartner().getAddress());

            jsonMessage.put("Partner", jsonPartner);
            return jsonMessage.toString();


        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
