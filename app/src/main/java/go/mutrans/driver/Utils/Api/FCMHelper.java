package go.mutrans.driver.Utils.Api;

import static go.mutrans.driver.Utils.Log.LOG;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import go.mutrans.driver.Json.fcm.FCMMessage;
import go.mutrans.driver.Utils.BooleanSerializerDeserializer;
import go.mutrans.driver.Utils.Log;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class FCMHelper {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "FCMHelper";
    private static final BooleanSerializerDeserializer booleanSerializerDeserializer = new BooleanSerializerDeserializer();
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            .registerTypeAdapter(Boolean.class, booleanSerializerDeserializer)
            .registerTypeAdapter(boolean.class, booleanSerializerDeserializer)
            .create();

    public static Call sendMessage(String serverKey, FCMMessage message) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if(LOG){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        OkHttpClient client = builder.build();

        String json = gson.toJson(message);
        Log.e("FCM REQUEST", json);
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url("https://fcm.googleapis.com/fcm/send")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=" + serverKey)
                .post(body)
                .build();

        return client.newCall(request);

//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//
//        if(LOG){
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder.addInterceptor(logging);
//        }
//        OkHttpClient client = builder.build();
//
//        String json = gson.toJson(message);
//        Log.e("FCM REQUEST", json);
//        RequestBody body = RequestBody.create(JSON, json);
//
//        Request request = new Request.Builder()
//                .url("https://fcm.googleapis.com/fcm/send")
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Authorization", "key=" + serverKey)
//                .post(body)
//                .build();
//
//        return client.newCall(request);
    }
}
