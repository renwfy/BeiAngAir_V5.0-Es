package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiang.airdog.constant.AirConstant;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.business.ihomer.HomeCommandPair.RspHomeCommand;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.Helper;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.Settings;
import com.beiang.airdog.view.WeatherView;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.dtr.zxing.activity.EncodeActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DeviceControlActivity extends BaseMultiPartActivity implements OnClickListener, OnItemClickListener {
	private DevEntity mDevice;
	private AirInfo mAirInfo;

	Handler mHandler;
	private boolean canRefrash;
	private boolean isActivity;

	private WeatherView weather_layout;
	private TextView tv_dev_nicename;

	RelativeLayout ll_ctrl_level1;
	ImageView iv_cr, iv_air_level, iv_air_pointer;

	RelativeLayout ll_ctrl_level2;
	ImageView iv_power, iv_speed, iv_lock, iv_auto, iv_sleep, iv_clean;
	LinearLayout ll_tvoc;
	ImageView ic_tvoc_level;

	RelativeLayout ll_ctrl_level3;
	TextView tv_pm25_inside;

	Animation roatAnim;
	AnimationDrawable animBatteryCharging;

	int ScreenW;
	int ViewW;
	int fromDegrees = 0;
	int toDegrees = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_control);

		initMenuData();
		setOnItemClickListener(this);

		initData();
		initView();
		setData();
		weather_layout.load();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		canRefrash = true;
	    //加载一次数据
		queryMXData();
	    ///定时刷新
		startMxRefreshTimer();
		
		/** 加载家庭数据 */
		// queryIhomerData();
	}

	/** 初始化数据 */
	void initData() {
		ScreenW = Settings.P_WIDTH;
		LogUtil.i("ScreenW = " + ScreenW);
		if (ScreenW <= 480) {
			ViewW = ScreenW - 40;
		} else if (ScreenW == 1536) {
			ViewW = ScreenW - 210;
		} else if (ScreenW == 1440) {
			ViewW = ScreenW - 150;
		} else {
			ViewW = ScreenW - 60;
		}

		isActivity = true;
		mHandler = new Handler();
		roatAnim = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_anim);

		mDevice = CurrentDevice.instance().curDevice;
		mAirInfo = mDevice.airInfo;
		if (mAirInfo == null) {
			mAirInfo = new AirInfo();
		}
	}

	private void initView() {
		weather_layout = (WeatherView) findViewById(R.id.weather_layout);

		tv_dev_nicename = (TextView) findViewById(R.id.tv_dev_nicename);
		tv_dev_nicename.setOnClickListener(this);
		if (ScreenW <= 480 || ScreenW == 1536) {
			tv_dev_nicename.setVisibility(View.INVISIBLE);
		} else {
			tv_dev_nicename.setVisibility(View.VISIBLE);
		}
		tv_dev_nicename.setVisibility(View.VISIBLE);

		ll_tvoc = (LinearLayout) findViewById(R.id.ll_tvoc);
		ic_tvoc_level = (ImageView) findViewById(R.id.ic_tvoc_level);

		ll_ctrl_level1 = (RelativeLayout) findViewById(R.id.ll_ctrl_level1);
		setMargin(ll_ctrl_level1, ll_ctrl_level1.getLayoutParams(), ViewW, ViewW, 0, 0, 0, 0);

		iv_cr = (ImageView) findViewById(R.id.iv_cr);
		iv_air_level = (ImageView) findViewById(R.id.iv_air_level);
		iv_air_pointer = (ImageView) findViewById(R.id.iv_air_pointer);

		ll_ctrl_level2 = (RelativeLayout) findViewById(R.id.ll_ctrl_level2);
		setMargin(ll_ctrl_level2, ll_ctrl_level2.getLayoutParams(), ViewW, ViewW, 0, 0, 0, 0);

		iv_power = (ImageView) findViewById(R.id.iv_power);
		iv_power.setOnClickListener(this);
		iv_power.setTag(0);

		iv_speed = (ImageView) findViewById(R.id.iv_speed);
		iv_speed.setOnClickListener(this);
		iv_speed.setTag(290);

		iv_lock = (ImageView) findViewById(R.id.iv_lock);
		iv_lock.setOnClickListener(this);
		iv_lock.setTag(130);

		iv_auto = (ImageView) findViewById(R.id.iv_auto);
		iv_auto.setOnClickListener(this);
		iv_auto.setTag(230);

		iv_sleep = (ImageView) findViewById(R.id.iv_sleep);
		iv_sleep.setOnClickListener(this);
		iv_sleep.setTag(180);

		iv_clean = (ImageView) findViewById(R.id.iv_clean);
		iv_clean.setOnClickListener(this);
		iv_clean.setTag(70);

		ll_ctrl_level3 = (RelativeLayout) findViewById(R.id.ll_ctrl_level3);
		setMargin(ll_ctrl_level3, ll_ctrl_level3.getLayoutParams(), ViewW, ViewW, 0, 0, 0, 0);

		tv_pm25_inside = (TextView) findViewById(R.id.tv_pm25_inside);
	}

	private void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
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

	public void setMargin(View view, LayoutParams param, int w, int h, int left, int top, int right, int bottom) {
		if (param instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) param;
			params.width = w;
			params.height = h;
			params.setMargins(left, top, right, bottom);
			view.setLayoutParams(params);
		}
		if (param instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) param;
			params.width = w;
			params.height = h;
			params.setMargins(left, top, right, bottom);
			view.setLayoutParams(params);
		}
	}

	private void setData() {
		tv_dev_nicename.setText(mDevice.nickName);
		if (mAirInfo != null) {
			int airValue = mAirInfo.getAirValue();
			// pm2.5值 --
			int airPm = airValue;
			if (Device.DT_280E == mDevice.devType) {
				airPm = (airValue / 30) + 8;
			}

			// 显示PM2.5数值
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				tv_pm25_inside.setText("--");
			} else {
				// 开机状态下
				tv_pm25_inside.setText(airPm + "");
				// 开关机的时候数值没回来/数值异常
				if ((airPm == 0 || airPm > 1000)) {
					tv_pm25_inside.setText("--");
				}
			}

			// TVOC
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				ic_tvoc_level.setImageResource(R.drawable.ic_tvoc_level_off);
			} else {
				// 开机状态下
				if (Device.DT_JY300 == mDevice.devType || Device.DT_JY500 == mDevice.devType) {
					ll_tvoc.setVisibility(View.VISIBLE);
					int tvoc = mAirInfo.getAirTvoc();
					switch (tvoc) {
					case 1:
						ic_tvoc_level.setImageResource(R.drawable.ic_tvoc_level_1);
						break;
					case 2:
						ic_tvoc_level.setImageResource(R.drawable.ic_tvoc_level_2);
						break;
					case 3:
						ic_tvoc_level.setImageResource(R.drawable.ic_tvoc_level_3);
						break;
					case 4:
						ic_tvoc_level.setImageResource(R.drawable.ic_tvoc_level_4);
						break;
					default:
						break;
					}
				} else {
					ll_tvoc.setVisibility(View.INVISIBLE);
				}
			}

			// 空气质量状态
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				iv_air_level.setImageResource(R.drawable.ic_dev_air_level1);
			} else {
				// 开机状态下
				if (Device.DT_280E == mDevice.devType) {
					if (airPm <= 50) {
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level1);
					} else if (airPm > 50 && airPm <= 100) {
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level2);
					} else if (airPm > 100 && airPm <= 150) {
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level3);
					} else if (airPm > 150 && airPm <= 200) {
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level4);
					} else if (airPm > 200 && airPm <= 300) {
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level4);
					} else {
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level4);
					}
				} else {
					int level = mAirInfo.getAirLevel();
					switch (level) {
					case 0:
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level1);
						break;
					case 1:
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level2);
						break;
					case 2:
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level3);
						break;
					case 3:
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level4);
						break;
					default:
						iv_air_level.setImageResource(R.drawable.ic_dev_air_level1);
						break;
					}
				}
			}

			// 设置按钮状态
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				// 关机状态下
				iv_power.setImageResource(R.drawable.ic_power_off_selector);
				iv_auto.setImageResource(R.drawable.ic_auto_off_selector);
				iv_sleep.setImageResource(R.drawable.ic_sleep_off_selector);
				iv_lock.setImageResource(R.drawable.ic_lock_off_selector);
				if (Device.DT_JY500 == mDevice.devType) {
					iv_speed.setImageResource(R.drawable.ic_speed_mode2_off);
				} else {
					iv_speed.setImageResource(R.drawable.ic_speed_mode1_off);
				}
				iv_clean.setImageResource(R.drawable.ic_clean_off);
			} else {
				// 开机状态下
				iv_power.setImageResource(R.drawable.ic_power_on_selector);

				// 手自动状态
				if (mAirInfo.getIsAuto() == AirConstant.MODE.AUTO) {
					iv_auto.setImageResource(R.drawable.ic_auto_on_selector);
				} else {
					iv_auto.setImageResource(R.drawable.ic_auto_off_selector);
				}

				// 睡眠状态
				if (mAirInfo.getSleep() == AirConstant.SLEEP.ON) {
					iv_sleep.setImageResource(R.drawable.ic_sleep_on_selector);
				} else {
					iv_sleep.setImageResource(R.drawable.ic_sleep_off_selector);
				}

				// 童锁状态
				if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
					iv_lock.setImageResource(R.drawable.ic_lock_on_selector);
				} else {
					iv_lock.setImageResource(R.drawable.ic_lock_off_selector);
				}

				// 风速状态
				int speed = mAirInfo.getAirSpeed();
				switch (speed) {
				case AirConstant.WIND.LOW:
					if (Device.DT_JY500 == mDevice.devType) {
						iv_speed.setImageResource(R.drawable.ic_speed_mode2_1_on);
					} else {
						iv_speed.setImageResource(R.drawable.ic_speed_mode1_1_on);
					}
					break;
				case AirConstant.WIND.MID:
					if (Device.DT_JY500 == mDevice.devType) {
						iv_speed.setImageResource(R.drawable.ic_speed_mode2_2_on);
					} else {
						iv_speed.setImageResource(R.drawable.ic_speed_mode1_2_on);
					}
					break;
				case AirConstant.WIND.HIGH:
					if (Device.DT_JY500 == mDevice.devType) {
						iv_speed.setImageResource(R.drawable.ic_speed_mode2_3_on);
					} else {
						iv_speed.setImageResource(R.drawable.ic_speed_mode1_3_on);
					}
					break;
				case AirConstant.WIND.FOUR:
					iv_speed.setImageResource(R.drawable.ic_speed_mode2_4_on);
					break;
				case AirConstant.WIND.FIVE:
					iv_speed.setImageResource(R.drawable.ic_speed_mode2_5_on);
					break;
				}

				// 维护状态
				int serverEro = mAirInfo.getErr();
				if (1 == serverEro) {
					iv_clean.setImageResource(R.drawable.ic_clean_on);
				} else {
					iv_clean.setImageResource(R.drawable.ic_clean_off);
				}
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
							//老设备解析
							mAirInfo = airInfo;
							setData();
						}else{
							//新设备解析
							DevEntity entity = CurrentDevice.instance().queryDevice;
							try {
								HashMap<String, String> reportData = new Gson().fromJson(entity.value,
										new TypeToken<HashMap<String, String>>() {}.getType());
								String report = reportData.get("report");
								HashMap<String, String> data = new Gson().fromJson(new String(Base64.decode(report, Base64.DEFAULT)), new TypeToken<HashMap<String, String>>() {
								}.getType());
								airInfo = EParse.parseEairByte(Helper.ConvertStringToHexBytesArray(data.get("purifier")));
								if (airInfo != null) {
									mAirInfo = airInfo;
									setData();
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
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

	/***
	 * 家里面的设备获取数据
	 * 
	 */
	private void queryIhomerData() {
		LogUtil.i("queryMXData()");
		BsOperationHub.instance().homeGetDevice(4, Long.parseLong(mDevice.devId), new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (canRefrash) {
					if (rspData.isSuccess()) {
						RspHomeGetDev rsp = (RspHomeGetDev) rspData;
						List<RspHomeGetDev.Data> datas = rsp.datas;
						if (datas != null && datas.size() > 0) {
							HashMap<String, String> value = datas.get(0).value;
							AirInfo airInfo = null;
							try {
								String report = value.get("report");
								String result = new String(Base64.decode(report, Base64.DEFAULT));
								LogUtil.i(result);
								HashMap<String, String> data = new Gson().fromJson(result, new TypeToken<HashMap<String, String>>() {
								}.getType());
								LogUtil.i(data.get("purifier"));
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
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				if (canRefrash) {
				}
			}

		});
	}

	class TE {
		String purifier;
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
						LogUtil.i(rsp.reply);
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

	/**
	 * 服务器传输数据
	 * 
	 * @param data
	 */
	private void transportCloud(byte[] data) {
		LogUtil.i(data);
		BsOperationHub.instance().homeSendCmd(4, Long.parseLong(mDevice.devId), "", data, new ReqCbk<RspMsgBase>() {
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		MenuEntity ety = (MenuEntity) parent.getAdapter().getItem(position);
		if ("Auth".equals(ety.getMenu_key())) {
			if (!"owner".equals(mDevice.role)) {
				Toast.show(mActivity, "您不能把该设备授权给别人");
				return;
			}
			qDecode();
		}
	}

	/***
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (mAirInfo == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.iv_power:
			toDegrees = (Integer) v.getTag();
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
			optare();
			break;
		case R.id.iv_auto:
			toDegrees = (Integer) v.getTag();
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
				optare();
			}
			break;
		case R.id.iv_sleep:
			toDegrees = (Integer) v.getTag();
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
				optare();
			}
			break;
		case R.id.iv_lock:
			toDegrees = (Integer) v.getTag();
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				Toast.show(mActivity, getString(R.string.poweroff_hint));
				return;
			}
			if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
				mAirInfo.setChildLock(AirConstant.CHILDLOCK.OFF);
			} else {
				mAirInfo.setChildLock(AirConstant.CHILDLOCK.ON);
			}
			optare();
			break;
		case R.id.iv_speed:
			toDegrees = (Integer) v.getTag();
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				Toast.show(mActivity, getString(R.string.poweroff_hint));
			} else if (mAirInfo.getChildLock() == AirConstant.CHILDLOCK.ON) {
				Toast.show(mActivity, getString(R.string.lock_hint));
			} else {
				int speed = mAirInfo.getAirSpeed();
				if (Device.DT_JY500 == mDevice.devType) {
					if (speed < AirConstant.WIND.FIVE) {
						speed++;
					} else {
						speed = AirConstant.WIND.LOW;
					}
				} else {
					if (speed < AirConstant.WIND.HIGH) {
						speed++;
					} else {
						speed = AirConstant.WIND.LOW;
					}
				}
				mAirInfo.setIsAuto(AirConstant.MODE.HAND);
				mAirInfo.setAirSpeed(speed);
				optare();
			}
			break;
		case R.id.iv_clean:
			// 清洁复位
			toDegrees = (Integer) v.getTag();
			if (mAirInfo.getOnoff() == AirConstant.UNABLE) {
				Toast.show(mActivity, getString(R.string.poweroff_hint));
			} else {
				if (1 == mAirInfo.getErr()) {
					// 需要清洁复位
					mAirInfo.setClean(245);
				}
				if (2 == mAirInfo.getErr()) {
					// 需要清洗电极
				}
				optare();
			}
			break;
		default:
			break;
		}
	}

	private void optare() {
		controlBeiAngAir(EParse.parseEairInfo(mAirInfo));

		/** 云家庭的方式 */
		// transportCloud(EParse.parseEairInfo(mAirInfo));

		RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(300);
		rotateAnimation.setFillAfter(true);
		iv_air_pointer.startAnimation(rotateAnimation);
		fromDegrees = toDegrees;
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
			tv_dev_nicename.setText(mDevice.nickName);
		}
	}

}
