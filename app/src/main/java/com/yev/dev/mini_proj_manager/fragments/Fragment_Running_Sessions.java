package com.yev.dev.mini_proj_manager.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.yev.dev.mini_proj_manager.MainActivity;
import com.yev.dev.mini_proj_manager.R;
import com.yev.dev.mini_proj_manager.utilities.Const;
import com.yev.dev.mini_proj_manager.utilities.DBHelper;
import com.yev.dev.mini_proj_manager.utilities.Utility;

public class Fragment_Running_Sessions extends MyFragment {

	private MainActivity activity;
	private ListView list;
	private ArrayList<HashMap<String, Object>> data;
	private MyAdapter adapter;
	View v;
	
	private DurationThread duration_thread;
	
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
		
		setData();
		
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
		
		v = inflater.inflate(R.layout.fragment_running_sessions, container, false);
		
		setupViews();
		
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
	
	//SETUP VIEWS
	private void setupViews(){
		
		list = (ListView)v.findViewById(R.id.list);
		
		list.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                final int position, long id) {
	        	
	        	String SESSION_UID = (String)data.get(position).get(DBHelper.COL_UID);
	        	String PROJECT_UID = (String)data.get(position).get(Utility.HM_PROJECT_UID);
	        	String CUSTOMER_UID = (String)data.get(position).get(Utility.HM_CUSTOMER_UID);
	        	
	        	activity.setNavFragment(false, true, Const.SHOW_SESSIONS, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
	        	
	        	activity.setContFragment(false, true, Const.SHOW_SESSION_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
	        }
	    });
		
	}
	
	//SET DATA
	private void setData(){
		data = utility.getRunningSessions(activity);
		
		if(data.size() > 0){
			((TextView)v.findViewById(R.id.t_no_running_sessions)).setVisibility(View.GONE);
		}else{
			return;
		}
		
		adapter = new MyAdapter(activity, data, 0, null, null, activity.getLayoutInflater());
		
		list.setAdapter(adapter);
						
		duration_thread = new DurationThread();
		duration_thread.start();
		
	}
	
	//MAIN ACTIVITY MESSAGE
	@Override
	public void mainActivityMessage(int message, String data) {
		super.mainActivityMessage(message, data);
	}
	
	//ADAPTER
	private class MyAdapter extends SimpleAdapter{
		
		LayoutInflater inflater;
		
		public MyAdapter(Context context,
				List <? extends Map < String, Object >> data,
		        int resource, 
		        String[] from,
		        int[] to,
		        LayoutInflater inflater) {
			
				super(context, data, resource, from, to);
				
				this.inflater = inflater;
		        
		    }

		@Override
		public int getCount() {
			return data.size();
		}
		
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		    	
		    	if(convertView == null){
		    		convertView = inflater.inflate(R.layout.list_item_running_session, null);
		    	}
		    	
		    	HashMap<String, Object>hm = data.get(position);
		    	
		    	
		    	((TextView)convertView.findViewById(R.id.t_name)).setText((String)hm.get(DBHelper.COL_NAME));
		    	((TextView)convertView.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt(((int)hm.get(DBHelper.COL_ID)), Const.CODE_LENGHT_SESSION));		    	
		    	
		    	long start_time = (long)hm.get(DBHelper.COL_TIME_START);
				int rate = (int)hm.get(DBHelper.COL_HOURLY_RATE);
				
				((TextView)convertView.findViewById(R.id.t_added)).setText(utility.getFormattedTime(start_time, Const.DATE_FORMAT_DATE));
				((TextView)convertView.findViewById(R.id.t_rate)).setText(utility.getMoneyString(rate));
				
	    		long current_time = System.currentTimeMillis();
	    		((TextView)convertView.findViewById(R.id.t_duration)).setText(utility.getFormattedDuration(current_time - start_time));
	    		((TextView)convertView.findViewById(R.id.t_amount)).setText(utility.getMoneyString((int)utility.getSessionCosts(current_time - start_time, rate))); 
	    	
	    		
	    		((TextView)convertView.findViewById(R.id.t_customer_code)).setText(utility.getStringCodeFromInt((int)hm.get(DBHelper.COL_CUSTOMER_CODE), Const.CODE_LENGHT_CUSTOMER));
	    		((TextView)convertView.findViewById(R.id.t_customer_name)).setText((String)hm.get(Utility.HM_CUSTOMER_NAME));
	    		((TextView)convertView.findViewById(R.id.t_project_code)).setText(String.valueOf(utility.getStringCodeFromInt((int)hm.get(DBHelper.COL_PROJECT_CODE), Const.CODE_LENGHT_PROJECT)));
	    		((TextView)convertView.findViewById(R.id.t_project_name)).setText((String)hm.get(Utility.HM_PROJECT_NAME));
	    		
	    		ImageView icon_customer = (ImageView)convertView.findViewById(R.id.icon_customer);
	    		if((int)hm.get(Utility.HM_CUSTOMER_REMOVED) == 1){
	    			icon_customer.setImageResource(R.drawable.ic_customer_removed);
	    		}else{
	    			icon_customer.setImageResource(R.drawable.ic_customer);
	    		}
	    		
	    		ImageView icon_project = (ImageView)convertView.findViewById(R.id.icon_project);
	    		if((int)hm.get(Utility.HM_PROJECT_REMOVED) == 1){
	    			icon_project.setImageResource(R.drawable.ic_project_removed);
	    		}else{
	    			icon_project.setImageResource(R.drawable.ic_project);
	    		}
	    		
		        return convertView;
		    }
	}

	//UPDATE DURATION
	private void updateDuration(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		} 
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
}
