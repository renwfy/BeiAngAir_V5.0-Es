package com.beiang.airdog.utils.elink;

import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class FirstTimeConfig2 {
	private boolean stopSending;
	private String ip;
	private String ssid;
	private String key;
	private byte[] mDnsAckBytes;
	private int nSetup;
	private int nSync;
	private String syncLString;
	private String syncHString;
	private byte[] encryptionKey;
	private FtcEncode ftcData;
	InetSocketAddress sockAddr;
	private MulticastSocket listenSocket;
	int localPort;
	int listenPort;
	int waitForAckSocketTimeout;
	private FirstTimeConfigListener m_listener = null;
	Thread sendingThread;
	Thread ackWaitThread;
	private boolean isListenSocketGracefullyClosed;
	Thread mDnsThread;

	public FirstTimeConfig2(FirstTimeConfigListener listener, String Key,
			byte[] EncryptionKey, String Ip, String Ssid) throws Exception {
		this(listener, Key, EncryptionKey, Ip, Ssid, "Beiang".getBytes());
	}

	public FirstTimeConfig2(FirstTimeConfigListener listener, String Key,
			byte[] EncryptionKey, String Ip, String Ssid, byte[] ackbytes)
			throws Exception {
		this(listener, Key, EncryptionKey, Ip, Ssid, ackbytes, 5353);
	}

	public FirstTimeConfig2(FirstTimeConfigListener listener, String Key,
			byte[] EncryptionKey, String Ip, String Ssid, byte[] ackbytes,
			int notifyListenPort) throws Exception {
		this(listener, Key, EncryptionKey, Ip, Ssid, ackbytes,
				notifyListenPort, 15000);
	}

	public FirstTimeConfig2(FirstTimeConfigListener listener, String Key,
			byte[] EncryptionKey, String Ip, String Ssid, byte[] ackbytes,
			int notifyListenPort, int localPort) throws Exception {
		this(listener, Key, EncryptionKey, Ip, Ssid, ackbytes,
				notifyListenPort, localPort, 300000);
	}

	public FirstTimeConfig2(FirstTimeConfigListener listener, String Key,
			byte[] EncryptionKey, String Ip, String Ssid, byte[] ackbytes,
			int notifyListenPort, int localPort, int WaitForAckSocketTimeout)
			throws Exception {
		this(listener, Key, EncryptionKey, Ip, Ssid, ackbytes,
				notifyListenPort, localPort, WaitForAckSocketTimeout, 4, 10,
				"abc", "abcdefghijklmnopqrstuvw");
	}

	public FirstTimeConfig2(FirstTimeConfigListener listener, String Key,
			byte[] EncryptionKey, String Ip, String Ssid, byte[] ackbytes,
			int notifyListenPort, int LocalPort, int WaitForAckSocketTimeout,
			int numberOfSetups, int numberOfSyncs, String syncL, String syncH)
			throws Exception {
		int AES_LENGTH = 16;

		this.m_listener = listener;

		if ((EncryptionKey != null) && (EncryptionKey.length != 0)
				&& (EncryptionKey.length != 16)) {
			throw new Exception("Encryption key must have 16 characters!");
		}

		if (Key.length() > 32) {
			throw new Exception(
					"Password is too long! Maximum length is 32 characters.");
		}

		if (Ssid.length() > 32) {
			throw new Exception(
					"Network name (SSID) is too long! Maximum length is 32 characters.");
		}
		this.stopSending = true;

		this.isListenSocketGracefullyClosed = false;

		this.listenSocket = null;

		this.ip = Ip;
		this.ssid = Ssid;
		this.key = Key;
		this.nSetup = numberOfSetups;
		this.nSync = numberOfSyncs;
		this.syncLString = syncL;
		this.syncHString = syncH;

		this.encryptionKey = EncryptionKey;

		this.mDnsAckBytes = ackbytes;

		createBroadcastUDPSocket(notifyListenPort);
		this.localPort = new Random().nextInt(65535);

		this.listenPort = 5353;
		this.waitForAckSocketTimeout = WaitForAckSocketTimeout;

		this.sockAddr = new InetSocketAddress(this.ip, this.localPort);

		byte[] keyData = new byte[this.key.length()];
		System.arraycopy(this.key.getBytes(), 0, keyData, 0, this.key.length());

		keyData = encryptData(keyData);

		this.ftcData = new FtcEncode(this.ssid, keyData);
	}

	private void createBroadcastUDPSocket(int port) throws Exception {
		InetAddress wildcardAddr = null;
		InetSocketAddress localAddr = null;
		localAddr = new InetSocketAddress(wildcardAddr, port);
		this.listenSocket = new MulticastSocket(null);
		this.listenSocket.setReuseAddress(true);
		this.listenSocket.bind(localAddr);
		this.listenSocket.setTimeToLive(255);
		this.listenSocket.joinGroup(InetAddress.getByName("224.0.0.251"));
		this.listenSocket.setBroadcast(true);
	}

	private void sendSync() throws SocketException, Exception {
//		 this.ssid = "MXCHIP_FRON";
//this.key = "stm32f215";
		byte[] syncLBuffer = this.syncLString.getBytes();
		byte[] syncHBuffer = this.syncHString.getBytes();
		byte[] s_key = new byte[this.key.length()];
		byte[] s_ssid = new byte[this.ssid.getBytes("UTF-8").length];
		byte[] data = new byte[2];
		byte[] userinfo;
		byte[] inputBytes = this.mDnsAckBytes;
		int userlength = inputBytes.length;
		if (userlength == 0) {
			userlength++;
			userinfo = new byte[1];
			userinfo[0] = 0;
		} else {
			userinfo = new byte[userlength];
			System.arraycopy(inputBytes, 0, userinfo, 0,
					userlength);
		}

		System.arraycopy(this.key.getBytes(), 0, s_key, 0, this.key.length());
		System.arraycopy(this.ssid.getBytes("UTF-8"), 0, s_ssid, 0,
				this.ssid.getBytes("UTF-8").length);
		data[0] = (byte) s_ssid.length;
		data[1] = (byte) s_key.length;
		byte[] temp = Helper.byteMerger(s_ssid, s_key);
		data = Helper.byteMerger(data, temp);

		String head = "239.118.0.0";
		this.ip = head;
		for (int i = 0; i < 5; i++) {
			this.sockAddr = new InetSocketAddress(
					InetAddress.getByName(this.ip), this.getRandomNumber());
			sendData(new DatagramPacket(syncHBuffer, 20, this.sockAddr));
			// Thread.sleep(10L);
		}
//		if (userlength == 0) {
//			for (int k = 0; k < data.length; k += 2) {
//				if (k + 1 < data.length)
//					this.ip = "239.126." + (int) (data[k] & 0xff) + "."
//							+ (int) (data[k + 1] & 0xff);
//				else
//					this.ip = "239.126." + (int) (data[k] & 0xff) + ".0";
//				this.sockAddr = new InetSocketAddress(
//						InetAddress.getByName(this.ip), 10000);
//				byte[] bbbb = new byte[k / 2 + 20];
//				sendData(new DatagramPacket(bbbb, k / 2 + 20, this.sockAddr));
//				// Thread.sleep(5L);
//			}
//		} else {
			if (data.length % 2 == 0){
				if(userinfo.length == 0){
					byte[] temp_length = { (byte) userlength, 0, 0};
					data = Helper.byteMerger(data, temp_length);
				}
				else{
				byte[] temp_length = { (byte) userlength, 0 };
				data = Helper.byteMerger(data, temp_length);
				}							
			}
			else {
				byte[] temp_length = {0, (byte) userlength, 0};
				data = Helper.byteMerger(data, temp_length);
			}
			data = Helper.byteMerger(data, userinfo);
			for (int k = 0; k < data.length; k += 2) {
				if (k + 1 < data.length)
					this.ip = "239.126." + (int) (data[k] & 0xff) + "."
							+ (int) (data[k + 1] & 0xff);
				else
					this.ip = "239.126." + (int) (data[k] & 0xff) + ".0";
				this.sockAddr = new InetSocketAddress(
						InetAddress.getByName(this.ip), this.getRandomNumber());
				byte[] bbbb = new byte[k / 2 + 20];
				sendData(new DatagramPacket(bbbb, k / 2 + 20, this.sockAddr));
				// Thread.sleep(5L);
			}
//		}
	}

	private void send() throws Exception {
		// int numberOfPackets = this.ftcData.getmData().size();
		//
		// ArrayList packets = new ArrayList();
		// byte[] ftcBuffer = new byte[1600];

		// ftcBuffer = makePaddedByteArray(ftcBuffer.length);

		// packets = this.ftcData.getmData();

		while (!this.stopSending) {
			// for (int i = 0; i < this.nSetup; i++) {

			sendSync();
			// for (int j = 0; j < numberOfPackets; j++) {
			// sendData(
			// new DatagramPacket(ftcBuffer,
			// ((Integer) packets.get(j)).intValue(),
			// this.sockAddr), getRandomNumber());
			// }
			// }
			Thread.sleep(50L);
		}
	}

	private void sendData(DatagramPacket packet) throws Exception {
		MulticastSocket sock = null;
		sock = new MulticastSocket(10000);
		sock.joinGroup(InetAddress.getByName(this.ip));
		// sock.setReuseAddress(true);
		sock.send(packet);

		sock.close();
	}

	public void transmitSettings() throws Exception {
		this.stopSending = false;
		this.sendingThread = new Thread(new Runnable() {
			public void run() {
				try {
					FirstTimeConfig2.this.send();
				} catch (Exception e) {
					new FirstTimeConfig2.NotifyThread(
							FirstTimeConfig2.this.m_listener, e);
				}
			}
		});
		this.sendingThread.start();

		// this.ackWaitThread = new Thread(new Runnable() {
		// public void run() {
		// try {
		// FirstTimeConfig.this.waitForAck();
		// } catch (Exception e) {
		// new FirstTimeConfig.NotifyThread(
		// FirstTimeConfig.this.m_listener, e);
		// }
		// }
		// });
		// this.ackWaitThread.start();
	}

	public void stopTransmitting() throws Exception {
		this.isListenSocketGracefullyClosed = true;
		this.listenSocket.close();

		this.stopSending = true;
		if (Thread.currentThread() != this.sendingThread)
			this.sendingThread.join();
		if (Thread.currentThread() != this.ackWaitThread)
			this.ackWaitThread.join();
	}

	private void waitForAck() throws Exception {
		int RECV_BUFFER_LENGTH = 16384;

		byte[] recvBuffer = new byte[16384];

		DatagramPacket listenPacket = new DatagramPacket(recvBuffer, 16384);
		int timeout = this.waitForAckSocketTimeout;

		while (!this.stopSending) {
			long start = System.nanoTime();
			this.listenSocket.setSoTimeout(timeout);
			try {
				this.listenSocket.receive(listenPacket);
			} catch (InterruptedIOException e) {
				if (this.isListenSocketGracefullyClosed)
					break;
				new NotifyThread(this.m_listener,
						FirstTimeConfigListener.FtcEvent.FTC_TIMEOUT);
				break;
			} catch (Exception e) {
				if (!this.isListenSocketGracefullyClosed)
					break;
			}
			break;
		}
	}

	private boolean parseMDns(byte[] data) throws Exception {
		int MDNS_HEADER_SIZE = 12;
		int MDNS_HEADER_SIZE2 = 10;

		int pos = 12;

		if (data.length < pos + 1)
			return false;
		int len = data[pos] & 0xFF;
		pos++;

		while (len > 0) {
			if (data.length < pos + len) {
				return false;
			}
			pos += len;

			if (data.length < pos + 1)
				return false;
			len = data[pos] & 0xFF;
			pos++;
		}

		pos += 10;

		if (data.length < pos + 1)
			return false;
		len = data[pos] & 0xFF;
		pos++;

		if (data.length < pos + len)
			return false;
		String name = new String(data, pos, len);

		boolean bRet = name.equals(this.mDnsAckBytes);
		return bRet;
	}

	private byte[] encryptData(byte[] data) throws Exception {
		if (this.encryptionKey == null)
			return data;
		if (this.encryptionKey.length == 0) {
			return data;
		}
		int ZERO_OFFSET = 0;
		int AES_LENGTH = 16;
		int DATA_LENGTH = 32;

		Cipher c = null;
		byte[] encryptedData = null;
		byte[] paddedData = new byte[32];
		byte[] aesKey = new byte[16];

		for (int x = 0; x < 16; x++) {
			if (x < this.encryptionKey.length) {
				aesKey[x] = this.encryptionKey[x];
			} else {
				aesKey[x] = 0;
			}

		}

		int dataOffset = 0;
		if (data.length < 32) {
			paddedData[dataOffset] = ((byte) data.length);
			dataOffset++;
		}

		System.arraycopy(data, 0, paddedData, dataOffset, data.length);
		dataOffset += data.length;

		while (dataOffset < 32) {
			paddedData[dataOffset] = 0;
			dataOffset++;
		}

		c = Cipher.getInstance("AES/ECB/NoPadding");

		SecretKeySpec k = null;
		k = new SecretKeySpec(aesKey, "AES");

		c.init(1, k);

		encryptedData = c.doFinal(paddedData);

		return encryptedData;
	}

	private byte[] makePaddedByteArray(int length) throws Exception {
		byte[] paddedArray = new byte[length];

		for (int x = 0; x < length; x++) {
			paddedArray[x] = ((byte) "1".charAt(0));
		}

		return paddedArray;
	}

	private class NotifyThread implements Runnable {
		private FirstTimeConfigListener m_listener;
		private FirstTimeConfigListener.FtcEvent t_event;
		private Exception t_ex;

		public NotifyThread(FirstTimeConfigListener listener,
				FirstTimeConfigListener.FtcEvent event) {
			this.m_listener = listener;
			this.t_event = event;
			this.t_ex = null;
			// Thread t = new Thread(this);
			// t.start();
		}

		public NotifyThread(FirstTimeConfigListener listener, Exception ex) {
			this.m_listener = listener;
			this.t_event = FirstTimeConfigListener.FtcEvent.FTC_ERROR;
			this.t_ex = ex;
			// Thread t = new Thread(this);
			// t.start();
		}

		public void run() {
			try {
				// if (this.m_listener != null)
				// this.m_listener.onFirstTimeConfigEvent(this.t_event,
				// this.t_ex);
			} catch (Exception localException) {
			}
		}
	}

	private int getRandomNumber() {
		int num = new Random().nextInt(65536);
		if (num > 10000)
			return num;
		else
			return 15000;
	}

}