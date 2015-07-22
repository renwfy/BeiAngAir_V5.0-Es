package com.beiang.airdog.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.constant.AirConstant;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.view.WeatherView;
import com.broadlink.beiangair.R;

public class FreshAirActivity extends BaseMultiPartActivity implements OnClickListener {
	private WeatherView weather_layout;
	private ImageView fc_iv_power;
	private ImageView fc_iv_pm25_level;
	private TextView fc_tv_pm25_value;
	private TextView fc_tv_temp;
	private TextView fc_tv_co2;
	private TextView fc_tv_humidity;

	private boolean canRefrash;
	private boolean isActivity;
	private Handler mHandler;

	private DevEntity mDevice;
	private AirInfo mAirInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_freshair);

		setMenuEnable(false);

		init();
		initView();
		start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//加载一次数据
		queryMXData();

		startMxRefreshTimer();
	}

	/** 初始化数据 */
	void init() {
		isActivity = true;
		canRefrash = true;
		mDevice = CurrentDevice.instance().curDevice;
		mAirInfo = mDevice.airInfo;
		if (mAirInfo == null) {
			mAirInfo = new AirInfo();
		}

		mHandler = new Handler();
	}

	/** 初始化控件 */
	void initView() {
		weather_layout = (WeatherView) findViewById(R.id.weather_layout);

		(fc_iv_power = (ImageView) findViewById(R.id.fc_iv_power)).setOnClickListener(this);
		fc_iv_pm25_level = (ImageView) findViewById(R.id.fc_iv_pm25_level);
		fc_tv_pm25_value = (TextView) findViewById(R.id.fc_tv_pm25_value);
		fc_tv_temp = (TextView) findViewById(R.id.fc_tv_temp);
		fc_tv_co2 = (TextView) findViewById(R.id.fc_tv_co2);
		fc_tv_humidity = (TextView) findViewById(R.id.fc_tv_humidity);
	}

	/** 开始执行 */
	void start() {
		setData();
		weather_layout.load();
	}

	private void setData() {
		if (mAirInfo != null) {
			int airValue = mAirInfo.getAirValue();
			// pm2.5值 --
			int airPm25 = airValue;

			// 显示PM2.5数值
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				fc_tv_pm25_value.setText("--");
			} else {
				// 开机状态下
				fc_tv_pm25_value.setText(airPm25+"");
			}

			// 空气质量状态
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				fc_iv_pm25_level.setImageResource(R.drawable.ic_fc_pm25_level0);
			} else {
				// 开机状态下
				if (airPm25 < 50) {
					fc_iv_pm25_level.setImageResource(R.drawable.ic_fc_pm25_level1);
				}
				if (airPm25 >= 50 && airPm25 < 150) {
					fc_iv_pm25_level.setImageResource(R.drawable.ic_fc_pm25_level2);
				}
				if (airPm25 >= 150) {
					fc_iv_pm25_level.setImageResource(R.drawable.ic_fc_pm25_level3);
				}
			}

			// TVOC(这里暂时不需要)
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
			} else {
				// 开机状态下
			}

			// 温度
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				fc_tv_temp.setText("0");
			} else {
				// 开机状态下
				int temp = mAirInfo.getTem();
				fc_tv_temp.setText(temp + "");
			}

			// 湿度
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				fc_tv_humidity.setText("0");
			} else {
				// 开机状态下
				int hum = mAirInfo.getHum();
				fc_tv_humidity.setText(hum + "");
			}

			// CO2
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				fc_tv_co2.setText("0");
			} else {
				// 开机状态下
				int co2 = mAirInfo.getAirTvoc() * 256 + mAirInfo.getSignal();
				fc_tv_co2.setText(co2 + "");
			}

			// 设置按钮状态
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				fc_iv_power.setImageResource(R.drawable.ic_fc_power_off);
			} else {
				// 开机状态下
				fc_iv_power.setImageResource(R.drawable.ic_fc_power_on);
			}
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
		LogUtil.i("queryMXData()");
		BsOperationHub.instance().queryDevStatus(mDevice.devId, "beiang_status", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (canRefrash) {
					if (rspData.isSuccess()) {
						AirInfo airInfo = CurrentDevice.instance().queryDevice.airInfo;
						if (airInfo != null) {
							mAirInfo = airInfo;
							LogUtil.i("setData()");
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

	private void controlBeiAngAir(byte[] data) {
		//LogUtil.i(data);
		setData();
		if (!canRefrash) {
			return;
		}
		stopMxRefreshTimer();

		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					CommandPair.RspCommand rsp = (CommandPair.RspCommand) rspData;
					if (rsp.reply != null) {
						AirInfo airInfo = EParse.parseEairByte(rsp.reply);
						mAirInfo = airInfo;
						setData();
					}
				}
				canRefrash = true;
				startMxRefreshTimer();
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				canRefrash = true;
				startMxRefreshTimer();
			}
		});
	}

	/***
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fc_iv_power:
			if (mAirInfo.getOnoff() == AirConstant.ENABLE) {
				mAirInfo.setOnoff(AirConstant.UNABLE);
			} else {
				mAirInfo.setOnoff(AirConstant.ENABLE);
			}
			break;
		default:
			break;
		}
		controlBeiAngAir(EParse.parseEairInfo(mAirInfo));
	}

	@Override
	protected void onPause() {
		super.onPause();
		canRefrash = false;
		stopMxRefreshTimer();
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		isActivity = false;
		super.onDestroy();
	}

}
