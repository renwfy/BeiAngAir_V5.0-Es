package com.beiang.airdog.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.beiang.airdog.ui.heartbeat.HeartbeatAlarm;

/***
 * 监听
 * 
 * @author LSD
 * 
 */
public class BABroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
		} else if (HeartbeatAlarm.action.equals(action)) {
		}
	}

}
