package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeGetScriptPair.ReqHomeGetScript;
import com.beiang.airdog.net.business.ihomer.HomeGetScriptPair.RspHomeGetScript;
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
public class HomeGetScriptPair extends AbsSmartNetReqRspPair<ReqHomeGetScript, RspHomeGetScript> {

	public void sendRequest(long home_id, long script_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeGetScript req = new ReqHomeGetScript(home_id, script_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeGetScript extends AbsReqMsg {
		@SerializedName("params")
		public HomeGetScriptPama pama = new HomeGetScriptPama();

		public ReqHomeGetScript(long home_id, long script_id) {
			pama.home_id = home_id;
			pama.script_id = script_id;
		}

		public static class HomeGetScriptPama {
			public long home_id;
			public long script_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetScript.getMethod();
		}
	}

	public static class RspHomeGetScript extends AbsRspMsg {
		@SerializedName("data")
		public List<Data> datas;

		public static class Data {
			public long home_id;
			public long device_id;
			public String device_sn;
			public int type;
			public int repeat;
			public long secs;
			public List<Integer> week;
			public String user_time;
			public String factor;
			public String msg;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeGetScript> getResponseType() {
		return RspHomeGetScript.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeGetScript> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
