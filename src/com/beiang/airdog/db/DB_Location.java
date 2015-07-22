package com.beiang.airdog.db;

import java.util.List;

import android.content.Context;

public class DB_Location extends DB_Base {

	public DB_Location(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setName("DB_Location");
		getSettings();
	}
	
	public void saveCurCity(String curCity){
		setSaveString("curCity", curCity);
	}
	
	public String getCurCity(){
		return getSaveString("curCity", "");
	}
	
	public void saveSelectCity(String selectCity){
		setSaveString("selectCity", selectCity);
	}
	
	public String getSelectCity(){
		return getSaveString("selectCity", "");
	}
	
	public void saveCityArray(List<String> cityArray){
		setSaveMode("cityArray", cityArray);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getCityArray(){
		return (List<String>) getSaveMode("cityArray", null);
	}

}
