package com.beiang.airdog.utils;

import java.util.List;

import android.util.Log;

import com.beiang.airdog.constant.Config;

/***
 * 用于打印byte[]
 * 
 * @author LYSD
 * 
 */
public class LogUtil {
	public static String i(byte data) {
		String ss = String.format("%1$02x", data);
		return i(ss);
	}

	public static String i(byte[] data) {
		if (data != null) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < data.length; i++) {
				String ss = String.format("%1$02x", data[i]);
				sBuffer.append(ss + " ");
			}
			return i(sBuffer.toString().trim());
		}
		return "";
	}
	public static String s(byte[] data) {
		if (data != null) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < data.length; i++) {
				String ss = String.format("%1$02x", data[i]);
				sBuffer.append(ss + " ");
			}
			return sBuffer.toString().trim();
		}
		return "";
	}

	public static String i(List<String> data) {
		if (data != null) {
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < data.size(); i++) {
				String ss = data.get(i);
				sBuffer.append(ss + " ");
			}
			return i(sBuffer.toString().trim());
		}
		return "";
	}

	public static String i(String s) {
		if (Config.DEBUG)
			Log.i("LSD", s);
		return s;
	}

	public static String i(String Tag, String s) {
		if (Config.DEBUG)
			Log.i(Tag, s);
		return s;
	}

	public static String d(String s) {
		if (Config.DEBUG)
			Log.d("LSD", s);
		return s;
	}

	public static String d(String Tag, String s) {
		if (Config.DEBUG)
			Log.d(Tag, s);
		return s;   
	}

	public static String e(String s) {
		if (Config.DEBUG)
			Log.e("LSD", s);
		return s;
	}

	public static String e(String Tag, String s) {
		if (Config.DEBUG)
			Log.e(Tag, s);
		return s;
	}

}
