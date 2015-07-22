package com.beiang.airdog.net.business.homer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.getDevIpPair.ReqGetDevIp;
import com.beiang.airdog.net.business.homer.getDevIpPair.RspGetDevIp;
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

public class getDevIpPair extends AbsSmartNetReqRspPair<ReqGetDevIp, RspGetDevIp> {

	public void sendRequest(String devId, ReqCbk<RspMsgBase> cbk) {
		ReqGetDevIp req = new ReqGetDevIp(devId);
		sendMsg(req, cbk);
	}

	public static class ReqGetDevIp extends AbsReqMsg {
		@SerializedName("params")
		public ResetDevPama pama = new ResetDevPama();

		public ReqGetDevIp(String devId) {
			// TODO Auto-generated constructor stub
			pama.devId = devId;
		}

		public static class ResetDevPama {
			@SerializedName("ndevice_id")
			String devId;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_GetIp.getMethod();
		}
	}

	public static class RspGetDevIp extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("ip")
			public String ip;
		}
	}

	@Override
	public int getHttpMethod() {
		return Method.POST;
	}

	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}

	@Override
	public Class<RspGetDevIp> getResponseType() {
		return RspGetDevIp.class;
	}

	@Override
	public JsonSerializer<ReqGetDevIp> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
