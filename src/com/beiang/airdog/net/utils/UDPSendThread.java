package com.beiang.airdog.net.utils;

public class UDPSendThread extends Thread {
	private boolean statu;
	private boolean flag ;
	
	byte[] buffer;
	String ip;
	int port;

	public UDPSendThread(String ip ,int port) {
		// TODO Auto-generated constructor stub
		statu = true;
		flag = false;
		this.ip = ip;
		this.port = port;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (statu) {
			while(flag){
				UDP.send(ip, port, buffer);
				flag = false;
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void send(byte[] buffer){
		flag = true;
		this.buffer = buffer;
	}

	public void abort() {
		flag = false;
		statu = false;
	}
}
