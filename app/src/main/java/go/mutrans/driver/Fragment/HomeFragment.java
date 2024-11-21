package go.mutrans.driver.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import go.mutrans.driver.Item.BanklistItem;
import go.mutrans.driver.Json.GetOnRequestJson;
import go.mutrans.driver.Json.ResponseJson;
import go.mutrans.driver.Json.UpdateLocationRequestJson;
import go.mutrans.driver.Konstanta.BaseApp;
import go.mutrans.driver.Models.BankModels;
import go.mutrans.driver.Models.User;
import go.mutrans.driver.R;
import go.mutrans.driver.Utils.Api.Services.DriverService;
import go.mutrans.driver.Utils.Api.Services.ServiceGenerator;
import go.mutrans.driver.Utils.SettingPreference;
import go.mutrans.driver.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "Home";
    private Context context;
    private GoogleMap gMap;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private TextView saldo;
    private RelativeLayout rlprogress;
    private Button onoff, uangbelanja, autobid;
    private SettingPreference sp;
    private ArrayList<BankModels> mList;
    private String statusdriver, saldodriver;
    private Geocoder geocoder;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    boolean statusDriverReal = false;
    String keyLokasiDriver;
    LatLng latLngService;
    Circle userLocationAccuracyCircle;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = inflater.inflate(R.layout.fragment_home, container, false);

        context = requireActivity();

        RelativeLayout topup = getView.findViewById(R.id.topup);
        RelativeLayout withdraw = getView.findViewById(R.id.withdraw);
        RelativeLayout detail = getView.findViewById(R.id.detail);

        saldo = getView.findViewById(R.id.saldo);
        autobid = getView.findViewById(R.id.autobid);
        uangbelanja = getView.findViewById(R.id.maks);
        onoff = getView.findViewById(R.id.onoff);
        mList = new ArrayList<>();
        sp = new SettingPreference(context);
        rlprogress = getView.findViewById(R.id.rlprogress);

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, TopupSaldoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
                Toast.makeText(context, "Maaf, fitur ini belum tersedia.", Toast.LENGTH_SHORT).show();
            }
        });

        sp.updateNotif("OFF");
        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, WithdrawActivity.class);
                i.putExtra("type","withdraw");
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);*/
                Toast.makeText(context, "Maaf, fitur ini belum tersedia.", Toast.LENGTH_SHORT).show();

            }
        });

        detail.setOnClickListener(v -> {
            /*Intent i = new Intent(context, WalletActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);*/
            Toast.makeText(context, "Maaf, fitur ini belum tersedia.", Toast.LENGTH_SHORT).show();

        });

        uangbelanja.setOnClickListener(v -> dialog());


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);

        Objects.requireNonNull(mapFragment).getMapAsync(this);
        geocoder = new Geocoder(requireActivity());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(6000);
        locationRequest.setFastestInterval(6000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (sp.getSetting()[0].equals("OFF")) {
            autobid.setSelected(false);
        } else {
            autobid.setSelected(true);
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            statusdriver = bundle.getString("status");
            saldodriver = bundle.getString("saldo");
        }

        if (statusdriver.equals("1")) {
            rlprogress.setVisibility(View.GONE);
            sp.updateKerja("ON");
            onoff.setSelected(true);
            onoff.setText("ON");
            statusDriverReal = true;
        } else if (statusdriver.equals("4")) {
            rlprogress.setVisibility(View.GONE);
            onoff.setSelected(false);
            onoff.setText("OFF");
            sp.updateKerja("OFF");
            statusDriverReal = false;
        }

        onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getturnon();
            }
        });


        if (sp.getSetting()[1].isEmpty()) {
            Utility.currencyTXT(uangbelanja, "1000", context);
        } else if (sp.getSetting()[1].equals("Unlimited")) {
            uangbelanja.setText(sp.getSetting()[1]);
        } else {
            Utility.currencyTXT(uangbelanja, sp.getSetting()[1], context);
        }

        List<BankModels> items = getPeopleData(context);
        mList.addAll(items);

        return getView;
    }

    private static List<BankModels> getPeopleData(Context ctx) {
        List<BankModels> items = new ArrayList<>();
        @SuppressLint("Recycle") TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.list_maximum);
        String[] name_arr = ctx.getResources().getStringArray(R.array.list_maximum);

        for (int i = 0; i < drw_arr.length(); i++) {
            BankModels obj = new BankModels();
            obj.setText(name_arr[i]);
            items.add(obj);
        }
        return items;
    }
    Marker userLocationMarker;

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            if (gMap != null) {
                setUserLocationMarker(Objects.requireNonNull(locationResult.getLastLocation()));
            }
        }
    };

    private void setUserLocationMarker(Location lastLocation) {
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());


            latLngService = latLng;
            //use the previously created marker
            userLocationMarker.setPosition(latLng);
            userLocationMarker.setRotation(lastLocation.getBearing());

            if (statusDriverReal){
                if (keyLokasiDriver == null){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("driver", "Purbas");
                    hashMap.put("lat", latLng.latitude);
                    hashMap.put("lng", latLng.longitude);
                    hashMap.put("status", "aktif");

                    reference.child("driver_lokasi").push().setValue(hashMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null){

                                keyLokasiDriver = ref.getKey();

                            }
                        }
                    });
                }else{
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("driver", "Purbas");
                    hashMap.put("lat", latLng.latitude);
                    hashMap.put("lng", latLng.longitude);
                    hashMap.put("status", "aktif");

                    reference.child("driver_lokasi").child(keyLokasiDriver).setValue(hashMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null){
                                keyLokasiDriver = ref.getKey();
                            }
                        }
                    });
                }
            }else{
                if (keyLokasiDriver != null){
                    hapusDataLokasiDriver();
                }
            }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(lastLocation.getAccuracy());
            userLocationAccuracyCircle = gMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(lastLocation.getAccuracy());
        }
    }

    public void hapusDataLokasiDriver(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("driver_lokasi").child(keyLokasiDriver);
        databaseRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data berhasil dihapus
                        zoomToUserLocation();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gagal menghapus data, handle kesalahan di sini
                    }
                });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.clear();
        gMap.getUiSettings().setMyLocationButtonEnabled(false);
        gMap.getUiSettings().setMapToolbarEnabled(false);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        zoomToUserLocation();

    }

    private void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    startLocationUpdates();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery));
                    markerOptions.rotation(location.getBearing());
                    markerOptions.anchor((float) 0.5, (float) 0.5);
                    userLocationMarker = gMap.addMarker(markerOptions);
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
                    updateLokasi(location);
                }
            });
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
        autobid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sp.getSetting()[0].equals("OFF")) {
                    sp.updateAutoBid("ON");
                    autobid.setSelected(true);
                } else {
                    sp.updateAutoBid("OFF");
                    autobid.setSelected(false);
                }
            }
        });
        Utility.currencyTXT(saldo, saldodriver, context);
    }

    private void getturnon() {
        rlprogress.setVisibility(View.VISIBLE);
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        GetOnRequestJson param = new GetOnRequestJson();
        param.setId(loginUser.getId());
        if (statusdriver.equals("1")) {
            param.setOn(false);
        } else {
            param.setOn(true);
        }

        userService.turnon(param).enqueue(new Callback<ResponseJson>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    rlprogress.setVisibility(View.GONE);
                    statusdriver = Objects.requireNonNull(response.body()).getData();
                    if (response.body().getData().equals("1")) {
                        sp.updateKerja("ON");
                        onoff.setSelected(true);
                        onoff.setText("ON");
                        startLocationUpdates();
                    } else if (response.body().getData().equals("4")) {
                        sp.updateKerja("OFF");
                        onoff.setSelected(false);
                        onoff.setText("OFF");
                        stopLocationUpdates();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });
    }


    private void dialog() {
//        Toast.makeText(context, "Berhasil", Toast.LENGTH_SHORT).show();
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_bank);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final ImageView close = dialog.findViewById(R.id.close);
        final RecyclerView list = dialog.findViewById(R.id.recycleview);

        list.setHasFixedSize(true);
        list.setLayoutManager(new GridLayoutManager(context, 1));

        BanklistItem bankItem = new BanklistItem(context, mList, new BanklistItem.OnItemClickListener() {
            @Override
            public void onItemClick(BankModels item) {
                if (item.getText().equals("Unlimited")) {
                    uangbelanja.setText(item.getText());
                } else {
                    Utility.currencyTXT(uangbelanja, item.getText(), context);
                }
                sp.updateMaksimalBelanja(item.getText());
                dialog.dismiss();
            }
        });

        list.setAdapter(bankItem);


        close.setOnClickListener(v -> dialog.dismiss());


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void updateLokasi(Location location){
        User loginUser = BaseApp.getInstance(requireActivity()).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        UpdateLocationRequestJson param = new UpdateLocationRequestJson();

        param.setId(loginUser.getId());
        param.setLatitude(String.valueOf(location.getLatitude()));
        param.setLongitude(String.valueOf(location.getLongitude()));
        param.setBearing(String.valueOf(location.getBearing()));

        service.updatelocation(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull Response<ResponseJson> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("location", response.message());
                    stopLocationUpdates();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {

            }
        });


    }
}
