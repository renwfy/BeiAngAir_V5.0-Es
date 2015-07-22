package com.beiang.airdog.ui.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentHomer;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev.Data;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.IHomerDeviceAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.view.AlertDialog.AlertDialogCallBack;
import com.beiang.airdog.view.IHomerDeviceDialogs;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

public class IHomerDeviceActivity extends BaseMultiPartActivity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	long curHomeId;
	ListView idev_list;
	IHomerDeviceAdapter adapter;
	TextView iv_homername;
	TextView tv_newdev;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idevice);
		setMenuEnable(false);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadData();
	}

	private void initView() {
		idev_list = (ListView) findViewById(R.id.idev_list);
		idev_list.setAdapter(adapter = new IHomerDeviceAdapter());
		idev_list.setOnItemClickListener(this);
		idev_list.setOnItemLongClickListener(this);

		iv_homername = (TextView) findViewById(R.id.iv_homername);
		tv_newdev = (TextView) findViewById(R.id.tv_newdev);
		tv_newdev.setOnClickListener(this);
	}

	private void initData() {
		curHomeId = CurrentHomer.instance().curHomer.home_id;

		iv_homername.setText(CurrentHomer.instance().curHomer.name);
	}

	private void loadData() {
		getDevice();
	}

	private void getDevice() {
		BsOperationHub.instance().homeGetDevice(curHomeId, 0, new ReqCbk<RspMsgBase>() {
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

	private void removeDevice(final long home_id, long device_id) {
		BsOperationHub.instance().homeDelDevice(home_id, device_id, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "删除成功");
					getDevice();
					return;
				}
				Toast.show(mActivity, "删除失败");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "删除失败");
			}
		});
	}

	private void setMonitor(final long home_id, long device_id) {
		BsOperationHub.instance().homeSetMonitor(home_id, device_id, "", new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					Toast.show(mActivity, "设置成功");
					getDevice();
					return;
				}
				Toast.show(mActivity, "设置失败");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				Toast.show(mActivity, "设置失败");
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		RspHomeGetDev.Data data = (Data) adapter.getItem(position);
		if(CurrentDevice.instance().devList != null && CurrentDevice.instance().devList.size()>0){
			for(DevEntity entity :CurrentDevice.instance().devList){
				if(entity.devId.equals(data.device_id+"")){
					CurrentDevice.instance().curDevice = entity;
					break;
				}
			}
			
			if (0 == data.is_online) {
				Toast.show(mActivity, "设备不在线");
				return;
			}

			//现在跳转的都是通用界面（里面新老设备获取的数据有兼容）
			Intent intent = new Intent();
			if (data.getDevType() == Device.DT_Airdog) { // AirDog
				intent.setClass(mActivity, AirdogActivity.class);
			} else if (data.getDevType() == Device.DT_TAir) { // TAir
				intent.setClass(mActivity, TairActivity.class);
			} else if (data.getDevType() == Device.DT_Light) { // 灯
				intent.setClass(mActivity, DeviceTestActivity.class);
			} else if (data.getDevType() == Device.DT_PowerSocket) { // 插座
				intent.setClass(mActivity, DeviceTestActivity.class);
			} else if (data.getDevType() == Device.DT_FC1 || data.getDevType() == Device.DT_FA20) { // 新风设备
				intent.setClass(mActivity, FreshAirActivity.class);
			} else {
				intent.setClass(mActivity, DeviceControlActivity.class);
			}
			
			//跳转到测试云家庭详情页（使用的是设备在家庭里面的处理方式）
			//CurrentDevice.instance().curHomerDevice = data;
			//intent.setClass(mActivity, IHomerDeviceDetailsActivity.class);

			startActivity(intent);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		RspHomeGetDev.Data data = (Data) adapter.getItem(position);
		final long device_id = data.device_id;
		AlertDialog.show(mActivity, 0, "警告!\n您将操作设备？", "移除设备", "设置主设备", new AlertDialogCallBack() {
			@Override
			public void onRight() {
				// TODO Auto-generated method stub
				setMonitor(curHomeId, device_id);
			}

			@Override
			public void onLeft() {
				// TODO Auto-generated method stub
				removeDevice(curHomeId, device_id);
			}
		});
		//IHomerDeviceDialogs.showLongTouchDialog(mActivity);
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_newdev:
			startActivity(new Intent(mActivity, IHomerAddDeviceActivity.class));
			break;
		default:
			break;
		}
	}
}
