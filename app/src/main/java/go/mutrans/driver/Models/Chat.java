package go.mutrans.driver.Models;


import static go.mutrans.driver.Json.fcm.FCMType.CHAT;

import java.io.Serializable;

/**
 * Created by Ourdevelops Team on 19/10/2019.
 */
public class Chat implements Serializable{
    public int type = CHAT;
    public String senderid;
    public String receiverid;
    public String name;
    public String pic;
    public String tokendriver;
    public String tokenuser;
    public String message;
    public String isdriver;
}
