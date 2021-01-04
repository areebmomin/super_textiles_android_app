package com.areeb.supertextiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.areeb.supertextiles.models.Customer;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Objects;

import static com.areeb.supertextiles.AddChallanActivity.showErrorInTextField;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getAllCustomersReference;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getDeliveryAddressDatabaseReference;
import static com.areeb.supertextiles.LoginActivity.hideSoftKeyboard;
import static com.areeb.supertextiles.ViewCustomerActivity.CUSTOMER_ADDRESS;
import static com.areeb.supertextiles.ViewCustomerActivity.CUSTOMER_DELIVERY_ADDRESS_LIST;
import static com.areeb.supertextiles.ViewCustomerActivity.CUSTOMER_GST_NO;
import static com.areeb.supertextiles.ViewCustomerActivity.CUSTOMER_ID;
import static com.areeb.supertextiles.ViewCustomerActivity.CUSTOMER_NAME;

public class EditCustomerActivity extends AppCompatActivity {

    TextInputLayout nameEditCustomerTextField, addressEditCustomerTextField, GSTNoEditCustomerTextField,
            deliveryAddressEditCustomerTextField;
    TableLayout editCustomerTableLayout;
    String customerID, customerName, customerAddress, customerGSTNo;
    ArrayList<String> deliveryAddressList;
    DatabaseReference editCustomerDatabaseReference, editDeliveryAddressDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Customer");
        setContentView(R.layout.activity_edit_customer);

        //initializing xml view
        nameEditCustomerTextField = findViewById(R.id.nameEditCustomerTextField);
        addressEditCustomerTextField = findViewById(R.id.addressEditCustomerTextField);
        GSTNoEditCustomerTextField = findViewById(R.id.GSTNoEditCustomerTextField);
        deliveryAddressEditCustomerTextField = findViewById(R.id.deliveryAddressEditCustomerTextField);
        editCustomerTableLayout = findViewById(R.id.editCustomerTableLayout);

        //initialize DatabaseReference
        editCustomerDatabaseReference = getAllCustomersReference();
        editDeliveryAddressDatabaseReference = getDeliveryAddressDatabaseReference();

        //initialize deliveryAddressList
        deliveryAddressList = new ArrayList<>();

        //get data from intent
        if (getIntent() != null) {

            //store intent data into variables
            customerID = getIntent().getStringExtra(CUSTOMER_ID);
            customerName = getIntent().getStringExtra(CUSTOMER_NAME);
            customerAddress = getIntent().getStringExtra(CUSTOMER_ADDRESS);
            customerGSTNo = getIntent().getStringExtra(CUSTOMER_GST_NO);
            deliveryAddressList = getIntent().getStringArrayListExtra(CUSTOMER_DELIVERY_ADDRESS_LIST);

            //set intent data to EditText
            Objects.requireNonNull(nameEditCustomerTextField.getEditText()).setText(customerName);
            Objects.requireNonNull(addressEditCustomerTextField.getEditText()).setText(customerAddress);
            Objects.requireNonNull(GSTNoEditCustomerTextField.getEditText()).setText(customerGSTNo);

            //add delivery address list to table
            for (String deliveryAddress : deliveryAddressList) {
                addDeliveryAddressToTable(deliveryAddress);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_customer_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.done))) {
            editCustomer();
        }

        return super.onOptionsItemSelected(item);
    }

    //method to create new customer
    private void editCustomer() {

        //check for empty fields
        String newName = Objects.requireNonNull(nameEditCustomerTextField.getEditText()).getText().toString();
        String newAddress = Objects.requireNonNull(addressEditCustomerTextField.getEditText()).getText().toString();
        String newGST = Objects.requireNonNull(GSTNoEditCustomerTextField.getEditText()).getText().toString();

        //remove error of AddCustomer text fields
        nameEditCustomerTextField.setErrorEnabled(false);
        addressEditCustomerTextField.setErrorEnabled(false);
        GSTNoEditCustomerTextField.setErrorEnabled(false);
        deliveryAddressEditCustomerTextField.setErrorEnabled(false);

        if (newName.isEmpty()) {
            showErrorInTextField(nameEditCustomerTextField, "Name required");
            return;
        }
        else if(newAddress.isEmpty()) {
            showErrorInTextField(addressEditCustomerTextField, "Address required");
            return;
        }
        else if (newGST.isEmpty()) {
            showErrorInTextField(GSTNoEditCustomerTextField, "GST required");
            return;
        }

        //check for empty delivery address
        if (editCustomerTableLayout.getChildCount() < 2) {
            showErrorInTextField(deliveryAddressEditCustomerTextField, "Delivery address required");
            return;
        }

        //empty all TextFields
        nameEditCustomerTextField.getEditText().getText().clear();
        addressEditCustomerTextField.getEditText().getText().clear();
        GSTNoEditCustomerTextField.getEditText().getText().clear();

        //hide keyboard if open
        hideSoftKeyboard(this, nameEditCustomerTextField);

        //add customer data to database
        //initialize Customer object
        Customer customer = new Customer();

        //add values to customer object
        customer.setId(customerID);
        customer.setName(newName);
        customer.setAddress(newAddress);
        customer.setGSTNo(newGST);

        //store customer object to database
        editCustomerDatabaseReference.child(customerID).setValue(customer).addOnSuccessListener(aVoid ->
                Toast.makeText(this, "Customer edited", Toast.LENGTH_LONG).show());

        //store deliveryAddressList to database
        editDeliveryAddressDatabaseReference.child(customerID).setValue(deliveryAddressList);

        //finish AddCustomerActivity
        finish();
    }

    //addDeliveryAddress button onClick method
    public void addDeliveryAddress(View view) {
        //get deliveryAddress from EditText
        String deliveryAddress = Objects.requireNonNull(deliveryAddressEditCustomerTextField.getEditText()).getText().toString();

        //check if deliveryAddress is empty
        if (deliveryAddress.isEmpty()) {
            showErrorInTextField(deliveryAddressEditCustomerTextField, "Delivery Address required");
            return;
        }

        //remove error message from EditText
        deliveryAddressEditCustomerTextField.setErrorEnabled(false);

        //empty deliveryAddressAddCustomerTextField
        deliveryAddressEditCustomerTextField.getEditText().getText().clear();

        //add deliveryAddress to deliveryAddressList
        deliveryAddressList.add(deliveryAddress);

        //add address to table
        addDeliveryAddressToTable(deliveryAddress);
    }

    //method to add delivery address to table
    @SuppressLint("ClickableViewAccessibility")
    private void addDeliveryAddressToTable (String deliveryAddress) {
        //initialize new row in table
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.add_customer_table_row, null);

        //initialize TextView as Table column
        TextView SRNOTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView addressTableColumn = tableRow.findViewById(R.id.addressTableColumn);

        //add data in new row
        SRNOTableColumn.setText(String.valueOf(editCustomerTableLayout.getChildCount()));
        addressTableColumn.setText(deliveryAddress);

        //delete icon onClick listener
        addressTableColumn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= addressTableColumn.getRight() - addressTableColumn.getTotalPaddingRight()) {
                    //get TableRow to be removed
                    TableRow currentRow = (TableRow) addressTableColumn.getParent();

                    //get address from TableRow
                    String addressToBeRemoved = addressTableColumn.getText().toString();

                    //remove address from deliveryAddressList
                    deliveryAddressList.remove(addressToBeRemoved);

                    //remove TableRow from TableLayout
                    editCustomerTableLayout.removeView(currentRow);
                }
            }
            return true;
        });

        editCustomerTableLayout.addView(tableRow);
    }
}