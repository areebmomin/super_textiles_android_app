package com.areeb.supertextiles.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.adapters.ReportAdapter;
import com.areeb.supertextiles.interfaces.SearchInReport;
import com.areeb.supertextiles.models.Report;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getReportListDatabaseReference;

public class ReportFragment extends Fragment implements SearchInReport {

    RecyclerView reportRecyclerView;
    ReportAdapter reportAdapter;
    DatabaseReference reportListDatabaseReference;
    FirebaseRecyclerOptions<Report> reportFirebaseRecyclerOptions;

    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize reportListDatabaseReference
        reportListDatabaseReference = getReportListDatabaseReference();

        //initialize firebase recycler option
        FirebaseRecyclerOptions<Report> options = new FirebaseRecyclerOptions.Builder<Report>()
                .setQuery(reportListDatabaseReference, Report.class)
                .build();

        //initialize recycler view adapter
        reportAdapter = new ReportAdapter(options, getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        //initialize xml views
        reportRecyclerView = view.findViewById(R.id.reportRecyclerView);

        //set layout manager for recycler view
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set recycler view adapter
        reportRecyclerView.setAdapter(reportAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        reportAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        reportAdapter.stopListening();
    }

    @Override
    public void onSearchInReport(Query firebaseSearchQuery, RecyclerView reportRecyclerView) {
        if (reportRecyclerView != null) {
            reportFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Report>()
                    .setQuery(firebaseSearchQuery, Report.class)
                    .build();

            reportAdapter = new ReportAdapter(reportFirebaseRecyclerOptions, getContext());
            reportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            reportRecyclerView.setAdapter(reportAdapter);
            reportAdapter.startListening();
        }
    }
}