package com.beiang.airdog.net.utils;

import com.beiang.airdog.net.utils.TCPUtils.ITcp;

import android.os.Handler;
import android.os.Message;

public class TCPSendThead extends Thread {
	public static int YES = 100;
	public static int NO = -100;
	
	private TCP tcp;
	private boolean statu;
	private boolean flag;
	private Handler mHandler;

	byte[] buffer;

	public TCPSendThead(Handler mHandler) {
		// TODO Auto-generated constructor stub
		statu = true;
		flag = false;
		this.mHandler = mHandler;
	}

	public void connect(final String ip, final int port,final ITcp itcp) {
		if (tcp == null) {
			final Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					int what = msg.what;
					if(YES == what){
						itcp.success();
					}
					if(NO == what){
						itcp.failed();
					}
				}
			};
			new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					tcp = new TCP(mHandler);
					if (tcp.connect(ip, port)) {
						handler.sendEmptyMessage(YES);
						return;
					}
					tcp = null;
					handler.sendEmptyMessage(NO);
				}
			}.start();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (statu) {
			while (flag) {
				tcp.send(buffer);
				flag = false;
			}
		}
	}

	public void send(byte[] buffer) {
		flag = true;
		this.buffer = buffer;
	}

	public void abort() {
		flag = false;
		statu = false;
		tcp.abort();
		tcp = null;
	}
}
