package com.example.individual_assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bill detail");
        builder.setCancelable(true);

        EditText inputTitle = new EditText(this);
        inputTitle.setInputType(InputType.TYPE_CLASS_TEXT);
        inputTitle.setWidth(200);
        inputTitle.setHint("Bill name");

        EditText inputAmount = new EditText(this);
        inputAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputAmount.setWidth(200);
        inputAmount.setHint("Bill amount ");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputTitle);
        layout.addView(inputAmount);

        builder.setView(layout);
        builder.setPositiveButton("Continue", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        // verify the bill title and amount is not empty
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(inputTitle.getText().toString().trim().equalsIgnoreCase("") || inputAmount.getText().toString().trim().equalsIgnoreCase("")){
                            if(inputTitle.getText().toString().trim().equalsIgnoreCase("")){
                                inputTitle.setError("Required");
                            }
                            else{
                                inputAmount.setError("Required");
                            }
                        }
                        else{
                            String title = inputTitle.getText().toString();
                            String amount = inputAmount.getText().toString();
                            //Toast.makeText(getApplicationContext(), amount.toString(), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, MainActivity2.class);
                            i.putExtra("title", title);
                            i.putExtra("amount", amount);
                            startActivity(i);
                        }
                    }
                });
            }
        });


        Button button = findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
}