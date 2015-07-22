package com.beiang.airdog.net;

import java.io.File;

import android.content.Context;

import com.beiang.airdog.utils.Settings;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/***
 * 下载操作类
 * 
 * 实现基于XUtils
 * 
 * @author LSD
 * 
 */
public class BADloadUtil {
	private static HttpUtils mUtils;
	private static HttpHandler handler;

	private static void init() {
		if (mUtils == null)
			mUtils = new HttpUtils();
	}

	public static void download(String url, String fileName) {
		download(url, fileName, new RequestCallBack<File>() {
			@Override
			public void onStart() {
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
			}

			@Override
			public void onFailure(HttpException error, String msg) {
			}
		});
	}

	public static void download(String url, String fileName, RequestCallBack<File> callback) {
		init();
		String target = Settings.CONFIG_PATH + "/" + fileName;
		handler = mUtils.download(url, target, false, false, callback);
	}

	public static void cancel(Context context) {
		handler.cancel();
	}
}
