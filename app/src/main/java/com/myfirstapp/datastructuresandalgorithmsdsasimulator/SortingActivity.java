package com.myfirstapp.datastructuresandalgorithmsdsasimulator;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SortingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private AdView mAdView;
    private static final String TAG = "SortingActivity";
    TextView insert_here_txt;
    //the main linear layout
    LinearLayout list_of_elem;

    //index and element showing linear layout
    LinearLayout ind_and_ele;

    //the main arraylist
    ArrayList<TextView> aray = new ArrayList<>();

    //size of array input
    EditText sizeInput;

    int curr_max_index = -5;

    //element and position input
    TextView elemInput;
    TextView posInput;

    Button main_start_sort;

    Button stop_sort;

    Spinner bub_srt_tim;
    Spinner sort_selector;

    TextView fill_inp;
    int spd_mult;
    ImageButton pause_sort;
    ImageButton resume_sort;

    TextView status_txt;
    String curr_sort="none";
    Button removeArrButton;
    volatile int job_done=1;

    HashMap<String, Integer> spds = new HashMap<>();
    String selected_sort="BUB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
//        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("780072F37F4DACB81B1935C3613EFC66")).build();
//        MobileAds.setRequestConfiguration(configuration);

        //the ads section
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
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
            public void onAdFailedToLoad(LoadAdError error) {
                super.onAdFailedToLoad(error);
                mAdView.loadAd(adRequest);
                Log.d("Ads", error.toString());
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

        list_of_elem = findViewById(R.id.elem_list);
        insert_here_txt = findViewById(R.id.textView29);
        sizeInput = findViewById(R.id.editTextTextPersonName5);

        elemInput = findViewById(R.id.editTextTextPersonName2);
        posInput = findViewById(R.id.editTextTextPersonName4);
        removeArrButton = findViewById(R.id.button16);
        /*
         speeds of sorting (slow,medium,fast)
        */

        bub_srt_tim = findViewById(R.id.spinner2);
        String[] speeds = new String[]{"V  ","S  ","M  ","F  "};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, speeds);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bub_srt_tim.setAdapter(adapter1);
        bub_srt_tim.setOnItemSelectedListener(this);

        sort_selector = findViewById(R.id.spinner3);
        String[] sorts = new String[]{"BUB","MRG","QUI 1","QUI 2"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sorts);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_selector.setAdapter(adapter2);
        sort_selector.setOnItemSelectedListener(this);

        spds.put("V  ", 270);
        spds.put("S  ", 90);
        spds.put("M  ", 30);
        spds.put("F  ", 10);

        main_start_sort = findViewById(R.id.button23);

        stop_sort = findViewById(R.id.button21);
        stop_sort.setEnabled(false);

        resume_sort = findViewById(R.id.imageButton);
        resume_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        resume_sort.setEnabled(false);
        pause_sort = findViewById(R.id.imageButton2);
        pause_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        pause_sort.setEnabled(false);

        fill_inp = findViewById(R.id.editTextNumber2);

        status_txt = findViewById(R.id.textView47);
        status_txt.setText("Status : -");

        //declaring the index and the element textview inside the linear layout
        ind_and_ele = new LinearLayout(SortingActivity.this);
        ind_and_ele.setOrientation(LinearLayout.VERTICAL);

        TextView index = new TextView(SortingActivity.this);
        index.setText("Index");
        index.setTextSize(13);
        ind_and_ele.addView(index);

        TextView elem = new TextView(SortingActivity.this);
        elem.setText("Element");
        elem.setTextSize(13);
        LinearLayout.LayoutParams rtyn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rtyn.setMargins(0, 26, 0, 0);
        elem.setLayoutParams(rtyn);
        ind_and_ele.addView(elem);
    }

    // removing the current array
    public void removeArraySetup(View view) {
        checker=1;
        mrg_stop_flg=1;
        qui_stop_flg=1;

        new CountDownTimer(6000000,40) {
            @Override
            public void onTick(long l) {
                if(job_done==1){
                    removeArrayActual();
                    this.cancel();
                }
            }
            @Override
            public void onFinish() {
            }
        }.start();
    }
    public void removeArrayActual(){

        list_of_elem.removeAllViews();
        list_of_elem.addView(insert_here_txt);
        insert_here_txt.setVisibility(View.VISIBLE);

        curr_max_index = -5;
        aray.clear();

        resume_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        resume_sort.setEnabled(false);
        pause_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        pause_sort.setEnabled(false);
    }



    // adding a new array
    public void addArraySetup(View view) {

        String input = sizeInput.getText().toString();
        if (input.equals("")) {
            Toast.makeText(this, "The size input field is empty", Toast.LENGTH_SHORT).show();
            emptyElmBrnBrdr(sizeInput);
            return;
        }
        int inp = Integer.parseInt(input);
        if (inp > 100 || inp < 1) {
            Toast.makeText(this, "Size should be between 1 and 100", Toast.LENGTH_SHORT).show();
            return;
        }
        // stopping any sorts if any running
        checker=1;
        mrg_stop_flg=1;
        qui_stop_flg=1;
        new CountDownTimer(6000000,30) {
            @Override
            public void onTick(long l) {
                Log.d(TAG,"uio");
                if(job_done==1){
                    addArrayActual(view,inp);
                    this.cancel();
                }
            }
            @Override
            public void onFinish() {
            }
        }.start();
    }
    public void addArrayActual(View view,int inp){

        list_of_elem.removeAllViews();
        curr_max_index = -5;
        aray.clear();

        curr_max_index = inp - 1;
        TextView ind;
        Random rnd = new Random();
        int val;
        int lng = (667 - inp) / inp;
        for (int i = 0; i <= curr_max_index; i++) {

            ind = (TextView) getLayoutInflater().inflate(R.layout.one_elem_sorting, list_of_elem, false);
            val = Math.abs(rnd.nextInt() % 100) + 1;
            ind.setBackgroundColor(Color.rgb(0, 255 - val * 2, 0));
            ind.getLayoutParams().width = lng;
            ind.getLayoutParams().height = val*5;
            ind.requestLayout();

            aray.add(ind);
            list_of_elem.addView(ind);
        }
        Log.d(TAG,String.valueOf(System.currentTimeMillis()));
        sizeInput.setText("");

        pause_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        pause_sort.setEnabled(false);
    }


    // changing element of array
    public void addSetup(View view) {
        //in case of no array added
        if (curr_max_index==-5) {
            Toast.makeText(this, "Please insert an array", Toast.LENGTH_LONG).show();
            return;
        }
        String p = posInput.getText().toString();
        String i = elemInput.getText().toString();
        if (p.equals("")) {
            Toast.makeText(this, "The position / index field is empty", Toast.LENGTH_SHORT).show();
            emptyElmBrnBrdr(posInput);
            return;
        }
        if (i.equals("")) {
            Toast.makeText(this, "The num input field is empty", Toast.LENGTH_SHORT).show();
            emptyElmBrnBrdr(elemInput);
            return;
        }

        checker=1;
        mrg_stop_flg=1;
        qui_stop_flg=1;
        new CountDownTimer(6000000,20) {
            @Override
            public void onTick(long l) {
                if(job_done==1){
                    addActual(p,i);
                    this.cancel();
                }
            }
            @Override
            public void onFinish() {
            }
        }.start();
    }
    public void addActual(String p,String i){
        int pos = Integer.parseInt(p);
        int inp = Integer.parseInt(i);

        if (pos > curr_max_index || pos < 0) {
            Toast.makeText(this, "The position is out of bounds for length " + (curr_max_index + 1), Toast.LENGTH_SHORT).show();
            return;
        }
        if (inp > 100 || inp < 1) {
            Toast.makeText(this, "Size should be between 1 and 100", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView elem_to_change = getTV(pos);
        elem_to_change.getLayoutParams().height = inp * 5;
        elem_to_change.requestLayout();
        elem_to_change.setBackgroundColor(Color.rgb(0, 255 - inp * 2, 0));
    }


    // randomize elements in array
    public void randomizeSetup(View view) {
        if (curr_max_index == -5) {
            Toast.makeText(SortingActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }

        checker=1;
        mrg_stop_flg=1;
        qui_stop_flg=1;
        new CountDownTimer(6000000,40) {
            @Override
            public void onTick(long l) {
                if(job_done==1){
                    randomizeActual();
                    this.cancel();
                }
            }
            @Override
            public void onFinish() {
            }
        }.start();
    }
    public void randomizeActual(){
        Random rnd = new Random();
        int val;
        TextView all_elems;
        for (int i = 0; i < aray.size(); i++) {
            val = Math.abs(rnd.nextInt() % 100) + 1;
            all_elems = getTV(i);
            all_elems.setBackgroundColor(Color.rgb(0, 255 - val * 2, 0));

            all_elems.getLayoutParams().height = val * 5;
            all_elems.requestLayout();
        }
    }


    // fill all elements with a const value
    public void fillAllSetup(View view){

        if(curr_max_index==-5){
            Toast.makeText(SortingActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }
        String inp = fill_inp.getText().toString();
        if(inp.equals("")){
            Toast.makeText(SortingActivity.this, "The input field is empty", Toast.LENGTH_SHORT).show();
            emptyElmBrnBrdr(fill_inp);
            return;
        }
        int const_val=Integer.parseInt(inp);
        if(const_val<0 || const_val>100){
            Toast.makeText(SortingActivity.this, "The value should should be between (1-100)", Toast.LENGTH_SHORT).show();
            return;
        }

        checker=1;
        mrg_stop_flg=1;
        qui_stop_flg=1;
        new CountDownTimer(6000000,40) {
            @Override
            public void onTick(long l) {
                if(job_done==1){
                    fillAllActual(const_val);
                    this.cancel();
                }
            }
            @Override
            public void onFinish() {
            }
        }.start();
    }
    public void fillAllActual(int const_val){

        TextView trav;
        for(int i=0;i<=curr_max_index;i++){
            trav = getTV(i);
            trav.getLayoutParams().height=const_val*5;
            trav.requestLayout();
            setbg(trav,255-2*const_val);
        }
    }


    /*
     * BUBBLE SORT STARTS HERE
     */
    int xBubSrt, yBuBSrt, checker, pauseChecker;
    public void bubSort() {
        if (curr_max_index == -5) {
            Toast.makeText(SortingActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }

        //one elem array
        if(curr_max_index==0){
            Toast.makeText(SortingActivity.this, "Array sorted using Bubble Sort", Toast.LENGTH_SHORT).show();
            return;
        }
        job_done=0;
        curr_sort="Bubble";
        startSortButtonAlters("Bubble");
        checker = 0;
        pauseChecker = 0;
        xBubSrt = 0;
        yBuBSrt = 0;
        CountDownTimer cdm = new CountDownTimer(6000000, spd_mult) {

            public void onTick(long millisUntilFinished) {

                if(checker==1){

                    // in case the setting back to green shade part in the swapElems method executes after
                    // some other execution leading to app crash (Approx. line 8 in the swapElems method)
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            curr_sort="none";
                            if (xBubSrt == 0 && yBuBSrt == curr_max_index) {

                                Toast.makeText(SortingActivity.this, "Array sorted using Bubble Sort", Toast.LENGTH_SHORT).show();

                            }
                            else {
                                Toast.makeText(SortingActivity.this, "Bubble Sort terminated", Toast.LENGTH_SHORT).show();
                            }
                            bub_srt_tim.setEnabled(true);
                            status_txt.setText("Status : -");
                            sortDoneAlters();
                            job_done=1;
                        }
                    },spd_mult);
                    cancel();
                }
                else if (pauseChecker == 1) { }
                else {
                    swapElems(xBubSrt);
                }
            }

            @Override
            public void onFinish() {
                curr_sort="none";
                Toast.makeText(SortingActivity.this, "Bubble Sort terminated", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    public void swapElems(int a) {

        TextView t1 = getTV((Integer) a);
        TextView t2 = getTV((Integer) (a + 1));

        int t1l = t1.getLayoutParams().height;
        int t2l = t2.getLayoutParams().height;
        if (t1l>t2l) {
            t1.setBackgroundColor(Color.rgb(255,102,102));
            t2.setBackgroundColor(Color.rgb(255,102,102));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    t1.getLayoutParams().height = t2l;
                    t2.getLayoutParams().height = t1l;
                    t1.setBackgroundColor(Color.rgb(0,255-2*t1.getLayoutParams().height/5,0));
                    t2.setBackgroundColor(Color.rgb(0,255-2*t2.getLayoutParams().height/5,0));
                    t1.requestLayout();
                    t2.requestLayout();
                }
            },spd_mult/3);
        }
        else{
            t1.setBackgroundColor(Color.rgb(255,255,0));
            t2.setBackgroundColor(Color.rgb(255,255,0));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    t1.setBackgroundColor(Color.rgb(0,255-2*t1.getLayoutParams().height/5,0));
                    t2.setBackgroundColor(Color.rgb(0,255-2*t2.getLayoutParams().height/5,0));
                }
            },spd_mult/3);
        }
        if (xBubSrt == curr_max_index - yBuBSrt - 1) {
            xBubSrt = 0;
            yBuBSrt++;
        }
        else {
            xBubSrt++;
        }
        if (xBubSrt == 0 && yBuBSrt == curr_max_index) {
            checker = 1;
        }
    }

    public void stopBubble() {
        checker = 1;
    }

    public void pauseBubble() {
        pauseChecker = 1;
        status_txt.setText("Status : Bubble Sort paused");
    }

    public void resumeBubble() {
        pauseChecker = 0;
        status_txt.setText("Status : Bubble Sort running...");
    }
    /*
     * BUBBLE SORT ALGO ENDS HERE
     */



    /*
     * MERGE SORT ALGO ENDS HERE
     */
    int m_flag;
    int mrg_stop_flg, mrg_pause_flg, done_with_merging;

    public void mrgSort() {
        if (curr_max_index == -5) {
            Toast.makeText(SortingActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }


        // in case of one elem array
        if(curr_max_index==0){
            Toast.makeText(SortingActivity.this, "Array sorted using Merge Sort", Toast.LENGTH_SHORT).show();
            return;
        }
        job_done=0;
        curr_sort="Merge";
        startSortButtonAlters("Merge");
        m_flag = 0;
        mrg_stop_flg = 0;
        mrg_pause_flg = 0;
        Thread mrg = new Thread(new Runnable() {
            @Override
            public void run() {

                msort(0, curr_max_index);
            }
        });
        mrg.start();

        CountDownTimer checkMergeFinish = new CountDownTimer(6000000,50) {
            @Override
            public void onTick(long l) {
                if(m_flag==1){
                    if(mrg_stop_flg==0){
                        Toast.makeText(SortingActivity.this, "Array sorted using Merge Sort", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(SortingActivity.this, "Merge Sort terminated", Toast.LENGTH_SHORT).show();
                    }
                    status_txt.setText("Status : -");
                    curr_sort="none";
                    sortDoneAlters();
                    job_done=1;
                    this.cancel();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void msort(int l,int r){
        if(r>l){

            int m = (l+r)/2;
            msort(l,m);
            if(mrg_stop_flg==1 && (l!=0 || r!=curr_max_index)){
                return;
            }
            msort(m+1,r);
            if(mrg_stop_flg==1 && r!=curr_max_index){
                return;
            }
            done_with_merging=0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mergeIt(l,m,r);
                }
            });

            while(done_with_merging==0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(l==0 && r==curr_max_index){
                m_flag=1;
            }
        }
    }

    int curr,pehla,dusra;
    int []Lf;
    int []Rf;
    public void mergeIt(int l,int mid,int r){
        if(mrg_stop_flg==1){
            done_with_merging=1;
            return;
        }
        int l1 = mid-l+1;
        int l2 = r-mid;
        Lf = new int[l1];
        Rf = new int[l2];

        for (int i=0;i<l1;i++){
            Lf[i] = getTV(l+i).getLayoutParams().height;
        }
        for (int j=0;j<l2;j++){
            Rf[j] = getTV(mid+1+j).getLayoutParams().height;
        }
        TextView a = getTV(r);
        a.setBackgroundColor(Color.rgb(255,0,0));
        a=getTV(mid);
        a.setBackgroundColor(Color.rgb(255,255,0));
        pehla=0;
        dusra=0;
        curr=l;

        CountDownTimer cdt = new CountDownTimer(6000000,spd_mult*2L) {

            @Override
            public void onTick(long L) {
                if(mrg_stop_flg==1){
                    TextView a = getTV(r);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    a=getTV(mid);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    a=getTV(l);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    mrg_pause_flg=0;
                    done_with_merging=1;
                    this.cancel();
                }
                else if(mrg_pause_flg==1){
                }
                else{
                    if(pehla<l1 && dusra<l2){
                        TextView a = getTV(l);
                        a.setBackgroundColor(Color.rgb(255,0,0));
                        if(Lf[pehla]<=Rf[dusra]){
                            put(1,Lf);
                            pehla++;
                        }
                        else{
                            put(2,Rf);
                            dusra++;
                        }
                        curr++;
                    }
                    else{
                        if(pehla<l1){
                            put(1,Lf);
                            pehla++;
                            curr++;
                        }
                        else if(dusra<l2){
                            put(2,Rf);
                            dusra++;
                            curr++;
                        }
                        else{
                            TextView a = getTV(l);
                            a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                            done_with_merging=1;
                            this.cancel();
                        }
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void put(int a,int []arr){

        TextView t2 = getTV((Integer) curr);
        int g1;
        if(a==1){
            g1 = arr[pehla];
        }
        else{
            g1 = arr[dusra];
        }
        t2.setBackgroundColor(Color.rgb(0,255-2*g1/5,0));
        t2.getLayoutParams().height = g1;
        t2.requestLayout();
    }

    public void stopMerge(){
        mrg_stop_flg=1;
    }

    public void pauseMerge(){
        mrg_pause_flg=1;
        status_txt.setText("Status : Merge Sort paused");
    }

    public void resumeMerge(){
        mrg_pause_flg=0;
        status_txt.setText("Status : Merge Sort running...");
    }
    /*
     * MERGE SORT ALGO ENDS HERE
     */



    /*
     * QUICK SORT ALGO STARTS HERE
     */
    int qui_stop_flg, qui_pause_flg, done_with_part=0;

    int partition_type;
//    public void QS1(View view){
//        partition_type=1;
//        quiSort();
//    }
//    public void QS2(View view){
//        partition_type=2;
//        quiSort();
//    }
    public void quiSort(){
        if (curr_max_index == -5) {
            Toast.makeText(SortingActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }

        //one elem array
        if(curr_max_index==0){
            Toast.makeText(SortingActivity.this, "Array sorted using Quick Sort", Toast.LENGTH_SHORT).show();
            return;
        }
        job_done=0;
        curr_sort="Quick";
        startSortButtonAlters("Quick");
        qui_stop_flg = 0;
        qui_pause_flg = 0;
        Thread qck = new Thread(new Runnable() {
            @Override
            public void run() {
                qsort(0, curr_max_index);
            }
        });
        qck.start();
    }

    public void qsort(int l,int r){

        if(r>l){
            done_with_part=0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(partition_type==1){
                        partition1(l,r);
                    }
                    else if(partition_type==2){
                        partition2(l,r);
                    }
                }
            });
            while(done_with_part==0){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(qui_stop_flg==1 && (l!=0 || r!=curr_max_index)){
                return;
            }
            qsort(l,loin-1);
            if(qui_stop_flg==1 && (l!=0 || r!=curr_max_index)){
                return;
            }
            qsort(loin+1,r);
            if(l==0 && r==curr_max_index){
                Log.d(TAG,"idhar aya");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(qui_stop_flg==1){
                            Toast.makeText(SortingActivity.this, "Quick Sort terminated", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SortingActivity.this, "Array sorted using Quick Sort", Toast.LENGTH_SHORT).show();
                        }
                        status_txt.setText("Status : -");
                        curr_sort="none";
                        sortDoneAlters();
                        job_done=1;
                    }
                });
            }
            Log.d(TAG,String.valueOf(l)+" "+String.valueOf(r));
        }
    }

    // partition function utility variables
    int loin,piv,ptr;
    public void partition1(int low,int high){
        if(qui_stop_flg==1){
            done_with_part=1;
            return;
        }
        TextView a = getTV(high);
        a.setBackgroundColor(Color.rgb(255,0,0));

        loin=low-1;
        piv = getTV(high).getLayoutParams().height;
        ptr=low;

        CountDownTimer ct = new CountDownTimer(9999999,spd_mult) {
            @Override
            public void onTick(long l) {

                if(qui_stop_flg==1){
                    TextView a = getTV(low);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    a=getTV(high);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    a=getTV(loin);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    done_with_part=1;
                    this.cancel();
                }
                else if(qui_pause_flg==1){
                }
                else{
                    TextView a = getTV(low);
                    a.setBackgroundColor(Color.rgb(255,0,0));
                    if(ptr<high){

                        //to change the yellow color of loin back to its color corresponding to length
                        if(loin>=0){
                            TextView tv = getTV(loin);
                            tv.setBackgroundColor(Color.rgb(0,255-2*tv.getLayoutParams().height/5,0));
                        }
                        int curr = getTV(ptr).getLayoutParams().height;
                        if (curr<piv) {
                            loin++;
                            a = getTV(loin);
                            exchange(loin,ptr,1);
                            a.setBackgroundColor(Color.rgb(255,255,0));
                        }
                        ptr++;
                    }
                    else{
                        loin++;
                        exchange(loin,high,1);
                        a = getTV(low);
                        a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                        a = getTV(high);
                        a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                        done_with_part=1;
                        this.cancel();
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


    // exchanging elements method for both QuickSort1 and QuickSort2
    public void exchange(int a,int b,int QuickSortType){
        TextView t1=getTV(a);
        TextView t2=getTV(b);
        int l1=t1.getLayoutParams().height;
        int l2=t2.getLayoutParams().height;

        setbg(t2,255-2*l1/5);
        t2.getLayoutParams().height = l1;
        t2.requestLayout();
        setbg(t1,255-2*l2/5);
        t1.getLayoutParams().height = l2;
        t1.requestLayout();
    }

    int l_pointer,h_pointer,pivInd;
    char currPos;
    public void partition2(int low,int high){
        if(qui_stop_flg==1){
            done_with_part=1;
            return;
        }
        TextView colorMarking = getTV(low);
        colorMarking.setBackgroundColor(Color.rgb(255,0,0));
        colorMarking = getTV(high);
        colorMarking.setBackgroundColor(Color.rgb(255,0,0));
        l_pointer=low;
        h_pointer=high;
        pivInd=low;
        currPos='r';
        piv = getTV(low).getLayoutParams().height;
        CountDownTimer ct = new CountDownTimer(9999999,spd_mult) {
            @Override
            public void onTick(long l) {

                if(qui_stop_flg==1){
                    TextView a = getTV(low);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    a=getTV(high);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    a=getTV(pivInd);
                    a.setBackgroundColor(Color.rgb(0,255-2*a.getLayoutParams().height/5,0));
                    done_with_part=1;
                    this.cancel();
                }
                else if(qui_pause_flg==1){ }
                else{
                    if(currPos=='r'){
                        int height=getTV(h_pointer).getLayoutParams().height;
                        if(height>=piv){
                            getTV(high).setBackgroundColor(Color.rgb(255,0,0));
                            h_pointer--;
                        }
                        else{
                            exchange(h_pointer,pivInd,2);
                            pivInd=h_pointer;
                            getTV(pivInd).setBackgroundColor(Color.rgb(255,255,0));
                            currPos='l';
                        }
                    }
                    else if(currPos=='l'){
                        int height=getTV(l_pointer).getLayoutParams().height;
                        if(height<piv){
                            getTV(low).setBackgroundColor(Color.rgb(255,0,0));
                            l_pointer++;
                        }
                        else{
                            exchange(l_pointer,pivInd,2);
                            pivInd=l_pointer;
                            getTV(pivInd).setBackgroundColor(Color.rgb(255,255,0));
                            currPos='r';
                        }
                    }
                    if(h_pointer==l_pointer){
                        TextView setBackToGreen = getTV(low);
                        setBackToGreen.setBackgroundColor(Color.rgb(0,255-2*setBackToGreen.getLayoutParams().height/5,0));
                        setBackToGreen = getTV(high);
                        setBackToGreen.setBackgroundColor(Color.rgb(0,255-2*setBackToGreen.getLayoutParams().height/5,0));
                        setBackToGreen = getTV(pivInd);
                        setBackToGreen.setBackgroundColor(Color.rgb(0,255-2*setBackToGreen.getLayoutParams().height/5,0));
                        loin=pivInd;
                        done_with_part=1;
                        this.cancel();
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void stopQuick(){
        qui_stop_flg=1;
    }
    public void pauseQuick(){
        qui_pause_flg=1;
        status_txt.setText("Status : Quick Sort paused");
    }
    public void resumeQuick(){
        qui_pause_flg=0;
        status_txt.setText("Status : Quick Sort running...");
    }
    /*
     QUICK SORT ALGO ENDS HERE
     */


    /*
     Changes in buttons to be made either if sort is terminated
     or executed successfully
     */
    public void sortDoneAlters(){
        main_start_sort.setEnabled(true);

        pause_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        pause_sort.setEnabled(false);

        resume_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        resume_sort.setEnabled(false);

        stop_sort.setEnabled(false);
        bub_srt_tim.setEnabled(true);
    }

    public void startSortButtonAlters(String sortType){
        main_start_sort.setEnabled(false);

        stop_sort.setEnabled(true);
        pause_sort.setEnabled(true);
        pause_sort.setBackgroundResource(R.drawable.solid_green_var);
        bub_srt_tim.setEnabled(false);
        status_txt.setText("Status : "+sortType+" Sort running...");
    }

    /*
     common pause, resume, stop methods
     these call the actual methods from here
     */
    public void pauseSort(View view){
        pause_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        pause_sort.setEnabled(false);
        resume_sort.setBackgroundResource(R.drawable.solid_green_var);
        resume_sort.setEnabled(true);

        switch (curr_sort) {
            case "Bubble":
                pauseBubble();
                break;
            case "Merge":
                pauseMerge();
                break;
            case "Quick":
                pauseQuick();
                break;
        }
    }

    public void resumeSort(View view){
        pause_sort.setBackgroundResource(R.drawable.solid_green_var);
        pause_sort.setEnabled(true);
        resume_sort.setBackgroundResource(R.drawable.btn_disabled_bg);
        resume_sort.setEnabled(false);

        switch (curr_sort) {
            case "Bubble":
                resumeBubble();
                break;
            case "Merge":
                resumeMerge();
                break;
            case "Quick":
                resumeQuick();
                break;
        }
    }

    public void stopSort(View view){
        switch (curr_sort) {
            case "Bubble":
                stopBubble();
                break;
            case "Merge":
                stopMerge();
                break;
            case "Quick":
                stopQuick();
                break;
        }
    }

    public void initiateSort(View view){
        if(selected_sort.equals("BUB")){
            bubSort();
        }
        else if(selected_sort.equals("MRG")){
            mrgSort();
        }
        else if(selected_sort.equals("QUI 1")){
            partition_type=1;
            quiSort();
        }
        else if(selected_sort.equals("QUI 2")){
            partition_type=2;
            quiSort();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView==bub_srt_tim){
            String text = String.valueOf(adapterView.getItemAtPosition(i));
            spd_mult = spds.get(text);
        }

        else if(adapterView==sort_selector){
            selected_sort=String.valueOf(adapterView.getItemAtPosition(i));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /*
     some utility functions
     */
    public TextView getTV(int a) {
        return aray.get(a);
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

    public int getGreen(TextView a){
        return Color.green(((ColorDrawable) a.getBackground()).getColor());
    }

    public void setbg(TextView a, int grn_col) {
        a.setBackgroundColor(Color.rgb(0, grn_col, 0));
    }
}