package com.beiang.airdog.net.business.homer;

import android.util.Base64;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.QueryDevDataPair.ReqQueryDevData;
import com.beiang.airdog.net.business.homer.QueryDevDataPair.RspQueryDevData;
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

public class QueryDevDataPair extends AbsSmartNetReqRspPair<ReqQueryDevData, RspQueryDevData> {

	public void sendRequest(DevEntity entity,String key,ReqCbk<RspMsgBase> cbk) {
		ReqQueryDevData req = new ReqQueryDevData(entity,key);
		sendMsg(req, cbk);
	}

	public static class ReqQueryDevData extends AbsReqMsg {
		@SerializedName("params")
		public QueryDevDataPama pama = new QueryDevDataPama();
		
		public ReqQueryDevData(DevEntity dev,String key) {
			// TODO Auto-generated constructor stub
			pama.devId = dev.devId;
			pama.ndeviceSn = dev.deviceSn;
			pama.key = key;
		}
		
		public static class QueryDevDataPama{
			@SerializedName("ndevice_id")
			public String devId;
			
			@SerializedName("ndevice_sn")
			public String ndeviceSn;
			
			@SerializedName("key")
			public String key;
		}
		
		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_GetDeviceData.getMethod();
		}
	}

	public static class RspQueryDevData extends AbsRspMsg {
		@SerializedName("data")
		public Data data;
		
		public transient byte[] reply;//服务器传输的时候不包括

		public static class Data {
			@SerializedName("value")
			public String value;

			@SerializedName("key")
			public String key;
		}
		
		@Override
		public void onPostRsp() {
			// TODO Auto-generated method stub
				//reply = Base64.decode(data.value, Base64.DEFAULT);
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}

	@Override
	public Class<RspQueryDevData> getResponseType() {
		return RspQueryDevData.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqQueryDevData> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
