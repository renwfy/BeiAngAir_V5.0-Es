package com.beiang.airdog.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.beiang.airdog.constant.Constants.AirdogFC;
import com.beiang.airdog.constant.Constants.Function;
import com.beiang.airdog.constant.Constants.SubDevice;
import com.beiang.airdog.ui.activity.IHomerDeviceDetailsActivity.TESS;
import com.beiang.airdog.ui.model.AirConditionEntity;
import com.beiang.airdog.ui.model.AirInfo;
import com.beiang.airdog.ui.model.AirdogAlarm;
import com.beiang.airdog.ui.model.AirdogAlarm.Alarm;
import com.beiang.airdog.ui.model.AirdogCycle;
import com.beiang.airdog.ui.model.AirdogMusic;
import com.beiang.airdog.ui.model.AirdogVolume;
import com.beiang.airdog.ui.model.BusinessEntity;
import com.beiang.airdog.ui.model.CommEntity;
import com.beiang.airdog.ui.model.FirmwareEntity;
import com.beiang.airdog.ui.model.OPEntity;
import com.beiang.airdog.ui.model.PowerSocketEntity;
import com.beiang.airdog.ui.model.ScriptEntity;

public class EParse {

	/***
	 * 
	 * 解析25个的字节
	 * 
	 * byte[] --> AirInfo
	 * 
	 * @param data
	 * @return
	 */
	public static AirInfo parseEairByte(byte[] data) {
		if (data == null) {
			return null;
		}
		AirInfo info = null;
		if (data.length == 25) {
			info = new AirInfo();
			// 设备类型
			byte type = data[3];
			info.setDeviceType(Helper.hexadeToDecimal(type));

			// 开关机
			byte powerOn = data[4];
			info.setOnoff(Helper.hexadeToDecimal(powerOn));

			// 手自动
			byte isAuto = data[5];
			info.setIsAuto(Helper.hexadeToDecimal(isAuto));

			// 档位
			byte speed = data[6];
			info.setAirSpeed(Helper.hexadeToDecimal(speed));

			// 睡眠状态
			byte sleep = data[7];
			info.setSleep(Helper.hexadeToDecimal(sleep));

			// 儿童锁
			byte childLock = data[8];
			info.setChildLock(Helper.hexadeToDecimal(childLock));

			// 电极运行时间
			byte runTimeH = data[9];// 小时
			byte runTimeM = data[10];// 分钟
			info.setElectRunTimeHour(Helper.hexadeToDecimal(runTimeH));
			info.setElectRunTimeMin(Helper.hexadeToDecimal(runTimeM));

			// 空气质量
			byte airQuality = data[11];
			info.setAirLevel(Helper.hexadeToDecimal(airQuality));

			// 空气质量原始数值
			byte airValueH = data[12];
			byte airValueL = data[13];
			info.setAirValue((Helper.hexadeToDecimal(airValueH) * 256 + Helper.hexadeToDecimal(airValueL)));

			// 光照状态
			byte lightStatu = data[14];
			info.setLight(Helper.hexadeToDecimal(lightStatu));

			// 维护状态
			byte service = data[15];
			info.setErr(Helper.hexadeToDecimal(service));

			// 温度
			byte temp = data[16];
			info.setTem(Helper.hexadeToDecimal(temp));

			// 湿度
			byte humidity = data[17];
			info.setHum(Helper.hexadeToDecimal(humidity));

			// TVOC
			byte tvoc = data[18];
			info.setAirTvoc(Helper.hexadeToDecimal(tvoc));

			// signal信号强度
			byte signal = data[19];
			info.setSignal(Helper.hexadeToDecimal(signal));
			
			// 预留
			/*
			 * receive[20]; receive[21]; receive[22]; receive[23];
			 */
			// receive[24];结束位
		}
		return info;
	}

	/***
	 * 
	 * 解析模型数据 25个字节
	 * 
	 * AirInfo --> byte[]
	 * 
	 * @param info
	 * @return
	 */
	public static byte[] parseEairInfo(AirInfo info) {
		if (info == null) {
			return null;
		}
		byte[] sendB = new byte[25];
		// 针头
		sendB[0] = (byte) 0xfe;
		// 控制设备
		sendB[1] = 0x41;

		// 设备类型
		sendB[2] = 0x00;
		if (info != null && info.getDeviceType() != 0) {
			sendB[3] = 0x00;
			byte[] type = Helper.decimalToHex2Bytes(info.getDeviceType());
			sendB[2] = type[0];
			sendB[3] = type[1];
		}

		// 开关机
		sendB[4] = Helper.ConvertIntTo1byteHexaFormat(info.getOnoff());

		// 手自动
		sendB[5] = Helper.ConvertIntTo1byteHexaFormat(info.getIsAuto());

		// 档位
		sendB[6] = Helper.ConvertIntTo1byteHexaFormat(info.getAirSpeed());

		// 睡眠状态
		sendB[7] = Helper.ConvertIntTo1byteHexaFormat(info.getSleep());

		// 儿童锁
		sendB[8] = Helper.ConvertIntTo1byteHexaFormat(info.getChildLock());

		// 电极运行时间
		sendB[9] = 0x00;// 小时
		sendB[10] = 0x00;// 分钟

		// 电极运行时间
		sendB[9] = Helper.ConvertIntTo1byteHexaFormat(info.getElectRunTimeHour());// 小时
		sendB[10] = Helper.ConvertIntTo1byteHexaFormat(info.getElectRunTimeMin());// 分钟

		// 空气质量
		sendB[11] = 0x00;

		// 空气质量原始数值
		sendB[12] = 0x00;
		sendB[13] = 0x00;

		// 光照状态
		sendB[14] = 0x00;

		// 维护状态
		sendB[15] = 0x00;

		// 温度
		sendB[16] = 0x00;

		// 湿度
		sendB[17] = 0x00;

		// TVOC
		sendB[18] = 0x00;

		// signal信号强度
		sendB[19] = 0x00;

		// 预留
		sendB[20] = 0x00;
		sendB[21] = 0x00;
		sendB[22] = 0x00;

		// 清洁复位
		sendB[23] = Helper.ConvertIntTo1byteHexaFormat(info.getClean());

		// 尾针
		sendB[24] = (byte) 0xaa;

		return sendB;
	}


	private static byte[] parseVolumeControl(AirInfo info) {
		byte[] control = parseEairInfo(info);
		// 小狗 0：状态数据 1：控制数据
		control[4] = 0x00;
		return control;
	}

	/***
	 * 解析服务器返回的byte[]数据
	 * 
	 * byte[] --> OPEntity
	 * 
	 * @param data
	 * @return
	 */
	public static OPEntity parseOPBytes(byte[] data) {
		if (data == null) {
			return null;
		}
		try {
			OPEntity opEntity = new OPEntity();
			int i = 0;
			// 针头
			byte head = data[i++];

			// 数据长度
			byte lenH = data[i++];
			byte lenL = data[i++];

			// 设备类型
			byte devTypeH = data[i++];
			byte devTypeL = data[i++];
			opEntity.setDevType((Helper.hexadeToDecimal(devTypeH) * 256 + Helper.hexadeToDecimal(devTypeL)));

			// 设备子类型
			byte subDevType = data[i++];
			opEntity.setSubDevType(Helper.hexadeToDecimal(subDevType));

			// 命令类型
			byte comH = data[i++];// 读写
			byte comL = data[i++];// 功能码
			//opEntity.setCommand(Command.WTITE);
			opEntity.setFunction(Helper.hexadeToDecimal(comL));

			// 具体数据信息
			int len = data.length - i - 1;
			byte[] bsBytes = new byte[len];
			System.arraycopy(data, i, bsBytes, 0, len);
			opEntity.setbEntity(parseBusinessBytes(opEntity, bsBytes));

			// 针尾
			byte foot = data[i + len];
			return opEntity;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 
	 * 解析模型操作模型数据
	 * 
	 * OPEntity --> byte[]
	 * 
	 * @param entity
	 * @return
	 */
	public static byte[] parseparseOPEntity(OPEntity entity) {
		if (entity == null) {
			return null;
		}
		int i = 0;
		byte[] b = new byte[128];
		// 针头
		b[i++] = (byte) 0xfe;

		// 数据包长度
		b[i++] = 0x00;
		b[i++] = 0x00;

		// 设备类型
		byte[] type = Helper.decimalToHex2Bytes(entity.getDevType());
		b[i++] = type[0];
		b[i++] = type[1];

		// 设备子类型
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(entity.getSubDevType());

		// 命令
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(entity.getCommand().getValue());// 读/写
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(entity.getFunction());// 功能码

		// 数据
		byte[] business = parseBusinessEntity(entity);

		byte[] sendB = null;
		// 计算长度
		byte[] len = Helper.decimalToHex2Bytes(business.length + i + 1);
		b[1] = len[0];
		b[2] = len[1];

		// 截取需要发送的数据
		byte[] dataB = new byte[i];
		System.arraycopy(b, 0, dataB, 0, i);
		b = null;

		// 合并数据
		sendB = Helper.byteMerger(dataB, business);

		byte[] footB = new byte[] { (byte) 0xaa };
		sendB = Helper.byteMerger(sendB, footB);

		return sendB;
	}

	/***
	 * 解析服务器返回的设备byte[]数据
	 * 
	 * byte[] --> BusinessEntity
	 * 
	 * @param data
	 * @return
	 */
	private static BusinessEntity parseBusinessBytes(OPEntity opEntity, byte[] data) {
		BusinessEntity bEntity = null;
		int subDevType = opEntity.getSubDevType();
		if (subDevType == SubDevice.SDT_AirCondition) {
			bEntity = parseAirConditionBytes(data);
		}
		if (subDevType == SubDevice.SDT_AirPurifier) {
		}
		if (subDevType == SubDevice.SDT_PowerSocket) {
			bEntity = parsePowerSocketBytes(data);
		}
		if (subDevType == SubDevice.SDT_Airdog) {
			int function = opEntity.getFunction();
			if (function == AirdogFC.VOLUME.getValue()) {
				//小狗音量
				bEntity = parseAirdogVolmeBytes(data);
			}
			if (function == AirdogFC.ALARM.getValue()) {
				//小狗闹钟
				bEntity = parseAirdogAlarmBytes(data);
			}
			if (function == AirdogFC.MUSIC.getValue()) {
				//小狗音乐
				bEntity = parseAirdogMusicBytes(data);
			}
		}
		return bEntity;
	}

	/***
	 * 解析往服务器发送的设备模型数据
	 * 
	 * OPEntity --> byte[]
	 * 
	 * @param opEntity
	 * @param data
	 * @return
	 */
	private static byte[] parseBusinessEntity(OPEntity opEntity) {
		byte[] etyBytes = null;
		int subDevType = opEntity.getSubDevType();
		if (subDevType == SubDevice.SDT_AirCondition) {
			etyBytes = parseAirConditionEty(opEntity.getbEntity());
		}
		if (subDevType == SubDevice.SDT_AirPurifier) {
			int function = opEntity.getFunction();
			if(function == Function.FIRMWARE.getValue()){
				//固件
				etyBytes = parseFirmware(opEntity.getbEntity());
			}else{
				etyBytes = parseAirCleanEty(opEntity.getbEntity());
			}
		}
		if (subDevType == SubDevice.SDT_PowerSocket) {
			etyBytes = parsePowerSocketEty(opEntity.getbEntity());
		}
		if (subDevType == SubDevice.SDT_Airdog) {
			int function = opEntity.getFunction();
			if (function == AirdogFC.VOLUME.getValue()) {
				//小狗音量
				etyBytes = parseAirdogVolmeEty(opEntity.getbEntity());
			}
			if (function == AirdogFC.ALARM.getValue()) {
				//小狗闹钟
				etyBytes = parseAirdogAlarmEty(opEntity.getbEntity());
			}
			if(function == AirdogFC.MUSIC.getValue()){
				//小狗音乐
				etyBytes = parseAirdogMusicEty(opEntity.getbEntity());
			}
			if(function == AirdogFC.CYCLE.getValue()){
				//小狗自控制
				etyBytes = parseAirdogCycle(opEntity.getbEntity());
			}
			if(function == AirdogFC.SCRIPT.getValue()){
				//脚本
				etyBytes = parseAirdogScript(opEntity.getbEntity());
			}
			if(function == Function.FIRMWARE.getValue()){
				//固件
				etyBytes = parseFirmware(opEntity.getbEntity());
			}
			if(function == AirdogFC.YELP.getValue()){
				//狗吠
				etyBytes = parseYelp(opEntity.getbEntity());
			}
		}
		return etyBytes;
	}

	/***
	 * 解析服务器返回的空调byte[]数据
	 * 
	 * byte[]--> AirConditionEntity
	 * 
	 * @param data
	 * @return
	 */
	private static BusinessEntity parseAirConditionBytes(byte[] data) {
		AirConditionEntity bsEntity = null;
		if (data == null) {
			return null;
		}
		
		try {
			bsEntity = new AirConditionEntity();
			int i = 0;
			// 开关
			byte power = data[i++];
			bsEntity.setPower(Helper.hexadeToDecimal(power));

			// 模式
			byte model = data[i++];
			bsEntity.setModel(Helper.hexadeToDecimal(model));

			// 温度
			byte temp = data[i++];
			bsEntity.setTemp(Helper.hexadeToDecimal(temp));

			// 风速
			byte speed = data[i++];
			bsEntity.setSpeed(Helper.hexadeToDecimal(speed));

			// 摆风
			byte swing = data[i++];
			bsEntity.setSwing(Helper.hexadeToDecimal(swing));

			// 预留
			// data[i++];
			// data[i++]
			// data[i++]
			
			return bsEntity;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/***
	 * 解析空调控制模型数据
	 * 
	 * AirConditionEntity -->byte[]
	 * 
	 * @param entity
	 * @return
	 */
	private static byte[] parseAirConditionEty(BusinessEntity entity) {
		byte[] aircon = null;
		AirConditionEntity airEntity = (AirConditionEntity) entity;
		if (entity == null) {
			// 空数据8位
			return new byte[8];
		}

		int i = 0;
		aircon = new byte[128];

		// 开关机
		aircon[i++] = Helper.ConvertIntTo1byteHexaFormat(airEntity.getPower());

		// 模式
		aircon[i++] = Helper.ConvertIntTo1byteHexaFormat(airEntity.getModel());

		// 温度
		aircon[i++] = Helper.ConvertIntTo1byteHexaFormat(airEntity.getTemp());

		// 风速
		aircon[i++] = Helper.ConvertIntTo1byteHexaFormat(airEntity.getSpeed());

		// 摆风
		aircon[i++] = Helper.ConvertIntTo1byteHexaFormat(airEntity.getSwing());

		// 预留
		aircon[i++] = 0x00;
		aircon[i++] = 0x00;
		aircon[i++] = 0x00;

		byte[] dataB = new byte[i];
		System.arraycopy(aircon, 0, dataB, 0, i);
		aircon = null;
		return dataB;
	}
	
	/***
	 * 解析原来的25个字节
	 * 
	 * @param entity
	 * @return
	 */
	private static byte[] parseAirCleanEty(BusinessEntity entity) {
		AirInfo info = (AirInfo) entity;
		return parseEairInfo(info);
	}
	
	private static byte[] parsePowerSocketEty(BusinessEntity entity) {
		byte[] infoBytes = null;
		PowerSocketEntity info = (PowerSocketEntity) entity;
		if (entity == null) {
			// 空数据1位
			return new byte[1];
		}

		int i = 0;
		infoBytes = new byte[128];

		// 开关机
		infoBytes[i++] = Helper.ConvertIntTo1byteHexaFormat(Integer.parseInt(info.getState()));

		byte[] dataB = new byte[i];
		System.arraycopy(infoBytes, 0, dataB, 0, i);
		infoBytes = null;
		return dataB;
	}

	/***
	 * 解析小狗音量byte数据 
	 * 
	 * --> BusinessEntity
	 * 
	 * @param data
	 * @return
	 */
	private static BusinessEntity parseAirdogVolmeBytes(byte[] data) {
		AirdogVolume volumeEty = new AirdogVolume();
		if (data == null) {
			return null;
		}

		byte volume = data[0];
		volumeEty.setVolume(Helper.hexadeToDecimal(volume));
		return volumeEty;
	}

	/***
	 * 解析小狗音量模型数据
	 * 
	 * -->byte[]
	 * 
	 * @param entity
	 * @return
	 */
	private static byte[] parseAirdogVolmeEty(BusinessEntity entity) {
		byte[] airVolme = null;
		AirdogVolume volumeEty = (AirdogVolume) entity;
		if (entity == null) {
			// 空数据8位
			return new byte[1];
		}
		airVolme = new byte[1];
		airVolme[0] = Helper.ConvertIntTo1byteHexaFormat(volumeEty.getVolume());
		return airVolme;
	}
	
	/***
	 * 小狗音乐
	 * 
	 * @param entity
	 * @return
	 */
	private static byte[] parseAirdogMusicEty(BusinessEntity entity) {
		byte[] music = null;
		AirdogMusic musicEty = (AirdogMusic) entity;
		if (entity == null) {
			return new byte[1];
		}
		
		music = new byte[1024];
		int i = 0;
		music[i++] = Helper.ConvertIntTo1byteHexaFormat(musicEty.getFlag());
		byte totalLen[] = Helper.ConvertIntTo4bytesHexaFormat(musicEty.getTotal());
		music[i++] = totalLen[0];
		music[i++] = totalLen[1];
		music[i++] = totalLen[2];
		music[i++] = totalLen[3];
		
		/*
		byte totalLen[] = Helper.ConvertIntTo4bytesHexaFormat(musicEty.getTotal());
		music[i++] = totalLen[0];
		music[i++] = totalLen[1];
		music[i++] = totalLen[2];
		music[i++] = totalLen[3];
		byte curLen[] = Helper.ConvertIntTo4bytesHexaFormat(musicEty.getCurLen());
		music[i++] = curLen[0];
		music[i++] = curLen[1];
		music[i++] = curLen[2];
		music[i++] = curLen[3];
		byte[] data = musicEty.getData();
		for (int x = 0; x < data.length; x++) {
			music[i++] = data[x];
		}
		*/
		
		byte[] sendB = new byte[i];
		System.arraycopy(music, 0, sendB, 0, i);
		return sendB;
	}
	
	private static byte[] parseAirdogCycle(BusinessEntity entity) {
		AirdogCycle cycle = (AirdogCycle) entity;
		if (entity == null) {
			return new byte[1];
		}
		byte[] bys = new byte[1];
		bys[0] = Helper.ConvertIntTo1byteHexaFormat(cycle.getIsCylce());
		return bys;
	}
	
	private static byte[] parseFirmware(BusinessEntity entity) {
		FirmwareEntity info = (FirmwareEntity) entity;
		if (entity == null) {
			// 空数据1位
			return new byte[1];
		}

		// 对象
		byte targetByte = (byte) info.type;
		byte[] urlBytes = info.url.getBytes();
		return Helper.byteMerger(new byte[]{targetByte}, urlBytes);
	}
	
	private static byte[] parseYelp(BusinessEntity entity) {
		CommEntity info = (CommEntity) entity;
		if (entity == null) {
			// 空数据1位
			return new byte[1];
		}

		// 对象
		byte vale = (byte) info.value;
		return new byte[]{vale};
	}
	
	private static byte[] parseAirdogScript(BusinessEntity entity) {
		ScriptEntity ety = (ScriptEntity) entity;
		return ety.getData();
	}
	
	private static byte[] parseAirdogAlarmEty(BusinessEntity entity) {
		byte[] airAlarm = null;
		AirdogAlarm alarmEty = (AirdogAlarm) entity;
		if (entity == null) {
			// 空数据8位
			return new byte[1];
		}
		airAlarm = new byte[128];
		int i = 0;
		List<Alarm> alarms = alarmEty.getAlarms();
		//个数
		//airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarms.size());//添加的时候不需要
		
		int count = 0;
		while (count < alarms.size()) {
			Alarm alarm = alarms.get(count);
			//seq
			//airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(count);//添加的时候不需要
			
			//id
			airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarm.getId());
			
			//type
			airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarm.getType());
			
			//year
			byte[] year = Helper.decimalToHex2Bytes(alarm.getYear());
			airAlarm[i++] = year[0];
			airAlarm[i++] = year[1];
			
			//month
			airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarm.getMonth());
			
			//day
			airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarm.getDay());
			
			//week
			airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarm.getWeek());
			
			//hour
			airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarm.getHour());
			
			//min
			airAlarm[i++] = Helper.ConvertIntTo1byteHexaFormat(alarm.getMinute());
			
			//music
			airAlarm[i++] =0x00;
			
			//temp
			airAlarm[i++] =0x00;
			airAlarm[i++] =0x00;
			
			count ++;
		}
		byte[] dataB = new byte[i];
		System.arraycopy(airAlarm, 0, dataB, 0, i);
		airAlarm = null;
		return dataB;
	}
	
	private static BusinessEntity parseAirdogMusicBytes(byte[] data) {
		AirdogMusic musicEty = new AirdogMusic();
		if (data == null) {
			return null;
		}
		musicEty.setData(data);
		return musicEty;
	}
	
	private static BusinessEntity parsePowerSocketBytes(byte[] data) {
		PowerSocketEntity bsEntity = null;
		if (data == null) {
			return null;
		}
		try {
			bsEntity = new PowerSocketEntity();
			int i = 0;
			// 开关
			byte power = data[i++];
			bsEntity.setState(""+Helper.hexadeToDecimal(power));
			return bsEntity;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	
	/***
	 * 解析小狗闹钟byte数据
	 * --> BusinessEntity
	 * 
	 * @param data
	 * @return
	 */
	private static BusinessEntity parseAirdogAlarmBytes(byte[] data) {
		AirdogAlarm alarmEty = new AirdogAlarm();
		if (data == null) {
			return null;
		}
		
		int i = 0;
		int count = Helper.hexadeToDecimal(data[i++]);
		if(count == 0){
			return null;
		}
		
		List<Alarm> alarms = new ArrayList<Alarm>();
		int seq = 0;
		while (count > 0) {
			 if(seq == Helper.hexadeToDecimal(data[i++])) {
				int len = 12;
				byte[] alarmBytes = new byte[len];
				System.arraycopy(data, i, alarmBytes, 0, len);
				Alarm alarm = parseAlarmBytes(alarmBytes);
				if (alarm != null) {
					alarm.setSeq(seq);
				}
				alarms.add(alarm);
				i = i+12;
			 }
			 seq ++;
			count--;
		}
		alarmEty.setAlarms(alarms);
		return alarmEty;
	}
	
	private static Alarm parseAlarmBytes(byte[] data) {
		Alarm alarm = new Alarm();
		if (data == null) {
			return null;
		}
		if(data.length != 12){
			return null;
		}
		
		int i =0;
		//id
		byte id = data[i++];
		alarm.setId(Helper.hexadeToDecimal(id));
		
		//type
		byte type = data[i++];
		alarm.setType(Helper.hexadeToDecimal(type));
		
		//year
		byte yearH = data[i++];
		byte yearL = data[i++];
		alarm.setYear((Helper.hexadeToDecimal(yearH) * 256 + Helper.hexadeToDecimal(yearL)));
		
		//month
		byte month = data[i++];
		alarm.setMonth(Helper.hexadeToDecimal(month));
		
		//day
		byte day = data[i++];
		alarm.setDay(Helper.hexadeToDecimal(day));
		
		//week
		byte week = data[i++];
		alarm.setWeek(Helper.hexadeToDecimal(week));
		
		//hour
		byte hour = data[i++];
		alarm.setHour(Helper.hexadeToDecimal(hour));
		
		//minute
		byte minute = data[i++];
		alarm.setMinute(Helper.hexadeToDecimal(minute));
		
		//music
		byte music = data[i++];
		
		//temp
		byte tempH = data[i++];
		byte temL = data[i++];
		
		return alarm;
	}
	
	

	/***
	 * 获取查询byte数据 通过小狗控制时
	 * 
	 * --> byte[]
	 * 
	 * @param opEntity
	 * @return
	 */
	public static byte[] parseQuery(OPEntity opEntity) {
		int i = 0;
		byte[] b = new byte[128];
		// 针头
		b[i++] = (byte) 0xfe;

		// 长度
		b[i++] = 0x00;
		b[i++] = 0x00;

		// 设备类型
		byte[] type = Helper.decimalToHex2Bytes(opEntity.getDevType());
		b[i++] = type[0];
		b[i++] = type[1];

		// 设备子类型
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(opEntity.getSubDevType());

		// 命令
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(opEntity.getCommand().getValue());
		b[i++] = Helper.ConvertIntTo1byteHexaFormat(opEntity.getFunction());

		// 尾针
		b[i++] = (byte) 0xaa;

		// 计算长度
		byte[] len = Helper.decimalToHex2Bytes(i);
		b[1] = len[0];
		b[2] = len[1];

		byte[] dataB = new byte[i];
		System.arraycopy(b, 0, dataB, 0, i);
		b = null;
		return dataB;
	}
	
	public static byte[]  localRegister(Context context) {
		byte[] head = new byte[] { (byte) 0xFD, 0x00, 0x0E, 0x01, 0x0A, 0x02 };
		String mac = FunctionUtil.getMacAddress(context);
		byte[] macBytes = Helper.ConvertStringToHexBytesArray(mac);
		byte[] temp = Helper.byteMerger(head, macBytes);
		byte checkSum = CheckSumUtil.check(temp);
		byte end = (byte) 0xdf;
		byte[] loacalRe = Helper.byteMerger(temp, new byte[] { checkSum, end });

		return loacalRe;
	}
}
