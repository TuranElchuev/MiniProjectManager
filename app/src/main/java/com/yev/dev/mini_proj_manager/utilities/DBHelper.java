package com.yev.dev.mini_proj_manager.utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "com.abbox.abbox.database";

	public static final String TEXT_TYPE = " TEXT";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String COMMA_SEP = ", ";
	
	public static final String SORT_DESC = " DESC ";
	public static final String SORT_ASC = " ASC ";
	
	public static final String TABLE_NAME_CUSTOMERS = "table_customers";
	public static final String TABLE_NAME_PROJECTS = "table_projects";
	public static final String TABLE_NAME_SESSIONS = "table_sessions";
	public static final String TABLE_NAME_PAYMENTS = "table_payments";
	public static final String TABLE_NAME_FILES = "table_files";
	
	public static final String COL_NULL = "col_null";
	
    public static final String COL_ID = "id";
        
    //TEXT
    public static final String COL_UID = "col_uid";
    public static final String COL_OWNERS_UID = "col_owners_uid";
    public static final String COL_NAME = "col_name";
    public static final String COL_TEL_1 = "col_tel_1";
    public static final String COL_TEL_2 = "col_tel_2";
    public static final String COL_TEL_3 = "col_tel_3";
    public static final String COL_EMAIL_1 = "col_email_1";
    public static final String COL_EMAIL_2 = "col_email_2";
    public static final String COL_FAX = "col_fax";
    public static final String COL_WEB = "col_web";
    public static final String COL_ADDRESS = "col_address";
    public static final String COL_SKYPE = "col_skype";
    public static final String COL_FACEBOOK = "col_facebook";
    public static final String COL_LINKEDIN = "col_linkedin";
    public static final String COL_BANK_DETAILS = "col_bank_details";
    public static final String COL_COMMENT = "col_comment";
    public static final String COL_DESCRIPTION = "col_description";
    public static final String COL_URI = "col_uri";
    public static final String COL_AMOUNT = "col_amount";
    
    public static final String COL_CUS_EXTRA_TEXT_1 = "col_extra_text_1";
    public static final String COL_CUS_EXTRA_TEXT_2 = "col_extra_text_2";
    public static final String COL_CUS_EXTRA_TEXT_3 = "col_extra_text_3";
    public static final String COL_CUS_EXTRA_TEXT_4 = "col_extra_text_4";
    public static final String COL_CUS_EXTRA_TEXT_5 = "col_extra_text_5";
    
    public static final String COL_PROJ_EXTRA_TEXT_1 = "col_extra_text_1";
    public static final String COL_PROJ_EXTRA_TEXT_2 = "col_extra_text_2";
    public static final String COL_PROJ_EXTRA_TEXT_3 = "col_extra_text_3";
    public static final String COL_PROJ_EXTRA_TEXT_4 = "col_extra_text_4";
    public static final String COL_PROJ_EXTRA_TEXT_5 = "col_extra_text_5";

    public static final String COL_SES_EXTRA_TEXT_1 = "col_extra_text_1";
    public static final String COL_SES_EXTRA_TEXT_2 = "col_extra_text_2";
    
    public static final String COL_FIL_EXTRA_TEXT_1 = "col_extra_text_1";
    public static final String COL_FIL_EXTRA_TEXT_2 = "col_extra_text_2";
    
    
    //INTEGER
    public static final String COL_CUSTOMER_CODE = "col_customer_code";
    public static final String COL_PROJECT_CODE = "col_project_code";
    public static final String COL_TIME_ADDED = "col_time_added";
    public static final String COL_BALANCE = "col_balance";
    public static final String COL_DISCOUNT = "col_discount";
    public static final String COL_HOURLY_RATE = "col_hourly_rate";
    public static final String COL_PRIORITY = "col_priority";
    public static final String COL_DEADLINE = "col_deadline";
    public static final String COL_TIME_START = "col_time_start";
    public static final String COL_TIME_END = "col_time_end";
    public static final String COL_LAST_ACTION = "col_last_action";
    
    public static final String COL_CUS_EXTRA_INT_1 = "col_extra_int_1"; 
    public static final String COL_CUS_EXTRA_INT_2 = "col_extra_int_2";
    public static final String COL_CUS_EXTRA_INT_3 = "col_extra_int_3";
    public static final String COL_CUS_EXTRA_INT_4 = "col_extra_int_4";
    public static final String COL_CUS_EXTRA_INT_5 = "col_extra_int_5";
    
    public static final String COL_PROJ_EXTRA_INT_1 = "col_extra_int_1";
    public static final String COL_PROJ_EXTRA_INT_2 = "col_extra_int_2";
    public static final String COL_PROJ_EXTRA_INT_3 = "col_extra_int_3";
    public static final String COL_PROJ_EXTRA_INT_4 = "col_extra_int_4";
    public static final String COL_PROJ_EXTRA_INT_5 = "col_extra_int_5";

    public static final String COL_SES_EXTRA_INT_1 = "col_extra_int_1";
    public static final String COL_SES_EXTRA_INT_2 = "col_extra_int_2";

    public static final String COL_FIL_EXTRA_INT_1 = "col_extra_int_1";
    public static final String COL_FIL_EXTRA_INT_2 = "col_extra_int_2";
        
    //BOOLEAN (INTEGER)
    public static final String COL_ACTIVE = "col_active";
    public static final String COL_REMOVED = "col_removed";
    public static final String COL_IS_DISCOUNT = "col_is_discount"; 
    public static final String COL_STORE_IN_DB = "col_store_in_db";    

    public static final String COL_CUS_EXTRA_BOOL_1 = "col_extra_bool_1";
    public static final String COL_CUS_EXTRA_BOOL_2 = "col_extra_bool_2";
    public static final String COL_CUS_EXTRA_BOOL_3 = "col_extra_bool_3";
    public static final String COL_CUS_EXTRA_BOOL_4 = "col_extra_bool_4";
    public static final String COL_CUS_EXTRA_BOOL_5 = "col_extra_bool_5";    

    public static final String COL_PROJ_EXTRA_BOOL_1 = "col_extra_bool_1";
    public static final String COL_PROJ_EXTRA_BOOL_2 = "col_extra_bool_2";
    public static final String COL_PROJ_EXTRA_BOOL_3 = "col_extra_bool_3";
    public static final String COL_PROJ_EXTRA_BOOL_4 = "col_extra_bool_4";
    public static final String COL_PROJ_EXTRA_BOOL_5 = "col_extra_bool_5";

    public static final String COL_SES_EXTRA_BOOL_1 = "col_extra_bool_1";
    public static final String COL_SES_EXTRA_BOOL_2 = "col_extra_bool_2";

    public static final String COL_FIL_EXTRA_BOOL_1 = "col_extra_bool_1";
    public static final String COL_FIL_EXTRA_BOOL_2 = "col_extra_bool_2";
    
    //--------ENTRIES------------------------
    //CUSTOMERS
	private final String ENTRIES_TABLE_CUSTOMERS = 
			"CREATE TABLE " + TABLE_NAME_CUSTOMERS + " (" +
		    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
		    COL_UID + TEXT_TYPE + COMMA_SEP +
		    COL_NAME + TEXT_TYPE + COMMA_SEP +
		    COL_TEL_1 + TEXT_TYPE + COMMA_SEP +
		    COL_TEL_2 + TEXT_TYPE + COMMA_SEP +
		    COL_TEL_3 + TEXT_TYPE + COMMA_SEP +
		    COL_EMAIL_1 + TEXT_TYPE + COMMA_SEP +
		    COL_EMAIL_2 + TEXT_TYPE + COMMA_SEP +
		    COL_FAX + TEXT_TYPE + COMMA_SEP +
		    COL_WEB + TEXT_TYPE + COMMA_SEP +
		    COL_ADDRESS + TEXT_TYPE + COMMA_SEP +
		    COL_BANK_DETAILS + TEXT_TYPE + COMMA_SEP +
		    COL_SKYPE + TEXT_TYPE + COMMA_SEP +
		    COL_FACEBOOK + TEXT_TYPE + COMMA_SEP +
		    COL_LINKEDIN + TEXT_TYPE + COMMA_SEP +
		    COL_COMMENT + TEXT_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_TEXT_1 + TEXT_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_TEXT_2 + TEXT_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_TEXT_3 + TEXT_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_TEXT_4 + TEXT_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_TEXT_5 + TEXT_TYPE + COMMA_SEP +
		    
		    COL_CUSTOMER_CODE + INTEGER_TYPE + COMMA_SEP +
		    COL_TIME_ADDED + INTEGER_TYPE + COMMA_SEP +
		    COL_DISCOUNT + INTEGER_TYPE + COMMA_SEP +
		    COL_BALANCE + INTEGER_TYPE + COMMA_SEP +
		    COL_HOURLY_RATE + INTEGER_TYPE + COMMA_SEP +
		    COL_PRIORITY + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_INT_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_INT_2 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_INT_3 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_INT_4 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_INT_5 + INTEGER_TYPE + COMMA_SEP +
		    
		    COL_ACTIVE + INTEGER_TYPE + COMMA_SEP +
		    COL_REMOVED + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_BOOL_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_BOOL_2 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_BOOL_3 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_BOOL_4 + INTEGER_TYPE + COMMA_SEP +
		    COL_CUS_EXTRA_BOOL_5 + INTEGER_TYPE + 
		    " )";
			
	//PROJECTS
	private final String ENTRIES_TABLE_PROJECTS = 
		    "CREATE TABLE " + TABLE_NAME_PROJECTS + " (" +
		    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		    COL_UID + TEXT_TYPE + COMMA_SEP +
		    COL_OWNERS_UID + TEXT_TYPE + COMMA_SEP +
		    COL_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
		    COL_COMMENT + TEXT_TYPE + COMMA_SEP +
		    COL_NAME + TEXT_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_TEXT_1 + TEXT_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_TEXT_2 + TEXT_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_TEXT_3 + TEXT_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_TEXT_4 + TEXT_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_TEXT_5 + TEXT_TYPE + COMMA_SEP +
		    
		    COL_CUSTOMER_CODE + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJECT_CODE + INTEGER_TYPE + COMMA_SEP +
		    COL_TIME_ADDED + INTEGER_TYPE + COMMA_SEP +
		    COL_LAST_ACTION + INTEGER_TYPE + COMMA_SEP +
		    COL_DEADLINE + INTEGER_TYPE + COMMA_SEP +
		    COL_PRIORITY + INTEGER_TYPE + COMMA_SEP +
		    COL_HOURLY_RATE + INTEGER_TYPE + COMMA_SEP +
		    COL_BALANCE + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_INT_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_INT_2 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_INT_3 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_INT_4 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_INT_5 + INTEGER_TYPE + COMMA_SEP +
		    
		    COL_REMOVED + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_BOOL_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_BOOL_2 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_BOOL_3 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_BOOL_4 + INTEGER_TYPE + COMMA_SEP +
		    COL_PROJ_EXTRA_BOOL_5 + INTEGER_TYPE +
		    " )";
		    
	//SESSIONS
	private final String ENTRIES_TABLE_SESSIONS =
		    "CREATE TABLE " + TABLE_NAME_SESSIONS + " (" +
		    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		    COL_UID + TEXT_TYPE + COMMA_SEP +
		    COL_OWNERS_UID + TEXT_TYPE + COMMA_SEP +
		    COL_NAME + TEXT_TYPE + COMMA_SEP +
		    COL_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
		    COL_COMMENT + TEXT_TYPE + COMMA_SEP +
		    COL_SES_EXTRA_TEXT_1 + TEXT_TYPE + COMMA_SEP +
		    COL_SES_EXTRA_TEXT_2 + TEXT_TYPE + COMMA_SEP +
		    
		    COL_PROJECT_CODE + INTEGER_TYPE + COMMA_SEP +
		    COL_TIME_START + INTEGER_TYPE + COMMA_SEP +
		    COL_TIME_END + INTEGER_TYPE + COMMA_SEP +
		    COL_HOURLY_RATE + INTEGER_TYPE + COMMA_SEP +
		    COL_AMOUNT + INTEGER_TYPE + COMMA_SEP +
		    COL_SES_EXTRA_INT_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_SES_EXTRA_INT_2 + INTEGER_TYPE + COMMA_SEP +
		    
		    COL_REMOVED + INTEGER_TYPE + COMMA_SEP +
		    COL_SES_EXTRA_BOOL_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_SES_EXTRA_BOOL_2 + INTEGER_TYPE +
		    " )";
	
	//PAYMENTS
	private final String ENTRIES_TABLE_PAYMENTS =
		    "CREATE TABLE " + TABLE_NAME_PAYMENTS + " (" +
		    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		    COL_UID + TEXT_TYPE + COMMA_SEP +
		    COL_OWNERS_UID + TEXT_TYPE + COMMA_SEP +
		    COL_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
		    COL_COMMENT + TEXT_TYPE + COMMA_SEP +
		    
		    COL_PROJECT_CODE + INTEGER_TYPE + COMMA_SEP +
		    COL_TIME_ADDED + INTEGER_TYPE + COMMA_SEP +
		    COL_AMOUNT + INTEGER_TYPE + COMMA_SEP +
		    
		    COL_IS_DISCOUNT + INTEGER_TYPE + COMMA_SEP +
		    COL_REMOVED + INTEGER_TYPE +
		    " )";
	
	//FILES
	private final String ENTRIES_TABLE_FILES =
		    "CREATE TABLE " + TABLE_NAME_FILES + " (" +
		    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		    COL_OWNERS_UID + TEXT_TYPE + COMMA_SEP +
		    COL_URI + TEXT_TYPE + COMMA_SEP +
		    COL_NAME + TEXT_TYPE + COMMA_SEP +
		    COL_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
		    COL_COMMENT + TEXT_TYPE + COMMA_SEP +
		    COL_FIL_EXTRA_TEXT_1 + TEXT_TYPE + COMMA_SEP +
		    COL_FIL_EXTRA_TEXT_2 + TEXT_TYPE + COMMA_SEP +
		    
		    COL_TIME_ADDED + INTEGER_TYPE + COMMA_SEP +
		    COL_FIL_EXTRA_INT_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_FIL_EXTRA_INT_2 + INTEGER_TYPE + COMMA_SEP +
		    
		    COL_REMOVED + INTEGER_TYPE + COMMA_SEP +
		    COL_STORE_IN_DB + INTEGER_TYPE + COMMA_SEP +
		    COL_FIL_EXTRA_BOOL_1 + INTEGER_TYPE + COMMA_SEP +
		    COL_FIL_EXTRA_BOOL_2 + INTEGER_TYPE +
		    " )";
    
    

	//DELETE ENTRIES-------------------------
	private final String SQL_DELETE_CUSTOMERS =
		    "DROP TABLE IF EXISTS " + TABLE_NAME_CUSTOMERS;
	
	private final String SQL_DELETE_PROJECTS =
		    "DROP TABLE IF EXISTS " + TABLE_NAME_PROJECTS;
	
	private final String SQL_DELETE_SESSIONS =
		    "DROP TABLE IF EXISTS " + TABLE_NAME_SESSIONS;
	
	private final String SQL_DELETE_PAYMENTS =
		    "DROP TABLE IF EXISTS " + TABLE_NAME_PAYMENTS;
	
	private final String SQL_DELETE_FILES =
		    "DROP TABLE IF EXISTS " + TABLE_NAME_FILES;
	
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ENTRIES_TABLE_CUSTOMERS);
        db.execSQL(ENTRIES_TABLE_PROJECTS);
        db.execSQL(ENTRIES_TABLE_SESSIONS);
        db.execSQL(ENTRIES_TABLE_PAYMENTS);
        db.execSQL(ENTRIES_TABLE_FILES);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_CUSTOMERS);
        db.execSQL(SQL_DELETE_PROJECTS);
        db.execSQL(SQL_DELETE_SESSIONS);
        db.execSQL(SQL_DELETE_PAYMENTS);
        db.execSQL(SQL_DELETE_FILES);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
