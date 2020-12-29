package com.areeb.supertextiles;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.areeb.supertextiles.AboutUs.copyDataToClipBoard;

public class AddMetersActivity extends AppCompatActivity {
    
    LinearLayout designOneLinearLayout, designTwoLinearLayout, designThreeLinearLayout, designFourLinearLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Meters");
        setContentView(R.layout.activity_add_meters);

        //initialize xml view
        TextView totalPiecesValueTextView = findViewById(R.id.totalPiecesValueTextView);
        designOneLinearLayout = findViewById(R.id.designOneLinearLayout);
        designTwoLinearLayout = findViewById(R.id.designTwoLinearLayout);
        designThreeLinearLayout = findViewById(R.id.designThreeLinearLayout);
        designFourLinearLayout = findViewById(R.id.designFourLinearLayout);
        
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
            Intent intent = new Intent(AddMetersActivity.this, AddChallanActivity.class);
            startActivity(intent);
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
                designOneLinearLayout.setVisibility(View.VISIBLE);
                designTwoLinearLayout.setVisibility(View.GONE);
                designThreeLinearLayout.setVisibility(View.GONE);
                designFourLinearLayout.setVisibility(View.GONE);
            }
            else if (title.equals(getString(R.string._2))) {
                designOneLinearLayout.setVisibility(View.VISIBLE);
                designTwoLinearLayout.setVisibility(View.VISIBLE);
                designThreeLinearLayout.setVisibility(View.GONE);
                designFourLinearLayout.setVisibility(View.GONE);
            }
            else if (title.equals(getString(R.string._3))) {
                designOneLinearLayout.setVisibility(View.VISIBLE);
                designTwoLinearLayout.setVisibility(View.VISIBLE);
                designThreeLinearLayout.setVisibility(View.VISIBLE);
                designFourLinearLayout.setVisibility(View.GONE);
            }
            else if (title.equals(getString(R.string._4))) {
                designOneLinearLayout.setVisibility(View.VISIBLE);
                designTwoLinearLayout.setVisibility(View.VISIBLE);
                designThreeLinearLayout.setVisibility(View.VISIBLE);
                designFourLinearLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}