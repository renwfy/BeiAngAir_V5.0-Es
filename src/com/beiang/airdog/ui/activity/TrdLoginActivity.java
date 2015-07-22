package com.beiang.airdog.ui.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.alink.sdk.AlinkAccess;
import com.alibaba.alink.sdk.ICallback;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseSwipebackActivity;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;

/***
 * Description ：第三方登录界面
 * 
 * @author Lsd
 * @Date 2014-03-28
 * 
 */

public class TrdLoginActivity extends BaseSwipebackActivity implements OnClickListener {
	TextView tv_auth;
	String AppID="wx037f301a0ec34f84";
	String AppSecret ="a396ffb1fbdaa9855a84b0dc0f891c0b";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trdlogin);

		initView();
		addCustomPlatforms();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();
		if (intent.hasExtra("weixin_code")) {
			String code = intent.getStringExtra("weixin_code");
			getWinxinToken(code);
		}
	}
	

	private void initView() {
		// TODO Auto-generated method stub
		tv_auth = (TextView) findViewById(R.id.tv_auth);
		findViewById(R.id.iv_login_taobao).setOnClickListener(this);
		findViewById(R.id.iv_login_weix).setOnClickListener(this);
		findViewById(R.id.iv_login_sina).setOnClickListener(this);
		findViewById(R.id.iv_login_qq).setOnClickListener(this);
		tv_auth.setOnClickListener(this);
		tv_auth.setSelected(true);
	}

	/**
	 * 添加所有的平台</br>
	 */
	private void addCustomPlatforms() {
		// 添加微信配置
		addWXPlatform();
	}

	private void addWXPlatform() {
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_login_taobao:
			taobaoLogin();
			break;
		case R.id.iv_login_weix:
			weixinLogin();
			break;
		case R.id.iv_login_sina:
			Toast.show(mActivity, "暂未开放此功能");
			break;
		case R.id.iv_login_qq:
			Toast.show(mActivity, "暂未开放此功能");
			break;
		case R.id.tv_auth:
			if (tv_auth.isSelected()) {
				tv_auth.setSelected(false);
			} else {
				tv_auth.setSelected(false);
			}
			break;

		default:
			break;
		}
	}

	/***********************************************************************/
	// 微信登陆
	private void weixinLogin() {
		if (!tv_auth.isSelected()) {
			Toast.show(mActivity, "请同意第三方登陆");
			return;
		}
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "beiang_weixin_login";
		BeiAngAirApplaction.api.sendReq(req);
	}
	
	private void getWinxinToken(String code) {
		showDialog("正在授权");
		API.getWinxinToken(mActivity, AppID, AppSecret, code, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				LogUtil.i("response = "+response);
				HashMap<String, String> data = new Gson().fromJson(response, new TypeToken<HashMap<String, String>>() {
				}.getType());
				
				String token = data.get("access_token");
				String account = data.get("openid");;
				trdAuth("wx", account, token);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				hideDialog();
				Toast.show(mActivity, "登陆失败");
			}
		});
	}

	/***********************************************************************/
	// 淘宝登陆
	private void taobaoLogin() {
		if (!tv_auth.isSelected()) {
			Toast.show(mActivity, "请同意第三方登陆");
			return;
		}
		AlinkAccess alinkAccess = AlinkAccess.getInstance();
		alinkAccess.getAccessToken(mActivity, new ICallback() {
			@Override
			public void success(Map arg) {
				// success handle...
				StringBuffer sBuffer = new StringBuffer();
				Iterator<?> iterator = arg.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					sBuffer.append(key + "=" + arg.get(key) + "&");
				}
				String reqString = sBuffer.toString();
				LogUtil.i("success = " + reqString);
				final String account = (String) arg.get("account");
				final String token = (String) arg.get("token");

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showDialog("正在授权");
						trdAuth("ali",account, token);
					}
				});
			}

			@Override
			public void fail(Map arg) {
				// fail handle...
				Toast.show(mActivity, "登陆失败");
			}
		}, "23044994", "1c1ffa2141ae1a6c4a180958668ab5a6");
	}

	/***
	 * 第三方登陆贝昂平台授权
	 * 
	 * @param type
	 * @param account
	 * @param token
	 */
	private void trdAuth(String type,String account, String token) {
		BsOperationHub.instance().thdLogin(type, account, token, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					intentSkip();
				} else {
					hideDialog();
					String errorMsg = rspData.getErrorString();
					if (!"".equals(errorMsg))
						Toast.show(mActivity, errorMsg);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				hideDialog();
				String errMsg = err.getErrorString();
				if (!"".equals(errMsg)) {
					Toast.show(mActivity, errMsg);
				}
			}
		});
	}

	private void intentSkip() {
		hideDialog();

		startActivity(new Intent(mActivity, IHomerActivity.class));
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		TrdLoginActivity.this.finish();
	}

}