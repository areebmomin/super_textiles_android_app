package com.areeb.supertextiles.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.areeb.supertextiles.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static com.areeb.supertextiles.activities.AddChallanActivity.showErrorInTextField;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getQualityListDatabaseReference;

public class QualitiesActivity extends AppCompatActivity {

    TextInputLayout addQualityTextField;
    ArrayList<String> qualityList;
    ConstraintLayout qualityActivityParent;
    TableLayout qualityListTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Manage Quality");
        setContentView(R.layout.activity_qualities);

        //initialize xml views
        addQualityTextField = findViewById(R.id.addQualityTextField);
        qualityActivityParent = findViewById(R.id.qualityActivityParent);
        qualityListTableLayout = findViewById(R.id.qualityListTableLayout);

        //initialize array list
        qualityList = new ArrayList<>();

        //get quality list from database and put it in table
        getQualityListDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot qualitySnapshot : snapshot.getChildren()) {
                   String quality = qualitySnapshot.getValue(String.class);
                   addQualityToTable(qualityListTableLayout, quality, qualityList);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException().printStackTrace();
            }
        });
    }

    //add quality button onClick listener
    public void addQualityButtonOnClick(View view) {
        //get quality from designOneAddMeterTextField
        String quality = Objects.requireNonNull(addQualityTextField.getEditText()).getText().toString();

        //check if meter is empty
        if (quality.isEmpty()) {
            showErrorInTextField(addQualityTextField, "Enter Quality");
            return;
        }

        //remove error if applied
        addQualityTextField.setErrorEnabled(false);

        //clear EditText
        addQualityTextField.getEditText().getText().clear();

        //add quality to quality table
        addQualityToTable(qualityListTableLayout, quality, qualityList);

        //add quality to database
        getQualityListDatabaseReference().child(quality).setValue(quality);
    }

    //add data to table received from enter quality EditText
    @SuppressLint("ClickableViewAccessibility")
    private void addQualityToTable(TableLayout tableLayout, String quality, ArrayList<String> qualityList) {
        //inflate row
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.add_quality_table_row, qualityActivityParent, false);

        //initialize TableRow text views
        TextView SNoTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView qualityTableColumn = tableRow.findViewById(R.id.qualityTableColumn);

        //add data to table row
        SNoTableColumn.setText(String.valueOf(tableLayout.getChildCount()));
        qualityTableColumn.setText(quality);

        //add quality in quality list
        qualityList.add(quality);

        //delete row onClick
        qualityTableColumn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= qualityTableColumn.getRight() - qualityTableColumn.getTotalPaddingRight()) {
                    //get TableRow to be removed
                    TableRow tableRow1 = (TableRow) qualityTableColumn.getParent();

                    //get meter from TableRow
                    String qualityToBeRemoved = qualityTableColumn.getText().toString();

                    //show dialog to delete quality
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Do you want to delete " + qualityToBeRemoved)
                            .setTitle("Delete Quality")
                            .setCancelable(true)
                            .setPositiveButton("Delete", (dialog, which) -> {
                                //remove meter from meterList
                                qualityList.remove(qualityToBeRemoved);

                                //remove TableRow from TableLayout
                                tableLayout.removeView(tableRow1);

                                //remove quality from database
                                getQualityListDatabaseReference().child(qualityToBeRemoved).removeValue();

                            }).setNegativeButton("Cancel", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            return true;
        });

        //add row in table
        tableLayout.addView(tableRow);
    }
}