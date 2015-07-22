package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.TrdLoginPair.RspTrdLogin;
import com.beiang.airdog.net.business.account.TrdLoginPair.TrdLogin;
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

public class TrdLoginPair extends AbsSmartNetReqRspPair<TrdLogin, RspTrdLogin> {

	public void sendRequest(String channel, String account,String token, String info, ReqCbk<RspMsgBase> cbk) {
		TrdLogin req = new TrdLogin(channel, account,token,info);
		sendMsg(req, cbk);
	}

	public static class TrdLogin extends AbsReqMsg {
		@SerializedName("params")
		public TrdLoginPama pama = new TrdLoginPama();
		
		public TrdLogin(String channel, String account,String token, String info) {
			pama.channel = channel;
			pama.account = account;
			pama.token = token;
			pama.info = info;
		}
		
		public static class TrdLoginPama{
			@SerializedName("channel")
			public String channel;

			@SerializedName("account")
			public String account;
			
			@SerializedName("token")
			public String token;
			
			@SerializedName("info")
			public String info;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_ThdLogin.getMethod();
		}
	}

	public static class RspTrdLogin extends AbsRspMsg {

		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("user_id")
			public String userId;

			@SerializedName("cookie")
			public String cookie;
			
			@SerializedName("token")
			public String token;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.ACCOUNT;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public Class<RspTrdLogin> getResponseType() {
		// TODO Auto-generated method stub
		return RspTrdLogin.class;
	}

	@Override
	public JsonSerializer<TrdLogin> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}
}
