package com.beiang.airdog.net.business.account;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.account.EditUserPair.ReqEditUser;
import com.beiang.airdog.net.business.account.EditUserPair.RspEditUser;
import com.beiang.airdog.net.business.entity.UserEntity;
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

public class EditUserPair extends AbsSmartNetReqRspPair<ReqEditUser, RspEditUser> {

	public void sendRequst(UserEntity user, ReqCbk<RspMsgBase> cbk) {
		ReqEditUser req = new ReqEditUser(user);
		sendMsg(req, cbk);
	}

	public static class ReqEditUser extends AbsReqMsg {
		@SerializedName("params")
		public EditUserPama pama = new EditUserPama();
		
		public ReqEditUser(UserEntity user) {
			pama.userId = user.getUserId();
			pama.userName = user.getName();
			pama.nickname = user.getNickname();
			pama.sex = user.getSex();
			pama.email = user.getEmail();
			pama.mobile = user.getMobile();
		}
		
		public static class EditUserPama{
			@SerializedName("user_id")
			public String userId;

			@SerializedName("user_name")
			public String userName;
			
			@SerializedName("nickname")
			public String nickname;

			@SerializedName("sex")
			public String sex;

			@SerializedName("email")
			public String email;
			
			@SerializedName("mobile")
			public String mobile;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.ACCOUNT_UpdateUserInfo.getMethod();
		}
	}

	public static class RspEditUser extends AbsRspMsg {

		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("user_name")
			public String userName;
			
			@SerializedName("sex")
			public String sex;
			
			@SerializedName("mobile")
			public String mobile;
			
			@SerializedName("email")
			public String email;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.ACCOUNT;
	}

	@Override
	public Class<RspEditUser> getResponseType() {
		return RspEditUser.class;
	}

	@Override
	public JsonSerializer<ReqEditUser> getExcludeJsonSerializer() {
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
