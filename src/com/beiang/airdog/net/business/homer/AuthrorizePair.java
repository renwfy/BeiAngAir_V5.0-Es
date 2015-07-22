package com.beiang.airdog.net.business.homer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.AuthrorizePair.ReqAuthrorize;
import com.beiang.airdog.net.business.homer.AuthrorizePair.RspAuthrorize;
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
 * 授权
 * 
 * @author LSD
 * 
 */
public class AuthrorizePair extends AbsSmartNetReqRspPair<ReqAuthrorize, RspAuthrorize> {
	
	public void sendRequest(DevEntity entity, ReqCbk<RspMsgBase> cbk) {
		ReqAuthrorize req = new ReqAuthrorize(entity);
		sendMsg(req, cbk);
	}

	public static class ReqAuthrorize extends AbsReqMsg {
		@SerializedName("params")
		public AuthrorizePama pama = new AuthrorizePama();
		
		public ReqAuthrorize(DevEntity entity) {
			pama.role = "user";
			pama.devInfo = entity.devInfo;
			pama.devId = entity.devId;
			pama.deviceSn = entity.deviceSn;
			pama.nick_name = "贝昂";
			pama.code = "0";
		}
		
		public static class AuthrorizePama{
			@SerializedName("role")
			public String role;

			@SerializedName("device_info")
			public String devInfo;
			
			@SerializedName("ndevice_id")
			public String devId;
			
			@SerializedName("ndevice_sn")
			public String deviceSn;
			
			@SerializedName("nick_name")
			public String nick_name;
			
			@SerializedName("code")
			public String code;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_Authorize.getMethod();
		}
	}

	public static class RspAuthrorize extends AbsRspMsg {
		//
	}

	@Override
	public int getHttpMethod() {
		return Method.POST;
	}

	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}

	@Override
	public Class<RspAuthrorize> getResponseType() {
		return RspAuthrorize.class;
	}

	@Override
	public JsonSerializer<ReqAuthrorize> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
