package com.beiang.airdog.net.business.entity;

import java.util.List;

import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;

public class CurrentDevice {
	private static CurrentDevice cDevice;

	private CurrentDevice() {
	}

	synchronized public static CurrentDevice instance() {
		if (cDevice == null) {
			synchronized (CurrentDevice.class) {
				if (cDevice == null) {
					cDevice = new CurrentDevice();
				}
			}
		}
		return cDevice;
	}

	public List<DevEntity> devList;
	public DevEntity curDevice;
	public DevEntity queryDevice;
	
	public List<RspHomeGetDev.Data> homerDevList;
	public RspHomeGetDev.Data curHomerDevice;
	
	
	public void clean(){
		if(cDevice != null){
			cDevice = null;
		}
	}
}
