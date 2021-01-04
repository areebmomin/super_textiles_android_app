package com.areeb.supertextiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.areeb.supertextiles.AboutUs.getTextViewLongClickListener;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getDeliveryAddressDatabaseReferenceByID;

public class ViewCustomerActivity extends AppCompatActivity {

    public static final String CUSTOMER_ID = "customer_ID";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_ADDRESS = "customer_address";
    public static final String CUSTOMER_GST_NO = "customer_GST_no";
    public static final String CUSTOMER_DELIVERY_ADDRESS_LIST = "customer_delivery_address_list";
    TextView nameViewCustomerValueTextView, addressViewCustomerValueTextView, GStNoViewCustomerValueTextView;
    String customerID, customerName, customerAddress, customerGSTNo;
    DatabaseReference deliveryAddressDatabaseReference;
    TableLayout viewCustomerTableLayout;
    ArrayList<String> deliveryAddressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("View Customer");
        setContentView(R.layout.activity_view_customer);

        //initializing xml views
        nameViewCustomerValueTextView = findViewById(R.id.nameViewCustomerValueTextView);
        addressViewCustomerValueTextView = findViewById(R.id.addressViewCustomerValueTextView);
        GStNoViewCustomerValueTextView = findViewById(R.id.GStNoViewCustomerValueTextView);
        viewCustomerTableLayout = findViewById(R.id.viewCustomerTableLayout);

        //copy text to clipboard when onLongPress on TextView
        nameViewCustomerValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        addressViewCustomerValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        GStNoViewCustomerValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));

        //initializing string variables
        customerID = "";
        customerName = "";
        customerAddress = "";
        customerGSTNo = "";
        deliveryAddressList = new ArrayList<>();

        //get data from intent
        if (getIntent() != null) {
            //assign intent data to variables
            customerID = getIntent().getStringExtra(CUSTOMER_ID);
            customerName = getIntent().getStringExtra(CUSTOMER_NAME);
            customerAddress = getIntent().getStringExtra(CUSTOMER_ADDRESS);
            customerGSTNo = getIntent().getStringExtra(CUSTOMER_GST_NO);

            //set intent data to TextViews
            nameViewCustomerValueTextView.setText(customerName);
            addressViewCustomerValueTextView.setText(customerAddress);
            GStNoViewCustomerValueTextView.setText(customerGSTNo);

            //get all the delivery address from database using customer_id
            deliveryAddressDatabaseReference = getDeliveryAddressDatabaseReferenceByID(customerID);
            deliveryAddressDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot address : snapshot.getChildren()) {
                        //add address to table
                        String addressString = address.getValue(String.class);
                        addDeliveryAddressToTable(addressString);

                        //store address to String[]
                        deliveryAddressList.add(addressString);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_customer_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.edit_customer_option))) {
            if (viewCustomerTableLayout.getChildCount() > 1) {
                Intent intent = new Intent(ViewCustomerActivity.this, EditCustomerActivity.class);
                intent.putExtra(CUSTOMER_ID, customerID);
                intent.putExtra(CUSTOMER_NAME, customerName);
                intent.putExtra(CUSTOMER_ADDRESS, customerAddress);
                intent.putExtra(CUSTOMER_GST_NO, customerGSTNo);
                intent.putExtra(CUSTOMER_DELIVERY_ADDRESS_LIST, deliveryAddressList);
                startActivity(intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDeliveryAddressToTable(String deliveryAddress) {
        //initialize new row in table
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.view_customer_table_row, null);

        //initialize TextView as Table column
        TextView SRNOTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView addressTableColumn = tableRow.findViewById(R.id.addressTableColumn);

        //add data in new row
        SRNOTableColumn.setText(String.valueOf(viewCustomerTableLayout.getChildCount()));
        addressTableColumn.setText(deliveryAddress);

        //add new TableRow to TableLayout
        viewCustomerTableLayout.addView(tableRow);
    }
}