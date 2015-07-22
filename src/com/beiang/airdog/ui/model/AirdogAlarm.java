package com.beiang.airdog.ui.model;

import java.io.Serializable;
import java.util.List;

public class AirdogAlarm extends BusinessEntity {
	List<Alarm> alarms;

	public List<Alarm> getAlarms() {
		return alarms;
	}

	public void setAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}

	public static class Alarm implements Serializable{
		public int seq;
		public int id;
		public int type;
		public int year;
		public int month;// 年0(月重复)，年大于0(月不重复)
		public int day;
		public int week;// 0x01(1),0x02(2),0x03(4),0x04(8),0x05(16),0x06(32),0x07(64)
		public int hour;
		public int minute;
		public int temp;

		public int getSeq() {
			return seq;
		}

		public void setSeq(int seq) {
			this.seq = seq;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public int getWeek() {
			return week;
		}

		public void setWeek(int week) {
			this.week = week;
		}

		public int getHour() {
			return hour;
		}

		public void setHour(int hour) {
			this.hour = hour;
		}

		public int getMinute() {
			return minute;
		}

		public void setMinute(int minute) {
			this.minute = minute;
		}

		public int getTemp() {
			return temp;
		}

		public void setTemp(int temp) {
			this.temp = temp;
		}
	}
}
