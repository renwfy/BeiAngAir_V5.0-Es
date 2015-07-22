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
import com.broadlink.beiangair.R;

public class SplashActivity extends BaseActivity {
	ImageView iv_ad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		iv_ad = (ImageView) findViewById(R.id.iv_ad);
		
		new LocationUtils().startLocation(mActivity, null);

		loadView();
		ConfigUtils.autoUpdate(mActivity);
	}

	boolean showAd = false;
	private void loadView() {
		Bitmap bitmap = new ADConfigUtil().getAdPic();
		if (bitmap != null) {
			iv_ad.setImageBitmap(bitmap);
		} else {
			iv_ad.setImageResource(R.drawable.loading_bg);
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (CurrentUser.instance().isLogin()) {
					if(!TextUtils.isEmpty(CurrentUser.instance().getPhone())){
						startActivity(new Intent(mActivity, IHomerActivity.class));
					}else{
						startActivity(new Intent(mActivity, BindUserActivity.class));
					}
				} else {
					startActivity(new Intent(mActivity, LoginActivity.class).putExtra(LoginActivity.ACTION, "nomal"));
				}
				finish();
			}
		}, 2000);
	}
}
