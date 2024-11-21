package go.mutrans.driver.Item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import go.mutrans.driver.Konstanta.Constants;
import go.mutrans.driver.Models.AllTransaksiModel;
import go.mutrans.driver.OrderDetailActivity;
import go.mutrans.driver.R;
import go.mutrans.driver.Utils.Utility;


public class HistoryItem extends RecyclerView.Adapter<HistoryItem.ItemRowHolder> {

    private List<AllTransaksiModel> dataList;
    private Context mContext;
    private int rowLayout;


    public HistoryItem(Context context, List<AllTransaksiModel> dataList, int rowLayout) {
        this.dataList = dataList;
        this.mContext = context;
        this.rowLayout = rowLayout;

    }


    @NonNull
    @Override
    public ItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ItemRowHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ItemRowHolder holder, final int position) {
        final AllTransaksiModel singleItem = dataList.get(position);
        holder.text.setText("Order " + singleItem.getFitur());
        if (singleItem.getHome().equals("4")) {
            double totalbiaya = Double.parseDouble(singleItem.getTotalbiaya());
            Utility.currencyTXT(holder.nominal, String.valueOf(singleItem.getHarga()+ totalbiaya), mContext);
        } else {
            Utility.currencyTXT(holder.nominal, String.valueOf(singleItem.getHarga()), mContext);
        }
        holder.keterangan.setText(singleItem.getStatustransaksi());


        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        String finalDate = timeFormat.format(singleItem.getWaktuOrder());
        holder.tanggal.setText(finalDate);

        Glide.with(mContext)
                .load(Constants.IMAGESFITUR + singleItem.getIcon())
                .into(holder.images);

        if (singleItem.status == 4 && singleItem.getRate().isEmpty()) {
            holder.keterangan.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.colorgradient));
            holder.background.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rect));

        } else if (singleItem.status == 5) {
            holder.keterangan.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.background.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rect_red));

        } else {
            holder.keterangan.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.nominal.setTextColor(mContext.getResources().getColor(R.color.colorgradient));
            holder.background.setBackground(mContext.getResources().getDrawable(R.drawable.btn_rect));

        }
        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, OrderDetailActivity.class);
                i.putExtra("id_pelanggan", singleItem.getIdPelanggan());
                i.putExtra("id_transaksi", singleItem.getIdTransaksi());
                i.putExtra("response", String.valueOf(singleItem.status));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    static class ItemRowHolder extends RecyclerView.ViewHolder {
        TextView text, tanggal, nominal, keterangan;
        ImageView background, images;
        RelativeLayout itemlayout;

        ItemRowHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            images = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.text);
            tanggal = itemView.findViewById(R.id.texttanggal);
            nominal = itemView.findViewById(R.id.price);
            keterangan = itemView.findViewById(R.id.textket);
            itemlayout = itemView.findViewById(R.id.mainlayout);
        }
    }


}
