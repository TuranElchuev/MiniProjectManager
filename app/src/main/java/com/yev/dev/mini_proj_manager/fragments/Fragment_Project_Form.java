package com.yev.dev.mini_proj_manager.fragments;

import java.util.HashMap;

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
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
import com.yev.dev.mini_proj_manager.utilities.Utility;

public class Fragment_Project_Form extends MyFragment implements OnClickListener {

	private View v;
	
	private MainActivity activity;
	
	private HashMap<String, Object> data;
	
	
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
			v = inflater.inflate(R.layout.fragment_info_project, container, false);
		}else{
			v = inflater.inflate(R.layout.fragment_edit_project, container, false);			
		}
		
		return v;
	}
	
	
	//INITIALIZE INFO FORM
	private void initializeInfoForm(){
		
		data = utility.getProject(activity, PROJECT_UID);
		
		v.findViewById(R.id.edit).setOnClickListener(this);

		ImageButton rem_res = (ImageButton)v.findViewById(R.id.remove_restore);
		rem_res.setOnClickListener(this);
		if((int)data.get(DBHelper.COL_REMOVED) == 1){
			rem_res.setImageResource(R.drawable.restore);
		}
				
		ImageView icon = (ImageView)v.findViewById(R.id.icon);
		if((int)data.get(DBHelper.COL_REMOVED) == 1){
			icon.setImageResource(R.drawable.ic_project_removed);
		}else{
			icon.setImageResource(R.drawable.ic_project);
		}
		
		
		((TextView)v.findViewById(R.id.t_added)).setText((String)data.get(DBHelper.COL_TIME_ADDED));
		//((TextView)v.findViewById(R.id.t_deadline)).setText((String)data.get(DBHelper.COL_DEADLINE));
		((TextView)v.findViewById(R.id.t_last_action)).setText((String)data.get(DBHelper.COL_LAST_ACTION));
		((TextView)v.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)data.get(DBHelper.COL_PROJECT_CODE), Const.CODE_LENGHT_PROJECT));
		 
		((TextView)v.findViewById(R.id.t_total_amount)).setText(utility.getMoneyString((int)data.get(Utility.HM_TOTAL_AMOUNT)));
		((TextView)v.findViewById(R.id.t_total_time)).setText(utility.getFormattedDuration((long)data.get(Utility.HM_TOTAL_TIME)));
		
		((TextView)v.findViewById(R.id.t_name)).setText((String)data.get(DBHelper.COL_NAME));
		((TextView)v.findViewById(R.id.t_comment)).setText((String)data.get(DBHelper.COL_COMMENT));
		((TextView)v.findViewById(R.id.t_description)).setText((String)data.get(DBHelper.COL_DESCRIPTION));
		
		((TextView)v.findViewById(R.id.t_rate)).setText(utility.getMoneyString((int)data.get(DBHelper.COL_HOURLY_RATE)));
		
		TextView t_balance = (TextView)v.findViewById(R.id.t_balance);
		int balance = (int)data.get(DBHelper.COL_BALANCE);
		t_balance.setText(utility.getMoneyString(balance));
		t_balance.setTextColor(utility.getMoneyColor(activity, balance));
		
	}
	
	//INITIALIZE EDIT FORM
	private void initializeAddEditForm(){
		
		v.findViewById(R.id.save).setOnClickListener(this);
		v.findViewById(R.id.discard).setOnClickListener(this);
				
		switch (MODE) {
		case Const.MODE_NEW:
			
			HashMap<String, Object>customerData = utility.getCustomer(activity, CUSTOMER_UID);
			int CUSTOMER_RATE = (int)customerData.get(DBHelper.COL_HOURLY_RATE);
			
			((TextView)v.findViewById(R.id.t_code)).setText(R.string.new_project);
			
			((EditText)v.findViewById(R.id.et_rate)).setText(utility.getMoneyString(CUSTOMER_RATE));
			
			return;

		default:
			break;
		}
		
		
		data = utility.getProject(activity, PROJECT_UID);
		
		if(MODE == Const.MODE_EDIT
				&& (int)data.get(DBHelper.COL_REMOVED) == 1){
			((ImageView)v.findViewById(R.id.icon)).setImageResource(R.drawable.ic_project_removed);
		}
		
		((TextView)v.findViewById(R.id.t_added)).setText((String)data.get(DBHelper.COL_TIME_ADDED));		
		((TextView)v.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)data.get(DBHelper.COL_PROJECT_CODE), Const.CODE_LENGHT_PROJECT));
		//((EditText)v.findViewById(R.id.et_deadline)).setText((String)data.get(DBHelper.COL_DEADLINE));
		
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
	
	//SAVE
	private void save(){
		
		ContentValues cv = new ContentValues();
		
		String name = ((EditText)v.findViewById(R.id.et_name)).getText().toString();
		if(name.isEmpty()){
			name = getString(R.string.project);
		}
		
		cv.put(DBHelper.COL_NAME, name);
		cv.put(DBHelper.COL_COMMENT, ((EditText)v.findViewById(R.id.et_comment)).getText().toString());
		cv.put(DBHelper.COL_DESCRIPTION, ((EditText)v.findViewById(R.id.et_description)).getText().toString());		
		cv.put(DBHelper.COL_HOURLY_RATE, (utility.getMoneyInt(((EditText)v.findViewById(R.id.et_rate)).getText().toString())));
		cv.put(DBHelper.COL_DEADLINE, (System.currentTimeMillis()));
						
		if(MODE == Const.MODE_EDIT){//EDIT EXISTING
			
			utility.updateData(activity,
					DBHelper.TABLE_NAME_PROJECTS,
					cv,
					DBHelper.COL_UID + " LIKE ?",
					new String[] {PROJECT_UID});
			
		}else{//NEW PROJECT
			
			HashMap<String, Object>customerData = utility.getCustomer(activity, CUSTOMER_UID);
			int CUSTOMER_CODE = (int)customerData.get(DBHelper.COL_CUSTOMER_CODE);
			
			PROJECT_UID = utility.getUid();
			cv.put(DBHelper.COL_UID, PROJECT_UID);
			cv.put(DBHelper.COL_PROJECT_CODE, utility.getProjectCode(activity, CUSTOMER_UID, CUSTOMER_CODE));
			cv.put(DBHelper.COL_OWNERS_UID, CUSTOMER_UID);
			cv.put(DBHelper.COL_CUSTOMER_CODE, CUSTOMER_CODE);			
			cv.put(DBHelper.COL_TIME_ADDED, System.currentTimeMillis());
			cv.put(DBHelper.COL_LAST_ACTION, System.currentTimeMillis());
			cv.put(DBHelper.COL_BALANCE, 0);		
			
			cv.put(DBHelper.COL_REMOVED, 0);
			
			utility.add_data(activity, cv, DBHelper.TABLE_NAME_PROJECTS);

		}
		
		onDataChanged();
	}
	
	//DISCARD
	private void discard(){

		if(MODE == Const.MODE_NEW){
			activity.setContFragment(false, true, Const.SHOW_CUSTOMER_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, null, null, null);
		}else{
			activity.setContFragment(false, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
		}
	}
	
	//EDIT
	private void edit(){
		activity.setContFragment(false, true, Const.EDIT_PROJECT, Const.MODE_EDIT, CUSTOMER_UID, PROJECT_UID, null, null);
	}
	
	//REMOVE
	private void remove(){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COL_REMOVED, 1);
								
		utility.updateData(activity,
				DBHelper.TABLE_NAME_PROJECTS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {PROJECT_UID});
		
		onDataChanged();
	}
	
	//RESTORE
	private void restore(){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COL_REMOVED, 0);
								
		utility.updateData(activity,
				DBHelper.TABLE_NAME_PROJECTS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {PROJECT_UID});
		
		onDataChanged();
	}

	//ON DATA CHANGED
	private void onDataChanged(){
		utility.onProjectDataChanged(activity, CUSTOMER_UID, PROJECT_UID);
		
		activity.setContFragment(false, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
		activity.setNavFragment((MODE == Const.MODE_NEW), true, Const.SHOW_SESSIONS, CUSTOMER_UID, PROJECT_UID, null, null);
		
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
		
		String action = getString(R.string.save);
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


}
