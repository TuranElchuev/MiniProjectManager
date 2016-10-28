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
import com.yev.dev.mini_proj_manager.utilities.Const;
import com.yev.dev.mini_proj_manager.utilities.DBHelper;
import com.yev.dev.mini_proj_manager.R;

public class Fragment_Customers extends MyFragment implements OnClickListener {

	private MainActivity activity;
	
	private ListView list;
	private ArrayList<HashMap<String, Object>> data;
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
		View v = inflater.inflate(R.layout.fragment_customers, container, false);

		v.findViewById(R.id.recent_projects).setOnClickListener(this);
		v.findViewById(R.id.add).setOnClickListener(this);

		list = (ListView)v.findViewById(R.id.list);

		list.addFooterView(inflater.inflate(R.layout.list_footer, null));

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									final int position, long id) {

				if(position > data.size() - 1){
					return;
				}

				CUSTOMER_UID = (String)data.get(position).get(DBHelper.COL_UID);
				activity.setContFragment(false, true, Const.SHOW_CUSTOMER_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, null, null, null);
				activity.setNavFragment(false, true, Const.SHOW_PROJECTS, CUSTOMER_UID, null, null, null);
			}
		});
		
		return v;
	}
	
	//SET DATA
	private void setData(){
		data = utility.getCustomers(activity);
		
		adapter = new MyAdapter(activity, data, 0, null, null, activity.getLayoutInflater());
		
		list.setAdapter(adapter);
	}

	//ON CLICK
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.add:
				activity.setContFragment(true, true, Const.ADD_CUSTOMER, Const.MODE_NEW, null, null, null, null);
				break;
			case R.id.recent_projects:
				showRecentProjects();
				break;

			default:
				break;
		}
	}

	//SHOW RECENT PROJECTS
	private void showRecentProjects(){
		activity.setNavFragment(false, true, Const.SHOW_RECENT_PROJECTS, null, null, null, null);
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
		    		convertView = inflater.inflate(R.layout.list_item_customer, null);
		    	}
		    	
		    	HashMap<String, Object>hm = data.get(position);
		    	
		    	((TextView)convertView.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt((int)hm.get(DBHelper.COL_CUSTOMER_CODE), Const.CODE_LENGHT_CUSTOMER));
		    	((TextView)convertView.findViewById(R.id.t_name)).setText((String)hm.get(DBHelper.COL_NAME));

		    	int balance = (int)hm.get(DBHelper.COL_BALANCE);
				TextView t_balance = (TextView)convertView.findViewById(R.id.t_balance);
				t_balance.setTextColor(utility.getMoneyColor(activity, balance));
				t_balance.setText(utility.getMoneyString(balance));	
		    	
		    	((ImageView)convertView.findViewById(R.id.icon)).setImageResource(R.drawable.ic_project);
		    	
		    	ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
		    	
		    	if((int)hm.get(DBHelper.COL_REMOVED) == 1){
		    		icon.setImageResource(R.drawable.ic_customer_removed);
		    		convertView.setAlpha(Const.REMOVED_ALPHA);
		    	}else{
		    		icon.setImageResource(R.drawable.ic_customer);
		    		convertView.setAlpha(1f);
		    	}
		    	
		        return convertView;
		    }
	}
	

}
