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
import com.areeb.supertextiles.activities.ViewReportActivity;
import com.areeb.supertextiles.models.Report;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;

import static com.areeb.supertextiles.activities.ViewReportActivity.REPORT_OBJECT;

public class ReportAdapter extends FirebaseRecyclerAdapter<Report, ReportAdapter.ReportViewHolder> {

    Context context;

    public ReportAdapter(@NonNull FirebaseRecyclerOptions<Report> options, Context context) {
        super(options);

        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReportViewHolder holder, int position, @NonNull Report model) {

        //set model data in list view
        String billNo = "B No: " + model.getBillNo();
        holder.billNoReportTextView.setText(billNo);
        holder.dateReportTextView.setText(model.getDate());
        holder.partyReportTextView.setText(model.getParty());
        if (!model.getFinalAmount().trim().isEmpty() && !model.getReceivedAmount().trim().isEmpty()) {
            double pendingAmountDouble = Double.parseDouble(model.getFinalAmount()) - Double.parseDouble(model.getReceivedAmount());
            String remainingAmount = new DecimalFormat("##.##").format(pendingAmountDouble);
            holder.remainingAmountReportTextView.setText(context.getString(R.string.pending_amt, remainingAmount));
        } else if (!model.getFinalAmount().trim().isEmpty() && context != null) {
            holder.remainingAmountReportTextView.setText(context.getString(R.string.pending_amt, model.getFinalAmount()));
        }

        //cardView onClick listener
        holder.reportCardView.setOnClickListener(v -> {

            //initialize Gson
            Gson gson = new Gson();

            //convert Report object in to Json
            String reportJson = gson.toJson(model);

            //goto ViewReportActivity
            Intent intent = new Intent(context, ViewReportActivity.class);
            intent.putExtra(REPORT_OBJECT, reportJson);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_view, parent, false);
        return new ReportViewHolder(view);
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView billNoReportTextView, dateReportTextView, partyReportTextView, remainingAmountReportTextView;
        CardView reportCardView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            billNoReportTextView = itemView.findViewById(R.id.billNoReportTextView);
            dateReportTextView = itemView.findViewById(R.id.dateReportTextView);
            partyReportTextView = itemView.findViewById(R.id.partyReportTextView);
            remainingAmountReportTextView = itemView.findViewById(R.id.remainingAmountReportTextView);
            reportCardView = itemView.findViewById(R.id.reportCardView);
        }
    }
}
