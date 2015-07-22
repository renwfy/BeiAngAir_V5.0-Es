package com.beiang.airdog.ui.model;

public class AirdogMusic extends BusinessEntity {
	byte[] data;
	int total;
	int curLen;
	int flag;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurLen() {
		return curLen;
	}

	public void setCurLen(int curLen) {
		this.curLen = curLen;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	/***
	 * 
	 * @author LSD
	 * 
	 */
	public static class MFlag {
		public static int start = 1;
		public static int stop = 2;
	}

}
