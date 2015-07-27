package com.beiang.airdog.net.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.beiang.airdog.BeiAngAirApplaction;
import com.beiang.airdog.constant.Config;
import com.beiang.airdog.constant.Constants;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.db.DB_Device;
import com.beiang.airdog.net.business.account.EditUserPair;
import com.beiang.airdog.net.business.account.GetUserPair;
import com.beiang.airdog.net.business.account.LoginPair;
import com.beiang.airdog.net.business.account.RegisterPair;
import com.beiang.airdog.net.business.account.TrdLoginPair;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.entity.UserEntity;
import com.beiang.airdog.net.business.homer.QueryBindedListPair;
import com.beiang.airdog.net.business.homer.QueryBindedResultPair;
import com.beiang.airdog.net.business.homer.QueryDevStatusPair;
import com.beiang.airdog.net.business.impl.AccoutOperator;
import com.beiang.airdog.net.business.impl.BsOperator;
import com.beiang.airdog.net.business.impl.IHomerOperator;
import com.beiang.airdog.net.httpcloud.aync.ServerDefinition.BusinessErr;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorCause;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk.ErrorObject;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.TimerUtil;

public class BsOperationHub {
	private static BsOperationHub sOperator;

	private BaseAcountOp mAccoutOp;
	private BaseBsOp mBsOp;
	private BaseCloudIHomer mHomer;

	synchronized public static BsOperationHub instance() {
		if (sOperator == null) {
			sOperator = new BsOperationHub();
		}
		return sOperator;
	}

	private BsOperationHub() {
		mAccoutOp = new AccoutOperator();
		mBsOp = new BsOperator();
		mHomer = new IHomerOperator();
	}

	public void phoneRegister(final String phone, final String passwd, String code, String info, final ReqCbk<RspMsgBase> cloudCbk) {
		mAccoutOp.register(phone, passwd, code, info, cloudCbk);
	}

	public void register(final String userName, final String passwd, String info, final ReqCbk<RspMsgBase> cloudCbk) {
		ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				if (rspData.isSuccess()) {
					RegisterPair.RspRegister rsp = (RegisterPair.RspRegister) rspData;
					if (rsp.data != null) {
						CurrentUser.instance().setUserName(userName);
						CurrentUser.instance().setPasswd(passwd);
						CurrentUser.instance().setVerified(true);
						CurrentUser.instance().setUserId(rsp.data.userId);
					}
				}
				cloudCbk.onSuccess(rspData);
			}

			@Override
			public void onFailure(ErrorObject err) {
				cloudCbk.onFailure(err);
			}
		};
		mAccoutOp.register(userName, passwd, info, cbk);
	}

	public void login(final String userName, final String passwd, final ReqCbk<RspMsgBase> cloudCbk) {
		ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				if (rspData.isSuccess()) {
					LoginPair.RspLogin rsp = (LoginPair.RspLogin) rspData;
					if (rsp.data != null) {
						CurrentUser.instance().setLogin(true);
						CurrentUser.instance().setVerified(true);
						CurrentUser.instance().setUserId(rsp.data.userId);
						CurrentUser.instance().setToken(rsp.data.token);
						CurrentUser.instance().setCookie(rsp.data.cookie);
						CurrentUser.instance().setUserName(userName);
						CurrentUser.instance().setPasswd(passwd);
					}
				}
				cloudCbk.onSuccess(rspData);
			}

			@Override
			public void onFailure(ErrorObject err) {
				cloudCbk.onFailure(err);
			}
		};
		if (CurrentUser.instance().isLogin()) {
			ErrorObject err = new ErrorObject();
			err.cause = ErrorCause.LOGINED;
			cloudCbk.onFailure(err);
			return;
		}
		mAccoutOp.login(userName, passwd, cbk);
	}

	/** 第三方登陆 */
	public void thdLogin(String channel, String account, String token, String info, final ReqCbk<RspMsgBase> cloudCbk) {
		ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				if (rspData.isSuccess()) {
					TrdLoginPair.RspTrdLogin rsp = (TrdLoginPair.RspTrdLogin) rspData;
					if (rsp.data != null) {
						CurrentUser.instance().setLogin(true);
						CurrentUser.instance().setVerified(true);
						CurrentUser.instance().setUserId(rsp.data.userId);
						CurrentUser.instance().setToken(rsp.data.token);
						CurrentUser.instance().setCookie(rsp.data.cookie);
					}
				}
				cloudCbk.onSuccess(rspData);
			}

			@Override
			public void onFailure(ErrorObject err) {
				cloudCbk.onFailure(err);
			}
		};
		mAccoutOp.login(channel, account, token, info, cbk);
	}

	public void logout(final ReqCbk<RspMsgBase> cloudCbk) {
		//if (CurrentUser.instance().isLogin()) {
			ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
				@Override
				public void onSuccess(RspMsgBase rspData) {
					if (rspData.isSuccess()) {
						CurrentUser.instance().setLogin(false);
						CurrentUser.instance().setToken("");
						CurrentUser.instance().setUserId("");
						// CurrentUser.instance().setUserName("");
						CurrentUser.instance().setPasswd("");
						
						CurrentDevice.instance().devList = null;
					}
					cloudCbk.onSuccess(rspData);
				}

				@Override
				public void onFailure(ErrorObject err) {
					cloudCbk.onFailure(err);
				}
			};
			mAccoutOp.logout(cbk);
		/*} else {
			ErrorObject err = new ErrorObject();
			err.cause = ErrorCause.UN_LOGIN;
			cloudCbk.onFailure(err);
		}*/
	}

	public void getUser(String userId, String userName, final ReqCbk<RspMsgBase> cloudCbk) {
		ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				if (rspData.isSuccess()) {
					GetUserPair.RspGetUser rsp = (GetUserPair.RspGetUser) rspData;
					if (rsp.data != null) {
						CurrentUser.instance().setNickName(rsp.data.nickName);
						CurrentUser.instance().setEmail(rsp.data.email);
						CurrentUser.instance().setPhone(rsp.data.phone);
					}
				}
				if (cloudCbk != null)
					cloudCbk.onSuccess(rspData);
			}

			@Override
			public void onFailure(ErrorObject err) {
				if (cloudCbk != null)
					cloudCbk.onFailure(err);
			}
		};
		mAccoutOp.getUser(userId, userName, cbk);
	}

	public void editUser(UserEntity user, final ReqCbk<RspMsgBase> cloudCbk) {
		if (user.getUserId() == CurrentUser.instance().getUserId()) {
			ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
				@Override
				public void onSuccess(RspMsgBase rspData) {
					if (rspData.isSuccess()) {
						EditUserPair.RspEditUser rsp = (EditUserPair.RspEditUser) rspData;
						if (rsp.data != null) {
							CurrentUser.instance().setUserName(rsp.data.userName);
							CurrentUser.instance().setEmail(rsp.data.email);
						}
					}
					cloudCbk.onSuccess(rspData);
				}

				@Override
				public void onFailure(ErrorObject err) {
					cloudCbk.onFailure(err);
				}
			};
			mAccoutOp.editUser(user, cbk);
		} else {
			mAccoutOp.editUser(user, cloudCbk);
		}
	}

	/** 绑定用户 */
	public void bindUser(String code, String phone, String userName, String password, ReqCbk<RspMsgBase> cbk) {
		mAccoutOp.bindUser(code, phone, userName, password, cbk);
	}

	/*** 获取短信授权码 */
	public void authCode(String phone, ReqCbk<RspMsgBase> cloudCbk) {
		mAccoutOp.authCode(phone, cloudCbk);
	}

	/** 通过短信验证设置新密码 */
	public void updatePassByAuth(String phone, String userName, String newPass, String code, ReqCbk<RspMsgBase> cloudCbk) {
		mAccoutOp.updatePass(phone, userName, newPass, code, cloudCbk);
	}

	/*** 获取设备信息，通过设备id */
	public void getDeviceById(String ownerId, ReqCbk<RspMsgBase> cloudCbk) {
		mBsOp.getDevice(ownerId, cloudCbk);
	}

	/*** 绑定设备，通过设备信息 */
	public void bindDevice(DevEntity dev, ReqCbk<RspMsgBase> cloudCbk) {
		mBsOp.bindDevice(dev, cloudCbk);
	}

	/*** 解除绑定设备 */
	public void unbindDevice(String devId, ReqCbk<RspMsgBase> cloudCbk) {
		mBsOp.unbindDevice(devId, cloudCbk);
	}

	/** 复位设备 */
	public void resetDevice(String devId, ReqCbk<RspMsgBase> cloudCbk) {
		mBsOp.resetDevice(devId, cloudCbk);
	}

	/** 查询绑定设备绑定结果 */
	public void queryBindDev(final String userId, final String bindCode, final ReqCbk<RspMsgBase> cloudCbk) {
		queryBindDevice(userId, bindCode, cloudCbk);
	}

	/*** 查询绑定列表 */
	public void queryBindList(String userId, final ReqCbk<RspMsgBase> cloudCbk) {
		ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				List<DevEntity> list = null;
				if (rspData.isSuccess()) {
					QueryBindedListPair.RspQueryBindedList rsp = (QueryBindedListPair.RspQueryBindedList) rspData;
					if (rsp.data != null && rsp.data.size() > 0) {
						list = matchList(rsp.data);
					}
				}

				CurrentDevice.instance().devList = list;
				
				if(cloudCbk != null){
					cloudCbk.onSuccess(rspData);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				if(cloudCbk != null){
					cloudCbk.onFailure(err);
				}
			}
		};
		mBsOp.queryBindList(userId, cbk);
	}

	/** 获取ip */
	public void queryDevIp(String devIp, ReqCbk<RspMsgBase> cloudCbk) {
		mBsOp.queryDevIp(devIp, cloudCbk);
	}

	/** 查询天气 */
	public void queryWeather(String position, ReqCbk<RspMsgBase> cbk) {
		mBsOp.queryWeather(position, cbk);
	}

	/** 编辑设备信息 */
	public void editDevInfo(String devId, String nickname, ReqCbk<RspMsgBase> cbk) {
		mBsOp.editDevInfo(devId, nickname, cbk);
	}

	/** 查询硬件数据 (beiang_firmware) */
	public void queryDevFirmware(DevEntity entity, String key, ReqCbk<RspMsgBase> cloudCbk) {
		mBsOp.queryDevData(entity, key, cloudCbk);
	}

	/** 查询设备数据 (beiang_status) */
	/*public void queryDevData(DevEntity entity, String key, final ReqCbk<RspMsgBase> cloudCbk) {
		ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				if (rspData.isSuccess()) {
					QueryDevDataPair.RspQueryDevData rsp = (QueryDevDataPair.RspQueryDevData) rspData;
					if (rsp.data != null) {
						DevEntity dt = new DevEntity();
						byte[] devValues = null;
						try {
							devValues = Base64.decode(rsp.reply, Base64.DEFAULT);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (devValues != null && devValues.length == 25) {
							dt.airInfo = EParse.parseEairByte(devValues);
						}

						if (dt.airInfo != null) {
							dt.devType = dt.airInfo.getDeviceType();
						}

						CurrentDevice.instance().curDevice = dt;
						cloudCbk.onSuccess(rspData);
						return;
					}
				}
				cloudCbk.onFailure(getErrorObj(ErrorCause.BUSINESS_RESPONSE_CODE_ERROR, 0, "data error!"));
			}

			@Override
			public void onFailure(ErrorObject err) {
				cloudCbk.onFailure(err);
			}
		};

		if (!"beiang_status".equals(key)) {
			key = "beiang_status";
		}
		mBsOp.queryDevData(entity, key, cbk);
	}*/

	/** 查询设备状态 */
	public void queryDevStatus(String devId, String ndevSn, final ReqCbk<RspMsgBase> cloudCbk) {
		ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				if (rspData.isSuccess()) {
					QueryDevStatusPair.RspQueryDevStatus rsp = (QueryDevStatusPair.RspQueryDevStatus) rspData;
					if (rsp.data != null) {
						QueryDevStatusPair.RspQueryDevStatus.Data rspDev = rsp.data;
						DevEntity dt = new DevEntity();
						dt.devId = rspDev.devId;
						dt.product = rspDev.product;
						dt.devInfo = rspDev.devInfo;
						dt.deviceSn = rspDev.deviceSn;
						dt.module = rspDev.mod;
						dt.status = rspDev.status;
						dt.role = rspDev.role;
						dt.deviceSn = rspDev.deviceSn;
						dt.value = rspDev.value;
						byte[] devValues = null;
						try {
							devValues = Base64.decode(rspDev.value, Base64.DEFAULT);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (devValues != null && devValues.length == 25) {
							dt.airInfo = EParse.parseEairByte(devValues);
						}

						if (dt.airInfo != null) {
							dt.devType = dt.airInfo.getDeviceType();
						}

						CurrentDevice.instance().queryDevice = dt;
						cloudCbk.onSuccess(rspData);
						return;
					}
				}

				cloudCbk.onFailure(getErrorObj(ErrorCause.BUSINESS_RESPONSE_CODE_ERROR, 0, "data error!"));
			}

			@Override
			public void onFailure(ErrorObject err) {
				cloudCbk.onFailure(err);
			}
		};
		mBsOp.queryDevStatus(devId, ndevSn, cbk);
	}

	/*** 发送控制命令 */
	public void sendCtrlCmd(String devId, byte[] cmd, ReqCbk<RspMsgBase> cbk) {
		mBsOp.sendCtrlCmd(devId, cmd, cbk);
	}

	/** 授权 */
	public void Authrorize(DevEntity entity, ReqCbk<RspMsgBase> cbk) {
		mBsOp.Authrorize(entity, cbk);
	}

	/** 更新授权、昵称等信息 */
	public void upDateAuthrorize(DevEntity entity, String userId, ReqCbk<RspMsgBase> cbk) {
		mBsOp.upDateAuthrorize(entity, userId, cbk);
	}
	
	/**创建家庭*/
	public void addHomer(String name, ReqCbk<RspMsgBase> cbk){
		mHomer.addHome(name, cbk);
	}
	
	/**获取家庭*/
	public void getHomer(ReqCbk<RspMsgBase> cbk){
		mHomer.getHome(cbk);
	}
	
	/**删除家庭*/
	public void delHomer(long home_id,ReqCbk<RspMsgBase> cbk){
		mHomer.delHome(home_id, cbk);
	}
	
	/**家庭控制*/
	public void homeSendCmd(long home_id, long device_id, String device_sn, byte[] cmd, ReqCbk<RspMsgBase> cbk){
		mHomer.sendHomeCmd(home_id, device_id, device_sn, cmd, cbk);
	}
	
	/**添加定时器*/
	public void homeAddTime(long home_id, long device_id, String device_sn, int repeat, long secs, List<Integer> week, byte[]cmd,
			ReqCbk<RspMsgBase> cbk) {
		mHomer.addTime(home_id, device_id, device_sn, repeat, secs, week, cmd, cbk);
	}

	public void homeGetTime(long home_id, long timer_id, ReqCbk<RspMsgBase> cbk) {
		mHomer.getTime(home_id, timer_id, cbk);
	}

	/**更新定时器*/
	public void homeUpdateTime(long home_id, long timer_id,long device_id, String device_sn, int repeat, long secs, List<Integer> week, byte[] cmd,ReqCbk<RspMsgBase> cbk) {
		mHomer.updateTime(home_id, timer_id,device_id, device_sn, repeat, secs, week, cmd,cbk);
	}

	/**删除定时器*/
	public void homeDelTime(long home_id, long timer_id, ReqCbk<RspMsgBase> cbk) {
		mHomer.delTime(home_id, timer_id, cbk);
	}

	/**添加规则*/
	public void homeAddScript(long home_id, long device_id, String device_sn, int type, int repeat, long secs, List<Integer> week,
			String user_time, String factor, String msg, ReqCbk<RspMsgBase> cbk) {
		mHomer.addScript(home_id, device_id, device_sn, type, repeat, secs, week, user_time, factor, msg, cbk);
	}

	/**获取规则*/
	public void homeGetScript(long home_id, long script_id, ReqCbk<RspMsgBase> cbk) {
		mHomer.getScript(home_id, script_id, cbk);
	}

	/**更新规则*/
	public void homeUpdateScript(long home_id, long device_id,long script_id, String device_sn, int type, int repeat, long secs, List<Integer> week,
			String user_time, String factor, String msg, ReqCbk<RspMsgBase> cbk) {
		mHomer.updateScript(home_id, device_id,script_id, device_sn, type, repeat, secs, week, user_time, factor, msg, cbk);
	}

	/**删除规则*/
	public void homeDelScript(long home_id, long script_id, ReqCbk<RspMsgBase> cbk) {
		mHomer.delScript(home_id, script_id, cbk);
	}
	
	/**添加设备到家庭*/
	public void homeAddDevice(long home_id, long device_id, String device_sn, String device_info, String prodect_id,String name, String room, ReqCbk<RspMsgBase> cbk){
		mHomer.addDevice(home_id, device_id, device_sn, device_info, prodect_id, name, room, cbk);
	}
	
	/**家庭更新设备*/
	public void homeUpdateDevice(long home_id, long device_id, String device_sn, String device_info, String prodect_id,String name, String room, ReqCbk<RspMsgBase> cbk){
		mHomer.updateDevice(home_id, device_id, device_sn, device_info, prodect_id, name, room, cbk);
	}
	
	/**获取家庭中的设备*/
	public void homeGetDevice(long home_id, long device_id,ReqCbk<RspMsgBase> cbk){
		mHomer.getDevice(home_id, device_id,cbk);
	}
	
	/**删除家庭中设备*/
	public void homeDelDevice(long home_id, long device_id, String device_sn, ReqCbk<RspMsgBase> cbk){
		mHomer.delDevice(home_id, device_id, device_sn, cbk);
	}
	
	/**添加用户到家庭*/
	public void homeAddUser(long home_id, long user_id, long tar_id, String name, String role, ReqCbk<RspMsgBase> cbk){
		mHomer.addUser(home_id, user_id, tar_id, name, role, cbk);
	}
	
	/**获取家庭中的用户*/
	public void homeGetUser(long home_id, ReqCbk<RspMsgBase> cbk){
		mHomer.getUser(home_id, cbk);
	}
	
	/**删除家庭中用户*/
	public void homeDelUser(long home_id, long user_id, long tar_id, ReqCbk<RspMsgBase> cbk){
		mHomer.delUser(home_id, user_id, tar_id, cbk);
	}
	
	/**添加房间*/
	public void homeAddRoom(long home_id, String name, String brief, ReqCbk<RspMsgBase> cbk){
		mHomer.addRoom(home_id, name, brief, cbk);
	}

	/**更新房间*/
	public void homeUpdateRoom(long home_id, long room_id,String name, String brief, ReqCbk<RspMsgBase> cbk){
		mHomer.updateRoom(home_id, room_id,name, brief, cbk);
	}

	/**获取房间*/
	public void homeGetRoom(long home_id, ReqCbk<RspMsgBase> cbk){
		mHomer.getRoom(home_id, cbk);
	}

	/**删除房间*/
	public void homeDelRoom(long home_id, long room_id, ReqCbk<RspMsgBase> cbk){
		mHomer.delRoom(home_id, room_id, cbk);
	}
	
	/**s设置家庭中的主设备*/
	public void homeSetMonitor(long home_id, long device_id, String device_sn, ReqCbk<RspMsgBase> cbk){
		mHomer.setMonitor(home_id, device_id, device_sn, cbk);
	}
	
	/**获取家庭中的主设备*/
	public void homeGetMaster(long home_id,ReqCbk<RspMsgBase> cbk){
		mHomer.getMaster(home_id, cbk);
	}
	
	/**获取设置的数据*/
	public void homeGetData(long home_id, String type, ReqCbk<RspMsgBase> cbk) {
		mHomer.getData(home_id, type, cbk);
	}

	/**设置数据*/
	public void homeSetData(long home_id, String type, String data, ReqCbk<RspMsgBase> cbk) {
		mHomer.setData(home_id, type, data, cbk);
	}

	/**删除*/
	public void homeDelData(long home_id, String type, ReqCbk<RspMsgBase> cbk) {
		mHomer.delData(home_id, type, cbk);
	}

	/** 查找绑定结果操作 */
	boolean query = false;
	TimerUtil tUtil;

	public void stopQuery() {
		query = false;
		if (tUtil != null) {
			tUtil.stopTimer();
			tUtil = null;
		}
	}

	private void queryBindDevice(final String userId, final String bindCode, final ReqCbk<RspMsgBase> cloudCbk) {
		query = true;
		tUtil = new TimerUtil(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				stopQuery();
				cloudCbk.onFailure(new ErrorObject());
			}
		});
		tUtil.startTimer(Config.SEARCH_TIMEOUT);
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (query) {
					ReqCbk<RspMsgBase> cbk = new ReqCbk<RspMsgBase>() {
						@Override
						public void onSuccess(RspMsgBase rspData) {
							if (rspData.isSuccess()) {
								QueryBindedResultPair.RspQueryBindedResult rsp = (QueryBindedResultPair.RspQueryBindedResult) rspData;
								if (rsp.data != null && !TextUtils.isEmpty(rsp.data.devId)) {
									stopQuery();
									cloudCbk.onSuccess(rspData);
									return;
								}
							}
							if (rspData.errorCode == BusinessErr.DEVICE_BINDED || rspData.errorCode == BusinessErr.DEVICE_BINDED_OTHER) {
								stopQuery();
								cloudCbk.onSuccess(rspData);
								return;
							}
						}

						@Override
						public void onFailure(ErrorObject err) {
						}
					};
					mBsOp.queryBindDev(userId, bindCode, cbk);
					try {
						sleep(4500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/** 匹配设备列表 */
	private List<DevEntity> matchList(List<QueryBindedListPair.RspQueryBindedList.Data> rspList) {
		List<DevEntity> list = new ArrayList<DevEntity>();
		DB_Device dbDevice = new DB_Device(BeiAngAirApplaction.getInstance());
		for (QueryBindedListPair.RspQueryBindedList.Data rspDev : rspList) {
			DevEntity dt = new DevEntity();
			dt.devId = rspDev.devId;
			dt.nickName = rspDev.nickName;
			dt.devInfo = rspDev.devInfo;
			dt.status = rspDev.status;
			dt.role = rspDev.role;
			dt.deviceSn = rspDev.deviceSn;
			dt.value = rspDev.value;
			//这里的value要兼容老设备（老设备是直接base64的数据）
			byte[] devValues = null;
			try {
				//老是设备的数据(里面包含设备类型)
				devValues = Base64.decode(rspDev.value, Base64.DEFAULT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (devValues != null && devValues.length == 25) {
				dt.airInfo = EParse.parseEairByte(devValues);
			}

			//老的设备类型
			if (dt.airInfo != null) {
				dt.devType = dt.airInfo.getDeviceType();
				dbDevice.setDevType(dt.devId, dt.devType);
			} else {
				dt.devType = dbDevice.getDevType(dt.devId);
			}
			
			//新的设备用product_id表示设备类型
			dt.productId = rspDev.product_id;
			String product_id = rspDev.product_id;
			int dType = 0;
			if(!TextUtils.isEmpty(product_id)){
				if(Device.DN_280E.equals(product_id)){
					dType = Device.DT_280E;
				}
				if(Device.DN_JY300.equals(product_id)){
					dType = Device.DT_JY300;
				}
				if(Device.DN_JY500.equals(product_id)){
					dType = Device.DT_JY500;
				}
				if(Device.DN_Airdog.equals(product_id)){
					dType = Device.DT_Airdog;
				}
				if(Device.DN_TAir.equals(product_id)){
					dType = Device.DT_TAir;
				}
				if(Device.DN_PowerSocket.equals(product_id)){
					dType = Device.DT_PowerSocket;
				}
			}
			if(dType !=0){
				dt.devType = dType;
			}
			
			//处理昵称
			if (TextUtils.isEmpty(dt.nickName)) {
				dt.nickName = "贝昂-"+Constants.getDeviceName(dt.devType);
			}

			if ("online".equals(dt.status)) {
				list.add(0, dt);
			} else {
				list.add(dt);
			}
		}
		if(list.size()>0){
			return list;
		}else{
			return null;
		}
	}
	

	private static ErrorObject getErrorObj(ErrorCause cause, int code, String msg) {
		ErrorObject err = new ErrorObject();
		err.cause = cause;
		err.errorCode = code;
		err.errorMsg = msg;
		return err;
	}

}
