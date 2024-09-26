package com.example.paybizsdk.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybizsdk.Logger.FileLogger;
import com.example.paybizsdk.R;
import com.example.paybizsdk.service.DatabaseService;

import org.json.JSONException;

public class TransactionResult extends AppCompatActivity {

    private TextView resultText, paymentIdText, sdkTransIdText, ccNameText, currencyText, amountText;
    private Button button;

    private DatabaseService databaseService;

    private final String TAG = "Transaction Result Screen";

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_result);
        FileLogger.log("INFO", TAG, "Transaction Result Screen Opened");
        databaseService = new DatabaseService(this);
        resultText = findViewById(R.id.authStatus);
        button = findViewById(R.id.backButton);
        paymentIdText = findViewById(R.id.paymentId);
        sdkTransIdText = findViewById(R.id.sdkTransId);
        ccNameText = findViewById(R.id.ccName);
        currencyText = findViewById(R.id.currency);
        amountText = findViewById(R.id.amount);
        try {
            FileLogger.log("VERBOSE", TAG, "Getting Values from Intent, Database Saved Record and setting text dynamically on Screen");
            Intent intent = getIntent();
            String tranxResult = !intent.getStringExtra("transStatus").isEmpty() ? intent.getStringExtra("transStatus") : "Error in fetching Details";
            String sdkTransId = !intent.getStringExtra("sdkTransID").isEmpty() ? intent.getStringExtra("sdkTransID") : "N/A";
            String pId = "", amount = "", cardHolderName = "";
            Cursor cursor = databaseService.getLastTransaction();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    pId = !cursor.getString(cursor.getColumnIndex("paymentId")).isEmpty() ? cursor.getString(cursor.getColumnIndex("paymentId")) : "N/A";
                    amount = !cursor.getString(cursor.getColumnIndex("amount")).isEmpty() ? cursor.getString(cursor.getColumnIndex("amount")) : "N/A";
                    cardHolderName = !cursor.getString(cursor.getColumnIndex("cardHolderName")).isEmpty() ? cursor.getString(cursor.getColumnIndex("cardHolderName")) : "N/A";
                }
            }
            resultText.setText(tranxResult);
            paymentIdText.setText(pId);
            sdkTransIdText.setText(sdkTransId);
            ccNameText.setText(cardHolderName);
            currencyText.setText("SAR");
            amountText.setText(amount);
            databaseService.deleteLastTransaction();
        }catch (Exception e){
            FileLogger.log("ERROR", TAG, "Error while fetching Data: " + e.getMessage());
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}