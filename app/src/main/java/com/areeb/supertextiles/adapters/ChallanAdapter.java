package com.areeb.supertextiles.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.areeb.supertextiles.ChallanDetailsActivity;
import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Challan;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import static com.areeb.supertextiles.ChallanDetailsActivity.CHALLAN_NO;
import static com.areeb.supertextiles.ChallanDetailsActivity.CHALLAN_DATE;
import static com.areeb.supertextiles.ChallanDetailsActivity.PURCHASER;
import static com.areeb.supertextiles.ChallanDetailsActivity.LOT_NO;
import static com.areeb.supertextiles.ChallanDetailsActivity.LR_NO;
import static com.areeb.supertextiles.ChallanDetailsActivity.DELIVERY_AT;
import static com.areeb.supertextiles.ChallanDetailsActivity.PURCHASER_GST;
import static com.areeb.supertextiles.ChallanDetailsActivity.QUALITY;
import static com.areeb.supertextiles.ChallanDetailsActivity.TOTAL_PIECES;
import static com.areeb.supertextiles.ChallanDetailsActivity.TOTAL_METERS;
import static com.areeb.supertextiles.ChallanDetailsActivity.FOLD;
import static com.areeb.supertextiles.ChallanDetailsActivity.NO_OF_DESIGNS;

public class ChallanAdapter extends FirebaseRecyclerAdapter<Challan, ChallanAdapter.ChallanViewHolder> {

    Context context;

    public ChallanAdapter(@NonNull FirebaseRecyclerOptions<Challan> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChallanViewHolder holder, int position, @NonNull Challan model) {
        //set challan data in TextView
        holder.challanNumber.setText(model.getChallan_no());
        holder.purchaserName.setText(model.getPurchaser());
        holder.qualityName.setText(model.getQuality());
        String totalMeterString = model.getTotal_meters() + " Mtrs";
        holder.totalMeters.setText(totalMeterString);
        holder.date.setText(model.getDate());

        //challan list onClickListener
        holder.challanListCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChallanDetailsActivity.class);
            intent.putExtra(CHALLAN_NO, model.getChallan_no());
            intent.putExtra(CHALLAN_DATE, model.getDate());
            intent.putExtra(PURCHASER, model.getPurchaser());
            intent.putExtra(LOT_NO, model.getLot_no());
            intent.putExtra(LR_NO, model.getLr_no());
            intent.putExtra(DELIVERY_AT, model.getDelivery_at());
            intent.putExtra(PURCHASER_GST, model.getPurchaser_gst());
            intent.putExtra(QUALITY, model.getQuality());
            intent.putExtra(TOTAL_PIECES, model.getTotal_pieces());
            intent.putExtra(TOTAL_METERS, model.getTotal_meters());
            intent.putExtra(FOLD, model.getFold());
            intent.putExtra(NO_OF_DESIGNS, model.getNo_of_designs());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ChallanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.challan_list_view, parent, false);
        return new ChallanViewHolder(view);
    }

    static class ChallanViewHolder extends RecyclerView.ViewHolder {
        TextView challanNumber, purchaserName, qualityName, totalMeters, date;
        CardView challanListCardView;

        public ChallanViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialize xml views
            challanNumber = itemView.findViewById(R.id.challanNumber);
            purchaserName = itemView.findViewById(R.id.purchaserName);
            qualityName = itemView.findViewById(R.id.qualityName);
            totalMeters = itemView.findViewById(R.id.totalMeters);
            date = itemView.findViewById(R.id.date);
            challanListCardView = itemView.findViewById(R.id.challanListCardView);
        }
    }
}
