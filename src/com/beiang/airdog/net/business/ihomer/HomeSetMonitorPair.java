package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeSetMonitorPair.ReqHomeSetMonitor;
import com.beiang.airdog.net.business.ihomer.HomeSetMonitorPair.RspHomeSetMonitor;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerAdrs;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerMethod;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsReqMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsRspMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsSmartNetReqRspPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.ReqMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

/**
 * 设置设备为主设备
 * 
 * @author LSD
 * 
 */
public class HomeSetMonitorPair extends AbsSmartNetReqRspPair<ReqHomeSetMonitor, RspHomeSetMonitor> {

	public void sendRequest(long home_id, long device_id, String device_sn, ReqCbk<RspMsgBase> cbk) {
		ReqHomeSetMonitor req = new ReqHomeSetMonitor(home_id, device_id, device_sn);
		sendMsg(req, cbk);
	}

	public static class ReqHomeSetMonitor extends AbsReqMsg {
		@SerializedName("params")
		public HomeSetMonitorPama pama = new HomeSetMonitorPama();

		public ReqHomeSetMonitor(long home_id, long device_id, String device_sn) {
			pama.home_id = home_id;
			pama.device_id = device_id;
			pama.device_sn = device_sn;
		}

		public static class HomeSetMonitorPama {
			public long home_id;
			public long device_id;
			public String device_sn;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_SetMonitor.getMethod();
		}
	}

	public static class RspHomeSetMonitor extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			//
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeSetMonitor> getResponseType() {
		return RspHomeSetMonitor.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeSetMonitor> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
