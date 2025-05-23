package go.mutrans.driver.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class EditprofileRequestJson {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("fullnama")
    @Expose
    private String fullNama;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("email_lama")
    @Expose
    private String emaillama;

    @SerializedName("no_telepon")
    @Expose
    private String noTelepon;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("no_telepon_lama")
    @Expose
    private String phonelama;

    @SerializedName("tgl_lahir")
    @Expose
    private String tglLahir = "-";


    @SerializedName("fotodriver")
    @Expose
    private String fotopelanggan;

    @SerializedName("fotodriver_lama")
    @Expose
    private String fotopelangganlama;

    @SerializedName("countrycode")
    @Expose
    private String countrycode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullNama() {
        return fullNama;
    }

    public void setFullNama(String fullNama) {
        this.fullNama = fullNama;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmaillama() {
        return emaillama;
    }

    public void setEmaillama(String emaillama) {
        this.emaillama = emaillama;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(String tglLahir) {
        this.tglLahir = tglLahir;
    }


    public String getFotopelanggan() {
        return fotopelanggan;
    }

    public void setFotopelanggan(String fotopelanggan) {
        this.fotopelanggan = fotopelanggan;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhonelama() {
        return phonelama;
    }

    public void setPhonelama(String phonelama) {
        this.phonelama = phonelama;
    }

    public String getFotopelangganlama() {
        return fotopelangganlama;
    }

    public void setFotopelangganlama(String fotopelangganlama) {
        this.fotopelangganlama = fotopelangganlama;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}
