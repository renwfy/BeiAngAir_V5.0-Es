package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeGetTimerPair.ReqHomeGetTimer;
import com.beiang.airdog.net.business.ihomer.HomeGetTimerPair.RspHomeGetTimer;
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
 * 家中获取定时器
 * 
 * @author LSD
 * 
 */
public class HomeGetTimerPair extends AbsSmartNetReqRspPair<ReqHomeGetTimer, RspHomeGetTimer> {

	public void sendRequest(long home_id, long timer_id,ReqCbk<RspMsgBase> cbk) {
		ReqHomeGetTimer req = new ReqHomeGetTimer(home_id,timer_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeGetTimer extends AbsReqMsg {
		@SerializedName("params")
		public HomeGetTimerPama pama = new HomeGetTimerPama();

		public ReqHomeGetTimer(long home_id,long timer_id) {
			pama.home_id = home_id;
			pama.timer_id = timer_id;
		}

		public static class HomeGetTimerPama {
			public long home_id;
			public long timer_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetTimer.getMethod();
		}
	}

	public static class RspHomeGetTimer extends AbsRspMsg {
		@SerializedName("data")
		public List<Data> data;

		public static class Data {
			public long timer_id;
			public long device_id;
			public String device_sn;
			public int repeat;
			public long secs;
			public List<Integer> week;
			public String msg;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeGetTimer> getResponseType() {
		return RspHomeGetTimer.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeGetTimer> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
