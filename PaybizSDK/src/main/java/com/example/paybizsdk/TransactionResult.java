package com.example.paybizsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.paybizsdk.service.DatabaseService;

import org.json.JSONException;

public class TransactionResult extends AppCompatActivity {

    private TextView resultText, paymentIdText, sdkTransIdText, ccNameText, currencyText, amountText;
    private Button button;

    private DatabaseService databaseService;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_result);
        databaseService = new DatabaseService(this);
        String pId = "", amount = "", cardHolderName = "";
        Cursor cursor = databaseService.getLastTransaction();
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                pId = cursor.getString(cursor.getColumnIndex("paymentId"));
                amount = cursor.getString(cursor.getColumnIndex("amount"));
                cardHolderName = cursor.getString(cursor.getColumnIndex("cardHolderName"));
            }
        }
        resultText = findViewById(R.id.authStatus);
        button = findViewById(R.id.backButton);
        paymentIdText = findViewById(R.id.paymentId);
        sdkTransIdText = findViewById(R.id.sdkTransId);
        ccNameText = findViewById(R.id.ccName);
        currencyText = findViewById(R.id.currency);
        amountText = findViewById(R.id.amount);
        Intent intent = getIntent();
        String tranxResult = intent.getStringExtra("transStatus");
        String sdkTransId = intent.getStringExtra("sdkTransID");
        resultText.setText(tranxResult);
        paymentIdText.setText(pId);
        sdkTransIdText.setText(sdkTransId);
        ccNameText.setText(cardHolderName);
        currencyText.setText("SAR");
        amountText.setText(amount);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}