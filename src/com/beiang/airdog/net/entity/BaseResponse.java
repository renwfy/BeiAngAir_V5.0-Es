package com.beiang.airdog.net.entity;

import com.google.gson.Gson;

public class BaseResponse {
	public int code;
	public String msg;
	
	public String genReqStr() {
		Gson gson = new Gson();
		String jstr = gson.toJson(this);
		return jstr;
	}
}
