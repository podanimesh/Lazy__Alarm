package com.example.lazyalarm;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;




import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment.SavedState;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

public class AndroidTimeActivity extends Activity implements
		OnCheckedChangeListener, Serializable {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	
		lazyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				lazy_button_action();
			}
		});
		setAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				openTimePicker();

			}
		});

	}

	

	private void openTimePicker() {
		timeset = true;
		Calendar calendar = Calendar.getInstance();
		
		timePicker = new TimePickerDialog(AndroidTimeActivity.this,
				onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), false);
		timePicker.setTitle("Set Your Alarm Time");

		timePicker.setCanceledOnTouchOutside(true);
		timePicker.show();

	}

	OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			calNow = Calendar.getInstance();
			calSet = (Calendar) calNow.clone();

			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);

			if (calSet.compareTo(calNow) <= 0) {
				// Today Set time passed, count to tomorrow
				calSet.add(Calendar.DATE, 1);
			}

		}
	};

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

		switch (arg0.getId()) {
		case R.id.alarmEnabled:

			if (!arg0.isChecked()) {
				repeat.setChecked(false);
				lazyOption.setChecked(false);
				lazyOption.setEnabled(false);
				if (nm!=null){
					nm.cancelAll();
					nm=null;
				}
				if (alarmManager != null)
					alarmManager.cancel(pendingIntent);
				Toast.makeText(this, "Alarm Deactivated", Toast.LENGTH_SHORT)
						.show();

			} else {
				if (timeset == true) {
					Intent intent = new Intent(this, Alarm.class);
					// PendingIntent pendingIntent =
					// PendingIntent.getService(getBaseContext(), 0, intent, 0);
					pendingIntent = PendingIntent.getActivity(getBaseContext(),
							0, intent, Notification.DEFAULT_LIGHTS);
					alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							calSet.getTimeInMillis(), pendingIntent);
					Toast.makeText(this,"Alarm Set \n Time Remaining:"+ (calSet.getTimeInMillis() - calNow.getTimeInMillis()) / 60000+ "minutes" , Toast.LENGTH_SHORT).show();
					
					notifyuser();
						
					
					lazyOption.setEnabled(true);
				} else {
					Toast.makeText(this, "Set Time First", Toast.LENGTH_SHORT)
							.show();
					alarmEnabled.setChecked(false);
				}
			}
			break;
		case R.id.repeat:
			if (repeat.isChecked()) {
				if (alarmManager == null || (!alarmEnabled.isChecked())) {
					Toast.makeText(this, "No Repeat", Toast.LENGTH_SHORT)
							.show();
					repeat.setChecked(false);
				} else {
					repeat_enabled = true;
					alarmManager.cancel(pendingIntent);
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
							calSet.getTimeInMillis(),
							AlarmManager.INTERVAL_DAY, pendingIntent);
					Toast.makeText(this, "Daily Repeating Alarm Set",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				if (alarmManager == null || (!alarmEnabled.isChecked())) {
					Toast.makeText(this, "No Repeat Set", Toast.LENGTH_SHORT)
							.show();
					repeat.setChecked(false);
				} else {
					alarmManager.cancel(pendingIntent);
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							calSet.getTimeInMillis(), pendingIntent);
					Toast.makeText(this, "Alarm Changed To Single",
							Toast.LENGTH_SHORT).show();
					repeat_enabled = false;
				}
			}
			break;
		case R.id.lazy:
			if (lazyOption.isChecked()) {
				lazy_layout.setVisibility(View.VISIBLE);
				t1.setText("0");
				t2.setText("0");
			} else {
				if (lazyModeEnabled == true) {
					cancelAlarmLazy();
					lazyModeEnabled=false;
					
				}
				lazy_layout.setVisibility(View.INVISIBLE);
			}

		}
	}

	private void notifyuser() {
		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		n= new Notification(android.R.drawable.ic_lock_idle_alarm, "Alarm Set", (System.currentTimeMillis()));
		i1= new Intent (this, Notify.class );
		pi1= PendingIntent.getActivity(this, 0, i1, 0);   
		n.setLatestEventInfo(AndroidTimeActivity.this, "An Alarm is Set", "Time "+ calSet.get(Calendar.HOUR_OF_DAY) +":"+ calSet.get(Calendar.MINUTE), pi1);
		n.flags= n.FLAG_ONGOING_EVENT;
		nm.notify(0,n);
		
	}



	void lazy_button_action() {
		delay_time = t1.getText().toString();
		repeat_numb = t2.getText().toString();
		if (delay_time.equals(null) || repeat_numb.equals(null)
				|| delay_time.equals("0") || repeat_numb.equals("0")) {
			Toast.makeText(getBaseContext(), " Values cannot be 0",
					Toast.LENGTH_SHORT).show();
		} else {

			setAlarmLazy();

			Toast.makeText(getBaseContext(), "Lazy Alarms Set Successfully",
					Toast.LENGTH_SHORT).show();

		}
	}

	public void setAlarmLazy() {

		if (lazyModeEnabled == true) {
			cancelAlarmLazy();
		}
		pi = new PendingIntent[Integer.parseInt(repeat_numb)];
		am = new AlarmManager[Integer.parseInt(repeat_numb)];
		int duration = Integer.parseInt(delay_time);
		for (int i = 0; i < Integer.parseInt(repeat_numb); i++) {
			Intent intent = new Intent(getBaseContext(), Alarm.class);
			pi[i] = PendingIntent.getActivity(getBaseContext(), i, intent, 0);
			am[i] = (AlarmManager) getSystemService(ALARM_SERVICE);
			am[i].set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis()
					+ ((i) * duration * 60000), pi[i]);

		}
		lazyModeEnabled = true;

	}

	private void cancelAlarmLazy() {
		if (lazyModeEnabled == true) {
			for (int i = 0; i < Integer.parseInt(repeat_numb); i++) {
				am[i].cancel(pi[i]);
			}
		}

	}
	

	private void init() {
		alarmEnabled = (CheckBox) findViewById(R.id.alarmEnabled);
		repeat = (CheckBox) findViewById(R.id.repeat);
		alarmEnabled.setOnCheckedChangeListener(this);
		repeat.setOnCheckedChangeListener(this);
		lazyOption = (CheckBox) findViewById(R.id.lazy);
		lazyOption.setOnCheckedChangeListener(this);
		lazyButton = (Button) findViewById(R.id.lazyButton);
		t1 = (EditText) findViewById(R.id.delay);
		t2 = (EditText) findViewById(R.id.repeatTimes);
		lazy_layout = (LinearLayout) findViewById(R.id.layout);
		setAlarm = (Button) findViewById(R.id.startSetDialog);
		

	}
 

	static TimePicker myTime;
	Button setAlarm, lazyButton;
	TextView alarmTime;
	static CheckBox alarmEnabled, repeat, lazyOption;
	TimePickerDialog timePicker;
	PendingIntent pendingIntent;
	static AlarmManager alarmManager;
	static Calendar calSet;
	Calendar calNow;
	static boolean timeset = false, lazyModeEnabled = false;
	EditText t1, t2;
	LinearLayout lazy_layout;
	static String delay_time;
	static String repeat_numb;
	Intent lazy_service;
	static boolean repeat_enabled = false;
	PendingIntent pi[];// ;= new PendingIntent[Integer.parseInt(repeat_numb)];
	AlarmManager am[];// = new AlarmManager[Integer.parseInt(repeat_numb)];
	Notification n;
	static NotificationManager nm;
	Intent i1;
	PendingIntent pi1;

}