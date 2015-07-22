package com.beiang.airdog.utils;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.beiang.airdog.db.DB_Location;

public class LocationUtils {
	Context context;
	LocationClient mLocationClient;
	BDLocationListener ll;

	public void startLocation(Context context, BDLocationListener ll) {
		this.context = context;
		this.ll = ll;
		MyLocationListener locationListener = new MyLocationListener();
		mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);

		mLocationClient.registerLocationListener(locationListener);
		mLocationClient.start();
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			mLocationClient.stop();
			double lng = location.getLongitude();
			double lat = location.getLatitude();
			String cityName = location.getCity();
			String cityId = location.getCityCode();

			LogUtil.i("cityName = " + cityName);
			if (!TextUtils.isEmpty(cityName)){
				cityName = cityName.replace("市", "");
				new DB_Location(context).saveCurCity(cityName);
			}

			if (ll != null)
				ll.onReceiveLocation(location);
		}
	}

}
