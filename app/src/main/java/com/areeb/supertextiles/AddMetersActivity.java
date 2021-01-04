package com.areeb.supertextiles;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static com.areeb.supertextiles.AboutUs.copyDataToClipBoard;
import static com.areeb.supertextiles.ChallanDetailsActivity.TOTAL_PIECES;
import static com.areeb.supertextiles.ChallanDetailsActivity.TOTAL_METERS;
import static com.areeb.supertextiles.ChallanDetailsActivity.FOLD;
import static com.areeb.supertextiles.ChallanDetailsActivity.NO_OF_DESIGNS;
import static com.areeb.supertextiles.AddChallanActivity.showErrorInTextField;

public class AddMetersActivity extends AppCompatActivity {

    LinearLayout designOneLinearLayout, designTwoLinearLayout, designThreeLinearLayout, designFourLinearLayout;
    TextInputLayout designOneAddMeterTextField, designTwoAddMeterTextField, designThreeAddMeterTextField, designFourAddMeterTextField,
            designNoOneTextField, designNoTwoTextField, designNoThreeTextField, designNoFourTextField,
            designOneColorTextField, designTwoColorTextField, designThreeColorTextField, designFourColorTextField;
    TableLayout designOneTableLayout, designTwoTableLayout, designThreeTableLayout, designFourTableLayout;
    TextView totalPiecesValueTextView, totalMetersValueTextView;
    double totalMeters = 0;
    int totalRemainingPieces = 0, numberOfDesigns = 1;
    PreferenceManager preferenceManager;
    ArrayList<String> design1MeterList, design2MeterList, design3MeterList, design4MeterList;

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

        //get data from intent
        if (getIntent() != null) {
            //set value in totalPiecesValueTextView
            String totalPiecesString = getIntent().getStringExtra(TOTAL_PIECES);
            totalPiecesValueTextView.setText(totalPiecesString);

            //set totalMetersValueTextView
            totalMeters = Double.parseDouble(getIntent().getStringExtra(TOTAL_METERS));
            totalMetersValueTextView.setText(String.valueOf(totalMeters));

            //set number of designs
            numberOfDesigns = getIntent().getIntExtra(NO_OF_DESIGNS, 1);

            //check radio number based on number of designs
            checkRadioButton(numberOfDesigns);

            //update totalPieces
            totalRemainingPieces = Integer.parseInt(totalPiecesString);
        }

        //initialize preferenceManager
        preferenceManager = new PreferenceManager(this);

        //initialize ArrayList's
        design1MeterList = new ArrayList<>();
        design2MeterList = new ArrayList<>();
        design3MeterList = new ArrayList<>();
        design4MeterList = new ArrayList<>();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.done))) {

            //check design number and color for every design is added
            //return false if all design number and color not added
            if (!checkDesignNumberAndColor()) return false;

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
            }
            else if (title.equals(getString(R.string._2))) {
                showDesignFields(View.VISIBLE, View.GONE, View.GONE);
                numberOfDesigns = 2;
            }
            else if (title.equals(getString(R.string._3))) {
                showDesignFields(View.VISIBLE, View.VISIBLE, View.GONE);
                numberOfDesigns = 3;
            }
            else if (title.equals(getString(R.string._4))) {
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
        getMeterAndAddMeterToTable(designOneAddMeterTextField, designOneTableLayout, design1MeterList);
    }

    //design two add button onClick method
    public void designTwoAddButtonOnClick(View view) {
        getMeterAndAddMeterToTable(designTwoAddMeterTextField, designTwoTableLayout, design2MeterList);
    }

    //design three add button onClick method
    public void designThreeAddButtonOnClick(View view) {
        getMeterAndAddMeterToTable(designThreeAddMeterTextField, designThreeTableLayout, design3MeterList);
    }

    //design four add button onClick method
    public void designFourAddButtonOnClick(View view) {
        getMeterAndAddMeterToTable(designFourAddMeterTextField, designFourTableLayout, design4MeterList);
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
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.add_meter_table_row, null);

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

    //method to check if design number and color for design is added
    //return true if all data added
    //return false if any field is empty
    private boolean checkDesignNumberAndColor () {
        if (numberOfDesigns == 1) {
            //check for design 1
            return checkEmptyFieldForDesign1();
        }
        else if (numberOfDesigns == 2) {
            //check for design 1 and design 2
            return checkEmptyFieldForDesign12();
        }
        else if (numberOfDesigns == 3) {
            //check for design 1,  design 2 and design 3
            return checkEmptyFieldForDesign123();
        }
        else if (numberOfDesigns == 4) {
            //check for design 1,  design 2, design 3 and design 4
            return checkEmptyFieldForDesign1234();
        }

        return false;
    }

    //check if design number and color field is empty for design 1
    private boolean checkEmptyFieldForDesign1() {
        //for Design No. field
        String designNumber = Objects.requireNonNull(designNoOneTextField.getEditText()).getText().toString();
        if (designNumber.isEmpty()) {
            showErrorInTextField(designNoOneTextField, "Design No. required");
            return false;
        }
        //remove error if data is entered
        designNoOneTextField.setErrorEnabled(false);

        //for color field
        String color = Objects.requireNonNull(designOneColorTextField.getEditText()).getText().toString();
        if (color.isEmpty()) {
            showErrorInTextField(designOneColorTextField, "Color required");
            return false;
        }
        //remove error if data is entered
        designOneColorTextField.setErrorEnabled(false);

        return true;
    }

    //check if design number and color field is empty for design 2
    private boolean checkEmptyFieldForDesign12() {
        //check for design 1 empty fields
        boolean design1Status = checkEmptyFieldForDesign1();

        if (!design1Status) return false;

        //for Design No. field
        String designNumber = Objects.requireNonNull(designNoTwoTextField.getEditText()).getText().toString();
        if (designNumber.isEmpty()) {
            showErrorInTextField(designNoTwoTextField, "Design No. required");
            return false;
        }
        //remove error if data is entered
        designNoTwoTextField.setErrorEnabled(false);

        //for color field
        String color = Objects.requireNonNull(designTwoColorTextField.getEditText()).getText().toString();
        if (color.isEmpty()) {
            showErrorInTextField(designTwoColorTextField, "Color required");
            return false;
        }
        //remove error if data is entered
        designTwoColorTextField.setErrorEnabled(false);

        return true;
    }

    //check if design number and color field is empty for design 3
    private boolean checkEmptyFieldForDesign123() {
        //check for design 1 and design 2 empty fields
        boolean design1And2Status = checkEmptyFieldForDesign12();

        if (!design1And2Status) return false;

        //for Design No. field
        String designNumber = Objects.requireNonNull(designNoThreeTextField.getEditText()).getText().toString();
        if (designNumber.isEmpty()) {
            showErrorInTextField(designNoThreeTextField, "Design No. required");
            return false;
        }
        //remove error if data is entered
        designNoThreeTextField.setErrorEnabled(false);

        //for color field
        String color = Objects.requireNonNull(designThreeColorTextField.getEditText()).getText().toString();
        if (color.isEmpty()) {
            showErrorInTextField(designThreeColorTextField, "Color required");
            return false;
        }
        //remove error if data is entered
        designThreeColorTextField.setErrorEnabled(false);

        return true;
    }

    //check if design number and color field is empty for design 4
    private boolean checkEmptyFieldForDesign1234() {
        //check for design 1, design 2 and design 3 empty fields
        boolean design12And3Status = checkEmptyFieldForDesign123();

        if (!design12And3Status) return false;

        //for Design No. field
        String designNumber = Objects.requireNonNull(designNoFourTextField.getEditText()).getText().toString();
        if (designNumber.isEmpty()) {
            showErrorInTextField(designNoFourTextField, "Design No. required");
            return false;
        }
        //remove error if data is entered
        designNoFourTextField.setErrorEnabled(false);

        //for color field
        String color = Objects.requireNonNull(designFourColorTextField.getEditText()).getText().toString();
        if (color.isEmpty()) {
            showErrorInTextField(designFourColorTextField, "Color required");
            return false;
        }
        //remove error if data is entered
        designFourColorTextField.setErrorEnabled(false);

        return true;
    }

    //method to check that meter is added for every design
    //return true when meter is added for every design
    //return false if meter is not added for every design
    private boolean checkMeterForEveryDesign() {
        if (numberOfDesigns == 1) {
            return true;
        }
        else if (numberOfDesigns == 2) {
            return design1MeterList.size() > 0 && design2MeterList.size() > 0;
        }
        else if (numberOfDesigns == 3) {
            return design1MeterList.size() > 0 && design2MeterList.size() > 0 && design3MeterList.size() > 0;
        }
        else if (numberOfDesigns == 4) {
            return design1MeterList.size() > 0 && design2MeterList.size() > 0 && design3MeterList.size() > 0 && design4MeterList.size() > 0;
        }

        return false;
    }

    //method to store design data and meters in SharedPreference
    private void storeDesignDataToSharedPreference() {
        if (numberOfDesigns == 1) {
            storeDesign1DataInSharedPreference();
        }
        else if (numberOfDesigns == 2) {
            storeDesign1DataInSharedPreference();
            storeDesign2DataInSharedPreference();
        }
        else if (numberOfDesigns == 3) {
            storeDesign1DataInSharedPreference();
            storeDesign2DataInSharedPreference();
            storeDesign3DataInSharedPreference();
        }
        else if (numberOfDesigns == 4) {
            storeDesign1DataInSharedPreference();
            storeDesign2DataInSharedPreference();
            storeDesign3DataInSharedPreference();
            storeDesign4DataInSharedPreference();
        }
    }

    //method to store design 1 data and meters
    private void storeDesign1DataInSharedPreference() {
        //store design 1 no
        preferenceManager.setDesign1No(Objects.requireNonNull(designNoOneTextField.getEditText()).getText().toString());

        //store design 1 color
        preferenceManager.setDesign1Color(Objects.requireNonNull(designOneColorTextField.getEditText()).getText().toString());

        //store design 1 meter list
        preferenceManager.setDesign1MeterList(new LinkedHashSet<>(design1MeterList));
    }

    //method to store design 2 data and meters
    private void storeDesign2DataInSharedPreference() {
        //store design 1 no
        preferenceManager.setDesign2No(Objects.requireNonNull(designNoTwoTextField.getEditText()).getText().toString());

        //store design 1 color
        preferenceManager.setDesign2Color(Objects.requireNonNull(designTwoColorTextField.getEditText()).getText().toString());

        //store design 1 meter list
        preferenceManager.setDesign2MeterList(new LinkedHashSet<>(design2MeterList));
    }

    //method to store design 3 data and meters
    private void storeDesign3DataInSharedPreference() {
        //store design 1 no
        preferenceManager.setDesign3No(Objects.requireNonNull(designNoThreeTextField.getEditText()).getText().toString());

        //store design 1 color
        preferenceManager.setDesign3Color(Objects.requireNonNull(designThreeColorTextField.getEditText()).getText().toString());

        //store design 1 meter list
        preferenceManager.setDesign3MeterList(new LinkedHashSet<>(design3MeterList));
    }

    //method to store design 4 data and meters
    private void storeDesign4DataInSharedPreference() {
        //store design 1 no
        preferenceManager.setDesign4No(Objects.requireNonNull(designNoFourTextField.getEditText()).getText().toString());

        //store design 1 color
        preferenceManager.setDesign4Color(Objects.requireNonNull(designFourColorTextField.getEditText()).getText().toString());

        //store design 1 meter list
        preferenceManager.setDesign4MeterList(new LinkedHashSet<>(design4MeterList));
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
}