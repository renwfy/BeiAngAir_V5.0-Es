package com.beiang.airdog.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.broadlink.beiangair.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class HelpActivity extends BaseMultiPartActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		
		setMenuEnable(false);

		initView();
	}

	
	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		String verson = null;
		try {
			verson = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((TextView) findViewById(R.id.cur_verson)).setText("当前版本：" + verson);

		findViewById(R.id.about).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mActivity, AboutActivity.class));
			}
		});
		findViewById(R.id.check_update).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog("检查更新");
				UmengUpdateAgent.setUpdateOnlyWifi(false);
				UmengUpdateAgent.setUpdateCheckConfig(false);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
						// TODO Auto-generated method stub
						hideDialog();
						switch (updateStatus) {
						case UpdateStatus.Yes: // has update
							UmengUpdateAgent.showUpdateDialog(mActivity, updateInfo);
							break;
						case UpdateStatus.No: // has no update
							Toast.makeText(mActivity, "当前已是最新版", Toast.LENGTH_SHORT).show();
							break;
						case UpdateStatus.NoneWifi: // none wifi
							// Toast.makeText(mActivity, "没有wifi连接， 只在wifi下更新",
							// Toast.LENGTH_SHORT).show();
							break;
						case UpdateStatus.Timeout: // time out
							Toast.makeText(mActivity, "连接超时", Toast.LENGTH_SHORT).show();
							break;
						}

					}
				});
				UmengUpdateAgent.forceUpdate(mActivity);
			}
		});
	}
}
