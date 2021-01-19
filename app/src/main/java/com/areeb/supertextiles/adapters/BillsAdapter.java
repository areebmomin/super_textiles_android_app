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

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.activities.ViewBillActivity;
import com.areeb.supertextiles.models.Bill;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.gson.Gson;

import static com.areeb.supertextiles.activities.ViewBillActivity.BILL_OBJECT;

public class BillsAdapter extends FirebaseRecyclerAdapter<Bill, BillsAdapter.BillsViewHolder> {

    Context context;

    public BillsAdapter(@NonNull FirebaseRecyclerOptions<Bill> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull BillsViewHolder holder, int position, @NonNull Bill model) {

        //set data in TextView's
        String billNo = "B No: " + model.getBillNo();
        holder.billNoTextView.setText(billNo);
        holder.dateTextView.setText(model.getDate());
        holder.messersTextView.setText(model.getMessers());
        holder.amountAfterTaxTextView.setText(model.getAmountAfterTax());

        //set list view onClickListener
        holder.billsCardView.setOnClickListener(v -> {
            //initialize Gson
            Gson gson = new Gson();

            //get Json String of Bill object
            String billsJson = gson.toJson(model);

            //send intent data to ViewBillActivity
            Intent intent = new Intent(context, ViewBillActivity.class);
            intent.putExtra(BILL_OBJECT, billsJson);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public BillsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bills_list_view, parent, false);
        return new BillsViewHolder(view);
    }

    static class BillsViewHolder extends RecyclerView.ViewHolder {
        TextView billNoTextView, dateTextView, messersTextView, amountAfterTaxTextView;
        CardView billsCardView;

        public BillsViewHolder(@NonNull View itemView) {
            super(itemView);

            billsCardView = itemView.findViewById(R.id.billsCardView);
            billNoTextView = itemView.findViewById(R.id.billNoTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            messersTextView = itemView.findViewById(R.id.messersTextView);
            amountAfterTaxTextView = itemView.findViewById(R.id.amountAfterTaxTextView);
        }
    }
}
