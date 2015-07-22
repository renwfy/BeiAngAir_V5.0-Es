package com.beiang.airdog.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class ReadRaw {
	public static byte[] readRawData(Context c, int id) {
		InputStream is = null;
		byte[] data = null;
		try {
			is = c.getResources().openRawResource(id);
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = is.read(buff)) != -1) {
				byte[] read = new byte[len];
				System.arraycopy(buff, 0, read, 0, len);
				if (data != null) {
					data = byteMerger(data, read);
				} else {
					data = read;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			is = null;
		}
		return data;
	}

	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}
}
