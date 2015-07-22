package com.beiang.airdog.net.httpcloud.aync;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ssl.SslHttpStack;
import com.android.volley.toolbox.Volley;

/***
 * 网络请求框架
 * 
 * 基于Volley实现
 * 
 * @author LSD
 * 
 */
public class BARequestManager {
	private volatile static BARequestManager instance;

	/** 内部实现 */
	private BARequestManager(Context context) {
		init(context);
	}

	/** 单例 */
	public static BARequestManager instance(Context context) {
		if (instance == null) {
			synchronized (BARequestManager.class) {
				if (instance == null) {
					instance = new BARequestManager(context);
				}
			}
		}
		return instance;
	}

	/** Volley RequestQueue */
	private RequestQueue mQueue;

	/** Init RequestQueue */
	private void init(Context context) {
		if (mQueue == null)
			mQueue = Volley.newRequestQueue(context,new SslHttpStack(true));
	}

	/** getRequestQueue */
	public RequestQueue getRequestQueue() {
		if (mQueue != null) {
			return mQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}

	/** addRequest */
	public void addRequest(Request<?> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		request.setRetryPolicy(new DefaultRetryPolicy(4 * 1000, 1, 1.0f));
		mQueue.add(request);
	}

	/** cancelRequest */
	public void cancelRequest(Object tag) {
		mQueue.cancelAll(tag);
	}
}
