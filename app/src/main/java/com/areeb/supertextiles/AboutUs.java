package com.areeb.supertextiles;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("About Us");
        setContentView(R.layout.activity_about_us);

        //initialize xml view
        TextView businessGstNo = findViewById(R.id.businessGstNo);
        TextView bankNameValue = findViewById(R.id.bankNameValue);
        TextView branchName = findViewById(R.id.branchName);
        TextView accountNoValue = findViewById(R.id.accountNoValue);
        TextView ifscCodeValue = findViewById(R.id.ifscCodeValue);
        TextView typeOfAccountValue = findViewById(R.id.typeOfAccountValue);

        //businessGstNo TextView long Press Listeners
        businessGstNo.setOnLongClickListener(v -> {
            copyDataToClipBoard(businessGstNo.getTag().toString(), businessGstNo.getText().toString(), this);
            return true;
        });

        //bankNameValue TextView long Press Listeners
        bankNameValue.setOnLongClickListener(v -> {
            copyDataToClipBoard(bankNameValue.getTag().toString(), bankNameValue.getText().toString(), this);
            return true;
        });

        //branchName TextView long Press Listeners
        branchName.setOnLongClickListener(v -> {
            copyDataToClipBoard(branchName.getTag().toString(), branchName.getText().toString(), this);
            return true;
        });

        //accountNoValue TextView long Press Listeners
        accountNoValue.setOnLongClickListener(v -> {
            copyDataToClipBoard(accountNoValue.getTag().toString(), accountNoValue.getText().toString(), this);
            return true;
        });

        //ifscCodeValue TextView long Press Listeners
        ifscCodeValue.setOnLongClickListener(v -> {
            copyDataToClipBoard(ifscCodeValue.getTag().toString(), ifscCodeValue.getText().toString(), this);
            return true;
        });

        //typeOfAccountValue TextView long Press Listeners
        typeOfAccountValue.setOnLongClickListener(v -> {
            copyDataToClipBoard(typeOfAccountValue.getTag().toString(), typeOfAccountValue.getText().toString(), this);
            return true;
        });

    }

    //this method is used to copy EditText data to clipboard
    public static void copyDataToClipBoard(String label, String data, Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(label, data);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }
}