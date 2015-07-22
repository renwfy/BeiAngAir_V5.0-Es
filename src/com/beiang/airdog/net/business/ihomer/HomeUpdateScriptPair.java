package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeUpdateScriptPair.ReqHomeUpdateScript;
import com.beiang.airdog.net.business.ihomer.HomeUpdateScriptPair.RspHomeUpdateScript;
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
public class HomeUpdateScriptPair extends AbsSmartNetReqRspPair<ReqHomeUpdateScript, RspHomeUpdateScript> {

	public void sendRequest(long home_id, long device_id, long script_id, String device_sn, int type, int repeat, long secs, List<Integer> week,
			String user_time, String factor, String msg, ReqCbk<RspMsgBase> cbk) {
		ReqHomeUpdateScript req = new ReqHomeUpdateScript(home_id, device_id, script_id, device_sn, type, repeat, secs, week, user_time, factor, msg);
		sendMsg(req, cbk);
	}

	public static class ReqHomeUpdateScript extends AbsReqMsg {
		@SerializedName("params")
		public HomeUpdateScriptPama pama = new HomeUpdateScriptPama();

		public ReqHomeUpdateScript(long home_id, long device_id, long script_id, String device_sn, int type, int repeat, long secs, List<Integer> week,
				String user_time, String factor, String msg) {
			pama.home_id = home_id;
			pama.device_id = device_id;
			pama.script_id = script_id;
			pama.device_sn = device_sn;
			pama.type = type;
			pama.repeat = repeat;
			pama.secs = secs;
			pama.week = week;
			pama.user_time = user_time;
			pama.factor = factor;
			pama.msg = msg;
		}

		public static class HomeUpdateScriptPama {
			public long home_id;
			public long device_id;
			public long script_id;
			public String device_sn;
			public int type;
			public int repeat;
			public long secs;
			public List<Integer> week;
			public String user_time;
			public String factor;
			public String msg;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_UpdateScript.getMethod();
		}
	}

	public static class RspHomeUpdateScript extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			long script_id;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeUpdateScript> getResponseType() {
		return RspHomeUpdateScript.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeUpdateScript> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
