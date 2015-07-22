package com.beiang.airdog.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.beiang.airdog.ui.model.FirmwareEntity;

public class XmlUtils {

	public static List<FirmwareEntity> parse(String response) {
		List<FirmwareEntity> list = null;
		FirmwareEntity entity = null;
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new ByteArrayInputStream(response.getBytes()), "utf-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = parser.getName();
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					list = new ArrayList<FirmwareEntity>();
					break;
				case XmlPullParser.START_TAG:
					if ("verson".equals(name)) {
						entity = new FirmwareEntity();
					}
					if ("prodect_id".equals(name)) {
						entity.prodect_id = parser.nextText();
					}
					if ("type".equals(name)) {
						entity.type = Integer.parseInt(parser.nextText());
					}
					if ("url".equals(name)) {
						entity.url = parser.nextText();
					}
					if ("ver".equals(name)) {
						entity.ver = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					if ("verson".equals(name)) {
						list.add(entity);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
