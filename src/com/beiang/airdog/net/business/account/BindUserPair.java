package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.BindUserPair.ReqBindUser;
import com.beiang.airdog.net.business.account.BindUserPair.RspBindUser;
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

public class BindUserPair extends AbsSmartNetReqRspPair<ReqBindUser, RspBindUser> {

	public void sendRequest(String code, String phone, String userName, String password, ReqCbk<RspMsgBase> cbk) {
		ReqBindUser req = new ReqBindUser(code, phone, userName, password);
		sendMsg(req, cbk);
	}

	public static class ReqBindUser extends AbsReqMsg {
		@SerializedName("params")
		public BindUserPama pama = new BindUserPama();

		public ReqBindUser(String code, String phone, String userName, String password) {
			// TODO Auto-generated constructor stub
			pama.code = code;
			pama.phone = phone;
			pama.userName = userName;
			pama.password = MD5.encryptMD5(password);
		}

		public static class BindUserPama {
			@SerializedName("code")
			public String code;

			@SerializedName("phone")
			public String phone;

			@SerializedName("password")
			public String password;

			@SerializedName("user_name")
			public String userName;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_BindUser.getMethod();
		}
	}

	public static class RspBindUser extends AbsRspMsg {

		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("nick_name")
			public String nickName;

			@SerializedName("sex")
			public String sex;

			@SerializedName("phone")
			public String phone;

			@SerializedName("email")
			public String email;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.ACCOUNT;
	}

	@Override
	public Class<RspBindUser> getResponseType() {
		return RspBindUser.class;
	}

	@Override
	public JsonSerializer<ReqBindUser> getExcludeJsonSerializer() {
		return null;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}
}
