package com.beiang.airdog.net.business.impl;

import com.beiang.airdog.net.business.BaseAcountOp;
import com.beiang.airdog.net.business.account.AuthCodePair;
import com.beiang.airdog.net.business.account.BindUserPair;
import com.beiang.airdog.net.business.account.EditUserPair;
import com.beiang.airdog.net.business.account.GetUserPair;
import com.beiang.airdog.net.business.account.LoginPair;
import com.beiang.airdog.net.business.account.LogoutPair;
import com.beiang.airdog.net.business.account.PhoneRegisterPair;
import com.beiang.airdog.net.business.account.RegisterPair;
import com.beiang.airdog.net.business.account.TrdLoginPair;
import com.beiang.airdog.net.business.account.UpdatePassByAuthPair;
import com.beiang.airdog.net.business.entity.UserEntity;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;

public class AccoutOperator implements BaseAcountOp {
	@Override
	public void register(String userName, String passwd, String info, ReqCbk<RspMsgBase> cbk) {
		RegisterPair pair = new RegisterPair();
		pair.sendRequest(userName, passwd, info, cbk);
	}

	@Override
	public void register(String phone, String passwd, String code, String info, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		PhoneRegisterPair pair = new PhoneRegisterPair();
		pair.sendRequest(phone, passwd, code, info, cbk);
	}

	@Override
	public void login(String userName, String passwd, ReqCbk<RspMsgBase> cbk) {
		LoginPair pair = new LoginPair();
		pair.sendRequest(userName, passwd, cbk);
	}

	@Override
	public void login(String channel, String account, String token, String info, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		TrdLoginPair pair = new TrdLoginPair();
		pair.sendRequest(channel, account, token, info, cbk);
	}

	@Override
	public void logout(ReqCbk<RspMsgBase> cbk) {
		LogoutPair pair = new LogoutPair();
		pair.sendRequest(cbk);
	}

	@Override
	public void getUser(String userId, String userName, ReqCbk<RspMsgBase> cbk) {
		GetUserPair pair = new GetUserPair();
		pair.sendRequest(userId, userName, cbk);
	}

	@Override
	public void editUser(UserEntity user, ReqCbk<RspMsgBase> cbk) {
		EditUserPair pair = new EditUserPair();
		pair.sendRequst(user, cbk);
	}

	@Override
	public void bindUser(String code, String phone, String userName, String password, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		BindUserPair pair = new BindUserPair();
		pair.sendRequest(code, phone, userName, password, cbk);
	}

	@Override
	public void authCode(String phone, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		AuthCodePair pair = new AuthCodePair();
		pair.sendRequest(phone, cbk);
	}

	@Override
	public void updatePass(String phone, String userName, String newPw, String code, ReqCbk<RspMsgBase> cbk) {
		// TODO Auto-generated method stub
		UpdatePassByAuthPair pair = new UpdatePassByAuthPair();
		pair.sendRequest(phone, userName, newPw, code, cbk);
	}
}
