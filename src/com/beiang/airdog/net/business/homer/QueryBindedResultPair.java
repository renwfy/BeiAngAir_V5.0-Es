package com.beiang.airdog.net.business.homer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.QueryBindedResultPair.ReqQueryBindedResult;
import com.beiang.airdog.net.business.homer.QueryBindedResultPair.RspQueryBindedResult;
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

public class QueryBindedResultPair extends AbsSmartNetReqRspPair<ReqQueryBindedResult, RspQueryBindedResult> {

	public void sendRequest(String userId,String bindCode, ReqCbk<RspMsgBase> cbk) {
		ReqQueryBindedResult req = new ReqQueryBindedResult(userId,bindCode);
		sendMsg(req, cbk);
	}

	public static class ReqQueryBindedResult extends AbsReqMsg {
		@SerializedName("params")
		public QueryBindedResultPama pama = new QueryBindedResultPama();
		
		public ReqQueryBindedResult(String userId,String bindCode) {
			// TODO Auto-generated constructor stub
			pama.userId = userId;
			pama.bindCode = bindCode;
		}
		
		public static class QueryBindedResultPama{
			@SerializedName("user_id")
			public String userId;
			
			@SerializedName("bind_code")
			public String bindCode;
			
		}
		
		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_GetBindResult.getMethod();
		}
	}

	public static class RspQueryBindedResult extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("ndevice_id")
			public String devId;

			@SerializedName("device_info")
			public String devInfo;

			@SerializedName("devicestatus")
			public String status; // online, offline
			
			@SerializedName("ip")
			public String netIp;
			
			@SerializedName("right")
			public String right;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}

	@Override
	public Class<RspQueryBindedResult> getResponseType() {
		return RspQueryBindedResult.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqQueryBindedResult> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
