package com.beiang.airdog.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
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
import com.beiang.airdog.view.DatePickDialog;
import com.beiang.airdog.view.DatePickDialog.DatePickDialogCallBack;
import com.beiang.airdog.view.TimerPickDialog;
import com.beiang.airdog.view.TimerPickDialog.TimerPickDialogCallBack;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

/***
 * 提醒闹钟
 * 
 * @author LSD
 * 
 */
public class AirdogNoticeAlarmActivity extends BaseMultiPartActivity implements OnClickListener {
	private DevEntity mDevice;

	TextView tv_alrm_notice;
	TextView tv_meeting;
	TextView tv_rest;
	TextView tv_sleep;
	TextView tv_other;
	TextView tv_define;
	TextView tv_event;
	TextView tv_date;
	TextView tv_timer;
	LinearLayout ll_voice_set;
	TextView tv_voice_set;
	EditText et_content;

	TextView yes;
	ImageView iv_delete;

	int year = 2016;
	int month = 1;
	int day = 22;
	int hour = 8;
	int min = 30;
	int weekSum = 0;

	Alarm tempAlarm;
	int tempType;

	OPEntity opEntity;
	
	int tempTT = 170;
	TextView test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_alarm);
		setMenuEnable(false);

		initView();
		initData();
		setData();
	}

	private void initView() {
		tv_alrm_notice = (TextView) findViewById(R.id.tv_alrm_notice);
		tv_meeting = (TextView) findViewById(R.id.tv_meeting);
		tv_rest = (TextView) findViewById(R.id.tv_rest);
		tv_sleep = (TextView) findViewById(R.id.tv_sleep);
		tv_other = (TextView) findViewById(R.id.tv_other);
		tv_define = (TextView) findViewById(R.id.tv_define);
		tv_event = (TextView) findViewById(R.id.tv_event);
		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_timer = (TextView) findViewById(R.id.tv_timer);
		ll_voice_set = (LinearLayout) findViewById(R.id.ll_voice_set);
		tv_voice_set = (TextView) findViewById(R.id.tv_voice_set);
		et_content = (EditText) findViewById(R.id.et_content);
		yes = (TextView) findViewById(R.id.yes);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);

		tv_alrm_notice.setSelected(true);
		tv_meeting.setOnClickListener(this);
		tv_rest.setOnClickListener(this);
		tv_sleep.setOnClickListener(this);
		tv_other.setOnClickListener(this);
		tv_other.setSelected(true);
		tv_define.setOnClickListener(this);
		tv_event.setOnClickListener(this);
		tv_date.setOnClickListener(this);
		tv_timer.setOnClickListener(this);
		ll_voice_set.setOnClickListener(this);
		yes.setOnClickListener(this);
		iv_delete.setOnClickListener(this);
		findViewById(R.id.test).setOnClickListener(this);
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
			year = alarm.year;
			month = alarm.month;
			day = alarm.day;
			hour = alarm.hour;
			min = alarm.minute;
			weekSum = alarm.week;
			tempType = alarm.type;
			iv_delete.setVisibility(View.VISIBLE);
			yes.setVisibility(View.VISIBLE);
		} else {
			Calendar curCalendar = Calendar.getInstance();
			int curY = curCalendar.get(Calendar.YEAR);
			int curM = curCalendar.get(Calendar.MONTH) + 1;
			int curD = curCalendar.get(Calendar.DAY_OF_MONTH);

			int curH = curCalendar.get(Calendar.HOUR_OF_DAY);
			int curMin = curCalendar.get(Calendar.MINUTE);

			year = curY;
			month = curM;
			day = curD;
			hour = curH;
			min = curMin;
			tempType = AlarmType.ORTHER.getValue();

			iv_delete.setVisibility(View.INVISIBLE);
			yes.setVisibility(View.VISIBLE);
		}

		setDateStatus(year, month, day);
		setTimerStatus(hour, min);
		setTimerType(tempType);
	}

	private void setDateStatus(int year, int mouth, int day) {
		tv_date.setText(String.format("%d", year) + "/" + String.format("%02d", mouth) + "/" + String.format("%02d", day));
	}

	private void setTimerStatus(int hour, int min) {
		tv_timer.setText(String.format("%02d", hour) + ":" + String.format("%02d", min));
	}

	private void setTimerType(int type) {
		reset();
		if (AlarmType.MEETING.getValue() == type) {
			tv_meeting.setSelected(true);
		} else if (AlarmType.REST.getValue() == type) {
			tv_rest.setSelected(true);
		} else if (AlarmType.SLEEP.getValue() == type) {
			tv_sleep.setSelected(true);
		}else if (AlarmType.DEFINE.getValue() == type) {
			tv_define.setSelected(true);
		}else if (AlarmType.ORTHER.getValue() == type) {
			tv_other.setSelected(true);
		} else if (AlarmType.EVENT.getValue() == type) {
			tv_event.setSelected(true);
		}
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
						//LogUtil.i(rsp.reply);
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
		case R.id.tv_date:
			new DatePickDialog().show(mActivity, year, month, day, new DatePickDialogCallBack() {
				@Override
				public void onConfirm(Integer[] select) {
					// TODO Auto-generated method stub
					year = select[0];
					month = select[1];
					day = select[2];
					setDateStatus(year, month, day);
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			});
			break;
		case R.id.tv_timer:
			new TimerPickDialog().show(mActivity, hour, min, new TimerPickDialogCallBack() {
				@Override
				public void onConfirm(Integer[] select) {
					// TODO Auto-generated method stub
					hour = select[0];
					min = select[1];
					setTimerStatus(hour, min);
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}

			});
			break;
		case R.id.tv_alrm_notice:
			if (v.isSelected()) {
				v.setSelected(false);
			} else {
				v.setSelected(true);
			}
			break;
		case R.id.tv_meeting:
			reset();
			v.setSelected(true);
			tempType = AlarmType.MEETING.getValue();
			break;
		case R.id.tv_rest:
			reset();
			v.setSelected(true);
			tempType = AlarmType.REST.getValue();
			break;
		case R.id.tv_sleep:
			reset();
			v.setSelected(true);
			tempType = AlarmType.SLEEP.getValue();
			break;
		case R.id.tv_other:
			reset();
			v.setSelected(true);
			tempType = AlarmType.ORTHER.getValue();
			break;
		case R.id.tv_define:
			reset();
			v.setSelected(true);
			tempType = AlarmType.DEFINE.getValue();
			break;
		case R.id.tv_event:
			reset();
			v.setSelected(true);
			tempType = AlarmType.EVENT.getValue();
			break;
		case R.id.ll_voice_set:
			if(tempType == AlarmType.DEFINE.getValue()){
				startActivity(new Intent(mActivity,AirdogRecordActivity.class));
			}
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
			alarm1.type = tempType;
			alarm1.year = year;
			alarm1.month = month;
			alarm1.day = day;
			alarm1.hour = hour;
			alarm1.minute = min;

			AirdogAlarm alarmEty1 = new AirdogAlarm();
			List<Alarm> alarms1 = new ArrayList<Alarm>();
			alarms1.add(alarm1);
			alarmEty1.setAlarms(alarms1);

			opEntity.setbEntity(alarmEty1);
			opEntity.setCommand(Command.WTITE);
			opEntity.setFunction(AirdogFC.ALARM.getValue());

			transportCloud(Command.WTITE, EParse.parseparseOPEntity(opEntity));
			break;
			case R.id.test:
				Alarm alarmTest = new Alarm();
				//新建
				alarmTest.id = 0;
				
				alarmTest.type = tempType;
				alarmTest.year = year;
				alarmTest.month = month;
				alarmTest.day = day;
				alarmTest.hour = hour;
				alarmTest.minute = min;

				AirdogAlarm alarmEntityTest = new AirdogAlarm();
				List<Alarm> alarmsTest = new ArrayList<Alarm>();
				alarmsTest.add(alarmTest);
				alarmEntityTest.setAlarms(alarmsTest);

				opEntity.setbEntity(alarmEntityTest);
				opEntity.setCommand(Command.TEST);
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

	private void reset() {
		tv_meeting.setSelected(false);
		tv_rest.setSelected(false);
		tv_sleep.setSelected(false);
		tv_other.setSelected(false);
		tv_define.setSelected(false);
		tv_event.setSelected(false);
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
