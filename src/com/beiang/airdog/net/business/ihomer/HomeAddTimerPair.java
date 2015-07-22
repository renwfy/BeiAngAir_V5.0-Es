package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import android.util.Base64;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeAddTimerPair.ReqHomeAddTimer;
import com.beiang.airdog.net.business.ihomer.HomeAddTimerPair.RspHomeAddTimer;
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
 * 家中添加定时器
 * 
 * @author LSD
 *
 */
public class HomeAddTimerPair extends AbsSmartNetReqRspPair<ReqHomeAddTimer, RspHomeAddTimer> {

	public void sendRequest(long home_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week, byte[] cmd,
			ReqCbk<RspMsgBase> cbk) {
		ReqHomeAddTimer req = new ReqHomeAddTimer(home_id, device_id, device_sn, repeat, secs,week,cmd);
		sendMsg(req, cbk);
	}

	public static class ReqHomeAddTimer extends AbsReqMsg {
		@SerializedName("params")
		public HomeAddTimerPama pama = new HomeAddTimerPama();

		public ReqHomeAddTimer(long home_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week, byte[] cmd) {
			pama.home_id = home_id;
			pama.device_id = device_id;
			pama.device_sn = device_sn;
			pama.repeat = repeat;
			pama.secs = secs;
			pama.week = week;
			pama.msg = Base64.encodeToString(cmd, Base64.DEFAULT);
		}

		public static class HomeAddTimerPama {
			public long home_id;
			public long device_id;
			public String device_sn;
			public int repeat;
			public long secs;
			public List<Integer> week;
			public String msg;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_AddTimer.getMethod();
		}
	}

	public static class RspHomeAddTimer extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			long id;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeAddTimer> getResponseType() {
		return RspHomeAddTimer.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeAddTimer> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
