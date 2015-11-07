package com.beiang.airdog.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.beiang.airdog.db.DB_User;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.activity.BindUserActivity;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;

public class ConfigUtils {
	
	public static void checkBind(final Context context) {
		String phone = CurrentUser.instance().getPhone();
		if (!TextUtils.isEmpty(phone)) {
			return;
		}
		final DB_User db = new DB_User(context);
		if (!db.getBindTip()) {
			return;
		}
		BsOperationHub.instance().getUser(CurrentUser.instance().getUserId(), CurrentUser.instance().getUserName(),
				new ReqCbk<RspMsgBase>() {
					@Override
					public void onSuccess(RspMsgBase rspData) {
						// TODO Auto-generated method stub
						if (rspData.isSuccess()) {
							String phone = CurrentUser.instance().getPhone();
							if (TextUtils.isEmpty(phone)) {
								db.setBindTip();
								AlertDialog.show(context, 0, "绑定手机号\n手机号可以用于找回密码，是否绑定？", true, new AlertDialogCallBack() {
									@Override
									public void onLeft() {
										// TODO Auto-generated method stub
									}

									@Override
									public void onRight() {
										// TODO Auto-generated method stub
										((Activity)context).startActivity(new Intent(context, BindUserActivity.class));
									}
								});
							}
						}
					}

					@Override
					public void onFailure(ErrorObject err) {
						// TODO Auto-generated method stub
					}
				});
	}
	
	/***
	 * 友盟自动更新
	 */
	public static void autoUpdate(Context context) {
		// TODO Auto-generated method stub
		UmengUpdateAgent.setUpdateCheckConfig(false);
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		// UmengUpdateAgent.silentUpdate(this); 后台自动下载
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.update(context);
	}
	
	/***
	 * 更新回调
	 * 
	 * @param context
	 * @param listener
	 */
	public static void update(Context context,UmengUpdateListener listener){
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateCheckConfig(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(listener);
		UmengUpdateAgent.update(context);
	}

}
