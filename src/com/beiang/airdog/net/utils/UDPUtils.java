package com.beiang.airdog.net.utils;

import android.os.Handler;
import android.os.Message;

public class UDPUtils {
	// FD + LEN + 父设备 + 子设备 + 命令 + CS + DF
	static byte[] GET_IP = new byte[] { (byte)0xFD, 0x00,0x08, 0x01, 0x0a, 0x01, 0x11, (byte) 0xdf };
	static UDPReceiveThead rThread;
	static UDPSendThread sThread;

	public static void getUdpHost(final int targetPort, final int localPort, final Handler handler) {
		new Thread() {
			@Override
			public void run() {
				String hostIp = null;
				hostIp = UDP.getIp(targetPort, localPort, GET_IP);

				Message msg = handler.obtainMessage();
				msg.obj = hostIp;
				handler.sendMessage(msg);
			}
		}.start();
	}

	public static void startReceive(int port, Handler mHandler) {
		if (rThread != null) {
			rThread.abort();
		}
		rThread = new UDPReceiveThead(port, mHandler);
		rThread.start();
	}

	public static void stopReceive() {
		if (rThread != null) {
			rThread.abort();
			rThread = null;
		}
	}

	public static void startSend(String ip, int port, byte[] buffer) {
		if (sThread == null) {
			sThread = new UDPSendThread(ip, port);
			sThread.start();
		}
		sThread.send(buffer);
	}

	public static void stopSend() {
		if (sThread != null) {
			sThread.abort();
			sThread = null;
		}
	}
}
