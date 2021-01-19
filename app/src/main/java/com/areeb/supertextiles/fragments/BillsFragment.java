package com.areeb.supertextiles.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.areeb.supertextiles.activities.AddBillActivity;
import com.areeb.supertextiles.R;
import com.areeb.supertextiles.adapters.BillsAdapter;
import com.areeb.supertextiles.interfaces.SearchInBills;
import com.areeb.supertextiles.models.Bill;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Objects;

import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getBillListDatabaseReference;

public class BillsFragment extends Fragment implements SearchInBills {

    RecyclerView billsRecyclerView;
    BillsAdapter billsAdapter;
    DatabaseReference billsDatabaseReference;
    FloatingActionButton addBillButton;
    FirebaseRecyclerOptions<Bill> billFirebaseRecyclerOptions;

    public BillsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize database reference
        billsDatabaseReference = getBillListDatabaseReference();

        //initialize firebase recycler options
        FirebaseRecyclerOptions<Bill> options = new FirebaseRecyclerOptions.Builder<Bill>()
                .setQuery(billsDatabaseReference, Bill.class)
                .build();

        //initialize recycler adapter
        billsAdapter = new BillsAdapter(options, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        //initializing xml views
        billsRecyclerView = view.findViewById(R.id.billsRecyclerView);
        addBillButton = view.findViewById(R.id.addBillButton);

        //addButton onCLick listener
        addBillButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddBillActivity.class);
            Objects.requireNonNull(getContext()).startActivity(intent);
        });

        //set recycler view layout manager
        billsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set recycler adapter
        billsRecyclerView.setAdapter(billsAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        billsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        billsAdapter.stopListening();
    }

    @Override
    public void onSearchInBills(Query firebaseSearchQuery, RecyclerView billsRecyclerView) {
        if (billsRecyclerView != null) {
            billFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Bill>()
                    .setQuery(firebaseSearchQuery, Bill.class)
                    .build();

            billsAdapter = new BillsAdapter(billFirebaseRecyclerOptions, getContext());
            billsRecyclerView.setAdapter(billsAdapter);
            billsAdapter.startListening();
        }
    }
}