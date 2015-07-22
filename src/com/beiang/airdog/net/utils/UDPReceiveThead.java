package com.beiang.airdog.net.utils;

import android.os.Handler;
import android.os.Message;

public class UDPReceiveThead extends Thread {
	private boolean statu;
	int port;
	Handler mHandler;

	public UDPReceiveThead(int port, Handler mHandler) {
		// TODO Auto-generated constructor stub
		statu = true;
		this.mHandler = mHandler;
		this.port = port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (statu) {
			byte[] buffer = UDP.receive(port);
			if (buffer != null) {
				Message msg = mHandler.obtainMessage();
				msg.obj = buffer;
				mHandler.sendMessage(msg);
			}
		}
	}

	public void abort() {
		UDP.stopReceive();
		statu = false;
	}
}
