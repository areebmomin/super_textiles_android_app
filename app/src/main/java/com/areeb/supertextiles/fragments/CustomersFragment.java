package com.areeb.supertextiles.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.activities.AddCustomerActivity;
import com.areeb.supertextiles.adapters.CustomersAdapter;
import com.areeb.supertextiles.interfaces.SearchInCustomer;
import com.areeb.supertextiles.models.Customer;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getAllCustomersReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getCustomerDatabaseReferenceByID;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getDeliveryAddressDatabaseReference;

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

        //set swipe call back
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(customersRecyclerView);

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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            TextView customerNameTextView = viewHolder.itemView.findViewById(R.id.customerName);
            String customerName = customerNameTextView.getText().toString();
            int position = viewHolder.getLayoutPosition();
            Customer customerToBeDeleted = customersAdapter.getItem(position);

            if (direction == ItemTouchHelper.LEFT) {

                //create alert dialog for delete customer request
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete customer " + customerName + " ?")
                        .setTitle("Delete Customer")
                        .setCancelable(true)
                        .setPositiveButton("Delete", (dialog, which) -> {

                            //delete customer from customer list in database
                            getCustomerDatabaseReferenceByID(customerToBeDeleted.getId()).removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Customer deleted", Toast.LENGTH_SHORT).show());

                            //delete delivery address of customer from database
                            getDeliveryAddressDatabaseReference().child(customerToBeDeleted.getId()).removeValue();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> customersAdapter.notifyDataSetChanged())
                        .setOnCancelListener(dialog -> customersAdapter.notifyDataSetChanged());

                //show alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.swipe_delete_background))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_32)
                    .setSwipeLeftActionIconTint(Color.BLACK)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            return 0.5f;
        }
    };
}