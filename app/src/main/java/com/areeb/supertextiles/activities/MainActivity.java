package com.areeb.supertextiles.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.areeb.supertextiles.R;
import com.areeb.supertextiles.fragments.BillsFragment;
import com.areeb.supertextiles.fragments.ChallansFragment;
import com.areeb.supertextiles.fragments.CustomersFragment;
import com.areeb.supertextiles.fragments.MoreFragment;
import com.areeb.supertextiles.fragments.ReportFragment;
import com.areeb.supertextiles.interfaces.SearchInBills;
import com.areeb.supertextiles.interfaces.SearchInChallan;
import com.areeb.supertextiles.interfaces.SearchInCustomer;
import com.areeb.supertextiles.interfaces.SearchInReport;
import com.areeb.supertextiles.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import java.util.Objects;

import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.MESSERS_LOWER_CASE;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.NAME_LOWER_CASE;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.PARTY_LOWER_CASE;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.PURCHASER_LOWER_CASE;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getAllCustomersReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getBillListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getChallanListDatabaseReference;
import static com.areeb.supertextiles.utilities.FirebaseDatabaseHelper.getReportListDatabaseReference;

public class MainActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    ConstraintLayout mainActivityRootLayout;
    BottomNavigationView bottomNavigationView;
    PreferenceManager preferenceManager;
    public static final String CHALLAN_FRAGMENT = "CHALLAN_FRAGMENT";
    public static final String BILL_FRAGMENT = "BILL_FRAGMENT";
    public static final String REPORT_FRAGMENT = "REPORT_FRAGMENT";
    public static final String CUSTOMER_FRAGMENT = "CUSTOMER_FRAGMENT";
    public static final String MORE_FRAGMENT = "MORE_FRAGMENT";

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        String title = item.getTitle().toString();
        Fragment selectedFragment = null;

        if (title.equals(getString(R.string.challans))) {
            selectedFragment = new ChallansFragment();
            preferenceManager.setLastVisitedFragment(CHALLAN_FRAGMENT);
        } else if (title.equals(getString(R.string.bills))) {
            selectedFragment = new BillsFragment();
            preferenceManager.setLastVisitedFragment(BILL_FRAGMENT);
        } else if (title.equals(getString(R.string.report))) {
            selectedFragment = new ReportFragment();
            preferenceManager.setLastVisitedFragment(REPORT_FRAGMENT);
        } else if (title.equals(getString(R.string.customers))) {
            selectedFragment = new CustomersFragment();
            preferenceManager.setLastVisitedFragment(CUSTOMER_FRAGMENT);
        } else if (title.equals(getString(R.string.more))) {
            selectedFragment = new MoreFragment();
            preferenceManager.setLastVisitedFragment(MORE_FRAGMENT);
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
        }

        return true;
    };

    private final BottomNavigationView.OnNavigationItemReselectedListener navigationItemReselectedListener = item -> {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing xml views
        mainActivityRootLayout = findViewById(R.id.mainActivityRootLayout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //initialize singed in user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //initialize Preference Manager
        preferenceManager = new PreferenceManager(this);

        //set up bottom navigation
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, listFragment).commit();

        //set bottomNavigationView selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //set bottomNavigationView reselected listener
        bottomNavigationView.setOnNavigationItemReselectedListener(navigationItemReselectedListener);

        //set ChallansFragment by default
        String lastVisitedFragment = preferenceManager.getLastVisitedFragment();
        switch (lastVisitedFragment) {
            case CHALLAN_FRAGMENT:
                bottomNavigationView.setSelectedItemId(R.id.challansOption);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ChallansFragment()).commit();
                break;
            case BILL_FRAGMENT:
                bottomNavigationView.setSelectedItemId(R.id.billsOption);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new BillsFragment()).commit();
                break;
            case REPORT_FRAGMENT:
                bottomNavigationView.setSelectedItemId(R.id.reportOption);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ReportFragment()).commit();
                break;
            case CUSTOMER_FRAGMENT:
                bottomNavigationView.setSelectedItemId(R.id.customersOption);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CustomersFragment()).commit();
                break;
            case MORE_FRAGMENT:
                bottomNavigationView.setSelectedItemId(R.id.moreOption);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MoreFragment()).commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //initialize searchView
        MenuItem searchViewItem = menu.findItem(R.id.searchOption);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) searchViewItem.getActionView();
            searchView.setQueryHint("Search");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            searchView.setIconified(true);

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    firebaseSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    firebaseSearch(newText);
                    return true;
                }
            };

            searchView.setOnQueryTextListener(queryTextListener);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = item.getTitle().toString();
        if (getString(R.string.change_password).equals(title)) {
            showChangePasswordDialog();
        } else if (getString(R.string.logout).equals(title)) {
            //logout current signed in user
            logout();
        } else if (getString(R.string.about_us).equals(title)) {
            //go to AboutUs Activity
            goToAboutUsActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    //Firebase search clients by name method
    public void firebaseSearch(String queryText) {
        int selectedItemId = bottomNavigationView.getSelectedItemId();
        if (selectedItemId == R.id.challansOption) {
            Query firebaseSearchQuery = getChallanListDatabaseReference().orderByChild(PURCHASER_LOWER_CASE)
                    .startAt(queryText.toLowerCase()).endAt(queryText.toLowerCase() + "\uf8ff");
            if (queryText.isEmpty()) firebaseSearchQuery = getChallanListDatabaseReference();
            updateRecyclerView(firebaseSearchQuery, selectedItemId);
        } else if (selectedItemId == R.id.billsOption) {
            Query firebaseSearchQuery = getBillListDatabaseReference().orderByChild(MESSERS_LOWER_CASE)
                    .startAt(queryText.toLowerCase()).endAt(queryText.toLowerCase() + "\uf8ff");
            if (queryText.isEmpty()) firebaseSearchQuery = getBillListDatabaseReference();
            updateRecyclerView(firebaseSearchQuery, selectedItemId);
        } else if (selectedItemId == R.id.reportOption) {
            Query firebaseSearchQuery = getReportListDatabaseReference().orderByChild(PARTY_LOWER_CASE)
                    .startAt(queryText.toLowerCase()).endAt(queryText.toLowerCase() + "\uf8ff");
            if (queryText.isEmpty()) firebaseSearchQuery = getReportListDatabaseReference();
            updateRecyclerView(firebaseSearchQuery, selectedItemId);
        } else if (selectedItemId == R.id.customersOption) {
            Query firebaseSearchQuery = getAllCustomersReference().orderByChild(NAME_LOWER_CASE)
                    .startAt(queryText.toLowerCase()).endAt(queryText.toLowerCase() + "\uf8ff");
            if (queryText.isEmpty()) firebaseSearchQuery = getAllCustomersReference();
            updateRecyclerView(firebaseSearchQuery, selectedItemId);
        } //else if (selectedItemId == R.id.moreOption) {
//                    Query firebaseSearchQuery = clientDatabaseReference.orderByChild(NAME_LOWER_CASE)
//                .startAt(queryText.toLowerCase()).endAt(queryText.toLowerCase() + "\uf8ff");
//        updateRecyclerView(firebaseSearchQuery, selectedItemId);
//        }
    }

    //set new RecyclerView data
    public void updateRecyclerView(Query firebaseSearchQuery, int selectedItemId) {
        //query database for clients list
        if (selectedItemId == R.id.challansOption) {
            RecyclerView challansRecyclerView = findViewById(R.id.challansRecyclerView);
            SearchInChallan searchInChallan = new ChallansFragment();
            searchInChallan.onSearchInChallan(firebaseSearchQuery, challansRecyclerView);
        } else if (selectedItemId == R.id.billsOption) {
            RecyclerView billsRecyclerView = findViewById(R.id.billsRecyclerView);
            SearchInBills searchInBills = new BillsFragment();
            searchInBills.onSearchInBills(firebaseSearchQuery, billsRecyclerView);
        } else if (selectedItemId == R.id.reportOption) {
            RecyclerView reportRecyclerView = findViewById(R.id.reportRecyclerView);
            SearchInReport searchInReport = new ReportFragment();
            searchInReport.onSearchInReport(firebaseSearchQuery, reportRecyclerView);
        } else if (selectedItemId == R.id.customersOption) {
            RecyclerView customersRecyclerView = findViewById(R.id.customersRecyclerView);
            SearchInCustomer searchInCustomer = new CustomersFragment();
            searchInCustomer.onSearchInCustomer(firebaseSearchQuery, customersRecyclerView);
        } //else if (selectedItemId == R.id.moreOption) {
//        options = new FirebaseRecyclerOptions.Builder<Client>()
//                .setQuery(firebaseSearchQuery, Client.class)
//                .build();
//
//        clientAdapter = new ClientAdapter(options, this);
//        clientsRecyclerView.setAdapter(clientAdapter);
//        clientAdapter.startListening();
        //}
    }

    //method to logout user
    private void logout() {
        //sign out currently signed in user
        FirebaseAuth.getInstance().signOut();

        //goto login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    //method to goto AboutUs Activity
    private void goToAboutUsActivity() {
        Intent intent = new Intent(MainActivity.this, AboutUs.class);
        startActivity(intent);
    }

    //method to show change password dialog
    private void showChangePasswordDialog() {
        //set custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.change_password_dialog, null);

        //initialize alert dialog edit text
        TextInputLayout newPasswordEditText = customLayout.findViewById(R.id.newPasswordEditText);

        //request focus when dialog created
        newPasswordEditText.requestFocus();

        //create alert dialog alertDialog
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_password))
                .setView(customLayout)
                .setPositiveButton(getString(R.string.change), null)
                .setNegativeButton("Cancel", null)
                .create();

        //AlertDialog OnShowListener implementation
        alertDialog.setOnShowListener(dialog -> {

            //code for positive positiveButton
            Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(v -> {
                //get password from EditText
                String newPassword = String.valueOf(Objects.requireNonNull(newPasswordEditText.getEditText()).getText());

                //check if password is empty
                if (newPassword.isEmpty()) {
                    newPasswordEditText.requestFocus();
                    newPasswordEditText.setError("Password cannot be empty");
                    return;
                }

                //check if password less than 6 characters
                if (newPassword.length() < 6) {
                    newPasswordEditText.requestFocus();
                    newPasswordEditText.setError("Password must have 6 characters");
                    return;
                }

                //change password if credential is correct
                changePassword(newPassword, alertDialog, customLayout);
            });

            //code for negative button
            Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> closeAlertDialog(alertDialog, customLayout));
        });

        //show dialog
        alertDialog.show();
    }

    //method to change password
    private void changePassword(String password, AlertDialog alertDialog, View customLayout) {
        currentUser.updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        closeAlertDialog(alertDialog, customLayout);
                        Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    //method to close AlertDialog
    private void closeAlertDialog(AlertDialog alertDialog, View customLayout) {
        //hide keyboard if open
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(customLayout.getWindowToken(), 0);

        //close the dialog
        alertDialog.dismiss();
    }

}