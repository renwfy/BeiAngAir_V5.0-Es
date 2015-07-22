package com.beiang.airdog.net.utils;

import com.beiang.airdog.utils.LogUtil;

import android.os.Handler;
import android.os.Message;

public class TCPUtils {
	static TCPUtils tcpUtils;
	static TCPSendThead sThread;
	static Handler mHandler;

	public interface ITcp {
		public void success();

		public void failed();
	}

	synchronized public static TCPUtils instance(Handler handler) {
		mHandler = handler;
		if (tcpUtils == null) {
			tcpUtils = new TCPUtils();
		}
		return tcpUtils;
	}

	private TCPUtils() {
	}

	public void connect(String ip, int port, final ITcp itcp) {
		if (sThread == null) {
			sThread = new TCPSendThead(mHandler);
			sThread.connect(ip, port,new ITcp() {
				@Override
				public void success() {
					// TODO Auto-generated method stub
					sThread.start();
					itcp.success();
				}
				
				@Override
				public void failed() {
					// TODO Auto-generated method stub
					sThread = null;
					itcp.failed();
				}
			});
		}
	}

	public void startSend(byte[] buffer) {
		sThread.send(buffer);
	}

	public void stopSend() {
		if (sThread != null) {
			sThread.abort();
			sThread = null;
		}
	}
}
