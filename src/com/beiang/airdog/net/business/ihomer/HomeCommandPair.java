package com.beiang.airdog.net.business.ihomer;

import android.util.Base64;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeCommandPair.ReqHomeCommand;
import com.beiang.airdog.net.business.ihomer.HomeCommandPair.RspHomeCommand;
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
 * 家庭控制命令
 * 
 * @author LSD
 *
 */
public class HomeCommandPair extends AbsSmartNetReqRspPair<ReqHomeCommand, RspHomeCommand> {

	public void sendRequest(long home_id, long device_id, String device_sn, byte[] cmd, ReqCbk<RspMsgBase> cbk) {
		ReqHomeCommand req = new ReqHomeCommand(home_id, device_id, device_sn, cmd);
		sendMsg(req, cbk);
	}

	public static class ReqHomeCommand extends AbsReqMsg {
		@SerializedName("params")
		public HomeCommandPama pama = new HomeCommandPama();

		public ReqHomeCommand(long home_id, long device_id, String device_sn, byte[] cmd) {
			pama.home_id = home_id;
			pama.device_id = device_id;
			pama.device_sn = device_sn;
			pama.msg = Base64.encodeToString(cmd, Base64.DEFAULT);
		}

		public static class HomeCommandPama {
			public long home_id;
			public long device_id;
			public String device_sn;
			public String msg_type = "b64";//编码类型
			public String msg;// Base64 encode
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_Command.getMethod();
		}
	}

	public static class RspHomeCommand extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("reply")
			public String reply;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeCommand> getResponseType() {
		return RspHomeCommand.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeCommand> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
