package go.mutrans.driver.Utils.Api;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

//import id.mutrans.driver.activity.MainActivity;
//import id.mutrans.driver.gmap.GMapDirection;
//import id.mutrans.driver.gmap.directions.Directions;
//import id.mutrans.driver.gmap.directions.Leg;
//import id.mutrans.driver.gmap.directions.Route;
//import id.mutrans.driver.gmap.directions.Step;
import go.mutrans.driver.Directions.Directions;
import go.mutrans.driver.Directions.Leg;
import go.mutrans.driver.Directions.Route;
import go.mutrans.driver.Directions.Step;
import go.mutrans.driver.R;
import go.mutrans.driver.Utils.GMapDirection;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Ourdevelops Team on 10/16/2019.
 */

public class MapDirectionAPI {

    public static Call getDirection(LatLng pickUp, LatLng destination, Activity context) {
        OkHttpClient client = new OkHttpClient();
        GMapDirection gMapDirection = new GMapDirection();

        Request request = new Request.Builder()
                .url(gMapDirection.getUrl(pickUp, destination, GMapDirection.MODE_DRIVING, false, context))
                .build();

        return client.newCall(request);
    }

    public static Call getAddress(LatLng address, Activity context) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/geocode/json?latlng="+address.latitude+","+address.longitude+"&key="+ context.getResources().getString(R.string.google_maps_key))
                .build();

        return client.newCall(request);
    }

    public static Call getDirectionVia(Activity context, LatLng pickUp, LatLng... destination) {
        OkHttpClient client = new OkHttpClient();
        GMapDirection gMapDirection = new GMapDirection();

        Request request = new Request.Builder()
                .url(gMapDirection.getUrlVia(context, GMapDirection.MODE_DRIVING, false, pickUp, destination))
                .build();

        return client.newCall(request);
    }





    public static long getDistance(Context context, String json) {
        long dist = 0;
        if (json != null) {
            Directions directions = new Directions(context);
            List<Route> routes;

            try {
                routes = directions.parse(json);
            } catch (Exception e) {
                e.printStackTrace();
                return -1L;
            }

            for (Route route : routes) {
                for (Leg leg : route.getLegs()) {
                    for (Step step : leg.getSteps()) {
                        dist += step.getDistance().getValue();
                    }
                }
            }

            if (routes.size() == 0) return -1L;

        }
        return dist;
    }

    public static String getTimeDistance(Context context, String json) {
        String time = "0 mins";
        if (json != null) {
            Directions directions = new Directions(context);
            List<Route> routes = null;

            try {
                routes = directions.parse(json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Route route : routes) {
                for (Leg leg : route.getLegs()) {
                        time = leg.getDuration().getText();

                }
            }



        }
        return time;
    }


}
