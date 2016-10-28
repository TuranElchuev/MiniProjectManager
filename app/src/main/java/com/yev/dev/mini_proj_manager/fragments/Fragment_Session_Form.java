package com.yev.dev.mini_proj_manager.fragments;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yev.dev.mini_proj_manager.MainActivity;
import com.yev.dev.mini_proj_manager.R;
import com.yev.dev.mini_proj_manager.utilities.Const;
import com.yev.dev.mini_proj_manager.utilities.DBHelper;
import com.yev.dev.mini_proj_manager.utilities.Dialogs;

public class Fragment_Session_Form extends MyFragment implements OnClickListener{

	private View v;
	
	private MainActivity activity;
	
	private HashMap<String, Object> data;
	
	private DurationThread duration_thread;
	
	private TextView duration_view;
	private TextView amount_view;
	
	//DURATION THREAD MESSAGE HANDLER
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			updateDuration();
		}
	}; 
	
//--------------------LIFE CIRCLE--------------------------
	
	//ON ACTIVITY CREATED
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		activity = (MainActivity)getActivity();
		
		activity.setContListener(this);
		
		if(MODE == Const.MODE_DEFAULT){
			initializeInfoForm();
		}else{
			initializeAddEditForm();			
		}
		
		super.onActivityCreated(savedInstanceState);
	}
	
	//ON CREATE
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}
	
	//ON CREATE VIEW
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
			if(MODE == Const.MODE_DEFAULT){
				v = inflater.inflate(R.layout.fragment_info_session, container, false);
			}else{
				v = inflater.inflate(R.layout.fragment_edit_session, container, false);			
			}
					
		return v;
	}
	
	//ON DESTROY VIEW
	@Override
	public void onDestroyView() {
		
		if(duration_thread != null
				&& duration_thread.isAlive()){
			try{
				duration_thread.cancel();
			}catch(Exception e){}
		}
		
		super.onDestroyView();
	}
	
	//INITIALIZE INFO FORM
	private void initializeInfoForm(){
		
		data = utility.getSession(activity, SESSION_UID);
		
		v.findViewById(R.id.edit).setOnClickListener(this);
		v.findViewById(R.id.end).setOnClickListener(this);
		v.findViewById(R.id.duplicate).setOnClickListener(this);

		ImageButton rem_res = (ImageButton)v.findViewById(R.id.remove_restore);
		rem_res.setOnClickListener(this);
		if((int)data.get(DBHelper.COL_REMOVED) == 1){
			rem_res.setImageResource(R.drawable.restore);
		}

		long time_start = (long)data.get(DBHelper.COL_TIME_START);
		long time_end = (long)data.get(DBHelper.COL_TIME_END);

		ImageView icon = (ImageView)v.findViewById(R.id.icon);
		if((int)data.get(DBHelper.COL_REMOVED) == 1){
			icon.setImageResource(R.drawable.ic_time_removed);
		}else if(time_end != 0) {
			icon.setImageResource(R.drawable.ic_completed);
		}else{
			icon.setImageResource(R.drawable.ic_time);
		}		

		((TextView)v.findViewById(R.id.t_start_time)).setText(utility.getFormattedTime(time_start, Const.DATE_FORMAT_DATETIME));		
		((TextView)v.findViewById(R.id.t_end_time)).setText(utility.getFormattedTime(time_end, Const.DATE_FORMAT_DATETIME));
		
		if(time_end == 0){
			
			duration_view = (TextView)v.findViewById(R.id.t_duration);
			amount_view = (TextView)v.findViewById(R.id.t_amount);
			
			duration_thread = new DurationThread();
			duration_thread.start();
			
		}else{
			((TextView)v.findViewById(R.id.t_duration)).setText(utility.getFormattedDuration(time_end - time_start));
			((TextView)v.findViewById(R.id.t_amount)).setText(utility.getMoneyString((int)data.get(DBHelper.COL_AMOUNT)));
			v.findViewById(R.id.end).setVisibility(View.GONE);
		}
		
		((TextView)v.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)data.get(DBHelper.COL_ID), Const.CODE_LENGHT_SESSION));
		
		((TextView)v.findViewById(R.id.t_name)).setText((String)data.get(DBHelper.COL_NAME));
		((TextView)v.findViewById(R.id.t_comment)).setText((String)data.get(DBHelper.COL_COMMENT));
		((TextView)v.findViewById(R.id.t_description)).setText((String)data.get(DBHelper.COL_DESCRIPTION));
		
		((TextView)v.findViewById(R.id.t_rate)).setText(utility.getMoneyString((int)data.get(DBHelper.COL_HOURLY_RATE)));
		
	}
		
	//INITIALIZE EDIT FORM
	private void initializeAddEditForm(){
		
		v.findViewById(R.id.save).setOnClickListener(this);
		v.findViewById(R.id.discard).setOnClickListener(this);
				
		switch (MODE) {
		case Const.MODE_NEW:
			((TextView)v.findViewById(R.id.t_code)).setText(R.string.new_session);
			
			HashMap<String, Object>projectData = utility.getProject(activity, PROJECT_UID);
			int PROJECT_RATE = (int)projectData.get(DBHelper.COL_HOURLY_RATE);
		
			((EditText)v.findViewById(R.id.et_rate)).setText(utility.getMoneyString(PROJECT_RATE));

			((ImageButton)v.findViewById(R.id.save)).setImageResource(R.drawable.start);

			return;

		case Const.MODE_DUPLICATE:
			((ImageButton)v.findViewById(R.id.save)).setImageResource(R.drawable.start);
			break;

		default:
			break;
		}

		
		data = utility.getSession(activity, SESSION_UID);

		long time_end = (long)data.get(DBHelper.COL_TIME_END);

		if(MODE == Const.MODE_EDIT) {
			ImageView icon = (ImageView)v.findViewById(R.id.icon);
			if((int)data.get(DBHelper.COL_REMOVED) == 1){
				icon.setImageResource(R.drawable.ic_time_removed);
			}else if(time_end != 0) {
				icon.setImageResource(R.drawable.ic_completed);
			}else{
				icon.setImageResource(R.drawable.ic_time);
			}
		}

		long time_start = (long)data.get(DBHelper.COL_TIME_START);


		((TextView)v.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)data.get(DBHelper.COL_ID), Const.CODE_LENGHT_SESSION));
		((TextView)v.findViewById(R.id.t_start_time)).setText(utility.getFormattedTime((long)data.get(DBHelper.COL_TIME_START), Const.DATE_FORMAT_DATETIME));
		((EditText)v.findViewById(R.id.et_name)).setText((String)data.get(DBHelper.COL_NAME));
		((EditText)v.findViewById(R.id.et_comment)).setText((String)data.get(DBHelper.COL_COMMENT));
		((EditText)v.findViewById(R.id.et_description)).setText((String)data.get(DBHelper.COL_DESCRIPTION));
				
		((EditText)v.findViewById(R.id.et_rate)).setText(utility.getMoneyString((int)data.get(DBHelper.COL_HOURLY_RATE)));
				
	}

	//MAIN ACTIVITY MESSAGE
	@Override
	public void mainActivityMessage(int message, String data) {
		
		switch (message) {
		case Const.CONT_BACK:
			confirmDiscard();
			break;

		default:
			break;
		}		
		
		super.mainActivityMessage(message, data);
	}

	//ON CLICK
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			confirmSave();
			break;
		case R.id.discard:
			confirmDiscard();
			break;
		case R.id.edit:
			edit();
			break;
		case R.id.end:
			confirmEnd();
			break;
		case R.id.duplicate:
			duplicate();
			break;
		case R.id.remove_restore:
			if((int)data.get(DBHelper.COL_REMOVED) == 1){
				confirmRestore();
			}else{
				confirmRemove();
			}
			break;

		default:
			break;
		}
	}
		
	//END
	private void end(){
		long end_time = System.currentTimeMillis();
		long start_time = (long)data.get(DBHelper.COL_TIME_START);
		int rate = (int)data.get(DBHelper.COL_HOURLY_RATE);
		
		ContentValues cv = new ContentValues();
				
		cv.put(DBHelper.COL_TIME_END, end_time);
		cv.put(DBHelper.COL_AMOUNT, utility.getSessionCosts(end_time - start_time, rate));	
		
		utility.updateData(activity,
				DBHelper.TABLE_NAME_SESSIONS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {SESSION_UID});
		
		onDataChanged();
	}
	
	//DUPLICATE
	private void duplicate(){
		activity.setContFragment(false, true, Const.EDIT_SESSION, Const.MODE_DUPLICATE, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
	}
	
	//SAVE
	private void save(){
		
		ContentValues cv = new ContentValues();
		
		String name = ((EditText)v.findViewById(R.id.et_name)).getText().toString();
		if(name.isEmpty()){
			name = getString(R.string.session);
		}
		
		cv.put(DBHelper.COL_NAME, name);
		cv.put(DBHelper.COL_COMMENT, ((EditText)v.findViewById(R.id.et_comment)).getText().toString());
		cv.put(DBHelper.COL_DESCRIPTION, ((EditText)v.findViewById(R.id.et_description)).getText().toString());		
		
		int rate = utility.getMoneyInt(((EditText)v.findViewById(R.id.et_rate)).getText().toString());
		cv.put(DBHelper.COL_HOURLY_RATE, rate);
				
		
		
		if(MODE == Const.MODE_EDIT){//EDIT EXISTING
						
			long end_time = (long)data.get(DBHelper.COL_TIME_END);
			if(end_time > 0){
				long start_time = (long)data.get(DBHelper.COL_TIME_START);
				cv.put(DBHelper.COL_AMOUNT,	utility.getSessionCosts(end_time - start_time, rate));
			}
								
			utility.updateData(activity,
					DBHelper.TABLE_NAME_SESSIONS,
					cv,
					DBHelper.COL_UID + " LIKE ?",
					new String[] {SESSION_UID});
			
		}else{//NEW SESSION (CAN BE DUPLICATE OR SIMPLY NEW)
						
			SESSION_UID = utility.getUid();
			cv.put(DBHelper.COL_UID, SESSION_UID);
			cv.put(DBHelper.COL_OWNERS_UID, PROJECT_UID);			
			cv.put(DBHelper.COL_TIME_START, System.currentTimeMillis());
			
			cv.put(DBHelper.COL_TIME_END, 0);		
			cv.put(DBHelper.COL_AMOUNT, 0);			
			cv.put(DBHelper.COL_REMOVED, 0);
			
			utility.add_data(activity, cv, DBHelper.TABLE_NAME_SESSIONS);
			
		}
		
		onDataChanged();
	}
	
	//DISCARD
	private void discard(){
		
		if(MODE == Const.MODE_NEW){
			activity.setContFragment(false, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
		}else{
			activity.setContFragment(false, true, Const.SHOW_SESSION_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
		}
	}
	
	//EDIT
	private void edit(){
		activity.setContFragment(false, true, Const.EDIT_SESSION, Const.MODE_EDIT, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
	}

	//REMOVE
	private void remove(){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COL_REMOVED, 1);
								
		utility.updateData(activity,
				DBHelper.TABLE_NAME_SESSIONS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {SESSION_UID});
		
		onDataChanged();
	}
	
	//RESTORE
	private void restore(){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COL_REMOVED, 0);
								
		utility.updateData(activity,
				DBHelper.TABLE_NAME_SESSIONS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {SESSION_UID});
		
		onDataChanged();
	}
	
	//DURATION THREAD
	private class DurationThread extends Thread {
		
		public boolean run = true;	
		
		public DurationThread() {
		}
		
	    public void run() {
	    	while(run){
	    		try{
	    			TimeUnit.SECONDS.sleep(1);
	    		}catch(InterruptedException e){}

	    		mHandler.obtainMessage(0, 0, -1, null).sendToTarget();
	    		
	    	}	       	    	
	    }
	    
	    public void cancel(){
	    	run = false;
	    }

	}

	//UPDATE DURATION
	private void updateDuration(){
		long current_time = System.currentTimeMillis();
		long start_time = (long)data.get(DBHelper.COL_TIME_START);
		int rate = (int)data.get(DBHelper.COL_HOURLY_RATE);
		
		duration_view.setText(utility.getFormattedDuration(current_time - start_time));
		amount_view.setText(utility.getMoneyString((int)utility.getSessionCosts(current_time - start_time, rate))); 
	}
	
	//ON DATA CHANGED
	private void onDataChanged(){
		utility.onProjectDataChanged(activity, CUSTOMER_UID, PROJECT_UID);
		
		activity.setContFragment(false, true, Const.SHOW_SESSION_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
		activity.setNavFragment(false, true, Const.SHOW_SESSIONS, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
		
	}
	

	//-------------------------------DIALOGS--------------------------------------
	
	
	//CONFIRM DISCARD
	private void confirmDiscard(){
		
		Dialogs dialogs = new Dialogs();
		
		String action = getString(R.string.discard);
		String message = "";
		
		final Dialog d = dialogs.getConfirmationDialog(activity, action, message);
		
		d.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				d.cancel();
				
				discard();
			}
		});
		
		d.show();
		
	}
	
	//CONFIRM SAVE
	private void confirmSave(){
		Dialogs dialogs = new Dialogs();
		
		String action = getString(R.string.start);
		String message = "";
		
		final Dialog d = dialogs.getConfirmationDialog(activity, action, message);
		
		d.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				d.cancel();
				
				save();
			}
		});
		
		d.show();
	}
	
	//CONFIRM REMOVE
	private void confirmRemove(){
		Dialogs dialogs = new Dialogs();
		
		String action = getString(R.string.remove);
		String message = "";
		
		final Dialog d = dialogs.getConfirmationDialog(activity, action, message);
		
		d.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				d.cancel();
				
				remove();
			}
		});
		
		d.show();
	}
	
	//CONFIRM RESTORE
	private void confirmRestore(){
		Dialogs dialogs = new Dialogs();
		
		String action = getString(R.string.restore);
		String message = "";
		
		final Dialog d = dialogs.getConfirmationDialog(activity, action, message);
		
		d.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				d.cancel();
				
				restore();
			}
		});
		
		d.show();
	}

	//CONFIRM END
	private void confirmEnd(){
		Dialogs dialogs = new Dialogs();
		
		String action = getString(R.string.end);
		String message = "";
		
		final Dialog d = dialogs.getConfirmationDialog(activity, action, message);
		
		d.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				d.cancel();
				
				end();
			}
		});
		
		d.show();
	}

	
	
}
