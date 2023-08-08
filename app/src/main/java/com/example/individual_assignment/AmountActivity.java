package com.example.individual_assignment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class AmountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount);

        String billTitle = this.getIntent().getStringExtra("title");
        TextView titleEditText = findViewById(R.id.billTitle2);
        titleEditText.setText(billTitle);
        String billAmount = this.getIntent().getStringExtra("amount");
        TextView amountEditText = findViewById(R.id.billAmount2);
        amountEditText.setText(billAmount);


        LinearLayout listLayout = findViewById(R.id.personList2);
        LinearLayout inputBox;
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearParams.setMargins(30, 10, 30, 0);

        LinearLayout.LayoutParams personParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        personParams.weight=3;

        LinearLayout.LayoutParams amountParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        amountParams.weight=1;

        EditText person;
        EditText amount;

        ArrayList<LinearLayout> personList = new ArrayList<LinearLayout>();

        //create default two persons in the list
        for(int i = 1; i <= 2; i++){
            inputBox = new LinearLayout(this);
            inputBox.setLayoutParams(linearParams);

            person = new EditText(this);
            person.setInputType(InputType.TYPE_CLASS_TEXT);
            person.setText("person" + i);
            person.setLayoutParams(personParams);

            amount = new EditText(this);
            amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            amount.setText("0.00");
            amount.setLayoutParams(amountParams);

            inputBox.addView(person);
            inputBox.addView(amount);
            personList.add(inputBox);
            listLayout.addView(inputBox);
        }

        Button add = findViewById(R.id.addButton2);
        Button remove = findViewById(R.id.removeButton2);
        Button confirm = findViewById(R.id.confirmButton2);
        CheckBox checkBox = findViewById(R.id.checkBox2);

        //calculate the amount share equally and disable add and remove button when checkbox is checked
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton)view).isChecked()){
                    listLayout.removeAllViews();
                    int size = personList.size();
                    for(LinearLayout i : personList){
                        EditText amountTemp = (EditText) i.getChildAt(1);
                        double amountPerN = Double.parseDouble(billAmount) / size;
                        amountTemp.setText(String.format("%.2f", amountPerN));
                        amountTemp.setFocusable(false);
                        listLayout.addView(i);
                    }
                    add.setEnabled(false);
                    remove.setEnabled(false);
                }else{
                    listLayout.removeAllViews();
                    for(LinearLayout i : personList){
                        EditText amountTemp = (EditText) i.getChildAt(1);
                        amountTemp.setFocusableInTouchMode(true);
                        listLayout.addView(i);
                    }
                    add.setEnabled(true);
                    remove.setEnabled(true);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPerson(personList, listLayout, personParams, amountParams, linearParams);
            }
        });

        remove.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePerson(personList, listLayout);
            }
        }));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double amountTotal = 0;
                for(LinearLayout i : personList){
                    double amountTemp;
                    if(!((EditText) i.getChildAt(1)).getText().toString().trim().equalsIgnoreCase("")){
                        amountTemp = Double.parseDouble(((EditText) i.getChildAt(1)).getText().toString());
                    }
                    else{
                        amountTemp = 0.00;
                    }
                    amountTotal += amountTemp;
                }
                if(amountTotal != Double.parseDouble(billAmount)){
                    TextView errorText = findViewById(R.id.errorText2);
                    errorText.setVisibility(View.VISIBLE);
                }
                else{
                    ArrayList<PersonAmount> personAmountList = new ArrayList<PersonAmount>();
                    for(LinearLayout i : personList){
                        double amountTemp;
                        if(!((EditText) i.getChildAt(1)).getText().toString().trim().equalsIgnoreCase("")){
                            amountTemp = Double.parseDouble(((EditText) i.getChildAt(1)).getText().toString());
                        }
                        else{
                            amountTemp = 0.00;
                        }
                        String nameTemp = ((EditText)i.getChildAt(0)).getText().toString();
                        PersonAmount temp = new PersonAmount(nameTemp, amountTemp);
                        personAmountList.add(temp);
                    }
                    Intent i = new Intent(AmountActivity.this, ResultActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("result",(Serializable)personAmountList);
                    i.putExtra("BUNDLE",args);
                    i.putExtra("title", billTitle);
                    i.putExtra("amount", billAmount);
                    startActivity(i);
                }
            }
        });

    }

    //add person in list view
    protected void addPerson(ArrayList<LinearLayout> personList, LinearLayout listLayout,
                             LinearLayout.LayoutParams personParams, LinearLayout.LayoutParams amountParams,
                             LinearLayout.LayoutParams linearParams){

        int size = personList.size();

        LinearLayout inputBox = new LinearLayout(this);
        inputBox.setLayoutParams(linearParams);

        EditText person = new EditText(this);
        person.setInputType(InputType.TYPE_CLASS_TEXT);
        person.setText("person" + (size+1));
        person.setLayoutParams(personParams);
        EditText amount = new EditText(this);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        amount.setText("0.00");
        amount.setLayoutParams(amountParams);

        inputBox.addView(person);
        inputBox.addView(amount);
        personList.add(inputBox);
        listLayout.addView(inputBox);
    }

    //remove person in list view, minimum must have 2 persons
    protected void removePerson(ArrayList<LinearLayout> personList, LinearLayout listLayout){
        int size = personList.size();

        if(size > 2){
            personList.remove(size-1);
            listLayout.removeAllViews();
            for(LinearLayout i : personList){
                listLayout.addView(i);
            }
        }
    }
}