package com.beiang.airdog.net;

import java.util.Iterator;
import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.utils.LogUtil;

/***
 * 网络操作类
 * 
 * Http实现基于Volley
 * 
 * @author LSD
 * 
 */
public class BANetUtil {
	private static RequestQueue mQueue;

	private static void init(Context context) {
		if (mQueue == null)
			mQueue = Volley.newRequestQueue(context);
	}

	public static void get(Context context, String path,Map<String, String> parmas, Listener<String> listener, ErrorListener errorListener) {
		init(context);
		String parma = getParmas(parmas);

		String url = path + parma;
		LogUtil.i(url);
		StringRequest request = new StringRequest(Method.GET, url, listener, errorListener);
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		request.setTag(context);
		mQueue.add(request);
	}

	public static void post(Context context, String path,final Map<String, String> parmas, Listener<String> listener, ErrorListener errorListener) {
		init(context);
		String url = API.HostName;
		StringRequest request = new StringRequest(Method.POST, url, listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return parmas;
			}
		};
		request.setRetryPolicy(new DefaultRetryPolicy(5 * 1000, 1, 1.0f));
		request.setTag(context);
		mQueue.add(request);
	}

	public static void cancel(Context context) {
		mQueue.cancelAll(context);
	}

	private static String getParmas(Map<String, String> params) {
		if(null == params){
			return "";
		}
		StringBuffer sBuffer = new StringBuffer();
		Iterator<String> iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			sBuffer.append(key + "=" + params.get(key) + "&");
		}
		return sBuffer.toString();
	}
}
