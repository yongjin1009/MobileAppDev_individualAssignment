package com.example.individual_assignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PercentageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage);

        //set value for bill title and bill total amount
        String billTitle = this.getIntent().getStringExtra("title");
        TextView titleEditText = findViewById(R.id.billTitle);
        titleEditText.setText(billTitle);
        String billAmount = this.getIntent().getStringExtra("amount");
        TextView amountEditText = findViewById(R.id.billAmount);
        amountEditText.setText(billAmount);


        LinearLayout listLayout = findViewById(R.id.personList);
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
            amount.setInputType(InputType.TYPE_CLASS_NUMBER);
            amount.setText("0");
            amount.setLayoutParams(amountParams);

            inputBox.addView(person);
            inputBox.addView(amount);
            personList.add(inputBox);
            listLayout.addView(inputBox);
        }

        CheckBox checkBox = findViewById(R.id.checkBox);
        Button add = findViewById(R.id.addButton);
        Button remove = findViewById(R.id.removeButton);
        Button confirm = findViewById(R.id.confirmButton);
        RadioButton ratioRadio = findViewById(R.id.ratioRadio);
        RadioButton percentRadio = findViewById(R.id.percentRadio);

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

        //calculate the amount share equally and disable add and remove button when checkbox is checked
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton)view).isChecked()){
                    listLayout.removeAllViews();
                    if(ratioRadio.isChecked()){
                        for(LinearLayout i : personList){
                            EditText amountTemp = (EditText) i.getChildAt(1);
                            amountTemp.setText("1");
                            amountTemp.setFocusable(false);
                            listLayout.addView(i);
                        }
                    }else{
                        int percentPerN = 100 / personList.size();
                        for(LinearLayout i : personList){
                            EditText amountTemp = (EditText) i.getChildAt(1);
                            amountTemp.setText(Integer.toString(percentPerN));
                            amountTemp.setFocusable(false);
                            listLayout.addView(i);
                        }
                    }
                    add.setEnabled(false);
                    remove.setEnabled(false);
                    ratioRadio.setEnabled(false);
                    percentRadio.setEnabled(false);
                }
                else{
                    listLayout.removeAllViews();
                    for(LinearLayout i : personList){
                        EditText amountTemp = (EditText) i.getChildAt(1);
                        amountTemp.setFocusableInTouchMode(true);
                        listLayout.addView(i);
                    }
                    add.setEnabled(true);
                    remove.setEnabled(true);
                    ratioRadio.setEnabled(true);
                    percentRadio.setEnabled(true);
                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratioRadio.isChecked()){
                    ArrayList<PersonAmount> personAmountList = new ArrayList<PersonAmount>();
                    int totalRatio = 0;
                    for(LinearLayout i : personList){
                        if(!((EditText) i.getChildAt(1)).getText().toString().trim().equalsIgnoreCase("")) {
                            totalRatio += Integer.parseInt(((EditText) i.getChildAt(1)).getText().toString());
                        }
                    }
                    for(LinearLayout i : personList){
                        int ratio;
                        if(!((EditText) i.getChildAt(1)).getText().toString().trim().equalsIgnoreCase("")) {
                            ratio = Integer.parseInt(((EditText) i.getChildAt(1)).getText().toString());
                        }else{
                            ratio = 0;
                        }
                        String nameTemp = ((EditText)i.getChildAt(0)).getText().toString();
                        double amountPerN = Double.parseDouble(billAmount) * ratio / totalRatio;
                        PersonAmount temp = new PersonAmount(nameTemp, amountPerN);
                        personAmountList.add(temp);
                    }
                    Intent i = new Intent(PercentageActivity.this, ResultActivity.class);
                    Bundle args = new Bundle();
                    args.putSerializable("result",(Serializable)personAmountList);
                    i.putExtra("BUNDLE",args);
                    i.putExtra("title", billTitle);
                    i.putExtra("amount", billAmount);
                    startActivity(i);
                }
                else{
                    int totalPercent = 0;
                    for(LinearLayout i : personList){
                        if(!((EditText) i.getChildAt(1)).getText().toString().trim().equalsIgnoreCase("")){
                            totalPercent += Integer.parseInt(((EditText) i.getChildAt(1)).getText().toString());
                        }
                    }
                    if(totalPercent != 100){
                        TextView errorText = findViewById(R.id.errorText);
                        errorText.setVisibility(View.VISIBLE);
                    }else{
                        ArrayList<PersonAmount> personAmountList = new ArrayList<PersonAmount>();
                        for(LinearLayout i : personList){
                            int percent;
                            if(!((EditText) i.getChildAt(1)).getText().toString().trim().equalsIgnoreCase("")){
                                percent = Integer.parseInt(((EditText) i.getChildAt(1)).getText().toString());
                            }
                            else{
                                percent = 0;
                            }
                            String nameTemp = ((EditText)i.getChildAt(0)).getText().toString();
                            double amountPerN = Double.parseDouble(billAmount) * percent / 100;
                            PersonAmount temp = new PersonAmount(nameTemp, amountPerN);
                            personAmountList.add(temp);
                        }
                        Intent i = new Intent(PercentageActivity.this, ResultActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("result",(Serializable)personAmountList);
                        i.putExtra("BUNDLE",args);
                        i.putExtra("title", billTitle);
                        i.putExtra("amount", billAmount);
                        startActivity(i);
                    }
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
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
        amount.setText("0");
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