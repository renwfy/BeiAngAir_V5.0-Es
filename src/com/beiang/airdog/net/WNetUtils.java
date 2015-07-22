package com.beiang.airdog.net;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.MultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.beiang.airdog.net.entity.BaseResponse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.MD5;
import com.google.gson.Gson;

/****
 * 测试用工具类
 * 
 * Http实现基于Volley
 * 
 * @author LSD
 * 
 */
public class WNetUtils {
	public interface NetCallBack<T> {
		public void success(T rspData);

		public void failed(String msg);
	}

	public static String baseUrl = "http://test.smart.99.com/v1/partner/";
	private static RequestQueue mQueue;

	private static void init(Context context) {
		if (mQueue == null)
			mQueue = Volley.newRequestQueue(context);
	}

	public static void WPost(Context context, String urlMethod, Map<String, String> params, final NetCallBack<BaseResponse> callBack,
			final Class<?> rspCls) {
		init(context);
		final JSONObject jsonRequest = getBody(params);
		WPost(context, urlMethod, jsonRequest, callBack, rspCls);
	}

	public static void WPost(Context context, String urlMethod, JSONObject jsonRequest, final NetCallBack<BaseResponse> callBack,
			final Class<?> rspCls) {
		final String url = baseUrl + urlMethod;
		LogUtil.i("req :" + url + "  " + jsonRequest.toString());
		MultiPartRequest request = new MultiPartRequest(Method.POST, url, jsonRequest, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				LogUtil.i("rsp :" + response.toString());
				BaseResponse rsp = null;
				try {
					Gson gson = new Gson();
					rsp = (BaseResponse) gson.fromJson(response.toString(), rspCls);
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("NetUtils", "数据解析出错 !");
					e.printStackTrace();
				}
				if (callBack != null) {
					if (rsp != null) {
						int code = rsp.code;
						if (code != 0) {
							callBack.failed(rsp.msg);
						} else {
							callBack.success(rsp);
						}
					} else {
						callBack.failed("解析出错!");
					}
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				callBack.failed(error.getMessage());
			}
		});
		String body = jsonRequest.toString();
		String sing = MD5.encryptMD5(body + "Config.product_key");
		request.setHeader("ND-Pid", "Config.nd_openid");
		request.setHeader("ND-Sign", sing);
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		request.setTag(context);
		mQueue.add(request);
	}

	public static JSONObject getBody(Map<String, String> params) {
		JSONObject object = new JSONObject();
		Iterator<?> iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = params.get(key);
			try {
				object.put(key, value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return object;
	}
}
