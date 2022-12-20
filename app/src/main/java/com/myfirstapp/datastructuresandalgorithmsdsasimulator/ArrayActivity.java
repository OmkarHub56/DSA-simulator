package com.myfirstapp.datastructuresandalgorithmsdsasimulator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArrayActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private AdView mAdView;
    private static final String TAG = "WelcomeActivity";
    TextView insert_here_txt;

    //the main linear layout ans list
    LinearLayout list_of_elem;
    ArrayList<TextView> aray = new ArrayList<>();

    int curr_max_index = -5;

    //size of queue input
    EditText size_input, low_bnd, upr_bnd;

    TextView total_elem_shower;

    //enqueue input
    TextView elemInput, posInput;

    //info about rear,front amd its elements
    TextView max_elem_shower, max_elem_index, min_elem_shower, min_elem_index;

    //max and min elem and index store
    int max_elem = -5;
    int max_ind = -5;
    int min_elem = -5;
    int min_ind = -5;

    EditText findElemTxt;
    EditText incrVal;
    EditText incrPos;
    int curr_i_status;
    Spinner incrdcr;
    EditText incrAllVal;

    int incrOption;
    int doneStatus = 0;
    int opr_performed = -5;
    TextView opr_performed_shower;
    List<int[]> sorted_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array);
        Log.i(TAG,"onCreatewarr called");
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

        AdRequest finalAdRequest = adRequest;
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
                mAdView.loadAd(finalAdRequest);
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

        list_of_elem = findViewById(R.id.elem_list);
        insert_here_txt = findViewById(R.id.textView29);
        size_input = findViewById(R.id.editTextTextPersonName5);

        elemInput = findViewById(R.id.editTextTextPersonName2);
        posInput = findViewById(R.id.editTextTextPersonName4);

        total_elem_shower = findViewById(R.id.textView53);
        low_bnd = findViewById(R.id.editTextTextPersonName8);
        upr_bnd = findViewById(R.id.editTextTextPersonName9);

        //details about max/min elem
        max_elem_shower = findViewById(R.id.textView39);
        max_elem_index = findViewById(R.id.textView41);
        min_elem_shower = findViewById(R.id.textView48);
        min_elem_index = findViewById(R.id.textView50);

        findElemTxt = findViewById(R.id.editTextNumber);

        incrVal = findViewById(R.id.editTextNumber3);
        incrPos = findViewById(R.id.editTextNumber4);
        incrdcr = findViewById(R.id.spinner3);
        incrAllVal = findViewById(R.id.editTextNumber5);

        opr_performed_shower = findViewById(R.id.textView57);

        String[] incdc = new String[]{"Dcr", "Icr"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, incdc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incrdcr.setAdapter(adapter);
        incrdcr.setOnItemSelectedListener(this);
        curr_i_status = 1;
    }

    public void removeArray(View view) {

        //removing thr current queue
        list_of_elem.removeAllViews();
        list_of_elem.addView(insert_here_txt);
        insert_here_txt.setVisibility(View.VISIBLE);

        curr_max_index = -5;
        aray.clear();

        max_elem_shower.setText("-");
        max_elem_index.setText("-");
        min_elem_shower.setText("-");
        min_elem_index.setText("-");

        max_elem = max_ind = min_elem = min_ind = opr_performed = - 5;
        total_elem_shower.setText("-");
        opr_performed_shower.setText("-");
    }

    public void addArray(View view) {
        String sizeInput = size_input.getText().toString();
        if (sizeInput.equals("")) {
            emptyElmBrnBrdr(size_input);
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }
        int inp = Integer.parseInt(sizeInput);
        if (inp > 100 || inp < 0) {
            Toast.makeText(this, "Size should be between 0 and 100", Toast.LENGTH_SHORT).show();
            return;
        }

        list_of_elem.removeAllViews();
        aray.clear();

        curr_max_index = inp - 1;
        sorted_list = new ArrayList<int[]>();
        for (int i = 0; i <= curr_max_index; i++) {
            int[] one_el = {i, 0};
            sorted_list.add(one_el);
        }

        //adding the entered no of elements in the main linear layout nad arraylist
        LinearLayout r;
        TextView ind;
        for (int i = 0; i <= curr_max_index; i++) {
            r = (LinearLayout) getLayoutInflater().inflate(R.layout.one_element_queue, list_of_elem, false);
            ind = r.findViewById(R.id.number_elem);
            ind.setText(String.valueOf(i));

            //adding the actual element to ll and al
            aray.add(r.findViewById(R.id.actual_number));
            list_of_elem.addView(r);
        }

        size_input.setText("");
        total_elem_shower.setText(String.valueOf(curr_max_index + 1));

        //setting all elems to 0
        reset(view);

        //setting the views
        max_elem_shower.setText("0");
        max_elem_index.setText("0");
        min_elem_shower.setText("0");
        min_elem_index.setText("0");
        opr_performed_shower.setText("0");
        max_elem = max_ind = min_elem = min_ind = opr_performed = 0;
    }

    public void add(View view) {

        //in case of no array added
        if (curr_max_index == -5) {
            Toast.makeText(this, "Please insert an array", Toast.LENGTH_LONG).show();
            return;
        }

        //position check
        String temp = posInput.getText().toString();
        if (temp.equals("")) {
            emptyElmBrnBrdr(posInput);
            Toast.makeText(this, "Please enter a position / index", Toast.LENGTH_SHORT).show();
            return;
        }
        int pos = Integer.parseInt(temp);
        if (pos > curr_max_index || pos < 0) {
            Toast.makeText(this, "The position is out of bounds for length " + (curr_max_index + 1), Toast.LENGTH_SHORT).show();
            posInput.setText("");
            return;
        }

        //element check
        temp = elemInput.getText().toString();
        if (temp.equals("")) {
            emptyElmBrnBrdr(elemInput);
            Toast.makeText(this, "Please enter a element", Toast.LENGTH_SHORT).show();
            return;
        }
        int inp = Integer.parseInt(temp);
        if (inp > 100 || inp < 0) {
            Toast.makeText(this, "Number should be between 0 and 100", Toast.LENGTH_SHORT).show();
            elemInput.setText("");
            return;
        }

        putIntoSortedArray(pos, inp);
        setMinMax();
        getTV(pos).setText(temp);
        elemInput.setText("");
        posInput.setText("");
        opr_performed_shower.setText(String.valueOf(++opr_performed));
    }

    public void reset(View view) {
        if (curr_max_index==-5) {
            Toast.makeText(this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < aray.size(); i++) {
            getTV(i).setText("0");
            sorted_list.get(i)[0] = i;
            sorted_list.get(i)[1] = 0;
        }
        max_elem_index.setText("0");
        min_elem_index.setText("0");
        max_elem_shower.setText("0");
        min_elem_shower.setText("0");
        max_elem = min_elem = 0;
        opr_performed_shower.setText(String.valueOf(++opr_performed));
    }


    public void randomize(View view) {
        if (curr_max_index == -5) {
            Toast.makeText(ArrayActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }
        String temp1=String.valueOf(low_bnd.getText()),temp2=String.valueOf(upr_bnd.getText());

        //empty input condition
        if(temp1.equals("") && temp2.equals("")){
            Toast.makeText(this, "At least one bound value should be input", Toast.LENGTH_SHORT).show();
            emptyElmBrnBrdr(low_bnd);
            emptyElmBrnBrdr(upr_bnd);
            return;
        }
        int lb=0,ub=0;
        if(temp1.equals("")){
            ub=Integer.parseInt(temp2);
            if(ub>100){
                Toast.makeText(this, "The upper bound should be smaller than 100", Toast.LENGTH_SHORT).show();
                return;
            }
            lb=0;
        }
        else if(temp2.equals("")){
            lb = Integer.parseInt(temp1);
            if(lb<0){
                Toast.makeText(this, "The lower bound should be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            ub=100;
        }
        else{
            lb = Integer.parseInt(temp1);
            ub = Integer.parseInt(temp2);
            if(ub < lb) {
                Toast.makeText(this, "Upper bound cannot be less than lower bound", Toast.LENGTH_SHORT).show();
                return;
            }
            if(lb<0 || ub>100){
                Toast.makeText(this, "Both bounds should be between 0 and 100", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Random rnd = new Random();
        int val;
        for (int i = 0; i < aray.size(); i++) {
            val = Math.abs(rnd.nextInt() % (ub - lb + 1)) + lb;
            putIntoSortedArray(i, val);
            getTV(i).setText(String.valueOf(val));
            setMinMax();
        }
        opr_performed_shower.setText(String.valueOf(++opr_performed));
    }

    public void sortArray(View view) {
        if (curr_max_index == -5) {
            Toast.makeText(ArrayActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i <= curr_max_index; i++) {
            sorted_list.get(i)[0] = i;
            getTV(i).setText(String.valueOf(sorted_list.get(i)[1]));
        }
        max_elem_index.setText(String.valueOf(curr_max_index));
        min_elem_index.setText(String.valueOf(0));
        opr_performed_shower.setText(String.valueOf(++opr_performed));
        Toast.makeText(ArrayActivity.this, "Array sorted", Toast.LENGTH_SHORT).show();
    }

    public void findElem(View view) {
        if (curr_max_index == -5) {
            Toast.makeText(ArrayActivity.this, "Please insert an array", Toast.LENGTH_SHORT).show();
            return;
        }
        String temp=String.valueOf(findElemTxt.getText());
        if(temp.equals("")){
            emptyElmBrnBrdr(findElemTxt);
            Toast.makeText(this, "Please input a search element", Toast.LENGTH_SHORT).show();
            return;
        }
        int toFind = Integer.parseInt(temp);

        //wrote this before opr_performed because would need to write 2 times for element found and not found
        opr_performed_shower.setText(String.valueOf(++opr_performed));
        int low = 0, high = curr_max_index;
        while (low <= high) {
            int mid = (low + high) / 2;
            int found = sorted_list.get(mid)[1];
            if (found == toFind) {
                Toast.makeText(ArrayActivity.this, "Element present at index " + sorted_list.get(mid)[0], Toast.LENGTH_SHORT).show();
                return;
            } else if (toFind > found) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        Toast.makeText(ArrayActivity.this, "Element not present", Toast.LENGTH_SHORT).show();
    }

    public void incrOne(View view) {
        //in case of no array added
        if (curr_max_index == -5) {
            Toast.makeText(this, "Please insert an array", Toast.LENGTH_LONG).show();
            return;
        }

        //position check
        String temp = incrPos.getText().toString();
        if (temp.equals("")) {
            emptyElmBrnBrdr(incrPos);
            Toast.makeText(this, "Please enter a position / index", Toast.LENGTH_SHORT).show();
            return;
        }
        int pos = Integer.parseInt(temp);
        if (pos > curr_max_index || pos < 0) {
            Toast.makeText(this, "The position is out of bounds for length " + (curr_max_index + 1), Toast.LENGTH_SHORT).show();
            return;
        }


        temp=incrVal.getText().toString();
        if (temp.equals("")) {
            emptyElmBrnBrdr(incrVal);
            Toast.makeText(this, "Please enter a element", Toast.LENGTH_SHORT).show();
            return;
        }
        int val = Integer.parseInt(temp);

        TextView tp = getTV(pos);
        int curr = Integer.parseInt(tp.getText().toString());
        int modcurr = curr + val * curr_i_status;
        if (modcurr < 0 || modcurr > 100) {
            Toast.makeText(this, "The incremented / decremented value goes out of the range (0 - 100)", Toast.LENGTH_SHORT).show();
            elemInput.setText("");
            return;
        }
        putIntoSortedArray(pos, modcurr);
        setMinMax();
        tp.setText(String.valueOf(modcurr));
        opr_performed_shower.setText(String.valueOf(++opr_performed));
    }

    public void incrAll(View view) {

        //in case of no array added
        if (curr_max_index == -5) {
            Toast.makeText(this, "Please insert an array", Toast.LENGTH_LONG).show();
            return;
        }
        String temp = String.valueOf(incrAllVal.getText());
        if (temp.equals("")) {
            emptyElmBrnBrdr(incrAllVal);
            Toast.makeText(this, "Please enter a element", Toast.LENGTH_SHORT).show();
            return;
        }
        int val = Integer.parseInt(temp);
        String[] choices = {"CANCEL OPERATION", "SET TO TERMINALS", "CHANGE THE POSSIBLE ONES ONLY"};
        int modcurr;
        if (curr_i_status == -1) {
            modcurr = min_elem + val * curr_i_status;
        } else {
            modcurr = max_elem + val * curr_i_status;
        }
        if (modcurr >= 0 && modcurr <= 100) {
            for (int i = 0; i <= curr_max_index; i++) {
                int curr = Integer.parseInt(getTV(i).getText().toString());
                modcurr = curr + val * curr_i_status;
                putIntoSortedArray(i, modcurr);
                getTV(i).setText(String.valueOf(modcurr));
            }
            setMinMax();
            opr_performed_shower.setText(String.valueOf(++opr_performed));
            return;
        }
        doneStatus = 0;
        AlertDialog.Builder ask = new AlertDialog.Builder(ArrayActivity.this);
        ask.setTitle("Element limit exceeded");
        ask.setSingleChoiceItems(choices, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                incrOption = i;
            }
        });
        ask.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doneStatus = 1;
            }
        });
        ask.create().show();

        new CountDownTimer(9999999, 100) {
            @Override
            public void onTick(long l) {
                if (doneStatus == 1) {
                    incrAccToOption(incrOption, val);
                    cancel();
                }
            }
            @Override
            public void onFinish() {

            }
        }.start();

    }

    public void incrAccToOption(int option, int incVal) {
        if (option == 0) {
            Toast.makeText(ArrayActivity.this, "Incr / Dcr operation cancelled", Toast.LENGTH_SHORT).show();
            return;
        }
        int modcurr;
        if (option == 1) {
            for (int i = 0; i <= curr_max_index; i++) {
                int curr = Integer.parseInt(getTV(i).getText().toString());
                modcurr = curr + incVal * curr_i_status;
                if (modcurr < 0) {
                    putIntoSortedArray(i, 0);
                    getTV(i).setText("0");
                } else if (modcurr > 100) {
                    putIntoSortedArray(i, 100);
                    getTV(i).setText("100");
                } else {
                    putIntoSortedArray(i, modcurr);
                    getTV(i).setText(String.valueOf(modcurr));
                }
            }
        } else if (option == 2) {
            for (int i = 0; i <= curr_max_index; i++) {
                int curr = Integer.parseInt(getTV(i).getText().toString());
                modcurr = curr + incVal * curr_i_status;
                if (modcurr >= 0 && modcurr <= 100) {
                    putIntoSortedArray(i, modcurr);
                    getTV(i).setText(String.valueOf(modcurr));
                }
            }
        }
        setMinMax();
        opr_performed_shower.setText(String.valueOf(++opr_performed));
    }

    public void resetOprPerformed(View view) {
        if (curr_max_index == -5) {
            return;
        }
        opr_performed = 0;
        opr_performed_shower.setText("0");
    }

    public TextView getTV(int a) {
        return aray.get(a);
    }

    public void setMinMax() {
        max_elem = sorted_list.get(curr_max_index)[1];
        max_elem_shower.setText(String.valueOf(max_elem));
        max_ind = sorted_list.get(curr_max_index)[0];
        max_elem_index.setText(String.valueOf(max_ind));

        min_elem = sorted_list.get(0)[1];
        min_elem_shower.setText(String.valueOf(min_elem));
        min_ind = sorted_list.get(0)[0];
        min_elem_index.setText(String.valueOf(min_ind));
    }

    public void putIntoSortedArray(int pos, int elem_to_add) {
        TextView tp = getTV(pos);
        int elem_to_replace = Integer.parseInt(String.valueOf(tp.getText()));
//        Log.d(TAG, "START_START_START_START_START_START_START_START_START_START");
//        Log.d(TAG, "element to replace : " + elem_to_replace);
        int low = 0, high = curr_max_index;
        int ind = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            int curr = sorted_list.get(mid)[1];
            if (curr == elem_to_replace) {
                ind = mid;
                break;
            } else if (elem_to_replace > curr) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        int ind_copy = ind;
        int flag = 0;
        while (ind <= curr_max_index && sorted_list.get(ind)[1] == elem_to_replace) {
            if (sorted_list.get(ind)[0] == pos) {
                flag = 1;
                break;
            } else {
                ind++;
            }
        }
        if (flag == 0) {
            while (ind_copy >= 0 && sorted_list.get(ind_copy)[1] == elem_to_replace) {
                if (sorted_list.get(ind_copy)[0] == pos) {
                    break;
                } else {
                    ind_copy--;
                }
            }
        }
        if (flag == 0) {
            ind = ind_copy;
        }
        sorted_list.remove(ind);
        ind = 0;
        int[] putting = {pos, elem_to_add};
        for (int i = curr_max_index - 1; i >= 0; i--) {
            int a = sorted_list.get(i)[1];
            if (elem_to_add >= a) {
                ind = i + 1;
                sorted_list.add(ind, putting);
                return;
            }

        }
        sorted_list.add(ind, putting);
        for (int j = 0; j <= curr_max_index; j++) {
            Log.d(TAG, sorted_list.get(j)[0] + " " + sorted_list.get(j)[1]);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        curr_i_status = (i - 1) * 2 + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        curr_i_status = 1;
    }

    public void emptyElmBrnBrdr(TextView tv) {
        tv.setBackgroundResource(R.drawable.empty_ones_thin_border_highlight);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv.setBackgroundResource(R.drawable.solid_lgray_black_border_thin);
            }
        }, 2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStoparr called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResumearr called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroyarr called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStartarr called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPausearr called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestartarr called");
    }
}