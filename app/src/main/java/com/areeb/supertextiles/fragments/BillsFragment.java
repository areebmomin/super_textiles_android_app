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
import com.areeb.supertextiles.activities.AddBillActivity;
import com.areeb.supertextiles.activities.EditBillActivity;
import com.areeb.supertextiles.adapters.BillsAdapter;
import com.areeb.supertextiles.interfaces.SearchInBills;
import com.areeb.supertextiles.models.Bill;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.areeb.supertextiles.activities.ViewBillActivity.BILL_OBJECT;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getBillListDatabaseReference;

public class BillsFragment extends Fragment implements SearchInBills {

    RecyclerView billsRecyclerView;
    BillsAdapter billsAdapter;
    DatabaseReference billsDatabaseReference;
    FloatingActionButton addBillButton;
    FirebaseRecyclerOptions<Bill> billFirebaseRecyclerOptions;
    Gson gson;

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

        //initialize Gson object
        gson = new Gson();
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

        //set swipe call back
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(billsRecyclerView);

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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            TextView billNoTextView = viewHolder.itemView.findViewById(R.id.billNoTextView);
            String billNo = billNoTextView.getText().toString().split(":")[1].trim();
            int position = viewHolder.getLayoutPosition();
            Bill billToBeEdited = billsAdapter.getItem(position);
            String billJson = "";

            if (direction == ItemTouchHelper.LEFT) {

                //create alert dialog for delete bill request
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete Bill No. " + billNo + " ?")
                        .setTitle("Delete Bill")
                        .setCancelable(true)
                        .setPositiveButton("Delete", (dialog, which) -> {

                            //delete bill from bill list in database
                            getBillListDatabaseReference().child(billNo).removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Bill deleted", Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> billsAdapter.notifyDataSetChanged())
                        .setOnCancelListener(dialog -> billsAdapter.notifyDataSetChanged());

                //show alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else if (direction == ItemTouchHelper.RIGHT) {
                if (billToBeEdited != null) {
                    billJson = gson.toJson(billToBeEdited);
                }

                //create alert dialog for edit bill request
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                String finalBillJson = billJson;
                builder.setMessage("Do you want to edit Bill No. " + billNo + " ?")
                        .setTitle("Edit Bill")
                        .setCancelable(true)
                        .setPositiveButton("Edit", (dialog, which) -> {

                            //send intent data to EditBillActivity
                            Intent intent = new Intent(getContext(), EditBillActivity.class);

                            //send intent data if bill object is not null
                            if (finalBillJson != null) {
                                intent.putExtra(BILL_OBJECT, finalBillJson);
                            }

                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> billsAdapter.notifyDataSetChanged())
                        .setOnCancelListener(dialog -> billsAdapter.notifyDataSetChanged());

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
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.swipe_edit_background))
                    .addSwipeRightActionIcon(R.drawable.ic_edit_swipe)
                    .setSwipeRightActionIconTint(Color.BLACK)
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