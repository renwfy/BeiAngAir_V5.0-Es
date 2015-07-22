package com.beiang.airdog.net.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import com.beiang.airdog.constant.Config;
import com.beiang.airdog.utils.LogUtil;

import android.text.TextUtils;

public class UDP {
	static DatagramSocket InSocket = null;

	/***
	 * 
	 * UDP发送
	 * 
	 * @param ip
	 * @param port
	 * @param buffer
	 */
	public static void send(String ip, int port, byte[] buffer) {
		if (TextUtils.isEmpty(ip)) {
			ip = "255.255.255.255";
		}
		try {
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ip), port);
			if (Config.DEBUG) {
				LogUtil.i("send buffer");
			}
			socket.send(dataPacket);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * UDP接收数据
	 * 
	 * @param port
	 * @return
	 */
	public static byte[] receive(int port) {
		try {
			if (InSocket == null) {
				InSocket = new DatagramSocket(null);
				InSocket.setReuseAddress(true);
				InSocket.bind(new InetSocketAddress(port));
			}

			DatagramPacket dpIn = null;
			byte[] bufferIn = new byte[32];
			dpIn = new DatagramPacket(bufferIn, bufferIn.length);

			InSocket.receive(dpIn);
			byte[] in = dpIn.getData();
			return in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 停止接收 */
	public static void stopReceive() {
		if (InSocket != null) {
			InSocket.disconnect();
			InSocket.close();
			InSocket = null;
		}
	}

	/***
	 * 取ip
	 * 
	 * @param port
	 * @return
	 */
	public static String getIp(int targetPort, int localPort, byte[] buffer) {
		try {
			DatagramSocket socket = new DatagramSocket(null);
			socket.setReuseAddress(true);
			socket.bind(new InetSocketAddress(localPort));
			socket.setSoTimeout(3100);
			DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("255.255.255.255"), targetPort);
			if (Config.DEBUG) {
				LogUtil.i("getIp");
				LogUtil.i(buffer);
			}
			int index =0;
			while(index<5){
				socket.send(dataPacket);
				index++;
			}

			DatagramPacket dpIn = null;
			byte[] bufferIn = new byte[buffer.length];
			dpIn = new DatagramPacket(bufferIn, bufferIn.length);

			socket.receive(dpIn);
			String host = dpIn.getAddress().getHostAddress();
			socket.close();
			return host;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
