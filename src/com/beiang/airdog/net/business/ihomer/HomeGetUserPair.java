package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeGetUserPair.ReqHomeGetUser;
import com.beiang.airdog.net.business.ihomer.HomeGetUserPair.RspHomeGetUser;
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

/**
 * 获取家中用户
 * 
 * @author LSD
 * 
 */
public class HomeGetUserPair extends AbsSmartNetReqRspPair<ReqHomeGetUser, RspHomeGetUser> {

	public void sendRequest(long home_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeGetUser req = new ReqHomeGetUser(home_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeGetUser extends AbsReqMsg {
		@SerializedName("params")
		public HomeGetUserPama pama = new HomeGetUserPama();

		public ReqHomeGetUser(long home_id) {
			pama.home_id = home_id;
		}

		public static class HomeGetUserPama {
			public long home_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetUser.getMethod();
		}
	}

	public static class RspHomeGetUser extends AbsRspMsg {
		@SerializedName("data")
		public List<Data> datas;

		public static class Data {
			public long user_id;
			public String name;
			public String role;
			public int is_online;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeGetUser> getResponseType() {
		return RspHomeGetUser.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeGetUser> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
