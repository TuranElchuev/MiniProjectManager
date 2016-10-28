package com.yev.dev.mini_proj_manager.utilities;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yev.dev.mini_proj_manager.R;

public class Dialogs {

	public Dialog getConfirmationDialog(Context context,
										String action,
										String message){
		
		final Dialog d = new Dialog(context);
		
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		d.setContentView(R.layout.dialog_confirm);
		
		((TextView)d.findViewById(R.id.t_action)).setText(action);
		((TextView)d.findViewById(R.id.t_message)).setText(message);
		
		((Button)d.findViewById(R.id.cancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				d.cancel();
			}
		});
		
		return d;
		
	}
	
}
