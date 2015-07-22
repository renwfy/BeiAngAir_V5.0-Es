package com.beiang.airdog.net.business.account;

import android.text.TextUtils;

import com.android.volley.Request.Method;
import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.db.DB_User;
import com.beiang.airdog.net.business.account.LogoutPair.ReqLogout;
import com.beiang.airdog.net.business.account.LogoutPair.RspLogout;
import com.beiang.airdog.net.business.entity.CurrentUser;
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

public class LogoutPair extends AbsSmartNetReqRspPair<ReqLogout, RspLogout> {

	public void sendRequest(ReqCbk<RspMsgBase> cbk) {
		ReqLogout req = new ReqLogout();
		sendMsg(req, cbk);
	}

	public static class ReqLogout extends AbsReqMsg {
		@SerializedName("params")
		public LogoutPama pama = new LogoutPama();

		public ReqLogout() {
			// TODO Auto-generated constructor stub
			pama.userId = CurrentUser.instance().getUserId();
			if(TextUtils.isEmpty(pama.userId)){
				pama.userId = new DB_User(BeiAngAirApplaction.getInstance()).getUserId();
			}
			pama.token = CurrentUser.instance().getToken();
			if(TextUtils.isEmpty(pama.token)){
				pama.token = new DB_User(BeiAngAirApplaction.getInstance()).getUserToken();
			}
		}

		public static class LogoutPama {
			@SerializedName("user_id")
			public String userId;

			@SerializedName("token")
			public String token;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_Logout.getMethod();
		}
	}

	public static class RspLogout extends AbsRspMsg {
		//
	}

	@Override
	public String getUri() {
		return APIServerAdrs.ACCOUNT;
	}

	@Override
	public Class<RspLogout> getResponseType() {
		return RspLogout.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqLogout> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
