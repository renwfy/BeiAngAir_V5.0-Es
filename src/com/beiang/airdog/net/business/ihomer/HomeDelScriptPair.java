package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeDelScriptPair.ReqHomeDelScript;
import com.beiang.airdog.net.business.ihomer.HomeDelScriptPair.RspHomeDelScript;
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
 * 家中添加规则
 * 
 * @author LSD
 * 
 */
public class HomeDelScriptPair extends AbsSmartNetReqRspPair<ReqHomeDelScript, RspHomeDelScript> {

	public void sendRequest(long home_id, long script_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeDelScript req = new ReqHomeDelScript(home_id, script_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeDelScript extends AbsReqMsg {
		@SerializedName("params")
		public HomeDelScriptPama pama = new HomeDelScriptPama();

		public ReqHomeDelScript(long home_id, long script_id) {
			pama.home_id = home_id;
			pama.script_id = script_id;
		}

		public static class HomeDelScriptPama {
			public long home_id;
			public long script_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_DelScript.getMethod();
		}
	}

	public static class RspHomeDelScript extends AbsRspMsg {
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
	public Class<RspHomeDelScript> getResponseType() {
		return RspHomeDelScript.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeDelScript> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
