package com.beiang.airdog.net.business.ihomer;

import java.util.List;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.ihomer.HomeGetRoomPair.ReqHomeGetRoom;
import com.beiang.airdog.net.business.ihomer.HomeGetRoomPair.RspHomeGetRoom;
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
 * 家中获取房间
 * 
 * @author LSD
 * 
 */
public class HomeGetRoomPair extends AbsSmartNetReqRspPair<ReqHomeGetRoom, RspHomeGetRoom> {

	public void sendRequest(long home_id,ReqCbk<RspMsgBase> cbk) {
		ReqHomeGetRoom req = new ReqHomeGetRoom(home_id);
		sendMsg(req, cbk);
	}

	public static class ReqHomeGetRoom extends AbsReqMsg {
		@SerializedName("params")
		public HomeGetRoomPama pama = new HomeGetRoomPama();

		public ReqHomeGetRoom(long home_id) {
			pama.home_id = home_id;
		}

		public static class HomeGetRoomPama {
			public long home_id;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.IHOMER_GetRoom.getMethod();
		}
	}

	public static class RspHomeGetRoom extends AbsRspMsg {
		@SerializedName("data")
		public List<Data> datas;

		public static class Data {
			public long room_id;
			public String name;
			public String brief;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.IHOMER;
	}

	@Override
	public Class<RspHomeGetRoom> getResponseType() {
		return RspHomeGetRoom.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqHomeGetRoom> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
