package go.mutrans.driver.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.Objects;

import go.mutrans.driver.Item.HistoryItem;
import go.mutrans.driver.Json.AllTransResponseJson;
import go.mutrans.driver.Json.DetailRequestJson;
import go.mutrans.driver.Konstanta.BaseApp;
import go.mutrans.driver.Models.User;
import go.mutrans.driver.R;
import go.mutrans.driver.Utils.Api.Services.DriverService;
import go.mutrans.driver.Utils.Api.Services.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {


    private Context context;
    private ShimmerFrameLayout shimmer;
    private RecyclerView recycle;
    private HistoryItem historyItem;
    private RelativeLayout rlnodata;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View getView = inflater.inflate(R.layout.fragment_history, container, false);
        context = getContext();
        shimmer = getView.findViewById(R.id.shimmerwallet);
        recycle = getView.findViewById(R.id.inboxlist);
        rlnodata = getView.findViewById(R.id.rlnodata);

        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new GridLayoutManager(context, 1));

        return getView;
    }

    private void shimmershow() {
        recycle.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmer();
    }

    private void shimmertutup() {

        recycle.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmer();
    }

    private void getdatatrans() {
        shimmershow();
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService driverService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(loginUser.getId());
        driverService.history(param).enqueue(new Callback<AllTransResponseJson>() {
            @Override
            public void onResponse(@NonNull Call<AllTransResponseJson> call, @NonNull Response<AllTransResponseJson> response) {
                if (response.isSuccessful()) {
                    shimmertutup();
                    historyItem = new HistoryItem(context, Objects.requireNonNull(response.body()).getData(), R.layout.item_order);
                    recycle.setAdapter(historyItem);
                    if (response.body().getData().isEmpty()) {
                        recycle.setVisibility(View.GONE);
                        rlnodata.setVisibility(View.VISIBLE);
                    } else {
                        recycle.setVisibility(View.VISIBLE);
                        rlnodata.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AllTransResponseJson> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getdatatrans();
    }
}
