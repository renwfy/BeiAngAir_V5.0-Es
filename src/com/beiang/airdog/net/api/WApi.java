package com.beiang.airdog.net.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.beiang.airdog.net.OkNetUtils;
import com.beiang.airdog.net.WNetUtils;
import com.beiang.airdog.net.WNetUtils.NetCallBack;
import com.beiang.airdog.net.entity.BaseResponse;

/***
 * 测试用接口
 * 
 * @author LSD
 *
 */
public class WApi {

	/***
	 * 获取验证码
	 * 
	 * @param context
	 * @param phone
	 * @param callBack
	 * @param rspCls
	 */
	public static void trd_authCode(Context context, String phone, final NetCallBack<BaseResponse> callBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		WNetUtils.WPost(context, "3rd_auth_code", params, callBack, rspCls);
	}

	/***
	 * 用户授权
	 * 
	 * @param context
	 * @param union_id
	 * @param phone
	 * @param auth_code
	 * @param callBack
	 * @param rspCls
	 */
	public static void trd_authorize(Context context, String union_id, String phone, String auth_code,
			final NetCallBack<BaseResponse> callBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		if (!TextUtils.isEmpty(phone)) {
			params.put("auth_code", auth_code);
			params.put("phone", phone);
		}
		params.put("union_id", union_id);
		WNetUtils.WPost(context, "3rd_authorize", params, callBack, rspCls);
	}

	/***
	 * 获取设备列表
	 * 
	 * @param context
	 * @param union_id
	 * @param callBack
	 * @param rspCls
	 */
	public static void trd_get_device(Context context, String union_id, final NetCallBack<BaseResponse> callBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("union_id", union_id);
		//WNetUtils.WPost(context, "3rd_get_device", params, callBack, rspCls);
		OkNetUtils.okPost("3rd_get_device", params, callBack, rspCls);
	}

	/***
	 * 获取Token
	 * 
	 * @param context
	 * @param did
	 * @param union_id
	 * @param callBack
	 * @param rspCls
	 */
	public static void trd_get_token(Context context, String did, String union_id, final NetCallBack<BaseResponse> callBack,
			final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("did", did);
		params.put("union_id", union_id);
		WNetUtils.WPost(context, "3rd_get_token", params, callBack, rspCls);
	}

	/***
	 * 第三方控制
	 * 
	 * @param context
	 * @param did
	 * @param trd_token
	 * @param control
	 * @param callBack
	 * @param rspCls
	 */
	public static void trd_control(Context context, JSONObject object, final NetCallBack<BaseResponse> callBack,
			final Class<?> rspCls) {
		WNetUtils.WPost(context, "3rd_control", object, callBack, rspCls);
	}

	/***
	 * 查询数据
	 * 
	 * @param context
	 * @param did
	 * @param trd_token
	 * @param callBack
	 * @param rspCls
	 */
	public static void trd_query_status(Context context, String did, String trd_token, final NetCallBack<BaseResponse> callBack,
			final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("did", did);
		params.put("3rd_token", trd_token);
		WNetUtils.WPost(context, "3rd_query_status", params, callBack, rspCls);
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
