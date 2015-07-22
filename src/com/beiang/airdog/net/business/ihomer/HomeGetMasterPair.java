package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeGetMasterPair.ReqHomeGetMaster;
import com.beiang.airdog.net.business.ihomer.HomeGetMasterPair.RspHomeGetMaster;
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
 * 获取主设备
 * 
 * @author LSD
 * 
 */
public class HomeGetMasterPair extends AbsSmartNetReqRspPair<ReqHomeGetMaster, RspHomeGetMaster> {

	public void sendRequest(long home_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeGetMaster req = new ReqHomeGetMaster(home_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeGetMaster extends AbsReqMsg {
		@SerializedName("params")
		public HomeGetMasterPama pama = new HomeGetMasterPama();

		public ReqHomeGetMaster(long home_id) {
			pama.home_id = home_id;
		}

		public static class HomeGetMasterPama {
			public long home_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetMaster.getMethod();
		}
	}

	public static class RspHomeGetMaster extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			public long device_id;
			public String device_sn;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeGetMaster> getResponseType() {
		return RspHomeGetMaster.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeGetMaster> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
