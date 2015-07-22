package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeDelDataPair.ReqHomeDelData;
import com.beiang.airdog.net.business.ihomer.HomeDelDataPair.RspHomeDelData;
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
 * 删除设备设置数据（脚本）
 * 
 * @author LSD
 * 
 */
public class HomeDelDataPair extends AbsSmartNetReqRspPair<ReqHomeDelData, RspHomeDelData> {

	public void sendRequest(long home_id,String type,ReqCbk<RspMsgBase> cbk) {
		ReqHomeDelData req = new ReqHomeDelData(home_id,type);
		sendMsg(req, cbk);
	}

	public static class ReqHomeDelData extends AbsReqMsg {
		@SerializedName("params")
		public HomeDelDataPama pama = new HomeDelDataPama();

		public ReqHomeDelData(long home_id,String type) {
			pama.home_id = home_id;
			pama.type = type;
		}

		public static class HomeDelDataPama {
			public long home_id;
			public String type;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_DelData.getMethod();
		}
	}

	public static class RspHomeDelData extends AbsRspMsg {
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
	public Class<RspHomeDelData> getResponseType() {
		return RspHomeDelData.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeDelData> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
