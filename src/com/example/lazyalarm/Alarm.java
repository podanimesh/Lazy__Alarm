package com.example.lazyalarm;

import java.io.IOException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Alarm extends Activity {
	private MediaPlayer mMediaPlayer; 
	NotificationManager nm;
	int a1,a2,a3,b1,b2,b3,c1,c2,c3;
	TextView t1,t2,t3;
	EditText et1,et2,et3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        init();
    	QuestionGenerate();
        ImageView stopAlarm = (ImageView) findViewById(R.id.stopAlarm);
        stopAlarm.setOnTouchListener(new OnTouchListener() {
           

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if(check()){
					try{
				mMediaPlayer.stop();
				mMediaPlayer.release();
	                finish();
	                AndroidTimeActivity.nm.cancelAll();
	                AndroidTimeActivity.nm=null;
	                if(AndroidTimeActivity.alarmManager!= null && AndroidTimeActivity.repeat_enabled== false){
	                	AndroidTimeActivity.alarmManager= null;
	                	AndroidTimeActivity.alarmEnabled.setChecked(false);
	                }
	                if(AndroidTimeActivity.myTime!= null)
	                	AndroidTimeActivity.myTime= null;
					}
					catch(Exception e){
						Log.e("Error", "Unknown error");
					}
				}
				else{
					Toast.makeText(getBaseContext(), "Please Answer Correctly", Toast.LENGTH_SHORT).show();
				}
	                return false;
			}

			private boolean check() {
				
					if(Integer.parseInt(et1.getText().toString()) == a3 && 
						Integer.parseInt(et2.getText().toString()) == b3 && 
						Integer.parseInt(et3.getText().toString()) == c3){
				return true ;
				}
					else {
						return false;
					}
				
				
			}
        });
 
        playSound(this, getAlarmUri());
        
    }
 
    private void QuestionGenerate() {
		int min=0, max=1000;
    	a1= (int) (Math.random()* 1000);
	
		a2= (int) (Math.random()* 1000);
		b1= (int) (Math.random()* 1000);
		b2= (int) (Math.random()* 1000);
		c1= (int) (Math.random()* 1000);
		c2= (int) (Math.random()* 1000);
		a3= a1+a2;
		if (b2>b1){
			int temp= b1;
			b1=b2;
			b2=temp;
		}
		if(b1==b2){
			b1+=1;
		}
		b3=b1-b2;
		c3= c1+c2;
		t1.setText("Q1: "+ a1 + " + " +a2  );
		t2.setText("Q2: "+ b1 + " - " +b2  );
		t3.setText("Q3: "+ c1 + " + " +c2  );
	}

	private void init() {
		t1= (TextView) findViewById(R.id.end1);
		t2= (TextView) findViewById(R.id.end2);
		t3= (TextView) findViewById(R.id.end3);
		et1= (EditText) findViewById(R.id.ans1);
		et2= (EditText) findViewById(R.id.ans2);
		et3= (EditText) findViewById(R.id.ans3);
		et1.setText("0");
		et2.setText("0");
		et3.setText("0");
	}



	private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            Log.e("Error", "Some Error");
        }
    }
 
    
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
    @Override
    protected void onStart() {
    	
    	super.onStart();
    	
		
    }
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	
    	Toast.makeText(this, "Please Answer the questions", Toast.LENGTH_SHORT).show();
    }
}