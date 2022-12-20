package com.myfirstapp.datastructuresandalgorithmsdsasimulator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Random;

public class activity_normal_queue_demo extends AppCompatActivity {
    private AdView mAdView;
    TextView insert_here_txt;
    //the main linear layout
    LinearLayout list_of_elem;

    //the main arraylist
    ArrayList<LinearLayout> queue = new ArrayList<>();

    //size of queue input
    EditText size_input;

    //the front and rear
    int front=-5,rear=-5;
    //max index of current queue
    int curr_max_index=-5;
    //total elements count
    int total_elem_in_queue=-5;
    //total elements shower textView
    TextView total_elem_shower;

    //enqueue input
    TextView nqInput;

    String TAG="OMK";
    //info about rear,front amd its elements
    TextView rear_shower;
    TextView rear_elem_shower;
    TextView front_shower;
    TextView front_elem_shower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_queue_demo);
        adsSet();
        assignments();
    }

    public void adsSet(){
//        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("780072F37F4DACB81B1935C3613EFC66")).build();
//        MobileAds.setRequestConfiguration(configuration);
        //ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
                Log.d("Ads", adError.toString());
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }
        });
    }
    public void assignments(){
        list_of_elem = findViewById(R.id.elem_list);
        insert_here_txt = findViewById(R.id.textView29);
        size_input = findViewById(R.id.editTextTextPersonName5);
        nqInput = findViewById(R.id.editTextTextPersonName2);
        total_elem_shower = findViewById(R.id.textView53);
        rear_shower = findViewById(R.id.textView39);
        rear_elem_shower = findViewById(R.id.textView41);
        front_shower = findViewById(R.id.textView48);
        front_elem_shower = findViewById(R.id.textView50);
    }

    public void removeQueue(View view){

        //removing thr current queue
        list_of_elem.removeAllViews();
        list_of_elem.addView(insert_here_txt);
        insert_here_txt.setVisibility(View.VISIBLE);
        front=-5;
        rear=-5;
        total_elem_in_queue=-5;
        curr_max_index=-5;
        queue.clear();
        rear_shower.setText("-");
        rear_elem_shower.setText("-");
        front_shower.setText("-");
        front_elem_shower.setText("-");
        total_elem_shower.setText("-");
    }

    public void addQueue(View view){
        String input = String.valueOf(size_input.getText());
        if(input.equals("")){
            Toast.makeText(activity_normal_queue_demo.this, "Please enter a number", Toast.LENGTH_SHORT).show();
            size_input.setBackgroundResource(R.drawable.empty_ones_thin_border_highlight);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    size_input.setBackgroundResource(R.drawable.solid_lgray_black_border_thin);
                }
            },2000);
            return;
        }
        int inp=Integer.parseInt(size_input.getText().toString());
        if(inp>100 || inp<1){
            Toast.makeText(this, "Size less than 1 or above 100 not allowed", Toast.LENGTH_SHORT).show();
            size_input.setText("");
            return;
        }

        //clearing the current queue to add new one
        removeQueue(view);
        //initializing some variables
        curr_max_index = inp-1;
        total_elem_in_queue=0;
        front=0;
        rear=-1;

        //removing the insert queue text
        insert_here_txt.setVisibility(View.GONE);

        //adding the entered no of elements in the main linear layout nad arraylist
        LinearLayout r;
        TextView ind;
        for(int i=0;i<=curr_max_index;i++){

            r = (LinearLayout)getLayoutInflater().inflate(R.layout.one_element_queue,list_of_elem,false);
            ind = r.findViewById(R.id.number_elem);
            ind.setText(String.valueOf(i));
            queue.add(r);
            list_of_elem.addView(r);
        }

        size_input.setText("");
        total_elem_shower.setText("0");
        rear_shower.setText(String.valueOf(rear));
        front_shower.setText(String.valueOf(front));
    }

    public void enqueue(View view){

        //in case of no queue added
        if(curr_max_index==-5){
            Toast.makeText(this, "Insert a queue first before doing any operation", Toast.LENGTH_LONG).show();
            return;
        }
        if (rear==curr_max_index) {
            Toast.makeText(this, "Queue doesn't have enough space / QUEUE OVERFLOW", Toast.LENGTH_SHORT).show();
            nqInput.setText("");
            return;
        }
        //to store the element to enqueue
        String input_elem = nqInput.getText().toString();
        if (input_elem.equals("")) {
            Toast.makeText(activity_normal_queue_demo.this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }
        int inp = Integer.parseInt(input_elem);
        if(inp<1 || inp>100){
            Toast.makeText(activity_normal_queue_demo.this, "Number less than 1 or above 100 not allowed", Toast.LENGTH_SHORT).show();
            nqInput.setText("");
            return;
        }
        //textview to be worked on
        TextView tp;
        rear++;
        tp = getTV(rear);
        tp.setText(input_elem);
        tp.setBackground(getD(1));
        rear_elem_shower.setText(input_elem);
        if (rear == 0) {
            front_elem_shower.setText(input_elem);
        } else if (rear == front + 1) {

        } else if (rear == front) {
            front_elem_shower.setText(input_elem);
        } else {
            tp = getTV(rear - 1);
            tp.setBackground(getD(0));
        }
        rear_shower.setText(String.valueOf(rear));
        nqInput.setText("");
        total_elem_shower.setText(String.valueOf(++total_elem_in_queue));

    }

    public void dequeue(View view){
        if(curr_max_index==-5){
            Toast.makeText(this, "Insert a queue first before doing any operation", Toast.LENGTH_LONG).show();
            return;
        }
        if(front>rear){
            Toast.makeText(this, "Queue has no elements / QUEUE UNDERFLOW", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView tp = getTV(front);

        tp.setText("-");
        tp.setBackground(getD(0));
        front++;
        if(front==rear+1){
            front_elem_shower.setText("-");
            rear_elem_shower.setText("-");
        }
        else{
            //Toast.makeText(this, "here is mistake", Toast.LENGTH_SHORT).show();
            tp = getTV(front);
            tp.setBackground(getD(1));
            front_elem_shower.setText(tp.getText().toString());
        }
        front_shower.setText(String.valueOf(front));
        nqInput.setText("");
        total_elem_shower.setText(String.valueOf(--total_elem_in_queue));
    }

    public void emptyQueue(View view){
        if(curr_max_index==-5){
            Toast.makeText(this, "Please insert a queue", Toast.LENGTH_SHORT).show();
            return;
        }
        if(rear==-1){
            Toast.makeText(this, "Queue is already empty", Toast.LENGTH_SHORT).show();
        }
        else{
            TextView abcf = getTV(rear);
            abcf.setBackground(getD(0));
            abcf.setText("-");
            if(front<=curr_max_index){
                abcf = getTV(front);
                abcf.setText("-");
                abcf.setBackground(getD(0));
            }
            for(int i=front+1;i<=rear-1;i++){
                abcf = getTV(i);
                abcf.setText("-");
            }
            total_elem_in_queue=0;
            total_elem_shower.setText(String.valueOf(total_elem_in_queue));
            rear=-1;
            front=0;
            rear_shower.setText(String.valueOf(-1));
            rear_elem_shower.setText("-");
            front_shower.setText(String.valueOf(0));
            front_elem_shower.setText("-");
            total_elem_in_queue=0;
            total_elem_shower.setText(String.valueOf(total_elem_in_queue));
        }
    }

    public Drawable getD(int a){
        if(a==0){
            return getDrawable(R.drawable.empty_black_border);
        }
        else if(a==1){
            return getDrawable(R.drawable.solid_green_black_border);
        }
        else{
            return getDrawable(R.drawable.solid_yellow_black_border);
        }
    }

    public TextView getTV(int a){
        return queue.get(a).findViewById(R.id.actual_number);
    }

    public void fillRand(View view){
        if(curr_max_index==-5){
            Toast.makeText(activity_normal_queue_demo.this, "Please insert a queue", Toast.LENGTH_SHORT).show();
            return;
        }
        Random rando = new Random();
        TextView a;
        if(front<=curr_max_index){
            a=getTV(front);
            a.setBackground(getD(0));
        }
        if(rear>=0){
            a=getTV(rear);
            a.setBackground(getD(0));
        }
        for(int i=0;i<=curr_max_index;i++){
            a = getTV(i);
            a.setText(String.valueOf(rando.nextInt(100)+1));
        }
        a=getTV(0);
        a.setBackground(getD(1));
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