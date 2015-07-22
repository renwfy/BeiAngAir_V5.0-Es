package com.beiang.airdog.ui.base;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import android.app.Activity;
import android.os.Bundle;

import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.ui.ActivityManager;
import com.beiang.airdog.widget.CustomProgressDialog;

/***
 * 支持左滑关闭
 * 
 * @author LSD
 *
 */
public class BaseSwipebackActivity extends SwipeBackActivity {
	public BeiAngAirApplaction mApplication;
	public ActivityManager activityManager;
	public Activity mActivity;
	private CustomProgressDialog dialog;

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
