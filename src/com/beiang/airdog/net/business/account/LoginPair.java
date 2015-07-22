package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.LoginPair.ReqLogin;
import com.beiang.airdog.net.business.account.LoginPair.RspLogin;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerAdrs;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerMethod;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsReqMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsRspMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsSmartNetReqRspPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.ReqMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.utils.MD5;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

public class LoginPair extends AbsSmartNetReqRspPair<ReqLogin, RspLogin> {

	public void sendRequest(String name, String passwd, ReqCbk<RspMsgBase> cbk) {
		ReqLogin req = new ReqLogin(name, passwd);
		sendMsg(req, cbk);
	}

	public static class ReqLogin extends AbsReqMsg {
		@SerializedName("params")
		public LoginPama pama = new LoginPama();
		
		public ReqLogin(String name, String passwd) {
			pama.name = name;
			pama.passwd = MD5.encryptMD5(passwd);
		}
		
		public static class LoginPama{
			@SerializedName("user_name")
			public String name;

			@SerializedName("password")
			public String passwd;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_Login.getMethod();
		}
	}

	public static class RspLogin extends AbsRspMsg {

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
	public Class<RspLogin> getResponseType() {
		// TODO Auto-generated method stub
		return RspLogin.class;
	}

	@Override
	public JsonSerializer<ReqLogin> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}
}
