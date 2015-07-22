package com.beiang.airdog.ui.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.beiang.airdog.constant.Constants.Command;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.Function;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.ui.model.BusinessEntity;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.ui.model.PowerSocketEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.view.WeatherView;
import com.broadlink.beiangair.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DeviceTestActivity extends BaseMultiPartActivity implements OnClickListener {
	private DevEntity mDevice;
	private OPEntity opEntity;
	private PowerSocketEntity mEty;
	private WeatherView weather_layout;
	ImageView iv_power;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);


		initView();
		initData();
		setData();
		weather_layout.load();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryDataCloud();
	}

	private void initView() {
		weather_layout = (WeatherView) findViewById(R.id.weather_layout);

		iv_power = (ImageView) findViewById(R.id.iv_power);
		iv_power.setOnClickListener(this);
		iv_power.setTag(0);
	}
	
	/**初始化数据*/
	void initData() {
		mDevice = CurrentDevice.instance().curDevice;
		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_PowerSocket);
		
		mEty = (PowerSocketEntity) opEntity.getbEntity();
		if(mEty == null){
			mEty = new PowerSocketEntity();
		}
	}

	private void setData() {
		if (mEty != null) {
			// 设置按钮状态
			if ("0".equals(mEty.getState())) {
				// 关机状态下
				iv_power.setImageResource(R.drawable.ic_power_off_selector);
			} else {
				// 开机状态下
				iv_power.setImageResource(R.drawable.ic_power_on_selector);
			}
		}

	}

	private void queryDataCloud() {
		BsOperationHub.instance().queryDevStatus(mDevice.devId, "beiang_status", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					BusinessEntity bEty = null;
					DevEntity devEty = CurrentDevice.instance().queryDevice;
					try {
						
						HashMap<String, String> reportData = new Gson().fromJson(devEty.value,
								new TypeToken<HashMap<String, String>>() {}.getType());
						String bs64Report = reportData.get("report");
						String result = new String(Base64.decode(bs64Report, Base64.DEFAULT));
						bEty = new Gson().fromJson(result, PowerSocketEntity.class);
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(bEty != null){
						mEty = (PowerSocketEntity) bEty;
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

	/***
	 * 传输控制数据
	 * 
	 * @param data
	 */
	private void transportCloud(byte[] data) {
		setData();
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					CommandPair.RspCommand rsp = (CommandPair.RspCommand) rspData;
					if (rsp.reply != null) {
						OPEntity oEty = EParse.parseOPBytes(rsp.reply);
						if(oEty != null){
							BusinessEntity bEntity = oEty.getbEntity();
							if(bEntity != null && bEntity instanceof PowerSocketEntity){
								mEty = (PowerSocketEntity)bEntity;
								LogUtil.i("ok");
								setData();
							}
						}
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	/***
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mEty == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.iv_power:
			// 开关机时风速1档
			if ("1".equals(mEty.getState())) {
				mEty.setState("0");
			} else {
				mEty.setState("1");
			}
			opEntity.setbEntity(mEty);
			opEntity.setCommand(Command.WTITE);
			opEntity.setFunction(Function.FC0.getValue());
			transportCloud(EParse.parseparseOPEntity(opEntity));
			break;
		default:
			break;
		}
	}
}
