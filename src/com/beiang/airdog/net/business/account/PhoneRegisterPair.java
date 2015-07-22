package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.PhoneRegisterPair.ReqPhoneRegsiter;
import com.beiang.airdog.net.business.account.PhoneRegisterPair.RspPhoneRegister;
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

public class PhoneRegisterPair extends AbsSmartNetReqRspPair<ReqPhoneRegsiter, RspPhoneRegister> {

	public void sendRequest(String phone, String passwd, String code,String info, ReqCbk<RspMsgBase> cbk) {
		ReqPhoneRegsiter req = new ReqPhoneRegsiter(phone, passwd, code,info);
		sendMsg(req, cbk);
	}

	public static class ReqPhoneRegsiter extends AbsReqMsg {
		@SerializedName("params")
		public PhoneRegsiterPama pama = new PhoneRegsiterPama();

		public ReqPhoneRegsiter(String phone, String passwd, String code,String info) {
			pama.phone = phone;
			pama.passwd = MD5.encryptMD5(passwd);
			pama.code = code;
			pama.info = info;
		}

		public static class PhoneRegsiterPama {
			@SerializedName("phone")
			public String phone;

			@SerializedName("password")
			public String passwd;
			
			@SerializedName("code")
			public String code;

			@SerializedName("info")
			public String info;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_PhoneRegister.getMethod();
		}
	}

	public static class RspPhoneRegister extends AbsRspMsg {

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
	public Class<RspPhoneRegister> getResponseType() {
		return RspPhoneRegister.class;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

	@Override
	public JsonSerializer<ReqPhoneRegsiter> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}
}
