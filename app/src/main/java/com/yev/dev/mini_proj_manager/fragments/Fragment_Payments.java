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

public class Fragment_Payments extends MyFragment implements OnClickListener {

	private MainActivity activity;
	public ListView list;
	public ArrayList<HashMap<String, Object>> data;
	public MyAdapter adapter;
	public View v;
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
		
		v = inflater.inflate(R.layout.fragment_payments, container, false);

		v.findViewById(R.id.add).setOnClickListener(this);
		v.findViewById(R.id.sessions).setOnClickListener(this);
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

				PAYMENT_UID = (String)data.get(position).get(DBHelper.COL_UID);
				activity.setContFragment(true, true, Const.SHOW_PAYMENT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, PAYMENT_UID);
				adapter.notifyDataSetChanged();
			}
		});
		
		return v;
	}

	//SET DATA
	public void setData(){
		data = utility.getPayments(activity, PROJECT_UID);
		
		adapter = new MyAdapter(activity, data, 0, null, null, activity.getLayoutInflater());
		
		list.setAdapter(adapter);
		

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
			activity.setContFragment(true, true, Const.ADD_PAYMENT, Const.MODE_NEW, CUSTOMER_UID, PROJECT_UID, null, null);
			break;
		case R.id.sessions:
			activity.setNavFragment(false, true, Const.SHOW_SESSIONS, CUSTOMER_UID, PROJECT_UID, null, null);
			activity.setContFragment(false, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
			break;
		case R.id.ll_customer:
			activity.setContFragment(false, true, Const.SHOW_CUSTOMER_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
			activity.setNavFragment(false, true, Const.SHOW_PROJECTS, CUSTOMER_UID, null, null, null);
			break;
		case R.id.ll_project:
			activity.setContFragment(true, true, Const.SHOW_PROJECT_INFO, Const.MODE_DEFAULT, CUSTOMER_UID, PROJECT_UID, null, null);
			activity.setNavFragment(false, true, Const.SHOW_PAYMENTS, CUSTOMER_UID, PROJECT_UID, null, null);
			break;

		default:
			break;
		}
	}
	
	//ADAPTER
	public class MyAdapter extends SimpleAdapter{
		
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
		    		convertView = inflater.inflate(R.layout.list_item_payment, null);
		    	}
		    	
		    	HashMap<String, Object>hm = data.get(position);
		    			    	
		    	((TextView)convertView.findViewById(R.id.t_added)).setText(utility.getFormattedTime((long)hm.get(DBHelper.COL_TIME_ADDED), Const.DATE_FORMAT_DATETIME));		    	
		    	((TextView)convertView.findViewById(R.id.t_amount)).setText(utility.getMoneyString((int)hm.get(DBHelper.COL_AMOUNT)));		    	
		    	((TextView)convertView.findViewById(R.id.t_code)).setText(utility.getStringCodeFromInt(((int)hm.get(DBHelper.COL_ID)), Const.CODE_LENGHT_PAYMENT));
		    	
		    	ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
		    	
		    	if((int)hm.get(DBHelper.COL_REMOVED) == 1){
		    		icon.setImageResource(R.drawable.ic_money_removed);
		    		convertView.setAlpha(Const.REMOVED_ALPHA);
		    	}else{
		    		icon.setImageResource(R.drawable.ic_money);
		    		convertView.setAlpha(1f);
		    	}
		    	
		    	
		    	if(((String)hm.get(DBHelper.COL_UID)).equals(PAYMENT_UID)){
		    		convertView.setBackgroundResource(R.drawable.selector_list_item_focused);
		    	}else{
		    		convertView.setBackgroundResource(R.drawable.selector_list_item_not_focused);
		    	}
		    	
		        return convertView;
		 }
	}
}
