package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.AddHomerPair.ReqAddHome;
import com.beiang.airdog.net.business.ihomer.AddHomerPair.RspAddHomer;
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
 * 添加家庭
 * 
 * @author LSD
 * 
 */
public class AddHomerPair extends AbsSmartNetReqRspPair<ReqAddHome, RspAddHomer> {
	public void sendRequest(String name, ReqCbk<RspMsgBase> cbk) {
		ReqAddHome req = new ReqAddHome(name);
		sendMsg(req, cbk);
	}

	public static class ReqAddHome extends AbsReqMsg {
		@SerializedName("params")
		public AddHomePama pama = new AddHomePama();

		public ReqAddHome(String name) {
			// TODO Auto-generated constructor stub
			pama.name = name;
		}

		public static class AddHomePama {
			public String name;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_AddHome.getMethod();
		}
	}

	public static class RspAddHomer extends RspMsgBase {
		@SerializedName("data")
		public Data data;

		public static class Data {
			public long home_id;
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
	public Class<RspAddHomer> getResponseType() {
		// TODO Auto-generated method stub
		return RspAddHomer.class;
	}

	@Override
	public JsonSerializer<ReqAddHome> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}
}
