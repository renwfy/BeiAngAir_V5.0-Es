package com.beiang.airdog.net.httpcloud.aync;

import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.APIErr;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorCause;

public class ErrorDefinition {
	public static String errorMsg(int code) {
		switch (code) {
		case ServerDefinition.SERVER_RETURN_OK:
		case ServerDefinition.SERVER_RETURN_SUCCESS:
			return "操作成功";
		case APIErr.MESSAGE_SEND_ERROR:
			return "短信发送失败";
		case APIErr.MESSAGE_SENDED:
			return "短信已发送";
		case APIErr.FORMAT_PHONE_ERROR:
			return "手机号码格式不对";
		case APIErr.TOKEN_ERROR:
			return "token错误";
		case APIErr.USER_NAME_ERROR:
			return "账号或密码错误";
		case APIErr.PASSWORLD_ERROR:
			return "账号或密码错误";
		case APIErr.USER_NAME_NOT_EXIST:
			return "用户名不存在";
		case APIErr.USER_NOT_LOGIN:
			return "用户未登陆";
		case APIErr.UNBIND_PHONE_ERROR:
			return "手机号未绑定";
		case APIErr.FORMAT_PASS_ERROR:
			return "密码格式错误";
		case APIErr.USER_AND_PHONE_NOT:
			return "用户名与手机号不符";
		case APIErr.SAME_PASSWORLD:
			return "密码相同";
		case APIErr.PHONE_BINDED:
			return "该手机号已经绑定过";
		case APIErr.CODE_NOT_SEND:
			return "验证码获取失败,请重新获取";
		case APIErr.CODE_ERROR:
			return "验证码错误";
		case APIErr.USER_LOGINED:
			return "用户已登陆";
		case APIErr.FORMAT_USER_NAME_ERROR:
			return "用户名格式不对";
		case APIErr.USER_NAME_IS_EXIXTED:
			return "用户名已存在";
		case APIErr.DEVICE_NOT_EXIXT:
			return "设备不存在";
		case APIErr.THIRD_LOGIN_NOT_EXIXT:
			return "第三方登陆不存在";
		case APIErr.THIRD_LOGIN_FAILD:
			return "第三方登陆失败";
		case APIErr.COMMOND_TIMEOUT:
			return "控制超时";
		case APIErr.DEVIDE_EXIST_HOMER:
			return "设备在家中，请先移除";
		case APIErr.DEVICE_OUTLINE:
			return "设备不在线";
		default:
			return "";
		}
	}

	public static String errorMsg(ErrorCause cause) {
		switch (cause) {
		case NETWORK_TIMEOUT:
			return "网络连接超时";
		case NETWORK_UNAVAILABLE:
			return "网络连接异常";
		case BUSINESS_RESPONSE_CODE_ERROR:
			return "获取数据错误";
		case UN_LOGIN:
			return "你还没有登陆";
		case LOGINED:
			return "你已经登陆";
		default:
			return "";
		}
	}

}
