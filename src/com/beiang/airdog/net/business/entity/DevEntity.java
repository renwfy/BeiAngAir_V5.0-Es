package com.beiang.airdog.net.business.entity;

import com.beiang.airdog.ui.model.AirInfo;
import com.google.gson.annotations.SerializedName;

public class DevEntity {
	@SerializedName("uid")//设备在系统中分配的唯一ID
	public int uid;
	
	@SerializedName("role")//身份
	public String role;
	
	@SerializedName("ndevice_id")//设备ID
	public String devId;
	
	@SerializedName("device_info")//nickName/……
	public String devInfo;
	
	@SerializedName("nick_name")//nickName/……
	public String nickName;
	
	@SerializedName("ndevice_sn")//子设备ID”
	public String deviceSn;
	
	@SerializedName("product")//产品ID
	public String product;
	
	@SerializedName("product_id")//产品ID
	public String productId;
	
	@SerializedName("module")//功能类型
	public String module;
	
	@SerializedName("type")//硬件类型
	public int devType;
	
	@SerializedName("status")//online/offline
	public String status;
	
	@SerializedName("value")//base64 value
	public String value;
	
	
	//public transient String cityCode ;
	//public transient String netIP;
	
	public transient AirInfo airInfo;
	//public transient WeatherInfo weatherInfo;
}
