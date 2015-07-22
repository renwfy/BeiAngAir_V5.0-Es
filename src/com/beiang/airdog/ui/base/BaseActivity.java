package com.beiang.airdog.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.ui.ActivityManager;
import com.beiang.airdog.widget.CustomProgressDialog;


public class BaseActivity extends FragmentActivity {
	private CustomProgressDialog dialog;
	public BeiAngAirApplaction mApplication;
	public ActivityManager activityManager;
	public Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = this;
		mApplication = BeiAngAirApplaction.getInstance();
		activityManager = ActivityManager.getScreenManager();

		activityManager.pushActivity(this);
		
		 initDialog();
	}
	
	
	 public void initDialog() {
			// TODO Auto-generated method stub
			dialog = CustomProgressDialog.createDialog(mActivity);
			dialog.setCanceledOnTouchOutside(false);
		}
		
		public void showDialog(String msg) {
			if (!"".equals(msg))
				dialog.setMessage(msg);
			
			if (!dialog.isShowing())
				dialog.show();

		}
		
		public void hideDialog() {
			if (dialog.isShowing())
				dialog.dismiss();

		}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (activityManager != null) {
			activityManager.popActivity(this);
		}
		super.onDestroy();
	}
}
