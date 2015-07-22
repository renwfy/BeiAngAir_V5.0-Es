package com.beiang.airdog.net.business;

import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;

public interface BaseBsOp {
	public void getDevice(String ownerId, ReqCbk<RspMsgBase> cbk);
	
	public void editDevice(DevEntity entity, ReqCbk<RspMsgBase> cbk);
	
	public void bindDevice(DevEntity dev, ReqCbk<RspMsgBase> cbk);

	public void unbindDevice(String devId, ReqCbk<RspMsgBase> cbk);

	public void resetDevice(String devId, ReqCbk<RspMsgBase> cbk);

	public void queryBindDev(String userId, String bindCode,ReqCbk<RspMsgBase> cbk);
	
	public void queryBindList(String userId, ReqCbk<RspMsgBase> cbk);
	
	public void Authrorize(DevEntity entity , ReqCbk<RspMsgBase> cbk);
	
	public void upDateAuthrorize(DevEntity entity , String userId,ReqCbk<RspMsgBase> cbk);

	public void queryDevIp(String devId, ReqCbk<RspMsgBase> cbk);
	
	public void editDevInfo(String devId, String nickname,ReqCbk<RspMsgBase> cbk);
	
	public void queryDevData(DevEntity entity, String key,ReqCbk<RspMsgBase> cbk);
	
	public void queryDevStatus(String devId,String ndevSn, ReqCbk<RspMsgBase> cbk);

	public void sendCtrlCmd(String devId, byte[] cmd, ReqCbk<RspMsgBase> cbk);
	
	public void queryWeather(String position,ReqCbk<RspMsgBase> cbk);

}
