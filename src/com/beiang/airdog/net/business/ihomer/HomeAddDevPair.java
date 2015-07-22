package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeAddDevPair.ReqHomeAddDev;
import com.beiang.airdog.net.business.ihomer.HomeAddDevPair.RspHomeAddDev;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerAdrs;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerMethod;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsReqMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsRspMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsSmartNetReqRspPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.ReqMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.utils.LogUtil;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

/***
 * 家中添加设备
 * 
 * @author LSD
 * 
 */
public class HomeAddDevPair extends AbsSmartNetReqRspPair<ReqHomeAddDev, RspHomeAddDev> {

	public void sendRequest(long home_id, long device_id, String device_sn, String device_info, String prodect_id, String name,
			String room, ReqCbk<RspMsgBase> cbk) {
		ReqHomeAddDev req = new ReqHomeAddDev(home_id, device_id, device_sn, device_info, prodect_id, name, room);
		sendMsg(req, cbk);
	}

	public static class ReqHomeAddDev extends AbsReqMsg {
		@SerializedName("params")
		public HomeAddDevPama pama = new HomeAddDevPama();

		public ReqHomeAddDev(long home_id, long device_id, String device_sn, String device_info, String prodect_id, String name, String room) {
			pama.home_id = home_id;
			pama.device_id = device_id;
			pama.device_sn = device_sn;
			pama.device_info = device_info;
			pama.prodect_id = prodect_id;
			LogUtil.i("this pid = "+prodect_id);
			pama.name = name;
			pama.room = Long.parseLong(room);
		}

		public static class HomeAddDevPama {
			public long home_id;
			public long device_id;
			public String device_sn;
			public String device_info;
			
			@SerializedName("pid")
			public String prodect_id;
			
			public String name;
			public long room;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_AddDevice.getMethod();
		}
	}

	public static class RspHomeAddDev extends AbsRspMsg {
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
	public Class<RspHomeAddDev> getResponseType() {
		return RspHomeAddDev.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeAddDev> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
