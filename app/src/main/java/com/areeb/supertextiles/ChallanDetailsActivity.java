package com.areeb.supertextiles;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.areeb.supertextiles.models.Challan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import static com.areeb.supertextiles.AboutUs.getTextViewLongClickListener;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_1;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_2;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_3;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_4;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_COLOR;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.DESIGN_NO;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.METER_LIST;
import static com.areeb.supertextiles.FirebaseDatabaseHelper.getDesignDataDatabaseReference;

public class ChallanDetailsActivity extends AppCompatActivity {

    public static final String CHALLAN_NO = "CHALLAN_NO";
    public static final String CHALLAN_DATE = "CHALLAN_DATE";
    public static final String PURCHASER = "PURCHASER";
    public static final String LOT_NO = "LOT_NO";
    public static final String LR_NO = "LR_NO";
    public static final String DELIVERY_AT = "DELIVERY_AT";
    public static final String PURCHASER_GST = "PURCHASER_GST";
    public static final String QUALITY = "QUALITY";
    public static final String TOTAL_PIECES = "TOTAL_PIECES";
    public static final String TOTAL_METERS = "TOTAL_METERS";
    public static final String FOLD = "FOLD";
    public static final String NO_OF_DESIGNS = "NO_OF_DESIGNS";
    TextView challanNoValueTextView, dateValueTextView, purchaserNameTextView, lotNoValueTextView, LRNoValueTextView,
            deliveryAtValueTextView, purchaserGSTValueTextView, qualityValueTextView, totalPiecesValueTextView,
            totalMetersValueTextView, foldValueTextView, designNo1ValueTextView, design1ColorValueTextView,
            designNo2ValueTextView, design2ColorValueTextView, designNo3ValueTextView, design3ColorValueTextView,
            designNo4ValueTextView, design4ColorValueTextView;
    ConstraintLayout design1ConstraintLayout, design2ConstraintLayout, design3ConstraintLayout, design4ConstraintLayout;
    TableLayout designOneTableLayout, designTwoTableLayout, designThreeTableLayout, designFourTableLayout;
    Challan challan;
    DatabaseReference designDataDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Challan Details");
        setContentView(R.layout.activity_challan_details);

        //initializing xml views
        challanNoValueTextView = findViewById(R.id.challanNoValueTextView);
        dateValueTextView = findViewById(R.id.billDateValueTextView);
        purchaserNameTextView = findViewById(R.id.purchaserNameTextView);
        lotNoValueTextView = findViewById(R.id.lotNoValueTextView);
        LRNoValueTextView = findViewById(R.id.LRNoValueTextView);
        deliveryAtValueTextView = findViewById(R.id.deliveryAtValueTextView);
        purchaserGSTValueTextView = findViewById(R.id.purchaserGSTValueTextView);
        qualityValueTextView = findViewById(R.id.qualityValueTextView);
        totalPiecesValueTextView = findViewById(R.id.totalPiecesValueTextView);
        totalMetersValueTextView = findViewById(R.id.totalMetersValueTextView);
        foldValueTextView = findViewById(R.id.foldValueTextView);
        designNo1ValueTextView = findViewById(R.id.designNo1ValueTextView);
        design1ColorValueTextView = findViewById(R.id.design1ColorValueTextView);
        designNo2ValueTextView = findViewById(R.id.designNo2ValueTextView);
        design2ColorValueTextView = findViewById(R.id.design2ColorValueTextView);
        designNo3ValueTextView = findViewById(R.id.designNo3ValueTextView);
        design3ColorValueTextView = findViewById(R.id.design3ColorValueTextView);
        designNo4ValueTextView = findViewById(R.id.designNo4ValueTextView);
        design4ColorValueTextView = findViewById(R.id.design4ColorValueTextView);
        design1ConstraintLayout = findViewById(R.id.design1ConstraintLayout);
        design2ConstraintLayout = findViewById(R.id.design2ConstraintLayout);
        design3ConstraintLayout = findViewById(R.id.design3ConstraintLayout);
        design4ConstraintLayout = findViewById(R.id.design4ConstraintLayout);
        designOneTableLayout = findViewById(R.id.designOneTableLayout);
        designTwoTableLayout = findViewById(R.id.designTwoTableLayout);
        designThreeTableLayout = findViewById(R.id.designThreeTableLayout);
        designFourTableLayout = findViewById(R.id.designFourTableLayout);

        //set challan details in TextView's
        if (getIntent() != null) {

            //initialize Challan Object
            challan = new Challan();

            challan.setChallan_no(getIntent().getStringExtra(CHALLAN_NO));
            challan.setDate(getIntent().getStringExtra(CHALLAN_DATE));
            challan.setPurchaser(getIntent().getStringExtra(PURCHASER));
            challan.setLot_no(getIntent().getStringExtra(LOT_NO));
            challan.setLr_no(getIntent().getStringExtra(LR_NO));
            challan.setDelivery_at(getIntent().getStringExtra(DELIVERY_AT));
            challan.setPurchaser_gst(getIntent().getStringExtra(PURCHASER_GST));
            challan.setQuality(getIntent().getStringExtra(QUALITY));
            challan.setTotal_pieces(getIntent().getStringExtra(TOTAL_PIECES));
            challan.setTotal_meters(getIntent().getStringExtra(TOTAL_METERS));
            challan.setFold(getIntent().getStringExtra(FOLD));
            challan.setNo_of_designs(getIntent().getStringExtra(NO_OF_DESIGNS));

            //set Challan details in TextView
            challanNoValueTextView.setText(challan.getChallan_no());
            dateValueTextView.setText(challan.getDate());
            purchaserNameTextView.setText(challan.getPurchaser());
            lotNoValueTextView.setText(challan.getLot_no());
            LRNoValueTextView.setText(challan.getLr_no());
            deliveryAtValueTextView.setText(challan.getDelivery_at());
            purchaserGSTValueTextView.setText(challan.getPurchaser_gst());
            qualityValueTextView.setText(challan.getQuality());
            totalPiecesValueTextView.setText(challan.getTotal_pieces());
            totalMetersValueTextView.setText(challan.getTotal_meters());
            foldValueTextView.setText(challan.getFold());

            //show design fields based on number_of_designs
            switch (challan.getNo_of_designs()) {
                case "2":
                    design2ConstraintLayout.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    design2ConstraintLayout.setVisibility(View.VISIBLE);
                    design3ConstraintLayout.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    design2ConstraintLayout.setVisibility(View.VISIBLE);
                    design3ConstraintLayout.setVisibility(View.VISIBLE);
                    design4ConstraintLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //get design data from database
        if (!challan.getChallan_no().isEmpty() && challan.getChallan_no() != null) {
            //initialize design_data DatabaseReference
            designDataDatabaseReference = getDesignDataDatabaseReference(challan.getChallan_no());

            //get design data from database
            designDataDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() == 1) {
                        setDataInDesign1Field(snapshot);
                    } else if (snapshot.getChildrenCount() == 2) {
                        setDataInDesign1Field(snapshot);
                        setDataInDesign2Field(snapshot);
                    } else if (snapshot.getChildrenCount() == 3) {
                        setDataInDesign1Field(snapshot);
                        setDataInDesign2Field(snapshot);
                        setDataInDesign3Field(snapshot);
                    } else if (snapshot.getChildrenCount() == 4) {
                        setDataInDesign1Field(snapshot);
                        setDataInDesign2Field(snapshot);
                        setDataInDesign3Field(snapshot);
                        setDataInDesign4Field(snapshot);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });
        }

        //copy text to clipboard when onLongPress on TextView
        challanNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        dateValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        purchaserNameTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        lotNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        LRNoValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        deliveryAtValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        purchaserGSTValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        qualityValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        totalPiecesValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        totalMetersValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        foldValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo1ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design1ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo2ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design2ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo3ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design3ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        designNo4ValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
        design4ColorValueTextView.setOnLongClickListener(getTextViewLongClickListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.challan_details_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        String title = item.getTitle().toString();

        if (title.equals(getString(R.string.challan_pdf))) {
            Toast.makeText(this, "PDF downloaded", Toast.LENGTH_SHORT).show();
        }
        else if (title.equals(getString(R.string.edit_challan))) {
            if (challan != null) {
                Intent intent = new Intent(ChallanDetailsActivity.this, AddChallanActivity.class);
                intent.putExtra(CHALLAN_NO, challan.getChallan_no());
                intent.putExtra(CHALLAN_DATE, challan.getDate());
                intent.putExtra(PURCHASER, challan.getPurchaser());
                intent.putExtra(LOT_NO, challan.getLot_no());
                intent.putExtra(LR_NO, challan.getLr_no());
                intent.putExtra(DELIVERY_AT, challan.getDelivery_at());
                intent.putExtra(PURCHASER_GST, challan.getPurchaser_gst());
                intent.putExtra(QUALITY, challan.getQuality());
                intent.putExtra(TOTAL_PIECES, challan.getTotal_pieces());
                intent.putExtra(TOTAL_METERS, challan.getTotal_meters());
                intent.putExtra(FOLD, challan.getFold());
                intent.putExtra(NO_OF_DESIGNS, challan.getNo_of_designs());

                //add design data in intent based on number of designs
                addDesignDataInIntent(intent);

                startActivity(intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //method to set data in design 1 field
    private void setDataInDesign1Field(DataSnapshot snapshot) {
        //get design1No and designColor from snapshot
        String design1No = snapshot.child(DESIGN_1).child(DESIGN_NO).getValue(String.class);
        String designColor = snapshot.child(DESIGN_1).child(DESIGN_COLOR).getValue(String.class);

        //set design1No and designColor to TextView
        designNo1ValueTextView.setText(design1No);
        design1ColorValueTextView.setText(designColor);

        //get meter list from snapshot
        for (DataSnapshot meter : snapshot.child(DESIGN_1).child(METER_LIST).getChildren()) {
            addRowInTableLayout(designOneTableLayout, meter.getValue(String.class));
        }
    }

    //method to set data in design 2 field
    private void setDataInDesign2Field(DataSnapshot snapshot) {
        //get design1No and designColor from snapshot
        String design2No = snapshot.child(DESIGN_2).child(DESIGN_NO).getValue(String.class);
        String designColor = snapshot.child(DESIGN_2).child(DESIGN_COLOR).getValue(String.class);

        //set design1No and designColor to TextView
        designNo2ValueTextView.setText(design2No);
        design2ColorValueTextView.setText(designColor);

        //get meter list from snapshot
        for (DataSnapshot meter : snapshot.child(DESIGN_2).child(METER_LIST).getChildren()) {
            addRowInTableLayout(designTwoTableLayout, meter.getValue(String.class));
        }
    }

    //method to set data in design 3 field
    private void setDataInDesign3Field(DataSnapshot snapshot) {
        //get design1No and designColor from snapshot
        String design3No = snapshot.child(DESIGN_3).child(DESIGN_NO).getValue(String.class);
        String designColor = snapshot.child(DESIGN_3).child(DESIGN_COLOR).getValue(String.class);

        //set design1No and designColor to TextView
        designNo3ValueTextView.setText(design3No);
        design3ColorValueTextView.setText(designColor);

        //get meter list from snapshot
        for (DataSnapshot meter : snapshot.child(DESIGN_3).child(METER_LIST).getChildren()) {
            addRowInTableLayout(designThreeTableLayout, meter.getValue(String.class));
        }
    }

    //method to set data in design 4 field
    private void setDataInDesign4Field(DataSnapshot snapshot) {
        //get design1No and designColor from snapshot
        String design4No = snapshot.child(DESIGN_4).child(DESIGN_NO).getValue(String.class);
        String designColor = snapshot.child(DESIGN_4).child(DESIGN_COLOR).getValue(String.class);

        //set design1No and designColor to TextView
        designNo4ValueTextView.setText(design4No);
        design4ColorValueTextView.setText(designColor);

        //get meter list from snapshot
        for (DataSnapshot meter : snapshot.child(DESIGN_4).child(METER_LIST).getChildren()) {
            addRowInTableLayout(designFourTableLayout, meter.getValue(String.class));
        }
    }

    //method to add row in TableLayout
    private void addRowInTableLayout(TableLayout tableLayout, String meter) {
        //initialize new row in table
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.view_challan_table_row, null);

        //initialize TextView as Table column
        TextView SRNOTableColumn = tableRow.findViewById(R.id.SNoTableColumn);
        TextView meterTableColumn = tableRow.findViewById(R.id.meterTableColumn);

        //add data in new row
        SRNOTableColumn.setText(String.valueOf(tableLayout.getChildCount()));
        meterTableColumn.setText(meter);

        //add new TableRow to TableLayout
        tableLayout.addView(tableRow);
    }

    //method to add design data in intent based on number of designs
    private void addDesignDataInIntent(Intent intent) {
        String numberOfDesigns = challan.getNo_of_designs();

        switch (numberOfDesigns) {
            case "1":

                break;
            case "2":

                break;
            case "3":

                break;
            case "4":

                break;
        }

    }
}