package com.beiang.airdog.net.httpcloud.aync.abs;

import com.beiang.airdog.net.httpcloud.aync.ErrorDefinition;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class BaseMsg {

	public static abstract class ReqMsgBase {
		public boolean hasData() {
			return true;
		}

		public String genReqStr() {
			Gson gson = new Gson();
			String jstr = gson.toJson(this);
			return jstr;
		}

		public ReqMsgBase getTag() {
			return this;
		}
	}

	public static abstract class RspMsgBase {
		@SerializedName("code")
		public int errorCode = ServerDefinition.SERVER_UNKNOW_ERROR;

		@SerializedName("message")
		public String msg;

		public boolean isSuccess() {
			return errorCode == ServerDefinition.SERVER_RETURN_OK || errorCode == ServerDefinition.SERVER_RETURN_SUCCESS;
		}
		
		public String getErrorString() {
			return ErrorDefinition.errorMsg(errorCode);
		}

		public void onPostRsp() {
		}
	}
}
