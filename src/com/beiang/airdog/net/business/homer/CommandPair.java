package com.beiang.airdog.net.business.homer;

import android.util.Base64;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.CommandPair.ReqCommand;
import com.beiang.airdog.net.business.homer.CommandPair.RspCommand;
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

public class CommandPair extends AbsSmartNetReqRspPair<ReqCommand, RspCommand> {

	public void sendRequest(String devId, byte[] command, ReqCbk<RspMsgBase> cbk) {
		ReqCommand req = new ReqCommand(devId, command);
		sendMsg(req, cbk);
	}

	public static class ReqCommand extends AbsReqMsg {
		@SerializedName("params")
		public CommandPama pama = new CommandPama();

		public ReqCommand(String devId, byte[] command) {
			pama.devId = devId;
			pama.ndeviceSn = "";
			pama.command = Base64.encodeToString(command, Base64.DEFAULT);
		}

		public static class CommandPama {
			@SerializedName("ndevice_id")
			public String devId;

			@SerializedName("ndevice_sn")
			public String ndeviceSn;

			@SerializedName("command")
			public String command;// Base64 encode
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_Command.getMethod();
		}
	}

	public static class RspCommand extends AbsRspMsg {
		public transient byte[] reply;

		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("reply")
			public String reply;
		}

		@Override
		public void onPostRsp() {
			if (data != null && data.reply != null) {
				reply = Base64.decode(data.reply, Base64.DEFAULT);
			}
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}

	@Override
	public Class<RspCommand> getResponseType() {
		return RspCommand.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqCommand> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
