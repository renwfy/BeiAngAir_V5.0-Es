package com.beiang.airdog.constant;

import com.beiang.airdog.constant.Constants.Network;

public class Config {
	/** APP路径 */
	public static String FULL_APP_PATH;
	/** 在SharedPre保存是否第一次启动 */
	public static final String FIRST_START = "tag_first_start";

	/** 调试Log */
	public static boolean DEBUG = true;
	
	/** 网络模式 */
	public static Network NET = Network.SERVER;
	
	/**服务器通讯超时时间*/
	public static int NET_TIMEOUT = 5500;
	
	/**查找设备超时时间*/
	public static int SEARCH_TIMEOUT = 60000;
	
}
