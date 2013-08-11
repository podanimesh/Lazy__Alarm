package com.example.lazyalarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends Activity {
	ProgressBar pb;
    protected boolean _active = true;
    protected int _splashTime = 2000; // time to display the splash screen in ms



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        pb= (ProgressBar) findViewById(R.id.progressBar1);
        pb.setMax(10);
        pb.setProgress(2);
        Thread t=new Thread()
        {

            public void run()
            {   
            	pb.setProgress(4);
                try {

                    sleep(2000);
                    pb.setProgress(10);
                    finish();
                    sleep(150);
                    Intent cv=new Intent(SplashScreen.this,AndroidTimeActivity.class/*otherclass*/);
                    startActivity(cv);
                } 

                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
     }