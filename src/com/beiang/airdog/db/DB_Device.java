package com.beiang.airdog.db;

import android.content.Context;

public class DB_Device extends DB_Base {

	public DB_Device(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setName("DB_Device");
		getSettings();
	}

	public void setDevType(String devId, int devType) {
		settings.edit().putInt("type"+devId, devType).commit();
	}

	public int getDevType(String devId) {
		return settings.getInt("type"+devId, 0);
	}
	
	public void setDevName(String devId, String devName) {
		settings.edit().putString("name"+devId, devName).commit();
	}

	public String getDevName(String devId) {
		return settings.getString("name"+devId, "贝昂");
	}
	
	public void cleanDevData(){
		settings.edit().clear().commit();
	}

}
