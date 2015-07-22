package com.beiang.airdog.ui.heartbeat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.beiang.airdog.receiver.BABroadcastReceiver;

public class HeartbeatAlarm {
	public static String action = "com.beiang.airdog.heartbeat";
	private volatile static HeartbeatAlarm hAlarm;

	private AlarmManager am;
	private PendingIntent pi;
	private boolean running;

	/***
	 * 内部实现
	 */
	private HeartbeatAlarm() {
		// TODO Auto-generated method stub
	}

	/***
	 * 单例
	 * 
	 * @return
	 */
	public static HeartbeatAlarm getInstance() {
		if (hAlarm == null) {
			synchronized (HeartbeatAlarm.class) {
				if (hAlarm == null) {
					hAlarm = new HeartbeatAlarm();
				}
			}
		}
		return hAlarm;
	}

	/***
	 * 初始化
	 * 
	 * @param context
	 */
	public void initAlarm(Context context) {
		Intent intent = new Intent(action);
		intent.setClass(context, BABroadcastReceiver.class);

		pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	/***
	 * 开始
	 * 
	 * @param context
	 */
	public void start(Context context) {
		if (am == null) {
			initAlarm(context);
		}
		running = true;
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, 60000, pi);
	}

	/***
	 * 停止
	 * 
	 * @param context
	 */
	public void stop(Context context) {
		if (am == null) {
			initAlarm(context);
		}
		running = false;
		am.cancel(pi);
	}
	
	public boolean isRunning(){
		return running;
	}

}
