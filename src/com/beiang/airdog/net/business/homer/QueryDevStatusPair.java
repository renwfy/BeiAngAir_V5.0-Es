package com.beiang.airdog.net.business.homer;

import java.util.List;

import android.util.Base64;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.QueryDevStatusPair.ReqQueryDevStatus;
import com.beiang.airdog.net.business.homer.QueryDevStatusPair.RspQueryDevStatus;
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

public class QueryDevStatusPair extends AbsSmartNetReqRspPair<ReqQueryDevStatus, RspQueryDevStatus> {

	public void sendRequest(String devId,String ndevSn, ReqCbk<RspMsgBase> cbk) {
		ReqQueryDevStatus req = new ReqQueryDevStatus(devId,ndevSn);
		sendMsg(req, cbk);
	}

	public static class ReqQueryDevStatus extends AbsReqMsg {
		@SerializedName("params")
		public QueryDevStatusPama pama = new QueryDevStatusPama();

		public ReqQueryDevStatus(String devId,String ndevSn) {
			// TODO Auto-generated constructor stub
			pama.devId = devId;
			pama.ndeviceSn = ndevSn;
		}

		public static class QueryDevStatusPama {
			@SerializedName("ndevice_id")
			public String devId;

			@SerializedName("ndevice_sn")
			public String ndeviceSn;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_GetDeviceStatus.getMethod();
		}
	}

	public static class RspQueryDevStatus extends AbsRspMsg {
		@SerializedName("data")
		public List<Data> datas;

		//public transient byte[] reply;

		public transient Data data;

		public static class Data {
			@SerializedName("value")
			public String value;

			@SerializedName("ndevice_id")
			public String devId;

			@SerializedName("product")
			public String product;

			@SerializedName("mod")
			public String mod;

			@SerializedName("device_info")
			public String devInfo;

			@SerializedName("ndevice_sn")
			public String deviceSn;

			@SerializedName("devicestatus")
			public String status; // online, offline

			@SerializedName("status")
			public String isstatus; // no user

			@SerializedName("role")
			public String role; // owner, user , guest

			@SerializedName("time")
			public String time;

		}

		@Override
		public void onPostRsp() {
			// TODO Auto-generated method stub
			if (datas != null && datas.size() > 0) {
				data = datas.get(0);
				//reply = Base64.decode(data.value, Base64.DEFAULT);
			}
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}

	@Override
	public Class<RspQueryDevStatus> getResponseType() {
		return RspQueryDevStatus.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqQueryDevStatus> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
