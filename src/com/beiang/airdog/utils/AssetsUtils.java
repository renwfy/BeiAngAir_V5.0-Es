package com.beiang.airdog.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.content.Context;
import android.graphics.Typeface;

import com.beiang.airdog.BeiAngAirApplaction;

/**
 * 字体加载工具
 * 
 * @author daiye
 * 
 */
public class AssetsUtils {

	// 方正兰亭细黑
	private static String FZLTXHPath = "fonts/FZLTXH.ttf";

	public static Typeface getFZLTXH(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), FZLTXHPath);
		return tf;
	}
	
	/**
	 * 获取assets文件内容
	 * @param fileName
	 * @return
	 */
	public static String getAgreementByName(String name) {
		try {
			InputStreamReader inputReader = new InputStreamReader(
					BeiAngAirApplaction.getInstance().getResources().getAssets().open(name));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
