package com.beiang.airdog.net.business.ihomer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeUpdateRoomPair.ReqHomeUpdateRoom;
import com.beiang.airdog.net.business.ihomer.HomeUpdateRoomPair.RspHomeUpdateRoom;
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
 * 更新家中的房间
 * 
 * @author LSD
 * 
 */
public class HomeUpdateRoomPair extends AbsSmartNetReqRspPair<ReqHomeUpdateRoom, RspHomeUpdateRoom> {

	public void sendRequest(long home_id, long room_id, String name, String brief, ReqCbk<RspMsgBase> cbk) {
		ReqHomeUpdateRoom req = new ReqHomeUpdateRoom(home_id, room_id, name, brief);
		sendMsg(req, cbk);
	}

	public static class ReqHomeUpdateRoom extends AbsReqMsg {
		@SerializedName("params")
		public HomeUpdateRoomPama pama = new HomeUpdateRoomPama();

		public ReqHomeUpdateRoom(long home_id, long room_id, String name, String brief) {
			pama.home_id = home_id;
			pama.room_id = room_id;
			pama.name = name;
			pama.brief = brief;
		}

		public static class HomeUpdateRoomPama {
			public long home_id;
			public long room_id;
			public String name;
			public String brief;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_UpdateRoom.getMethod();
		}
	}

	public static class RspHomeUpdateRoom extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			public long home_id;
			public String name;
			public String brief;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeUpdateRoom> getResponseType() {
		return RspHomeUpdateRoom.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeUpdateRoom> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
