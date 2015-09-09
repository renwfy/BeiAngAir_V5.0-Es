package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiang.airdog.db.DB_Device;
import com.beiang.airdog.db.DB_User;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.account.GetUserPair;
import com.beiang.airdog.net.business.account.LoginPair;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

/***
 * Description ：登录界面
 * 
 * @author Lsd
 * @Date 2014-03-28
 * 
 */

public class LoginActivity extends BaseMultiPartActivity implements OnItemClickListener {
	public static String ACTION = "action";
	DB_User user;
	EditText et_user, et_pass;
	TextView tv_login, tv_register, tv_remember, tv_forget, tv_thrlogin;
	ImageView iv_clear;
	
	LinearLayout unlogin_layout;
	LinearLayout login_layout;
	
	TextView tv_user,tv_user_id,tv_phone,tv_logout;
	
	String action = "";
	boolean canBack ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		setOnClickLisener();
		setOnItemClickListener(this);

		setMenuEnable(true);

		if (getIntent().hasExtra(ACTION)) {
			action = getIntent().getStringExtra(ACTION);
		}
		if ("nomal".equals(action)) {
			canBack = false;
		} else {
			canBack = true;
		}
		setSwipeBackEnable(canBack);

		initMenuData();
		initUserView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initUserAccount();
	}

	private void initView() {
		iv_clear = (ImageView) findViewById(R.id.iv_clear);
		et_user = (EditText) findViewById(R.id.et_user);
		et_pass = (EditText) findViewById(R.id.et_pass);
		tv_login = (TextView) findViewById(R.id.tv_login);
		tv_thrlogin = (TextView) findViewById(R.id.tv_thrlogin);
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_remember = (TextView) findViewById(R.id.tv_remember);
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		
		unlogin_layout = (LinearLayout) findViewById(R.id.unlogin_layout);
		login_layout = (LinearLayout) findViewById(R.id.login_layout);
		tv_user = (TextView) findViewById(R.id.tv_user);
		tv_user_id = (TextView) findViewById(R.id.tv_user_id);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_logout = (TextView) findViewById(R.id.tv_logout);
	}

	private void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("regist");
		menu.setMenu_name("注册");
		menu.setMenu_icon(R.drawable.ic_menu_acout);
		menus.add(menu);

		/*menu = new MenuEntity();
		menu.setMenu_key("bind");
		menu.setMenu_name("绑定账号");
		menu.setMenu_icon(R.drawable.ic_menu_bind);
		menus.add(menu);*/

		menu = new MenuEntity();
		menu.setMenu_key("about");
		menu.setMenu_name("关于我们");
		menu.setMenu_icon(R.drawable.ic_menu_about);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("versions");
		menu.setMenu_name("系统版本");
		menu.setMenu_icon(R.drawable.ic_menu_system);
		menus.add(menu);

		/*menu = new MenuEntity();
		menu.setMenu_key("guide");
		menu.setMenu_name("使用教程");
		menu.setMenu_icon(R.drawable.ic_menu_help);
		menus.add(menu);*/

		menu = new MenuEntity();
		menu.setMenu_key("logout");
		menu.setMenu_name("注销账号");
		menu.setMenu_icon(R.drawable.ic_menu_logout);
		menus.add(menu);
		prepareOptionsMenu(menus);
	}
	
	void initUserView(){
		if(user== null){
			user = new DB_User(mActivity);
		}
		
		//记住密码
		tv_remember.setSelected(user.isAutoLogin());
		
		final String userName = user.getUserName();
		final String pass = user.getUserPass();
		if ("nomal".equals(action)) {
			if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(pass) && user.isAutoLogin()){
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						login(userName, pass);
					}
				}, 100);
			}
		}else{
			unlogin_layout.setVisibility(View.GONE);
			login_layout.setVisibility(View.VISIBLE);
			tv_user.setText("用户名："+CurrentUser.instance().getUserName());
			tv_user_id.setText("用户ID："+CurrentUser.instance().getUserId());
			tv_phone.setText("手机号："+CurrentUser.instance().getPhone());
		}
	}

	private void initUserAccount() {
		if(user== null){
			user = new DB_User(mActivity);
		}
		String name = user.getUserName();
		String pass = user.getUserPass();

		et_user.setText(name);
		if(user.isAutoLogin()){
			et_pass.setText(pass);
		}else{
			et_pass.setText("");
		}
	}

	public void setOnClickLisener() {
		tv_login.setOnClickListener(listener);
		tv_register.setOnClickListener(listener);
		tv_forget.setOnClickListener(listener);
		tv_thrlogin.setOnClickListener(listener);

		iv_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_user.setText("");
			}
		});

		tv_remember.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tv_remember.isSelected()) {
					tv_remember.setSelected(false);
					user.setAutoLogin(false);
				} else {
					tv_remember.setSelected(true);
					user.setAutoLogin(true);
				}
			}
		});
		
		tv_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.show(mActivity, 0, "注销当前账号", true, new AlertDialogCallBack() {
					@Override
					public void onRight() {
						// TODO Auto-generated method stub
						logout();
					}

					@Override
					public void onLeft() {
						// TODO Auto-generated method stub
					}
				});
			}
		});
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_login:
				dismissKeyboard();

				signInInfo();
				break;
			case R.id.tv_register:
				startActivity(new Intent(mActivity, RegisterActivity.class));
				break;
			case R.id.tv_forget:
				startActivity(new Intent(mActivity, NewPassActivity.class));
				break;
			case R.id.tv_thrlogin:
				startActivity(new Intent(mActivity, TrdLoginActivity.class));
				break;
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		MenuEntity ety = (MenuEntity) parent.getAdapter().getItem(position);
		if ("logout".equals(ety.getMenu_key())) {
			AlertDialog.show(mActivity, 0, "注销当前账号", true, new AlertDialogCallBack() {
				@Override
				public void onRight() {
					// TODO Auto-generated method stub
					logout();
				}

				@Override
				public void onLeft() {
					// TODO Auto-generated method stub
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if ("nomal".equals(action) ){
			startActivity(new Intent(mActivity, ExitActivity.class));
			finish();
		} else {
			if(canBack){
				finish();
			}else{
				startActivity(new Intent(mActivity, ExitActivity.class));
				finish();
			}
		}
	}

	private void signInInfo() {
		// TODO Auto-generated method stub
		final String name = et_user.getText().toString();
		final String pass = et_pass.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Toast.show(mActivity, "请输入用户名");
			return;
		}
		if (TextUtils.isEmpty(pass)) {
			Toast.show(mActivity, "请输入密码");
			return;
		}

		login(name, pass);
	}

	/**
	 * 登陆
	 */
	private void login(final String name, final String pass) {
		showDialog("正在登陆");
		BsOperationHub.instance().login(name, pass, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					DB_User user = new DB_User(mActivity);
					user.setUserName(name);
					user.setUserPass(pass);
					
					LoginPair.RspLogin rsp = (LoginPair.RspLogin) rspData;
					if (rsp.data != null) {
						user.setUserId(rsp.data.userId);
						user.setUserToken(rsp.data.token);
					}

					//intentSkip();
					getUserInfo();
				} else {
					hideDialog();
					String errorMsg = rspData.getErrorString();
					if (!"".equals(errorMsg))
						Toast.show(mActivity, errorMsg);
					else
						Toast.show(mActivity, "登陆失败");
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
					Toast.show(mActivity, "登陆失败");
			}

		});
	}

	private void intentSkip() {
		hideDialog();

		startActivity(new Intent(mActivity, DeviceActivity.class));
		LoginActivity.this.finish();
	}

	private void getUserInfo() {
		BsOperationHub.instance().getUser(CurrentUser.instance().getUserId(), CurrentUser.instance().getUserName(), new ReqCbk<RspMsgBase>(){
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					GetUserPair.RspGetUser rsp = (GetUserPair.RspGetUser) rspData;
					String phone = rsp.data.phone;
					if(!TextUtils.isEmpty(phone)){
						intentSkip();
					}else{
						startActivity(new Intent(mActivity, BindUserActivity.class));
						finish();
					}
				}
			}

			@Override
			public void onFailure(com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void logout() {
		showDialog("正在注销");
		BsOperationHub.instance().logout(new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				hideDialog();
				if (rspData.isSuccess()) {
					logOutOprate();
					return;
				}
				String errStr = rspData.getErrorString();
				if ("".equals(errStr)) {
					Toast.show(mActivity, errStr);
					return;
				}
				Toast.show(mActivity, "注销失败");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				hideDialog();
				String s = err.getErrorString();
				if (!"".endsWith(s)) {
					Toast.show(mActivity, s);
					return;
				}
				Toast.show(mActivity, "注销失败");
			}

		});
	}

	private void logOutOprate() {
		// new DB_User(mActivity).setUserName("");
		new DB_User(mActivity).setUserPass("");
		new DB_User(mActivity).setUserId("");
		new DB_User(mActivity).setUserToken("");
		
		new DB_Device(mActivity).cleanDevData();
		Toast.show(mActivity, "注销成功");
		canBack = false;
		setSwipeBackEnable(canBack);
		initUserAccount();
		
		unlogin_layout.setVisibility(View.VISIBLE);
		login_layout.setVisibility(View.GONE);
	}

	// 隐藏输入法
	private void dismissKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_pass.getWindowToken(), 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}
}