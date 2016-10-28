package com.yev.dev.mini_proj_manager.fragments;

import android.app.Fragment;

import com.yev.dev.mini_proj_manager.MainActivity.MainActivityCallbacks;
import com.yev.dev.mini_proj_manager.utilities.Utility;

public class MyFragment extends Fragment implements MainActivityCallbacks {

	public int MODE;
	
	public String CUSTOMER_UID;
	public String PROJECT_UID;
	public String SESSION_UID;
	public String PAYMENT_UID;
			
	public Utility utility = new Utility();

	@Override
	public void mainActivityMessage(int message, String data) {
		// TODO Auto-generated method stub

	}
	
}
