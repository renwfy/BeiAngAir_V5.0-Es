package com.beiang.airdog.utils;

import android.os.Handler;
import android.os.Message;

public class TimerLeftUtil {
	private Handler handler;
	int time=0;

	public TimerLeftUtil(Handler handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	public void startTimer(int time) {
		this.time = time;
		handler.postDelayed(task, 1000);
	}

	public void stopTimer() {
		handler.removeCallbacks(task);
		time = 0;
	}

	private Runnable task = new Runnable() {
		public void run() {
			if(time ==0){
				stopTimer();
				handler.sendEmptyMessage(0);
			}else{
				time --;
				Message msg = handler.obtainMessage();
				msg.arg1 = time;
				msg.what = 1;
				handler.sendMessage(msg);
				handler.postDelayed(task, 1000);
			}
		}
	};

}
