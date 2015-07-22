package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.ReqGetHomer;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerAdrs;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerMethod;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsReqMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsSmartNetReqRspPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.ReqMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

/***
 * 获取家庭
 * 
 * @author LSD
 * 
 */
public class GetHomerPair extends AbsSmartNetReqRspPair<ReqGetHomer, RspGetHomer> {
	public void sendRequest(ReqCbk<RspMsgBase> cbk) {
		ReqGetHomer req = new ReqGetHomer();
		sendMsg(req, cbk);
	}

	public static class ReqGetHomer extends AbsReqMsg {
		@SerializedName("params")
		public GetHomePama pama = new GetHomePama();

		public ReqGetHomer() {
			// TODO Auto-generated constructor stub
		}

		public static class GetHomePama {
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetHome.getMethod();
		}
	}

	public static class RspGetHomer extends RspMsgBase {
		@SerializedName("data")
		public List<Data> datas;

		public static class Data {
			public long home_id;
			public String name;
			public String role;
		}
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspGetHomer> getResponseType() {
		// TODO Auto-generated method stub
		return RspGetHomer.class;
	}

	@Override
	public JsonSerializer<ReqGetHomer> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}
}
