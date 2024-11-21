package go.mutrans.driver.Json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import go.mutrans.driver.Models.JobModel;


/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class JobResponseJson {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<JobModel> data = new ArrayList<>();


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<JobModel> getData() {
        return data;
    }

    public void setData(List<JobModel> data) {
        this.data = data;
    }
}
