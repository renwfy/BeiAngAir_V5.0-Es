package com.beiang.airdog.db;

import android.content.Context;

public class DB_User extends DB_Base {

	public DB_User(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setName("DB_USER");
		getSettings();
	}

	public void setIsLogin(boolean isLogin) {
		settings.edit().putBoolean("isLogin", isLogin).commit();
	}

	public boolean isLogin() {
		return settings.getBoolean("isLogin", false);
	}

	public void setUserName(String name) {
		settings.edit().putString("name", name).commit();
	}

	public String getUserName() {
		return settings.getString("name", "");
	}

	public void setUserPass(String pass) {
		settings.edit().putString("pass", pass).commit();
	}

	public String getUserPass() {
		return settings.getString("pass", "");
	}
	
	public void setUserId(String userId) {
		settings.edit().putString("userId", userId).commit();
	}

	public String getUserId() {
		return settings.getString("userId", "");
	}
	
	public void setUserToken(String userToken) {
		settings.edit().putString("userToken", userToken).commit();
	}

	public String getUserToken() {
		return settings.getString("userToken", "");
	}

	public void setAutoLogin(boolean autoLogin) {
		settings.edit().putBoolean("autoLogin", autoLogin).commit();
	}

	public boolean isAutoLogin() {
		return settings.getBoolean("autoLogin", true);
	}
	
	public boolean getBindTip(){
		long before = settings.getLong("tipTime", 0);
		long cur = System.currentTimeMillis();
		if(cur - before > 1000*60*60*24){
			return true;
		}
		return false;
	}
	
	public void setBindTip(){
		settings.edit().putLong("tipTime", System.currentTimeMillis()).commit();
	}
	
	public void setWifiPass(String ssid,String pass){
		settings.edit().putString(ssid, pass).commit();
	}
	public String getWifiPass(String ssid){
		return settings.getString(ssid, "");
	}
}
