package com.areeb.supertextiles.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.areeb.supertextiles.activities.AddCustomerActivity;
import com.areeb.supertextiles.R;
import com.areeb.supertextiles.adapters.CustomersAdapter;
import com.areeb.supertextiles.interfaces.SearchInCustomer;
import com.areeb.supertextiles.models.Customer;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getAllCustomersReference;

public class CustomersFragment extends Fragment implements SearchInCustomer {

    RecyclerView customersRecyclerView;
    DatabaseReference customersListDatabaseReference;
    CustomersAdapter customersAdapter;
    FloatingActionButton addCustomerButton;
    FirebaseRecyclerOptions<Customer> customerFirebaseRecyclerOptions;

    public CustomersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize Firebase DatabaseReference
        customersListDatabaseReference = getAllCustomersReference();

        //initialize FirebaseRecyclerOptions and setAdapter to RecyclerView
        FirebaseRecyclerOptions<Customer> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Customer>()
                .setQuery(customersListDatabaseReference, Customer.class)
                .build();

        customersAdapter = new CustomersAdapter(firebaseRecyclerOptions, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_customers, container, false);

        //initialize xml views
        addCustomerButton = fragmentView.findViewById(R.id.addCustomerButton);
        customersRecyclerView = fragmentView.findViewById(R.id.customersRecyclerView);

        //set LayoutManager of RecyclerView
        customersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set adapter for RecyclerView
        customersRecyclerView.setAdapter(customersAdapter);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //addCustomerButton onClickListener
        addCustomerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddCustomerActivity.class);
            startActivity(intent);
        });

        customersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        customersAdapter.stopListening();
    }

    @Override
    public void onSearchInCustomer(Query firebaseSearchQuery, RecyclerView customersRecyclerView) {
        if (customersRecyclerView != null) {
            customerFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Customer>()
                    .setQuery(firebaseSearchQuery, Customer.class)
                    .build();

            customersAdapter = new CustomersAdapter(customerFirebaseRecyclerOptions, getContext());
            customersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            customersRecyclerView.setAdapter(customersAdapter);
            customersAdapter.startListening();
        }
    }
}