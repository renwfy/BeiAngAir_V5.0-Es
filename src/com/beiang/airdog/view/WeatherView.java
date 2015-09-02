package com.beiang.airdog.view;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.db.DB_Location;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.QueryWeatherPair;
import com.beiang.airdog.net.business.homer.QueryWeatherPair.RspQueryWeather.Weatherinfo;
import com.beiang.airdog.net.business.homer.getDevIpPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.activity.AddCityActivity;
import com.beiang.airdog.utils.LocationUtils;
import com.beiang.airdog.utils.LogUtil;
import com.broadlink.beiangair.R;

public class WeatherView extends RelativeLayout implements View.OnClickListener {
	private TextView tv_location, tv_time, tv_pm25, tv_level, tv_temper,
			tv_humidity;
	private int time = 0;
	private boolean can;

	public WeatherView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public WeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_comm_air_view,
				this, true);

		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_location.setOnClickListener(this);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_pm25 = (TextView) findViewById(R.id.tv_pm25);
		tv_level = (TextView) findViewById(R.id.tv_level);
		tv_temper = (TextView) findViewById(R.id.tv_temper);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);

		time = 0;
	}

	public void setCanLoadMore(boolean can) {
		this.can = can;
	}

	public void load() {
		queryDevIpWeather();
	}

	public void load(String location) {
		if (!TextUtils.isEmpty(location))
			queryWeatherFromNet(location);
	}

	private void queryDevIpWeather() {
		DevEntity mDevice = CurrentDevice.instance().curDevice;
		if (mDevice == null) {
			getCurCityWeather();
			return;
		}
		BsOperationHub.instance().queryDevIp(mDevice.devId,
				new ReqCbk<RspMsgBase>() {
					@Override
					public void onSuccess(RspMsgBase rspData) {
						// TODO Auto-generated method stub
						if (rspData.isSuccess()) {
							getDevIpPair.RspGetDevIp rsp = (getDevIpPair.RspGetDevIp) rspData;
							if (rsp.data != null) {
								String ip = rsp.data.ip;
								LogUtil.i("devIp = " + ip);
								if (!TextUtils.isEmpty(ip)) {
									queryWeatherFromNet(ip);
									return;
								}
							}
						}
						getCurCityWeather();
					}

					@Override
					public void onFailure(ErrorObject err) {
						// TODO Auto-generated method stub
						getCurCityWeather();
					}
				});
	}

	private void getCurCityWeather() {
		String curCity = new DB_Location(BeiAngAirApplaction.getInstance())
				.getCurCity();
		if (TextUtils.isEmpty(curCity)) {
			new LocationUtils().startLocation(
					BeiAngAirApplaction.getInstance(),
					new BDLocationListener() {
						@Override
						public void onReceiveLocation(BDLocation arg0) {
							// TODO Auto-generated method stub
							String curCity = new DB_Location(
									BeiAngAirApplaction.getInstance())
									.getCurCity();
							if (!TextUtils.isEmpty(curCity)) {
								queryWeatherFromNet(curCity);
							}
						}
					});
			return;
		}
		queryWeatherFromNet(curCity);
	}

	private void queryWeatherFromNet(final String location) {
		BsOperationHub.instance().queryWeather(location,
				new ReqCbk<RspMsgBase>() {
					@Override
					public void onSuccess(RspMsgBase rspData) {
						// TODO Auto-generated method stub
						if (rspData.isSuccess()) {
							QueryWeatherPair.RspQueryWeather rsp = (QueryWeatherPair.RspQueryWeather) rspData;
							initWeatherLayout(rsp);
						} else {
							if (time > 2) {
								return;
							}
							queryWeatherFromNet(location);
							time++;
						}
					}

					@Override
					public void onFailure(ErrorObject err) {
						// TODO Auto-generated method stub
						if (time > 2) {
							return;
						}
						queryWeatherFromNet(location);
						time++;
					}
				});
	}

	private void initWeatherLayout(QueryWeatherPair.RspQueryWeather weather) {
		if (weather.data != null) {
			Weatherinfo weatherinfo = weather.data.weatherinfo;
			if (weatherinfo != null) {
				if (!TextUtils.isEmpty(weatherinfo.city)) {
					tv_location.setText(weatherinfo.city.replace("市", ""));
				}
				Calendar curCalendar = Calendar.getInstance();
				int curH = curCalendar.get(Calendar.HOUR_OF_DAY);
				int curMin = curCalendar.get(Calendar.MINUTE);
				String min = String.format("%02d", curMin);
				if (min.length() == 1) {
					min = "0" + min;
				}
				tv_time.setText("今天" + String.format("%02d", curH) + ":" + min
						+ "发布");

				int pm25 = 0;
				try {
					pm25 = Integer.parseInt(weatherinfo.pm25);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tv_pm25.setText(pm25 + "");
				if (pm25 < 35) {
					tv_level.setText("空气洁净");
					tv_level.setBackgroundResource(R.drawable.weather_layout_outstatus_level1);
				}
				if (pm25 >= 35 && pm25 < 75) {
					tv_level.setText("空气良好");
					tv_level.setBackgroundResource(R.drawable.weather_layout_outstatus_level2);
				}
				if (pm25 >= 75 && pm25 < 115) {
					tv_level.setText("轻度污染");
					tv_level.setBackgroundResource(R.drawable.weather_layout_outstatus_level3);
				}
				if (pm25 >= 115 && pm25 < 150) {
					tv_level.setText("中度污染");
					tv_level.setBackgroundResource(R.drawable.weather_layout_outstatus_level4);
				}
				if (pm25 >= 150 && pm25 < 250) {
					tv_level.setText("重度污染");
					tv_level.setBackgroundResource(R.drawable.weather_layout_outstatus_level5);
				}
				if (pm25 >= 250) {
					tv_level.setText("重度污染");
					tv_level.setBackgroundResource(R.drawable.weather_layout_outstatus_level5);
				}

				tv_temper.setText("温度" + weatherinfo.temp + "℃");
				tv_humidity.setText("湿度" + weatherinfo.humidity + "%");
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_location:
			if (!can) {
				return;
			}
			((Activity) getContext()).startActivityForResult(new Intent(
					getContext(), AddCityActivity.class), 1000);
			break;

		default:
			break;
		}
	}

}
