package com.yev.dev.mini_proj_manager;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.yev.dev.mini_proj_manager.fragments.Fragment_Customer_Form;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Customers;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Recent_Projects;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Payment_Form;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Payments;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Project_Form;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Projects;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Running_Sessions;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Session_Form;
import com.yev.dev.mini_proj_manager.fragments.Fragment_Sessions;
import com.yev.dev.mini_proj_manager.fragments.MyFragment;
import com.yev.dev.mini_proj_manager.utilities.Const;
import com.yev.dev.mini_proj_manager.utilities.DBHelper;
import com.yev.dev.mini_proj_manager.utilities.Utility;

public class MainActivity extends Activity implements OnClickListener{

	private final float LANDSCAPE_MODE_MIN_WIDTH_DP = 650;

	private int LAST_NAV_COMMAND;
	private int LAST_CONT_COMMAND;
	private final String KEY_LAST_NAV_COMMAND = "last_nav_command";
	private final String KEY_LAST_CONT_COMMAND = "last_cont_command";
	
	public interface MainActivityCallbacks{
		
		public void mainActivityMessage(int message, String data);
		
	}
	
	private EditText et_search;
	
	private MainActivityCallbacks navigationCallback;
	private MainActivityCallbacks contentCallback;

	private SlidingPaneLayout slider;

		/*
	private static final String FRAGMENT_TAG_MAIN = "1";
	private static final String FRAGMENT_TAG_CURRENT_SESSIONS = "2";
	private static final String FRAGMENT_TAG_CUSTOMERS = "3";
	private static final String FRAGMENT_TAG_PROJECTS = "4";
	private static final String FRAGMENT_TAG_SESSIONS = "5";
	private static final String FRAGMENT_TAG_PAYMENTS = "6";
	private static final String FRAGMENT_TAG_CUSTOMER_INFO = "7";
	private static final String FRAGMENT_TAG_PROJECT_INFO = "8";
	private static final String FRAGMENT_TAG_SESSION_INFO = "9";
	private static final String FRAGMENT_TAG_PAYMENT_INFO = "10";*/

//------------LIFE CIRCLE--------------------------
	//ON CREATE
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			// clear FLAG_TRANSLUCENT_STATUS flag:
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

			window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));

			//window.setNavigationBarColor(ContextCompat.getColor(this, R.color.status_bar));
		}

		/*
		set to portrait if screen is not larger than XLARGE
		 */
		if (getScreenLargestSideDP() < LANDSCAPE_MODE_MIN_WIDTH_DP) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		ActionBar bar = getActionBar();
		if(bar != null) {
			bar.hide();
		}
		
		if(savedInstanceState == null){
			homePage();
		}else{
			LAST_NAV_COMMAND = savedInstanceState.getInt(KEY_LAST_NAV_COMMAND);
			LAST_CONT_COMMAND = savedInstanceState.getInt(KEY_LAST_CONT_COMMAND);
		}
	
		findViewById(R.id.home).setOnClickListener(this);
		findViewById(R.id.search).setOnClickListener(this);
		
		et_search = (EditText)findViewById(R.id.et_search);

		slider = (SlidingPaneLayout)findViewById(R.id.slider);
		if(slider != null){

			slider.setShadowResourceLeft(R.drawable.shadow_left);
			slider.setSliderFadeColor(Color.parseColor("#00000000"));

			slider.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {

				View navigationPanel = findViewById(R.id.navigation);
				View contentPanel = findViewById(R.id.content);
				@Override
				public void onPanelSlide(View panel, float slideOffset) {
					navigationPanel.setTranslationX( - navigationPanel.getWidth() * (1 - slideOffset));

					contentPanel.setAlpha(1 - slideOffset);
				}

				@Override
				public void onPanelOpened(View panel) {

				}

				@Override
				public void onPanelClosed(View panel) {

				}
			});
		}

	}
	
	//ON SAVE INSTANCE STATE
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(KEY_LAST_CONT_COMMAND, LAST_CONT_COMMAND);
		outState.putInt(KEY_LAST_NAV_COMMAND, LAST_NAV_COMMAND);
		
		super.onSaveInstanceState(outState);
	}

	//ON CONFIGURATION CHANGE
	@Override
	public void onConfigurationChanged (Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		if (getScreenLargestSideDP() < LANDSCAPE_MODE_MIN_WIDTH_DP) {

			//do not change screen orientation if screen is smaller than XLARGE
			return;
		}
	}

	//GET THE DIAGONAL OF SCREEN IN INCHES
	private float getScreenLargestSideDP(){
		float diagonal = 0;

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		return Math.max(metrics.widthPixels / metrics.density, metrics.heightPixels / metrics.density);
	}

	//CHECK IF SCREEN IS LARGE THAN XLARGE
	private boolean isXLargeScreen(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}
//--------------------------------------------------------------------------
	
	
	//ON CLICK
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home:
			homePage();
			break;
		case R.id.search:
			search();
			break;

		default:
			break;
		}
	}
	
	//ON BACK PRESSED
	@Override
	public void onBackPressed() {

		if(LAST_CONT_COMMAND == Const.ADD_CUSTOMER
				|| LAST_CONT_COMMAND == Const.ADD_PROJECT
				|| LAST_CONT_COMMAND == Const.ADD_SESSION
				|| LAST_CONT_COMMAND == Const.ADD_PAYMENT
				|| LAST_CONT_COMMAND == Const.EDIT_CUSTOMER
				|| LAST_CONT_COMMAND == Const.EDIT_PROJECT
				|| LAST_CONT_COMMAND == Const.EDIT_SESSION
				|| LAST_CONT_COMMAND == Const.EDIT_PAYMENT){

			if (slider != null && slider.isOpen()) {
				closeSlider();
			}else{
				contentCallback.mainActivityMessage(Const.CONT_BACK, null);
			}

			return;

		}

		if(slider != null && !slider.isOpen()){
			openSlider();
			return;
		}

		switch (LAST_NAV_COMMAND) {
		case Const.SHOW_PROJECTS:
			navigationCallback.mainActivityMessage(Const.NAV_BACK, null);
			return;
		case Const.SHOW_SESSIONS:
			navigationCallback.mainActivityMessage(Const.NAV_BACK, null);
			return;
		case Const.SHOW_PAYMENTS:
			navigationCallback.mainActivityMessage(Const.NAV_BACK, null);
			return;

		default:
			break;
		}
		
		super.onBackPressed();
	}

	//CLOSE SLIDER
	private void closeSlider(){
		if(slider != null){
			slider.closePane();
		}
	}

	//CLOSE OPEN
	public void openSlider(){
		if(slider != null){
			slider.openPane();
		}
	}


	//SEARCH
	private void search(){
		if(et_search.getText().toString().isEmpty())
			return;
		
		int code = Integer.parseInt(et_search.getText().toString());
		
		HashMap<String, Object> data;

		String CUSTOMER_UID;
		String PROJECT_UID;
		
		Utility utl = new Utility();
		
		if(String.valueOf(code).length() > Const.CODE_LENGHT_PROJECT){
			
			Toast.makeText(this, getString(R.string.invalid_code), Toast.LENGTH_SHORT).show();
		
		}else if(String.valueOf(code).length() > Const.CODE_LENGHT_CUSTOMER){ //PROJECT
			
			data = utl.getProjectByCode(this, code);
			
			CUSTOMER_UID = (String)data.get(DBHelper.COL_OWNERS_UID);
			PROJECT_UID = (String)data.get(DBHelper.COL_UID);
			
			if(PROJECT_UID == null || PROJECT_UID.isEmpty()){
				Toast.makeText(this, R.string.no_matches, Toast.LENGTH_SHORT).show();;
				return;
			}
			
			setNavFragment(false, true, Const.SHOW_SESSIONS, CUSTOMER_UID, PROJECT_UID, null, null);
			setContFragment(true, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
			
		}else{ //CUSTOMER
			
			data = utl.getCustomerByCode(this, code);
		
			CUSTOMER_UID = (String)data.get(DBHelper.COL_UID);
			
			if(CUSTOMER_UID == null || CUSTOMER_UID.isEmpty()){
				Toast.makeText(this, R.string.no_matches, Toast.LENGTH_SHORT).show();;
				return;
			}
			
			setNavFragment(false, true, Const.SHOW_PROJECTS, CUSTOMER_UID, null, null, null);
			setContFragment(true, true, Const.SHOW_CUSTOMER_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, null, null, null);
			
		}
		
		et_search.setText(""); 
	}
	
	//HOME PAGE
	private void homePage(){
		setNavFragment(false, true, Const.SHOW_RECENT_PROJECTS, null, null, null, null);
		setContFragment(false, true, Const.SHOW_RUNNING_SESSIONS, Const.MODE_DEFAULT, null, null, null, null);
	}
	
	//SET NAVIGATION FRAGMENT
	public void setNavFragment(boolean openSlider,
							   boolean anim,
								int COMMAND,
								String CUSTOMER_UID,
								String PROJECT_UID,
								String SESSION_UID,
								String PAYMENT_UID){
		
		MyFragment fragment = null;
						
		switch (COMMAND) {
		case Const.SHOW_RECENT_PROJECTS:
			fragment = new Fragment_Recent_Projects();
			break;
		case Const.SHOW_CUSTOMERS:
			fragment = new Fragment_Customers();
			break;
		case Const.SHOW_PROJECTS:
			fragment = new Fragment_Projects();
			break;
		case Const.SHOW_SESSIONS:
			fragment = new Fragment_Sessions();
			break;
		case Const.SHOW_PAYMENTS:
			fragment = new Fragment_Payments();
			break;

		default:
			break;
		}
		
		fragment.CUSTOMER_UID = CUSTOMER_UID;
		fragment.PROJECT_UID = PROJECT_UID;
		fragment.SESSION_UID = SESSION_UID;
		fragment.PAYMENT_UID = PAYMENT_UID;

		FragmentTransaction f_trans = getFragmentManager()
		.beginTransaction();
		if(anim){
			f_trans.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
		}
		f_trans.replace(R.id.navigation, fragment)
		.commit();
		
		LAST_NAV_COMMAND = COMMAND;

		if(openSlider){
			openSlider();
		}
						
	}
	
	//SET CONTENT FRAGMENT
	public void setContFragment(boolean closeSlider,
								boolean anim,
								int COMMAND,
								int MODE,
								String CUSTOMER_UID,
								String PROJECT_UID,
								String SESSION_UID,
								String PAYMENT_UID){
		MyFragment fragment = null;
		
		switch (COMMAND) {
		case Const.ADD_CUSTOMER:
			fragment = new Fragment_Customer_Form();
			break;
		case Const.ADD_PAYMENT:
			fragment = new Fragment_Payment_Form();
			break;
		case Const.ADD_PROJECT:
			fragment = new Fragment_Project_Form();
			break;
		case Const.ADD_SESSION:
			fragment = new Fragment_Session_Form();
			break;
		case Const.EDIT_CUSTOMER:
			fragment = new Fragment_Customer_Form();
			break;
		case Const.EDIT_PAYMENT:
			fragment = new Fragment_Payment_Form();
			break;
		case Const.EDIT_PROJECT:
			fragment = new Fragment_Project_Form();
			break;
		case Const.EDIT_SESSION:
			fragment = new Fragment_Session_Form();
			break;
		case Const.SHOW_RUNNING_SESSIONS:
			fragment = new Fragment_Running_Sessions();
			break;
		case Const.SHOW_CUSTOMER_INFO:
			fragment = new Fragment_Customer_Form();
			break;
		case Const.SHOW_PAYMENT_INFO:
			fragment = new Fragment_Payment_Form();
			break;
		case Const.SHOW_PROJECT_INFO:
			fragment = new Fragment_Project_Form();
			break;
		case Const.SHOW_SESSION_INFO:
			fragment = new Fragment_Session_Form();
			break;

		default:
			break;
		}
		
		fragment.MODE = MODE;
		
		fragment.CUSTOMER_UID = CUSTOMER_UID;
		fragment.PROJECT_UID = PROJECT_UID;
		fragment.SESSION_UID = SESSION_UID;
		fragment.PAYMENT_UID = PAYMENT_UID;		
		
		FragmentTransaction f_trans = getFragmentManager()
				.beginTransaction();
				if(anim){
					f_trans.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
				}
				f_trans.replace(R.id.content, fragment)
				.commit();
				
		LAST_CONT_COMMAND = COMMAND;

		if(closeSlider){
			closeSlider();
		}
	}
	
	//SET CONT LISTENER
	public void setContListener(MyFragment fragment){
		contentCallback = fragment;
	}
	
	//SET NAV LISTENER
	public void setNavListener(MyFragment fragment){
		navigationCallback = fragment;
	}
	
}
