package com.beiang.airdog.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
import com.beiang.airdog.ui.model.AirConditionEntity;
import com.beiang.airdog.ui.model.AirConditionEntity.Model;
import com.beiang.airdog.ui.model.AirConditionEntity.Power;
import com.beiang.airdog.ui.model.AirConditionEntity.Swing;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.utils.EParse;
import com.beiang.airdog.utils.Helper;
import com.beiang.airdog.utils.LogUtil;
import com.broadlink.beiangair.R;

public class AirDogControlActivity extends BaseMultiPartActivity implements OnClickListener {
	private DevEntity mDevice;
	private OPEntity opEntity;
	private AirConditionEntity airEntity;
	private Button bt_power, bt_temp_up, bt_temp_down, bt_speed, bt_swing,bt_model;
	private TextView tv;
	
	int lastSpeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.airdog_control);

		initView();
		initData();
		
		query();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initView() {
		bt_power = (Button) findViewById(R.id.bt_power);
		bt_temp_up = (Button) findViewById(R.id.bt_temper_add);
		bt_temp_down = (Button) findViewById(R.id.bt_temper_sub);
		bt_speed = (Button) findViewById(R.id.bt_speeder);
		bt_swing = (Button) findViewById(R.id.bt_swinger);
		bt_model = (Button) findViewById(R.id.bt_modeler);

		bt_power.setOnClickListener(this);
		bt_temp_up.setOnClickListener(this);
		bt_temp_down.setOnClickListener(this);
		bt_speed.setOnClickListener(this);
		bt_swing.setOnClickListener(this);
		bt_model.setOnClickListener(this);
		
		tv = (TextView) findViewById(R.id.tv);
	}

	void initData() {
		mDevice = CurrentDevice.instance().curDevice;
		opEntity = new OPEntity();
		opEntity.setDevType(Device.DT_Airdog);
		opEntity.setCommand(Command.WTITE);
		opEntity.setSubDevType(SubDevice.SDT_AirCondition);
		
		airEntity = new AirConditionEntity();
	}
	
	void setDataToView(){
		if(airEntity != null){
			StringBuffer sBuffer = new StringBuffer();
			int power = airEntity.getPower();
			if(power == Power.off){
				sBuffer.append("开关状态 ："+"关");
			}else{
				sBuffer.append("开关状态 ："+"开");
			}
			
			sBuffer.append("\n");
			
			int mode = airEntity.getModel();
			if (mode == Model.auto) {
				sBuffer.append("模式："+"自动");
			}
			if (mode == Model.cold) {
				sBuffer.append("模式："+"制冷");
			}
			if (mode == Model.wet) {
				sBuffer.append("模式："+"抽湿");
			}
			if (mode == Model.hot) {
				sBuffer.append("模式："+"制热");
			}
			
			sBuffer.append("\n");
			
			int speed = airEntity.getSpeed();
			sBuffer.append("风速："+speed+"档");
			
			sBuffer.append("\n");
			
			int swing = airEntity.getSwing();
			if (swing == Swing.off) {
				sBuffer.append("摆风："+"关");
			}
			if (swing == Swing.on) {
				sBuffer.append("摆风："+"开");
			}
			
			sBuffer.append("\n");
			int temp = airEntity.getTemp();
			sBuffer.append("温度："+(temp+16));
			
			tv.setText(sBuffer.toString());
		}
		
	}
	
	void query(){
		int i=0;
		byte[] b = new byte[128];
		//针头
		b[i++] = (byte) 0xfe;
		
		//长度
		b[i++] = 0x00;
		b[i++] = 0x00;
		
		//设备类型
		byte[] type = Helper.decimalToHex2Bytes(opEntity.getDevType());
		b[i++] = type[0];
		b[i++] = type[1];
		
		//设备子类型
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(opEntity.getSubDevType());
		
		//命令
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(Command.READ.getValue());
		b[i++] = 0x00;
		
		//尾针
		b[i++]= (byte) 0xaa;
		
		// 计算长度
		byte[] len = Helper.decimalToHex2Bytes(i);
		b[1] = len[0];
		b[2] = len[1];
				
		byte[] sendB = new byte[i];
		System.arraycopy(b, 0, sendB, 0, i);
		b = null;
		LogUtil.i(sendB);
		queryBeiAngAir(sendB);
	}
	

	private void controlBeiAngAir(byte[] data) {
		LogUtil.i("控制");
		LogUtil.i(data);
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					CommandPair.RspCommand rsp = (CommandPair.RspCommand) rspData;
					if (rsp.reply != null) {
						LogUtil.i("控制返回");
						LogUtil.i(rsp.reply);
						OPEntity opEntity = EParse.parseOPBytes(rsp.reply);
						if(opEntity != null){
							airEntity = (AirConditionEntity) opEntity.getbEntity();
						}
						
						setDataToView();
					}
				}
			}

			@Override
			public void onFailure(ErrorObject err) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void queryBeiAngAir(byte[] data) {
		BsOperationHub.instance().sendCtrlCmd(mDevice.devId, data, new ReqCbk<RspMsgBase>() {
			@Override
			public void onSuccess(RspMsgBase rspData) {
				// TODO Auto-generated method stub
				if (rspData.isSuccess()) {
					CommandPair.RspCommand rsp = (CommandPair.RspCommand) rspData;
					if (rsp.reply != null) {
						LogUtil.i(rsp.reply);
						OPEntity opEntity = EParse.parseOPBytes(rsp.reply);
						airEntity = (AirConditionEntity) opEntity.getbEntity();
						lastSpeed = airEntity.getSpeed();
						
						setDataToView();
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
	 * OnClickListener
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_power:
			int power = airEntity.getPower();
			if(power == AirConditionEntity.Power.off){
				power = AirConditionEntity.Power.on;
			}else{
				power = AirConditionEntity.Power.off;
			}
			airEntity.setPower(power);
			break;
		case R.id.bt_temper_add:
			int temp = airEntity.getTemp();
			//if(temp <30){
				temp  = temp+1;
			///}
			airEntity.setTemp(temp);
			break;
		case R.id.bt_temper_sub:
			int tempdown = airEntity.getTemp();
			//if(tempdown >16){
				tempdown  = tempdown -1;
			//}
			airEntity.setTemp(tempdown);
			break;
		case R.id.bt_speeder:
			int speed = airEntity.getSpeed();
			if(lastSpeed < AirConditionEntity.Speed.level4){
				if(lastSpeed == speed){
					speed = speed+1;
				}
			}else{
				speed = 0;
			}
			/*if(speed == AirConditionEntity.Speed.level4){
				speed = 0;
			}else{
				if(speed < AirConditionEntity.Speed.level4){
					speed =speed+1;
				}
			}*/
			lastSpeed = speed;
			airEntity.setSpeed(speed);
			break;
		case R.id.bt_swinger:
			int swing = airEntity.getSwing();
			if(swing == AirConditionEntity.Swing.off){
				swing = AirConditionEntity.Swing.on;
			}else{
				swing = AirConditionEntity.Swing.off;
			}
			airEntity.setSwing(swing);
			break;
		case R.id.bt_modeler:
			int model = airEntity.getModel();
			if (model == AirConditionEntity.Model.hot) {
				model = 0;
			}else{
				if (model < AirConditionEntity.Model.hot) {
					model = model+1;
				}
			}
			airEntity.setModel(model);
			break;
		default:
			break;
		}
		opEntity.setbEntity(airEntity);
		controlBeiAngAir(EParse.parseparseOPEntity(opEntity));
	}
}
