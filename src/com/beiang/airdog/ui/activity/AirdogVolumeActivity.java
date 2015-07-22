package com.beiang.airdog.ui.activity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
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
import com.beiang.airdog.ui.base.BaseMultiPartActivity;
import com.beiang.airdog.ui.model.AirdogVolume;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.ClickUtil;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.LogUtil;
import com.broadlink.beiangair.R;

public class AirdogVolumeActivity extends BaseMultiPartActivity {
	private DevEntity mDevice;
	private OPEntity opEntity;
	private AirdogVolume aVolume;
	private TextView tv_cur_volume;
	private SeekBar vSeekBar;
	
	boolean needRefrash = true;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_airdog_volume);
		setMenuEnable(false);

		initView();
		initData();
		queryData();
	}

	/**初始化界面*/
	private void initView() {
		tv_cur_volume = (TextView) findViewById(R.id.tv_cur_volume);
		vSeekBar = (SeekBar) findViewById(R.id.iv_volume_adjust);
		vSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if (progress % 10 == 0) {
					if(ClickUtil.isFastDoubleClick()){
						return;
					}
					if(needRefrash){
						return;
					}
					tv_cur_volume.setText("当前音量：" + progress + "%");
					
					aVolume.setVolume(progress);
					opEntity.setbEntity(aVolume);
					opEntity.setCommand(Command.WTITE);
					opEntity.setFunction(AirdogFC.VOLUME.getValue());
					
					transportCloud(EParse.parseparseOPEntity(opEntity));
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int progress = seekBar.getProgress();
				int mInteger = progress / 10;
				int mRemainder = progress % 10;
				if (mRemainder >= 5) {
					seekBar.setProgress(((mInteger + 1) * 10) > 100 ? 100 : ((mInteger + 1) * 10));
				} else {
					seekBar.setProgress((mInteger) * 10);
				}
			}
		});
	}

	/**初始化数据*/
	void initData() {
		mDevice = CurrentDevice.instance().curDevice;
		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setSubDevType(SubDevice.SDT_Airdog);

		aVolume = new AirdogVolume();
	}

	
	/**查询数据*/
	private void queryData() {
		needRefrash = true;
		
		opEntity.setCommand(Command.READ);
		opEntity.setFunction(AirdogFC.VOLUME.getValue());
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
						// LogUtil.i(rsp.reply);
						OPEntity opEntity = EParse.parseOPBytes(rsp.reply);
						if (opEntity != null) {
							aVolume = (AirdogVolume) opEntity.getbEntity();
							setDataToView();
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
	

	/**设置数据*/
	private void setDataToView() {
		if (aVolume != null) {
			int volume = aVolume.getVolume();
			String newVolume = "当前音量：" + volume + "%";
			String oldVolume = tv_cur_volume.getText().toString();
			if (oldVolume.equals(newVolume)) {
				needRefrash = false;
			} else {
				needRefrash = true;
			}
			if (needRefrash) {
				tv_cur_volume.setText(newVolume);

				int mInteger = volume / 10;
				int mRemainder = volume % 10;
				if (mRemainder >= 5) {
					vSeekBar.setProgress(((mInteger + 1) * 10) > 100 ? 100 : ((mInteger + 1) * 10));
				} else {
					vSeekBar.setProgress((mInteger) * 10);
				}
				needRefrash = false;
			}
		}
	}
}
