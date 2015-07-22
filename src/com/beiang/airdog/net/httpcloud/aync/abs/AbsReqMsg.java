package com.beiang.airdog.net.httpcloud.aync.abs;

import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.ReqMsgBase;
import com.beiang.airdog.utils.MD5;
import com.google.gson.annotations.SerializedName;

public abstract class AbsReqMsg extends ReqMsgBase {
	@SerializedName("system")
	public ReqSys reqSys;

	@SerializedName("request")
	public ReqBank reqBank;

	@SerializedName("method")
	public String method;

	@SerializedName("id")
	public int id = 0;

	public AbsReqMsg() {
		// TODO Auto-generated constructor stub
		reqSys = new ReqSys();
		reqBank = new ReqBank();
		method = getReqMethod();
		id = ServerDefinition.API_ID += 1;
	}

	//public abstract ReqSys reqSysConfig();

	//public abstract ReqBank reqBankConfig();

	public abstract String getReqMethod();

	public static class ReqSys {
		@SerializedName("version")
		public double version;

		@SerializedName("jsonrpc")
		public double jsonrpc;

		@SerializedName("sign")
		public String sign;

		@SerializedName("key")
		public String key;

		@SerializedName("time")
		public String time;

		public ReqSys() {
			// TODO Auto-generated constructor stub
			this.version = ServerDefinition.API_VERSION;
			this.jsonrpc = ServerDefinition.API_JSONRPC;
			this.key = ServerDefinition.API_KEY;
			this.time = (System.currentTimeMillis() / 1000) + "";

			String token = "";
			String userId = "";
			if (CurrentUser.instance().isLogin()) {
				token = CurrentUser.instance().getToken();
				userId = CurrentUser.instance().getUserId();
			}
			this.sign = MD5.encryptMD5(key + time + userId + token + ServerDefinition.API_SECRET);
		}
	}

	public static class ReqBank {
		@SerializedName("user_id")
		public String user_id;

		@SerializedName("token")
		public String token;

		public ReqBank() {
			// TODO Auto-generated constructor stub
			this.user_id = "";
			this.token = "";
			if (CurrentUser.instance().isLogin()) {
				this.user_id = CurrentUser.instance().getUserId();
				this.token = CurrentUser.instance().getToken();
			}
		}
	}

}
