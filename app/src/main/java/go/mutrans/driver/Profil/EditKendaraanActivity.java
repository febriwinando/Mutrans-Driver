package go.mutrans.driver.Profil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import go.mutrans.driver.Json.EditKendaraanRequestJson;
import go.mutrans.driver.Json.LoginResponseJson;
import go.mutrans.driver.Konstanta.BaseApp;
import go.mutrans.driver.Models.FirebaseToken;
import go.mutrans.driver.Models.User;
import go.mutrans.driver.R;
import go.mutrans.driver.Utils.Api.Services.DriverService;
import go.mutrans.driver.Utils.Api.Services.ServiceGenerator;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditKendaraanActivity extends AppCompatActivity {

    ImageView backbtn;
    Button submit;
    EditText brand, type, platnomor, warna;
    TextView notiftext;
    RelativeLayout rlnotif, rlprogress;
    String disableback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kendaraan);
        User driver = BaseApp.getInstance(this).getLoginUser();
        backbtn = findViewById(R.id.back_btn_verify);
        submit = findViewById(R.id.buttonconfirm);
        brand = findViewById(R.id.brand);
        type = findViewById(R.id.type);
        platnomor = findViewById(R.id.platnomor);
        warna = findViewById(R.id.color);
        notiftext = findViewById(R.id.textnotif2);
        rlnotif = findViewById(R.id.rlnotif2);
        rlprogress = findViewById(R.id.rlprogress);

        disableback = "false";
        brand.setText(driver.getMerek());
        type.setText(driver.getTipe());
        platnomor.setText(driver.getNomorkendaraan());
        warna.setText(driver.getWarna());

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (brand.getText().toString().isEmpty()) {
                    notif("vehicle brand cant be empty!");
                } else if (type.getText().toString().isEmpty()) {
                    notif("vehicle type cant be empty!");
                } else if (platnomor.getText().toString().isEmpty()) {
                    notif("vehicle number cant be empty!");
                } else if (warna.getText().toString().isEmpty()) {
                    notif("vehicle color cant be empty!");
                } else {
                    get();
                }

            }
        });

    }

    private void get() {
        progressshow();
        EditKendaraanRequestJson request = new EditKendaraanRequestJson();
        User loginuser = BaseApp.getInstance(EditKendaraanActivity.this).getLoginUser();
        request.setNoTelepon(loginuser.getNoTelepon());
        request.setId(loginuser.getId());
        request.setId_kendaraan(loginuser.getIdk());
        request.setMerek(brand.getText().toString());
        request.setTipe(type.getText().toString());
        request.setNo_kendaraan(platnomor.getText().toString());
        request.setWarna(warna.getText().toString());

        DriverService service = ServiceGenerator.createService(DriverService.class, request.getNoTelepon(), loginuser.getPassword());
        service.editKendaraan(request).enqueue(new Callback<LoginResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseJson> call, @NonNull Response<LoginResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {

                        User user = response.body().getData().get(0);
                        saveUser(user);
                        finish();


                    } else {
                        notif(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseJson> call, @NonNull Throwable t) {
                progresshide();
                t.printStackTrace();
                notif("error");
            }
        });
    }

    public void progressshow() {
        rlprogress.setVisibility(View.VISIBLE);
        disableback = "true";
    }

    public void progresshide() {
        rlprogress.setVisibility(View.GONE);
        disableback = "false";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!disableback.equals("true")) {
            finish();
        }
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        notiftext.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(user);
        realm.commitTransaction();
        BaseApp.getInstance(EditKendaraanActivity.this).setLoginUser(user);
    }

    public void onMessageEvent(FirebaseToken response) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(FirebaseToken.class);
        realm.copyToRealm(response);
        realm.commitTransaction();
    }


}
