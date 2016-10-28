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
import android.view.View.OnClickListener;
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

public class Fragment_Sessions extends MyFragment implements OnClickListener{

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
		
		activity.setNavListener(this);
		
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
		
		v = inflater.inflate(R.layout.fragment_sessions, container, false);

		v.findViewById(R.id.add).setOnClickListener(this);
		v.findViewById(R.id.payments).setOnClickListener(this);
		v.findViewById(R.id.ll_customer).setOnClickListener(this);
		v.findViewById(R.id.ll_project).setOnClickListener(this);

		list = (ListView)v.findViewById(R.id.list);
		list.addFooterView(inflater.inflate(R.layout.list_footer, null));
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									final int position, long id) {

				if(position > data.size() - 1){
					return;
				}

				SESSION_UID = (String)data.get(position).get(DBHelper.COL_UID);
				activity.setContFragment(true, true, Const.SHOW_SESSION_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, SESSION_UID, null);
				adapter.notifyDataSetChanged();
			}
		});
		
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

	//SET DATA
	private void setData(){
		data = utility.getSessions(activity, PROJECT_UID);
		
		adapter = new MyAdapter(activity, data, 0, null, null, activity.getLayoutInflater());
		
		list.setAdapter(adapter);
		
		if(SESSION_UID != null){
			int pos = 0;
			for(HashMap<String, Object>hm: data){
				String UID = (String)hm.get(DBHelper.COL_UID);
				if(UID.equals(SESSION_UID)){
					break;
				}
				pos++;
			}
			list.setSelection(pos);
		}
		
		
		HashMap<String, Object> customerData = utility.getCustomer(activity, CUSTOMER_UID);
		
		ImageView icon_customer = (ImageView)v.findViewById(R.id.icon_customer);
		if((int)customerData.get(DBHelper.COL_REMOVED) == 1){
			icon_customer.setImageResource(R.drawable.ic_customer_removed);
		}else{
			icon_customer.setImageResource(R.drawable.ic_customer);
		}
		
		int customer_balance = (int)customerData.get(DBHelper.COL_BALANCE);
		TextView t_customer_balance = (TextView)v.findViewById(R.id.t_customer_balance);
		t_customer_balance.setTextColor(utility.getMoneyColor(activity, customer_balance));
						
		((TextView)v.findViewById(R.id.t_customer_code)).setText(utility.getStringCodeFromInt((int)customerData.get(DBHelper.COL_CUSTOMER_CODE), Const.CODE_LENGHT_CUSTOMER));
		((TextView)v.findViewById(R.id.t_customer_name)).setText((String)customerData.get(DBHelper.COL_NAME));
		t_customer_balance.setText(utility.getMoneyString(customer_balance));
		
		
		
		HashMap<String, Object> projectData = utility.getProject(activity, PROJECT_UID);
		
		ImageView icon_project = (ImageView)v.findViewById(R.id.icon_project);
		if((int)projectData.get(DBHelper.COL_REMOVED) == 1){
			icon_project.setImageResource(R.drawable.ic_project_removed);
		}else{
			icon_project.setImageResource(R.drawable.ic_project);
		}
		
		int project_balance = (int)projectData.get(DBHelper.COL_BALANCE);
		TextView t_project_balance = (TextView)v.findViewById(R.id.t_project_balance);
		t_project_balance.setTextColor(utility.getMoneyColor(activity, project_balance));
				
		((TextView)v.findViewById(R.id.t_project_code)).setText(utility.getStringCodeFromInt((int)projectData.get(DBHelper.COL_PROJECT_CODE), Const.CODE_LENGHT_PROJECT));
		((TextView)v.findViewById(R.id.t_project_name)).setText((String)projectData.get(DBHelper.COL_NAME));
		t_project_balance.setText(utility.getMoneyString(project_balance));
		
		
		duration_thread = new DurationThread();
		duration_thread.start();
		
	}
	
	//MAIN ACTIVITY MESSAGE
	@Override
	public void mainActivityMessage(int message, String data) {
		
		switch (message) {
		case Const.NAV_BACK:
			activity.setNavFragment(false, true, Const.SHOW_PROJECTS, CUSTOMER_UID, null, null, null);
			activity.setContFragment(false, true, Const.SHOW_CUSTOMER_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, null, null, null);
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
			case R.id.add:
				activity.setContFragment(true, true, Const.ADD_SESSION, Const.MODE_NEW, CUSTOMER_UID, PROJECT_UID, null, null);
				break;
			case R.id.payments:
				activity.setNavFragment(false, true, Const.SHOW_PAYMENTS, CUSTOMER_UID, PROJECT_UID, null, null);
				activity.setContFragment(false, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
				break;
			case R.id.ll_customer:
				activity.setContFragment(false, true, Const.SHOW_CUSTOMER_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
				activity.setNavFragment(false, true, Const.SHOW_PROJECTS, CUSTOMER_UID, null, null, null);
				break;
			case R.id.ll_project:
				activity.setContFragment(true, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
				activity.setNavFragment(false, true, Const.SHOW_SESSIONS, CUSTOMER_UID, PROJECT_UID, null, null);
				break;

			default:
				break;
			}
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
		    		convertView = inflater.inflate(R.layout.list_item_session, null);
		    	}
		    	
		    	HashMap<String, Object>hm = data.get(position);
		    			    	
		    	((TextView)convertView.findViewById(R.id.t_name)).setText((String)hm.get(DBHelper.COL_NAME));
		    	((TextView)convertView.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt(((int)hm.get(DBHelper.COL_ID)), Const.CODE_LENGHT_SESSION));		    	
		    	
		    	long start_time = (long)hm.get(DBHelper.COL_TIME_START);
		    	long end_time = (long)hm.get(DBHelper.COL_TIME_END);
				int rate = (int)hm.get(DBHelper.COL_HOURLY_RATE);
				
				((TextView)convertView.findViewById(R.id.t_added)).setText(utility.getFormattedTime(start_time, Const.DATE_FORMAT_DATE));
				
				((TextView)convertView.findViewById(R.id.t_rate)).setText(utility.getMoneyString(rate));
				
				ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
				
		    	if(end_time == 0){
		    		long current_time = System.currentTimeMillis();
		    		((TextView)convertView.findViewById(R.id.t_duration)).setText(utility.getFormattedDuration(current_time - start_time));
		    		((TextView)convertView.findViewById(R.id.t_amount)).setText(utility.getMoneyString((int)utility.getSessionCosts(current_time - start_time, rate))); 
		    	}else{
		    		((TextView)convertView.findViewById(R.id.t_duration)).setText(utility.getFormattedDuration(end_time - start_time));
		    		((TextView)convertView.findViewById(R.id.t_amount)).setText(utility.getMoneyString((int)hm.get(DBHelper.COL_AMOUNT)));
		    	}
		    	
		    	if((int)hm.get(DBHelper.COL_REMOVED) == 1){
		    		icon.setImageResource(R.drawable.ic_time_removed);
		    		convertView.setAlpha(Const.REMOVED_ALPHA);
		    	}else if(end_time == 0){
		    		icon.setImageResource(R.drawable.ic_running);
		    		convertView.setAlpha(1f);
		    	}else{
		    		icon.setImageResource(R.drawable.ic_completed);
		    		convertView.setAlpha(1f);
		    	}
		    	
		    	if(((String)hm.get(DBHelper.COL_UID)).equals(SESSION_UID)){
		    		convertView.setBackgroundResource(R.drawable.selector_list_item_focused);
		    	}else{
		    		convertView.setBackgroundResource(R.drawable.selector_list_item_not_focused);
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
