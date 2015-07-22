package com.beiang.airdog.net.business.entity;

import com.beiang.airdog.net.business.homer.QueryWeatherPair;

public class CurrentWeather {
	private static CurrentWeather cWeather;

	private CurrentWeather() {
	}

	synchronized public static CurrentWeather instance() {
		if (cWeather == null) {
			synchronized (CurrentWeather.class) {
				if (cWeather == null) {
					cWeather = new CurrentWeather();
				}
			}
		}
		return cWeather;
	}

	public QueryWeatherPair.RspQueryWeather weather;

}
