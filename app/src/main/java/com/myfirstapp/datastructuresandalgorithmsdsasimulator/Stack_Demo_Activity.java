package com.myfirstapp.datastructuresandalgorithmsdsasimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Stack_Demo_Activity extends AppCompatActivity{
    int curr_max_index=9;
    int curr_top=-1;

    //arraylist to store linear layout consisting of the elements
    ArrayList<LinearLayout> list_of_elem = new ArrayList<>();

    //the main linear layout
    LinearLayout elem_list;

    //taking input element to PUSH
    EditText stackInput;
    TextView top_of_stack;
    TextView last_popped;
    TextView bottom_of_stack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting the activity_stack_demo
        setContentView(R.layout.activity_stack_demo);

        //main linear layout
        elem_list = findViewById(R.id.linlal);

        //taking input element to PUSH
        stackInput = findViewById(R.id.editTextTextPersonName3);

        //initializing the displaying text views
        top_of_stack = findViewById(R.id.textView23);
        last_popped = findViewById(R.id.textView21);
        bottom_of_stack = findViewById(R.id.textView26);

        //Index displayed from 1 and not 0
        TextView temp;
        LinearLayout new_elem = (LinearLayout) getLayoutInflater().inflate(R.layout.one_element,elem_list,false);
        temp = new_elem.findViewById(R.id.number_elem);
        temp.setText(String.valueOf(0));
        list_of_elem.add(new_elem);
        elem_list.addView(new_elem,0);
        for(int i=1;i<=9;i++){

            new_elem = (LinearLayout) getLayoutInflater().inflate(R.layout.one_element,elem_list,false);
            temp = new_elem.findViewById(R.id.number_elem);
            temp.setText(String.valueOf(i));
            list_of_elem.add(new_elem);
            elem_list.addView(new_elem,0);
        }
    }

    public void addElement(View v) {

        //adding a new block to stack
        LinearLayout new_elem = (LinearLayout) getLayoutInflater().inflate(R.layout.one_element,elem_list,false);
        TextView temp = new_elem.findViewById(R.id.number_elem);
        temp.setText(String.valueOf(++curr_max_index));
        list_of_elem.add(new_elem);
        elem_list.addView(new_elem,0);
    }

    public void push(View view){
        String inp=String.valueOf(stackInput.getText());
        if(inp.equals("")){
            emptyElmBrnBrdr(stackInput);
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }
        //stack full error
        if(curr_top==curr_max_index){
            Toast.makeText(this, "Your Stack doesn't have enough space/STACK OVERFLOW", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.parseInt(inp)<0 || Integer.parseInt(inp)>100){
            Toast.makeText(this, "Number should be between 0 and 100", Toast.LENGTH_SHORT).show();
            return;
        }
        //adding the new element
        TextView b;
        if(curr_top>=0){
            b = list_of_elem.get(curr_top).findViewById(R.id.actual_number);
            b.setBackground(getDrawable(R.drawable.empty_black_border));
        }
        b = list_of_elem.get(++curr_top).findViewById(R.id.actual_number);
        b.setText(stackInput.getText());
        b.setBackground(getDrawable(R.drawable.solid_green_black_border));
        top_of_stack.setText(stackInput.getText());
        TextView c = list_of_elem.get(0).findViewById(R.id.actual_number);
        bottom_of_stack.setText(c.getText());
        stackInput.setText("");
    }

    public void pop(View view){

        //stack empty error
        if(curr_top==-1){
            Toast.makeText(this, "Your Stack is empty/STACK UNDERFLOW", Toast.LENGTH_SHORT).show();
            return;
        }

        //popping the top element
        TextView b = list_of_elem.get(curr_top).findViewById(R.id.actual_number);
        last_popped.setText(b.getText());
        b.setText("-");
        b.setBackground(getDrawable(R.drawable.empty_black_border));

        if(curr_top==0){
            curr_top--;
            top_of_stack.setText("-");
            bottom_of_stack.setText("-");
        }
        else{
            b = list_of_elem.get(--curr_top).findViewById(R.id.actual_number);
            b.setBackground(getDrawable(R.drawable.solid_green_black_border));
            top_of_stack.setText(b.getText());
        }
    }

    public void clear(View view){
        for(int i=curr_top;i>=0;i--){
            TextView a = list_of_elem.get(i).findViewById(R.id.actual_number);
            a.setBackground(getDrawable(R.drawable.empty_black_border));
            a.setText("-");
        }
        curr_top=-1;
        top_of_stack.setText("-");
        last_popped.setText("-");
        bottom_of_stack.setText("-");
    }

    public void emptyElmBrnBrdr(TextView tv){
        tv.setBackgroundResource(R.drawable.empty_ones_thin_border_highlight);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setBackgroundResource(R.drawable.solid_lgray_black_border_thin);
            }
        },2000);
    }
}