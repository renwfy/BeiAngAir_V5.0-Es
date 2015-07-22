package com.beiang.airdog.constant;

public class Constants {
	// 本地数据库名
	public static final String DB_NAME = "beiangAir.db";
	public static final String CITY_DB_NAME = "cities.db";
	// 本地文件
	public static final String FILE_NAME = "BeiAngAir";
	// 更新的APK名称
	public static final String UPDATE_APK_NAME = "updata.apk";
	public static final String ICON_HEAD = "device_";
	public static final String ICON_TYPE = ".png";
	public static final String TEMP_IMAGE = "temp_image.png";
	// 首页广告配置文件
	public static final String CONFIG_AD = "ad.png";
	public static final String CONFIG_FILE = "config.json";
	/** 文件夹名 **/
	public static final String FILE_DEVICE_ICON = "DeviceIcon";
	public static final String FILE_QR = "qr";
	/** 文件夹名 **/
	public static final String FILE_ALL_DEVICE_ICON = "AllDeviceIcon";
	/**录音*/
	public static final String RECORD = "record";

	/** 网络模式 */
	public enum Network {
		LOCAL(), SERVER();
		private Network() {
		}
	}

	/** 设备类型/名称 */
	public static class Device {
		public static final int DT_280B = 1;
		public static final int DT_280E = 2;
		public static final int DT_CAR = 3;
		public static final int DT_AURA100 = 4;
		public static final int DT_JY300 = 5;
		public static final int DT_JY500 = 6;
		public static final int DT_JY300S = 8;
		public static final int DT_Airdog = 160;
		public static final int DT_TAir = 80;
		public static final int DT_PowerSocket = 33;
		public static final int DT_Light = 34;
		public static final int DT_FC1 = 48;
		public static final int DT_FA20 = 49;
		
		
		public static final String DN_280B = "280B";
		public static final String DN_280E = "280E";
		public static final String DN_CAR = "CAR";
		public static final String DN_AURA100 = "AURA100";
		public static final String DN_JY300 = "JY300";
		public static final String DN_JY500 = "JY500";
		public static final String DN_JY300S = "JY300S";
		public static final String DN_Airdog = "Airdog";
		public static final String DN_TAir = "TAir";
		public static final String DN_PowerSocket = "powersocket";
		public static final String DN_Light = "Light";
		public static final String DN_FC1 = "FC1";
		public static final String DN_FA20 = "FA20";
		public static final String DN_DEFULT = "Device";
	}

	/***
	 * 
	 * 子设备
	 */
	public static class SubDevice {
		public static final int SDT_Airdog = 0;
		public static final int SDT_AirCondition = 1;
		public static final int SDT_AirPurifier = 2;
		public static final int SDT_PowerSocket = 3;
	}

	/***
	 * 
	 * 操作类型
	 * 
	 * @author LSD
	 * 
	 */
	public enum Command {
		READ(1), WTITE(2), DELETE(3), SET(4), TEST(170);

		int command = 0;

		private Command(int command) {
			this.command = command;
		}

		public int getValue() {
			return this.command;
		}
	}

	/**
	 * 小狗功能码
	 * 
	 * @author LSD
	 */
	public enum AirdogFC {
		VOLUME(0), ALARM(1), MUSIC(2),CYCLE(3),SCRIPT(4),YELP(5),TEST_CC(204);

		int fc = 0;

		private AirdogFC(int fc) {
			this.fc = fc;
		}

		public int getValue() {
			return this.fc;
		}
	}
	
	/***
	 * 功能码
	 * 
	 * @author LSD
	 *
	 */
	public enum Function {
		FC0(0),FIRMWARE(136);

		int fc = 0;

		private Function(int fc) {
			this.fc = fc;
		}

		public int getValue() {
			return this.fc;
		}
	}

	/***
	 * 闹钟类型
	 * 
	 * @author LSD
	 * 
	 */
	public enum AlarmType {
		GETUP(1), MEETING(2), REST(3), SLEEP(4), ORTHER(5),DEFINE(6),EVENT(7);
		int type = 1;

		private AlarmType(int type) {
			this.type = type;
		}

		public int getValue() {
			return this.type;
		}
	}

	
	/**获取设备名称*/
	public static String getDeviceName(int deviceType) {
		String name = "";
		switch (deviceType) {
		case Device.DT_280B:
			name = Device.DN_280B;
			break;
		case Device.DT_280E:
			name = Device.DN_280E;
			break;
		case Device.DT_CAR:
			name = Device.DN_CAR;
			break;
		case Device.DT_AURA100:
			name = Device.DN_AURA100;
			break;
		case Device.DT_Airdog:
			name = Device.DN_Airdog;
			break;
		case Device.DT_JY300:
			name = Device.DN_JY300;
			break;
		case Device.DT_JY500:
			name = Device.DN_JY500;
			break;
		case Device.DT_JY300S:
			name = Device.DN_JY300S;
			break;
		case Device.DT_TAir:
			name = Device.DN_TAir;
			break;
		case Device.DT_PowerSocket:
			name = Device.DN_PowerSocket;
			break;
		default:
			name = Device.DN_DEFULT;
			break;
		}
		return name;
	}
}
