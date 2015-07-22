package com.beiang.airdog.net.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class TCPReceiveThread extends Thread {
	boolean state;
	int rlRead;
	private Socket socket;
	private Handler mHandler;
	private byte[] sData;
	private InputStream input;

	public TCPReceiveThread(Socket socket, Handler mHandler) {
		// TODO Auto-generated constructor stub
		sData = new byte[1024];
		state = true;
		this.socket = socket;
		this.mHandler = mHandler;
		try {
			input = this.socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (state) {
			try {
				rlRead = input.read(sData);// 对方断开返回-1
				if (rlRead > 0) {
					byte[] temp = new byte[rlRead];
					System.arraycopy(sData, 0, temp, 0, rlRead);

					sleep(5);
					Message msg = mHandler.obtainMessage();
					if (temp != null) {
						msg.obj = temp;
						msg.what = 1;
						mHandler.sendMessage(msg);
					}
				} else {
					// 连接主动断开
					Message msg = mHandler.obtainMessage();
					msg.obj = new byte[] { 0x00, (byte) 0xff };
					msg.what = 1;
					mHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public void abort() {
		state = false;
		sData = null;
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			input = null;
		}
	}
}
