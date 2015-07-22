package com.beiang.airdog.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiang.airdog.db.DB_User;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseSwipebackActivity;
import com.beiang.airdog.utils.FunctionUtil;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

/***
 * Description ：注册界面
 * 
 * @author Lsd
 * @Date 2014-03-28
 * 
 */

public class RegisterActivity extends BaseSwipebackActivity {
	int type = 0;
	EditText et_user, et_pass, et_pass2, et_vercode;
	ImageView iv_clear;
	TextView get_vercode, tv_register, tv_thrlogin;
	LinearLayout vercode_layout;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		
		initView();
		setOnClickLisener();
	}
	
	private void initView() {
		et_user = (EditText) findViewById(R.id.et_user);
		et_pass = (EditText) findViewById(R.id.et_pass);
		et_pass2 = (EditText) findViewById(R.id.et_pass2);
		et_vercode = (EditText) findViewById(R.id.et_vercode);
		iv_clear = (ImageView) findViewById(R.id.iv_clear);
		get_vercode = (TextView) findViewById(R.id.get_vercode);
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_thrlogin = (TextView) findViewById(R.id.tv_thrlogin);
		vercode_layout = (LinearLayout) findViewById(R.id.vercode_layout);
		
		et_user.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String intputUser = s.toString();
				if (FunctionUtil.isMobileNumber(intputUser)) {
					vercode_layout.setVisibility(View.VISIBLE);
					type = 1;
				} else {
					type = 0;
					vercode_layout.setVisibility(View.GONE);
				}
			}
		});
	}
	
	public void setOnClickLisener() {
		iv_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_user.setText("");
			}
		});
		get_vercode.setOnClickListener(listener);
		tv_register.setOnClickListener(listener);
		tv_thrlogin.setOnClickListener(listener);
	}

	/***
	 * 点击事件
	 */
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_register:
				dismissKeyboard();
				String name = et_user.getText().toString();
				String pass = et_pass.getText().toString();
				String passAg = et_pass2.getText().toString();
				if (TextUtils.isEmpty(name)) {
					Toast.show(mActivity, "请输入用户名 ");
					return;
				}
				if (name.length() < 6) {
					Toast.show(mActivity, "用户名长度不足6位 ");
					return;
				}
				if (TextUtils.isEmpty(pass)) {
					Toast.show(mActivity, "请输入密码 ");
					return;
				}
				if (TextUtils.isEmpty(passAg)) {
					Toast.show(mActivity, "请再次输入密码 ");
					return;
				}
				if (!pass.equals(passAg)) {
					Toast.show(mActivity, "两次输入密码不一致 ");
					return;
				}
				
				signUp(name, pass);
				
				break;
			case R.id.get_vercode:
				String vphone = et_user.getText().toString();
				if (TextUtils.isEmpty(vphone)) {
					Toast.show(mActivity, "请输入手机号");
					return;
				}
				getVcode(vphone);
				break;
			}
		}
	};

	private void getVcode(String phone) {
		BsOperationHub.instance().authCode(phone, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "您将2分钟内收到验证码");
				} else {
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

	private void signUp(String name, String pass) {
		if (type == 0) {
			register(name, pass);
		} else {
			String code = et_vercode.getText().toString();
			if (TextUtils.isEmpty(code)) {
				Toast.show(mActivity, "请输入验证码");
				return;
			}
			phoneRegister(name, pass, code);
		}
	}

	private void register(final String name, final String pass) {
		showDialog("正在注册");
		BsOperationHub.instance().register(name, pass, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				hideDialog();
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "注册成功,请登录!");
					new DB_User(mActivity).setUserName(name);
					new DB_User(mActivity).setUserPass(pass);
					RegisterActivity.this.finish();
				} else {
					String errorMsg = rspData.getErrorString();
					if (!"".equals(errorMsg))
						Toast.show(mActivity, errorMsg);
					else
						Toast.show(mActivity, "注册失败");
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
					Toast.show(mActivity, "注册失败");
			}
		});
	}

	private void phoneRegister(final String phone, final String pass, String code) {
		showDialog("正在注册");
		BsOperationHub.instance().phoneRegister(phone, pass, code, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				hideDialog();
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "注册成功,请登录!");
					new DB_User(mActivity).setUserName(phone);
					new DB_User(mActivity).setUserPass(pass);
					RegisterActivity.this.finish();
				} else {
					String errorMsg = rspData.getErrorString();
					if (!"".equals(errorMsg))
						Toast.show(mActivity, errorMsg);
					else
						Toast.show(mActivity, "注册失败");
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
					Toast.show(mActivity, "注册失败");
			}
		});
	}

	// 隐藏输入法
	private void dismissKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_user.getWindowToken(), 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}
