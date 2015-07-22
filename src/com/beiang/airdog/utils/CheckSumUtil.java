package com.beiang.airdog.utils;

public class CheckSumUtil {
	/**
	 * 校验和
	 * 
	 * @param msg
	 *            需要计算校验和的byte数组
	 * @param length
	 *            校验和位数
	 * @return 计算出的校验和数组
	 */
	public static byte[] check(byte[] msg, int length) {
		long mSum = 0;
		byte[] mByte = new byte[length];

		/** 逐Byte添加位数和 */
		for (byte byteMsg : msg) {
			long mNum = ((long) byteMsg >= 0) ? (long) byteMsg : ((long) byteMsg + 256);
			mSum += mNum;
		}

		/** 位数和转化为Byte数组 */
		for (int liv_Count = 0; liv_Count < length; liv_Count++) {
			mByte[length - liv_Count - 1] = (byte) (mSum >> (liv_Count * 8) & 0xff);
		}

		return mByte;
	}
	
	
	public static byte check(byte[] msg) {
		byte temp = 0;
		for (int i = 0; i < msg.length; i++) {
			temp = (byte) (temp + msg[i]);
		}
		return temp;
	}
	
}
