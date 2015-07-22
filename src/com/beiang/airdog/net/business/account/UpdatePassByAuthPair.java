package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.UpdatePassByAuthPair.ReqUpdatePassByAuth;
import com.beiang.airdog.net.business.account.UpdatePassByAuthPair.RspUpdatePassByAuth;
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

public class UpdatePassByAuthPair extends AbsSmartNetReqRspPair<ReqUpdatePassByAuth, RspUpdatePassByAuth> {

	public void sendRequest(String phone,String userName,String newPass,String code, ReqCbk<RspMsgBase> cbk) {
		ReqUpdatePassByAuth req = new ReqUpdatePassByAuth(phone,userName,newPass,code);
		sendMsg(req, cbk);
	}

	public static class ReqUpdatePassByAuth extends AbsReqMsg {
		@SerializedName("params")
		public UpdatePassByAuthPama pama = new UpdatePassByAuthPama();

		public ReqUpdatePassByAuth(String phone,String userName,String newPass,String code) {
			pama.phone = phone;
			//pama.userName = userName;
			pama.newPass = MD5.encryptMD5(newPass);
			pama.code = code;
		}

		public static class UpdatePassByAuthPama {
			@SerializedName("phone")
			public String phone;
			
			//@SerializedName("user_name")
			//public String userName;
			
			@SerializedName("newpasswd")
			public String newPass;
			
			@SerializedName("code")
			public String code;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_UpdatePassByAuth.getMethod();
		}
	}

	public static class RspUpdatePassByAuth extends AbsRspMsg {

		@SerializedName("data")
		public Data data;

		public static class Data {
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
	public Class<RspUpdatePassByAuth> getResponseType() {
		return RspUpdatePassByAuth.class;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

	@Override
	public JsonSerializer<ReqUpdatePassByAuth> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}
}
