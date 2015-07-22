package com.beiang.airdog.net.business.homer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.GetDevInfoPair.ReqGetDevInfo;
import com.beiang.airdog.net.business.homer.GetDevInfoPair.RspGetDevInfo;
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

public class GetDevInfoPair extends AbsSmartNetReqRspPair<ReqGetDevInfo, RspGetDevInfo>{
	
	public void sendRequest(String ownerId,ReqCbk<RspMsgBase> cbk) {
		ReqMsgBase req = new ReqGetDevInfo(ownerId);
		sendMsg(req, cbk);
	}
	
	public static class ReqGetDevInfo extends AbsReqMsg{
		@SerializedName("params")
		public GetDevInfoPama pama = new GetDevInfoPama();
		
		public ReqGetDevInfo(String ownerId) {
			// TODO Auto-generated constructor stub
			pama.ownerId = ownerId;
		}
		
		public static class GetDevInfoPama{
			@SerializedName("owner_id")
			public String ownerId;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_GetDevice.getMethod();
		}
	}
	 
	public static class RspGetDevInfo extends AbsRspMsg {		
		
		@SerializedName("data")
		public List<Data> data;
		
		public static class Data {
			
			@SerializedName("uid")//设备在系统中分配的唯一ID
			public int uid;
			
			@SerializedName("nickname")
			public String nickname;
			
			@SerializedName("role")//身份
			public String role;
			
			@SerializedName("owner_id")//设备ID
			public String ownerId;
			
			@SerializedName("ndevice_sn")//子设备ID”
			public String ndeviceSn;
			
			@SerializedName("product_id")//产品ID
			public String productId;
			
			@SerializedName("status")//online/outline
			public String status;
			
			@SerializedName("type")//硬件类型
			public String type;
			
			@SerializedName("module")//功能类型
			public String module;
		}
	}


	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}
	

	@Override
	public Class<RspGetDevInfo> getResponseType() {
		return RspGetDevInfo.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqGetDevInfo> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}


}
