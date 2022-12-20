package com.myfirstapp.datastructuresandalgorithmsdsasimulator;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class activity_circular_queue_demo extends activity_normal_queue_demo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_queue_demo);
        super.adsSet();
        super.assignments();
    }

    @Override
    public void enqueue(View view){

        //in case of no queue added
        if(curr_max_index==-5){
            Toast.makeText(this, "Please insert a queue", Toast.LENGTH_LONG).show();
            return;
        }
        if(total_elem_in_queue-1==curr_max_index){
            Toast.makeText(this, "Queue doesn't have enough space / QUEUE OVERFLOW", Toast.LENGTH_SHORT).show();
            nqInput.setText("");
            return;
        }
        //to store the element to enqueue
        String input_elem = nqInput.getText().toString();
        if (input_elem.equals("")) {
            Toast.makeText(activity_circular_queue_demo.this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }
        int inp = Integer.parseInt(input_elem);
        if(inp<1 || inp>100){
            Toast.makeText(activity_circular_queue_demo.this, "Number less than 1 or above 100 not allowed", Toast.LENGTH_SHORT).show();
            nqInput.setText("");
            return;
        }
        //textview to be worked on
        TextView tp;
        rear = (rear+1)%(curr_max_index+1);
        tp = getTV(rear);
        tp.setText(input_elem);
        tp.setBackground(getD(1));
        rear_elem_shower.setText(input_elem);
        if(rear==front){
            front_elem_shower.setText(input_elem);
        }
        else if(rear==(front+1)%(curr_max_index+1)){
            getTV(front).setBackground(getD(2));
        }
        else{
            tp = getTV((rear+curr_max_index)%(curr_max_index+1));
            tp.setBackground(getD(0));
        }
        rear_shower.setText(String.valueOf(rear));
        nqInput.setText("");
        total_elem_shower.setText(String.valueOf(++total_elem_in_queue));
    }
    @Override
    public void dequeue(View view){
        if(curr_max_index==-5){
            Toast.makeText(this, "Insert a queue first before doing any operation", Toast.LENGTH_LONG).show();
        }
        if(total_elem_in_queue==0){
            Toast.makeText(this, "Queue has no elements / QUEUE UNDERFLOW", Toast.LENGTH_SHORT).show();
        }
        else{

            TextView tp = getTV(front);

            tp.setText("-");
            tp.setBackground(getD(0));
            front = (front+1)%(curr_max_index+1);
            if(front==(rear+1)%(curr_max_index+1)){
                front_elem_shower.setText("-");
                rear_elem_shower.setText("-");
            }
            else{
                tp = getTV(front);
                tp.setBackground(getD(2));
                front_elem_shower.setText(tp.getText().toString());
            }
            front_shower.setText(String.valueOf(front));
            nqInput.setText("");
            total_elem_shower.setText(String.valueOf(--total_elem_in_queue));
        }
    }
    @Override
    public void emptyQueue(View view){
        if(curr_max_index==-5){
            Toast.makeText(this, "Please insert a queue", Toast.LENGTH_SHORT).show();
            return;
        }
        if(rear==-1){
            Toast.makeText(this, "Queue is already empty", Toast.LENGTH_SHORT).show();
        }
        else{
            for(int i=Math.min(front,rear);i<=Math.max(front,rear);i++){
                getTV(i).setText("-");
                getTV(i).setBackground(getD(0));
            }
            total_elem_in_queue=0;
            total_elem_shower.setText(String.valueOf(total_elem_in_queue));
            rear=-1;
            front=0;
            rear_shower.setText(String.valueOf(rear));
            rear_elem_shower.setText("-");
            front_shower.setText(String.valueOf(front));
            front_elem_shower.setText("-");
        }

    }
    @Override
    public void fillRand(View view){
        if(curr_max_index==-5){
            Toast.makeText(activity_circular_queue_demo.this, "Please insert a queue", Toast.LENGTH_SHORT).show();
            return;
        }
        Random rando = new Random();
        TextView a;
        a=getTV(front);
        a.setBackground(getD(0));
        if(rear>=0){
            a=getTV(rear);
            a.setBackground(getD(0));
        }
        for(int i=0;i<=curr_max_index;i++){
            a = getTV(i);
            a.setText(String.valueOf(rando.nextInt(100)+1));
        }
        a=getTV(0);
        a.setBackground(getD(2));
        a=getTV(curr_max_index);
        a.setBackground(getD(1));

        front=0;
        rear=curr_max_index;
        rear_shower.setText(String.valueOf(rear));
        front_shower.setText(String.valueOf(front));

        rear_elem_shower.setText(getTV(curr_max_index).getText());
        front_elem_shower.setText(getTV(0).getText());
        total_elem_shower.setText(String.valueOf(curr_max_index+1));
        total_elem_in_queue=curr_max_index+1;
    }
}