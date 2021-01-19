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
import com.areeb.supertextiles.activities.AddChallanActivity;
import com.areeb.supertextiles.adapters.ChallanAdapter;
import com.areeb.supertextiles.interfaces.SearchInChallan;
import com.areeb.supertextiles.models.Challan;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getChallanListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getDesignDataDatabaseReference;

public class ChallansFragment extends Fragment implements SearchInChallan {

    FloatingActionButton addChallanButton;
    RecyclerView challansRecyclerView;
    ChallanAdapter challanAdapter;
    DatabaseReference challanListDatabaseReference;
    FirebaseRecyclerOptions<Challan> challanOptions;
    View fragmentView;

    public ChallansFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize DatabaseReference
        challanListDatabaseReference = getChallanListDatabaseReference();

        //initialize FirebaseRecyclerOption
        FirebaseRecyclerOptions<Challan> options = new FirebaseRecyclerOptions.Builder<Challan>()
                .setQuery(challanListDatabaseReference, Challan.class)
                .build();

        //initialize ChallanAdapter
        challanAdapter = new ChallanAdapter(options, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialize fragment view
        fragmentView = inflater.inflate(R.layout.fragment_challans, container, false);

        //initialize xml view
        addChallanButton = fragmentView.findViewById(R.id.addChallanButton);
        challansRecyclerView = fragmentView.findViewById(R.id.challansRecyclerView);

        // To display the Recycler view linearly
        challansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set adapter of RecyclerView
        challansRecyclerView.setAdapter(challanAdapter);

        //set swipe call back
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(challansRecyclerView);

        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        challanAdapter.startListening();

        //addChallanButton onClickListener
        addChallanButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddChallanActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        challanAdapter.stopListening();
    }

    @Override
    public void onSearchInChallan(Query firebaseSearchQuery, RecyclerView challansRecyclerView) {
        if (challansRecyclerView != null) {
            challanOptions = new FirebaseRecyclerOptions.Builder<Challan>()
                    .setQuery(firebaseSearchQuery, Challan.class)
                    .build();

            challanAdapter = new ChallanAdapter(challanOptions, getContext());
            challansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            challansRecyclerView.setAdapter(challanAdapter);
            challanAdapter.startListening();
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            TextView challanNoTextView = viewHolder.itemView.findViewById(R.id.challanNumber);
            String challanNo = challanNoTextView.getText().toString();

            if (direction == ItemTouchHelper.LEFT) {

                //create alert dialog for delete challan request
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete Challan No. " + challanNo + " ?")
                        .setTitle("Delete Challan")
                        .setCancelable(true)
                        .setPositiveButton("Delete", (dialog, which) -> {

                            //delete challan from challan list in database
                            getChallanListDatabaseReference().child(challanNo).removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Challan deleted", Toast.LENGTH_SHORT).show());

                            //delete design data of challan from database
                            getDesignDataDatabaseReference(challanNo).removeValue();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> challanAdapter.notifyDataSetChanged())
                        .setOnCancelListener(dialog -> challanAdapter.notifyDataSetChanged());

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