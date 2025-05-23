package go.mutrans.driver.Profil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import go.mutrans.driver.Json.ChangePassRequestJson;
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


public class ChangepassActivity extends AppCompatActivity {

    ImageView backbtn;
    Button submit;
    EditText pass, passbaru;
    TextView notiftext;
    RelativeLayout rlnotif, rlprogress;
    String disableback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        backbtn = findViewById(R.id.back_btn_verify);
        submit = findViewById(R.id.buttonconfirm);
        pass = findViewById(R.id.password);
        passbaru = findViewById(R.id.newpassword);
        notiftext = findViewById(R.id.textnotif2);
        rlnotif = findViewById(R.id.rlnotif2);
        rlprogress = findViewById(R.id.rlprogress);

        disableback = "false";

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getText().toString().isEmpty()) {
                    notif("Password lama tidak boleh kosong!");
                } else if (passbaru.getText().toString().isEmpty()) {
                    notif("Silahkan input password baru anda!");
                } else {
                    get();
                }

            }
        });

    }

    private void get() {
        progressshow();
        ChangePassRequestJson request = new ChangePassRequestJson();
        User loginuser = BaseApp.getInstance(ChangepassActivity.this).getLoginUser();
        request.setNotelepon(loginuser.getNoTelepon());
        request.setPassword(pass.getText().toString());
        request.setNewPassword(passbaru.getText().toString());

        DriverService service = ServiceGenerator.createService(DriverService.class, request.getNotelepon(), request.getPassword());
        service.changepass(request).enqueue(new Callback<LoginResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseJson> call,@NonNull Response<LoginResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("found")) {

                        User user = response.body().getData().get(0);
                        saveUser(user);
                        finish();


                    } else {
                        notif(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseJson> call,@NonNull Throwable t) {
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
        BaseApp.getInstance(ChangepassActivity.this).setLoginUser(user);
    }

    public void onMessageEvent(FirebaseToken response) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(FirebaseToken.class);
        realm.copyToRealm(response);
        realm.commitTransaction();
    }


}
