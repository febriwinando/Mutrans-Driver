package go.mutrans.driver.Utils.Api.Services;


import go.mutrans.driver.Json.AcceptRequestJson;
import go.mutrans.driver.Json.AcceptResponseJson;
import go.mutrans.driver.Json.AllTransResponseJson;
import go.mutrans.driver.Json.ChangePassRequestJson;
import go.mutrans.driver.Json.DetailRequestJson;
import go.mutrans.driver.Json.DetailTransResponseJson;
import go.mutrans.driver.Json.EditKendaraanRequestJson;
import go.mutrans.driver.Json.EditprofileRequestJson;
import go.mutrans.driver.Json.GetHomeRequestJson;
import go.mutrans.driver.Json.GetHomeResponseJson;
import go.mutrans.driver.Json.GetOnRequestJson;
import go.mutrans.driver.Json.JobResponseJson;
import go.mutrans.driver.Json.LoginRequestJson;
import go.mutrans.driver.Json.LoginResponseJson;
import go.mutrans.driver.Json.PrivacyRequestJson;
import go.mutrans.driver.Json.PrivacyResponseJson;
import go.mutrans.driver.Json.RegisterRequestJson;
import go.mutrans.driver.Json.RegisterResponseJson;
import go.mutrans.driver.Json.ResponseJson;
import go.mutrans.driver.Json.UpdateLocationRequestJson;
import go.mutrans.driver.Json.VerifyRequestJson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface DriverService {

    @POST("driver/login")
    Call<LoginResponseJson> login(@Body LoginRequestJson param);

    @POST("driver/update_location")
    Call<ResponseJson> updatelocation(@Body UpdateLocationRequestJson param);

    @POST("driver/syncronizing_account")
    Call<GetHomeResponseJson> home(@Body GetHomeRequestJson param);

    @POST("driver/logout")
    Call<GetHomeResponseJson> logout(@Body GetHomeRequestJson param);

    @POST("driver/turning_on")
    Call<ResponseJson> turnon(@Body GetOnRequestJson param);

    @POST("driver/accept")
    Call<AcceptResponseJson> accept(@Body AcceptRequestJson param);

    @POST("driver/start")
    Call<AcceptResponseJson> startrequest(@Body AcceptRequestJson param);

    @POST("driver/finish")
    Call<AcceptResponseJson> finishrequest(@Body AcceptRequestJson param);

    @POST("driver/edit_profile")
    Call<LoginResponseJson> editProfile(@Body EditprofileRequestJson param);

    @POST("driver/edit_kendaraan")
    Call<LoginResponseJson> editKendaraan(@Body EditKendaraanRequestJson param);

    @POST("driver/changepass")
    Call<LoginResponseJson> changepass(@Body ChangePassRequestJson param);

    @POST("driver/history_progress")
    Call<AllTransResponseJson> history(@Body DetailRequestJson param);

    @POST("driver/forgot")
    Call<LoginResponseJson> forgot(@Body LoginRequestJson param);

    @POST("driver/register_driver")
    Call<RegisterResponseJson> register(@Body RegisterRequestJson param);

//    @POST("pelanggan/list_bank")
//    Call<BankResponseJson> listbank(@Body WithdrawRequestJson param);

    @POST("driver/detail_transaksi")
    Call<DetailTransResponseJson> detailtrans(@Body DetailRequestJson param);

    @POST("driver/job")
    Call<JobResponseJson> job();

    @POST("pelanggan/privacy")
    Call<PrivacyResponseJson> privacy(@Body PrivacyRequestJson param);

//    @POST("pelanggan/topupstripe")
//    Call<TopupResponseJson> topup(@Body TopupRequestJson param);
//
//    @POST("driver/withdraw")
//    Call<WithdrawResponseJson> withdraw(@Body WithdrawRequestJson param);
//
//    @POST("pelanggan/wallet")
//    Call<WalletResponseJson> wallet(@Body WalletRequestJson param);
//
//    @POST("driver/topuppaypal")
//    Call<ResponseJson> topuppaypal(@Body WithdrawRequestJson param);

    @POST("driver/verifycode")
    Call<ResponseJson> verifycode(@Body VerifyRequestJson param);


}
