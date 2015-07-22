package com.beiang.airdog.net;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.beiang.airdog.net.entity.BaseResponse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.MD5;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/****
 * 测试用工具类
 * 
 * Http实现基于OKHttp
 * 
 * @author LSD
 * 
 */
public class OkNetUtils {
	public interface NetCallBack<T> {
		public void success(T rspData);

		public void failed(String msg);
	}

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
	public static String baseUrl = "http://test.smart.99.com/v1/partner/";
	private static OkHttpClient client = new OkHttpClient();

	public static void okGet(String urlMethod, Map<String, String> params, final NetCallBack<BaseResponse> callBack, final Class<?> rspCls) {
		String url = baseUrl + urlMethod+getReqPama(params);
		Request request = new Request.Builder()
		.url(url)
		.build();
		
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Response response) throws IOException {
				// TODO Auto-generated method stub
				String rsp = response.body().string();
				LogUtil.i("LSD",rsp);
			}

			@Override
			public void onFailure(Request request, IOException e) {
				// TODO Auto-generated method stub

			}
		});
	}

	public static void okPost(String urlMethod, Map<String, String> params, final com.beiang.airdog.net.WNetUtils.NetCallBack<BaseResponse> callBack,
			final Class<?> rspCls) {
		String url = baseUrl + urlMethod;
		String json = getBody(params).toString();
		String sing = MD5.encryptMD5(json + "b3018a898393e4991324e0411aef43dc");
		RequestBody body = RequestBody.create(JSON, json);//jsonBody
		Request request = new Request.Builder()
		.addHeader("ND-Pid", "beiang")
		.addHeader("ND-Sign", sing)
		.url(url)
		//.post(getFormBody(params))
		.post(body)
		.build();
		
		client.setConnectTimeout(6, TimeUnit.SECONDS);
		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onResponse(Response response) throws IOException {
				// TODO Auto-generated method stub
				String rsp = response.body().string();
				LogUtil.i("LSD",rsp);
			}

			@Override
			public void onFailure(Request request, IOException e) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	/***
	 * post 参数JSONObjectBody
	 * 
	 * @param params
	 * @return
	 */
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
	
	/***
	 * 键值对Body
	 * 
	 * @param params
	 * @return
	 */
	public static RequestBody getFormBody(Map<String, String> params) {
		FormEncodingBuilder builder = new FormEncodingBuilder();
		Iterator<?> iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = params.get(key);
			builder.add(key, value);
		}
		RequestBody formBody = builder.build();
		return formBody;
	}
	
	/***
	 * get请求参数
	 * 
	 * @param params
	 * @return
	 */
	public static String getReqPama(Map<String, String> params) {
		StringBuffer sBuffer = new StringBuffer();
		Iterator<?> iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			sBuffer.append(key + "=" + params.get(key) + "&");
		}
		String reqString = sBuffer.toString();
		return reqString.substring(0, reqString.length() - 1);
	}

}
