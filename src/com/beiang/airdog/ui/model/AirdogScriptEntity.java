package com.beiang.airdog.ui.model;

import java.util.List;

public class AirdogScriptEntity extends BusinessEntity {
	//List<AirdogScript> 

	public class AirdogScript {
		String type;
		int enable;
		String time;
		String value;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getEnable() {
			return enable;
		}

		public void setEnable(int enable) {
			this.enable = enable;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
}
