package com.beiang.airdog.ui.model;

/***
 * 
 * 空调逻辑模型
 * 
 * @author LSD
 * 
 */
public class AirConditionEntity extends BusinessEntity {
	/** 00 关机   01开机 */
	int power;
	public class Power{
		public static final int off = 0;
		public static final int on = 1;
	}
	
	/** 00 自动    01 制冷    02 抽湿    03 制热 */
	int model;
	public class Model{
		public static final int auto = 0;
		public static final int cold = 1;
		public static final int wet = 2;
		public static final int hot = 3;
	}
	
	/** 00 16度    0E 30度*/
	int temp;
	
	/**风速    00 自动      01 1档      02 2档      03 3档     04 最大档风速*/
	int speed;
	public class Speed{
		public static final int auto = 0;
		public static final int level1 =1;
		public static final int level2 =2;
		public static final int level3 =3;
		public static final int level4 =4;
	}

	/**00 上下摆风    01 不摆风  */
	int swing;
	public class Swing{
		public static final int on = 1;
		public static final int off = 0;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getModel() {
		return model;
	}

	public void setModel(int model) {
		this.model = model;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSwing() {
		return swing;
	}

	public void setSwing(int swing) {
		this.swing = swing;
	}
	
	
}
