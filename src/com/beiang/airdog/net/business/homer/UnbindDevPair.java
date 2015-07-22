package com.beiang.airdog.net.business.homer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.UnbindDevPair.ReqUnbindDev;
import com.beiang.airdog.net.business.homer.UnbindDevPair.RspUnbindDev;
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

public class UnbindDevPair extends AbsSmartNetReqRspPair<ReqUnbindDev, RspUnbindDev> {

	public void sendRequest(String devId, ReqCbk<RspMsgBase> cbk) {
		ReqUnbindDev req = new ReqUnbindDev(devId);
		sendMsg(req, cbk);
	}

	public static class ReqUnbindDev extends AbsReqMsg {
		@SerializedName("params")
		public UnbindDevPama pama = new UnbindDevPama();

		public ReqUnbindDev(String devId) {
			// TODO Auto-generated constructor stub
			pama.devId = devId;
		}

		public static class UnbindDevPama {
			@SerializedName("ndevice_id")
			String devId;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_Unbind.getMethod();
		}
	}

	public static class RspUnbindDev extends AbsRspMsg {
		//
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
	public Class<RspUnbindDev> getResponseType() {
		return RspUnbindDev.class;
	}

	@Override
	public JsonSerializer<ReqUnbindDev> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
