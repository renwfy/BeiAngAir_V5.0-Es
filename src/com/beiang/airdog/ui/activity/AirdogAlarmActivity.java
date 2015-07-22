package com.beiang.airdog.ui.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.AirdogFC;
import com.beiang.airdog.constant.Constants.Command;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.adapter.AlarmListAdapter;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirdogAlarm;
import com.beiang.airdog.ui.model.AirdogAlarm.Alarm;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.broadlink.beiangair.R;

/***
 * 闹钟
 * 
 * @author LSD
 * 
 */
public class AirdogAlarmActivity extends BaseMultiPartActivity implements OnClickListener {
	private DevEntity mDevice;

	TextView tv_alrm_getup;
	TextView tv_alrm_notice;
	ImageView iv_getup_set;
	ImageView iv_notice_set;
	ListView alarm_list;

	AlarmListAdapter adapter;
	OPEntity opEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airdog_alarm);
		setMenuEnable(false);

		initView();
		initData();
		queryData();
	}

	private void initView() {
		tv_alrm_getup = (TextView) findViewById(R.id.tv_alrm_getup);
		tv_alrm_notice = (TextView) findViewById(R.id.tv_alrm_notice);
		iv_getup_set = (ImageView) findViewById(R.id.iv_getup_set);
		iv_notice_set = (ImageView) findViewById(R.id.iv_notice_set);
		alarm_list = (ListView) findViewById(R.id.alarm_list);
		alarm_list.setAdapter(adapter = new AlarmListAdapter(mActivity));
		
		findViewById(R.id.layout_getup).setOnClickListener(this);
		findViewById(R.id.layout_notice).setOnClickListener(this);
	}
	
	/**初始化数据*/
	void initData() {
		mDevice = CurrentDevice.instance().curDevice;
		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);
	}
	
	/**查询数据*/
	private void queryData() {
		opEntity.setCommand(Command.READ);
		opEntity.setFunction(AirdogFC.ALARM.getValue());
		byte[] data = EParse.parseQuery(opEntity);
		transportCloud(data);
	}
	
	
	/**
	 * 服务器传输数据
	 * 
	 * @param data
	 */
	private void transportCloud(byte[] data) {
		LogUtil.i(data);
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					CommandPair.RspCommand rsp = (CommandPair.RspCommand) rspData;
					if (rsp.reply != null) {
						LogUtil.i(rsp.reply);
						OPEntity opEntity = EParse.parseOPBytes(rsp.reply);
						if(opEntity != null){
							AirdogAlarm ctrlEntity = (AirdogAlarm) opEntity.getbEntity();
							if(ctrlEntity != null){
								List<Alarm> alarms = ctrlEntity.getAlarms();

								setDataToView(alarms);
								return;
							}
						}
					}
					//空数据列表
					adapter.setData(null);
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	private void setDataToView(List<Alarm> alarms) {
		// TODO Auto-generated method stub
		adapter.setData(alarms);
	}

	/***
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layout_getup:
		case R.id.iv_getup_set:
			startActivityForResult((new Intent(mActivity, AirdogGetUpAlarmActivity.class)),100);
			break;
		case R.id.layout_notice:
		case R.id.iv_notice_set:
			startActivityForResult((new Intent(mActivity, AirdogNoticeAlarmActivity.class)),100);
			break;
		default:
			break;
		}
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		LogUtil.i("refrash");
		queryData();
	}

}
