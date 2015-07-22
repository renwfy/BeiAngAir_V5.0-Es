package com.beiang.airdog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingUnit {
	public static final String UNACTIVE = "UNACTIVE";

	private SharedPreferences mSettingSharedPreference;

	public SettingUnit(Context context) {
		mSettingSharedPreference = context.getSharedPreferences("rm_vibrate", Context.MODE_PRIVATE);
	}

	public void putCityCode(String cityCode) {
		Editor editor = mSettingSharedPreference.edit();
		editor.putString("cityCode", cityCode);
		editor.commit();
	}

	public String getCityCode() {
		return mSettingSharedPreference.getString("cityCode", "no");
	}

}
