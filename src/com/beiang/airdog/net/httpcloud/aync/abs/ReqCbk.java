package com.beiang.airdog.net.httpcloud.aync.abs;

import com.beiang.airdog.net.httpcloud.aync.ErrorDefinition;

public abstract class ReqCbk<T> {
	
	public static enum ErrorCause {
		/**
		 * 网络不可用
		 */
		NETWORK_UNAVAILABLE,
		/**
		 * 网络连接超时
		 */
		NETWORK_TIMEOUT,
		/**
		 * HTTP返回码错误
		 */
		HTTP_RESPONSE_CODE_ERROR,

		/**
		 * 业务返回码错误
		 */
		BUSINESS_RESPONSE_CODE_ERROR,

		/**
		 * 未登录
		 */
		UN_LOGIN,
		
		/**
		 * 已登录
		 */
		LOGINED,

		/**
		 * 未知错误
		 */
		UNKNOWN_ERROR;
	}
	
	public static class ErrorObject {
		public ErrorCause cause;
		public int errorCode;
		public String errorMsg;
		
		public ErrorObject() {
			cause = ErrorCause.UNKNOWN_ERROR;
			errorMsg = "unknow error";
		}
		
		public String getErrorString() {
			return ErrorDefinition.errorMsg(cause);
		}
	}
	public abstract void onSuccess(T rspData);
	
	public abstract void onFailure(ErrorObject err);	
}
