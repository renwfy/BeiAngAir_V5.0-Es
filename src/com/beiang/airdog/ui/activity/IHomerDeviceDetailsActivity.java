package com.beiang.airdog.ui.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beiang.airdog.constant.AirConstant;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentHomer;
import com.beiang.airdog.net.business.ihomer.HomeCommandPair.RspHomeCommand;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.ui.model.BusinessEntity;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.Helper;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/***
 * 云家庭中查询、控制
 * @author LSD
 *
 */
public class IHomerDeviceDetailsActivity extends BaseMultiPartActivity implements OnClickListener {
	RspHomeGetDev.Data curHomerDevice;
	long curHomeId;
	long curDevId;

	Button bt_power, bt_auto,bt_sleep,bt_child,bt_seep,update;
	EditText name;

	TextView tv_details;
	OPEntity opEntity;
	RspHomeGetDev.Data curDev;

	AirInfo mAirInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idevice_details);
		setMenuEnable(false);

		initView();
		initData();
		setDataToView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadData();
	}

	private void initView() {
		tv_details = (TextView) findViewById(R.id.tv_details);
		(bt_power = (Button) findViewById(R.id.bt_power)).setOnClickListener(this);
		(bt_auto = (Button) findViewById(R.id.bt_auto)).setOnClickListener(this);
		(bt_sleep = (Button) findViewById(R.id.bt_sleep)).setOnClickListener(this);
		(bt_child = (Button) findViewById(R.id.bt_child)).setOnClickListener(this);
		(bt_seep = (Button) findViewById(R.id.bt_seep)).setOnClickListener(this);
		(update = (Button) findViewById(R.id.update)).setOnClickListener(this);

		name = (EditText) findViewById(R.id.name);
	}

	void initData() {
		curHomerDevice = CurrentDevice.instance().curHomerDevice;
		curHomeId = CurrentHomer.instance().curHomer.home_id;
		curDev = CurrentDevice.instance().curHomerDevice;
		curDevId = CurrentDevice.instance().curHomerDevice.device_id;

		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);

		if (mAirInfo == null) {
			mAirInfo = new AirInfo();
		}
	}

	void setDataToView() {
		name.setText(CurrentDevice.instance().curHomerDevice.name);
	}

	void loadData() {
		queryIhomerData();
	}

	/***
	 * 查询数据
	 */
	private void queryIhomerData() {
		LogUtil.i("queryMXData()");
		BsOperationHub.instance().homeGetDevice(curHomeId, curDevId, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspHomeGetDev rsp = (RspHomeGetDev) rspData;
					List<RspHomeGetDev.Data> datas = rsp.datas;
					if (datas != null && datas.size() > 0) {
						HashMap<String, String> value = datas.get(0).value;
						AirInfo airInfo = null;
						try {
							String report = value.get("report");
							String result = new String(Base64.decode(report, Base64.DEFAULT));
							HashMap<String, String> data = new Gson().fromJson(result, new TypeToken<HashMap<String, String>>() {
							}.getType());
							airInfo = EParse.parseEairByte(Helper.ConvertStringToHexBytesArray(data.get("purifier")));
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						if (airInfo != null) {
							mAirInfo = airInfo;
							setData();
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

	private void setData() {
		StringBuffer sBuffer = new StringBuffer();
		if (mAirInfo != null) {
			int airValue = mAirInfo.getAirValue();
			// pm2.5值 --
			int airPm = airValue;

			// TVOC
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				sBuffer.append("设备关机");
			} else {
				// 开机状态下
				sBuffer.append("设备开机\n");
				int tvoc = mAirInfo.getAirTvoc();
				sBuffer.append("TVOC : " + tvoc + "\n");

				// 空气质量状态
				int level = mAirInfo.getAirLevel();
				sBuffer.append("空气质量 : " + level + "\n");
				// pm2.5
				sBuffer.append("PM2.5 : " + airPm + "\n");

				// 设置按钮状态

				// 手自动状态
				if (mAirInfo.getIsAuto() == AirConstant.MODE.AUTO) {
					sBuffer.append("当前：手动\n");
				} else {
					sBuffer.append("当前：自动\n");
				}

				// 睡眠状态
				if (mAirInfo.getSleep() == AirConstant.SLEEP.ON) {
					sBuffer.append("睡眠：开\n");
				} else {
					sBuffer.append("睡眠：关\n");
				}

				// 童锁状态
				if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
					sBuffer.append("童锁：开\n");
				} else {
					sBuffer.append("童锁：关\n");
				}

				// 风速状态
				int speed = mAirInfo.getAirSpeed();
				sBuffer.append("风速 : " + speed + "\n");
			}
		}
		tv_details.setText(sBuffer.toString());

	}

	private void updateDev(final long home_id, final long dev_id, String name) {
		BsOperationHub.instance().homeUpdateDevice(home_id, dev_id, "", "devInfo", "prodect_id", name, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "更新成功");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "更新失败");
			}
		});
	}

	byte b = 0;

	/**
	 * 服务器传输数据
	 * 
	 * @param data
	 */
	private void transportCloud(byte[] data) {
		LogUtil.i(data);
		BsOperationHub.instance().homeSendCmd(curHomeId,curDevId, "", data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspHomeCommand rsp = (RspHomeCommand) rspData;
					String reply = rsp.data.reply;
					if (reply != null) {
						AirInfo airInfo = null;
						try {
							byte[] result = Base64.decode(reply, Base64.DEFAULT);
							airInfo = EParse.parseEairByte(result);
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (airInfo != null) {
							mAirInfo = airInfo;
							setData();
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

	public static class TESS extends BusinessEntity {
		byte[] data;

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_power:
			if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
				Toast.show(mActivity, getString(R.string.lock_hint));
				return;
			}
			// 开关机时风速1档
			mAirInfo.setAirSpeed(mAirInfo.getAirSpeed() == 0 ? 1 : mAirInfo.getAirSpeed());
			if (mAirInfo.getOnoff() == AirConstant.ENABLE) {
				mAirInfo.setOnoff(AirConstant.UNABLE);
			} else {
				mAirInfo.setOnoff(AirConstant.ENABLE);
			}
			transportCloud(EParse.parseEairInfo(mAirInfo));
			break;
		case R.id.bt_auto:
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				Toast.show(mActivity, getString(R.string.poweroff_hint));
			} else if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
				Toast.show(mActivity, getString(R.string.lock_hint));
			} else {
				if (mAirInfo.getIsAuto() == AirConstant.MODE.HAND) {
					mAirInfo.setIsAuto(AirConstant.MODE.AUTO);
				} else {
					mAirInfo.setIsAuto(AirConstant.MODE.HAND);
				}
				transportCloud(EParse.parseEairInfo(mAirInfo));
			}
			break;
		case R.id.bt_sleep:
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				Toast.show(mActivity, getString(R.string.poweroff_hint));
			} else if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
				Toast.show(mActivity, getString(R.string.lock_hint));
			} else {
				if (mAirInfo.getSleep() == AirConstant.SLEEP.ON) {
					mAirInfo.setSleep(AirConstant.SLEEP.OFF);
				} else {
					mAirInfo.setSleep(AirConstant.SLEEP.ON);
				}
				transportCloud(EParse.parseEairInfo(mAirInfo));
			}
			break;
		case R.id.bt_child:
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				Toast.show(mActivity, getString(R.string.poweroff_hint));
				return;
			}
			if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
				mAirInfo.setChildLock(AirConstant.CHILDLOCK.OFF);
			} else {
				mAirInfo.setChildLock(AirConstant.CHILDLOCK.ON);
			}
			transportCloud(EParse.parseEairInfo(mAirInfo));
			break;
		case R.id.bt_seep:
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				Toast.show(mActivity, getString(R.string.poweroff_hint));
			} else if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
				Toast.show(mActivity, getString(R.string.lock_hint));
			} else {
				int speed = mAirInfo.getAirSpeed();
				if (speed < AirConstant.WIND.FIVE) {
					speed++;
				} else {
					speed = AirConstant.WIND.LOW;
				}
				mAirInfo.setIsAuto(AirConstant.MODE.HAND);
				mAirInfo.setAirSpeed(speed);
				transportCloud(EParse.parseEairInfo(mAirInfo));
			} 
			break;
		case R.id.update:
			String nameStr = name.getText().toString();
			updateDev(curHomeId, curDevId, nameStr);
			break;
		default:
			break;
		}
	}
}
