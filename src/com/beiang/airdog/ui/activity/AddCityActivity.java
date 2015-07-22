package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.beiang.airdog.db.DB_Location;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.AddCityAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.utils.LocationUtils;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

public class AddCityActivity extends BaseMultiPartActivity {
	private TextView tv_location_city;
	private EditText et_input;
	private Button bt_add;
	private GridView city_glist;

	AddCityAdapter adapter;
	DB_Location db_Location;
	
	List<String> cityList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcity);
		setMenuEnable(false);

		db_Location = new DB_Location(mActivity);
		init();
		getData();
	}

	private void init() {
		tv_location_city = (TextView) findViewById(R.id.tv_location_city);
		et_input = (EditText) findViewById(R.id.et_input);
		bt_add = (Button) findViewById(R.id.bt_add);
		city_glist = (GridView) findViewById(R.id.city_glist);
		/*tv_location_city.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db_Location.saveSelectCity(tv_location_city.getText().toString());
				setResult(RESULT_OK);
				finish();
			}
		});*/

		city_glist.setAdapter(adapter = new AddCityAdapter());
		bt_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cityStr = et_input.getText().toString().replace("市", "");
				if (TextUtils.isEmpty(cityStr)) {
					Toast.show(mActivity, "请输入你需要添加的城市");
					return;
				}
				cityList = adapter.getData();
				if (cityList == null) {
					cityList = new ArrayList<String>();
					
					queryWeatherFromNet(cityStr);
				} else {
					boolean b = false;
					for (String string : cityList) {
						if (cityStr.contains(string)) {
							b = true;
							break;
						}
					}
					if (b) {
						Toast.show(mActivity, "你输入的城市已存在");
						return;
					}

					queryWeatherFromNet(cityStr);
				}
			}
		});
		/*city_glist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String string = (String) adapter.getItem(position);
				db_Location.saveSelectCity(string);
				setResult(RESULT_OK);
				finish();
			}
		});*/
	}

	private void getData() {
		String curCity = db_Location.getCurCity();
		if (TextUtils.isEmpty(curCity)) {
			MyLocationListener listener = new MyLocationListener();
			new LocationUtils().startLocation(mActivity, listener);
		} else {
			tv_location_city.setText(curCity);
		}

		List<String> cityList = db_Location.getCityArray();
		adapter.setData(cityList);
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			String cityName = location.getCity();
			if (!TextUtils.isEmpty(cityName)){
				cityName = cityName.replace("市", "");
				tv_location_city.setText(cityName);
			}
		}
	}
	
	private void queryWeatherFromNet(final String location) {
		BsOperationHub.instance().queryWeather(location, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					cityList.add(0, location);
					adapter.setData(cityList);
					db_Location.saveCityArray(cityList);
					et_input.setText("");
				} else {
					Toast.show(mActivity, "该地址不能正常获取天气");
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "该地址不能正常获取天气");
			}
		});
	}
}
