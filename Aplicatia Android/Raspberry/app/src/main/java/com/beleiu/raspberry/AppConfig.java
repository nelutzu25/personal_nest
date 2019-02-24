package com.beleiu.raspberry;

/**
 * Created by Beleiu on 02.05.2015.
 */
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://1.cateisibiu.ro/index.php";

    // Server user register url
    public static String URL_REGISTER = "http://1.cateisibiu.ro/index.php";

    public static String URL_GCM = "http://1.cateisibiu.ro/gcm_register.php";

    public static String URL_SAVE_SETTINGS = "http://1.cateisibiu.ro/save_settings.php";

    // Server url for getting the last temperature
    public static final String URL_RECENT_PARAMETERS = "http://1.cateisibiu.ro/get_most_recent_parameters.php";

    // Server url for updating the desired temperature
    public static final String URL_UPDATE_DESIRED_TEMPERATURE = "http://1.cateisibiu.ro/update_desired_temperature.php";


    public static final String URL_GET_ALL_PARAMETERS = "http://1.cateisibiu.ro/get_all_parameters.php";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
}
