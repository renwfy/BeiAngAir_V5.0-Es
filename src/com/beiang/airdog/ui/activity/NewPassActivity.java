package com.beiang.airdog.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.db.DB_User;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseSwipebackActivity;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

/***
 * Description ：设置新密码
 * 
 * @author Lsd
 * @Date 2014-03-28
 * 
 */

public class NewPassActivity extends BaseSwipebackActivity {
	EditText et_phone, et_vercode, et_pass, tvPhone;
	ImageView phone_clear;
	TextView get_vercode, tv_set;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newpass);

		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_vercode = (EditText) findViewById(R.id.et_vercode);
		et_pass = (EditText) findViewById(R.id.et_pass);
		et_pass = (EditText) findViewById(R.id.et_pass);
		phone_clear = (ImageView) findViewById(R.id.phone_clear);
		get_vercode = (TextView) findViewById(R.id.get_vercode);
		tv_set = (TextView) findViewById(R.id.tv_set);

		get_vercode.setOnClickListener(listener);
		tv_set.setOnClickListener(listener);
		phone_clear.setOnClickListener(listener);
	}

	/***
	 * 点击事件
	 */
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.get_vercode:
				dismissKeyboard();
				String phone = et_phone.getText().toString();
				if (TextUtils.isEmpty(phone)) {
					Toast.show(mActivity, "请输入手机号");
					return;
				}
				getVcode(phone);
				break;
			case R.id.tv_set:
				dismissKeyboard();
				SetNewPassInfo();
				break;
			case R.id.phone_clear:
				et_phone.setText("");
				break;
			}
		}
	};

	private void getVcode(String phone) {
		BsOperationHub.instance().authCode(phone, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess())
					Toast.show(mActivity, "您将2分钟内收到验证码");
				else {
					String errMsg = rspData.getErrorString();
					if ("".equals(errMsg))
						Toast.show(mActivity, "获取验证码失败");
					else
						Toast.show(mActivity, errMsg);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				String errMsg = err.getErrorString();
				if ("".equals(errMsg))
					Toast.show(mActivity, "获取验证码失败");
				else
					Toast.show(mActivity, errMsg);
			}
		});
	}

	private void SetNewPassInfo() {
		DB_User db_User = new DB_User(mActivity);
		String userName = db_User.getUserName();
		String phone = et_phone.getText().toString();
		String authCode = et_vercode.getText().toString();
		String newPass = et_pass.getText().toString();

		if (TextUtils.isEmpty(authCode)) {
			Toast.show(mActivity, "请输入验证码");
			return;
		}
		if (TextUtils.isEmpty(newPass)) {
			Toast.show(mActivity, "请输入密码");
			return;
		}
		setNewPass(phone, userName, newPass, authCode);
	}

	private void setNewPass(String phone, final String userName, final String pass, String code) {
		showDialog("正在设置新密码");
		BsOperationHub.instance().updatePassByAuth(phone, userName, pass, code, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				hideDialog();
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "新密码设置成功，重新登陆");
					new DB_User(mActivity).setUserName(userName);
					new DB_User(mActivity).setUserPass(pass);
					finish();
				} else {
					String errorMsg = rspData.getErrorString();
					if (!"".equals(errorMsg))
						Toast.show(mActivity, errorMsg);
					else
						Toast.show(mActivity, "设置新密码失败");
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				hideDialog();
				String errorMsg = err.getErrorString();
				if (!"".equals(errorMsg))
					Toast.show(mActivity, errorMsg);
				else
					Toast.show(mActivity, "设置新密码失败");
			}
		});
	}

	// 隐藏输入法
	private void dismissKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}
