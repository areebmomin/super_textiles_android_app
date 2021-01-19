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

import com.areeb.supertextiles.activities.ChallanDetailsActivity;
import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Challan;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.gson.Gson;

import static com.areeb.supertextiles.activities.ChallanDetailsActivity.CHALLAN_OBJECT;

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
            //initialize Gson object
            Gson gson = new Gson();

            //get Json string of Challan object
            String challanJsonString = gson.toJson(model);

            //send intent data to ChallanDetailsActivity
            Intent intent = new Intent(context, ChallanDetailsActivity.class);
            intent.putExtra(CHALLAN_OBJECT, challanJsonString);
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
