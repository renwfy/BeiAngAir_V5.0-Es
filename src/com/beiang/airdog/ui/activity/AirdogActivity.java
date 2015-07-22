package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.beiang.airdog.constant.Constants.AirdogFC;
import com.beiang.airdog.constant.Constants.Command;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.Function;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.business.homer.QueryDevDataPair;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.ui.model.CommEntity;
import com.beiang.airdog.ui.model.FirmwareEntity;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.XmlUtils;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.WeatherView;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.dtr.zxing.activity.EncodeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AirdogActivity extends BaseMultiPartActivity implements OnClickListener, OnItemClickListener {
	private DevEntity mDevice;
	private AirInfo mAirInfo;

	Handler mHandler;
	private boolean canRefrash;
	private boolean isActivity;

	private WeatherView weather_layout;

	TextView tv_inside_temper_value, tv_inside_humidity_value, tv_inside_time, tv_inside_date, tv_dev_nickname;
	TextView tv_pm25_11;
	ImageView iv_cr, battery;

	Animation roatAnim;
	AnimationDrawable animBatteryCharging;
	
	OPEntity opEntity;
	String curMusicVer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airdog);

		initMenuData();
		setOnItemClickListener(this);

		initView();
		initData();
		setData();
		weather_layout.load();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		iv_cr.clearAnimation();
		iv_cr.setAnimation(roatAnim);

		canRefrash = true;
		queryMXData();
		/** 加载云家庭数据 */
		// queryIhomerData();
		devMusic();
	}

	private void initView() {
		weather_layout = (WeatherView) findViewById(R.id.weather_layout);

		tv_inside_temper_value = (TextView) findViewById(R.id.tv_inside_temper_value);
		tv_inside_humidity_value = (TextView) findViewById(R.id.tv_inside_humidity_value);
		tv_inside_time = (TextView) findViewById(R.id.tv_inside_time);
		tv_inside_date = (TextView) findViewById(R.id.tv_inside_date);
		tv_dev_nickname = (TextView) findViewById(R.id.tv_dev_nickname);
		tv_pm25_11 = (TextView) findViewById(R.id.tv_pm25_11);

		iv_cr = (ImageView) findViewById(R.id.iv_cr);
		battery = (ImageView) findViewById(R.id.battery_charging);
		battery.setOnClickListener(this);
	}
	
	void initData(){
		isActivity = true;
		mDevice = CurrentDevice.instance().curDevice;
		mAirInfo = mDevice.airInfo;
		if(mAirInfo == null){
			mAirInfo = new AirInfo();
		}
		mHandler = new Handler();
		roatAnim = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_anim);
		
		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);
	}

	private void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("clock");
		menu.setMenu_name("闹钟");
		menu.setMenu_icon(R.drawable.ic_menu_clock);
		menus.add(menu);

		/*menu = new MenuEntity();
		menu.setMenu_key("record");
		menu.setMenu_name("语音");
		menu.setMenu_icon(R.drawable.ic_menu_record);
		menus.add(menu);*/

		menu = new MenuEntity();
		menu.setMenu_key("volume");
		menu.setMenu_name("音量");
		menu.setMenu_icon(R.drawable.ic_menu_volume);
		menus.add(menu);

		/*menu = new MenuEntity();
		menu.setMenu_key("control");
		menu.setMenu_name("空调控制");
		menu.setMenu_icon(R.drawable.ic_menu_share);
		menus.add(menu);*/
		
		/*menu = new MenuEntity();
		menu.setMenu_key("musicVer");
		menu.setMenu_name("音乐版本");
		menu.setMenu_icon(0);
		menus.add(menu);*/
		
		menu = new MenuEntity();
		menu.setMenu_key("yelp");
		menu.setMenu_name("狗吠");
		menu.setMenu_icon(0);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("edit");
		menu.setMenu_name("自定义");
		menu.setMenu_icon(R.drawable.ic_menu_edit);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("Auth");
		menu.setMenu_name("授权");
		menu.setMenu_icon(R.drawable.ic_menu_auth);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("share");
		menu.setMenu_name("分享");
		menu.setMenu_icon(R.drawable.ic_menu_share);
		menus.add(menu);
		prepareOptionsMenu(menus);
	}

	private void setData() {
		tv_dev_nickname.setText(mDevice.nickName);

		if (mAirInfo != null) {
			// pm25
			tv_pm25_11.setText(mAirInfo.getAirValue() + "");

			int temp = mAirInfo.getTem();
			if (temp > 128) {
				temp = temp - 256;
			}
			tv_inside_temper_value.setText(temp + "");
			tv_inside_humidity_value.setText(mAirInfo.getHum() + "");

			int dian = mAirInfo.getAirSpeed();
			if (dian >= 128) {
				battery.setBackgroundResource(R.anim.battery_charging_anim);
				animBatteryCharging = (AnimationDrawable) battery.getBackground();
				animBatteryCharging.stop();
				animBatteryCharging.start();
			} else {
				if (animBatteryCharging != null) {
					animBatteryCharging.stop();
				}
				battery.setBackgroundColor(Color.parseColor("#00000000"));

				if (dian >= 85) {
					battery.setImageResource(R.drawable.ic_battery_4);
				}
				if (25 <= dian && dian < 85) {
					battery.setImageResource(R.drawable.ic_battery_3);
				}
				if (25 <= dian && dian < 55) {
					battery.setImageResource(R.drawable.ic_battery_2);
				}
				if (5 <= dian && dian < 25) {
					battery.setImageResource(R.drawable.ic_battery_1);
				}
				if (dian < 5) {
					battery.setImageResource(R.drawable.ic_battery_0);
				}
			}
		}

		Calendar curCalendar = Calendar.getInstance();
		int curY = curCalendar.get(Calendar.YEAR);
		int curM = curCalendar.get(Calendar.MONTH) + 1;
		int curD = curCalendar.get(Calendar.DAY_OF_MONTH);
		tv_inside_date.setText(curY + "/" + String.format("%02d", curM) + "/" + String.format("%02d", curD));

		int curH = curCalendar.get(Calendar.HOUR_OF_DAY);
		int curMin = curCalendar.get(Calendar.MINUTE);
		tv_inside_time.setText(String.format("%02d", curH) + ":" + String.format("%02d", curMin));
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
		//beiang_status
		BsOperationHub.instance().queryDevStatus(mDevice.devId, "beiang_status", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (canRefrash) {
					if (rspData.isSuccess()) {
						AirInfo airInfo = CurrentDevice.instance().queryDevice.airInfo;
						if (airInfo != null) {
							//老设备解析方式
							mAirInfo = airInfo;
							setData();
						}else{
							//新的解析方式
							DevEntity entity = CurrentDevice.instance().queryDevice;
							try {
								HashMap<String, String> reportData = new Gson().fromJson(entity.value,
										new TypeToken<HashMap<String, String>>() {}.getType());
								String report = reportData.get("report");
								HashMap<String, String> data = new Gson().fromJson(new String(Base64.decode(report, Base64.DEFAULT)), new TypeToken<HashMap<String, String>>() {
								}.getType());
								
								airInfo = new AirInfo();
								airInfo.setAirValue(Integer.parseInt(data.get("pm25")));
								airInfo.setTem(Integer.parseInt(data.get("temp")));
								airInfo.setHum(Integer.parseInt(data.get("humidity")));
								airInfo.setSignal(Integer.parseInt(data.get("signal")));
								airInfo.setAirSpeed(Integer.parseInt(data.get("battery")));
								
								mAirInfo = airInfo;
								setData();
							} catch (Exception e) {
								// TODO: handle exception
							}
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

	private void queryIhomerData() {
		LogUtil.i("queryMXData()");
		BsOperationHub.instance().homeGetDevice(4, Long.parseLong(mDevice.devId), new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (canRefrash) {
					if (rspData.isSuccess()) {
						// LogUtil.i(rspData)
						RspHomeGetDev rsp = (RspHomeGetDev) rspData;
						List<RspHomeGetDev.Data> datas = rsp.datas;
						if (datas != null && datas.size() > 0) {
							HashMap<String, String> value = datas.get(0).value;
							String report = value.get("report");
							byte[] result = Base64.decode(report, Base64.DEFAULT);
						}
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				if (canRefrash) {
				}
			}

		});
	}

	private void transportCloud(byte[] data) {
		LogUtil.i(data);
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "通知硬件更新成功");
					return;
				}
				Toast.show(mActivity, "通知硬件更新失败");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "通知硬件更新失败");
			}
		});
	}
	
	private void transportCloud_V2(byte[] data) {
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	private void devMusic() {
		// TODO Auto-generated method stub
		// 主要使用 mDevice.devId
		BsOperationHub.instance().queryDevFirmware(mDevice, "music", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					QueryDevDataPair.RspQueryDevData rsp = (QueryDevDataPair.RspQueryDevData) rspData;
					if (rsp.data != null) {
						curMusicVer = rsp.data.value;
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void getMusicNewVerson() {
		showDialog("正在获取新版本");
		API.getFirmwareNewVerson(mActivity, mDevice.devType, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				hideDialog();
				List<FirmwareEntity> list = XmlUtils.parse(response);
				if (list == null || list.size() == 0) {
					Toast.show(mActivity, "没有找到新版本");
					return;
				}

				for (final FirmwareEntity entity : list) {
					if (entity.type == 2) {
						// 音乐
						String newVer = entity.ver;
						int b = 0;
						try {
							b = newVer.compareTo(curMusicVer);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (b > 0) {
							AlertDialog.show(mActivity, 0, "检测到新版本,是否更新 ？", true, new AlertDialogCallBack() {
								@Override
								public void onLeft() {
									// TODO Auto-generated method stub
								}

								@Override
								public void onRight() {
									// TODO Auto-generated method stub
									noticeMusicUpdate(entity);
								}
							});
						} else {
							Toast.show(mActivity, "没有找到新版本");
						}
						return;
					}
				}
				Toast.show(mActivity, "没有找到新版本");
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				hideDialog();
				Toast.show(mActivity, "没有找到新版本");
			}
		});
	}
	private void noticeMusicUpdate(FirmwareEntity ware) {
		OPEntity opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);
		opEntity.setbEntity(ware);

		opEntity.setCommand(Command.WTITE);
		opEntity.setFunction(Function.FIRMWARE.getValue());
		transportCloud(EParse.parseparseOPEntity(opEntity));
	}

	private void qDecode() {
		String codeStr = "";
		if (mDevice != null) {
			codeStr = API.AppAuthUrl + "id=" + mDevice.devId + "&auth=" + 0;
			launchSearch(codeStr);
		}
	}

	/***
	 * 生成二维码
	 * 
	 * @param text
	 */
	private void launchSearch(String text) {
		Intent intent = new Intent();
		intent.setClass(mActivity, EncodeActivity.class);
		intent.putExtra(EncodeActivity.CONTENT, text);
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		canRefrash = false;
		stopMxRefreshTimer();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		MenuEntity ety = (MenuEntity) parent.getAdapter().getItem(position);
		if ("Auth".equals(ety.getMenu_key())) {
			if (!"owner".equals(mDevice.role)) {
				Toast.show(mActivity, "不能获取此种权限");
				return;
			}
			qDecode();
		}
		if ("cycle".equals(ety.getMenu_key())) {
			//小狗自己控制（不要了）
		}
		if ("yelp".equals(ety.getMenu_key())) {
			//通知小狗叫
			CommEntity entity = new CommEntity();
			entity.value = 1;
			opEntity.setbEntity(entity);
			opEntity.setCommand(Command.WTITE);
			opEntity.setFunction(AirdogFC.YELP.getValue());
			transportCloud_V2(EParse.parseparseOPEntity(opEntity));
		}
		if("musicVer".equals(ety.getMenu_key())){
			getMusicNewVerson();
		}
	}

	/***
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.battery_charging:
			FirmwareEntity ware = new FirmwareEntity();
			ware.type = FirmwareEntity.Type.firmware;
			ware.url ="http://121.207.243.132/v1/fs/firmware/20020/v2.0.1/airdog_2.0.1.bin";
			opEntity.setbEntity(ware);

			opEntity.setCommand(Command.WTITE);
			opEntity.setFunction(Function.FIRMWARE.getValue());
			transportCloud_V2(EParse.parseparseOPEntity(opEntity));
			break;

		default:
			break;
		}
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
		if (requestCode == 200) {
			tv_dev_nickname.setText(mDevice.nickName);
		}
	}

}
