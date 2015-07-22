package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.AirdogFC;
import com.beiang.airdog.constant.Constants.Command;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentHomer;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.business.ihomer.HomeGetMasterPair.RspHomeGetMaster;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.IHomerCenterDeviceAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.CommEntity;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.ui.model.ScriptEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.view.IHomerSetMasterDialog;
import com.beiang.airdog.view.IHomerSetMasterDialog.ConfirmListener;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/***
 * 云家庭中心
 * 
 * @author LSD
 * 
 */
public class IHomerCenterActivity extends BaseMultiPartActivity implements OnClickListener, OnItemClickListener {
	ImageView iv_more;
	TextView tv_homer_pm25;
	TextView tv_homer_temp;
	TextView tv_homer_humidity;
	TextView tv_script;
	GridView homer_devlist;
	ImageView iv_dog;

	PopupWindow popWin;

	IHomerCenterDeviceAdapter adapter;
	OPEntity opEntity;
	RspHomeGetMaster.Data curMaster;
	long curHomerId;
	long curMasterId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ihomer_center);

		//setMenuEnable(false);
		// 菜单的点击事件
		setOnItemClickListener(this);

		initView();
		initMenuData();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	private void initView() {
		initPopWin(mActivity);
		(iv_more = (ImageView) findViewById(R.id.iv_more)).setOnClickListener(this);
		tv_homer_pm25 = (TextView) findViewById(R.id.tv_homer_pm25);
		tv_homer_temp = (TextView) findViewById(R.id.tv_homer_temp);
		tv_homer_humidity = (TextView) findViewById(R.id.tv_homer_humidity);
		(tv_script = (TextView) findViewById(R.id.tv_script)).setOnClickListener(this);
		homer_devlist = (GridView) findViewById(R.id.homer_devlist);
		homer_devlist.setAdapter(adapter = new IHomerCenterDeviceAdapter());
		homer_devlist.setOnItemClickListener(this);
		(iv_dog = (ImageView) findViewById(R.id.iv_dog)).setOnClickListener(this);
	}

	void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("manage");
		menu.setMenu_name("管理设备");
		menus.add(menu);

	/*	menu = new MenuEntity();
		menu.setMenu_key("script");
		menu.setMenu_name("设置规则");
		menus.add(menu);*/
		prepareOptionsMenu(menus);
	}

	private void initPopWin(Context context) {
		View popView = LayoutInflater.from(context).inflate(R.layout.layout_home_pop_view, null);
		if (popWin == null) {
			popWin = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		}
		// 需要设置一下此参数，点击外边可消失
		popWin.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		popWin.setOutsideTouchable(false);
		// 设置此参数获得焦点，否则无法点击
		popWin.setFocusable(true);
		popView.findViewById(R.id.tv_ac_manage).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismissPopWin();
				startActivity(new Intent(mActivity, IHomerDeviceActivity.class));
			}
		});
		popView.findViewById(R.id.tv_ac_script).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismissPopWin();
				showMasterDialog();
			}
		});
	}

	public void showPopWin() {
		if (popWin != null) {
			popWin.showAsDropDown(iv_more);
		}
	}

	public void dismissPopWin() {
		if (popWin != null) {
			popWin.dismiss();
		}
	}

	private void initData() {
		curHomerId = CurrentHomer.instance().curHomer.home_id;

		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);
	}

	private void loadData() {
		getMaster();
		//getDevice();
	}

	private void getDevice() {
		BsOperationHub.instance().homeGetDevice(curHomerId, 0, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspHomeGetDev rsp = (RspHomeGetDev) rspData;
					List<RspHomeGetDev.Data> datas = rsp.datas;
					CurrentDevice.instance().homerDevList = datas;
					adapter.setData(datas);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void getMaster() {
		BsOperationHub.instance().homeGetMaster(curHomerId, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspHomeGetMaster rsp = (RspHomeGetMaster) rspData;
					if (rsp.data != null) {
						curMaster = rsp.data;
						curMasterId = curMaster.device_id;
						getMasterDevice(curMaster.device_id);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void getMasterDevice(long device_id) {
		BsOperationHub.instance().homeGetDevice(curHomerId, device_id, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspHomeGetDev rsp = (RspHomeGetDev) rspData;
					List<RspHomeGetDev.Data> datas = rsp.datas;
					Map<String, String> data = null;
					if (datas != null && datas.size() > 0) {
						try {
							HashMap<String, String> value = datas.get(0).value;
							String report = value.get("report");
							byte[] result = Base64.decode(report, Base64.DEFAULT);
							data = new Gson().fromJson(new String(result), new TypeToken<HashMap<String, String>>() {
							}.getType());
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					if (data != null) {
						setData(data);
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
	 * 自定义脚本
	 * 
	 * @param sH
	 * @param sM
	 * @param eH
	 * @param eM
	 * @param temp
	 * @param hum
	 */
	private void setScript(String sH, String sM, String eH, String eM, String temp, String hum) {
		showDialog("请稍后……");
		String s = "[{type:\"temp\",enable:1,time:\"" + sH + ":" + sM + "-" + eH + ":" + eM + "\",value:\"" + temp
				+ "\"},{type:\"humidity\",enable:1,time:\"" + sH + ":" + sM + "-" + eH + ":" + eM + "\",value:\"" + hum
				+ "\"},{type:\"pm25\",enable:1,time:\"" + sH + ":" + sM + "-" + eH + ":" + eM + "\",value:\"01\"}]";
		ScriptEntity ety = new ScriptEntity();
		ety.setData(s.getBytes());
		opEntity.setbEntity(ety);
		opEntity.setCommand(Command.WTITE);
		opEntity.setFunction(AirdogFC.SCRIPT.getValue());
		byte[] send = EParse.parseparseOPEntity(opEntity);
		LogUtil.i(send);

		BsOperationHub.instance().homeSendCmd(curHomerId, curMasterId, "", send, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				hideDialog();
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "设置成功");
				} else {
					Toast.show(mActivity, "设置失败");
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				hideDialog();
				Toast.show(mActivity, "设置失败");
			}
		});
	}
	
	private void transportCloud_V2(byte[] data) {
		BsOperationHub.instance().homeSendCmd(curHomerId, curMasterId, "",  data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setData(Map<String, String> data) {
		if (data != null) {
			if (data.containsKey("pm25")) {
				String pm25 = data.get("pm25");
				tv_homer_pm25.setText(pm25);
			}
			if (data.containsKey("temp")) {
				String temp = data.get("temp");
				tv_homer_temp.setText(temp);
			}
			if (data.containsKey("humidity")) {
				String humidity = data.get("humidity");
				tv_homer_humidity.setText(humidity);
			}
		}
	}

	private void showMasterDialog() {
		if (curMaster == null) {
			Toast.show(mActivity, "你还没有设置中控设备，请手动设置");
			return;
		}
		IHomerSetMasterDialog.showHome(mActivity, new ConfirmListener() {
			@Override
			public void start(String sH, String sM, String eH, String eM, String temp, String hum) {
				// TODO Auto-generated method stub
				setScript(sH, sM, eH, eM, temp, hum);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_more:
			showPopWin();
			break;
		case R.id.tv_script:
			showMasterDialog();
			break;
		case R.id.iv_dog:
			//通知小狗叫
			if (curMaster == null) {
				Toast.show(mActivity, "你还没有设置中控设备，请手动设置");
				return;
			}
			CommEntity entity = new CommEntity();
			entity.value = 1;
			opEntity.setbEntity(entity);
			opEntity.setCommand(Command.WTITE);
			opEntity.setFunction(AirdogFC.YELP.getValue());
			transportCloud_V2(EParse.parseparseOPEntity(opEntity));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Object object = parent.getAdapter().getItem(position);
		if (object instanceof MenuEntity) {
			// 菜单点击事件
			MenuEntity ety = (MenuEntity) object;
			if ("script".equals(ety.getMenu_key())) {
				showMasterDialog();
			}
		}
		if (object instanceof RspHomeGetDev.Data) {
			// 设备列表点击(测试用)
		}
	}
}
