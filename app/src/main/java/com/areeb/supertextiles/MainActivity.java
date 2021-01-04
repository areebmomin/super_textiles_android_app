package com.areeb.supertextiles;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    ConstraintLayout mainActivityRootLayout;
    BottomNavigationView bottomNavigationView;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        String title = item.getTitle().toString();
        Fragment selectedFragment = null;

        if (title.equals(getString(R.string.challans))) {
            selectedFragment = new ChallansFragment();
        }
        else if (title.equals(getString(R.string.bills))) {
            selectedFragment = new BillsFragment();
        }
        else if (title.equals(getString(R.string.report))) {
            selectedFragment = new ReportFragment();
        }
        else if (title.equals(getString(R.string.customers))) {
            selectedFragment = new CustomersFragment();
        }
        else if (title.equals(getString(R.string.more))) {
            selectedFragment = new MoreFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectedFragment).commit();
        }

        return true;
    };

    private final BottomNavigationView.OnNavigationItemReselectedListener navigationItemReselectedListener = item -> {
        String title = item.getTitle().toString();
        if (title.equals(getString(R.string.bills))) {
            Intent intent = new Intent(MainActivity.this, ViewBillActivity.class);
            startActivity(intent);
        }
        else if (title.equals(getString(R.string.report))) {
            Intent intent = new Intent(MainActivity.this, ViewReportActivity.class);
            startActivity(intent);
        }
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

        //set up bottom navigation
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, listFragment).commit();

        //set bottomNavigationView selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        //set bottomNavigationView reselected listener
        bottomNavigationView.setOnNavigationItemReselectedListener(navigationItemReselectedListener);

        //set ChallansFragment by default
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ChallansFragment()).commit();
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
                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Toast.makeText(MainActivity.this, newText, Toast.LENGTH_SHORT).show();
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
                changePassword(newPassword);
            });

            //code for negative button
            Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(v -> {
                //hide keyboard if open
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(customLayout.getWindowToken(), 0);

                //close the dialog
                alertDialog.dismiss();
            });
        });

        //show dialog
        alertDialog.show();
    }

    //method to change password
    private void changePassword(String password) {
        currentUser.updatePassword(password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG).show();
                    }
                })
        .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

}