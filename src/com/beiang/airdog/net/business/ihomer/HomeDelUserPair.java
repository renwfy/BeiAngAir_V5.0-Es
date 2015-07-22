package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeDelUserPair.ReqHomeDelUser;
import com.beiang.airdog.net.business.ihomer.HomeDelUserPair.RspHomeDelUser;
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
 * 删除家中用户
 * 
 * @author LSD
 * 
 */
public class HomeDelUserPair extends AbsSmartNetReqRspPair<ReqHomeDelUser, RspHomeDelUser> {

	public void sendRequest(long home_id, long user_id, long tar_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeDelUser req = new ReqHomeDelUser(home_id, user_id, tar_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeDelUser extends AbsReqMsg {
		@SerializedName("params")
		public HomeDelUserPama pama = new HomeDelUserPama();

		public ReqHomeDelUser(long home_id, long user_id, long tar_id) {
			pama.home_id = home_id;
			pama.user_id = user_id;
			pama.tar_id = tar_id;
		}

		public static class HomeDelUserPama {
			public long home_id;
			public long user_id;
			public long tar_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_DelUser.getMethod();
		}
	}

	public static class RspHomeDelUser extends AbsRspMsg {
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
	public Class<RspHomeDelUser> getResponseType() {
		return RspHomeDelUser.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeDelUser> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
