package com.beiang.airdog.net.httpcloud.aync;

import com.android.volley.Request.Method;

public class ServerDefinition {
	public static int API_ID = 1;
	public final static double API_VERSION = 1.0;
	public final static double API_JSONRPC = 2.0;
	//forrelease
	//public static final String API_KEY = "34b9a684f1fd61194026d92b7cf2a11c";
	//public static final String API_SECRET = "dc52bdb7601eafb7fa580e000f8d293f";
	//public static final String IP_ADDRESS_EXT = "http://p.smart.99.com";
	
	
	//fortest
	public final static String API_KEY = "c2674528e7fab0e856d9b4a563168f19";
	public final static String API_SECRET = "02a2025b903e585efff6b2fe73b15675";
	public static final String IP_ADDRESS_EXT = "http://121.207.243.132";
	
	

	public static String getSmarthomeHostUrl() {
		return IP_ADDRESS_EXT;
	}

	public static final int HTTP_METHOD = Method.POST;

	public static final String API_SERVER_VERSION = "v1/"; 

	public static class APIServerAdrs {
		public final static String ACCOUNT = "v1/account";
		public final static String HOMER = "v1/homer";
		public final static String IHOMER = "v2/homer";//智能云家庭
	}

	public final static int SERVER_RETURN_OK = 0;
	public final static int SERVER_RETURN_SUCCESS = 200;
	public final static int SERVER_UNKNOW_ERROR = -1;

	public static class GeneralErr {
		public final static int COOKIE_ERROR = 100;
		public final static int ALREADY_EXIST = 101;
		public final static int PARAMETER_ERROR = 102;
	}

	public static class BusinessErr {
		public final static int DEVICE_BINDED_OTHER = 611;
		public final static int DEVICE_BINDED = 2102;
	}

	public static class APIErr {
		public final static int UNKNOW_ERROR = 1000;
		public final static int SEND_CMD_FAIL = 1003;
		public final static int GET_DEV_INFO_FAIL = 1004;
		public final static int DEV_UNREACHABLE = 1005;
		public final static int RSPONSE_DATA_ERROR = 1006;

		public final static int MESSAGE_SEND_ERROR = 5000;
		public final static int MESSAGE_SENDED = 5001;
		public final static int FORMAT_PHONE_ERROR = 5002;
		public final static int TOKEN_ERROR = 5003;
		public final static int USER_NAME_ERROR = 5004;
		public final static int PASSWORLD_ERROR = 5005;
		public final static int USER_NAME_NOT_EXIST = 5006;
		public final static int USER_NOT_LOGIN = 5007;
		public final static int POST_DATA_ERROR = 5008;
		public final static int SYSTEM_ERROR = 5009;
		public final static int UNBIND_PHONE_ERROR = 5010;
		public final static int FORMAT_PASS_ERROR = 5011;
		public final static int USER_AND_PHONE_NOT = 5012;
		public final static int SAME_PASSWORLD = 5013;
		public final static int PHONE_BINDED = 5014;
		public final static int CODE_NOT_SEND = 5015;
		public final static int CODE_ERROR = 5016;
		public final static int USER_LOGINED = 5017;
		public final static int FORMAT_USER_NAME_ERROR = 5018;
		public final static int MOTHD_NOT_EXIXT = 5019;
		public final static int USER_NAME_IS_EXIXTED = 5020;
		public final static int DEVICE_NOT_EXIXT = 5021;
		public final static int THIRD_LOGIN_NOT_EXIXT = 5022;
		public final static int THIRD_LOGIN_FAILD = 5023;

		public final static int COMMOND_TIMEOUT = 6000;
		public final static int DEVICE_OUTLINE = 6001;
		public final static int TOKEN_PASSTIME = 6002;
		public final static int SYSTEM_HOLD_ERROR = 6003;
		public final static int AUTH_ERROR = 6004;
		public final static int SINGE_ERROR = 6005;
		public final static int SIGLE_PASSTIME = 6006;
		
		public final static int DEVIDE_EXIST_HOMER = 3017;

	}

	public static enum APIServerMethod {
		ACCOUNT_AuthCode("authCode"),

		ACCOUNT_UpdateUserInfo("updateUserInfo"),

		ACCOUNT_GetUserInfo("getUserInfo"),

		ACCOUNT_BindUser("bindUser"),

		ACCOUNT_Login("login"),

		ACCOUNT_Logout("logout"),

		ACCOUNT_PhoneRegister("phoneRegister"),

		ACCOUNT_Register("register"),

		ACCOUNT_ThdLogin("3rdLogin"),

		ACCOUNT_UpdatePassByAuth("updatePassword2"),

		HOMER_Authorize("authorize"),

		HOMER_UpdateAuthorize("updateAuthorize"),

		HOMER_Bind("bind"),

		HOMER_Command("command"),

		HOMER_UpdateDeviceInfo("updateDeviceInfo"),

		HOMER_GetDevice("getDevice"),

		HOMER_GetIp("getIp"),

		HOMER_GetBind("getBind"),

		HOMER_GetBindResult("getBindResult"),

		HOMER_GetDeviceData("getDeviceData"),

		HOMER_GetDeviceStatus("getDeviceStatus"),

		HOMER_ResetDevice("resetDevice"),

		HOMER_Unbind("unbind"), 
		
		HOMER_QueryWeather("queryWeather"), 
		
		IHOMER_AddHome("ihome_addHome"), 
		
		IHOMER_GetHome("ihome_getHome"),
		
		IHOMER_DelHome("ihome_delHome"),
		
		IHOMER_Command("ihome_command"),
		
		IHOMER_GetScript("ihome_getScript"),
		
		IHOMER_AddScript("ihome_addScript"),
		
		IHOMER_UpdateScript("ihome_updateScript"),
		
		IHOMER_DelScript("ihome_delScript"),
		
		IHOMER_GetTimer("ihome_getTimer"),
		
		IHOMER_AddTimer("ihome_addTimer"),
		
		IHOMER_UpdateTimer("ihome_updateTimer"),
		
		IHOMER_DelTimer("ihome_delTimer"),
		
		IHOMER_AddDevice("ihome_addDevice"),
		
		IHOMER_UpdateDevice("ihome_updateDevice"),
		
		IHOMER_GetDevice("ihome_getDevice"),
		
		IHOMER_DelDevice("ihome_delDevice"),
		
		IHOMER_AddUser("ihome_addUser"),
		
		IHOMER_GetUser("ihome_getUser"),
		
		IHOMER_DelUser("ihome_delUser"),
		
		IHOMER_SetMonitor("ihome_setMaster"),
		
		IHOMER_GetMaster("ihome_getMaster"),
		
		IHOMER_UpdateRoom("ihome_updateRoom"),
		
		IHOMER_GetRoom("ihome_getRoom"),
		
		IHOMER_DelRoom("ihome_delRoom"),
		
		IHOMER_AddRoom("ihome_addRoom"),
		
		IHOMER_GetData("ihome_getData"),

		IHOMER_DelData("ihome_delData"),

		IHOMER_SetData("ihome_setData"),
		;
		

		String method = "";

		private APIServerMethod(String method) {
			this.method = method;
		}

		public String getMethod() {
			return this.method;
		}
	}

}
