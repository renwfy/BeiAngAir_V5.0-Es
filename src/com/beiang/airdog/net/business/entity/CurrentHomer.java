package com.beiang.airdog.net.business.entity;

import java.util.List;

import com.beiang.airdog.net.business.ihomer.GetHomerPair.RspGetHomer;

public class CurrentHomer {
	private static CurrentHomer curClass;

	private CurrentHomer() {
	}

	synchronized public static CurrentHomer instance() {
		if (curClass == null) {
			synchronized (CurrentHomer.class) {
				if (curClass == null) {
					curClass = new CurrentHomer();
				}
			}
		}
		return curClass;
	}

	public List<RspGetHomer.Data> homeList;

	public RspGetHomer.Data curHomer;

}
