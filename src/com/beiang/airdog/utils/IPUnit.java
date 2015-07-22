package com.beiang.airdog.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.ui.model.IpEntity;
import com.beiang.airdog.ui.model.IpEntity.IpInfo;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class IPUnit {

	public static void startLocation(Context context, String ip) {

		Map<String, String> parmas = new HashMap<String, String>();
		parmas.put("ip", ip);
		API.getAddressWithIp(context, parmas, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				LogUtil.i(response);
				IpEntity ipEntity = null;
				try {
					ipEntity = new Gson().fromJson(response, IpEntity.class);
				} catch (JsonSyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (ipEntity != null) {
					IpInfo ipInfo = ipEntity.getData();
					if (ipInfo != null) {
						String city = ipInfo.getCity();
						if (city.contains("市")) {
							city = city.substring(0, city.length() - 1);
						}
					}
				}

			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
			}
		});
	}
}
