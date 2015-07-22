package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeDelDevPair.ReqHomeDelDev;
import com.beiang.airdog.net.business.ihomer.HomeDelDevPair.RspHomeDelDev;
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
 * 家庭删除设备
 * 
 * @author LSD
 *
 */
public class HomeDelDevPair extends AbsSmartNetReqRspPair<ReqHomeDelDev, RspHomeDelDev> {

	public void sendRequest(long home_id, long device_id, String device_sn,ReqCbk<RspMsgBase> cbk) {
		ReqHomeDelDev req = new ReqHomeDelDev(home_id, device_id,device_sn);
		sendMsg(req, cbk);
	}

	public static class ReqHomeDelDev extends AbsReqMsg {
		@SerializedName("params")
		public HomeDelDevPama pama = new HomeDelDevPama();

		public ReqHomeDelDev(long home_id, long device_id,String device_sn) {
			pama.home_id = home_id;
			pama.device_id = device_id;
			pama.device_sn = device_sn;
		}

		public static class HomeDelDevPama {
			public long home_id;
			public long device_id;
			public String device_sn;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_DelDevice.getMethod();
		}
	}

	public static class RspHomeDelDev extends AbsRspMsg {
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
	public Class<RspHomeDelDev> getResponseType() {
		return RspHomeDelDev.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeDelDev> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
