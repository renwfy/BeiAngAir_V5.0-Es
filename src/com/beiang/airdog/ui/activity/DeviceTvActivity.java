package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.view.WeatherView;
import com.broadlink.beiangair.R;

public class DeviceTvActivity extends BaseMultiPartActivity implements OnClickListener {
	private DevEntity mDevice;
	private AirInfo mAirInfo;

	Handler mHandler;
	private boolean canRefrash;
	private boolean isActivity;

	private WeatherView weather_layout;
	
	TextView tv_inside_temper_value, tv_inside_humidity_value, tv_inside_time, tv_inside_date, tv_dev_nickname,tv_pm25_inside;
	ImageView iv_cr, battery;

	Animation roatAnim;
	AnimationDrawable animBatteryCharging;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tv);

		isActivity = true;
		mDevice = CurrentDevice.instance().curDevice;
		mAirInfo = mDevice.airInfo;

		mHandler = new Handler();

		roatAnim = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_anim);

		initView();
		initMenuData();

		setData();
		weather_layout.load();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		canRefrash = true;
		
		queryMXData();
	}

	private void initView() {
		weather_layout = (WeatherView) findViewById(R.id.weather_layout);

		tv_inside_temper_value = (TextView) findViewById(R.id.tv_inside_temper_value);
		tv_inside_humidity_value = (TextView) findViewById(R.id.tv_inside_humidity_value);
		tv_inside_time = (TextView) findViewById(R.id.tv_inside_time);
		tv_inside_date = (TextView) findViewById(R.id.tv_inside_date);
		tv_dev_nickname = (TextView) findViewById(R.id.tv_dev_nickname);
		tv_pm25_inside = (TextView) findViewById(R.id.tv_pm25_inside);

		iv_cr = (ImageView) findViewById(R.id.iv_cr);
		battery = (ImageView) findViewById(R.id.battery_charging);
	}
	
	private void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("edit");
		menu.setMenu_name("自定义");
		menu.setMenu_icon(R.drawable.ic_menu_edit);
		menus.add(menu);
		prepareOptionsMenu(menus);
	}


	private void setData() {
		if (mAirInfo != null) {
			int temp = mAirInfo.getTem();
			if (temp > 128) {
				temp = temp - 256;
			}
			tv_inside_temper_value.setText(temp + "");
			tv_inside_humidity_value.setText(mAirInfo.getHum() + "");

			
			int airValue = mAirInfo.getAirValue();
			// pm2.5值 --
			int airPm = airValue;
			if (Device.DT_280E == mDevice.devType) {
				airPm = (airValue / 30) + 8;
			}
			tv_pm25_inside.setText(airPm + "");
		}

	}


	/**************
	 * 定时器
	 */
	private void startMxRefreshTimer() {
		mHandler.postDelayed(task, 4500);
	}

	private void stopMxRefreshTimer() {
		canRefrash = false;
		mHandler.removeCallbacks(task);
	}

	private Runnable task = new Runnable() {
		public void run() {
			if (canRefrash && isActivity) {
				queryMXData();
			}
		}
	};

	private void queryMXData() {
		BsOperationHub.instance().queryDevStatus(mDevice.devId, "beiang_status", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (canRefrash) {
					if (rspData.isSuccess()) {
						AirInfo airInfo = CurrentDevice.instance().queryDevice.airInfo;
						if (airInfo != null) {
							mAirInfo = null;
							mAirInfo = airInfo;
							setData();
						}
					}
					canRefrash = true;
					startMxRefreshTimer();
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				if (canRefrash) {
					canRefrash = true;
					startMxRefreshTimer();
				}
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		canRefrash = false;
		stopMxRefreshTimer();
	}

	/***
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		isActivity = false;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
	}

}
