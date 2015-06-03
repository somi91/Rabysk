package com.uslive.rabyks.common;

import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by ustankovic on 03-06-15.
 */
public class Common {

    public static String hashRequest(String url, String data) {
        try {
            String salt = "registration salt";
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt.getBytes());
            byte[] hash = digest.digest((url + data).getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            Log.d("Hash failed!", e.getMessage());
            return "";
        }
    }

}
