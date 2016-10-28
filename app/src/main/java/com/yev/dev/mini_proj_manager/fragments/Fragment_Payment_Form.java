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

public class Fragment_Payment_Form extends MyFragment implements OnClickListener{

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
			v = inflater.inflate(R.layout.fragment_info_payment, container, false);
		}else{
			v = inflater.inflate(R.layout.fragment_edit_payment, container, false);			
		}
		
		return v;
	}
	
	
	//INITIALIZE INFO FORM
	private void initializeInfoForm(){
		
		data = utility.getPayment(activity, PAYMENT_UID);
		
		v.findViewById(R.id.edit).setOnClickListener(this);

		ImageButton rem_res = (ImageButton)v.findViewById(R.id.remove_restore);
		rem_res.setOnClickListener(this);
		if((int)data.get(DBHelper.COL_REMOVED) == 1){
			rem_res.setImageResource(R.drawable.restore);
		}
				
		((TextView)v.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)data.get(DBHelper.COL_ID), Const.CODE_LENGHT_PAYMENT));
		((TextView)v.findViewById(R.id.t_added)).setText(utility.getFormattedTime((long)data.get(DBHelper.COL_TIME_ADDED), Const.DATE_FORMAT_DATETIME));
		((TextView)v.findViewById(R.id.t_comment)).setText((String)data.get(DBHelper.COL_COMMENT));
		((TextView)v.findViewById(R.id.t_description)).setText((String)data.get(DBHelper.COL_DESCRIPTION));
		
		((TextView)v.findViewById(R.id.t_amount)).setText(utility.getMoneyString((int)data.get(DBHelper.COL_AMOUNT)));
		
	}
	
	//INITIALIZE EDIT FORM
	private void initializeAddEditForm(){
		
		v.findViewById(R.id.save).setOnClickListener(this);
		v.findViewById(R.id.discard).setOnClickListener(this);
				
		switch (MODE) {
		case Const.MODE_NEW:
			((TextView)v.findViewById(R.id.t_code)).setText(R.string.new_payment);
			return;
		default:
			break;
		}
		
			
		data = utility.getPayment(activity, PAYMENT_UID);
		
		if(MODE == Const.MODE_EDIT
				&& (int)data.get(DBHelper.COL_REMOVED) == 1){
			((ImageView)v.findViewById(R.id.icon)).setImageResource(R.drawable.ic_money_removed);
		}
		
		((TextView)v.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)data.get(DBHelper.COL_ID), Const.CODE_LENGHT_PAYMENT));
		((TextView)v.findViewById(R.id.t_added)).setText(utility.getFormattedTime((long)data.get(DBHelper.COL_TIME_ADDED), Const.DATE_FORMAT_DATETIME));
		((EditText)v.findViewById(R.id.et_comment)).setText((String)data.get(DBHelper.COL_COMMENT));
		((EditText)v.findViewById(R.id.et_description)).setText((String)data.get(DBHelper.COL_DESCRIPTION));
		
		((EditText)v.findViewById(R.id.et_amount)).setText(utility.getMoneyString((int)data.get(DBHelper.COL_AMOUNT)));
				
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
		
		cv.put(DBHelper.COL_COMMENT, ((EditText)v.findViewById(R.id.et_comment)).getText().toString());
		cv.put(DBHelper.COL_DESCRIPTION, ((EditText)v.findViewById(R.id.et_description)).getText().toString());		
		cv.put(DBHelper.COL_AMOUNT, (utility.getMoneyInt(((EditText)v.findViewById(R.id.et_amount)).getText().toString())));
						
		if(MODE == Const.MODE_EDIT){//EDIT EXISTING
			
			utility.updateData(activity,
					DBHelper.TABLE_NAME_PAYMENTS,
					cv,
					DBHelper.COL_UID + " LIKE ?",
					new String[] {PAYMENT_UID});
			
		}else{//NEW PAYMENT
						
			PAYMENT_UID = utility.getUid();
			cv.put(DBHelper.COL_UID, PAYMENT_UID);
			cv.put(DBHelper.COL_OWNERS_UID, PROJECT_UID);		
			cv.put(DBHelper.COL_TIME_ADDED, System.currentTimeMillis());		
			
			cv.put(DBHelper.COL_REMOVED, 0);
			
			utility.add_data(activity, cv, DBHelper.TABLE_NAME_PAYMENTS);
			
		}
		
		onDataChanged();
	}
	
	//DISCARD
	private void discard(){
		
		if(MODE == Const.MODE_NEW){
			activity.setContFragment(false, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
		}else{
			activity.setContFragment(false, true, Const.SHOW_PAYMENT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, PAYMENT_UID);
		}

	}
	
	//EDIT
	private void edit(){
		activity.setContFragment(false, true, Const.EDIT_PAYMENT, Const.MODE_EDIT, CUSTOMER_UID, PROJECT_UID, null, PAYMENT_UID);
	}

	//REMOVE
	private void remove(){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COL_REMOVED, 1);
								
		utility.updateData(activity,
				DBHelper.TABLE_NAME_PAYMENTS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {PAYMENT_UID});
		
		onDataChanged();
	}
	
	//RESTORE
	private void restore(){
		ContentValues cv = new ContentValues();
		
		cv.put(DBHelper.COL_REMOVED, 0);
								
		utility.updateData(activity,
				DBHelper.TABLE_NAME_PAYMENTS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {PAYMENT_UID});
		
		onDataChanged();
		
	}
	
	//ON DATA CHANGED
	private void onDataChanged(){
		utility.onProjectDataChanged(activity, CUSTOMER_UID, PROJECT_UID);
		
		activity.setContFragment(false, true, Const.SHOW_PAYMENT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, PAYMENT_UID);
		activity.setNavFragment(false, true, Const.SHOW_PAYMENTS, CUSTOMER_UID, PROJECT_UID, null, PAYMENT_UID);
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
