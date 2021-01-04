package com.areeb.supertextiles;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.areeb.supertextiles.adapters.ChallanAdapter;
import com.areeb.supertextiles.models.Challan;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

import static com.areeb.supertextiles.FirebaseDatabaseHelper.getChallanListDatabaseReference;

public class ChallansFragment extends Fragment {

    FloatingActionButton addChallanButton;
    RecyclerView challansRecyclerView;
    ChallanAdapter challanAdapter;
    DatabaseReference challanListDatabaseReference;

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
        View fragmentView = inflater.inflate(R.layout.fragment_challans, container, false);

        //initialize xml view
        addChallanButton = fragmentView.findViewById(R.id.addChallanButton);
        challansRecyclerView = fragmentView.findViewById(R.id.challansRecyclerView);

        // To display the Recycler view linearly
        challansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set adapter of RecyclerView
        challansRecyclerView.setAdapter(challanAdapter);

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
}