package com.example.individual_assignment;

import android.app.Person;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String billTitle = this.getIntent().getStringExtra("title");
        TextView titleEditText = findViewById(R.id.textView10);
        titleEditText.setText(billTitle);
        String billAmount = this.getIntent().getStringExtra("amount");
        TextView amountEditText = findViewById(R.id.textView12);
        amountEditText.setText(billAmount);

        Bundle args = this.getIntent().getBundleExtra("BUNDLE");
        ArrayList<PersonAmount> personList = (ArrayList<PersonAmount>)args.getSerializable("result");


        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearParams.setMargins(30, 10, 30, 0);

        LinearLayout.LayoutParams personParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        personParams.weight=3;

        LinearLayout.LayoutParams amountParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        amountParams.weight=1;

        LinearLayout listLayout;
        TextView name;
        TextView amount;
        LinearLayout resultLayout = (LinearLayout)findViewById(R.id.resultLayout);
        //resultLayout.removeAllViews();
        for(PersonAmount x: personList){
            name = new TextView(this);
            name.setLayoutParams(personParams);
            name.setText(x.getName());
            name.setTextSize(20);
            name.setTextColor(Color.BLACK);

            amount = new TextView(this);
            amount.setLayoutParams(amountParams);
            amount.setText(String.format("RM %.2f", x.getAmount()));
            amount.setTextSize(20);
            amount.setTextColor(Color.BLACK);

            listLayout = new LinearLayout(this);
            listLayout.setLayoutParams(linearParams);
            listLayout.addView(name);
            listLayout.addView(amount);
            resultLayout.addView(listLayout);
        }

        Button home = findViewById(R.id.homeButton);
        Button share = findViewById(R.id.shareButton);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        //redirect to whatsapp, show error when whatsapp is not installed
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String textToSend = "Bill: "+ billTitle + "\nAmount: RM" + billAmount + "\n"+dateFormat.format(date)+"\n\n";
                for(PersonAmount x : personList){
                    textToSend = textToSend.concat(x.getName()+String.format(" RM%.2f", x.getAmount())+"\n");
                }
                try{
                    Intent whatsappIntent = new Intent();
                    whatsappIntent.setAction(Intent.ACTION_SEND);
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    startActivity(whatsappIntent);
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Whatsapp is not found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}