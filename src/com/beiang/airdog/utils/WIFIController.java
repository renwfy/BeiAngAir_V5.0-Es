package com.beiang.airdog.utils;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

public class WIFIController {
	private Context context;
	private Handler mHandler;
	private WifiManager wifiMng;
	private ConnectivityManager connManager;

	public WIFIController(Context context) {
		this.context = context;
		mHandler = new Handler();
		wifiMng = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
		connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/**
	 * wifi 是否可用
	 * 
	 * @return
	 */
	public boolean isWifiEnabled() {
		return wifiMng.isWifiEnabled();
	}

	/***
	 * 注册wifi状态改变监听
	 */
	private void registerWIFI() {
		IntentFilter mWifiFilter = new IntentFilter();
		mWifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		context.registerReceiver(mReceiver, mWifiFilter);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
				int message = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
				switch (message) {
				case WifiManager.WIFI_STATE_DISABLED:
					//
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					//
					break;
				case WifiManager.WIFI_STATE_ENABLED:
					//
					break;
				case WifiManager.WIFI_STATE_ENABLING:
					//
					break;
				case WifiManager.WIFI_STATE_UNKNOWN:
					//
					break;
				default:
					break;
				}
			}
		}
	};

	/***
	 * 启用 关闭WIFI
	 * 
	 * @param enable
	 * @param listener
	 */
	public void setWifiEnable(final boolean enable, final WIFIControllerListener listener) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				wifiMng.setWifiEnabled(enable);
				mHandler.post(new Runnable() {
					@Override
					public void run() { // TODO Auto-generated method
						if (enable) {
							if (isWifiEnabled()) {
							} else {
							}
						} else {
							if (isWifiEnabled()) {
							} else {
							}
						}
					}
				});
			}
		}.start();
	}

	/***
	 * 异步打开 wifi
	 * 
	 * @param listener
	 * @return
	 */
	public void openWifi(final WIFIControllerListener listener) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				wifiMng.setWifiEnabled(true);
				mHandler.post(new Runnable() {
					@Override
					public void run() { // TODO Auto-generated method
						if (listener != null) {
							if (isWifiEnabled()) {
							} else {
							}
						}
					}
				});
			}
		}.start();
	}

	/***
	 * 打开wifi
	 * 
	 * @return
	 */
	public boolean openWifi() {
		if (!wifiMng.isWifiEnabled()) {
			Log.i("WIFIController", "setWifiEnabled.....");
			wifiMng.setWifiEnabled(true);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.i("WIFIController", "setWifiEnabled.....end");
		}
		return wifiMng.isWifiEnabled();
	}

	public int getWifiState() {
		if (wifiMng != null) {
			return wifiMng.getWifiState();
		}
		return 0;
	}

	/***
	 * 关闭wifi
	 */
	public void closeWifi() {
		if (wifiMng.isWifiEnabled()) {
			wifiMng.setWifiEnabled(false);
		}
	}

	/***
	 * 断开指定ID的网络
	 * 
	 * @param netId
	 */
	public void disconnectWifi(int netId) {
		wifiMng.disableNetwork(netId);
		wifiMng.disconnect();
	}

	/***
	 * 检查是否配置过这个wifi
	 * 
	 * @param SSID
	 * @return
	 */
	public WifiConfiguration IsExsits(String SSID) {
		List<WifiConfiguration> existingConfigs = wifiMng.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
				return existingConfig;
			}
		}
		return null;
	}

	/***
	 * 获取已链接wifi
	 * 
	 * @return
	 */
	public WifiInfo getConnectionWifi() {
		return wifiMng.getConnectionInfo();
	}

	/**
	 * 添加一个网络配置并连接
	 * 
	 * @param wcg
	 * @return
	 */
	public boolean addNetwork(WifiConfiguration wcg) {
		int wcgID = wifiMng.addNetwork(wcg);
		Log.i("WIFIController", "wcgID = "+wcgID);
		return wifiMng.enableNetwork(wcgID, true);
	}

	public boolean isPassWorldEnable(String SSID, String Password) {
		WifiConfiguration wifiConfig = CreateWifiInfo(SSID, Password, getCipherType(SSID));

		WifiConfiguration tempConfig = IsExsits(SSID);
		if (tempConfig != null) {
			wifiMng.removeNetwork(tempConfig.networkId);
		}

		return addNetwork(wifiConfig);
	}

	/***
	 * 已配置好的所有wifi链接
	 * 
	 * @return
	 */
	public List<WifiConfiguration> getConfiguration() {
		return wifiMng.getConfiguredNetworks();
	}

	/**
	 * 指定配置好的网络进行连接
	 * 
	 * @param config
	 */
	public void connectConfiguration(WifiConfiguration config) {
		wifiMng.enableNetwork(config.networkId, true);
	}

	/***
	 * 得到加密类型
	 * 
	 * @param context
	 * @param ssid
	 * @return
	 */
	public int getCipherType(String ssid) {
		List<ScanResult> list = wifiMng.getScanResults();
		for (ScanResult scResult : list) {
			if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {
				String capabilities = scResult.capabilities;
				if (!TextUtils.isEmpty(capabilities)) {
					if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
						return 3;
					} else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
						return 2;
					} else {
						return 1;
					}
				}
			}
		}
		return 1;
	}

	/***
	 * 链接wifi
	 * 
	 * @param SSID
	 * @param Password
	 * @param type
	 *            1.没有密码:WIFICIPHER_NOPASS
	 * 
	 *            2.用wep加密:WIFICIPHER_WEP
	 * 
	 *            3.用wpa加密:WIFICIPHER_WPA
	 * @return
	 */
	public void connectWifi(final String SSID, final String Password, final int type,
			final WIFIControllerListener listener) {
		if (!isWifiEnabled()) {
			// wifi disable
			return;
		}

		if (wifiMng.getWifiState() != WifiManager.WIFI_STATE_ENABLED) {
			// wifi state disable
			return;
		}

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				WifiConfiguration wifiConfig = CreateWifiInfo(SSID, Password, type);
				if (wifiConfig == null) {
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
						}
					});
					return;
				}

				WifiConfiguration tempConfig = IsExsits(SSID);
				if (tempConfig != null) {
					wifiMng.removeNetwork(tempConfig.networkId);
				}

				final boolean bRet = addNetwork(wifiConfig);
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						NetworkInfo netInfo = connManager.getActiveNetworkInfo();
						boolean connect = (wifiMng.getWifiState() == WifiManager.WIFI_STATE_ENABLED)
								&& (netInfo != null && netInfo.isConnected());
						if (bRet && connect) {
						} else {
						}
					}
				}, 5000);
			}
		}.start();
	}

	/***
	 * 创建wifi链接
	 * 
	 * @param SSID
	 * @param Password
	 * @param Type
	 *            1.没有密码:WIFICIPHER_NOPASS
	 * 
	 *            2.用wep加密:WIFICIPHER_WEP
	 * 
	 *            3.用wpa加密:WIFICIPHER_WPA
	 * @return
	 */
	public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
		Log.i("WIFIController", "SSID:" + SSID + ",password:" + Password);
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";

		/*
		 * WifiConfiguration tempConfig = this.IsExsits(SSID);
		 * 
		 * if (tempConfig != null) {
		 * wifiMng.removeNetwork(tempConfig.networkId); } else {
		 * Log.i("WIFIController", "IsExsits is null."); }
		 */

		if (Type == 1) // WIFICIPHER_NOPASS
		{
			Log.i("WIFIController", "Type =1.");
			config.wepKeys[0] = "";
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 2) // WIFICIPHER_WEP
		{
			Log.i("WIFIController", "Type =2.");
			config.hiddenSSID = true;
			config.wepKeys[0] = "\"" + Password + "\"";
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		}
		if (Type == 3) // WIFICIPHER_WPA
		{

			Log.i("WIFIController", "Type =3.");
			config.preSharedKey = "\"" + Password + "\"";

			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		}
		return config;
	}

	/***
	 * s扫描WIFI获取列表
	 * 
	 * @param listener
	 */
	public void getWifiList(final WIFIControllerListener listener) {
		if (!isWifiEnabled()) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				wifiMng.startScan();
				final List<ScanResult> wifiList = wifiMng.getScanResults();
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (wifiList != null) {
						} else {
						}
					}
				});
			}
		}.start();
	}

	/***
	 * WIFI 操作回调接口
	 * 
	 * @author LSD
	 * 
	 */
	public interface WIFIControllerListener {
		public void onSuccess(Object type, Object obj);

		public void onFail(Object failType, Object failMsg);
	}

}
