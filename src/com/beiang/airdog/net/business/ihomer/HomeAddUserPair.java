package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeAddUserPair.ReqHomeAddUser;
import com.beiang.airdog.net.business.ihomer.HomeAddUserPair.RspHomeAddUser;
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

/***
 * 家中添加用户
 * 
 * @author LSD
 *
 */
public class HomeAddUserPair extends AbsSmartNetReqRspPair<ReqHomeAddUser, RspHomeAddUser> {

	public void sendRequest(long home_id, long user_id, long tar_id, String name, String role, ReqCbk<RspMsgBase> cbk) {
		ReqHomeAddUser req = new ReqHomeAddUser(home_id, user_id, tar_id, name, role);
		sendMsg(req, cbk);
	}

	public static class ReqHomeAddUser extends AbsReqMsg {
		@SerializedName("params")
		public HomeAddUserPama pama = new HomeAddUserPama();

		public ReqHomeAddUser(long home_id, long user_id, long tar_id, String name, String role) {
			pama.home_id = home_id;
			pama.user_id = user_id;
			pama.tar_id = tar_id;
			pama.name = name;
			pama.role = role;
		}

		public static class HomeAddUserPama {
			public long home_id;
			public long user_id;
			public long tar_id;
			public String name;
			public String role;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_AddUser.getMethod();
		}
	}

	public static class RspHomeAddUser extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			//
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeAddUser> getResponseType() {
		return RspHomeAddUser.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeAddUser> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
