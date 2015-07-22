package com.beiang.airdog.net.business;

import com.beiang.airdog.net.business.entity.UserEntity;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;

public interface BaseAcountOp {
	public void register(String userName, String passwd, String info, ReqCbk<RspMsgBase> cbk);

	public void register(String phone, String passwd, String code, String info, ReqCbk<RspMsgBase> cbk);

	public void login(String userName, String passwd, ReqCbk<RspMsgBase> cbk);

	public void login(String channel, String account, String token, String info, ReqCbk<RspMsgBase> cbk);

	public void logout(ReqCbk<RspMsgBase> cbk);

	public void getUser(String userId, String userName, ReqCbk<RspMsgBase> cbk);

	public void editUser(UserEntity user, ReqCbk<RspMsgBase> cbk);

	public void bindUser(String code, String phone, String userName, String password, ReqCbk<RspMsgBase> cbk);

	public void authCode(String phone, ReqCbk<RspMsgBase> cbk);

	public void updatePass(String phone, String userName, String newPw, String code, ReqCbk<RspMsgBase> cbk);

}
