package com.beiang.airdog.ui.activity;

import java.io.File;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.beiang.airdog.constant.Constants.AirdogFC;
import com.beiang.airdog.constant.Constants.Command;
import com.beiang.airdog.constant.Constants.Device;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.db.DB_Data;
import com.beiang.airdog.net.business.entity.CurrentDevice;
import com.beiang.airdog.net.business.entity.DevEntity;
import com.beiang.airdog.net.utils.TCPUtils;
import com.beiang.airdog.net.utils.TCPUtils.ITcp;
import com.beiang.airdog.net.utils.UDPUtils;
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirdogMusic;
import com.beiang.airdog.ui.model.AirdogMusic.MFlag;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.CheckSumUtil;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.FileUtils;
import com.beiang.airdog.utils.LogUtil;
import com.beiang.airdog.utils.ReadRaw;
import com.beiang.airdog.utils.Settings;
import com.beiang.airdog.utils.TimerLeftUtil;
import com.beiang.airdog.utils.TimerUtil;
import com.beiang.airdog.utils.audio.AudioRecorder;
import com.beiang.airdog.view.AlertDialog;
import com.beiang.airdog.widget.Toast;
import com.broadlink.beiangair.R;

public class AirdogRecordActivity extends BaseMultiPartActivity implements OnClickListener {
	public final static int STATE_0 = 0, STATE_1 = 1, STATE_2 = 2, STATE_3 = 3, STATE_RECORD = 4, STATE_STOP_RECORD = 5,
			STATE_RECORD_FINISH = 6, STATE_READY_TO_SEND = 7, STATE_MATCH_SEND = 8;

	private int state = 0;
	private int stateCount = 0;

	private TextView net_state,timeleft;
	private ImageView record;
	private DevEntity mDevice;
	OPEntity opEntity;
	DB_Data db_Data;

	byte[] music;
	byte[] tempMusic;
	byte[] startBuffer;
	byte[] localRegBuffer;
	SeekBar seekBar;

	TCPUtils tcpUtils;
	boolean vEnable;//可点击
	boolean isConnected;//连接成功
	boolean recording;//正在录音
	boolean isActivity;//Activity激活

	AudioRecorder recorder;
	File audioFile;
	Handler stateHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airdog_record);
		setMenuEnable(false);
		
		isActivity = true;

		init();
		initView();
		initLocalMusic();
		startRecordState();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/** 初始化数据 */
	void init() {
		db_Data = new DB_Data(mActivity);

		mDevice = CurrentDevice.instance().curDevice;
		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);

		// 初始化tcp
		if (tcpUtils == null) {
			tcpUtils = TCPUtils.instance(new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					byte[] buffer = (byte[]) msg.obj;
					tcpReceive(buffer);
				}
			});
		}

		// 初始化录音
		recorder = new AudioRecorder();

		// 事件分发
		stateHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				handleEventMessage(msg);
			}
		};
	}

	/** 初始化界面 */
	void initView() {
		TextView tv_alarm_tip = null;
		(tv_alarm_tip = (TextView) findViewById(R.id.tv_notice_tip)).setOnClickListener(this);
		if (db_Data.getAlarmTip()) {
			tv_alarm_tip.setSelected(true);
		} else {
			tv_alarm_tip.setSelected(false);
		}

		TextView tv_notice_tip = null;
		(tv_notice_tip = (TextView) findViewById(R.id.tv_alarm_tip)).setOnClickListener(this);
		if (db_Data.getNoticeTip()) {
			tv_notice_tip.setSelected(true);
		} else {
			tv_notice_tip.setSelected(false);
		}

		net_state = (TextView) findViewById(R.id.net_state);
		timeleft= (TextView) findViewById(R.id.timeleft);
		(record = (ImageView) findViewById(R.id.record)).setOnClickListener(this);
		seekBar = (SeekBar) findViewById(R.id.seekbar);
	}

	/***
	 * localMusic()
	 * 
	 * 获取音乐数据
	 */
	void initLocalMusic() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				music = ReadRaw.readRawData(mActivity, R.raw.airdog);
				tempMusic = music;
			}
		}.start();
	}

	/** 开始处理状态 */
	void startRecordState() {
		vEnable = false;
		stateHandler.sendEmptyMessage(STATE_0);
	}

	TimerUtil stateTUtil = new TimerUtil(new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// 失败超时，重新处理此时状态
			switch (state) {
			case STATE_0:
				LogUtil.i("retry: function0_GetHostIp()");
				break;
			case STATE_1:
				LogUtil.i("retry: function1_Connect()");
				break;
			case STATE_2:
				LogUtil.i("retry: function2_LocalRegister()");
				break;
			case STATE_3:
				break;
			default:
				break;
			}
			stateHandler.sendEmptyMessage(state);
		}
	});

	/***
	 * 处理消息事件
	 */
	void handleEventMessage(Message msg) {
		if(!isActivity){
			//已经关闭
			return;
		}
		int what = msg.what;
		// 失败次数过多
		if (stateCount > 10) {
			vEnable = true;
			stateCount = 0;
			stateTUtil.stopTimer();
			return;
		}
		if (state == what) {
			stateCount++;
		}

		switch (what) {
		case STATE_0:
			// 获取IP
			LogUtil.i("function0_GetHostIp()");

			state = STATE_0;
			stateTUtil.startTimer(4 * 1000);
			function0_GetHostIp();
			break;
		case STATE_1:
			// TCP连接
			LogUtil.i("function1_Connect()");

			state = STATE_1;
			stateTUtil.stopTimer();
			stateTUtil.startTimer(4 * 1000);
			function1_Connect();
			break;
		case STATE_2:
			// 本地注册
			LogUtil.i("function2_LocalRegister()");

			state = STATE_2;
			stateTUtil.stopTimer();
			stateTUtil.startTimer(4 * 1000);
			function2_LocalRegister();
			break;
		case STATE_3:
			// 本地注册完成
			LogUtil.i("function3_LocalRegisterComplete()");

			state = STATE_3;
			stateTUtil.stopTimer();
			function3_LocalRegisterComplete();
			break;
		case STATE_RECORD:
			// 开始录音
			LogUtil.i("function_Record()");

			function_Record();
			break;
		case STATE_STOP_RECORD:
			// 停止录音
			LogUtil.i("function_StopRecord()");

			function_StopRecord();
			break;
		case STATE_RECORD_FINISH:
			// 录音结束
			LogUtil.i("function_RecordFinish()");

			function_RecordFinish();
			break;
		case STATE_READY_TO_SEND:
			// 准备发送
			LogUtil.i("function_ReadyToSend()");

			function_ReadyToSend();
			break;
		case STATE_MATCH_SEND:
			LogUtil.i("matchSend()");

			hideDialog();
			matchSend();
			break;
		}
	}

	/** 获取HostIp */
	void function0_GetHostIp() {
		net_state.setTextColor(getResources().getColor(R.color.red));
		net_state.setText("网络未连接");
		UDPUtils.getUdpHost(8089, 4660, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				String ip = (String) msg.obj;
				LogUtil.i("ip =" + ip);
				if (!TextUtils.isEmpty(ip)) {
					hostIp = ip;
					stateHandler.sendEmptyMessage(STATE_1);
				}
			}
		});
	}

	/** TCP连接 */
	void function1_Connect() {
		net_state.setTextColor(getResources().getColor(R.color.red));
		net_state.setText("连接中……");
		tcpUtils.connect(hostIp, 8080, new ITcp() {
			@Override
			public void success() {
				// TODO Auto-generated method stub
				stateHandler.sendEmptyMessageDelayed(STATE_2, 1000);
			}

			@Override
			public void failed() {
				// TODO Auto-generated method stub
				net_state.setTextColor(getResources().getColor(R.color.red));
				net_state.setText("连接失败");
			}
		});
	}

	/** 本地注册 */
	void function2_LocalRegister() {
		localRegBuffer = EParse.localRegister(mActivity);
		LogUtil.i(localRegBuffer);
		tcpUtils.startSend(localRegBuffer);
	}

	/** 本地注册完成 */
	void function3_LocalRegisterComplete() {
		isConnected = true;
		vEnable = true;
		net_state.setTextColor(getResources().getColor(R.color.green));
		net_state.setText("网络连接成功");
	}

	/** 录音 */
	void function_Record() {
		if (audioFile == null) {
			audioFile = new File(Settings.RECORD_PATH);
		}
		recording = true;
		Toast.show(mActivity, "录音中…");
		leftUtil.startTimer(10);
		record.setImageResource(R.drawable.plugin_audio_recorder_stop_selector);
		recorder.startRecord(audioFile, 2, "test");
	}

	/** 停止录音 */
	void function_StopRecord() {
		showDialog("请稍后…");
		leftUtil.stopTimer();
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				recorder.stopRecord();
				stateHandler.sendEmptyMessage(STATE_RECORD_FINISH);
			}
		}.start();
	}

	/** 录音结束 */
	void function_RecordFinish() {
		String filePath = recorder.getRecordFile();
		LogUtil.i("filePath = " + filePath);
		tempMusic = FileUtils.getCodeByFilePath(filePath);
		recording = false;
		record.setImageResource(R.drawable.plugin_audio_recorder_record_selector);

		initSend();
		stateHandler.sendEmptyMessage(STATE_READY_TO_SEND);
	}

	/** 准备可以发送,让硬件擦除空间，存储数据 */
	private void function_ReadyToSend() {
		AirdogMusic musicEty = new AirdogMusic();
		musicEty.setFlag(MFlag.start);
		musicEty.setTotal(total);

		opEntity.setbEntity(musicEty);
		opEntity.setCommand(Command.WTITE);
		opEntity.setFunction(AirdogFC.MUSIC.getValue());
		startBuffer = EParse.parseparseOPEntity(opEntity);
		LogUtil.i("readyToSend:");
		LogUtil.i(startBuffer);
		tcpUtils.startSend(startBuffer);
	}

	/** TCP处理接收数据 */
	void tcpReceive(byte[] buffer) {
		LogUtil.i("receive:");
		LogUtil.i(buffer);
		if (localRegBuffer != null && CheckSumUtil.check(buffer) == CheckSumUtil.check(localRegBuffer)) {
			// 局域网注册完成 -- 准备发送开辟空间
			isConnected = true;
			stateHandler.sendEmptyMessage(STATE_3);
		}
		if (startBuffer != null
				&& CheckSumUtil.check(buffer) == CheckSumUtil.check(new byte[] { (byte) 0xfe, 0x00, 0x0a, 0x00, (byte) 0xa0, 0x00, 0x02,
						0x02, 0x01, (byte) 0xaa })) {
			// 开辟空间完成 -- 可以发送了
			stateHandler.sendEmptyMessageDelayed(STATE_MATCH_SEND, 2000);
		}
		if(CheckSumUtil.check(buffer) == CheckSumUtil.check(new byte[]{0x00,(byte) 0xff})){
			//连接主动断开
			isConnected = false;
			vEnable = false;
			tcpUtils.stopSend();
			stateHandler.sendEmptyMessageDelayed(STATE_1, 2000);
		}
	}

	/** 初始化发送的数据 */
	String hostIp = null;
	int total;
	int len = 512;
	int offset = 0;
	int sect = 0;
	boolean endFlag = true;

	private void initSend() {
		offset = 0;
		sect = 0;
		endFlag = false;

		total = tempMusic.length;
		LogUtil.i("总长度：" + total);
		sect = total / len;
		seekBar.setMax(total);
		seekBar.setProgress(0);
	}

	/***
	 * 拼接需要发送的数据
	 */
	private void matchSend() {
		AirdogMusic musicEty = new AirdogMusic();
		byte[] datapkg = null;
		musicEty.setTotal(total);
		if (offset < sect) {
			int pro = (int) ((offset * len));
			seekBar.setProgress(pro);

			endFlag = false;
			int befor = offset;
			offset++;
			musicEty.setCurLen(offset * len);
			datapkg = new byte[len];
			System.arraycopy(tempMusic, befor * len, datapkg, 0, len);
			musicEty.setData(datapkg);
			sendTask(musicEty);
		} else {
			if (((offset) * len) < total) {
				seekBar.setProgress(total);

				musicEty.setCurLen(total);
				datapkg = new byte[total - ((offset) * len)];
				System.arraycopy(tempMusic, (offset * len), datapkg, 0, total - (offset * len));
				musicEty.setData(datapkg);
				sendTask(musicEty);
				endFlag = true;
			}
		}
	}

	void stopSend() {
		AirdogMusic musicEty = new AirdogMusic();
		musicEty.setFlag(MFlag.stop);
		musicEty.setTotal(total);

		opEntity.setbEntity(musicEty);
		opEntity.setCommand(Command.WTITE);
		opEntity.setFunction(AirdogFC.MUSIC.getValue());
		tcpUtils.startSend(EParse.parseparseOPEntity(opEntity));
	}

	/***
	 * 发送任务
	 * 
	 * @param musicEty
	 */
	private void sendTask(AirdogMusic musicEty) {
		opEntity.setbEntity(musicEty);
		opEntity.setCommand(Command.WTITE);
		opEntity.setFunction(AirdogFC.MUSIC.getValue());
		// transportUdp(Command.SET, EParse.parseparseOPEntity(opEntity));
		// 这里直接发送音乐数据
		transportUdp(Command.SET, musicEty.getData());
	}

	/***
	 * 传输
	 * 
	 * @param comm
	 * @param data
	 */
	private void transportUdp(final Command comm, byte[] data) {
		tcpUtils.startSend(data);
		// 延时
		tUtil.startTimer(50);
	}

	/**定时*/
	TimerUtil tUtil = new TimerUtil(new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (!endFlag) {
				matchSend();
			} else {
				Toast.show(mActivity, "发送完成");
				initSend();
			}
		}
	});
	
	/**倒计时*/
	TimerLeftUtil leftUtil = new TimerLeftUtil(new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if( 0 == msg.what){
				stateHandler.sendEmptyMessage(STATE_STOP_RECORD);
			}
			if(1 == msg.what){
				int arg1 = msg.arg1;
				timeleft.setText(arg1+"");
			}
		}
	});

	/** OnClickListener */
	@Override
	public void onClick(final View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_notice_tip:
			if (v.isSelected()) {
				v.setSelected(false);
				db_Data.saveNoticeTip(false);
			} else {
				v.setSelected(true);
				db_Data.saveNoticeTip(true);
				AlertDialog.show(mActivity, R.drawable.ic_top_header, mDevice.nickName + "提醒主人：" + "\n指数提醒已打开", false, null);
			}
			break;
		case R.id.tv_alarm_tip:
			if (v.isSelected()) {
				v.setSelected(false);
				db_Data.savealArmTip(false);
			} else {
				v.setSelected(true);
				db_Data.savealArmTip(true);
				AlertDialog.show(mActivity, R.drawable.ic_top_header, mDevice.nickName + "提醒主人：" + "\n闹钟播报已打开", false, null);
			}
			break;
		case R.id.record:
			if(!vEnable){
				return;
			}
			if (isConnected) {
				if (recording) {
					stateHandler.sendEmptyMessage(STATE_STOP_RECORD);
				} else {
					stateHandler.sendEmptyMessage(STATE_RECORD);
				}
			} else {
				vEnable = false;
				stateTUtil.stopTimer();
				stateHandler.sendEmptyMessage(state);
			}
			break;
		default:
			break;
		}
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		isActivity = false;
		stateTUtil.stopTimer();
		tcpUtils.stopSend();
		super.onDestroy();
	}

}
