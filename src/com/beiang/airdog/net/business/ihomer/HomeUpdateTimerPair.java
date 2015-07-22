package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import android.util.Base64;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeUpdateTimerPair.ReqHomeUpdateTimer;
import com.beiang.airdog.net.business.ihomer.HomeUpdateTimerPair.RspHomeUpdateTimer;
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
 * 家中更新定时器
 * 
 * @author LSD
 * 
 */
public class HomeUpdateTimerPair extends AbsSmartNetReqRspPair<ReqHomeUpdateTimer, RspHomeUpdateTimer> {

	public void sendRequest(long home_id, long timer_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week,
			byte[] cmd, ReqCbk<RspMsgBase> cbk) {
		ReqHomeUpdateTimer req = new ReqHomeUpdateTimer(home_id, timer_id, device_id, device_sn, repeat, secs, week, cmd);
		sendMsg(req, cbk);
	}

	public static class ReqHomeUpdateTimer extends AbsReqMsg {
		@SerializedName("params")
		public HomeUpdateTimerPama pama = new HomeUpdateTimerPama();

		public ReqHomeUpdateTimer(long home_id, long timer_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week,
				byte[] cmd) {
			pama.home_id = home_id;
			pama.timer_id = timer_id;
			pama.device_id = device_id;
			pama.device_sn = device_sn;
			pama.repeat = repeat;
			pama.secs = secs;
			pama.week = week;
			pama.msg = Base64.encodeToString(cmd, Base64.DEFAULT);
		}

		public static class HomeUpdateTimerPama {
			public long home_id;
			public long timer_id;
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
			return APIServerMethod.IHOMER_UpdateTimer.getMethod();
		}
	}

	public static class RspHomeUpdateTimer extends AbsRspMsg {
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
	public Class<RspHomeUpdateTimer> getResponseType() {
		return RspHomeUpdateTimer.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeUpdateTimer> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
