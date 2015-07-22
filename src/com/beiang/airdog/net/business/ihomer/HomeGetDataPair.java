package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeGetDataPair.ReqHomeGetData;
import com.beiang.airdog.net.business.ihomer.HomeGetDataPair.RspHomeGetData;
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
 * 往设备设置数据（脚本）
 * 
 * @author LSD
 * 
 */
public class HomeGetDataPair extends AbsSmartNetReqRspPair<ReqHomeGetData, RspHomeGetData> {

	public void sendRequest(long home_id,String type,ReqCbk<RspMsgBase> cbk) {
		ReqHomeGetData req = new ReqHomeGetData(home_id,type);
		sendMsg(req, cbk);
	}

	public static class ReqHomeGetData extends AbsReqMsg {
		@SerializedName("params")
		public HomeGetDataPama pama = new HomeGetDataPama();

		public ReqHomeGetData(long home_id,String type) {
			pama.home_id = home_id;
			pama.type = type;
		}

		public static class HomeGetDataPama {
			public long home_id;
			public String type;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetData.getMethod();
		}
	}

	public static class RspHomeGetData extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			public String data;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeGetData> getResponseType() {
		return RspHomeGetData.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeGetData> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
