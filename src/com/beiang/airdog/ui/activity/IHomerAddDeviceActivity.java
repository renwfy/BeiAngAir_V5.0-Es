package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.CurrentHomer;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.ihomer.HomeGetDevPair.RspHomeGetDev;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.IHomerAddDeviceAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;
/***
 * 
 * @author LSD
 * 
 * 添加设备到云家庭
 *
 */
public class IHomerAddDeviceActivity extends BaseMultiPartActivity implements OnClickListener ,OnItemClickListener{
	long curHomerId;
	TextView tv_ok;
	ListView idev_list;
	IHomerAddDeviceAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ihomer_adddevice);
		setMenuEnable(false);
		
		curHomerId = CurrentHomer.instance().curHomer.home_id;

		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadData();
	}

	void initView() {
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(this);

		idev_list = (ListView) findViewById(R.id.idev_list);
		idev_list.setAdapter(adapter = new IHomerAddDeviceAdapter());
		idev_list.setOnItemClickListener(this);
	}

	void loadData() {
		List<DevEntity> list = new ArrayList<DevEntity>();
		
		List<DevEntity> devList = CurrentDevice.instance().devList;
		List<RspHomeGetDev.Data> homerDevList = CurrentDevice.instance().homerDevList;
		for (DevEntity dEntity : devList) {
			boolean b = true;
			if(homerDevList != null){
				for (RspHomeGetDev.Data data : homerDevList) {
					if(dEntity.devId.equals(data.device_id+"")){
						b = false;
						break;
					}
				}	
			}
			if(b){
				list.add(dEntity);
			}
		}
		adapter.setData(list);
	}
	
	void addDevToHomer(final int position,DevEntity ety) {
		LogUtil.i("productId = "+ety.productId);
		BsOperationHub.instance().homeAddDevice(curHomerId, Long.parseLong(ety.devId), "", "", ety.productId, ety.nickName, "0",
				new ReqCbk<RspMsgBase>() {
					@Override
					public void onSuccess(RspMsgBase rspData) {
						// TODO Auto-generated method stub
						if (rspData.isSuccess()) {
							Toast.show(mActivity, "添加成功");
							adapter.delData(position);
							return;
						}
						Toast.show(mActivity, "添加失败");
					}

					@Override
					public void onFailure(ErrorObject err) {
						// TODO Auto-generated method stub
						Toast.show(mActivity, "添加失败");
					}
				});

	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_ok:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		DevEntity ety = (DevEntity) adapter.getItem(position);
		addDevToHomer(position, ety);
	}
}
