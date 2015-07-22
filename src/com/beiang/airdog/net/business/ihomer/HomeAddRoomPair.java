package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeAddRoomPair.ReqHomeAddRoom;
import com.beiang.airdog.net.business.ihomer.HomeAddRoomPair.RspHomeAddRoom;
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
 * 家中添加房间
 * 
 * @author LSD
 * 
 */
public class HomeAddRoomPair extends AbsSmartNetReqRspPair<ReqHomeAddRoom, RspHomeAddRoom> {

	public void sendRequest(long home_id, String name, String brief, ReqCbk<RspMsgBase> cbk) {
		ReqHomeAddRoom req = new ReqHomeAddRoom(home_id, name, brief);
		sendMsg(req, cbk);
	}

	public static class ReqHomeAddRoom extends AbsReqMsg {
		@SerializedName("params")
		public HomeAddRoomPama pama = new HomeAddRoomPama();

		public ReqHomeAddRoom(long home_id, String name, String brief) {
			pama.home_id = home_id;
			pama.name = name;
			pama.brief = brief;
		}

		public static class HomeAddRoomPama {
			public long home_id;
			public String name;
			public String brief;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_AddRoom.getMethod();
		}
	}

	public static class RspHomeAddRoom extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			public long home_id;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeAddRoom> getResponseType() {
		return RspHomeAddRoom.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeAddRoom> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
