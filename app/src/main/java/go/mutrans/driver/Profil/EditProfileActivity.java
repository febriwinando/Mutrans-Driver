package go.mutrans.driver.Profil;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


import go.mutrans.driver.Json.EditprofileRequestJson;
import go.mutrans.driver.Json.LoginResponseJson;
import go.mutrans.driver.Konstanta.BaseApp;
import go.mutrans.driver.Konstanta.Constants;
import go.mutrans.driver.Models.User;
import go.mutrans.driver.R;
import go.mutrans.driver.Utils.Api.Services.DriverService;
import go.mutrans.driver.Utils.Api.Services.ServiceGenerator;
import go.mutrans.driver.Utils.PicassoTrustAll;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    ImageView foto, gantifoto, backbtn, backButtonverify;
    EditText phone, nama, email;
    TextView tanggal, countryCode, textnotif;
    Button submit;
    RelativeLayout rlnotif;
    private SimpleDateFormat dateFormatter, dateFormatterview;
    byte[] imageByteArray;
    Bitmap decoded;
    String dateview, disableback, onsubmit;

    String country_iso_code = "en";
    DatePickerDialog datepickerTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        foto = findViewById(R.id.foto);
        gantifoto = findViewById(R.id.editfoto);
        backbtn = findViewById(R.id.back_btn);
        phone = findViewById(R.id.phonenumber);
        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        tanggal = findViewById(R.id.tanggal);
        submit = findViewById(R.id.submit);
        rlnotif = findViewById(R.id.rlnotif);
        textnotif = findViewById(R.id.textnotif);
        countryCode = findViewById(R.id.countrycode);
        backButtonverify = findViewById(R.id.back_btn_verify);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        onsubmit = "true";

        User loginUser = BaseApp.getInstance(this).getLoginUser();
        PicassoTrustAll.getInstance(this)
                .load(Constants.IMAGESDRIVER + loginUser.getFotodriver())
                .placeholder(R.drawable.image_placeholder)
                .resize(250, 250)
                .into(foto);

        phone.setText(loginUser.getPhone());
        nama.setText(loginUser.getFullnama());
        email.setText(loginUser.getEmail());
        dateview = loginUser.getTglLahir();
        countryCode.setText(loginUser.getCountrycode());

        gantifoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        countryCode.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
//                final CountryPicker picker = CountryPicker.newInstance("Select Country");
//                picker.setListener(new CountryPickerListener() {
//                    @Override
//                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
//                        countryCode.setText(dialCode);
//                        picker.dismiss();
//                        country_iso_code = code;
//                    }
//                });
//                picker.setStyle(R.style.countrypicker_style, R.style.countrypicker_style);
//                picker.show(getSupportFragmentManager(), "Select Country");
            }
        });

        Date myDate = null;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatterview = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        try {
            if (loginUser.getTglLahir() != null) {
                myDate = dateFormatter.parse(loginUser.getTglLahir());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalDate = dateFormatterview.format(Objects.requireNonNull(myDate));
        tanggal.setText(finalDate);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                final String emailvalidate = email.getText().toString();

                if (TextUtils.isEmpty(phone.getText().toString())) {

                    notif(getString(R.string.phoneempty));

                } else if (TextUtils.isEmpty(nama.getText().toString())) {

                    notif("Name cant be empty");

                } else if (TextUtils.isEmpty(email.getText().toString())) {

                    notif(getString(R.string.emailempty));

                } else if (TextUtils.isEmpty(tanggal.getText().toString())) {

                    notif("birthday cant be empty!");

                } else if (!emailvalidate.matches(emailPattern)) {

                    notif("wrong email format!");

                } else {
                    if (onsubmit.equals("true")) {
                        submit.setText(getString(R.string.waiting_pleaseWait));
                        submit.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button));
                        editprofile();
                    }
                }
            }
        });

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showTanggal();
                datepickerTanggal.show();
            }
        });
        disableback = "false";
        showTanggal();

    }

    private void showTanggal() {
        DatePickerDialog.OnDateSetListener datasetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long date_ship_millis = calendar.getTimeInMillis();
                tanggal.setText(dateFormatterview.format(date_ship_millis));
                dateview = dateFormatter.format(date_ship_millis);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int tahun = calendar.get(Calendar.YEAR);
        int bulan = calendar.get(Calendar.MONTH);
        int hari = calendar.get(Calendar.DAY_OF_MONTH);

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add( Calendar.MONTH, 0);
        long maxDate = c.getTime().getTime();
        datepickerTanggal = new DatePickerDialog(this, 0, datasetListener, tahun, bulan, hari);
        datepickerTanggal.getDatePicker().setMaxDate(maxDate);

    }


    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }


    /**
     * uploadfoto-------------start.
     */
    private boolean check_ReadStoragepermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.permission_Read_data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    private void selectImage() {
        if (check_ReadStoragepermission()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        }
    }

    public String getPath(Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = this.getContentResolver().openInputStream(Objects.requireNonNull(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);

                String path = getPath(selectedImage);
                Matrix matrix = new Matrix();
                ExifInterface exif;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    try {
                        exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                matrix.postRotate(90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                matrix.postRotate(180);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                matrix.postRotate(270);
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), matrix, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                foto.setImageBitmap(rotatedBitmap);
                imageByteArray = baos.toByteArray();
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()));

            }

        }

    }

    private void editprofile() {
        onsubmit = "false";
        User loginUser = BaseApp.getInstance(this).getLoginUser();
        EditprofileRequestJson request = new EditprofileRequestJson();
        request.setFullNama(nama.getText().toString());
        request.setEmail(email.getText().toString());
        request.setEmaillama(loginUser.getEmail());
        request.setId(loginUser.getId());
        request.setNoTelepon(countryCode.getText().toString().replace("+", "") + phone.getText().toString());
        request.setPhone(phone.getText().toString());
        request.setCountrycode(countryCode.getText().toString());
        request.setPhonelama(loginUser.getNoTelepon());
        if (imageByteArray != null) {
            request.setFotopelangganlama(loginUser.getFotodriver());
            request.setFotopelanggan(getStringImage(decoded));
        }
        request.setTglLahir(dateview);


        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        service.editProfile(request).enqueue(new Callback<LoginResponseJson>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<LoginResponseJson> call, @NonNull Response<LoginResponseJson> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getMessage().equalsIgnoreCase("success")) {
                        User user = response.body().getData().get(0);
                        saveUser(user);
                        finish();
                        onsubmit = "true";

                    } else {
                        onsubmit = "true";
                        submit.setText("Submit");
                        submit.setBackground(getResources().getDrawable(R.drawable.button_round_1));
                        notif(response.body().getMessage());
                    }
                } else {
                    onsubmit = "true";
                    submit.setText("Submit");
                    submit.setBackground(getResources().getDrawable(R.drawable.button_round_1));
                    notif("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseJson> call, @NonNull Throwable t) {
                t.printStackTrace();
                notif("error!");
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        imageByteArray = baos.toByteArray();
        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    private void saveUser(User user) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(User.class);
        realm.copyToRealm(user);
        realm.commitTransaction();
        BaseApp.getInstance(EditProfileActivity.this).setLoginUser(user);
    }
}
