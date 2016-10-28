package com.yev.dev.mini_proj_manager.utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;

import com.yev.dev.mini_proj_manager.MainActivity;
import com.yev.dev.mini_proj_manager.R;

public class Utility {
			
	//HASHMAP KEYS
	public static final String HM_CUSTOMER_NAME = "customer_name";
	public static final String HM_PROJECT_NAME = "project_name";
	public static final String HM_CUSTOMER_UID = "customer_uid";
	public static final String HM_PROJECT_UID = "project_uid";
	public static final String HM_CUSTOMER_REMOVED = "customer_removed";
	public static final String HM_PROJECT_REMOVED = "project_removed";
	
	public static final String HM_TOTAL_TIME = "total_hours";
	public static final String HM_TOTAL_AMOUNT = "total_amount";
	
//-------------------------GET DATA------------------------------------
	//CUSTOMERS
	public ArrayList<HashMap<String, Object>> getCustomers(Context context){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		String[] projection = {DBHelper.COL_CUSTOMER_CODE,
								DBHelper.COL_NAME,
								DBHelper.COL_REMOVED,
								DBHelper.COL_BALANCE,
								DBHelper.COL_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_CUSTOMERS,
			    projection,
			    null,
			    null,
			    null,
			    null,
			    DBHelper.COL_REMOVED + DBHelper.SORT_ASC +
			    ", CAST (" + DBHelper.COL_CUSTOMER_CODE + " AS INTEGER)" + DBHelper.SORT_DESC
			    
			    );		
			
		HashMap<String, Object>hm;
			
		if(c.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				
				hm.put(DBHelper.COL_CUSTOMER_CODE, c.getInt(c.getColumnIndex(dbh.COL_CUSTOMER_CODE)));
				hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(dbh.COL_NAME)));
				hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(dbh.COL_UID)));
				
				hm.put(DBHelper.COL_BALANCE, c.getInt(c.getColumnIndex(dbh.COL_BALANCE)));				
				hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(dbh.COL_REMOVED)));
				
				data.add(hm);
				
			}while(c.moveToNext());
		}
		
		db.close();
				
		return data;
	}
	
	//PROJECTS
	public ArrayList<HashMap<String, Object>> getProjects(Context context, String CUSTOMER_UID){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		String[] projection = {DBHelper.COL_UID,
								DBHelper.COL_PROJECT_CODE,
								DBHelper.COL_REMOVED,
								DBHelper.COL_BALANCE,
								DBHelper.COL_NAME};
		
		String selection = DBHelper.COL_OWNERS_UID + " LIKE ?";
		String[] selectionArgs = {CUSTOMER_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PROJECTS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    DBHelper.COL_REMOVED + DBHelper.SORT_ASC +
			    ", CAST (" + DBHelper.COL_PROJECT_CODE + " AS INTEGER)" + DBHelper.SORT_DESC
			    
			    );		
			
		HashMap<String, Object>hm;
			
		if(c.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				
				hm.put(DBHelper.COL_PROJECT_CODE, c.getInt(c.getColumnIndex(dbh.COL_PROJECT_CODE)));
				hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(dbh.COL_NAME)));
				hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(dbh.COL_UID)));
				
				hm.put(DBHelper.COL_BALANCE, c.getInt(c.getColumnIndex(dbh.COL_BALANCE)));				
				hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(dbh.COL_REMOVED)));
				
				data.add(hm);
				
			}while(c.moveToNext());
		}
		
		db.close();
				
		return data;
	}
	
	//RECENT PROJECTS
	public ArrayList<HashMap<String, Object>> getRecentProjects(Context context){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		String[] projection = {DBHelper.COL_UID,
								DBHelper.COL_OWNERS_UID,
								DBHelper.COL_PROJECT_CODE,
								DBHelper.COL_BALANCE,
								DBHelper.COL_NAME};
		
		String selection = DBHelper.COL_REMOVED + " = ?";
		String[] selectionArgs = {"0"};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PROJECTS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    DBHelper.COL_LAST_ACTION + DBHelper.SORT_DESC			    
			    );		
			
		HashMap<String, Object>hm;
			
		if(c.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				
				hm.put(DBHelper.COL_PROJECT_CODE, c.getInt(c.getColumnIndex(dbh.COL_PROJECT_CODE)));
				hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(dbh.COL_NAME)));
				hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(dbh.COL_UID)));
				hm.put(DBHelper.COL_OWNERS_UID, c.getString(c.getColumnIndex(dbh.COL_OWNERS_UID)));
				
				hm.put(DBHelper.COL_BALANCE, c.getInt(c.getColumnIndex(dbh.COL_BALANCE)));	
				
				data.add(hm);
				
			}while(c.moveToNext());
		}
		
		db.close();
				
		return data;
	}
	 
	
	//SESSIONS
	public ArrayList<HashMap<String, Object>> getSessions(Context context, String PROJECT_UID){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		String[] projection = {DBHelper.COL_UID,
								DBHelper.COL_ID,
								DBHelper.COL_REMOVED,
								DBHelper.COL_AMOUNT,
								DBHelper.COL_TIME_START,
								DBHelper.COL_TIME_END,
								DBHelper.COL_HOURLY_RATE,
								DBHelper.COL_NAME};
		
		String selection = DBHelper.COL_OWNERS_UID + " LIKE ?";
		String[] selectionArgs = {PROJECT_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_SESSIONS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    DBHelper.COL_REMOVED + DBHelper.SORT_ASC + 
			    ", " + DBHelper.COL_ID + DBHelper.SORT_DESC
			    
			    );		
			
		HashMap<String, Object>hm;
			
		if(c.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				
				hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(dbh.COL_NAME)));
				hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(dbh.COL_UID)));

				hm.put(DBHelper.COL_ID, c.getInt((c.getColumnIndex(dbh.COL_ID))));		
				hm.put(DBHelper.COL_AMOUNT, c.getInt(c.getColumnIndex(dbh.COL_AMOUNT)));
				hm.put(DBHelper.COL_HOURLY_RATE, c.getInt(c.getColumnIndex(dbh.COL_HOURLY_RATE)));
				hm.put(DBHelper.COL_TIME_START, c.getLong(c.getColumnIndex(dbh.COL_TIME_START)));
				hm.put(DBHelper.COL_TIME_END, c.getLong(c.getColumnIndex(dbh.COL_TIME_END)));
				hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(dbh.COL_REMOVED)));
				
				
				data.add(hm);
				
			}while(c.moveToNext());
		}
		
		db.close();
				
		return data;
	}
	
	//RUNNING SESSIONS
	public ArrayList<HashMap<String, Object>> getRunningSessions(Context context){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		String[] projection = {DBHelper.COL_UID,
								DBHelper.COL_ID,
								DBHelper.COL_OWNERS_UID,
								DBHelper.COL_TIME_START,
								DBHelper.COL_HOURLY_RATE,
								DBHelper.COL_NAME};
		
		String selection = DBHelper.COL_TIME_END + " = ? AND " + DBHelper.COL_REMOVED + " = ?";
		String[] selectionArgs = {"0", "0"};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_SESSIONS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    "CAST (" + DBHelper.COL_TIME_START + " AS INTEGER)" + DBHelper.SORT_ASC
			    );		
			
		HashMap<String, Object>hm;
			
		if(c.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				
				hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(dbh.COL_NAME)));
				hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(dbh.COL_UID)));

				hm.put(DBHelper.COL_ID, c.getInt((c.getColumnIndex(dbh.COL_ID))));		
				hm.put(DBHelper.COL_HOURLY_RATE, c.getInt(c.getColumnIndex(dbh.COL_HOURLY_RATE)));
				hm.put(DBHelper.COL_TIME_START, c.getLong(c.getColumnIndex(dbh.COL_TIME_START)));

				String PROJECT_UID = c.getString(c.getColumnIndex(DBHelper.COL_OWNERS_UID));				
				HashMap<String, Object> projectData = getProject(context, PROJECT_UID);
				
				String CUSTOMER_UID = (String)projectData.get(DBHelper.COL_OWNERS_UID);
				HashMap<String, Object> customerData = getCustomer(context, CUSTOMER_UID);
								
				hm.put(DBHelper.COL_CUSTOMER_CODE, (int)customerData.get(DBHelper.COL_CUSTOMER_CODE));
				hm.put(HM_CUSTOMER_NAME, (String)customerData.get(DBHelper.COL_NAME));
				hm.put(HM_CUSTOMER_UID, CUSTOMER_UID);
				hm.put(HM_CUSTOMER_REMOVED, (int)customerData.get(DBHelper.COL_REMOVED));
				
				hm.put(DBHelper.COL_PROJECT_CODE, (int)projectData.get(DBHelper.COL_PROJECT_CODE));
				hm.put(HM_PROJECT_NAME, (String)projectData.get(DBHelper.COL_NAME));
				hm.put(HM_PROJECT_UID, PROJECT_UID);
				hm.put(HM_PROJECT_REMOVED, (int)projectData.get(DBHelper.COL_REMOVED));
				
				data.add(hm);
				
			}while(c.moveToNext());
		}
		
		db.close();
				
		return data;
	}
	
	
	//PAYMENTS
	public ArrayList<HashMap<String, Object>> getPayments(Context context, String PROJECT_UID){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		String[] projection = {DBHelper.COL_UID,
								DBHelper.COL_ID,
								DBHelper.COL_REMOVED,
								DBHelper.COL_TIME_ADDED,
								DBHelper.COL_AMOUNT};
		
		String selection = DBHelper.COL_OWNERS_UID + " LIKE ?";
		String[] selectionArgs = {PROJECT_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PAYMENTS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    DBHelper.COL_REMOVED + DBHelper.SORT_ASC +
			    ", " + DBHelper.COL_ID + DBHelper.SORT_DESC
			    
			    );		
			
		HashMap<String, Object>hm;
			
		if(c.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				
				hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(dbh.COL_UID)));
				
				hm.put(DBHelper.COL_AMOUNT, c.getInt(c.getColumnIndex(dbh.COL_AMOUNT)));
				hm.put(DBHelper.COL_ID, c.getInt((c.getColumnIndex(dbh.COL_ID))));								
				hm.put(DBHelper.COL_TIME_ADDED, c.getLong(c.getColumnIndex(dbh.COL_TIME_ADDED)));
				hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(dbh.COL_REMOVED)));
				
				data.add(hm);
				
			}while(c.moveToNext());
		}
		
		db.close();
				
		return data;
	}

	//FILES
	public ArrayList<HashMap<String, Object>> getFiles(Context context, String OWNER_UID){
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
		
		return data;
	}
	
	//1 CUSTOMER
	public HashMap<String, Object> getCustomer(Context context, String CUSTOMER_UID){
		
		HashMap<String, Object> hm = new HashMap<String,Object>();
		
		//TOTALS
		HashMap<String, Object>totals = getTotalsForCustomer(context, CUSTOMER_UID);
		
		hm.put(HM_TOTAL_AMOUNT, (int)totals.get(HM_TOTAL_AMOUNT));
		hm.put(HM_TOTAL_TIME, (long)totals.get(HM_TOTAL_TIME));
		///
				
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		String selection = DBHelper.COL_UID + " LIKE ?";
		String[] selectionArgs = {CUSTOMER_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_CUSTOMERS,
			    null,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
						
		if(c.moveToFirst()){
			hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(DBHelper.COL_UID)));
			hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(DBHelper.COL_NAME)));
			hm.put(DBHelper.COL_TEL_1, c.getString(c.getColumnIndex(DBHelper.COL_TEL_1)));
			hm.put(DBHelper.COL_TEL_2, c.getString(c.getColumnIndex(DBHelper.COL_TEL_2)));
			hm.put(DBHelper.COL_TEL_3, c.getString(c.getColumnIndex(DBHelper.COL_TEL_3)));
			hm.put(DBHelper.COL_EMAIL_1, c.getString(c.getColumnIndex(DBHelper.COL_EMAIL_1)));
			hm.put(DBHelper.COL_EMAIL_2, c.getString(c.getColumnIndex(DBHelper.COL_EMAIL_2)));
			hm.put(DBHelper.COL_FAX, c.getString(c.getColumnIndex(DBHelper.COL_FAX)));
			hm.put(DBHelper.COL_WEB, c.getString(c.getColumnIndex(DBHelper.COL_WEB)));
			hm.put(DBHelper.COL_ADDRESS, c.getString(c.getColumnIndex(DBHelper.COL_ADDRESS)));
			hm.put(DBHelper.COL_BANK_DETAILS, c.getString(c.getColumnIndex(DBHelper.COL_BANK_DETAILS)));
			hm.put(DBHelper.COL_SKYPE, c.getString(c.getColumnIndex(DBHelper.COL_SKYPE)));
			hm.put(DBHelper.COL_FACEBOOK, c.getString(c.getColumnIndex(DBHelper.COL_FACEBOOK)));
			hm.put(DBHelper.COL_LINKEDIN, c.getString(c.getColumnIndex(DBHelper.COL_LINKEDIN)));
			hm.put(DBHelper.COL_COMMENT, c.getString(c.getColumnIndex(DBHelper.COL_COMMENT)));
			
			hm.put(DBHelper.COL_CUSTOMER_CODE, c.getInt(c.getColumnIndex(DBHelper.COL_CUSTOMER_CODE)));
			hm.put(DBHelper.COL_BALANCE, c.getInt(c.getColumnIndex(DBHelper.COL_BALANCE)));
						
			String time_added_str = getFormattedTime(c.getLong(c.getColumnIndex(DBHelper.COL_TIME_ADDED)),
													Const.DATE_FORMAT_DATE);
			hm.put(DBHelper.COL_TIME_ADDED, time_added_str);

			hm.put(DBHelper.COL_DISCOUNT, c.getInt(c.getColumnIndex(DBHelper.COL_DISCOUNT)));
			hm.put(DBHelper.COL_HOURLY_RATE, c.getInt(c.getColumnIndex(DBHelper.COL_HOURLY_RATE)));
				
			hm.put(DBHelper.COL_ACTIVE, c.getInt(c.getColumnIndex(DBHelper.COL_ACTIVE)));
			hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(DBHelper.COL_REMOVED)));
				
		}
		
		db.close();
		
		return hm;
	}
	
	//GET CUSTOMER BY CODE
	public HashMap<String, Object> getCustomerByCode(Context context, int CUSTOMER_CODE){
		
		HashMap<String, Object> hm = new HashMap<String,Object>();
				
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		String selection = DBHelper.COL_CUSTOMER_CODE + " = ?";
		String[] selectionArgs = {String.valueOf(CUSTOMER_CODE)};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_CUSTOMERS,
			    null,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
						
		if(c.moveToFirst()){
			hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(DBHelper.COL_UID)));							
		}
		
		db.close();
		
		return hm;
	}
	
	//1 PROJECT
	public HashMap<String, Object> getProject(Context context, String PROJECT_UID){
		HashMap<String, Object> hm = new HashMap<String,Object>();
		
		//TOTALS
		HashMap<String, Object>totals = getTotalsForProject(context, PROJECT_UID);
		
		hm.put(HM_TOTAL_AMOUNT, (int)totals.get(HM_TOTAL_AMOUNT));
		hm.put(HM_TOTAL_TIME, (long)totals.get(HM_TOTAL_TIME));
		///
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		String selection = DBHelper.COL_UID + " LIKE ?";
		String[] selectionArgs = {PROJECT_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PROJECTS,
			    null,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
						
		if(c.moveToFirst()){
			hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(DBHelper.COL_UID)));
			hm.put(DBHelper.COL_OWNERS_UID, c.getString(c.getColumnIndex(DBHelper.COL_OWNERS_UID)));
			hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(DBHelper.COL_NAME)));
			hm.put(DBHelper.COL_DESCRIPTION, c.getString(c.getColumnIndex(DBHelper.COL_DESCRIPTION)));
			hm.put(DBHelper.COL_COMMENT, c.getString(c.getColumnIndex(DBHelper.COL_COMMENT)));
			
			hm.put(DBHelper.COL_PROJECT_CODE, c.getInt(c.getColumnIndex(DBHelper.COL_PROJECT_CODE)));
			hm.put(DBHelper.COL_BALANCE, c.getInt(c.getColumnIndex(DBHelper.COL_BALANCE)));
						
			String time_added_str = getFormattedTime(c.getLong(c.getColumnIndex(DBHelper.COL_TIME_ADDED)),
													Const.DATE_FORMAT_DATE);
			hm.put(DBHelper.COL_TIME_ADDED, time_added_str);
			
			String deadline_str = getFormattedTime(c.getLong(c.getColumnIndex(DBHelper.COL_DEADLINE)),
													Const.DATE_FORMAT_DATE);
			hm.put(DBHelper.COL_DEADLINE, deadline_str);
			
			String last_action = getFormattedTime(c.getLong(c.getColumnIndex(DBHelper.COL_LAST_ACTION)),
													Const.DATE_FORMAT_DATETIME);
			hm.put(DBHelper.COL_LAST_ACTION, last_action);
			
			hm.put(DBHelper.COL_HOURLY_RATE, c.getInt(c.getColumnIndex(DBHelper.COL_HOURLY_RATE)));
				
			hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(DBHelper.COL_REMOVED)));
					
		}
		
		db.close();
		
		return hm;
	}
		
	//GET PROJECT BY CODE
	public HashMap<String, Object> getProjectByCode(Context context, int PROJECT_CODE){
		HashMap<String, Object> hm = new HashMap<String,Object>();
				
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		String selection = DBHelper.COL_PROJECT_CODE + " = ?";
		String[] selectionArgs = {String.valueOf(PROJECT_CODE)};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PROJECTS,
			    null,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
						
		if(c.moveToFirst()){
			hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(DBHelper.COL_UID)));
			hm.put(DBHelper.COL_OWNERS_UID, c.getString(c.getColumnIndex(DBHelper.COL_OWNERS_UID)));			
		}
		
		db.close();
		
		return hm;
	}
	
	//1 SESSIONS
	public HashMap<String, Object> getSession(Context context, String SESSION_UID){
		HashMap<String, Object> hm = new HashMap<String,Object>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		String selection = DBHelper.COL_UID + " LIKE ?";
		String[] selectionArgs = {SESSION_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_SESSIONS,
			    null,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
						
		if(c.moveToFirst()){
			hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(DBHelper.COL_UID)));
			hm.put(DBHelper.COL_NAME, c.getString(c.getColumnIndex(DBHelper.COL_NAME)));
			hm.put(DBHelper.COL_DESCRIPTION, c.getString(c.getColumnIndex(DBHelper.COL_DESCRIPTION)));
			hm.put(DBHelper.COL_COMMENT, c.getString(c.getColumnIndex(DBHelper.COL_COMMENT)));

			hm.put(DBHelper.COL_TIME_START, c.getLong(c.getColumnIndex(DBHelper.COL_TIME_START)));
			
			hm.put(DBHelper.COL_TIME_END, c.getLong(c.getColumnIndex(DBHelper.COL_TIME_END)));
						
			hm.put(DBHelper.COL_ID, c.getInt(c.getColumnIndex(DBHelper.COL_ID)));
			hm.put(DBHelper.COL_HOURLY_RATE, c.getInt(c.getColumnIndex(DBHelper.COL_HOURLY_RATE)));
			hm.put(DBHelper.COL_AMOUNT, c.getInt(c.getColumnIndex(DBHelper.COL_AMOUNT)));
				
			hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(DBHelper.COL_REMOVED)));
					
		}
		
		db.close();
		
		return hm;	
	}
	
	//1 PAYMENT
	public HashMap<String, Object> getPayment(Context context, String PAYMENT_UID){
		HashMap<String, Object> hm = new HashMap<String,Object>();
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		String selection = DBHelper.COL_UID + " LIKE ?";
		String[] selectionArgs = {PAYMENT_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PAYMENTS,
			    null,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
						
		if(c.moveToFirst()){
			hm.put(DBHelper.COL_UID, c.getString(c.getColumnIndex(DBHelper.COL_UID)));
			hm.put(DBHelper.COL_DESCRIPTION, c.getString(c.getColumnIndex(DBHelper.COL_DESCRIPTION)));
			hm.put(DBHelper.COL_COMMENT, c.getString(c.getColumnIndex(DBHelper.COL_COMMENT)));

			hm.put(DBHelper.COL_TIME_ADDED, c.getLong(c.getColumnIndex(DBHelper.COL_TIME_ADDED)));
			
			hm.put(DBHelper.COL_ID, c.getInt(c.getColumnIndex(DBHelper.COL_ID)));
			hm.put(DBHelper.COL_AMOUNT, c.getInt(c.getColumnIndex(DBHelper.COL_AMOUNT)));
			
			hm.put(DBHelper.COL_REMOVED, c.getInt(c.getColumnIndex(dbh.COL_REMOVED)));
						
		}
		
		db.close();
		
		return hm;		
	} 
	
	//1 FILE
	public HashMap<String, Object> getFile(Context context, String URI){
		HashMap<String, Object> data = new HashMap<String,Object>();
		
		return data;
	} 
	
//-------------------------MANAGE DATA------------------------------
	//ADD
	public void add_data(Context context, ContentValues cv, String table){
		DBHelper dbh = new DBHelper(context);
		
		SQLiteDatabase db = dbh.getWritableDatabase();

		db.insert(
	         table,
	         DBHelper.COL_NULL,
	         cv);
		
		db.close();
	}
	
	//UPDATE
	public void updateData(Context context,
							String table,
							ContentValues values,
							String selection,
							String[] selectionArgs){
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();

		db.update(table, values, selection, selectionArgs);
		
		db.close();
		
	}	
	
	//DELETE
	public void deleteData(Context context,
						String table,
						String selection,
						String[] selectionArgs){
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();

		db.delete(table, selection, selectionArgs);
		
		db.close();
		
	}

	//-----------------------UTILS----------------------
	
	//GET FORMATTED TIME
	public String getFormattedTime(long time_millis, String format){
		
		if(time_millis == 0){
			return "-";
		}
		
		Date date = new Date(time_millis);
		
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	
    	return sdf.format(date);
	}
		
	//GET UID
	public String getUid(){
		return UUID.randomUUID().toString(); 
	}
	
	//GET CUSTOMER CODE
	public int getCustomerCode(Context context){
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		int code;
		
		String[] projection = {DBHelper.COL_CUSTOMER_CODE};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_CUSTOMERS,
			    projection,
			    null,
			    null,
			    null,
			    null,
			    "CAST (" + DBHelper.COL_CUSTOMER_CODE + " AS INTEGER)" + DBHelper.SORT_DESC
			    );		
			
			
		if(c.moveToFirst()){
			code = c.getInt(c.getColumnIndex(DBHelper.COL_CUSTOMER_CODE)) + 1;
		}else{
			code = 1;
		}
		
		db.close();
		
		return code;
	}
	
	//GET PROJECT CODE
	public int getProjectCode(Context context, String CUSTOMER_UID, int CUSTOMER_CODE){
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		int code;
		
		String[] projection = {DBHelper.COL_PROJECT_CODE};
		String selection = DBHelper.COL_OWNERS_UID + " LIKE ?";
		String[] selectionArgs = {CUSTOMER_UID};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PROJECTS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    "CAST (" + DBHelper.COL_PROJECT_CODE + " AS INTEGER)" + DBHelper.SORT_DESC
			    );		
			
			
		if(c.moveToFirst()){
			code = c.getInt(c.getColumnIndex(DBHelper.COL_PROJECT_CODE)) + 1;
		}else{
			
			code = CUSTOMER_CODE * (int)Math.pow(10, Const.CODE_LENGHT_CUSTOMER) + 1;
			
		}
		
		db.close();
		
		return code;
	}
	
	//GET INT FROM EDITTEXT
	public int getIntFromEditText(EditText et){
		int value = 0;
		String text = et.getText().toString();
		if(!text.isEmpty()){
			try{
				value = Integer.parseInt(text);
			}catch(Exception e){}
		}
		
		return value;
	}
	
	//GET DEFAULT 
	public int getDefaultRate(Context context){
		SharedPreferences sPref = ((MainActivity)context).getPreferences(Activity.MODE_PRIVATE);
		
		return sPref.getInt(Const.PREF_DEFAULT_RATE, 0);
	}
	
	//GET PROJECT WORKED HOURS
	public long getProjectWorkedHours(String PROJECT_UID){
		return 0;
	}

	//GET FORMATTED DURATION
	public String getFormattedDuration(long millis){		
		millis /= 60000;
				
		long m = millis % 60;
		
		long h = millis / 60;
		
		return String.format("%02d:%02d", h, m);
	}
	
	//GET MONEY STRING FROM INT AMOUNT
	public String getMoneyString(int amount){
		return String.format("%.2f", (float)amount / 100) ;
	} 
	
	//GET CODE FROM INT
	public String getStringCodeFromInt(int value,  int lenght){
		String value_str = String.valueOf(value);
		
		String code = "";
		
		for(int i = 0; i < lenght - value_str.length(); i++){
			code += "0";
		}
		
		return code + value_str;
		
	}
	
	/*//GET INT CODE FROM STRING
	public int getIntCodeFromString(){
		
	}*/
	
	//GET INT AMOUNT FROM MONEY STRING
	public int getMoneyInt(String string){
		if(string == null || string.isEmpty())
			return 0;
		try{
			return (int)(Float.parseFloat(string) * 100);
		}catch(NumberFormatException e){
			return 0;
		}
	}
	
	//GET COSTS
	public long getSessionCosts(long millis, int rate){
		
		millis /= 60000; //minutes
		
		return millis * rate / 60;
		
	}

	//ON PROJECT DATA CHANGED
	public void onProjectDataChanged(Context context, String CUSTOMER_UID, String PROJECT_UID){
		
		long balance = 0;
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		//SUBTRACTING ENDED AND NOT REMOVED SESSIONS COSTS////////////////
		String[] projection = {DBHelper.COL_AMOUNT};
		
		String selection = DBHelper.COL_OWNERS_UID + " LIKE ? AND "
							+ DBHelper.COL_REMOVED + " = ?";
		String[] selectionArgs = {PROJECT_UID, "0"};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_SESSIONS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
		
		long start_time;
		long end_time;
		int rate;
			
		if(c.moveToFirst()){
			do{
				
				balance -= c.getInt(c.getColumnIndex(DBHelper.COL_AMOUNT));
				
			}while(c.moveToNext());
		}
		
		//ADDING AMOUNTS FROM NOT REMOVED PAYMENTS
		String[] projection_2 = {DBHelper.COL_AMOUNT};

		String selection_2 = DBHelper.COL_OWNERS_UID + " LIKE ? AND "
					+ DBHelper.COL_REMOVED + " = ?";
		String[] selectionArgs_2 = {PROJECT_UID, "0"};
		
		c = db.query(
				DBHelper.TABLE_NAME_PAYMENTS,
				projection_2,
				selection_2,
				selectionArgs_2,
				null,
				null,
				null
				);		

		if(c.moveToFirst()){
			do{				
				balance += c.getInt(c.getColumnIndex(DBHelper.COL_AMOUNT));				
			}while(c.moveToNext());
		}
		
		
		//UPDATING PROJECT
		ContentValues cv = new ContentValues();
		cv.put(DBHelper.COL_BALANCE, balance);
		cv.put(DBHelper.COL_LAST_ACTION, System.currentTimeMillis());
		
		updateData(context,
				DBHelper.TABLE_NAME_PROJECTS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {PROJECT_UID});
		
		//GETTING BALANCES OF PROJECTS OF THE CUSTOMER
		balance = 0;
		
		String[] projection_3 = {DBHelper.COL_BALANCE};

		String selection_3 = DBHelper.COL_OWNERS_UID + " LIKE ? AND "
					+ DBHelper.COL_REMOVED + " = ?";
		String[] selectionArgs_3 = {CUSTOMER_UID, "0"};
		
		c = db.query(
				DBHelper.TABLE_NAME_PROJECTS,
				projection_3,
				selection_3,
				selectionArgs_3,
				null,
				null,
				null
				);		

		if(c.moveToFirst()){
			do{				
				balance += c.getInt(c.getColumnIndex(DBHelper.COL_BALANCE));				
			}while(c.moveToNext());
		}
		
		//UPDATING BALANCE OF CUSTOMER
		cv = new ContentValues();
		cv.put(DBHelper.COL_BALANCE, balance);
		
		updateData(context,
				DBHelper.TABLE_NAME_CUSTOMERS,
				cv,
				DBHelper.COL_UID + " LIKE ?",
				new String[] {CUSTOMER_UID});
		
		db.close();
	}

	//GET TOTALS FOR PROJECT
	public HashMap<String, Object> getTotalsForProject(Context context, String PROJECT_UID){
		
		HashMap<String, Object>data = new HashMap<String, Object>();
		
		long total_time = 0;
		int total_amount = 0;
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		//TOTAL HOURS
		String[] projection = {DBHelper.COL_TIME_START,
								DBHelper.COL_TIME_END};
		
		String selection = DBHelper.COL_OWNERS_UID + " LIKE ? AND "
							+ DBHelper.COL_REMOVED + " = ? AND "
							+ DBHelper.COL_TIME_END + " > ?";
		String[] selectionArgs = {PROJECT_UID, "0", "0"};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_SESSIONS,
			    projection,
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
		
		if(c.moveToFirst()){
			do{
				
				total_time += (c.getLong(c.getColumnIndex(DBHelper.COL_TIME_END)) 
								- c.getLong(c.getColumnIndex(DBHelper.COL_TIME_START)));
				
			}while(c.moveToNext());
		}
		
		
		//TOTAL MONEY		
		String selection_2 = DBHelper.COL_OWNERS_UID + " LIKE ? AND "
							+ DBHelper.COL_REMOVED + " = ?";
		String[] selectionArgs_2 = {PROJECT_UID, "0"};
		
		c = db.query(
				DBHelper.TABLE_NAME_PAYMENTS,
				new String[] {DBHelper.COL_AMOUNT},
				selection_2,
				selectionArgs_2,
			    null,
			    null,
			    null
			    );		
		
		if(c.moveToFirst()){
			do{
				
				total_amount += c.getInt(c.getColumnIndex(DBHelper.COL_AMOUNT));
				
			}while(c.moveToNext());
		}
		
		db.close();
		
		data.put(HM_TOTAL_TIME, total_time);
		data.put(HM_TOTAL_AMOUNT, total_amount);
		
		return data;
	}

	//GET TOTALS FOR CUSTOMER
	public HashMap<String, Object> getTotalsForCustomer(Context context, String CUSTOMER_UID){
		HashMap<String, Object>data = new HashMap<String, Object>();
		
		ArrayList<String> project_uids = new ArrayList<String>();
		
		long total_time = 0;
		int total_amount = 0;
		
		DBHelper dbh = new DBHelper(context);		
		SQLiteDatabase db = dbh.getWritableDatabase();
				
		//ALL PROJECTS
		
		String selection = DBHelper.COL_OWNERS_UID + " LIKE ? AND "
							+ DBHelper.COL_REMOVED + " = ?";
		String[] selectionArgs = {CUSTOMER_UID, "0"};
		
		Cursor c = db.query(
				DBHelper.TABLE_NAME_PROJECTS,
			    new String[] {DBHelper.COL_UID},
			    selection,
			    selectionArgs,
			    null,
			    null,
			    null
			    );		
		
		if(c.moveToFirst()){
			do{
				project_uids.add(c.getString(c.getColumnIndex(DBHelper.COL_UID)));
			}while(c.moveToNext());
		}
		
		db.close();
		
		for(String PROJECT_UID: project_uids){
			HashMap<String, Object>project_totals = getTotalsForProject(context, PROJECT_UID);
			
			total_time += (long)project_totals.get(HM_TOTAL_TIME);
			total_amount += (int)project_totals.get(HM_TOTAL_AMOUNT);
		}
		
		data.put(HM_TOTAL_TIME, total_time);
		data.put(HM_TOTAL_AMOUNT, total_amount);
		
		return data;
	}

	
	
	
	//---------------------------------DESIGN--------------------------------
	public int getMoneyColor(Context context, int amount){
		if(amount < 0){
			return context.getResources().getColor(R.color.negative_number);
		}else if(amount > 0){
			return context.getResources().getColor(R.color.positive_number);
		}
		
		return context.getResources().getColor(R.color.zero_number);		
	}
	
}

