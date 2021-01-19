package com.areeb.supertextiles.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.areeb.supertextiles.R;

public class AboutUs extends AppCompatActivity {

    TextView businessGstNo, bankNameValue, branchName, accountNoValue, ifscCodeValue, typeOfAccountValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("About Us");
        setContentView(R.layout.activity_about_us);

        //initialize xml view
        businessGstNo = findViewById(R.id.businessGstNo);
        bankNameValue = findViewById(R.id.bankNameValue);
        branchName = findViewById(R.id.branchName);
        accountNoValue = findViewById(R.id.accountNoValue);
        ifscCodeValue = findViewById(R.id.ifscCodeValue);
        typeOfAccountValue = findViewById(R.id.typeOfAccountValue);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //businessGstNo TextView long Press Listeners
        businessGstNo.setOnLongClickListener(getTextViewLongClickListener(this));

        //bankNameValue TextView long Press Listeners
        bankNameValue.setOnLongClickListener(getTextViewLongClickListener(this));

        //branchName TextView long Press Listeners
        branchName.setOnLongClickListener(getTextViewLongClickListener(this));

        //accountNoValue TextView long Press Listeners
        accountNoValue.setOnLongClickListener(getTextViewLongClickListener(this));

        //ifscCodeValue TextView long Press Listeners
        ifscCodeValue.setOnLongClickListener(getTextViewLongClickListener(this));

        //typeOfAccountValue TextView long Press Listeners
        typeOfAccountValue.setOnLongClickListener(getTextViewLongClickListener(this));
    }

    //this method is used to copy EditText data to clipboard
    public static void copyDataToClipBoard(String label, String data, Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(label, data);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    //method to get OnLongClickListener on TextView
    public static View.OnLongClickListener getTextViewLongClickListener(Context context) {
        return v -> {
            //cast view object to TextView
            TextView textView = (TextView) v;
            copyDataToClipBoard(textView.getTag().toString(), textView.getText().toString(), context);

            return true;
        };
    }
}