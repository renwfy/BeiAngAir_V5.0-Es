package com.beiang.airdog.net.business.ihomer;

import java.util.HashMap;
import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.ReqHomeGetDev;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
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
 * 家庭获取设备
 * 
 * @author LSD
 * 
 */
public class HomeGetDevPair extends AbsSmartNetReqRspPair<ReqHomeGetDev, RspHomeGetDev> {

	public void sendRequest(long home_id, long device_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeGetDev req = new ReqHomeGetDev(home_id, device_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeGetDev extends AbsReqMsg {
		@SerializedName("params")
		public HomeGetDevPama pama = new HomeGetDevPama();

		public ReqHomeGetDev(long home_id, long device_id) {
			pama.home_id = home_id;
			pama.device_id = device_id;
		}

		public static class HomeGetDevPama {
			public long home_id;
			public long device_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetDevice.getMethod();
		}
	}

	public static class RspHomeGetDev extends AbsRspMsg {
		@SerializedName("data")
		public List<Data> datas;

		public static class Data {
			public long device_id;
			public String device_sn;
			public String name;
			public String room;
			public int is_online;
			public String mode;
			public String device_info;
			public String product_id;
			public HashMap<String, String> value;

			public int getDevType() {
				return devType(product_id);
			}
		}

		private static int devType(String product_id) {
			int dType = 0;
			if (Device.DN_280E.equals(product_id)) {
				dType = Device.DT_280E;
			}
			if (Device.DN_JY300.equals(product_id)) {
				dType = Device.DT_JY300;
			}
			if (Device.DN_JY500.equals(product_id)) {
				dType = Device.DT_JY500;
			}
			if (Device.DN_Airdog.equals(product_id)) {
				dType = Device.DT_Airdog;
			}
			if (Device.DN_TAir.equals(product_id)) {
				dType = Device.DT_TAir;
			}
			if (Device.DN_PowerSocket.equals(product_id)) {
				dType = Device.DT_PowerSocket;
			}
			return dType;
		}

	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeGetDev> getResponseType() {
		return RspHomeGetDev.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeGetDev> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
