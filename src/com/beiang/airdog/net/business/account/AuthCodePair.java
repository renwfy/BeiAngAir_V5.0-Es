package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.AuthCodePair.ReqAuthCode;
import com.beiang.airdog.net.business.account.AuthCodePair.RspAuthCode;
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

public class AuthCodePair extends AbsSmartNetReqRspPair<ReqAuthCode, RspAuthCode> {

	public void sendRequest(String phone, ReqCbk<RspMsgBase> cbk) {
		ReqAuthCode req = new ReqAuthCode(phone);
		sendMsg(req, cbk);
	}

	public static class ReqAuthCode extends AbsReqMsg {
		@SerializedName("params")
		public AuthCodePama pama = new AuthCodePama();

		public ReqAuthCode(String phone) {
			pama.phone = phone;
			pama.action = "贝昂";
		}

		public static class AuthCodePama {
			@SerializedName("phone")
			public String phone;
			
			@SerializedName("action")
			public String action;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_AuthCode.getMethod();
		}
	}

	public static class RspAuthCode extends AbsRspMsg {

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
	public Class<RspAuthCode> getResponseType() {
		return RspAuthCode.class;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

	@Override
	public JsonSerializer<ReqAuthCode> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}
}
