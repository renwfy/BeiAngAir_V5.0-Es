package com.beiang.airdog.net.business.impl;

import com.beiang.airdog.net.business.BaseBsOp;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.AuthrorizePair;
import com.beiang.airdog.net.business.homer.BindDevPair;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.business.homer.EditDevInfoPair;
import com.beiang.airdog.net.business.homer.GetDevInfoPair;
import com.beiang.airdog.net.business.homer.QueryBindedListPair;
import com.beiang.airdog.net.business.homer.QueryBindedResultPair;
import com.beiang.airdog.net.business.homer.QueryDevDataPair;
import com.beiang.airdog.net.business.homer.QueryDevStatusPair;
import com.beiang.airdog.net.business.homer.QueryWeatherPair;
import com.beiang.airdog.net.business.homer.ResetDevPair;
import com.beiang.airdog.net.business.homer.UnbindDevPair;
import com.beiang.airdog.net.business.homer.UpdateAuthrorizePair;
import com.beiang.airdog.net.business.homer.getDevIpPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;

/**
 * 
 * @author LSD
 * 
 */
public class BsOperator implements BaseBsOp {
	@Override
	public void getDevice(String ownerId, ReqCbk<RspMsgBase> cbk) { 
		// TODO Auto-generated method stub
		GetDevInfoPair pair = new GetDevInfoPair();
		pair.sendRequest(ownerId, cbk);
	}

	@Override
	public void editDevice(DevEntity entity, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		EditDevInfoPair pair = new EditDevInfoPair();
		pair.sendRequest(entity.devId, entity.devInfo, cbk);
		
	}
	
	@Override
	public void bindDevice(DevEntity dev, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		BindDevPair pair = new BindDevPair();
		pair.sendRequest(dev,cbk);
	}

	@Override
	public void unbindDevice(String devId, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		UnbindDevPair pair = new UnbindDevPair();
		pair.sendRequest(devId,cbk);
	}

	@Override
	public void resetDevice(String devId, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		ResetDevPair pair = new ResetDevPair();
		pair.sendRequest(devId, cbk);
	}

	@Override
	public void queryBindDev(String userId, String bindCode,ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		QueryBindedResultPair pair = new QueryBindedResultPair();
		pair.sendRequest(userId,bindCode,cbk);
	}
	
	@Override
	public void queryBindList(String userId,ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		QueryBindedListPair pair = new QueryBindedListPair();
		pair.sendRequest(userId,cbk);
	}
	
	@Override
	public void queryDevIp(String devId, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		getDevIpPair pair = new getDevIpPair();
		pair.sendRequest(devId, cbk);
	}
	
	@Override
	public void editDevInfo(String devId, String nickname,ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		EditDevInfoPair pair = new EditDevInfoPair();
		pair.sendRequest(devId, nickname, cbk);
	}

	@Override
	public void sendCtrlCmd(String devId, byte[] cmd, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		CommandPair pair = new CommandPair();
		pair.sendRequest(devId, cmd, cbk);
	}
	
	@Override
	public void queryDevData(DevEntity entity, String key,ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		QueryDevDataPair pair = new QueryDevDataPair();
		pair.sendRequest(entity, key,cbk);
	}
	
	@Override
	public void queryDevStatus(String devId,String ndevSn, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		QueryDevStatusPair pair = new QueryDevStatusPair();
		pair.sendRequest(devId,ndevSn, cbk);
	}
	
	@Override
	public void Authrorize(DevEntity entity, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		AuthrorizePair pair = new AuthrorizePair();
		pair.sendRequest(entity, cbk);
	}

	@Override
	public void upDateAuthrorize(DevEntity entity, String userId, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		UpdateAuthrorizePair pair = new UpdateAuthrorizePair();
		pair.sendRequest(entity, userId, cbk);
	}

	@Override
	public void queryWeather(String position, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		QueryWeatherPair pair = new QueryWeatherPair();
		pair.sendRequest(position, cbk);
	}

}
