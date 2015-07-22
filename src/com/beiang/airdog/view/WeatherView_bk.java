package com.beiang.airdog.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.beiang.airdog.db.DB_Location;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.QueryWeatherPair;
import com.beiang.airdog.net.business.homer.QueryWeatherPair.RspQueryWeather.Weatherinfo;
import com.beiang.airdog.net.business.homer.getDevIpPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.activity.AddCityActivity;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.WeatherEncodeUtil;
import com.broadlink.beiangair.R;

public class WeatherView_bk extends RelativeLayout implements OnClickListener {
	private boolean can = false;
	private DevEntity mDevice;
	private TextView tv_location, tv_time, tv_pm25, tv_level, tv_temper, tv_humidity;
	private int time = 0;

	public WeatherView_bk(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public WeatherView_bk(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_comm_air_view, this, true);

		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_pm25 = (TextView) findViewById(R.id.tv_pm25);
		tv_level = (TextView) findViewById(R.id.tv_level);
		tv_temper = (TextView) findViewById(R.id.tv_temper);
		tv_humidity = (TextView) findViewById(R.id.tv_humidity);

		tv_location.setOnClickListener(this);
		time = 0;
	}
	
	public void setCanChange(boolean can){
		this.can = can;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_location:
			if(!can){
				return;
			}
			((Activity) getContext()).startActivityForResult(new Intent(getContext(), AddCityActivity.class), 1000);
			break;

		default:
			break;
		}
	}

	public void load() {
		mDevice = CurrentDevice.instance().curDevice;
		if (!can && mDevice != null) {
			queryDevIp();
		} else {
			String citySelect = new DB_Location(getContext()).getSelectCity();
			if (!TextUtils.isEmpty(citySelect)){
				queryWeatherFromNet(citySelect);
				return;
			}
			String cityName = new DB_Location(getContext()).getCurCity();
			if (!TextUtils.isEmpty(cityName))
				queryWeatherFromNet(cityName);
		}
	}

	public void refrashWeather() {
		String cityName = new DB_Location(getContext()).getSelectCity();
		queryWeatherFromNet(cityName);
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
						LogUtil.i("devIp = "+ip);
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

	private void queryWeatherFromNet(String location) {
		BsOperationHub.instance().queryWeather(location, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					QueryWeatherPair.RspQueryWeather rsp = (QueryWeatherPair.RspQueryWeather) rspData;
					initWeatherLayout(rsp);
				} else {
					if (time > 10) {
						return;
					}
					String citySelect = new DB_Location(getContext()).getSelectCity();
					if (can && !TextUtils.isEmpty(citySelect)) {
						queryWeatherFromNet(citySelect);
					} else {
						String cityName = new DB_Location(getContext()).getCurCity();
						if (!TextUtils.isEmpty(cityName)) {
							queryWeatherFromNet(cityName);
						}
					}
					time++;
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				if (time > 10) {
					return;
				}
				String citySelect = new DB_Location(getContext()).getSelectCity();
				if (can && !TextUtils.isEmpty(citySelect)) {
					queryWeatherFromNet(citySelect);
				} else {
					String cityName = new DB_Location(getContext()).getCurCity();
					if (!TextUtils.isEmpty(cityName)) {
						queryWeatherFromNet(cityName);
					}
				}
				time++;
			}
		});
	}

	private void queryWeather(String ip) {
		String private_Key = "7944d9_SmartWeatherAPI_0dfa682";

		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put("areaid", "101010100");
		parmas.put("type", "index_v");

		Date currentTime = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
		String dateString = formatter.format(currentTime);
		parmas.put("date", dateString);

		parmas.put("appid", "a0fe32eddf9c601e");
		String key = WeatherEncodeUtil.standardURLEncoder(API.getParmas(parmas), private_Key);
		// String key = "Cg6h3yqhBS4Lo9%2BSzEZ3%2Bix8AZ0%3D";
		LogUtil.i("key = " + key);
		parmas.put("key", key);
		API.getWeather(getContext(), parmas, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				LogUtil.i("response = " + response);

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initWeatherLayout(QueryWeatherPair.RspQueryWeather weather) {
		if (weather.data != null) {
			Weatherinfo weatherinfo = weather.data.weatherinfo;
			tv_location.setText(weatherinfo.city.replace("市", ""));

			Calendar curCalendar = Calendar.getInstance();
			int curH = curCalendar.get(Calendar.HOUR_OF_DAY);
			int curMin = curCalendar.get(Calendar.MINUTE);
			String min = String.format("%02d", curMin);
			if (min.length() == 1) {
				min = "0" + min;
			}
			tv_time.setText("今天" + String.format("%02d", curH) + ":" + min + "发布");

			int pm25 = Integer.parseInt(weatherinfo.pm25);
			tv_pm25.setText(pm25+"");
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
