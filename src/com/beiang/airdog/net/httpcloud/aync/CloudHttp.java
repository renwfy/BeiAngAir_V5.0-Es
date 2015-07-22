package com.beiang.airdog.net.httpcloud.aync;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.MultiPartRequest;
import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.ReqMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorCause;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject;
import com.beiang.airdog.net.httpcloud.aync.abs.RspMsgError;
import com.beiang.airdog.utils.LogUtil;
import com.google.gson.Gson;

public class CloudHttp {
	public static boolean sendRequest(int method, String url, final ReqMsgBase req, final ReqCbk<RspMsgBase> cbk, final Class<?> rsp, final Object tag) {
		if (null == req) {
			return false;
		}
		MultiPartRequest request = getRequest(method, url, req, cbk, rsp);
		BARequestManager.instance(BeiAngAirApplaction.getInstance()).addRequest(request, tag);
		return true;
	}

	public static MultiPartRequest getRequest(int method, String url, final ReqMsgBase req, final ReqCbk<RspMsgBase> cbk, final Class<?> cls) {
		JSONObject jsonRequest = null;
		try {
			jsonRequest = new JSONObject(req.genReqStr());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtil.i("req：" + url + " " + jsonRequest.toString());
		String reqMethod = jsonRequest.optString("method");
		//LogUtil.i("req： " + reqMethod);
		MultiPartRequest request = new MultiPartRequest(method, url, jsonRequest, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				if (null == response) {
					if (null != cbk) {
						cbk.onFailure(getErrorObj(ErrorCause.BUSINESS_RESPONSE_CODE_ERROR, 0,
								ErrorDefinition.errorMsg(ErrorCause.BUSINESS_RESPONSE_CODE_ERROR)));
					}
					return;
				}
				LogUtil.i("rsp: " + response.toString());
				int retCode = getReturnCode(response);
				//LogUtil.i("rsp: " + retCode);
				// 如果是登录失败或者cookie错误，则执行登录并且重新发送本次请求
				if (ServerDefinition.GeneralErr.COOKIE_ERROR == retCode) {
				}
				RspMsgBase rsp = null;
				if (retCode != ServerDefinition.SERVER_RETURN_OK && retCode != ServerDefinition.SERVER_RETURN_SUCCESS) {
					try {
						Gson gson = new Gson();
						rsp = (RspMsgBase) gson.fromJson(getReturnResult(response), RspMsgError.class);
					} catch (Exception e) {
						LogUtil.e("Rsp RspMsgError :" + response.toString());
					}
				} else {
					try {
						Gson gson = new Gson();
						rsp = (RspMsgBase) gson.fromJson(getReturnResult(response), cls);
						rsp.onPostRsp();
					} catch ( Exception e) {
						e.printStackTrace();
					}
				}
				if (null == rsp) {
					cbk.onFailure(getErrorObj(ErrorCause.BUSINESS_RESPONSE_CODE_ERROR, 0,
							ErrorDefinition.errorMsg(ErrorCause.BUSINESS_RESPONSE_CODE_ERROR)));
					return;
				}
				if (null != cbk) {
					cbk.onSuccess(rsp);
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				if (error instanceof NetworkError) {
					LogUtil.e("NetworkError");
					if (null != cbk)
						cbk.onFailure(getErrorObj(ErrorCause.NETWORK_UNAVAILABLE, -1, ErrorDefinition.errorMsg(ErrorCause.NETWORK_UNAVAILABLE)));
				} else if (error instanceof ServerError) {
				} else if (error instanceof AuthFailureError) {
				} else if (error instanceof ParseError) {
				} else if (error instanceof NoConnectionError) {
				} else if (error instanceof TimeoutError) {
					LogUtil.e("TimeoutError");
					if (null != cbk)
						cbk.onFailure(getErrorObj(ErrorCause.NETWORK_TIMEOUT, -1, ErrorDefinition.errorMsg(ErrorCause.NETWORK_TIMEOUT)));
				}
			}
		});
		return request;
	}

	private static int getReturnCode(JSONObject jsonRsp) {
		int nCode = -1;
		try {
			nCode = jsonRsp.getJSONObject("result").getInt("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtil.e("get code error");
			e.printStackTrace();
		}
		return nCode;
	}

	private static String getReturnResult(JSONObject jsonRsp) {
		String result = "";
		try {
			result = jsonRsp.getJSONObject("result").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtil.e("get result error");
			e.printStackTrace();
		}
		return result;
	}

	private static ErrorObject getErrorObj(ErrorCause cause, int code, String msg) {
		ErrorObject err = new ErrorObject();
		err.cause = cause;
		err.errorCode = code;
		err.errorMsg = msg;
		return err;
	}
}
