package com.beiang.airdog.net.business.entity;

public class CurrentUser {
	private String userName;
	private String passwd;
	private String email;
	private boolean verified;
	private String userId;
	private String token;
	private String cookie;
	private boolean login;
	private boolean autoLogin;
	private String phone;
	private String sex;
	private String nickName;

	private static CurrentUser cUser;

	private CurrentUser() {
	}

	synchronized public static CurrentUser instance() {
		if (cUser == null) {
			synchronized (CurrentUser.class) {
				if (cUser == null) {
					cUser = new CurrentUser();
				}
			}
		}
		return cUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(boolean autoLogin) {
		this.autoLogin = autoLogin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public void clean(){
		if(cUser != null){
			cUser = null;
		}
	}
	
	
}
