package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeSetDataPair.ReqHomeSetData;
import com.beiang.airdog.net.business.ihomer.HomeSetDataPair.RspHomeSetData;
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
public class HomeSetDataPair extends AbsSmartNetReqRspPair<ReqHomeSetData, RspHomeSetData> {

	public void sendRequest(long home_id,String type,String data,ReqCbk<RspMsgBase> cbk) {
		ReqHomeSetData req = new ReqHomeSetData(home_id,type,data);
		sendMsg(req, cbk);
	}

	public static class ReqHomeSetData extends AbsReqMsg {
		@SerializedName("params")
		public HomeSetDataPama pama = new HomeSetDataPama();

		public ReqHomeSetData(long home_id,String type,String data) {
			pama.home_id = home_id;
			pama.type = type;
			pama.data = data;
		}

		public static class HomeSetDataPama {
			public long home_id;
			public String type;
			public String data;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_SetData.getMethod();
		}
	}

	public static class RspHomeSetData extends AbsRspMsg {
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
	public Class<RspHomeSetData> getResponseType() {
		return RspHomeSetData.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeSetData> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
