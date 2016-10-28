package com.yev.dev.mini_proj_manager.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
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

public class Fragment_Recent_Projects extends MyFragment implements OnClickListener {

	private MainActivity activity;
	
	private ListView list;
	private ArrayList<HashMap<String, Object>> recentProjectsData;
	private MyAdapter adapter;
	
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
		View v = inflater.inflate(R.layout.fragment_recent_projects, container, false);
		
		setupViews(v);
		
		return v;
	}
	
	
	//SETUP VIEWS
	private void setupViews(View v){
		v.findViewById(R.id.customers).setOnClickListener(this);
		
		list = (ListView)v.findViewById(R.id.list);
		
		list.setOnItemClickListener(new OnItemClickListener() {

	        @Override
	        public void onItemClick(AdapterView<?> parent, View view,
	                final int position, long id) {
	        	
	        	PROJECT_UID = (String)recentProjectsData.get(position).get(DBHelper.COL_UID);
	        	CUSTOMER_UID = (String)recentProjectsData.get(position).get(DBHelper.COL_OWNERS_UID);
	        	activity.setContFragment(false, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
	        	activity.setNavFragment(false, true, Const.SHOW_SESSIONS, CUSTOMER_UID, PROJECT_UID, null, null);
	        }
	    });
	}
	
	//SET DATA
	private void setData(){
		recentProjectsData = utility.getRecentProjects(activity);
		
		adapter = new MyAdapter(activity, recentProjectsData, 0, null, null, activity.getLayoutInflater());
		
		list.setAdapter(adapter);
			
	}
	
	//MAIN ACTIVITY MESSAGE
	@Override
	public void mainActivityMessage(int message, String data) {
		super.mainActivityMessage(message, data);
	}
	
	//ON CLICK
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.customers:
			showCustomers();
			break;

		default:
			break;
		}
	}
	
	
	//SHOW CUSTOMERS
	private void showCustomers(){
		activity.setNavFragment(false, true, Const.SHOW_CUSTOMERS, null, null, null, null);
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
			return recentProjectsData.size();
		}
		
		    @Override
		    public View getView(int position, View convertView, ViewGroup parent) {
		    	
		    	if(convertView == null){
		    		convertView = inflater.inflate(R.layout.list_item_project, null);
		    	}
		    	
		    	HashMap<String, Object>hm = recentProjectsData.get(position);
		    	
		    	((TextView)convertView.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)hm.get(DBHelper.COL_PROJECT_CODE), Const.CODE_LENGHT_PROJECT));
		    	((TextView)convertView.findViewById(R.id.t_name)).setText((String)hm.get(DBHelper.COL_NAME));
		    				    
		    	int project_balance = (int)hm.get(DBHelper.COL_BALANCE);
				TextView t_project_balance = (TextView)convertView.findViewById(R.id.t_balance);
				t_project_balance.setTextColor(utility.getMoneyColor(activity, project_balance));
				t_project_balance.setText(utility.getMoneyString(project_balance));	
		    	
		    	((ImageView)convertView.findViewById(R.id.icon)).setImageResource(R.drawable.ic_project);
				
		    	
		        return convertView;
		    }
	}

	
}
