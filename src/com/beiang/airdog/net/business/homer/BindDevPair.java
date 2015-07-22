package com.beiang.airdog.net.business.homer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.BindDevPair.ReqBindDev;
import com.beiang.airdog.net.business.homer.BindDevPair.RspBindDev;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerAdrs;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIServerMethod;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsReqMsg;
import com.beiang.airdog.net.httpcloud.aync.abs.AbsSmartNetReqRspPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.ReqMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;

/***
 * 绑定设备
 * @author LSD
 *
 */
public class BindDevPair extends AbsSmartNetReqRspPair<ReqBindDev, RspBindDev> {

	public void sendRequest(DevEntity dev, ReqCbk<RspMsgBase> cbk) {
		ReqBindDev req = new ReqBindDev(dev);
		sendMsg(req, cbk);
	}

	public static class ReqBindDev extends AbsReqMsg {
		@SerializedName("params")
		public BindDevPama pama = new BindDevPama();

		public ReqBindDev(DevEntity dev) {
			// TODO Auto-generated constructor stub
			pama.devId = dev.devId;
			pama.devInfo = dev.devInfo;
		}

		public static class BindDevPama {
			@SerializedName("ndevice_id")
			public String devId;

			@SerializedName("device_info")
			public String devInfo;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_Bind.getMethod();
		}
	}

	public static class RspBindDev extends RspMsgBase {
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
	public Class<RspBindDev> getResponseType() {
		return RspBindDev.class;
	}

	@Override
	public JsonSerializer<ReqBindDev> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}
}
