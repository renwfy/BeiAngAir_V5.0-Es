package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.AirdogFC;
import com.beiang.airdog.constant.Constants.AlarmType;
import com.beiang.airdog.constant.Constants.Command;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.net.business.BsOperationHub;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.business.homer.CommandPair;
import com.beiang.airdog.net.httpcloud.aync.abs.BaseMsg.RspMsgBase;
import com.beiang.airdog.net.httpcloud.aync.abs.ReqCbk;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirdogAlarm;
import com.beiang.airdog.ui.model.AirdogAlarm.Alarm;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.view.TimerPickDialog;
import com.beiang.airdog.view.TimerPickDialog.TimerPickDialogCallBack;
import com.beiang.airdog.view.WeekPickDialog;
import com.beiang.airdog.view.WeekPickDialog.RepeatDialogCallBack;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

/***
 * 起床闹钟
 * 
 * @author LSD
 * 
 */
public class AirdogGetUpAlarmActivity extends BaseMultiPartActivity implements OnClickListener {
	private DevEntity mDevice;

	TextView tv_alrm_getup;
	LinearLayout ll_time_set;
	TextView tv_time_set;
	LinearLayout ll_repeat_set;
	TextView tv_repeat_set;
	LinearLayout ll_voice_set;
	TextView tv_voice_set;
	TextView yes;
	ImageView iv_delete;

	int hour = 0;
	int min = 0;
	int weekSum = 0;
	Alarm tempAlarm;

	OPEntity opEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_getup_alarm);
		setMenuEnable(false);

		initView();
		initData();
		setData();
	}

	private void initView() {
		tv_alrm_getup = (TextView) findViewById(R.id.tv_alrm_getup);
		ll_time_set = (LinearLayout) findViewById(R.id.ll_time_set);
		tv_time_set = (TextView) findViewById(R.id.tv_time_set);
		ll_repeat_set = (LinearLayout) findViewById(R.id.ll_repeat_set);
		tv_repeat_set = (TextView) findViewById(R.id.tv_repeat_set);
		ll_voice_set = (LinearLayout) findViewById(R.id.ll_voice_set);
		tv_voice_set = (TextView) findViewById(R.id.tv_voice_set);
		yes = (TextView) findViewById(R.id.yes);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);

		tv_alrm_getup.setSelected(true);
		ll_time_set.setOnClickListener(this);
		ll_repeat_set.setOnClickListener(this);
		ll_voice_set.setOnClickListener(this);
		yes.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
	}

	/** 初始化数据 */
	void initData() {
		mDevice = CurrentDevice.instance().curDevice;
		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);
	}

	private void setData() {
		Intent intent = getIntent();
		if (intent.hasExtra("alarm")) {
			Alarm alarm = tempAlarm = (Alarm) intent.getSerializableExtra("alarm");
			hour = alarm.hour;
			min = alarm.minute;
			weekSum = alarm.week;
			iv_delete.setVisibility(View.VISIBLE);
			yes.setVisibility(View.VISIBLE);
		} else {
			Calendar curCalendar = Calendar.getInstance();
			int curY = curCalendar.get(Calendar.YEAR);
			int curM = curCalendar.get(Calendar.MONTH) + 1;
			int curD = curCalendar.get(Calendar.DAY_OF_MONTH);

			int curH = curCalendar.get(Calendar.HOUR_OF_DAY);
			int curMin = curCalendar.get(Calendar.MINUTE);

			hour = curH;
			min = curMin;

			iv_delete.setVisibility(View.INVISIBLE);
			yes.setVisibility(View.VISIBLE);
		}

		setTimeStatus(hour, min);
		setWeekStatus(weekSum);
	}

	private void setTimeStatus(int hour, int min) {
		tv_time_set.setText(String.format("%02d", hour) + "：" + String.format("%02d", min));
	}

	private void setWeekStatus(int weekSum) {
		String weekString = "";
		if (weekSum >= 64) {
			weekSum = weekSum - 64;
			weekString = "周日" + weekString;
		}
		if (weekSum >= 32) {
			weekSum = weekSum - 32;
			weekString = "周六  " + weekString;
		}
		if (weekSum >= 16) {
			weekSum = weekSum - 16;
			weekString = "周五  " + weekString;
		}
		if (weekSum >= 8) {
			weekSum = weekSum - 8;
			weekString = "周四  " + weekString;
		}
		if (weekSum >= 4) {
			weekSum = weekSum - 4;
			weekString = "周三  " + weekString;
		}
		if (weekSum >= 2) {
			weekSum = weekSum - 2;
			weekString = "周二  " + weekString;
		}
		if (weekSum == 1) {
			weekString = "周一  " + weekString;
		}
		tv_repeat_set.setText(weekString);
	}

	/**
	 * 服务器传输数据
	 * 
	 * @param data
	 */
	private void transportCloud(final Command comm, byte[] data) {
		LogUtil.i(data);
		showDialog("请稍后");
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				hideDialog();
				if (rspData.isSuccess()) {
					CommandPair.RspCommand rsp = (CommandPair.RspCommand) rspData;
					if (rsp.reply != null) {
						LogUtil.i(rsp.reply);
						if (comm == Command.WTITE) {
							Toast.show(mActivity, "添加闹钟成功");
						} else if (comm == Command.DELETE) {
							Toast.show(mActivity, "删除闹钟成功");
						}
						setResult(RESULT_OK);
						finish();
						return;
					}
				}
				Toast.show(mActivity, "操作失败");
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
				hideDialog();
				Toast.show(mActivity, "添加闹钟失败");
			}
		});
	}

	/***
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_time_set:
			new TimerPickDialog().show(mActivity, hour, min, new TimerPickDialogCallBack() {
				@Override
				public void onConfirm(Integer[] select) {
					// TODO Auto-generated method stub
					hour = select[0];
					min = select[1];
					setTimeStatus(select[0], select[1]);
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			});
			break;
		case R.id.ll_repeat_set:
			new WeekPickDialog().show(mActivity, weekSum, new RepeatDialogCallBack() {
				@Override
				public void onConfirm(int select) {
					// TODO Auto-generated method stub
					weekSum = select;
					setWeekStatus(select);
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			});
			break;
		case R.id.ll_voice_set:
			break;
		case R.id.yes:
			Alarm alarm1 = new Alarm();
			if (tempAlarm != null) {
				// 修改
				alarm1.id = tempAlarm.id;
			} else {
				//新建
				alarm1.id = 0;
			}
			alarm1.type = AlarmType.GETUP.getValue();
			alarm1.hour = hour;
			alarm1.minute = min;
			alarm1.week = weekSum;

			AirdogAlarm alarmEty1 = new AirdogAlarm();
			List<Alarm> alarms1 = new ArrayList<Alarm>();
			alarms1.add(alarm1);
			alarmEty1.setAlarms(alarms1);

			opEntity.setbEntity(alarmEty1);
			opEntity.setCommand(Command.WTITE);
			opEntity.setFunction(AirdogFC.ALARM.getValue());

			transportCloud(Command.WTITE, EParse.parseparseOPEntity(opEntity));
			break;
		case R.id.iv_delete:
			Alarm alarm2 = tempAlarm;
			AirdogAlarm alarmEty2 = new AirdogAlarm();

			List<Alarm> alarms2 = new ArrayList<Alarm>();
			alarms2.add(alarm2);
			alarmEty2.setAlarms(alarms2);

			opEntity.setbEntity(alarmEty2);
			opEntity.setCommand(Command.DELETE);
			opEntity.setFunction(AirdogFC.ALARM.getValue());

			transportCloud(Command.DELETE, EParse.parseparseOPEntity(opEntity));
			break;
		default:
			break;
		}
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
