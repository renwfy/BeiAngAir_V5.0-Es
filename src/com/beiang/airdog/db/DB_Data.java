package com.beiang.airdog.db;

import android.content.Context;

public class DB_Data extends DB_Base {

	public DB_Data(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setName("DB_DATA");
		getSettings();
	}

	public void saveNoticeTip(boolean value) {
		setSaveBoolean("notice_tip", value);
	}

	public boolean getNoticeTip() {
		return getSaveBoolean("notice_tip", false);
	}

	public void savealArmTip(boolean value) {
		setSaveBoolean("alarm_tip", value);
	}

	public boolean getAlarmTip() {
		return getSaveBoolean("alarm_tip", false);
	}

}
