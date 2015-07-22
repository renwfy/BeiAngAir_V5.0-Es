package com.beiang.airdog.utils;

import java.io.File;
import java.security.MessageDigest;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.broadlink.beiangair.R;

public class FunctionUtil {

	public static boolean checkNetwork(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isWifiConnect(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	/**
	 * 根据手机的分辨率dp 的单转成px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率px(像素) 的单转成dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 清理临时文件
	 */
	public static final void deleteTempFile() {
		File tempFolder = new File(Settings.CACHE_PATH);
		if (tempFolder.exists()) {
			File[] tempFiles = tempFolder.listFiles();
			for (File tempFile : tempFiles) {
				tempFile.delete();
			}
		}
	}

	/**
	 * 2个数组是否相
	 * @param bt1
	 * @param bt2
	 * @return
	 */
	public static boolean checkByteArrayEqual(byte[] bt1, byte[] bt2) {
		int length = bt1.length;
		boolean b = true;
		for (int i = 0; i < length; i++) {
			if (bt1[i] != bt2[i]) {
				b = false;
			}
		}
		return b;
	}
	
	/**
	 * 计算经纬度距
	 * @param startLat 
	 * @param startLng
	 * @param endLat
	 * @param endLng
	 * @return
	 */
	public static double getLocationDistance(double startLat, double startLng, double endLat, double endLng) {
		double EARTH_RADIUS = 6378137.0;
		double radLat1 = (startLat * Math.PI / 180.0);
		double radLat2 = (endLat * Math.PI / 180.0);

		double a = radLat1 - radLat2;
		double b = (startLng - endLng) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
		+ Math.cos(radLat1) * Math.cos(radLat2)
		* Math.pow(Math.sin(b / 2), 2)));

		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	/**************************************************************************
	 * 
	 * Toast Show
	 * @param context
	 * @param message
	 * 
	 * *************************************************************************
	 */
	public static void toastShow(Context context,String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	/*************************************************************************
	 * 
	 * Toast Show
	 * @param context
	 * @param message
	 * 
	 * *************************************************************************
	 */
	public static void toastShow(Context context,int messageId){
		Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
	}
	
	public static final int getPhoneHour() {
		Date curDate = new Date(System.currentTimeMillis());
		return curDate.getHours();
	}

	
	public static final String MD5(String data){ 
		try {
		    MessageDigest md = MessageDigest.getInstance("MD5");
		    byte[] sha1hash = new byte[40];

		    md.update(data.getBytes("iso-8859-1"), 0, data.length());
		    sha1hash = md.digest();
		    return convertToHex(sha1hash);
		} catch (Exception e) {}

	    return "";
	}
	
	public static boolean isMobileNumber(String str) {
		String s = formatNumberStr(str);
		if (s.length() == 11 && s.startsWith("1")) {
			return true;
		}
		return false;
	}
	
	public static String formatNumberStr(String str) {
		if (str == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int chr = str.charAt(i);
			if (chr >= 0x30 && chr <= 0x39) {
				sb.append((char) chr);
			}
		}
		return sb.toString();
	}
	
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	public static  boolean checkId(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	public static final int getSecByMill(long mill) {
		Date curDate = new Date(mill);
		return curDate.getSeconds();
	}
	
	
	private static String convertToHex(byte[] data) { 
	    StringBuffer buf = new StringBuffer();
	    int length = data.length;

	    for(int i = 0; i < length; ++i) { 
	        int halfbyte = (data[i] >>> 4) & 0x0F;
	        int two_halfs = 0;

	        do { 
	            if((0 <= halfbyte) && (halfbyte <= 9)) 
	                buf.append((char) ('0' + halfbyte));
	            else 
	                buf.append((char) ('a' + (halfbyte - 10)));

	            halfbyte = data[i] & 0x0F;
	        }

	        while(++two_halfs < 1);
	    } 

	    return buf.toString();
	}
	
	
	/**
	 * 将解释到的数据转为String
	 * @param receiverDate
	 * @param receiverLength
	 * @return
	 */
	public static String parseData(byte[] receiverDate){
		StringBuffer re = new StringBuffer();
		for(int i = 0; i < receiverDate.length ;i++){
			re.append(to16(receiverDate[i]));
		}
		
		return re.toString();
	}
	
	
	// 2进制 转 16进制
	public static String to16(int b) {
		String s = Integer.toHexString(b);
		int lenth = s.length();
		if (lenth == 1) {
			s = "0" + s;
		}
		if (lenth > 2) {
			s = s.substring(lenth - 2, lenth);
		}
		return s.toString();
	}
	

	/***
	 * 保留2位小数点
	 * @param data 0.00000
	 * @return 0.0
	 */
	public static float decimalFormat(float data){
		return (float) (Math.round(data * 100) /100.00);
	}
	
	/***
	 * 获取手机mac地址
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
        // 获取mac地址：
        String macAddress = "000000000000";
        try {
            WifiManager wifiMgr = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr
                    .getConnectionInfo());
            if (null != info) {
                if (!TextUtils.isEmpty(info.getMacAddress()))
                    macAddress = info.getMacAddress().replace(":", "");
                else
                    return macAddress;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return macAddress;
        }
        return macAddress;
    }
}
