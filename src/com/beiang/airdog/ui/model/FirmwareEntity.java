package com.beiang.airdog.ui.model;

public class FirmwareEntity extends BusinessEntity {
	public int type;// 1:固件，2：音乐
	public String url;
	public String prodect_id;
	public String ver;

	public class Type {
		public static final int firmware = 1;
		public static final int music = 2;
	}
}
