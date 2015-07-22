package com.beiang.airdog.net.business.impl;

import java.util.List;

import com.beiang.airdog.net.business.BaseCloudIHomer;
import com.beiang.airdog.net.business.ihomer.AddHomerPair;
import com.beiang.airdog.net.business.ihomer.DelHomerPair;
import com.beiang.airdog.net.business.ihomer.HomeAddDevPair;
import com.beiang.airdog.net.business.ihomer.HomeAddRoomPair;
import com.beiang.airdog.net.business.ihomer.HomeAddScriptPair;
import com.beiang.airdog.net.business.ihomer.HomeAddTimerPair;
import com.beiang.airdog.net.business.ihomer.HomeAddUserPair;
import com.beiang.airdog.net.business.ihomer.HomeCommandPair;
import com.beiang.airdog.net.business.ihomer.HomeDelDataPair;
import com.beiang.airdog.net.business.ihomer.HomeDelDevPair;
import com.beiang.airdog.net.business.ihomer.HomeDelRoomPair;
import com.beiang.airdog.net.business.ihomer.HomeDelScriptPair;
import com.beiang.airdog.net.business.ihomer.HomeDelTimerPair;
import com.beiang.airdog.net.business.ihomer.HomeDelUserPair;
import com.beiang.airdog.net.business.ihomer.HomeGetDataPair;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair;
import com.beiang.airdog.net.business.ihomer.HomeGetMasterPair;
import com.beiang.airdog.net.business.ihomer.HomeGetRoomPair;
import com.beiang.airdog.net.business.ihomer.HomeGetScriptPair;
import com.beiang.airdog.net.business.ihomer.HomeGetTimerPair;
import com.beiang.airdog.net.business.ihomer.HomeGetUserPair;
import com.beiang.airdog.net.business.ihomer.HomeSetDataPair;
import com.beiang.airdog.net.business.ihomer.HomeSetMonitorPair;
import com.beiang.airdog.net.business.ihomer.HomeUpdateDevPair;
import com.beiang.airdog.net.business.ihomer.HomeUpdateRoomPair;
import com.beiang.airdog.net.business.ihomer.HomeUpdateScriptPair;
import com.beiang.airdog.net.business.ihomer.HomeUpdateTimerPair;
import com.beiang.airdog.net.business.ihomer.GetHomerPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;

/**
 * 
 * @author LSD
 * 
 */
public class IHomerOperator implements BaseCloudIHomer {

	@Override
	public void addHome(String name, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		AddHomerPair pair = new AddHomerPair();
		pair.sendRequest(name, cbk);
	}

	@Override
	public void getHome(ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		GetHomerPair pair = new GetHomerPair();
		pair.sendRequest(cbk);
	}
	
	@Override
	public void delHome(long home_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		DelHomerPair pair = new DelHomerPair();
		pair.sendRequest(home_id,cbk);
	}

	@Override
	public void sendHomeCmd(long home_id, long device_id, String device_sn, byte[] cmd, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeCommandPair pair = new HomeCommandPair();
		pair.sendRequest(home_id, device_id, device_sn, cmd, cbk);
	}

	@Override
	public void addDevice(long home_id, long device_id, String device_sn, String device_info, String prodect_id, String name, String room,
			ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeAddDevPair pair = new HomeAddDevPair();
		pair.sendRequest(home_id, device_id, device_sn, device_info, prodect_id, name, room, cbk);
	}

	@Override
	public void updateDevice(long home_id, long device_id, String device_sn, String device_info, String prodect_id, String name,
			String room, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeUpdateDevPair pair = new HomeUpdateDevPair();
		pair.sendRequest(home_id, device_id, device_sn, device_info, prodect_id, name, room, cbk);
	}

	@Override
	public void getDevice(long home_id, long device_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeGetDevPair pair = new HomeGetDevPair();
		pair.sendRequest(home_id, device_id, cbk);
	}

	@Override
	public void delDevice(long home_id, long device_id, String device_sn, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeDelDevPair pair = new HomeDelDevPair();
		pair.sendRequest(home_id, device_id, device_sn, cbk);
	}

	@Override
	public void addUser(long home_id, long user_id, long tar_id, String name, String role, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeAddUserPair pair = new HomeAddUserPair();
		pair.sendRequest(home_id, user_id, tar_id, name, role, cbk);
	}

	@Override
	public void getUser(long home_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeGetUserPair pair = new HomeGetUserPair();
		pair.sendRequest(home_id, cbk);
	}

	@Override
	public void delUser(long home_id, long user_id, long tar_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeDelUserPair pair = new HomeDelUserPair();
		pair.sendRequest(home_id, user_id, tar_id, cbk);
	}

	@Override
	public void setMonitor(long home_id, long device_id, String device_sn, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeSetMonitorPair pair = new HomeSetMonitorPair();
		pair.sendRequest(home_id, device_id, device_sn, cbk);
	}
	
	@Override
	public void getMaster(long home_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeGetMasterPair pair = new HomeGetMasterPair();
		pair.sendRequest(home_id, cbk);
	}

	@Override
	public void addRoom(long home_id, String name, String brief, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeAddRoomPair pair = new HomeAddRoomPair();
		pair.sendRequest(home_id, name, brief, cbk);
	}

	@Override
	public void updateRoom(long home_id, long room_id, String name, String brief, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeUpdateRoomPair pair = new HomeUpdateRoomPair();
		pair.sendRequest(home_id, room_id, name, brief, cbk);
	}

	@Override
	public void getRoom(long home_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeGetRoomPair pair = new HomeGetRoomPair();
		pair.sendRequest(home_id, cbk);
	}

	@Override
	public void delRoom(long home_id, long room_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeDelRoomPair pair = new HomeDelRoomPair();
		pair.sendRequest(home_id, room_id, cbk);
	}

	@Override
	public void addTime(long home_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week, byte[] cmd,
			ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeAddTimerPair pair = new HomeAddTimerPair();
		pair.sendRequest(home_id, device_id, device_sn, repeat, secs, week, cmd, cbk);
	}

	@Override
	public void getTime(long home_id, long timer_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeGetTimerPair pair = new HomeGetTimerPair();
		pair.sendRequest(home_id, timer_id, cbk);
	}

	@Override
	public void updateTime(long home_id, long timer_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week,
			byte[] cmd, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeUpdateTimerPair pair = new HomeUpdateTimerPair();
		pair.sendRequest(home_id, timer_id, device_id, device_sn, repeat, secs, week, cmd, cbk);
	}

	@Override
	public void delTime(long home_id, long timer_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeDelTimerPair pair = new HomeDelTimerPair();
		pair.sendRequest(home_id, timer_id, cbk);
	}

	@Override
	public void addScript(long home_id, long device_id, String device_sn, int type, int repeat, long secs, List<Integer> week,
			String user_time, String factor, String msg, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeAddScriptPair pair = new HomeAddScriptPair();
		pair.sendRequest(home_id, device_id, device_sn, type, repeat, secs, week, user_time, factor, msg, cbk);
	}

	@Override
	public void getScript(long home_id, long script_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeGetScriptPair pair = new HomeGetScriptPair();
		pair.sendRequest(home_id, script_id, cbk);
	}

	@Override
	public void updateScript(long home_id, long device_id, long script_id, String device_sn, int type, int repeat, long secs,
			List<Integer> week, String user_time, String factor, String msg, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeUpdateScriptPair pair = new HomeUpdateScriptPair();
		pair.sendRequest(home_id, device_id, script_id, device_sn, type, repeat, secs, week, user_time, factor, msg, cbk);
	}

	@Override
	public void delScript(long home_id, long script_id, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeDelScriptPair pair = new HomeDelScriptPair();
		pair.sendRequest(home_id, script_id, cbk);
	}

	@Override
	public void getData(long home_id, String type, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeGetDataPair pair = new HomeGetDataPair();
		pair.sendRequest(home_id, type, cbk);
	}

	@Override
	public void setData(long home_id, String type, String data, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeSetDataPair pair = new HomeSetDataPair();
		pair.sendRequest(home_id, type, data, cbk);
	}

	@Override
	public void delData(long home_id, String type, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		HomeDelDataPair pair = new HomeDelDataPair();
		pair.sendRequest(home_id, type, cbk);
	}

}
