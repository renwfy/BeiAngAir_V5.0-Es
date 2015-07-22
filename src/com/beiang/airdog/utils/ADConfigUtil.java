package com.beiang.airdog.utils;

import java.io.File;
import java.util.Calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beiang.airdog.constant.Constants;
import com.beiang.airdog.net.api.API;
import com.beiang.airdog.ui.model.AdEntity;
import com.beiang.airdog.utils.DownLoadUtils.DownloadListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 获取广告配置文件
 * 
 * @author LSD
 * 
 */
public class ADConfigUtil {
	/** 下载广告图片 */
	public void downloadAdPic() {
		String url = API.AppAdConfogUrl + Constants.CONFIG_AD;
		String target = Settings.CONFIG_PATH + "/" + Constants.CONFIG_AD;
		DownLoadUtils.download(url, target, new DownloadListener() {
			@Override
			public void DownLoadFailed(String url, String outPath) {
				// TODO Auto-generated method stub
			}

			@Override
			public void DownLoadComplete(String url, String outPath) {
				// TODO Auto-generated method stub
			}
		});
		// BADloadUtil.download(API.AppAdConfogUrl + Constants.CONFIG_AD,
		// Constants.CONFIG_AD);
	}

	/** 下载广告配置文件 */
	public void downloadAdFile() {
		String url = API.AppAdConfogUrl + Constants.CONFIG_FILE;
		String target = Settings.CONFIG_PATH + "/" + Constants.CONFIG_FILE;
		DownLoadUtils.download(url, target, new DownloadListener() {
			@Override
			public void DownLoadFailed(String url, String outPath) {
				// TODO Auto-generated method stub
			}

			@Override
			public void DownLoadComplete(String url, String outPath) {
				// TODO Auto-generated method stub
			}
		});
		// BADloadUtil.download(API.AppAdConfogUrl + Constants.CONFIG_FILE,
		// Constants.CONFIG_FILE);
	}

	/** 获取 广告图片 */
	public Bitmap getAdPic() {
		if (!refrashFile()) {
			return null;
		}

		String path = Settings.CONFIG_PATH + "/" + Constants.CONFIG_AD;
		if (refrashPic(path)) {
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			return bitmap;
		}
		return null;
	}

	/** 获取 广告配置文件 */
	public String getAdFile() {
		String path = Settings.CONFIG_PATH + "/" + Constants.CONFIG_FILE;
		File file = new File(path);
		if (!file.exists()) {
			return "";
		}
		byte[] bytes = FileUtils.getCodeByFilePath(path);
		String s = new String(bytes);
		return s;
	}

	/** 是否过期 */
	public boolean isValid() {
		String s = getAdFile();
		try {
			AdEntity entity = new Gson().fromJson(s, AdEntity.class);
			String dateS = entity.getExpire();
			String[] strs = dateS.split("-");

			int year = Integer.parseInt(strs[0]);
			int month = Integer.parseInt(strs[1]);
			int day = Integer.parseInt(strs[2]);

			Calendar curCalendar = Calendar.getInstance();
			int curY = curCalendar.get(Calendar.YEAR);
			int curM = curCalendar.get(Calendar.MONTH) + 1;
			int curD = curCalendar.get(Calendar.DAY_OF_MONTH);
			
			int allDay = (year * 12 * 30) + (month * 30) + day;
			int curDay = (curY * 12 * 30) + (curM * 30) + curD;

			if (curDay <= allDay) {
				return true;
			}
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/** 刷新 文件 */
	public boolean refrashFile() {
		downloadAdFile();
		if (!isValid()) {
			return false;
		}
		return true;
	}

	/** 刷新 图片 */
	public boolean refrashPic(String path) {
		File file = new File(path);
		if (!file.exists()) {
			downloadAdPic();
			return false;
		}
		return true;
	}

}
