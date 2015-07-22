package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeDelRoomPair.ReqHomeDelRoom;
import com.beiang.airdog.net.business.ihomer.HomeDelRoomPair.RspHomeDelRoom;
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
 * 家中删除房间
 * 
 * @author LSD
 * 
 */
public class HomeDelRoomPair extends AbsSmartNetReqRspPair<ReqHomeDelRoom, RspHomeDelRoom> {

	public void sendRequest(long home_id, long room_id, ReqCbk<RspMsgBase> cbk) {
		ReqHomeDelRoom req = new ReqHomeDelRoom(home_id, room_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeDelRoom extends AbsReqMsg {
		@SerializedName("params")
		public HomeDelRoomPama pama = new HomeDelRoomPama();

		public ReqHomeDelRoom(long home_id, long room_id) {
			pama.home_id = home_id;
			pama.room_id = room_id;
		}

		public static class HomeDelRoomPama {
			public long home_id;
			public long room_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_DelRoom.getMethod();
		}
	}

	public static class RspHomeDelRoom extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			//
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeDelRoom> getResponseType() {
		return RspHomeDelRoom.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeDelRoom> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
