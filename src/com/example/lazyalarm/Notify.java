package com.example.lazyalarm;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Notify extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		TextView t= (TextView) findViewById(R.id.notifytext);
		Button b= (Button) findViewById(R.id.notifybutton);
		int hour=AndroidTimeActivity.calSet.get(Calendar.HOUR_OF_DAY);
		int min=AndroidTimeActivity.calSet.get(Calendar.MINUTE);
		if(AndroidTimeActivity.alarmEnabled.isChecked()){
			t.setText("Alarm Set for " + hour+":"+ min);
		}
		else{
			t.setText("No Alarm Set");
			finish();
		}
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try{
				if(AndroidTimeActivity.alarmManager!= null && AndroidTimeActivity.repeat_enabled== false){
                	AndroidTimeActivity.alarmManager= null;
                	AndroidTimeActivity.alarmEnabled.setChecked(false);
                }
                if(AndroidTimeActivity.myTime!= null)
                		AndroidTimeActivity.myTime= null;
                Toast.makeText(getApplicationContext(), "Alarm Disabled",Toast.LENGTH_SHORT ).show();
                finish();
               
				}
				catch(Exception e){
				
					Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}
}
