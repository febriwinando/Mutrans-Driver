package go.mutrans.driver.Utils.Api.Services;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

import go.mutrans.driver.ChatActivity;
import go.mutrans.driver.Konstanta.BaseApp;
import go.mutrans.driver.MainActivity;
import go.mutrans.driver.Models.User;
import go.mutrans.driver.NewOrderActivity;
import go.mutrans.driver.R;
import go.mutrans.driver.Starting.SplashActivity;
import go.mutrans.driver.Utils.SettingPreference;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MessagingService extends FirebaseMessagingService {
    Intent intent;
    public static final String BROADCAST_ACTION = "go.mutrans.driver";
    public static final String BROADCAST_ORDER = "order";
    Intent intentOrder;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        intentOrder = new Intent(BROADCAST_ORDER);

    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("NOTOFIKASIIIs", remoteMessage.getData().toString());

        if (!remoteMessage.getData().isEmpty()) {
            messageHandler(remoteMessage);
        }
    }

    private void messageHandler(RemoteMessage remoteMessage){
        User user = BaseApp.getInstance(this).getLoginUser();
        SettingPreference sp = new SettingPreference(this);
        if (Objects.requireNonNull(remoteMessage.getData().get("type")).equals("1")) {

            if (user != null) {

                String resp = remoteMessage.getData().get("response");
                if (resp == null ) {
                    if (sp.getSetting()[1].equals("Unlimited")) {
                        if (sp.getSetting()[2].equals("ON") && sp.getSetting()[3].equals("OFF")) {
                            notification(remoteMessage);
                        }
                    } else {
                        double uangbelanja = Double.parseDouble(sp.getSetting()[1]);
                        double harga = Double.parseDouble(Objects.requireNonNull(remoteMessage.getData().get("harga")));
                        if (uangbelanja > harga && sp.getSetting()[2].equals("ON") && sp.getSetting()[3].equals("OFF")) {
                            notification(remoteMessage);
                        }
                    }
                } else {
                    playSound();
                    intentCancel();
                }
            }
        } else if (Objects.requireNonNull(remoteMessage.getData().get("type")).equals("3")) {

            if (user != null) {
                Log.d("NOTOFIKASIIIs", "3");

                otherHandler(remoteMessage);
            }
        } else if (Objects.requireNonNull(remoteMessage.getData().get("type")).equals("4")) {
            Log.d("NOTOFIKASIIIs", "4");

                otherHandler2(remoteMessage);
        } else if (Objects.requireNonNull(remoteMessage.getData().get("type")).equals("2")) {

            if (user != null) {
                Log.d("NOTOFIKASIIIs", "2");

                chat(remoteMessage);
            }
        }
    }

    private void intentCancel() {
        Intent toMain = new Intent(getBaseContext(), MainActivity.class);
        toMain.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toMain);
    }

    private void otherHandler2(RemoteMessage remoteMessage) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "WhatsApp");
        Intent intent1 = new Intent(getApplicationContext(), SplashActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title"));
        bigTextStyle.bigText(remoteMessage.getData().get("message"));

        mBuilder.setContentIntent(pIntent1);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(remoteMessage.getData().get("title"));
        mBuilder.setContentText(remoteMessage.getData().get("message"));
        mBuilder.setStyle(bigTextStyle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "customer";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel customer",
                    NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Objects.requireNonNull(notificationManager).notify(0, mBuilder.build());
    }

    private void otherHandler(RemoteMessage remoteMessage) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "WhatsApp");
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title"));
        bigTextStyle.bigText(remoteMessage.getData().get("message"));

        mBuilder.setContentIntent(pIntent1);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(remoteMessage.getData().get("title"));
        mBuilder.setContentText(remoteMessage.getData().get("message"));
        mBuilder.setStyle(bigTextStyle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "customer";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel customer",
                    NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Objects.requireNonNull(notificationManager).notify(0, mBuilder.build());
    }

    private void chat(RemoteMessage remoteMessage) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "WhatsApp");
        Intent intent1 = new Intent(getApplicationContext(), ChatActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("senderid", remoteMessage.getData().get("receiverid"));
        intent1.putExtra("receiverid", remoteMessage.getData().get("senderid"));
        intent1.putExtra("name", remoteMessage.getData().get("name"));
        intent1.putExtra("tokenku", remoteMessage.getData().get("tokendriver"));
        intent1.putExtra("tokendriver", remoteMessage.getData().get("tokenuser"));
        intent1.putExtra("pic", remoteMessage.getData().get("pic"));
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("name"));
        bigTextStyle.bigText(remoteMessage.getData().get("message"));

        mBuilder.setContentIntent(pIntent1);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(remoteMessage.getData().get("name"));
        mBuilder.setContentText(remoteMessage.getData().get("message"));
        mBuilder.setStyle(bigTextStyle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "customer";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel customer",
                    NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Objects.requireNonNull(notificationManager).notify(0, mBuilder.build());
    }

    private void notification(RemoteMessage remoteMessage) {
        playSound();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent toOrder = new Intent(getApplicationContext(), NewOrderActivity.class);

        toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        toOrder.putExtra("id_transaksi", remoteMessage.getData().get("id_transaksi"));
        toOrder.putExtra("icon", remoteMessage.getData().get("icon"));
        toOrder.putExtra("layanan", remoteMessage.getData().get("layanan"));
        toOrder.putExtra("layanandesc", remoteMessage.getData().get("layanandesc"));
        toOrder.putExtra("alamat_asal", remoteMessage.getData().get("alamat_asal"));
        toOrder.putExtra("alamat_tujuan", remoteMessage.getData().get("alamat_tujuan"));
        toOrder.putExtra("estimasi_time", remoteMessage.getData().get("estimasi_time"));
        toOrder.putExtra("harga", remoteMessage.getData().get("harga"));
        toOrder.putExtra("biaya", remoteMessage.getData().get("biaya"));
        toOrder.putExtra("distance", remoteMessage.getData().get("distance"));
        toOrder.putExtra("pakai_wallet", remoteMessage.getData().get("pakai_wallet"));
        toOrder.putExtra("reg_id", remoteMessage.getData().get("reg_id_pelanggan"));
        toOrder.putExtra("order_fitur", remoteMessage.getData().get("order_fitur"));
        toOrder.putExtra("token_merchant", remoteMessage.getData().get("token_merchant"));
        toOrder.putExtra("id_pelanggan", remoteMessage.getData().get("id_pelanggan"));
        toOrder.putExtra("id_trans_merchant", remoteMessage.getData().get("id_trans_merchant"));
        toOrder.putExtra("waktu_order", remoteMessage.getData().get("waktu_order"));
        toOrder.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), toOrder, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("layanan"));
        bigTextStyle.bigText(remoteMessage.getData().get("biaya"));
        mBuilder.setContentIntent(pIntent1);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(remoteMessage.getData().get("layanan"));
        mBuilder.setContentText(remoteMessage.getData().get("biaya"));
        mBuilder.setStyle(bigTextStyle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "customer";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel customer",
                    NotificationManager.IMPORTANCE_HIGH);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Objects.requireNonNull(notificationManager).notify(0, mBuilder.build());

//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
//        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("layanan"));
//        bigTextStyle.bigText(remoteMessage.getData().get("harga"));
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "WhatsApp");
//        mBuilder.setSmallIcon(R.drawable.logo);
//        mBuilder.setContentTitle(remoteMessage.getData().get("layanan"));
//        mBuilder.setContentText(remoteMessage.getData().get("harga"));
//        mBuilder.setStyle(bigTextStyle);
//        mBuilder.setPriority(Notification.PRIORITY_MAX);
//        mBuilder.setAutoCancel(true);
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String channelId = "customer";
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel customer",
//                    NotificationManager.IMPORTANCE_HIGH);
//            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
//            mBuilder.setChannelId(channelId);
//        }
//
//        Objects.requireNonNull(notificationManager).notify(0, mBuilder.build());
//
//        Intent toOrder = new Intent();
//        toOrder.setClass(this, NewOrderActivity.class);
//        toOrder.putExtra("id_transaksi", remoteMessage.getData().get("id_transaksi"));
//        toOrder.putExtra("icon", remoteMessage.getData().get("icon"));
//        toOrder.putExtra("layanan", remoteMessage.getData().get("layanan"));
//        toOrder.putExtra("layanandesc", remoteMessage.getData().get("layanandesc"));
//        toOrder.putExtra("alamat_asal", remoteMessage.getData().get("alamat_asal"));
//        toOrder.putExtra("alamat_tujuan", remoteMessage.getData().get("alamat_tujuan"));
//        toOrder.putExtra("estimasi_time", remoteMessage.getData().get("estimasi_time"));
//        toOrder.putExtra("harga", remoteMessage.getData().get("harga"));
//        toOrder.putExtra("biaya", remoteMessage.getData().get("biaya"));
//        toOrder.putExtra("distance", remoteMessage.getData().get("distance"));
//        toOrder.putExtra("pakai_wallet", remoteMessage.getData().get("pakai_wallet"));
//        toOrder.putExtra("reg_id", remoteMessage.getData().get("reg_id_pelanggan"));
//        toOrder.putExtra("order_fitur", remoteMessage.getData().get("order_fitur"));
//        toOrder.putExtra("token_merchant", remoteMessage.getData().get("token_merchant"));
//        toOrder.putExtra("id_pelanggan", remoteMessage.getData().get("id_pelanggan"));
//        toOrder.putExtra("id_trans_merchant", remoteMessage.getData().get("id_trans_merchant"));
//        toOrder.putExtra("waktu_order", remoteMessage.getData().get("waktu_order"));
//        toOrder.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(toOrder);
//        startService(toOrder);

//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

//        PendingIntent pIntent1 = PendingIntent.getActivity(getBaseContext(), (int) System.currentTimeMillis(), toOrder, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
//        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
//        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title"));
//        bigTextStyle.bigText(remoteMessage.getData().get("message"));
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "WhatsApp")
//                .setContentIntent(pIntent1)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle(remoteMessage.getData().get("layanan"))
//                .setContentText(remoteMessage.getData().get("order_fitur"))
//                .setStyle(bigTextStyle)
//                .setSound(RingtoneManager.getDefaultUri(R.raw.notification))
//                .setFullScreenIntent(pIntent1, true)
//                .setSilent(true)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setAutoCancel(true)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            notificationManager.areBubblesEnabled();
//            notificationManager.getBubblePreference();
//        }
//
//        notificationManager.notify(0, mBuilder.build());
//        if (BG.isPlaying()){
//            BG.stop();
//            vibrator.cancel();
//        }
    }

    Uri soundUri;
    public static MediaPlayer serviceRingtone;
    Vibrator vibrator;
    private void playSound() {
        soundUri = RingtoneManager.getDefaultUri(R.raw.notification);
        serviceRingtone = MediaPlayer.create(getBaseContext(), soundUri);
        serviceRingtone.setLooping(true);
        serviceRingtone.setVolume(100, 100);
        serviceRingtone.start();

        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(10000);
    }

}
