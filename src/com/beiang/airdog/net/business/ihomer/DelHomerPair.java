package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.DelHomerPair.ReqDelHomer;
import com.beiang.airdog.net.business.ihomer.DelHomerPair.RspDelHomer;
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
 * 删除家庭
 * 
 * @author LSD
 * 
 */
public class DelHomerPair extends AbsSmartNetReqRspPair<ReqDelHomer, RspDelHomer> {
	public void sendRequest(long home_id,ReqCbk<RspMsgBase> cbk) {
		ReqDelHomer req = new ReqDelHomer(home_id);
		sendMsg(req, cbk);
	}

	public static class ReqDelHomer extends AbsReqMsg {
		@SerializedName("params")
		public DelHomerPama pama = new DelHomerPama();

		public ReqDelHomer(long home_id) {
			// TODO Auto-generated constructor stub
			pama.home_id = home_id;
		}

		public static class DelHomerPama {
			public long home_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_DelHome.getMethod();
		}
	}

	public static class RspDelHomer extends RspMsgBase {
		@SerializedName("data")
		public Data data;

		public static class Data {
		}
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspDelHomer> getResponseType() {
		// TODO Auto-generated method stub
		return RspDelHomer.class;
	}

	@Override
	public JsonSerializer<ReqDelHomer> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}
}
