package com.beiang.airdog.utils.elink;

import android.app.Activity;
import android.os.Bundle;

import com.beiang.airdog.ui.activity.ConfigDeviceActivity;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.elink.utils.EasyLinkUtils;
import com.beiang.airdog.utils.elink.utils.EasyLinkWifiManager;

public class EasyLinkConfigDemo extends Activity implements FirstTimeConfigListener {
	private EasyLinkWifiManager mWifiManager = null;
	private FirstTimeConfig2 firstConfig2 = null;
	public boolean isCalled2 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		EasyLinkUtils.setProtraitOrientationEnabled(this);

		try {
			sendPacketData2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkWifi();
	}

	private void checkWifi() {
		// TODO Auto-generated method stub
		if (getWiFiManagerInstance().isWifiConnected()) {
		} else {
			// wifi未连接
		}
	}

	private void sendPacketData2() throws Exception {
		if (!isCalled2) {
			isCalled2 = true;
			try {
				firstConfig2 = getFirstTimeConfigInstance2(this);
			} catch (Exception e) {
				LogUtil.e("Config erorr");
				return;
			}
			firstConfig2.transmitSettings();
		}
	}

	private FirstTimeConfig2 getFirstTimeConfigInstance2(FirstTimeConfigListener apiListener) throws Exception {
		String ssidFieldTxt = getWiFiManagerInstance().getCurrentSSID();//ssid
		String passwdText = "pass";
		byte[] totalBytes = null;
		String keyInput = null;
		return new FirstTimeConfig2(apiListener, passwdText, totalBytes, getWiFiManagerInstance().getGatewayIpAddress(), ssidFieldTxt, new byte[]{});
	}

	/**
	 * returns the Wifi Manager instance which gives the network related
	 * information like Wifi ,SSID etc.
	 * 
	 * @return Wifi Manager instance
	 */
	public EasyLinkWifiManager getWiFiManagerInstance() {
		if (mWifiManager == null) {
			mWifiManager = new EasyLinkWifiManager(this);
		}
		return mWifiManager;
	}

	@Override
	public void onFirstTimeConfigEvent(FtcEvent paramFtcEvent, Exception paramException) {
		// TODO Auto-generated method stub

	}
}
