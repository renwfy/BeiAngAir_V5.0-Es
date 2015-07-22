package com.beiang.airdog.net.business;

import java.util.List;

import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;

public interface BaseCloudIHomer {
	public void addHome(String name, ReqCbk<RspMsgBase> cbk);

	public void getHome(ReqCbk<RspMsgBase> cbk);
	
	public void delHome(long home_id,ReqCbk<RspMsgBase> cbk);

	public void sendHomeCmd(long home_id, long device_id, String device_sn, byte[] cmd, ReqCbk<RspMsgBase> cbk);

	public void addTime(long home_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week, byte[] cmd,
			ReqCbk<RspMsgBase> cbk);

	public void getTime(long home_id, long timer_id, ReqCbk<RspMsgBase> cbk);

	public void updateTime(long home_id, long timer_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week,
			byte[] cmd, ReqCbk<RspMsgBase> cbk);

	public void delTime(long home_id, long timer_id, ReqCbk<RspMsgBase> cbk);

	public void addScript(long home_id, long device_id, String device_sn, int type, int repeat, long secs, List<Integer> week,
			String user_time, String factor, String msg, ReqCbk<RspMsgBase> cbk);

	public void getScript(long home_id, long script_id, ReqCbk<RspMsgBase> cbk);

	public void updateScript(long home_id, long device_id, long script_id, String device_sn, int type, int repeat, long secs,
			List<Integer> week, String user_time, String factor, String msg, ReqCbk<RspMsgBase> cbk);

	public void delScript(long home_id, long script_id, ReqCbk<RspMsgBase> cbk);

	public void addDevice(long home_id, long device_id, String device_sn, String device_info,String prodect_id,String name, String room, ReqCbk<RspMsgBase> cbk);

	public void updateDevice(long home_id, long device_id, String device_sn, String device_info,String prodect_id,String name, String room, ReqCbk<RspMsgBase> cbk);

	public void getDevice(long home_id, long device_id,ReqCbk<RspMsgBase> cbk);

	public void delDevice(long home_id, long device_id, String device_sn, ReqCbk<RspMsgBase> cbk);

	public void addUser(long home_id, long user_id, long tar_id, String name, String role, ReqCbk<RspMsgBase> cbk);

	public void getUser(long home_id, ReqCbk<RspMsgBase> cbk);

	public void delUser(long home_id, long user_id, long tar_id, ReqCbk<RspMsgBase> cbk);

	public void addRoom(long home_id, String name, String brief, ReqCbk<RspMsgBase> cbk);

	public void updateRoom(long home_id, long room_id, String name, String brief, ReqCbk<RspMsgBase> cbk);

	public void getRoom(long home_id, ReqCbk<RspMsgBase> cbk);

	public void delRoom(long home_id, long room_id, ReqCbk<RspMsgBase> cbk);

	public void setMonitor(long home_id, long device_id, String device_sn, ReqCbk<RspMsgBase> cbk);
	
	public void getMaster(long home_id, ReqCbk<RspMsgBase> cbk);

	public void getData(long home_id, String type, ReqCbk<RspMsgBase> cbk);

	public void setData(long home_id, String type, String data, ReqCbk<RspMsgBase> cbk);

	public void delData(long home_id, String type, ReqCbk<RspMsgBase> cbk);

}
