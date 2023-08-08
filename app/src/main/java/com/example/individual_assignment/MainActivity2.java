package com.example.individual_assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String billTitle = this.getIntent().getStringExtra("title");
        TextView titleEditText = findViewById(R.id.textView4);
        titleEditText.setText(billTitle);
        String billAmount = this.getIntent().getStringExtra("amount");
        TextView amountEditText = findViewById(R.id.textView2);
        amountEditText.setText(billAmount);

        Button byAmount = findViewById(R.id.buttonByAmount);
        Button byPercent = findViewById(R.id.buttonByPercent);
        Button cancel = findViewById(R.id.buttonCancel);

        byAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity2.this, AmountActivity.class);
                i.putExtra("title", billTitle);
                i.putExtra("amount", billAmount);
                startActivity(i);
            }
        });

        byPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity2.this, PercentageActivity.class);
                i.putExtra("title", billTitle);
                i.putExtra("amount", billAmount);
                startActivity(i);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}