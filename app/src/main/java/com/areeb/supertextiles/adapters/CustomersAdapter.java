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
import com.areeb.supertextiles.activities.ViewCustomerActivity;
import com.areeb.supertextiles.models.Customer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import static com.areeb.supertextiles.activities.ViewCustomerActivity.CUSTOMER_ADDRESS;
import static com.areeb.supertextiles.activities.ViewCustomerActivity.CUSTOMER_GST_NO;
import static com.areeb.supertextiles.activities.ViewCustomerActivity.CUSTOMER_ID;
import static com.areeb.supertextiles.activities.ViewCustomerActivity.CUSTOMER_NAME;

public class CustomersAdapter extends FirebaseRecyclerAdapter<Customer, CustomersAdapter.CustomersViewHolder> {

    Context context;

    //CustomersAdapter's constructor
    public CustomersAdapter(@NonNull FirebaseRecyclerOptions<Customer> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomersViewHolder holder, int position, @NonNull Customer model) {
        //set data in list_view
        holder.customerNameTextView.setText(model.getName());
        holder.customerAddressTextView.setText(model.getAddress());

        //Customer List onClickListener
        holder.customerListCardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewCustomerActivity.class);
            intent.putExtra(CUSTOMER_ID, model.getId());
            intent.putExtra(CUSTOMER_NAME, model.getName());
            intent.putExtra(CUSTOMER_ADDRESS, model.getAddress());
            intent.putExtra(CUSTOMER_GST_NO, model.getGSTNo());
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public CustomersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_list_view, parent, false);
        return new CustomersViewHolder(view);
    }

    //customers view holder class
    static class CustomersViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTextView, customerAddressTextView;
        CardView customerListCardView;

        public CustomersViewHolder(@NonNull View view) {
            super(view);

            //initializing xml views
            customerListCardView = view.findViewById(R.id.customerListCardView);
            customerNameTextView = view.findViewById(R.id.customerName);
            customerAddressTextView = view.findViewById(R.id.customerAddress);
        }

    }
}
