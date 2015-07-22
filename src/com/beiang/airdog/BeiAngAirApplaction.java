package com.beiang.airdog;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaApplication;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.ui.ActivityManager;
import com.beiang.airdog.utils.FileUtils;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.Settings;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class BeiAngAirApplaction extends FrontiaApplication {
	public static BeiAngAirApplaction applaction;
	public static IWXAPI api;
	
	@Override
	public void onCreate() {
		super.onCreate();
		applaction = this;
		
		init();
	}
	
	public static BeiAngAirApplaction getInstance() {
		return applaction;
	}
	public void init() {
		//微信注册
		String appId = "wx037f301a0ec34f84";
		api = WXAPIFactory.createWXAPI(this, appId,true);
		api.registerApp(appId);
		
		//初始化百度分享
		Frontia.init(this.getApplicationContext(), "SMT9pXGos5t0ZR7mMlcVMGlx");
		
		//初始化屏幕大小
		Settings.P_HEIGHT = getResources().getDisplayMetrics().heightPixels;
		Settings.P_WIDTH = getResources().getDisplayMetrics().widthPixels;

		//初始化文件夹
		FileUtils.makedirs();
	}

	// 退出应用程序
	public void exit() {
		ActivityManager.getScreenManager().popAllActivity();
		onTerminate();
	}
	
	@Override
	public void onTerminate() {
		LogUtil.i("onTerminate");
		CurrentUser.instance().clean();
		CurrentDevice.instance().clean();
		super.onTerminate();
	}
	
}