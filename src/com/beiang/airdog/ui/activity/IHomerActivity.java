package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentHomer;
import com.beiang.airdog.net.business.entity.CurrentUser;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.IHomerAdapter;
import com.beiang.airdog.ui.adapter.IHomerPagerAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.MenuEntity;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.widget.Toast;
import com.beiang.airdog.widget.pagerindicator.CirclePageIndicator;
import com.broadlink.beiangair.R;

/***
 * 云家庭主界面
 * 
 * @author LSD
 * 
 */
public class IHomerActivity extends BaseMultiPartActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	TextView tv_newhomer;
	ViewPager viewpager;
	CirclePageIndicator page_indicator;
	GridView gv_device;
	ImageView iv_tips;
	ImageView iv_refrash;

	IHomerPagerAdapter adapter;
	IHomerAdapter devAdapter;

	List<RspHomeGetDev.Data> allHomeDeviceList;
	boolean getBindFinish = false;
	boolean getHomeFinish = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ihomer);

		setSwipeBackEnable(false);

		initView();
		initMenuData();
		loadData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refrashData();
			}
		}, 1000);
	}

	void initMenuData() {
		// TODO Auto-generated method stub
		List<MenuEntity> menus = new ArrayList<MenuEntity>();
		MenuEntity menu = new MenuEntity();
		menu.setMenu_key("acout");
		menu.setMenu_name("我的账号");
		menu.setMenu_icon(R.drawable.ic_menu_acout);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("plus");
		menu.setMenu_name("添加设备");
		menu.setMenu_icon(R.drawable.ic_menu_plus);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("help");
		menu.setMenu_name("帮助");
		menu.setMenu_icon(R.drawable.ic_menu_help);
		menus.add(menu);

		menu = new MenuEntity();
		menu.setMenu_key("share");
		menu.setMenu_name("分享");
		menu.setMenu_icon(R.drawable.ic_menu_share);
		menus.add(menu);
		prepareOptionsMenu(menus);
	}

	void initView() {
		(iv_tips = (ImageView) findViewById(R.id.iv_tips)).setOnClickListener(this);
		tv_newhomer = (TextView) findViewById(R.id.tv_newhomer);
		tv_newhomer.setOnClickListener(this);
		(iv_refrash = (ImageView) findViewById(R.id.iv_refrash)).setOnClickListener(this);

		viewpager = (ViewPager) findViewById(R.id.viewpager);
		page_indicator = (CirclePageIndicator) findViewById(R.id.page_indicator);
		viewpager.setAdapter(adapter = new IHomerPagerAdapter(mActivity));

		gv_device = (GridView) findViewById(R.id.gv_device);
		gv_device.setAdapter(devAdapter = new IHomerAdapter());
		gv_device.setOnItemClickListener(this);
		gv_device.setOnItemLongClickListener(this);
	}

	void loadData() {
		getHomerList();
	}

	void refrashData() {
		getDeviceList();
		getAllHomeDevList();
	}

	void getAllHomeDevList() {
		getHomeFinish = false;
		if (allHomeDeviceList != null) {
			allHomeDeviceList.clear();
		}
		List<RspGetHomer.Data> datas = CurrentHomer.instance().homeList;
		if (datas != null && datas.size() > 0) {
			for (int i = 0; i < datas.size(); i++) {
				RspGetHomer.Data data = datas.get(i);
				if (i == datas.size() - 1) {
					getHomerDeviceList(data.home_id, true);
				} else {
					getHomerDeviceList(data.home_id, false);
				}
			}
		}
	}

	/** 获取家庭数据 */
	private void getHomerList() {
		BsOperationHub.instance().getHomer(new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspGetHomer rsp = (RspGetHomer) rspData;
					List<RspGetHomer.Data> datas = rsp.datas;
					if (datas.size() > 0) {
						CurrentHomer.instance().homeList = datas;
						viewpager.setVisibility(View.VISIBLE);
						iv_tips.setVisibility(View.GONE);

						adapter.setData(datas);
						page_indicator.setViewPager(viewpager);
					} else {
						viewpager.setVisibility(View.GONE);
						iv_tips.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	/** 获取设备数据 */
	private void getDeviceList() {
		getBindFinish = false;
		BsOperationHub.instance().queryBindList(CurrentUser.instance().getUserId(), new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				getBindFinish = true;
				callback.success();
				// List<DevEntity> devList = CurrentDevice.instance().devList;
				// devAdapter.setData(devList);
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	/***
	 * 获取家庭中设备列表
	 * 
	 * @param curHomeId
	 */
	private void getHomerDeviceList(long curHomeId, final boolean last) {
		BsOperationHub.instance().homeGetDevice(curHomeId, 0, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					RspHomeGetDev rsp = (RspHomeGetDev) rspData;
					List<RspHomeGetDev.Data> datas = rsp.datas;
					if (allHomeDeviceList == null) {
						allHomeDeviceList = new ArrayList<RspHomeGetDev.Data>();
					}
					if (datas != null && datas.size() > 0) {
						allHomeDeviceList.addAll(datas);
					}
				}
				if (last) {
					getHomeFinish = true;
					callback.success();
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				if (last) {
					getHomeFinish = true;
					callback.success();
				}
			}
		});
	}

	private void onItemLongClick(final int position) {
		final DevEntity device = (DevEntity) devAdapter.getItem(position);
		String hintStr = getString(R.string.delete_device_hint);
		final String right = device.role;
		if ("owner".equals(right)) {
			hintStr = getString(R.string.delete_device_hint2);
		}
		AlertDialog.show(mActivity, 0, "删除设备\n" + hintStr, true, new AlertDialogCallBack() {
			@Override
			public void onLeft() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onRight() {
				// TODO Auto-generated method stub
				if ("owner".equals(right)) {
					// ///// 复位
					unbindDevice(device, position);
				} else {
					// //// 解绑
					unbindDevice(device, position);
				}
			}
		});
	}

	private void unbindDevice(final DevEntity device, final int position) {
		BsOperationHub.instance().unbindDevice(device.devId, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					devAdapter.delDevice(position);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void matchBindDevList() {
		List<DevEntity> devList = CurrentDevice.instance().devList;
		List<DevEntity> temp = new ArrayList<DevEntity>();
		for (DevEntity devEty : devList) {
			boolean b = true;
			if (allHomeDeviceList != null) {
				for (RspHomeGetDev.Data data : allHomeDeviceList) {
					if (devEty.devId.equals(data.device_id + "")) {
						b = false;
						break;
					}
				}
			}
			if (b) {
				temp.add(devEty);
			}
		}
		devAdapter.setData(temp);
	}

	NetCallback callback = new NetCallback() {
		@Override
		public void success() {
			// TODO Auto-generated method stub
			if (getHomeFinish && getBindFinish) {
				matchBindDevList();
				iv_refrash.clearAnimation();
			}
		}
	};

	@Override
	public void onBackPressed() {
		AlertDialog.show(mActivity, 0, "退出应用？", true, new AlertDialogCallBack() {
			@Override
			public void onRight() {
				// TODO Auto-generated method stub
				startActivity(new Intent(mActivity, ExitActivity.class));
				finish();
			}

			@Override
			public void onLeft() {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_newhomer:
			startActivity(new Intent(mActivity, IHomerNewHomeActivity.class));
			break;
		case R.id.iv_tips:
			Toast.show(mActivity, "请创建智能家庭");
			break;
		case R.id.iv_refrash:
			iv_refrash.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.air_speed_rotate));
			refrashData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		DevEntity curDevice = (DevEntity) devAdapter.getItem(position);
		CurrentDevice.instance().curDevice = curDevice;
		if ("offline".equals(curDevice.status)) {
			Toast.show(mActivity, "设备不在线");
			return;
		}

		//现在跳转的都是通用界面（里面新老设备获取的数据有兼容）
		Intent intent = new Intent();
		if (curDevice.devType == Device.DT_Airdog) { // AirDog
			intent.setClass(mActivity, AirdogActivity.class);
		} else if (curDevice.devType == Device.DT_TAir) { // TAir
			intent.setClass(mActivity, TairActivity.class);
		} else if (curDevice.devType == Device.DT_Light) { // 灯
			intent.setClass(mActivity, DeviceTestActivity.class);
		} else if (curDevice.devType == Device.DT_PowerSocket) { // 插座
			intent.setClass(mActivity, DeviceTestActivity.class);
		} else if (curDevice.devType == Device.DT_FC1 || curDevice.devType == Device.DT_FA20) { // 新风设备
			intent.setClass(mActivity, FreshAirActivity.class);
		} else {
			intent.setClass(mActivity, DeviceControlActivity.class);
		}

		startActivityForResult(intent, 200);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		onItemLongClick(position);
		return false;
	}

	public interface NetCallback {
		void success();
	}
}
