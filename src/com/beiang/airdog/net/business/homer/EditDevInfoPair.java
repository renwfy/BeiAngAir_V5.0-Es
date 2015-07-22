package com.beiang.airdog.net.business.homer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.EditDevInfoPair.ReqEditDevInfo;
import com.beiang.airdog.net.business.homer.EditDevInfoPair.RspEditDevInfo;
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

public class EditDevInfoPair extends AbsSmartNetReqRspPair<ReqEditDevInfo, RspEditDevInfo>{
	
	public void sendRequest(String devId,String nickname,ReqCbk<RspMsgBase> cbk) {
		ReqMsgBase req = new ReqEditDevInfo(devId,nickname);
		sendMsg(req, cbk);
	}
	
	public static class ReqEditDevInfo extends AbsReqMsg{
		@SerializedName("params")
		public EditDevInfoPama pama = new EditDevInfoPama();
		
		public ReqEditDevInfo(String devId, String nickname) {
			pama.ownerId = devId;
			pama.ndeviceSn = "";
			
			UpDateList list = new UpDateList();
			list.nick_name = nickname;
			pama.list = list;
		}
		
		public static class EditDevInfoPama{
			@SerializedName("owner_id")
			public String ownerId;		
			
			@SerializedName("ndevice_sn")
			public String ndeviceSn;
			
			@SerializedName("update_list")
			public UpDateList list;
		}
		
		public static class UpDateList{
			@SerializedName("nick_name")
			public String nick_name;	
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_UpdateDeviceInfo.getMethod();
		}
	}
	 
	public static class RspEditDevInfo extends AbsRspMsg {		
		
		@SerializedName("data")
		public Data data;
		
		public static class Data {
			
			@SerializedName("nickname")
			public String nickname;
			
			@SerializedName("ndevice_sn")
			public String ndeviceSn;
			
			@SerializedName("owner_id")
			public String ownerId;
			
			@SerializedName("product_id")
			public String productId;
			
			@SerializedName("bind_id")
			public String bindId;
		}
	}


	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}
	

	@Override
	public Class<RspEditDevInfo> getResponseType() {
		return RspEditDevInfo.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqEditDevInfo> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}


}
