package com.beiang.airdog.net.business.homer;

import com.android.volley.Request.Method;
import com.beiang.airdog.net.business.homer.QueryWeatherPair.ReqQueryWeather;
import com.beiang.airdog.net.business.homer.QueryWeatherPair.RspQueryWeather;
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

public class QueryWeatherPair extends AbsSmartNetReqRspPair<ReqQueryWeather, RspQueryWeather> {

	public void sendRequest(String position, ReqCbk<RspMsgBase> cbk) {
		ReqQueryWeather req = new ReqQueryWeather(position);
		sendMsg(req, cbk);
	}

	public static class ReqQueryWeather extends AbsReqMsg {
		@SerializedName("params")
		public QueryWeatherPama pama = new QueryWeatherPama();

		public ReqQueryWeather(String position) {
			// TODO Auto-generated constructor stub
			pama.position = position;
		}

		public static class QueryWeatherPama {
			@SerializedName("position")
			public String position;
		}

		@Override
		public String getReqMethod() {
			// TODO Auto-generated method stub
			return APIServerMethod.HOMER_QueryWeather.getMethod();
		}
	}

	public static class RspQueryWeather extends AbsRspMsg {
		@SerializedName("data")
		public Data data;

		public static class Data {
			@SerializedName("weatherinfo")
			public Weatherinfo weatherinfo;
		}

		public static class Weatherinfo {
			@SerializedName("city")
			public String city;

			@SerializedName("temp1")
			public String temp1;

			@SerializedName("temp2")
			public String temp2;

			@SerializedName("temp")
			public String temp;

			@SerializedName("air")
			public String air;

			@SerializedName("pm25")
			public String pm25;

			@SerializedName("weather")
			public String weather;

			@SerializedName("weathercode")
			public String weathercode;

			@SerializedName("wind")
			public String wind;

			@SerializedName("humidity")
			public String humidity;
		}
	}

	@Override
	public String getUri() {
		return APIServerAdrs.HOMER;
	}

	@Override
	public Class<RspQueryWeather> getResponseType() {
		return RspQueryWeather.class;
	}

	@Override
	public int getHttpMethod() {
		// TODO Auto-generated method stub
		return Method.POST;
	}

	@Override
	public JsonSerializer<ReqQueryWeather> getExcludeJsonSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMsg(ReqMsgBase req, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		return sendMsg(req, cbk, req.getTag());
	}

}
