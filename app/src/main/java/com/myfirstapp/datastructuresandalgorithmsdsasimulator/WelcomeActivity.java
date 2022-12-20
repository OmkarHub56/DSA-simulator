package com.myfirstapp.datastructuresandalgorithmsdsasimulator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private String TAG="WelcomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Log.i(TAG,"onCreatewc called");
    }

    public void mainPageOpen(View view){

        Intent nextPage = new Intent(this, OptionActivity.class);
        startActivity(nextPage);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStopwc called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResumewc called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroywc called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"onStartwc called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPausewc called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestartwc called");
    }
}