package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

/***
 * Description ：绑定手机号
 * 
 * @author Lsd
 * @Date 2014-03-28
 * 
 */

public class BindUserActivity extends BaseMultiPartActivity {
	EditText et_phone, et_vercode;
	ImageView phone_clear;
	TextView tv_bind, get_vercode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binduser);

		// 设置菜单
		setSwipeBackEnable(false);
		initMenuData();
				
		initView();
	}
	
	private void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("acout");
		menu.setMenu_name("更换账号");
		menu.setMenu_icon(R.drawable.ic_menu_acout);
		menus.add(menu);

		prepareOptionsMenu(menus);
	}

	private void initView() {
		// TODO Auto-generated method stub
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_vercode = (EditText) findViewById(R.id.et_vercode);
		get_vercode = (TextView) findViewById(R.id.get_vercode);
		tv_bind = (TextView) findViewById(R.id.tv_bind);

		get_vercode.setOnClickListener(listener);
		tv_bind.setOnClickListener(listener);
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
			case R.id.tv_bind:
				dismissKeyboard();
				bindUserInfo();
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

	private void bindUserInfo() {
		String phone = et_phone.getText().toString();
		String authCode = et_vercode.getText().toString();

		if (TextUtils.isEmpty(authCode)) {
			Toast.show(mActivity, "请输入验证码");
			return;
		}
		bindUser(phone, authCode);
	}

	private void bindUser(final String phone, String code) {
		showDialog("正在绑定手机号");
		String userName = CurrentUser.instance().getUserName();
		String pass = CurrentUser.instance().getPasswd();
		if(TextUtils.isEmpty(pass)){
			pass = "beiang";//默认密码
		}
		BsOperationHub.instance().bindUser(code, phone, userName, pass, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				hideDialog();
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "绑定成功");
					CurrentUser.instance().setPhone(phone);
					
					intentSkip();
				} else {
					String errorMsg = rspData.getErrorString();
					if (!"".equals(errorMsg))
						Toast.show(mActivity, errorMsg);
					else
						Toast.show(mActivity, "绑定失败");
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
					Toast.show(mActivity, "绑定失败");
			}
		});
	}
	
	private void intentSkip() {
		startActivity(new Intent(mActivity, IHomerActivity.class));
		finish();
	}

	// 隐藏输入法
	private void dismissKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_phone.getWindowToken(), 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}
