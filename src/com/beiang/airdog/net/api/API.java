package com.beiang.airdog.net.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.BANetUtil;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition;
import com.beiang.airdog.ui.activity.AirdogActivity;
import com.beiang.airdog.ui.activity.DeviceActivity;
import com.beiang.airdog.ui.activity.DeviceControlActivity;
import com.beiang.airdog.ui.activity.DeviceTestActivity;
import com.beiang.airdog.ui.activity.TairActivity;

/***
 * 
 * 通用API 公共操作接口
 * 
 * @author LSD
 * 
 */
public class API {
	public static final String HostName = "smart.99.com";
	public static String WLHost = "121.207.243.64";
	public static final int WLPort = 6066;

	// public static final String AppAuthUrl = "http://www.airdog.cn/auth?";
	public static final String AppAuthUrl = "http://www.airdog.cn/download?";

	public static final String AppAdConfogUrl = "http://121.207.243.132:8082/appIndex/beiang/";
	public static final String FirmwareNewVerson = ServerDefinition.IP_ADDRESS_EXT + "/v1/fs/firmware/";

	/***
	 * 获取ip
	 * 
	 * @param context
	 * @param parmas
	 * @param listener
	 * @param errorListener
	 */
	public static void getAddressWithIp(Context context, Map<String, String> parmas, Listener<String> listener, ErrorListener errorListener) {
		String GetAddressHost = "http://ip.taobao.com/service/getIpInfo.php?";
		BANetUtil.get(context, GetAddressHost, parmas, listener, errorListener);
	}

	/***
	 * 获取天气数据
	 * 
	 * @param context
	 * @param parmas
	 * @param listener
	 * @param errorListener
	 */
	public static void getWeather(Context context, Map<String, String> parmas, Listener<String> listener, ErrorListener errorListener) {
		String WeatherHost = "http://open.weather.com.cn/data/?";// 中国天气网
		BANetUtil.get(context, WeatherHost, parmas, listener, errorListener);
	}

	/***
	 * 获取微信Token
	 * 
	 * @param context
	 * @param appid
	 * @param secret
	 * @param code
	 * @param listener
	 * @param errorListener
	 */
	public static void getWinxinToken(Context context, String appid, String secret, String code, Listener<String> listener,
			ErrorListener errorListener) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put("appid", appid);
		parmas.put("secret", secret);
		parmas.put("code", code);
		parmas.put("grant_type", "authorization_code");
		BANetUtil.get(context, url, parmas, listener, errorListener);
	}

	/***
	 * 获取固件新版本
	 * 
	 * @param context
	 * @param devType
	 * @param listener
	 * @param errorListener
	 */
	public static void getFirmwareNewVerson(Context context, int devType, Listener<String> listener, ErrorListener errorListener) {
		String url = "";
		if (devType == Device.DT_Airdog) { // AirDog
			url = FirmwareNewVerson + "20020";
		} else if (devType == Device.DT_TAir) { // TAir
			url = FirmwareNewVerson + "20022";
		} else if (devType == Device.DT_Light) { // Light
		} else if (devType == Device.DT_PowerSocket) { // PowerSocket
			url = FirmwareNewVerson + "20031";
		} else if (devType == Device.DT_280E) { // 280E
			url = FirmwareNewVerson + "20026";
		} else if (devType == Device.DT_JY300) { // JY300
			url = FirmwareNewVerson + "1";
		} else if (devType == Device.DT_JY500) { // JY500
			url = FirmwareNewVerson + "20021";
		}
		BANetUtil.get(context, url, null, listener, errorListener);
	}

	public static String getParmas(Map<String, String> parmas) {
		if (null == parmas) {
			return "";
		}
		StringBuffer sBuffer = new StringBuffer();
		Iterator<String> iterator = parmas.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			sBuffer.append(key + "=" + parmas.get(key) + "&");
		}
		return sBuffer.toString();
	}

	/***
	 * 解析域名
	 * 
	 * @param host
	 * @return
	 */
	public String getNetIpAddress(String host) {
		String ipStr = "";
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(host);
			ipStr = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ipStr;
	}

}
