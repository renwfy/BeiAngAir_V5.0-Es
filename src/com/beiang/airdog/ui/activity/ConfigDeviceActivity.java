package com.beiang.airdog.ui.activity;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiang.airdog.db.DB_User;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.BusinessErr;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.utils.FunctionUtil;
import com.beiang.airdog.utils.Helper;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.elink.FirstTimeConfig2;
import com.beiang.airdog.utils.elink.FirstTimeConfigListener;
import com.beiang.airdog.utils.elink.utils.EasyLinkUtils;
import com.beiang.airdog.utils.elink.utils.EasyLinkWifiManager;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.dtr.zxing.activity.CaptureActivity;

public class ConfigDeviceActivity extends BaseMultiPartActivity implements OnClickListener, FirstTimeConfigListener {
	private EditText et_ssid;
	private EditText et_pass;
	private TextView tv_showpass;
	private TextView tv_remember;
	private TextView tv_config;
	private LinearLayout scan;

	LinearLayout ll_content;
	RelativeLayout ll_animation;
	LinearLayout ll_configing;
	LinearLayout ll_config_ok;
	ImageView iv_configing;
	ImageView iv_config_ok;
	ImageView top_wifi;

	AnimationDrawable animConfiging;
	AnimationDrawable animConfigOk;
	AnimationDrawable animWifi;

	boolean mInConfig = false;
	Handler handler;

	private EasyLinkWifiManager mWifiManager = null;
	public static boolean sIsNetworkAlertVisible = false;
	public boolean isNetworkConnecting = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cofigdev);

		setMenuEnable(false);
		handler = new Handler();

		initView();
		loadAnimation();

		EasyLinkUtils.setProtraitOrientationEnabled(ConfigDeviceActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkWifi();
		animWifi.stop();
		animWifi.start();
	}

	private void initView() {
		et_ssid = (EditText) findViewById(R.id.et_ssid);
		et_pass = (EditText) findViewById(R.id.et_pass);
		tv_showpass = (TextView) findViewById(R.id.tv_showpass);
		tv_remember = (TextView) findViewById(R.id.tv_remember);
		tv_config = (TextView) findViewById(R.id.tv_config);

		/** 配置动画层 */
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ll_animation = (RelativeLayout) findViewById(R.id.ll_animation);
		ll_configing = (LinearLayout) findViewById(R.id.ll_configing);
		ll_config_ok = (LinearLayout) findViewById(R.id.ll_config_ok);
		iv_configing = (ImageView) findViewById(R.id.iv_configing);
		iv_config_ok = (ImageView) findViewById(R.id.iv_config_ok);
		top_wifi = (ImageView) findViewById(R.id.top_wifi);
		scan = (LinearLayout) findViewById(R.id.scan);

		tv_showpass.setSelected(false);
		tv_remember.setSelected(true);

		tv_showpass.setOnClickListener(this);
		tv_remember.setOnClickListener(this);
		tv_config.setOnClickListener(this);
		top_wifi.setOnClickListener(this);
		scan.setOnClickListener(this);
	}

	private void loadAnimation() {
		iv_configing.setBackgroundResource(R.anim.configing_anim);
		animConfiging = (AnimationDrawable) iv_configing.getBackground();

		iv_config_ok.setBackgroundResource(R.anim.config_ok_anim);
		animConfigOk = (AnimationDrawable) iv_config_ok.getBackground();

		top_wifi.setBackgroundResource(R.anim.config_wifi_anim);
		animWifi = (AnimationDrawable) top_wifi.getBackground();
	}

	private void startConfig() {
		mInConfig = true;
		ll_content.setVisibility(View.GONE);
		ll_animation.setVisibility(View.VISIBLE);
		ll_configing.setVisibility(View.VISIBLE);
		ll_config_ok.setVisibility(View.GONE);
		scan.setVisibility(View.INVISIBLE);

		animConfigOk.stop();
		animConfiging.stop();
		animConfiging.start();
		
		//随机绑定码
		int  bindCode = calculateBindCode();
		
		try {
			sendPacketData2(bindCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 从服务器获取设备
		searchMxDeviceFromServer(bindCode);
	}

	private void configOk() {
		ll_configing.setVisibility(View.GONE);
		ll_config_ok.setVisibility(View.VISIBLE);

		animConfiging.stop();
		animConfigOk.stop();
		animConfigOk.start();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				stopConfig();
				setResult(RESULT_OK);
				finish();
			}
		}, 1500);
	}
	
	private void bindedOk(){
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setResult(RESULT_OK);
				finish();
			}
		}, 200);
	}

	private void stopConfig() {
		mInConfig = false;
		stopPacketData2();
		BsOperationHub.instance().stopQuery();

		animConfiging.stop();
		animConfigOk.stop();
		ll_content.setVisibility(View.VISIBLE);
		ll_animation.setVisibility(View.GONE);
		scan.setVisibility(View.VISIBLE);
	}

	private void checkWifi() {
		// TODO Auto-generated method stub
		if (getWiFiManagerInstance().isWifiConnected()) {
			String ssid = getWiFiManagerInstance().getCurrentSSID();
			et_ssid.setText(ssid);
			String pass = new DB_User(mActivity).getWifiPass(ssid);
			et_pass.setText(pass);
		} else {
			// wifi未连接
		}
	}

	/***
	 * 从服务器获取设备
	 */
	private void searchMxDeviceFromServer(final int bindCode) {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				BsOperationHub.instance().queryBindDev(CurrentUser.instance().getUserId(), bindCode+"", new ReqCbk<RspMsgBase>() {
					@Override
					public void onSuccess(RspMsgBase rspData) {
						// TODO Auto-generated method stub
						if (mInConfig) {
							if (rspData.errorCode == BusinessErr.DEVICE_BINDED_OTHER) {
								stopConfig();
								AlertDialog.show(mActivity, 0, "该设备已被其他用户绑定啦 !", false, null);
								return;
							}
							if (rspData.errorCode == BusinessErr.DEVICE_BINDED) {
								stopConfig();
								AlertDialog.show(mActivity, 0, "你已经绑定过该设备啦 !", true, new AlertDialogCallBack() {
									@Override
									public void onRight() {
										// TODO Auto-generated method stub
										bindedOk();
									}
									
									@Override
									public void onLeft() {
										// TODO Auto-generated method stub
										bindedOk();
									}
								});
								return;
							}
							refrashDevice();
						}
					}

					@Override
					public void onFailure(ErrorObject err) {
						// TODO Auto-generated method stub
						if (mInConfig) {
							stopConfig();
							Toast.show(mActivity, "配置超时");
						}
					}
				});
			}
		}, 5000);
	}

	/** 配置后刷新列表 */
	private void refrashDevice() {
		BsOperationHub.instance().queryBindList(CurrentUser.instance().getUserId(), new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					configOk();
				} else {
					stopConfig();
					Toast.show(mActivity, "没有新的绑定设备");
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				stopConfig();
				Toast.show(mActivity, "没有新的绑定设备");
			}
		});
	}

	/** 从绑定设备中获取 */
	private void queryBindedDevice(boolean b) {
		BsOperationHub.instance().queryBindList(CurrentUser.instance().getUserId(), new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if(rspData.isSuccess()){
					Toast.show(mActivity, "添加设备成功!");
					bindedOk();
				}else{
					Toast.show(mActivity, "添加设备失败!");
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				err.getErrorString();
			}
		});
	}

	private void otherBindDevWay(String string) {
		int index = string.indexOf("?");
		String pamaStr = string.substring(index + 1, string.length());

		int idIndex = pamaStr.indexOf("id=");
		String devId = "";
		if(pamaStr.contains("&")){
			int andIndex = pamaStr.indexOf("&");
			devId = pamaStr.substring(idIndex + 3, andIndex);
		}else{
			devId = pamaStr.substring(idIndex + 3, pamaStr.length());
		}
		String auth = "";
		if(pamaStr.contains("auth")){
			int authIndex = pamaStr.indexOf("auth=");
			auth = pamaStr.substring(authIndex+5, pamaStr.length());

		}
		
		if(!FunctionUtil.checkId(devId)){
			Toast.show(mActivity, "无效的二维码");
			return;
		}
		
		List<DevEntity> deviceList = CurrentDevice.instance().devList;
		boolean hasDev = false;
		if (deviceList != null && deviceList.size() > 0) {
			for (DevEntity ety : deviceList) {
				if (devId.equals(ety.devId)) {
					hasDev = true;
					break;
				}
			}
		}
		if (hasDev) {
			Toast.show(mActivity, "设备已存在");
			return;
		}

		DevEntity entity = new DevEntity();
		entity.devId = devId;
		entity.deviceSn = "";
		entity.devInfo = "贝昂";
		if ("1".equals(auth)) {
			BsOperationHub.instance().bindDevice(entity, new ReqCbk<RspMsgBase>() {
				@Override
				public void onSuccess(RspMsgBase rspData) {
					// TODO Auto-generated method stub
					if (rspData.isSuccess()) {
						queryBindedDevice(true);
					} else {
						Toast.show(mActivity, "授权失败");
					}
				}

				@Override
				public void onFailure(com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject err) {
					// TODO Auto-generated method stub
					Toast.show(mActivity, "授权失败");
				}
			});
		} else {
			BsOperationHub.instance().Authrorize(entity, new ReqCbk<RspMsgBase>() {
				@Override
				public void onSuccess(RspMsgBase rspData) {
					// TODO Auto-generated method stub
					if (rspData.isSuccess()) {
						queryBindedDevice(true);
					} else {
						Toast.show(mActivity, "授权失败");
					}
				}

				@Override
				public void onFailure(com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject err) {
					// TODO Auto-generated method stub
					Toast.show(mActivity, "授权失败");
				}
			});
		}
	}

	// -------------------------- 配置相关 -------------------
	/**计算随机绑定码*/
	private int calculateBindCode() {
		Random rand = new Random();
		return rand.nextInt(1) * 1000;
	}

	
	@Override
	public void onFirstTimeConfigEvent(FtcEvent arg0, Exception arg1) {
		//
	}

	private FirstTimeConfig2 firstConfig2 = null;
	public boolean isCalled2 = false;

	private void sendPacketData2(int bindCode) throws Exception {
		if (!isCalled2) {
			isCalled2 = true;
			try {
				firstConfig2 = getFirstTimeConfigInstance2(bindCode,ConfigDeviceActivity.this);
			} catch (Exception e) {
				LogUtil.e("Config erorr");
				return;
			}
			firstConfig2.transmitSettings();
		}
	}

	/**
	 * Stops the transmission of live packets to server. callback of FTC_SUCCESS
	 * or failure also will trigger this method.
	 */
	private void stopPacketData2() {
		if (isCalled2) {
			try {
				isCalled2 = false;
				firstConfig2.stopTransmitting();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/***
	 * 贝昂局域网配置发送的数据
	 * 
	 * @return
	 */
	private byte[] getUserInfo(int bindCode){
		String userId = "";
		if (CurrentUser.instance().isLogin()) {
			userId = CurrentUser.instance().getUserId();
		}
		int id = 0;
		try {
			id = Integer.parseInt(userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte input[] = Helper.ConvertIntTo4bytesHexaFormat(id);
		if (input == null) {
			input = "Beiang".getBytes();
		} else {
			input = Helper.byteMerger("Beiang".getBytes(), input);
		}
		//return input;
		byte bingCodeBys[] = Helper.ConvertIntTo4bytesHexaFormat(bindCode);
		return Helper.byteMerger(input, bingCodeBys);
	}

	private FirstTimeConfig2 getFirstTimeConfigInstance2(int bindCode,FirstTimeConfigListener apiListener) throws Exception {
		String ssidFieldTxt = et_ssid.getText().toString().trim();
		String passwdText = et_pass.getText().toString().trim();
		byte[] totalBytes = null;
		String keyInput = null;
		return new FirstTimeConfig2(apiListener, passwdText, totalBytes, getWiFiManagerInstance().getGatewayIpAddress(), ssidFieldTxt,
				getUserInfo(bindCode));
	}

	/**
	 * returns the Wifi Manager instance which gives the network related
	 * information like Wifi ,SSID etc.
	 * 
	 * @return Wifi Manager instance
	 */
	public EasyLinkWifiManager getWiFiManagerInstance() {
		if (mWifiManager == null) {
			mWifiManager = new EasyLinkWifiManager(ConfigDeviceActivity.this);
		}
		return mWifiManager;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		LogUtil.i("LSD", "onActivityResult");
		if (resultCode != RESULT_OK) {
			return;
		}
		String string = data.getExtras().getString("auth");
		if (TextUtils.isEmpty(string)) {
			Toast.show(mActivity, "无效的二维码");
			return;
		}

		otherBindDevWay(string);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_showpass:
			if (tv_showpass.isSelected()) {
				tv_showpass.setSelected(false);
				et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				et_pass.setSelection(et_pass.getText().length());
			} else {
				tv_showpass.setSelected(true);
				et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				et_pass.setSelection(et_pass.getText().length());
			}
			break;
		case R.id.tv_config:
			String ssidFieldTxt = et_ssid.getText().toString().trim();
			String passwdText = et_pass.getText().toString().trim();
			new DB_User(mActivity).setWifiPass(ssidFieldTxt, passwdText);

			startConfig();

			break;
		case R.id.tv_remember:
			if (tv_remember.isSelected()) {
				tv_remember.setSelected(false);
			} else {
				tv_remember.setSelected(true);
			}
			break;
		case R.id.top_wifi:
			animWifi.stop();
			animWifi.start();
			break;
		case R.id.scan:
			startActivityForResult(new Intent(mActivity,CaptureActivity.class),100);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mInConfig) {
				stopConfig();
			} else {
				finish();
			}
		}
		return true;
	}

	/** 点击空白处 关闭软键盘 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			closeInputMethod();
		}
		return super.onTouchEvent(event);
	}

	private void closeInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (ConfigDeviceActivity.this.getCurrentFocus() != null) {
			if (ConfigDeviceActivity.this.getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(ConfigDeviceActivity.this.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopConfig();
		super.onDestroy();
	}
}
