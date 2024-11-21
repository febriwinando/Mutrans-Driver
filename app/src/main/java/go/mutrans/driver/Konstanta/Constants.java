package go.mutrans.driver.Konstanta;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by ourdevelops Team on 10/23/2020.
 */

public class Constants {

    private static final String BASE_URL = "https://admmutrans.tebingtinggikota.go.id/";
    public static final String FCM_KEY = "AAAA9Fd5MzY:APA91bFfCY_V5jlo-8KvkBwU-io7_qa8wt1dPgk9qveXZDyalBsIK3GPW2Z63Di5trrx_qKKZBL5sU5Kf2SHNDUmEHcUDL8Jllw23aUGnTHOdEfiOAtW_zFGR9SI1n6TnHNRLx8WkPnU";
    public static final String CONNECTION = BASE_URL + "api/";
    public static final String IMAGESFITUR = BASE_URL + "images/fitur/";
    public static final String IMAGESBANK = BASE_URL + "images/bank/";
    public static final String IMAGESDRIVER = BASE_URL + "images/fotodriver/";
    public static final String IMAGESUSER = BASE_URL + "images/pelanggan/";
    public static final String IMAGESMERCHANT = BASE_URL + "images/merchant/";
    public static String CURRENCY = "";

    public static Double LATITUDE;
    public static Double LONGITUDE;
    public static String LOCATION;

    public static String TOKEN = "token";

    public static String USERID = "uid";

    public static String PREF_NAME = "pref_name";

    public static int permission_camera_code = 786;
    public static int permission_write_data = 788;
    public static int permission_Read_data = 789;
    public static int permission_Recording_audio = 790;

    public static SimpleDateFormat df =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    public static String versionname = "1.0";


}
