package com.beiang.airdog.net.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.os.Handler;

public class TCP {
	private Socket socket;// socket
	private OutputStream output;// 发送数据
	DataOutputStream dStream;
	private boolean connected;
	private TCPReceiveThread sReceive;
	private Handler mHandler;
	
	public TCP(Handler mHandler) {
		// TODO Auto-generated constructor stub
		this.mHandler = mHandler;
	}

	public boolean connect(String ip, int port) {
		if (createSocket(ip, port)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 创建TCP连接
	 * 
	 * @param ipAddres
	 * @param port
	 * @param type
	 * @return
	 */
	private boolean createSocket(String ipAddres, int port) {
		// TODO Auto-generated method stub
		socket = new Socket();
		SocketAddress remoteAddr = new InetSocketAddress(ipAddres, port);
		try {
			socket.connect(remoteAddr, 3000);
			
			if(sReceive == null){
				sReceive = new TCPReceiveThread(socket, mHandler);
				sReceive.start();
			}
			connected = true;
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/***
	 * 发送消息
	 * 
	 * @param msg
	 */
	public void send(byte[] datas) {
		if (datas == null) {
			return;
		}
		try {
			if(output == null){
				output = socket.getOutputStream();
			}
			output.write(datas);
			output.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 中断链接
	 * 
	 * @return
	 */
	public boolean abort() {
		if (connected) {
			connected = false;
			try {
				if (socket != null) {
					socket.shutdownOutput();
					socket.shutdownInput();
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				output = null;
			}
			
			if(sReceive != null){
				sReceive.abort();
				sReceive = null;
			}
		}
		return true;
	}
}
