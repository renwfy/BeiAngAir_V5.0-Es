package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.RegisterPair.ReqRegsiter;
import com.beiang.airdog.net.business.account.RegisterPair.RspRegister;
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

public class RegisterPair extends AbsSmartNetReqRspPair<ReqRegsiter, RspRegister> {

	public void sendRequest(String name, String passwd, String info, ReqCbk<RspMsgBase> cbk) {
		ReqRegsiter req = new ReqRegsiter(name, passwd, info);
		sendMsg(req, cbk);
	}

	public static class ReqRegsiter extends AbsReqMsg {
		@SerializedName("params")
		public RegsiterPama pama = new RegsiterPama();

		public ReqRegsiter(String name, String passwd, String info) {
			pama.name = name;
			pama.passwd = MD5.encryptMD5(passwd);
			pama.info = info;
		}

		public static class RegsiterPama {
			@SerializedName("user_name")
			public String name;

			@SerializedName("password")
			public String passwd;

			@SerializedName("info")
			public String info;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_Register.getMethod();
		}
	}

	public static class RspRegister extends AbsRspMsg {

		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("user_id")
			public String userId;

			@SerializedName("cookie")
			public String cookie;
		}
	}

	@Override
	public int getHttpMethod() {
		return Method.POST;
	}

	@Override
	public String getUri() {
		return APIServerAdrs.ACCOUNT;
	}

	@Override
	public Class<RspRegister> getResponseType() {
		return RspRegister.class;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

	@Override
	public JsonSerializer<ReqRegsiter> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}
}
