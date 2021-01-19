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
import com.areeb.supertextiles.activities.EditReportActivity;
import com.areeb.supertextiles.adapters.ReportAdapter;
import com.areeb.supertextiles.interfaces.SearchInReport;
import com.areeb.supertextiles.models.Report;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.areeb.supertextiles.activities.ViewReportActivity.REPORT_OBJECT;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getReportListDatabaseReference;

public class ReportFragment extends Fragment implements SearchInReport {

    RecyclerView reportRecyclerView;
    ReportAdapter reportAdapter;
    DatabaseReference reportListDatabaseReference;
    FirebaseRecyclerOptions<Report> reportFirebaseRecyclerOptions;
    Gson gson;

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

        //initialize Gson object
        gson = new Gson();
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

        //set swipe call back
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(reportRecyclerView);

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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            TextView billNoTextView = viewHolder.itemView.findViewById(R.id.billNoReportTextView);
            String billNo = billNoTextView.getText().toString().split(":")[1].trim();
            int position = viewHolder.getLayoutPosition();
            Report reportToBeEdited = reportAdapter.getItem(position);
            String reportJson = "";

            if (direction == ItemTouchHelper.LEFT) {

                //create alert dialog for delete bill request
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete Report of Bill No. " + billNo + " ?")
                        .setTitle("Delete Report")
                        .setCancelable(true)
                        .setPositiveButton("Delete", (dialog, which) -> {

                            //delete report from report list in database
                            getReportListDatabaseReference().child(billNo).removeValue()
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Report deleted", Toast.LENGTH_SHORT).show());
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> reportAdapter.notifyDataSetChanged())
                        .setOnCancelListener(dialog -> reportAdapter.notifyDataSetChanged());

                //show alert dialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else if (direction == ItemTouchHelper.RIGHT) {
                if (reportToBeEdited != null) {
                    reportJson = gson.toJson(reportToBeEdited);
                }

                //create alert dialog for edit bill request
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                String finalReportJson = reportJson;
                builder.setMessage("Do you want to edit Report of Bill No. " + billNo + " ?")
                        .setTitle("Edit Report")
                        .setCancelable(true)
                        .setPositiveButton("Edit", (dialog, which) -> {

                            //send intent data to EditBillActivity
                            Intent intent = new Intent(getContext(), EditReportActivity.class);

                            //send intent data if bill object is not null
                            if (finalReportJson != null) {
                                intent.putExtra(REPORT_OBJECT, finalReportJson);
                            }

                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> reportAdapter.notifyDataSetChanged())
                        .setOnCancelListener(dialog -> reportAdapter.notifyDataSetChanged());

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