package com.beiang.airdog.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

import com.beiang.airdog.net.api.WApi;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.ui.base.BaseActivity;
import com.beiang.airdog.utils.ADConfigUtil;
import com.beiang.airdog.utils.ConfigUtils;
import com.beiang.airdog.utils.LocationUtils;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SplashActivity extends BaseActivity {
	ImageView iv_ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		iv_ad = (ImageView) findViewById(R.id.iv_ad);
		
		new LocationUtils().startLocation(mActivity, null);

		loadView();
		update();
	}

	boolean showAd = false;
	private void loadView() {
		Bitmap bitmap = new ADConfigUtil().getAdPic();
		if (bitmap != null) {
			iv_ad.setImageBitmap(bitmap);
		} else {
			iv_ad.setImageResource(R.drawable.loading_bg);
		}
	}
	
	private void update(){
		ConfigUtils.update(mActivity, new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				// TODO Auto-generated method stub
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(mActivity, updateInfo);
					break;
				case UpdateStatus.No: // has no update
					startIntent();
					break;
				case UpdateStatus.Timeout: // time out
					startIntent();
					break;
				}
			}
		});
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
		    @Override
		    public void onClick(int status) {
		        switch (status) {
		        case UpdateStatus.Update:
		            Toast.show(mActivity, "正在更新应用");
		            startIntent();
		            break;
		        case UpdateStatus.Ignore:
		        	startIntent();
		            break;
		        case UpdateStatus.NotNow:
		        	startIntent();
		            break;
		        }
		    }
		});
	}

	private void startIntent() {
		if (CurrentUser.instance().isLogin()) {
			if (!TextUtils.isEmpty(CurrentUser.instance().getPhone())) {
				startActivity(new Intent(mActivity, DeviceActivity.class));
			} else {
				startActivity(new Intent(mActivity, BindUserActivity.class));
			}
		} else {
			startActivity(new Intent(mActivity, LoginActivity.class).putExtra(LoginActivity.ACTION, "nomal"));
		}
		finish();
	}
}
