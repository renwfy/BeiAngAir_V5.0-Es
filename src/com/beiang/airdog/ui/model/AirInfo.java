package com.beiang.airdog.ui.model;


/***
 * 
 * @author LSD
 * 
 */
public class AirInfo extends BusinessEntity {
	// 设备类型
	private int deviceType;
	// 开关机状态
	private int onoff;
	// 手动/自动状态
	private int isAuto;
	// 手动/自动状态
	private int airSpeed;
	// 睡眠状态
	private int sleep;
	// 儿童锁状态
	private int childLock;
	// 电极运行时间
	private int electRunTimeHour;
	// 电极运行时间
	private int electRunTimeMin;
	// 空气质量档位
	private int airLevel;
	// 空气质量原始数据
	private int airValue;
	// 光照状态
	private int light;
	// 维护状态
	private int err;
	// 温度
	private int tem;
	// 温度
	private int hum;
	// TVOC
	private int airTvoc;
	// 信号强度
	private int signal;
	// 清洁复位
	private int clean;

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	public int getOnoff() {
		return onoff;
	}

	public void setOnoff(int onoff) {
		this.onoff = onoff;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public int getAirSpeed() {
		return airSpeed;
	}

	public void setAirSpeed(int airSpeed) {
		this.airSpeed = airSpeed;
	}

	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	public int getChildLock() {
		return childLock;
	}

	public void setChildLock(int childLock) {
		this.childLock = childLock;
	}

	public int getElectRunTimeHour() {
		return electRunTimeHour;
	}

	public void setElectRunTimeHour(int electRunTimeHour) {
		this.electRunTimeHour = electRunTimeHour;
	}

	public int getElectRunTimeMin() {
		return electRunTimeMin;
	}

	public void setElectRunTimeMin(int electRunTimeMin) {
		this.electRunTimeMin = electRunTimeMin;
	}

	public int getAirLevel() {
		return airLevel;
	}

	public void setAirLevel(int airLevel) {
		this.airLevel = airLevel;
	}

	public int getAirValue() {
		return airValue;
	}

	public void setAirValue(int airValue) {
		this.airValue = airValue;
	}

	public int getLight() {
		return light;
	}

	public void setLight(int light) {
		this.light = light;
	}

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public int getTem() {
		return tem;
	}

	public void setTem(int tem) {
		this.tem = tem;
	}

	public int getHum() {
		return hum;
	}

	public void setHum(int hum) {
		this.hum = hum;
	}

	public int getAirTvoc() {
		return airTvoc;
	}

	public void setAirTvoc(int airTvoc) {
		this.airTvoc = airTvoc;
	}

	public int getSignal() {
		return signal;
	}

	public void setSignal(int signal) {
		this.signal = signal;
	}

	public int getClean() {
		return clean;
	}

	public void setClean(int clean) {
		this.clean = clean;
	}

}
