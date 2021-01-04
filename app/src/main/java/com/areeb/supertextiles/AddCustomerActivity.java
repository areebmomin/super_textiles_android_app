package com.areeb.supertextiles;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.areeb.supertextiles.models.Customer;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Objects;

import static com.areeb.supertextiles.AddChallanActivity.showErrorInTextField;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getAllCustomersReference;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getDeliveryAddressDatabaseReference;
import static com.areeb.supertextiles.LoginActivity.hideSoftKeyboard;

public class AddCustomerActivity extends AppCompatActivity {

    TextInputLayout nameAddCustomerTextField,addressAddCustomerTextField, GSTNoAddCustomerTextField,
            deliveryAddressAddCustomerTextField;
    TableLayout addCustomerTableLayout;
    DatabaseReference addCustomerDatabaseReference, addDeliveryAddressDatabaseReference;
    ArrayList<String> deliveryAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Customer");
        setContentView(R.layout.activity_add_customer);

        //initializing xml view
        nameAddCustomerTextField = findViewById(R.id.nameAddCustomerTextField);
        addressAddCustomerTextField = findViewById(R.id.addressAddCustomerTextField);
        GSTNoAddCustomerTextField = findViewById(R.id.GSTNoAddCustomerTextField);
        deliveryAddressAddCustomerTextField = findViewById(R.id.deliveryAddressAddCustomerTextField);
        addCustomerTableLayout = findViewById(R.id.addCustomerTableLayout);

        //initialize DatabaseReference
        addCustomerDatabaseReference = getAllCustomersReference();
        addDeliveryAddressDatabaseReference = getDeliveryAddressDatabaseReference();

        //initialize deliveryAddressList
        deliveryAddressList = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_customer_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.done))) {
            //call method addNewCustomer
            addNewCustomer();
        }

        return super.onOptionsItemSelected(item);
    }

    //method to create new customer
    private void addNewCustomer() {

        //check for empty fields
        String name = Objects.requireNonNull(nameAddCustomerTextField.getEditText()).getText().toString();
        String address = Objects.requireNonNull(addressAddCustomerTextField.getEditText()).getText().toString();
        String GST = Objects.requireNonNull(GSTNoAddCustomerTextField.getEditText()).getText().toString();

        //remove error of AddCustomer text fields
        nameAddCustomerTextField.setErrorEnabled(false);
        addressAddCustomerTextField.setErrorEnabled(false);
        GSTNoAddCustomerTextField.setErrorEnabled(false);
        deliveryAddressAddCustomerTextField.setErrorEnabled(false);

        if (name.isEmpty()) {
            showErrorInTextField(nameAddCustomerTextField, "Name required");
            return;
        }
        else if(address.isEmpty()) {
            showErrorInTextField(addressAddCustomerTextField, "Address required");
            return;
        }
        else if (GST.isEmpty()) {
            showErrorInTextField(GSTNoAddCustomerTextField, "GST required");
            return;
        }

        //check for empty delivery address
        if (addCustomerTableLayout.getChildCount() < 2) {
            showErrorInTextField(deliveryAddressAddCustomerTextField, "Delivery address required");
            return;
        }

        //empty all TextFields
        nameAddCustomerTextField.getEditText().getText().clear();
        addressAddCustomerTextField.getEditText().getText().clear();
        GSTNoAddCustomerTextField.getEditText().getText().clear();

        //hide keyboard if open
        hideSoftKeyboard(this, nameAddCustomerTextField);

        //add customer data to database goes here
        //initialize Customer object
        Customer customer = new Customer();

        //generate customer ID
        String id = String.valueOf(System.currentTimeMillis());

        //add values to customer object
        customer.setId(id);
        customer.setName(name);
        customer.setAddress(address);
        customer.setGSTNo(GST);

        //store customer object to database
        addCustomerDatabaseReference.child(id).setValue(customer).addOnSuccessListener(aVoid ->
                Toast.makeText(this, "Customer added", Toast.LENGTH_LONG).show());

        //store deliveryAddressList to database
        addDeliveryAddressDatabaseReference.child(id).setValue(deliveryAddressList);

        //finish AddCustomerActivity
        finish();
    }

    //addDeliveryAddress button onClick method
    @SuppressLint("ClickableViewAccessibility")
    public void addDeliveryAddress(View view) {
        //get deliveryAddress from EditText
        String deliveryAddress = Objects.requireNonNull(deliveryAddressAddCustomerTextField.getEditText()).getText().toString();

        //check if deliveryAddress is empty
        if (deliveryAddress.isEmpty()) {
            showErrorInTextField(deliveryAddressAddCustomerTextField, "Delivery Address required");
            return;
        }

        //remove error message from EditText
        deliveryAddressAddCustomerTextField.setErrorEnabled(false);

        //empty deliveryAddressAddCustomerTextField
        deliveryAddressAddCustomerTextField.getEditText().getText().clear();

        //initialize new row in table
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.add_customer_table_row, null);

        //initialize TextView as Table column
        TextView SRNOTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView addressTableColumn = tableRow.findViewById(R.id.addressTableColumn);

        //add data in new row
        SRNOTableColumn.setText(String.valueOf(addCustomerTableLayout.getChildCount()));
        addressTableColumn.setText(deliveryAddress);

        //delete icon onClick listener
        addressTableColumn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= addressTableColumn.getRight() - addressTableColumn.getTotalPaddingRight()) {
                    //get TableRow to be removed
                    TableRow tableRow1 = (TableRow) addressTableColumn.getParent();

                    //get address from TableRow
                    String addressToBeRemoved = addressTableColumn.getText().toString();

                    //remove address from deliveryAddressList
                    deliveryAddressList.remove(addressToBeRemoved);

                    //remove TableRow from TableLayout
                    addCustomerTableLayout.removeView(tableRow1);
                }
            }
            return true;
        });

        //add deliveryAddress to deliveryAddressList
        deliveryAddressList.add(deliveryAddress);

        //add new TableRow to TableLayout
        addCustomerTableLayout.addView(tableRow);
    }
}