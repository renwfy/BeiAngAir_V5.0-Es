package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeDelTimerPair.ReqHomeDelTimer;
import com.beiang.airdog.net.business.ihomer.HomeDelTimerPair.RspHomeDelTimer;
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
public class HomeDelTimerPair extends AbsSmartNetReqRspPair<ReqHomeDelTimer, RspHomeDelTimer> {

	public void sendRequest(long home_id, long timer_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeDelTimer req = new ReqHomeDelTimer(home_id, timer_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeDelTimer extends AbsReqMsg {
		@SerializedName("params")
		public HomeDelTimerPama pama = new HomeDelTimerPama();

		public ReqHomeDelTimer(long home_id, long timer_id) {
			pama.home_id = home_id;
			pama.timer_id = timer_id;
		}

		public static class HomeDelTimerPama {
			public long home_id;
			public long timer_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_DelTimer.getMethod();
		}
	}

	public static class RspHomeDelTimer extends AbsRspMsg {
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
	public Class<RspHomeDelTimer> getResponseType() {
		return RspHomeDelTimer.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeDelTimer> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
