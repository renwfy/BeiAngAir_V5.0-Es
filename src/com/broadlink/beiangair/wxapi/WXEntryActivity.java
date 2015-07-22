package com.broadlink.beiangair.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.ui.activity.TrdLoginActivity;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends BaseMultiPartActivity implements IWXAPIEventHandler {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BeiAngAirApplaction.api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		SendAuth.Resp sendResp = (SendAuth.Resp) resp;
		if (sendResp.errCode == BaseResp.ErrCode.ERR_OK) {
			// 同意登陆
			String code = sendResp.code;

			startActivity(new Intent(mActivity, TrdLoginActivity.class).putExtra("weixin_code", code));
			finish();
		}
	}
}
