package com.areeb.supertextiles.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.areeb.supertextiles.R;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static com.areeb.supertextiles.activities.AddChallanActivity.showErrorInTextField;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_HINT = 58;
    TextInputLayout emailTextField, passwordTextField;
    Button enterButton;
    CredentialsClient mCredentialsClient;
    private FirebaseAuth mAuth;
    ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        setContentView(R.layout.activity_login);

        //initializing xml views
        emailTextField = findViewById(R.id.emailTextField);
        passwordTextField = findViewById(R.id.passwordTextField);
        enterButton = findViewById(R.id.enterButton);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        //initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check if user is currently signed in or not
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //if statement will be executed when current user is signed in
        if (currentUser != null) {
            //goto MainActivity
            goToMainActivity();
        }

        //else statement will be executed when user is not signed in
        else {
            //show user email hint
            showEmailSignInHint();
        }
    }

    //enter button on click method
    public void loginUser(View view) {
        //get email and password from edit texts
        String email = Objects.requireNonNull(emailTextField.getEditText()).getText().toString();
        String password = Objects.requireNonNull(passwordTextField.getEditText()).getText().toString();

        //check if email is blank
        if (email.isEmpty()) {
            showErrorInTextField(emailTextField, "Email cannot be empty");
            return;
        }

        //check if password is blank
        if (password.isEmpty()) {
            showErrorInTextField(passwordTextField, "Password cannot be empty");
            return;
        }

        //check is password is less than 6 characters
        if (password.length() < 6) {
            showErrorInTextField(passwordTextField, "Password must have 6 characters");
            return;
        }

        //show ProgressBar
        loginProgressBar.setVisibility(View.VISIBLE);

        //sign in user if credentials are correct
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //hide keyboard if open
                        hideSoftKeyboard(this, enterButton);

                        //hide ProgressBar
                        loginProgressBar.setVisibility(View.INVISIBLE);

                        //go to MainActivity
                        goToMainActivity();
                    }
                    else {

                        //hide ProgressBar
                        loginProgressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(LoginActivity.this, "User Not found", Toast.LENGTH_LONG).show();
                    }
                });
    }

    //show soft keyboard method
//    public static void showSoftKeyboard(Context context, View editText) {
//        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
//    }

    //hide soft keyboard method
    public static void hideSoftKeyboard(Context context, View view) {
        //hide keyboard if open
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK && data != null) {
                //get data in credential
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);

                //set email id in email text field
                Objects.requireNonNull(emailTextField.getEditText()).setText(credential.getId());

                //set focus on password text field
                passwordTextField.requestFocus();
            }
            else {
                //set focus on email text field
                emailTextField.requestFocus();
            }
        }
    }

    //method to show email sign in hint
    private void showEmailSignInHint() {
        //initializing CredentialsClient
        mCredentialsClient = Credentials.getClient(this);

        //initialize HttpRequestHint
        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setEmailAddressIdentifierSupported(true)
                .setAccountTypes(IdentityProviders.GOOGLE, IdentityProviders.FACEBOOK, IdentityProviders.LINKEDIN,
                        IdentityProviders.MICROSOFT, IdentityProviders.TWITTER)
                .build();

        //initializing Pending Intent and creating email hint dialog
        PendingIntent pendingIntent = mCredentialsClient.getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(pendingIntent.getIntentSender(), RC_HINT, null, 0,0,0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}