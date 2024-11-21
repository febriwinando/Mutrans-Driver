package go.mutrans.driver.Fragment;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import go.mutrans.driver.ChatActivity;
import go.mutrans.driver.Directions.Directions;
import go.mutrans.driver.Directions.Route;
import go.mutrans.driver.Item.ItemPesananItem;
import go.mutrans.driver.Json.AcceptRequestJson;
import go.mutrans.driver.Json.AcceptResponseJson;
import go.mutrans.driver.Json.DetailRequestJson;
import go.mutrans.driver.Json.DetailTransResponseJson;
import go.mutrans.driver.Json.ResponseJson;
import go.mutrans.driver.Json.VerifyRequestJson;
import go.mutrans.driver.Json.fcm.FCMMessage;
import go.mutrans.driver.Konstanta.BaseApp;
import go.mutrans.driver.Konstanta.Constants;
import go.mutrans.driver.MainActivity;
import go.mutrans.driver.Models.OrderFCM;
import go.mutrans.driver.Models.PelangganModel;
import go.mutrans.driver.Models.TransaksiModel;
import go.mutrans.driver.Models.User;
import go.mutrans.driver.R;
import go.mutrans.driver.Utils.Api.FCMHelper;
import go.mutrans.driver.Utils.Api.MapDirectionAPI;
import go.mutrans.driver.Utils.Api.Services.DriverService;
import go.mutrans.driver.Utils.Api.Services.ServiceGenerator;
import go.mutrans.driver.Utils.Log;
import go.mutrans.driver.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {


    private Context context;
    private GoogleMap gMap;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final int REQUEST_PERMISSION_CALL = 992;
    private GoogleApiClient googleApiClient;
    private LatLng pickUpLatLng;
    private LatLng destinationLatLng;
    private Polyline directionLine;
    private Marker pickUpMarker;
    private Marker destinationMarker;
    private String idtrans, idpelanggan, response, fitur, onsubmit;

    LinearLayout bottomsheet;
    TextView layanan;
    TextView layanandesk;
    TextView verify;
    TextView namamerchant;
    LinearLayout llchat;
    CircleImageView foto;
    TextView pickUpText;
    TextView destinationText;
    TextView fiturtext;
    TextView distanceText;
    TextView priceText;
    RelativeLayout rlprogress;
    TextView textprogress;
    TextView cost;
    TextView deliveryfee;
    ImageView phone;

    ImageView chat;
    ImageView phonemerchant;
    ImageView chatmerchant;
    LinearLayout lldestination;
    LinearLayout llorderdetail;
    LinearLayout lldistance;
    LinearLayout lldetailsend;
    TextView produk;
    TextView sendername;
    TextView receivername;
    Button senderphone;
    Button receiverphone;
    ShimmerFrameLayout shimmerlayanan;
    ShimmerFrameLayout shimmerpickup;
    ShimmerFrameLayout shimmerdestination;
    ShimmerFrameLayout shimmerfitur;
    ShimmerFrameLayout shimmerdistance;
    ShimmerFrameLayout shimmerprice;
    Button submit;
    LinearLayout llmerchantdetail;
    LinearLayout llmerchantinfo;
    LinearLayout llbutton;
    RecyclerView rvmerchantnear;
    private ItemPesananItem itemPesananItem;
    private TextView totaltext;
    private String type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        context = getContext();


        bottomsheet = view.findViewById(R.id.bottom_sheet);
        layanan = view.findViewById(R.id.layanan);
        layanandesk = view.findViewById(R.id.layanandes);
        verify = view.findViewById(R.id.verifycation);
        namamerchant = view.findViewById(R.id.namamerchant);
        llchat = view.findViewById(R.id.llchat);
        foto = view.findViewById(R.id.background);
        pickUpText = view.findViewById(R.id.pickUpText);
        destinationText = view.findViewById(R.id.destinationText);
        fiturtext = view.findViewById(R.id.fitur);
        distanceText = view.findViewById(R.id.distance);
        priceText = view.findViewById(R.id.price);
        rlprogress = view.findViewById(R.id.rlprogress);
        textprogress = view.findViewById(R.id.textprogress);
        cost = view.findViewById(R.id.cost);
        deliveryfee = view.findViewById(R.id.deliveryfee);
        phone = view.findViewById(R.id.phonenumber);
        chat = view.findViewById(R.id.chat);
        phonemerchant = view.findViewById(R.id.phonemerchant);
        chatmerchant = view.findViewById(R.id.chatmerchant);
        lldestination = view.findViewById(R.id.lldestination);
        llorderdetail = view.findViewById(R.id.orderdetail);
        lldistance = view.findViewById(R.id.lldistance);
        lldetailsend = view.findViewById(R.id.senddetail);
        produk = view.findViewById(R.id.produk);
        sendername = view.findViewById(R.id.sendername);
        receivername = view.findViewById(R.id.receivername);
        senderphone = view.findViewById(R.id.senderphone);
        receiverphone = view.findViewById(R.id.receiverphone);
        shimmerlayanan = view.findViewById(R.id.shimmerlayanan);
        shimmerpickup = view.findViewById(R.id.shimmerpickup);
        shimmerdestination = view.findViewById(R.id.shimmerdestination);
        shimmerfitur = view.findViewById(R.id.shimmerfitur);
        shimmerdistance = view.findViewById(R.id.shimmerdistance);
        shimmerprice = view.findViewById(R.id.shimmerprice);
        submit = view.findViewById(R.id.order);
        llmerchantdetail = view.findViewById(R.id.merchantdetail);
        llmerchantinfo = view.findViewById(R.id.merchantinfo);
        llbutton = view.findViewById(R.id.llbutton);
        rvmerchantnear =  view.findViewById(R.id.merchantnear);

//        ButterKnife.bind(this, getView);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        totaltext = view.findViewById(R.id.totaltext);
        fitur = "0";
        type = "0";
        Bundle bundle = getArguments();
        if (bundle != null) {
            idpelanggan = bundle.getString("id_pelanggan");
            idtrans = bundle.getString("id_transaksi");
            response = bundle.getString("response");
        }
        shimmerload();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        switch (response) {
            case "2":
                onsubmit = "2";
                llchat.setVisibility(View.VISIBLE);
                break;
            case "3":
                onsubmit = "3";
                llchat.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                verify.setVisibility(View.GONE);
                submit.setText("finish");
                break;
            case "4":
                llchat.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                layanandesk.setText(getString(R.string.notification_finish));
                break;
            case "5":
                llchat.setVisibility(View.GONE);
                layanandesk.setText(getString(R.string.notification_cancel));
                break;
        }
        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        rlprogress.setVisibility(View.GONE);
        textprogress.setText(getString(R.string.waiting_pleaseWait));
        return view;
    }


    private void getData(final String idtrans, final String idpelanggan) {
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdPelanggan(idpelanggan);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<DetailTransResponseJson> call, @NonNull Response<DetailTransResponseJson> responsedata) {
                if (responsedata.isSuccessful()) {
                    shimmertutup();
                    Log.e("", String.valueOf(Objects.requireNonNull(responsedata.body()).getData().get(0)));
                    final TransaksiModel transaksi = responsedata.body().getData().get(0);
                    final PelangganModel pelanggan = responsedata.body().getPelanggan().get(0);
                    type = transaksi.getHome();

                    if (transaksi.isPakaiWallet()) {
                        totaltext.setText("Total (Wallet)");
                    } else {
                        totaltext.setText("Total (Cash)");
                    }

                    if (onsubmit.equals("2")) {

                        if (transaksi.getHome().equals("4")) {
                            layanandesk.setText("Sedang membeli pesanan");
                            submit.setText("Mengantar pesanan");
                            verify.setVisibility(View.VISIBLE);
                        } else {
//                            layanandesk.setText(getString(R.string.notification_accept));
                        }
                        submit.setVisibility(View.VISIBLE);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (transaksi.getHome().equals("4")) {

                                    if (verify.getText().toString().isEmpty()) {
                                        Toast.makeText(context, "Silahkan masukkan kode verifikasi!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                                        String finalDate = timeFormat.format(transaksi.getWaktuOrder());
                                        rlprogress.setVisibility(View.VISIBLE);
                                        verify(verify.getText().toString(), pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, finalDate);
                                    }
                                }
                                else {

                                    start(pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, String.valueOf(transaksi.getWaktuOrder()));
                                }

                            }
                        });
                    }
                    else if (onsubmit.equals("3")) {

                        if (transaksi.getHome().equals("4")) {
                            layanandesk.setText("Mengantar pesanan");
                        } else {
                            layanandesk.setText(getString(R.string.notification_start));
                        }

                        verify.setVisibility(View.GONE);
                        submit.setText("Finish");
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish(pelanggan, transaksi.token_merchant);
                            }
                        });
                    }

                    fitur = transaksi.getOrderFitur();

                    if (transaksi.getHome().equals("3")) {
                        lldestination.setVisibility(View.GONE);
                        lldistance.setVisibility(View.GONE);
                        fiturtext.setText(transaksi.getEstimasi());
                    } else if (transaksi.getHome().equals("4")) {
                        llorderdetail.setVisibility(View.VISIBLE);
                        llmerchantdetail.setVisibility(View.VISIBLE);
                        llmerchantinfo.setVisibility(View.VISIBLE);
                        Utility.currencyTXT(deliveryfee, String.valueOf(transaksi.getHarga()), context);
                        Utility.currencyTXT(cost, String.valueOf(transaksi.getTotal_biaya()), context);
                        namamerchant.setText(transaksi.getNama_merchant());
                        itemPesananItem = new ItemPesananItem(responsedata.body().getItem(), R.layout.item_pesanan);
                        rvmerchantnear.setAdapter(itemPesananItem);

                        phonemerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Customer");
                                alertDialogBuilder.setMessage("You want to call Merchant (+" + transaksi.getTeleponmerchant() + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:+" + transaksi.getTeleponmerchant()));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        chatmerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("senderid", loginUser.getId());
                                intent.putExtra("receiverid", transaksi.getId_merchant());
                                intent.putExtra("tokendriver", loginUser.getToken());
                                intent.putExtra("tokenku", transaksi.getToken_merchant());
                                intent.putExtra("name", transaksi.getNama_merchant());
                                intent.putExtra("pic", Constants.IMAGESMERCHANT + transaksi.getFoto_merchant());
                                startActivity(intent);
                            }
                        });

                    } else if (fitur.equalsIgnoreCase("5")) {
                        requestRoute();
                        lldetailsend.setVisibility(View.VISIBLE);
                        produk.setText(transaksi.getNamaBarang());
                        sendername.setText(transaksi.namaPengirim);
                        receivername.setText(transaksi.namaPenerima);

                        senderphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPengirim() + "(" + transaksi.teleponPengirim + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPengirim));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        receiverphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPenerima() + "(" + transaksi.teleponPenerima + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPenerima));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                    }
                    pickUpLatLng = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                    destinationLatLng = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());
                    if (pickUpMarker != null) pickUpMarker.remove();
                    pickUpMarker = gMap.addMarker(new MarkerOptions()
                            .position(pickUpLatLng)
                            .title("Pick Up")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup)));


                    if (destinationMarker != null) destinationMarker.remove();
                    destinationMarker = gMap.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title("Destination")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
                    updateLastLocation();
                    parsedata(transaksi, pelanggan);


                }
            }

            @Override
            public void onFailure(@NonNull retrofit2.Call<DetailTransResponseJson> call, @NonNull Throwable t) {

            }
        });


    }

    private void shimmerload() {
        shimmerlayanan.startShimmer();
        shimmerpickup.startShimmer();
        shimmerdestination.startShimmer();
        shimmerfitur.startShimmer();
        shimmerdistance.startShimmer();
        shimmerprice.startShimmer();

        layanan.setVisibility(View.GONE);
        layanandesk.setVisibility(View.GONE);
        pickUpText.setVisibility(View.GONE);
        destinationText.setVisibility(View.GONE);
        fiturtext.setVisibility(View.GONE);
        priceText.setVisibility(View.GONE);
    }

    private void shimmertutup() {
        shimmerlayanan.stopShimmer();
        shimmerpickup.stopShimmer();
        shimmerdestination.stopShimmer();
        shimmerfitur.stopShimmer();
        shimmerdistance.stopShimmer();
        shimmerprice.stopShimmer();

        shimmerlayanan.setVisibility(View.GONE);
        shimmerpickup.setVisibility(View.GONE);
        shimmerdestination.setVisibility(View.GONE);
        shimmerfitur.setVisibility(View.GONE);
        shimmerdistance.setVisibility(View.GONE);
        shimmerprice.setVisibility(View.GONE);

        layanan.setVisibility(View.VISIBLE);
        layanandesk.setVisibility(View.VISIBLE);
        pickUpText.setVisibility(View.VISIBLE);
        destinationText.setVisibility(View.VISIBLE);
        distanceText.setVisibility(View.VISIBLE);
        fiturtext.setVisibility(View.VISIBLE);
        priceText.setVisibility(View.VISIBLE);
    }

    private void parsedata(TransaksiModel request, final PelangganModel pelanggan) {
        requestRoute();
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        rlprogress.setVisibility(View.GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());

        Glide.with(context)
                .load(Constants.IMAGESUSER + pelanggan.getFoto())
                .placeholder(R.drawable.image_placeholder)
                .into(foto);


        layanan.setText(pelanggan.getFullnama());
        pickUpText.setText(request.getAlamatAsal());
        destinationText.setText(request.getAlamatTujuan());
        if (type.equals("4")) {
            double totalbiaya = Double.parseDouble(request.getTotal_biaya());
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga() + totalbiaya), context);
        } else {
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga()), context);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                alertDialogBuilder.setTitle("Call Customer");
                alertDialogBuilder.setMessage("You want to call Costumer (+" + pelanggan.getNoTelepon() + ")?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                    return;
                                }

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:+" + pelanggan.getNoTelepon()));
                                startActivity(callIntent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("senderid", loginUser.getId());
                intent.putExtra("receiverid", pelanggan.getId());
                intent.putExtra("tokendriver", loginUser.getToken());
                intent.putExtra("tokenku", pelanggan.getToken());
                intent.putExtra("name", pelanggan.getFullnama());
                intent.putExtra("pic", Constants.IMAGESUSER + pelanggan.getFoto());
                startActivity(intent);
            }
        });
    }

    private void updateLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        gMap.setMyLocationEnabled(true);

        if (pickUpLatLng != null) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    pickUpLatLng, 15f)
            );

            gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
        }
    }

    private okhttp3.Callback updateRouteCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

        }

        @Override
        public void onResponse(@NonNull okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
                final String json = Objects.requireNonNull(response.body()).string();
                final long distance = MapDirectionAPI.getDistance(context, json);
                final String time = MapDirectionAPI.getTimeDistance(context, json);
                if (distance >= 0) {
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateLineDestination(json);
                            float km = ((float) (distance)) / 1000f;
                            String format = String.format(Locale.US, "%.1f", km);
                            distanceText.setText(format);
                            fiturtext.setText(time);
                        }
                    });
                }
            }
        }
    };

    private void requestRoute() {
        if (pickUpLatLng != null && destinationLatLng != null) {
            MapDirectionAPI.getDirection(pickUpLatLng, destinationLatLng, requireActivity()).enqueue(updateRouteCallback);
        }
    }

    private void updateLineDestination(String json) {
        Directions directions = new Directions(context);
        try {
            List<Route> routes = directions.parse(json);

            if (directionLine != null) directionLine.remove();
            if (routes.size() > 0) {
                directionLine = gMap.addPolyline((new PolylineOptions())
                        .addAll(routes.get(0).getOverviewPolyLine())
                        .color(ContextCompat.getColor(context, R.color.colorgradient))
                        .width(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.style_json));

            if (!success) {
                android.util.Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            android.util.Log.e("", "Can't find style. Error: ", e);
        }
        getData(idtrans, idpelanggan);
    }

    private void start(final PelangganModel pelanggan, final String tokenmerchant, final String idtransmerchant, final String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.startrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        onsubmit = "3";
                        getData(idtrans, idpelanggan);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "3";
                        if (type.equals("4")) {
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = "INV-" + idtrans + idtransmerchant;
                            orderfcm.ordertime = waktuorder;
                            orderfcm.desc = "driver delivers the order";
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.desc = getString(R.string.notification_start);
                        }
                        sendMessageToDriver(pelanggan.getToken(), orderfcm);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void verify(String verificode, final PelangganModel pelanggan, final String tokenmerchant, final String idtransmerchant, final String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        VerifyRequestJson param = new VerifyRequestJson();
        param.setId(loginUser.getNoTelepon());
        param.setIdtrans(idtrans);
        param.setVerifycode(verificode);
        userService.verifycode(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<ResponseJson> call, @NonNull final Response<ResponseJson> response) {
                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {

                        start(pelanggan, tokenmerchant, idtransmerchant, waktuorder);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Toast.makeText(context, "verifycode not correct!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void finish(final PelangganModel pelanggan, final String tokenmerchant) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.finishrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {

                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "4";
                        orderfcm.desc = getString(R.string.notification_finish);
                        if (type.equals("4")) {
                            sendMessageToDriver(tokenmerchant, orderfcm);
                            sendMessageToDriver(pelanggan.getToken(), orderfcm);
                        } else {
                            sendMessageToDriver(pelanggan.getToken(), orderfcm);
                        }

                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void sendMessageToDriver(final String regIDTujuan, final OrderFCM response) {
        Toast.makeText(context, ""+regIDTujuan, Toast.LENGTH_SHORT).show();
        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(response);

        FCMHelper.sendMessage(requireActivity().getResources().getString(R.string.fcm_key), message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                android.util.Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

}