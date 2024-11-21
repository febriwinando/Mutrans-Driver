package go.mutrans.driver;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static go.mutrans.driver.Utils.Api.Services.MessagingService.serviceRingtone;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Objects;
import go.mutrans.driver.Json.AcceptRequestJson;
import go.mutrans.driver.Json.AcceptResponseJson;
import go.mutrans.driver.Json.fcm.FCMMessage;
import go.mutrans.driver.Konstanta.BaseApp;
import go.mutrans.driver.Konstanta.Constants;
import go.mutrans.driver.Models.OrderFCM;
import go.mutrans.driver.Models.User;
import go.mutrans.driver.Utils.Api.FCMHelper;
import go.mutrans.driver.Utils.Api.Services.DriverService;
import go.mutrans.driver.Utils.Api.Services.ServiceGenerator;
import go.mutrans.driver.Utils.SettingPreference;
import go.mutrans.driver.Utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrderActivity extends AppCompatActivity {
    TextView layanantext;
    TextView layanandesctext;
    TextView pickuptext;
    TextView destinationtext;
    TextView estimatetext;
    TextView distancetext;
    TextView costtext;
    TextView pricetext;
    TextView totaltext;
    ImageView icon;
    TextView timer;
    TextView time;
    TextView distancetextes;
    TextView costtextes;
    Button cancel;
    Button order;
    RelativeLayout rlprogress;
    LinearLayout lldestination;
    LinearLayout lldistance;

    String waktuorder,iconfitur, layanan, layanandesc, alamatasal, alamattujuan, estimasitime, hargatotal, cost, distance, idtrans, regid, orderfitur,tokenmerchant,idpelanggan,idtransmerchant;
    String wallett;
    MediaPlayer BG;
    Vibrator v;
    SettingPreference sp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        if (serviceRingtone.isPlaying()){
            serviceRingtone.stop();
        }
        layanantext = findViewById(R.id.layanan);
        layanandesctext = findViewById(R.id.layanandes);
        pickuptext = findViewById(R.id.pickUpText);
        destinationtext = findViewById(R.id.destinationText);
        estimatetext = findViewById(R.id.fitur);
        distancetext = findViewById(R.id.distance);
        costtext = findViewById(R.id.cost);
        pricetext = findViewById(R.id.price);
        totaltext = findViewById(R.id.totaltext);
        icon = findViewById(R.id.image);
        timer = findViewById(R.id.timer);
        time = findViewById(R.id.time);
        distancetextes = findViewById(R.id.distancetext);
        costtextes = findViewById(R.id.costtext);
        cancel = findViewById(R.id.cancel);
        order = findViewById(R.id.order);
        rlprogress = findViewById(R.id.rlprogress);
        lldestination = findViewById(R.id.lldestination);
        lldistance = findViewById(R.id.lldistance);

        removeNotif();
        setScreenOnFlags();
        sp = new SettingPreference(this);
        sp.updateNotif("ON");
        Intent intent = getIntent();
        iconfitur = intent.getStringExtra("icon");
        layanan = intent.getStringExtra("layanan");
        layanandesc = intent.getStringExtra("layanandesc");
        alamatasal = intent.getStringExtra("alamat_asal");
        alamattujuan = intent.getStringExtra("alamat_tujuan");
        estimasitime = intent.getStringExtra("estimasi_time");
        hargatotal = intent.getStringExtra("harga");
        cost = intent.getStringExtra("biaya");
        distance = intent.getStringExtra("distance");
        idtrans = intent.getStringExtra("id_transaksi");
        regid = intent.getStringExtra("reg_id");
        wallett = intent.getStringExtra("pakai_wallet");
        orderfitur = intent.getStringExtra("order_fitur");
        tokenmerchant = intent.getStringExtra("token_merchant");
        idpelanggan = intent.getStringExtra("id_pelanggan");
        idtransmerchant = intent.getStringExtra("id_trans_merchant");
        waktuorder = intent.getStringExtra("waktu_order");
        playSound();
        if (orderfitur.equalsIgnoreCase("3")) {
            lldestination.setVisibility(View.GONE);
            lldistance.setVisibility(View.GONE);

        }
        if (orderfitur.equalsIgnoreCase("4")) {

            estimatetext.setText(estimasitime);
            time.setText("Merchant");
            distancetextes.setText("Delivery Fee");
            costtextes.setText("Order Cost");
            Utility.currencyTXT(distancetext, distance, this);
            Utility.currencyTXT(costtext, cost, this);
        } else {

            estimatetext.setText(estimasitime);
            distancetext.setText(distance);
            costtext.setText(cost);
        }
        layanantext.setText(layanan);
        layanandesctext.setText(layanandesc);
        pickuptext.setText(alamatasal);
        destinationtext.setText(alamattujuan);
        Utility.currencyTXT(pricetext, hargatotal, this);
        if (wallett.equalsIgnoreCase("true")) {
            totaltext.setText("Total (WALLET)");
        } else {
            totaltext.setText("Total (CASH)");
        }


        Glide.with(this)
                .load(Constants.IMAGESFITUR + iconfitur)
                .placeholder(R.drawable.logo)
                .into(icon);

        timerplay.start();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BG.isPlaying()) {
                    BG.stop();
                    v.cancel();
                }
                timerplay.cancel();
                Intent toOrder = new Intent(NewOrderActivity.this, MainActivity.class);
                toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toOrder);

            }
        });

        if (new SettingPreference(this).getSetting()[0].equals("OFF")) {
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getaccept();

                }
            });
        } else {
            getaccept();
        }


    }

    CountDownTimer timerplay = new CountDownTimer(20000, 1000) {

        @SuppressLint("SetTextI18n")
        public void onTick(long millisUntilFinished) {
            timer.setText("" + millisUntilFinished / 1000);
        }


        public void onFinish() {
            if (BG.isPlaying()) {
                BG.stop();
                v.cancel();
            }
            timer.setText("0");
            Intent toOrder = new Intent(NewOrderActivity.this, MainActivity.class);
            toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(toOrder);
        }
    }.start();


    private void playSound() {
        v = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 700};
        Objects.requireNonNull(v).vibrate(pattern, 0);

        try {
            Uri notification = RingtoneManager.getDefaultUri(R.raw.notification);
            BG = MediaPlayer.create(getBaseContext(), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BG.setLooping(true);
        BG.setVolume(100, 100);
        BG.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getaccept() {
        if (BG.isPlaying()) {
            BG.stop();
            v.cancel();
        }
        timerplay.cancel();
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.accept(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AcceptResponseJson> call, @NonNull final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {
                    sp.updateNotif("OFF");
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "2";
                        if (orderfitur.equalsIgnoreCase("4")) {
                            orderfcm.desc = "the driver is buying an order";
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = "INV-"+idtrans+idtransmerchant;
                            orderfcm.ordertime = waktuorder;
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.ordertime = waktuorder;
                            orderfcm.desc = "Driver menuju lokasi";
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = hargatotal;
                            orderfcm.desc = getString(R.string.notification_start);
                        }

                        sendMessageToDriver(regid, orderfcm);
                    } else {
                        sp.updateNotif("OFF");
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(NewOrderActivity.this, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptResponseJson> call, @NonNull Throwable t) {
                Toast.makeText(NewOrderActivity.this, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
                sp.updateNotif("OFF");
                rlprogress.setVisibility(View.GONE);
                Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
    }

    private void sendMessageToDriver(final String regIDTujuan, final OrderFCM response) {

//        DatabaseReference token = FirebaseDatabase.getInstance().getReference("Tokens");
//        Query query = token.orderByKey().equalTo(receiver);

        final FCMMessage message = new FCMMessage();
//        message.setTo("dQ716_B3T1CRbmw-uVE96j:APA91bFx0P--YFiIY7YFrQgKMgSqXAzyPPO8oGeR51wLUFiS29fx1VbVgFb5Og3wrWeIF_4CbtT7xFfaZm-mS4KZTLPHHqFVTV6cIKpSkRmxCMSYnDYmYTXxYKRDeIVESrI4PzsxSZFI");

        message.setTo(regIDTujuan);
        message.setData(response);

        FCMHelper.sendMessage(NewOrderActivity.this.getResources().getString(R.string.fcm_key), message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) {
                Log.e("REQUEST TO DRIVER", response.isSuccessful()+" \n "+message.getData().toString());
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void removeNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).cancel(0);
    }

    private void setScreenOnFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            Objects.requireNonNull(keyguardManager).requestDismissKeyguard(this, null);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
    }
}
