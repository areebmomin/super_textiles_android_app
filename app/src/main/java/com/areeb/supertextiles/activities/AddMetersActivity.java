package com.areeb.supertextiles.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.areeb.supertextiles.utilities.PreferenceManager;
import com.areeb.supertextiles.R;
import com.areeb.supertextiles.models.Design;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

import static com.areeb.supertextiles.activities.AboutUs.copyDataToClipBoard;
import static com.areeb.supertextiles.activities.AddChallanActivity.showErrorInTextField;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_1_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_2_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_3_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.DESIGN_4_OBJECT;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.NO_OF_DESIGNS;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.TOTAL_METERS;
import static com.areeb.supertextiles.activities.ChallanDetailsActivity.TOTAL_PIECES;

public class AddMetersActivity extends AppCompatActivity {

    LinearLayout designOneLinearLayout, designTwoLinearLayout, designThreeLinearLayout, designFourLinearLayout;
    TextInputLayout designOneAddMeterTextField, designTwoAddMeterTextField, designThreeAddMeterTextField, designFourAddMeterTextField,
            designNoOneTextField, designNoTwoTextField, designNoThreeTextField, designNoFourTextField,
            designOneColorTextField, designTwoColorTextField, designThreeColorTextField, designFourColorTextField;
    TableLayout designOneTableLayout, designTwoTableLayout, designThreeTableLayout, designFourTableLayout;
    TextView totalPiecesValueTextView, totalMetersValueTextView;
    ConstraintLayout addMeterParent;
    double totalMeters = 0;
    int totalRemainingPieces = 0, numberOfDesigns = 1;
    PreferenceManager preferenceManager;
    Design design1, design2, design3, design4;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Meters");
        setContentView(R.layout.activity_add_meters);

        //initialize xml view
        totalPiecesValueTextView = findViewById(R.id.totalPiecesValueTextView);
        totalMetersValueTextView = findViewById(R.id.totalMetersValueTextView);
        designOneLinearLayout = findViewById(R.id.designOneLinearLayout);
        designTwoLinearLayout = findViewById(R.id.designTwoLinearLayout);
        designThreeLinearLayout = findViewById(R.id.designThreeLinearLayout);
        designFourLinearLayout = findViewById(R.id.designFourLinearLayout);
        designOneAddMeterTextField = findViewById(R.id.designOneAddMeterTextField);
        designTwoAddMeterTextField = findViewById(R.id.designTwoAddMeterTextField);
        designThreeAddMeterTextField = findViewById(R.id.designThreeAddMeterTextField);
        designFourAddMeterTextField = findViewById(R.id.designFourAddMeterTextField);
        designOneTableLayout = findViewById(R.id.designOneTableLayout);
        designTwoTableLayout = findViewById(R.id.designTwoTableLayout);
        designThreeTableLayout = findViewById(R.id.designThreeTableLayout);
        designFourTableLayout = findViewById(R.id.designFourTableLayout);
        designNoOneTextField = findViewById(R.id.designNoOneTextField);
        designNoTwoTextField = findViewById(R.id.designNoTwoTextField);
        designNoThreeTextField = findViewById(R.id.designNoThreeTextField);
        designNoFourTextField = findViewById(R.id.designNoFourTextField);
        designOneColorTextField = findViewById(R.id.designOneColorTextField);
        designTwoColorTextField = findViewById(R.id.designTwoColorTextField);
        designThreeColorTextField = findViewById(R.id.designThreeColorTextField);
        designFourColorTextField = findViewById(R.id.designFourColorTextField);
        addMeterParent = findViewById(R.id.addMeterParent);

        //initialize preferenceManager
        preferenceManager = new PreferenceManager(this);

        //initialize Design objects
        design1 = new Design();
        design2 = new Design();
        design3 = new Design();
        design4 = new Design();

        //initialize Gson object
        gson = new Gson();

        //get data from intent
        if (getIntent() != null) {
            //set value in totalPiecesValueTextView
            String totalPiecesString = getIntent().getStringExtra(TOTAL_PIECES);
            totalPiecesValueTextView.setText(totalPiecesString);

            //update totalPieces
            if (totalPiecesString != null) {
                totalRemainingPieces = Integer.parseInt(totalPiecesString);
            }

            //set totalMetersValueTextView if not null
            String totalMeterString = getIntent().getStringExtra(TOTAL_METERS);
            if (totalMeterString != null) {
                totalMeters = Double.parseDouble(totalMeterString);
                totalMetersValueTextView.setText(String.valueOf(totalMeters));
            }

            //set number of designs
            int numberOfDesignsTemp = getIntent().getIntExtra(NO_OF_DESIGNS, 1);
            if (numberOfDesignsTemp == 2 || numberOfDesignsTemp == 3 || numberOfDesignsTemp == 4){
                numberOfDesigns = numberOfDesignsTemp;
            }

            //check radio number based on number of designs
            checkRadioButton(numberOfDesigns);

            //get design data from intent
            getDesignDataAndPutInEditText(getIntent());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //totalPiecesValueTextView onLongClick listener
        totalPiecesValueTextView.setOnLongClickListener(v -> {
            copyDataToClipBoard(totalPiecesValueTextView.getTag().toString(), totalPiecesValueTextView.getText().toString(), AddMetersActivity.this);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_meters_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals(getString(R.string.done))) {

            //check design number and color for every design is added
            //return false if all design number and color not added
            //if (!checkDesignNumberAndColor()) return false;

            //check if meters are added for all pieces
            if (totalRemainingPieces > 0) {
                Toast.makeText(this, "Add Meter for every Piece", Toast.LENGTH_LONG).show();
                return false;
            }

            //check that meters is added for every design
            if (!checkMeterForEveryDesign()) {
                Toast.makeText(this, "Add Meter for every design", Toast.LENGTH_LONG).show();
                return false;
            }

            //check if meters are more than pieces
            if (totalRemainingPieces < 0) {
                Toast.makeText(this, "Meters are more than pieces", Toast.LENGTH_LONG).show();
                return false;
            }

            //add totalMeters and numberOfDesign data to preferenceManager
            preferenceManager.setTotalMeters(String.valueOf(totalMeters));
            preferenceManager.setNumberOfDesigns(numberOfDesigns);

            //add design objects to preferenceManager
            storeDesignDataToSharedPreference();

            //close AddMetersActivity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    //Radio button onClick method
    public void onRadioButtonClicked(View view) {
        //initialize RadioButton
        RadioButton radioButton = (RadioButton) view;

        //get RadioButton title
        String title = radioButton.getText().toString();

        //get RadioButton status
        boolean isChecked = radioButton.isChecked();

        if (isChecked) {
            if (title.equals(getString(R.string._1))) {
                showDesignFields(View.GONE, View.GONE, View.GONE);
                numberOfDesigns = 1;
            } else if (title.equals(getString(R.string._2))) {
                showDesignFields(View.VISIBLE, View.GONE, View.GONE);
                numberOfDesigns = 2;
            } else if (title.equals(getString(R.string._3))) {
                showDesignFields(View.VISIBLE, View.VISIBLE, View.GONE);
                numberOfDesigns = 3;
            } else if (title.equals(getString(R.string._4))) {
                showDesignFields(View.VISIBLE, View.VISIBLE, View.VISIBLE);
                numberOfDesigns = 4;
            }
        }
    }

    //method to show required number of design field
    private void showDesignFields(int designTwoVisibility, int designThreeVisibility, int designFourVisibility) {
        designTwoLinearLayout.setVisibility(designTwoVisibility);
        designThreeLinearLayout.setVisibility(designThreeVisibility);
        designFourLinearLayout.setVisibility(designFourVisibility);
    }

    //design one add button onClick method
    public void designOneAddButtonOnClick(View view) {
        getMeterAndAddMeterToTable(designOneAddMeterTextField, designOneTableLayout, design1.getMeterList());
    }

    //design two add button onClick method
    public void designTwoAddButtonOnClick(View view) {
        getMeterAndAddMeterToTable(designTwoAddMeterTextField, designTwoTableLayout, design2.getMeterList());
    }

    //design three add button onClick method
    public void designThreeAddButtonOnClick(View view) {
        getMeterAndAddMeterToTable(designThreeAddMeterTextField, designThreeTableLayout, design3.getMeterList());
    }

    //design four add button onClick method
    public void designFourAddButtonOnClick(View view) {
        getMeterAndAddMeterToTable(designFourAddMeterTextField, designFourTableLayout, design4.getMeterList());
    }

    //get meter data from enter meter EditText
    private void getMeterAndAddMeterToTable(TextInputLayout textInputLayout, TableLayout tableLayout, ArrayList<String> meterList) {

        //check if totalPieces limit exceeded
        if (totalRemainingPieces <= 0) {
            Toast.makeText(this, "Total Piece limit exceeded", Toast.LENGTH_LONG).show();
            return;
        }

        //get meter from designOneAddMeterTextField
        String meter = Objects.requireNonNull(textInputLayout.getEditText()).getText().toString();

        //check if meter is empty
        if (meter.isEmpty()) {
            showErrorInTextField(textInputLayout, "Enter meter");
            return;
        }

        //remove error if applied
        textInputLayout.setErrorEnabled(false);

        //clear EditText
        textInputLayout.getEditText().getText().clear();

        //convert string to double and vice versa
        double meterDouble = Double.parseDouble(meter);
        meter = String.valueOf(meterDouble);

        //add meter to table
        addMeterToTable(tableLayout, meter, meterList);

        //add meter to meterList
        meterList.add(meter);

        //add meter to totalMeters
        totalMeters += meterDouble;

        //update totalMetersValueTextView
        totalMetersValueTextView.setText(String.valueOf(totalMeters));

        //decrease totalPieces
        --totalRemainingPieces;
    }

    //add data to table received from enter meter EditText
    @SuppressLint("ClickableViewAccessibility")
    private void addMeterToTable(TableLayout tableLayout, String meter, ArrayList<String> meterList) {
        //inflate row
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.add_meter_table_row, addMeterParent, false);

        //initialize TableRow text views
        TextView SNoTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView meterTableColumn = tableRow.findViewById(R.id.meterTableColumn);

        //add data to table row
        SNoTableColumn.setText(String.valueOf(tableLayout.getChildCount()));
        meterTableColumn.setText(meter);

        //delete row onClick
        meterTableColumn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= meterTableColumn.getRight() - meterTableColumn.getTotalPaddingRight()) {
                    //get TableRow to be removed
                    TableRow tableRow1 = (TableRow) meterTableColumn.getParent();

                    //get meter from TableRow
                    String meterToBeRemoved = meterTableColumn.getText().toString();

                    //remove meter from meterList
                    meterList.remove(meterToBeRemoved);

                    //decrease totalMeters
                    totalMeters -= Double.parseDouble(meterToBeRemoved);

                    //update totalMetersValueTextView
                    totalMetersValueTextView.setText(String.valueOf(totalMeters));

                    //increase totalPieces
                    ++totalRemainingPieces;

                    //remove TableRow from TableLayout
                    tableLayout.removeView(tableRow1);
                }
            }

            return true;
        });

        //add row in table
        tableLayout.addView(tableRow);
    }

    //method to check that meter is added for every design
    //return true when meter is added for every design
    //return false if meter is not added for every design
    private boolean checkMeterForEveryDesign() {
        if (numberOfDesigns == 1) {
            return true;
        } else if (numberOfDesigns == 2) {
            return design1.getMeterList().size() > 0 && design2.getMeterList().size() > 0;
        } else if (numberOfDesigns == 3) {
            return design1.getMeterList().size() > 0 && design2.getMeterList().size() > 0 && design3.getMeterList().size() > 0;
        } else if (numberOfDesigns == 4) {
            return design1.getMeterList().size() > 0 && design2.getMeterList().size() > 0 && design3.getMeterList().size() > 0 && design4.getMeterList().size() > 0;
        }

        return false;
    }

    //method to store design data and meters in SharedPreference
    private void storeDesignDataToSharedPreference() {
        if (numberOfDesigns == 1) {
            storeDesign1DataInSharedPreference();
        } else if (numberOfDesigns == 2) {
            storeDesign1DataInSharedPreference();
            storeDesign2DataInSharedPreference();
        } else if (numberOfDesigns == 3) {
            storeDesign1DataInSharedPreference();
            storeDesign2DataInSharedPreference();
            storeDesign3DataInSharedPreference();
        } else if (numberOfDesigns == 4) {
            storeDesign1DataInSharedPreference();
            storeDesign2DataInSharedPreference();
            storeDesign3DataInSharedPreference();
            storeDesign4DataInSharedPreference();
        }
    }

    //method to store design 1 data and meters
    private void storeDesign1DataInSharedPreference() {
        //store design 1 object
        design1.setDesign_no(Objects.requireNonNull(designNoOneTextField.getEditText()).getText().toString());
        design1.setDesign_color(Objects.requireNonNull(designOneColorTextField.getEditText()).getText().toString());
        preferenceManager.setDesign1Object(design1);
    }

    //method to store design 2 data and meters
    private void storeDesign2DataInSharedPreference() {
        //store design 2 object
        design2.setDesign_no(Objects.requireNonNull(designNoTwoTextField.getEditText()).getText().toString());
        design2.setDesign_color(Objects.requireNonNull(designTwoColorTextField.getEditText()).getText().toString());
        preferenceManager.setDesign2Object(design2);
    }

    //method to store design 3 data and meters
    private void storeDesign3DataInSharedPreference() {
        //store design 2 object
        design3.setDesign_no(Objects.requireNonNull(designNoThreeTextField.getEditText()).getText().toString());
        design3.setDesign_color(Objects.requireNonNull(designThreeColorTextField.getEditText()).getText().toString());
        preferenceManager.setDesign3Object(design3);
    }

    //method to store design 4 data and meters
    private void storeDesign4DataInSharedPreference() {
        //store design 2 object
        design4.setDesign_no(Objects.requireNonNull(designNoFourTextField.getEditText()).getText().toString());
        design4.setDesign_color(Objects.requireNonNull(designFourColorTextField.getEditText()).getText().toString());
        preferenceManager.setDesign4Object(design4);

    }

    //method to check radio button based on number of designs
    private void checkRadioButton(int numberOfDesigns) {
        if (numberOfDesigns == 1) {
            RadioButton radioButton = findViewById(R.id.radioButtonOne);
            radioButton.setChecked(true);
            radioButton.performClick();
        } else if (numberOfDesigns == 2) {
            RadioButton radioButton = findViewById(R.id.radioButtonTwo);
            radioButton.setChecked(true);
            radioButton.performClick();
        } else if (numberOfDesigns == 3) {
            RadioButton radioButton = findViewById(R.id.radioButtonThree);
            radioButton.setChecked(true);
            radioButton.performClick();
        } else if (numberOfDesigns == 4) {
            RadioButton radioButton = findViewById(R.id.radioButtonFour);
            radioButton.setChecked(true);
            radioButton.performClick();
        }
    }

    //method to get design data from intent and set in EditText based on numberOfDesigns
    private void getDesignDataAndPutInEditText(Intent intent) {
        if (numberOfDesigns == 1) {
            //get design object from intent
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);

            //set design data in design field
            getDesignDataFromIntent(design1, designNoOneTextField, designOneColorTextField, designOneTableLayout);

            //re initialize if design1 is null
            if (design1 == null)
                design1 = new Design();
        }
        else if (numberOfDesigns == 2) {
            //get design object from intent
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);
            design2 = gson.fromJson(intent.getStringExtra(DESIGN_2_OBJECT), Design.class);

            //set design data in design field
            getDesignDataFromIntent(design1, designNoOneTextField, designOneColorTextField, designOneTableLayout);
            getDesignDataFromIntent(design2, designNoTwoTextField, designTwoColorTextField, designTwoTableLayout);
        }
        else if (numberOfDesigns == 3) {
            //get design object from intent
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);
            design2 = gson.fromJson(intent.getStringExtra(DESIGN_2_OBJECT), Design.class);
            design3 = gson.fromJson(intent.getStringExtra(DESIGN_3_OBJECT), Design.class);

            //set design data in design field
            getDesignDataFromIntent(design1, designNoOneTextField, designOneColorTextField, designOneTableLayout);
            getDesignDataFromIntent(design2, designNoTwoTextField, designTwoColorTextField, designTwoTableLayout);
            getDesignDataFromIntent(design3, designNoThreeTextField, designThreeColorTextField, designThreeTableLayout);
        }
        else if (numberOfDesigns == 4) {
            //get design object from intent
            design1 = gson.fromJson(intent.getStringExtra(DESIGN_1_OBJECT), Design.class);
            design2 = gson.fromJson(intent.getStringExtra(DESIGN_2_OBJECT), Design.class);
            design3 = gson.fromJson(intent.getStringExtra(DESIGN_3_OBJECT), Design.class);
            design4 = gson.fromJson(intent.getStringExtra(DESIGN_4_OBJECT), Design.class);

            //set design data in design field
            getDesignDataFromIntent(design1, designNoOneTextField, designOneColorTextField, designOneTableLayout);
            getDesignDataFromIntent(design2, designNoTwoTextField, designTwoColorTextField, designTwoTableLayout);
            getDesignDataFromIntent(design3, designNoThreeTextField, designThreeColorTextField, designThreeTableLayout);
            getDesignDataFromIntent(design4, designNoFourTextField, designFourColorTextField, designFourTableLayout);
        }
    }

    //method to get design data from intent and set in EditText
    private void getDesignDataFromIntent(Design designObject, TextInputLayout designNoTextField, TextInputLayout designColorTextField,
                                         TableLayout tableLayout) {

        //return if designObject is null
        if (designObject == null) return;


        //set data for designNoTextField
        Objects.requireNonNull(designNoTextField.getEditText()).setText(designObject.getDesign_no());

        //set data for designColorTextField
        Objects.requireNonNull(designColorTextField.getEditText()).setText(designObject.getDesign_color());

        //check if meter list is empty
        if (designObject.getMeterList() == null) return;

        //get meter list from intent and add in particular table
        for (String meter : designObject.getMeterList()) {
            //add meter row in table
            addMeterToTable(tableLayout, meter, designObject.getMeterList());

            //decrease totalPieces
            --totalRemainingPieces;
        }

    }
}