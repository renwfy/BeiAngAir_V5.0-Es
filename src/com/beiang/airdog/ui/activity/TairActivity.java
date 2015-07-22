package com.beiang.airdog.ui.activity;

import java.util.Calendar;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.business.homer.QueryWeatherPair;
import com.beiang.airdog.net.business.homer.QueryWeatherPair.RspQueryWeather.Weatherinfo;
import com.beiang.airdog.net.business.homer.getDevIpPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

public class TairActivity extends BaseMultiPartActivity implements OnClickListener{
	private DevEntity mDevice;
	private AirInfo mAirInfo;

	Handler mHandler;
	private TextView tv_location, tv_time, tv_pm25, tv_level, tv_temper, tv_humidity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tair_layout);

		mDevice = CurrentDevice.instance().curDevice;
		mAirInfo = mDevice.airInfo;

		mHandler = new Handler();

		initView();
		setMenuEnable(false);

		setData();

		queryDevIp();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		queryMXData();
	}

	private void initView() {
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_pm25 = (TextView) findViewById(R.id.tv_pm25);
		tv_level = (TextView) findViewById(R.id.tv_level);
		tv_temper = (TextView) findViewById(R.id.tv_temper);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);
	}

	private void setData() {

	}

	private void initWeatherLayout(QueryWeatherPair.RspQueryWeather weather) {
		if (weather.data != null) {
			Weatherinfo weatherinfo = weather.data.weatherinfo;
			tv_location.setText(weatherinfo.city);

			Calendar curCalendar = Calendar.getInstance();
			int curH = curCalendar.get(Calendar.HOUR_OF_DAY);
			int curMin = curCalendar.get(Calendar.MINUTE);
			tv_time.setText("今天" + String.format("%02d", curH) + ":" + String.format("%02d", curMin) + "发布");

			tv_pm25.setText(weatherinfo.pm25);
			int pm25 = Integer.parseInt(weatherinfo.pm25);
			if (pm25 < 35) {
				tv_level.setText("空气洁净");
			}
			if (pm25 > 35 && pm25 < 73) {
				tv_level.setText("空气良好");
			}
			if (pm25 > 75 && pm25 < 115) {
				tv_level.setText("轻度污染");
			}
			if (pm25 > 115 && pm25 < 150) {
				tv_level.setText("中度污染");
			}
			if (pm25 > 150 && pm25 < 250) {
				tv_level.setText("重度污染");
			}
			if (pm25 > 250) {
				tv_level.setText("重度污染");
			}

			tv_temper.setText("温度" + weatherinfo.temp + "℃");
			tv_humidity.setText("湿度" + weatherinfo.humidity + "%");
		}
	}

	private void queryDevIp() {
		BsOperationHub.instance().queryDevIp(mDevice.devId, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					getDevIpPair.RspGetDevIp rsp = (getDevIpPair.RspGetDevIp) rspData;
					if (rsp.data != null) {
						String ip = rsp.data.ip;
						queryWeatherFromNet(ip);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void queryWeatherFromNet(String ip) {
		BsOperationHub.instance().queryWeather(ip, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					QueryWeatherPair.RspQueryWeather rsp = (QueryWeatherPair.RspQueryWeather) rspData;
					initWeatherLayout(rsp);
				} else {
					String errStr = rspData.getErrorString();
					if (!TextUtils.isEmpty(errStr)) {
						Toast.show(mActivity, errStr);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				String errStr = err.getErrorString();
				if (!TextUtils.isEmpty(errStr)) {
					Toast.show(mActivity, errStr);
				}
			}
		});
	}

	private void queryMXData() {
		LogUtil.i("queryMXData()");
		BsOperationHub.instance().queryDevStatus(mDevice.devId, "beiang_status", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					AirInfo airInfo = CurrentDevice.instance().queryDevice.airInfo;
					if (airInfo != null) {
						mAirInfo = null;
						mAirInfo = airInfo;
						setData();
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}

		});
	}
	
	private void controlBeiAngAir(byte[] data) {
		setData();
		
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					CommandPair.RspCommand rsp = (CommandPair.RspCommand) rspData;
					if (rsp.reply != null) {
						AirInfo airInfo = EParse.parseEairByte(rsp.reply);
						mAirInfo = null;
						mAirInfo = airInfo;
						setData();
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
