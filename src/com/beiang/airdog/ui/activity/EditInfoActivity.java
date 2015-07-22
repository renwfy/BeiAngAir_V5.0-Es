package com.beiang.airdog.ui.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.beiang.airdog.constant.Constants.Command;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.Function;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.QueryDevDataPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.ui.model.FirmwareEntity;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.Helper;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.XmlUtils;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/***
 * Description ：修改昵称
 * 
 * @author Lsd
 * 
 */

public class EditInfoActivity extends BaseMultiPartActivity implements OnClickListener {
	EditText et_nickname;
	TextView tv_devid, tv_softver, tv_sinal, tips, tv_confirm;

	DevEntity mDevice;
	String nick;
	String curVer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editinfo);

		setSwipeBackEnable(true);
		setMenuEnable(false);

		mDevice = CurrentDevice.instance().curDevice;

		initView();
		//硬件版本
		devFirmware();
		//信号强度
		queryMXData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		tv_devid = (TextView) findViewById(R.id.tv_devid);
		tv_softver = (TextView) findViewById(R.id.tv_softver);
		tv_sinal = (TextView) findViewById(R.id.tv_sinal);
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		tips = (TextView) findViewById(R.id.tips);

		tv_confirm = (TextView) findViewById(R.id.tv_confirm);
		tv_confirm.setOnClickListener(this);
		findViewById(R.id.rl_ver_layout).setOnClickListener(this);

		tv_devid.setText(mDevice.devId);

		nick = mDevice.nickName;
		et_nickname.setText(mDevice.nickName);
	}

	/***
	 * 软件版本
	 */
	private void devFirmware() {
		// TODO Auto-generated method stub
		// 主要使用 mDevice.devId
		BsOperationHub.instance().queryDevFirmware(mDevice, "firmware", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					QueryDevDataPair.RspQueryDevData rsp = (QueryDevDataPair.RspQueryDevData) rspData;
					if (rsp.data != null) {
						String verson = curVer = rsp.data.value;
						tv_softver.setText("当前版本：" + verson);
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
	 * 
	 */

	private void queryMXData() {
		LogUtil.i("queryMXData()");
		BsOperationHub.instance().queryDevStatus(mDevice.devId, "beiang_status", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					AirInfo airInfo = CurrentDevice.instance().queryDevice.airInfo;
					if (airInfo != null) {
						// 老设备解析
						tv_sinal.setText(airInfo.getSignal() + "");
					} else {
						// 新设备解析
						DevEntity entity = CurrentDevice.instance().queryDevice;
						try {
							String report = new String(Base64.decode(entity.value, Base64.DEFAULT));
							HashMap<String, String> data = new Gson().fromJson(report, new TypeToken<HashMap<String, String>>() {
							}.getType());
							if(data.containsKey("purifier")){
								//净化器的上传方式
								airInfo = EParse.parseEairByte(Helper.ConvertStringToHexBytesArray(data.get("purifier")));
								if (airInfo != null) {
									tv_sinal.setText(airInfo.getSignal() + "");
								}
							}
							if(data.containsKey("signal")){
								//其他设备上传方式
								String signal = data.get("signal");
								tv_sinal.setText(signal + "");
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}

		});
	}

	private void getFirmwareNewVerson() {
		showDialog("正在获取新版本");
		API.getFirmwareNewVerson(mActivity, mDevice.devType, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				LogUtil.i(response);
				hideDialog();
				List<FirmwareEntity> list = XmlUtils.parse(response);
				if (list == null || list.size() == 0) {
					Toast.show(mActivity, "没有找到新版本");
					return;
				}
				for (final FirmwareEntity entity : list) {
					if (entity.type == 1) {
						// 固件
						String newVer = entity.ver;
						int b = 0;
						try {
							b = newVer.compareTo(curVer);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (b > 0) {
							AlertDialog.show(mActivity, 0, "检测到新版本,是否更新 ？", true, new AlertDialogCallBack() {
								@Override
								public void onLeft() {
									// TODO Auto-generated method stub
								}

								@Override
								public void onRight() {
									// TODO Auto-generated method stub
									noticeFirmwareUpdate(entity);
								}
							});
						} else {
							Toast.show(mActivity, "没有找到新版本");
						}
						return;
					}
				}
				Toast.show(mActivity, "没有找到新版本");
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				hideDialog();
				Toast.show(mActivity, "没有找到新版本");
			}
		});
	}

	private void transportCloud(byte[] data) {
		LogUtil.i(data);
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "通知硬件更新成功");
					return;
				}
				Toast.show(mActivity, "通知硬件更新失败");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "通知硬件更新失败");
			}
		});
	}

	private void noticeFirmwareUpdate(FirmwareEntity ware) {
		OPEntity opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		if (mDevice.devType == Device.DT_Airdog) {
			opEntity.setSubDevType(SubDevice.SDT_Airdog);
		} else {
			opEntity.setSubDevType(SubDevice.SDT_AirPurifier);
		}
		opEntity.setbEntity(ware);

		opEntity.setCommand(Command.WTITE);
		opEntity.setFunction(Function.FIRMWARE.getValue());
		transportCloud(EParse.parseparseOPEntity(opEntity));
	}

	private void upDevInfo() {
		final String nickname = et_nickname.getText().toString();
		DevEntity devEntity = new DevEntity();
		devEntity.nickName = nickname;
		devEntity.role = mDevice.role;
		devEntity.devInfo = mDevice.devInfo;
		devEntity.devId = mDevice.devId;
		devEntity.deviceSn = mDevice.deviceSn;
		BsOperationHub.instance().upDateAuthrorize(devEntity, CurrentUser.instance().getUserId(), new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "修改成功");
					mDevice.nickName = nickname;
					setResult(RESULT_OK);
					finish();
				} else {
					String eroMsg = rspData.getErrorString();
					if (!"".equals(eroMsg)) {
						Toast.show(mActivity, eroMsg);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				String eroMsg = err.getErrorString();
				if (!"".equals(eroMsg)) {
					Toast.show(mActivity, eroMsg);
				}
			}
		});
	}

	// 隐藏输入法
	private void dismissKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_nickname.getWindowToken(), 0);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_confirm:
			dismissKeyboard();
			String et_txt = et_nickname.getText().toString();
			if (et_txt.equals(nick)) {
				finish();
			} else {
				upDevInfo();
			}
			break;
		case R.id.rl_ver_layout:
			getFirmwareNewVerson();
			break;

		default:
			break;
		}
	}

}