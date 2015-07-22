package com.beiang.airdog.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

public class ImageUtil {
	private Context context;

	private int mPicWidth;
	private int mPicHeight;

	private File mPicFile;
	private File mPicFileTemp;

	public static final int CAPTURE_PIC = 0;
	public static final int HANDLE_PIC = 1;
	public static final int PICK_PIC = 2;

	public static final String DIR_NAME = "pu";

	public ImageUtil(Context context) {
		setContext(context);
	}

	public ImageUtil(Context context, int picWidth, int picHeight, String picName, String picNameTemp) {
		setContext(context);
		setPicWidth(picWidth);
		setPicHeight(picHeight);
		setPicFile(createImageFile(DIR_NAME, picName));
		setPicFileTemp(createImageFile(DIR_NAME, picNameTemp));
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public int getPicWidth() {
		return mPicWidth;
	}

	public void setPicWidth(int mPicWidth) {
		this.mPicWidth = mPicWidth;
	}

	public int getPicHeight() {
		return mPicHeight;
	}

	public void setPicHeight(int mPicHeight) {
		this.mPicHeight = mPicHeight;
	}

	public File getPicFile() {
		return mPicFile;
	}

	public void setPicFile(File mPicFile) {
		this.mPicFile = mPicFile;
	}

	public File getPicFileTemp() {
		return mPicFileTemp;
	}

	public void setPicFileTemp(File mPicFileTemp) {
		this.mPicFileTemp = mPicFileTemp;
	}

	public void deleteFile() {
		if (mPicFile.exists()) {
			mPicFile.delete();
		}
	}

	private File createImageFile(String path, String fileName) {
		File file = new File(Environment.getExternalStorageDirectory().getPath(), path);

		if (!file.exists()) {
			file.mkdirs();
		}

		Log.d("ImageUtil", file.getAbsolutePath());
		File f = new File(file, fileName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 拍照后,裁剪图片
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", mPicWidth);
		intent.putExtra("outputY", mPicHeight);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPicFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false);
		((Activity) context).startActivityForResult(intent, HANDLE_PIC);
	}

	/**
	 * 直接从图库裁剪图片
	 */
	public void getAndCropPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", mPicWidth);
		intent.putExtra("outputY", mPicHeight);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPicFile));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection
		((Activity) context).startActivityForResult(intent, HANDLE_PIC);
	}

	/**
	 * 从图库直接选取完整图片
	 */
	public void getExternalPic() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		((Activity) context).startActivityForResult(intent, PICK_PIC);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	public void setPicToView(Context context, ImageView view, Uri uri) {
		Bitmap photo = decodeUriAsBitmap(context, uri);

		if (null != photo) {
			view.setImageBitmap(photo);
		}
	}

	public Bitmap decodeUriAsBitmap(Context context, Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/***
	 * 编码图片
	 */
	public Bitmap decodeBitmapFormURL(Context context, Uri uri) {
		if (uri == null) {
			return null;
		}

		Bitmap bitmap = null;
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			bitmap = BitmapFactory.decodeFile(picturePath);
			cursor.close();
		}

		if (bitmap == null) {
			try {
				// bitmap =
				// MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
				bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
