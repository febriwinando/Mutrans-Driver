package go.mutrans.driver.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import go.mutrans.driver.Models.AllTransaksiModel;
import go.mutrans.driver.Models.PelangganModel;

/**
 * Created by Ourdevelops Team on 10/19/2019.
 */

public class AllTransResponseJson {

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("data")
    private List<AllTransaksiModel> data = new ArrayList<>();

    @Expose
    @SerializedName("driver")
    private List<PelangganModel> driver = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AllTransaksiModel> getData() {
        return data;
    }

    public void setData(List<AllTransaksiModel> data) {
        this.data = data;
    }

    public List<PelangganModel> getDriver() {
        return driver;
    }

    public void setDriver(List<PelangganModel> driver) {
        this.driver = driver;
    }
}
