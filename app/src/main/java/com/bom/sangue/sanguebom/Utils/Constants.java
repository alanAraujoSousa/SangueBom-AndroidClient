package com.bom.sangue.sanguebom.Utils;

/**
 * Created by alan on 29/11/15.
 */
public class Constants {

    // Use this for ADB
//    public static final String HOST = "10.0.2.2";

    // Use this for Genymotion
    public static final String HOST = "10.0.3.2";
    public static final String PORT = "8000";
    public static final String URL_SIGNUP = "http://" + HOST+":"+PORT+"/engine/users/";
    public static final String URL_SIGN = "http://"+HOST+":"+PORT+"/api-auth-token/";
    public static final String URL_LAST_DONATION = "http://" + HOST+":"+PORT+"/engine/users/{id}/last_donation/";
    public static final String URL_LIST_PATIENT = "http://" + HOST+":"+PORT+"/engine/patients/";

    // FIXME This is for prototipal purposes, change it for a regular user.
    public static final String ROOT_TOKEN = "79f5aa62a9ec986835d4b6890b6ec1c43dd61b59";
}
