package go.mutrans.driver.Models;


import static go.mutrans.driver.Json.fcm.FCMType.OTHER;

import java.io.Serializable;

/**
 * Created by Ourdevelops Team on 19/10/2019.
 */
public class Notif implements Serializable{
    public int type = OTHER;
    public String title;
    public String message;
}
