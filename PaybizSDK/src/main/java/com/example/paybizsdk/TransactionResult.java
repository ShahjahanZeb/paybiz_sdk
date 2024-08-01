package com.example.paybizsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

public class TransactionResult extends AppCompatActivity {

    private TextView result;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_result);
        result = findViewById(R.id.paymentResult);
        Intent intent = getIntent();
        String tranxResult = intent.getStringExtra("transStatus");
        result.setText(tranxResult);
        button = findViewById(R.id.backBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}