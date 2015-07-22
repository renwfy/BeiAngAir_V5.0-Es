package com.beiang.airdog.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.beiang.airdog.constant.Constants;

/**
 * 文件处理工具
 */
public class FileUtils {

	/**
	 * 保存字节流至文件
	 * 
	 * @param bytes
	 *            字节�?
	 * @param file
	 *            目标文件
	 */
	public static final boolean saveBytesToFile(byte[] bytes, File file) {
		if (bytes == null) {
			return false;
		}

		ByteArrayInputStream bais = null;
		BufferedOutputStream bos = null;
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();

			bais = new ByteArrayInputStream(bytes);
			bos = new BufferedOutputStream(new FileOutputStream(file));

			int size;
			byte[] temp = new byte[1024];
			while ((size = bais.read(temp, 0, temp.length)) != -1) {
				bos.write(temp, 0, size);
			}

			bos.flush();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bais = null;
			}
		}
		return false;
	}

	/**
	 * 复制文件�?
	 * 
	 * @param srcFile  源文�?
	 *           
	 * @param destFile 目标文件
	 *         
	 */
	public static final void copyFileFolder(String srcFile, String destFile) {
	      // 新建目标目录
        (new File(destFile)).mkdirs();
        // 获取源文件夹当前下的文件或目�?
        File[] file = (new File(srcFile)).listFiles();
        if(file != null){
        	   for (int i = 0; i < file.length; i++) {
                   if (file[i].isFile()) {
                       // 源文�?
                       File sourceFile = file[i];
                       // 目标文件
                       File targetFile = new File(new File(destFile).getAbsolutePath() + File.separator + file[i].getName());
                       copyFile(sourceFile, targetFile);
                   }
               }
        }
	}
	
	//复制文件�?
  public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
      // 新建目标目录
      (new File(targetDir)).mkdirs();
      // 获取源文件夹当前下的文件或目�?
      File[] file = (new File(sourceDir)).listFiles();
      for (int i = 0; i < file.length; i++) {
          if (file[i].isFile()) {
              // 源文�?
              File sourceFile = file[i];
              // 目标文件
              File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
              copyFile(sourceFile, targetFile);
          }
          if (file[i].isDirectory()) {
              // 准备复制的源文件�?
              String dir1 = sourceDir + "/" + file[i].getName();
              // 准备复制的目标文件夹
              String dir2 = targetDir + "/" + file[i].getName();
              copyDirectiory(dir1, dir2);
          }
      }
  }
	
	/**
	 * 根据URL获取到下载的文件名称
	 * @param url 
	 * 		下载路径
	 * @return 下载文件名称
	 */
	public static String getFileNameByUrl(String url){
		int postion = url.lastIndexOf("/");
		return url.substring(postion + 1);
	}
	
	/**
	 * 根据文件路径获取文件名称
	 * @param path 
	 * 		文件路径
	 * @return 文件名称
	 */
	public static String getFileNameByPath(String path){
		int postion = path.lastIndexOf("/");
		return path.substring(postion + 1);
	}
	
	/**
	 * 复制文件
	 * 
	 * @param srcFile
	 *            源文�?
	 * @param destFile
	 *            目标文件
	 */
	public static final boolean copyFile(File srcFile, File destFile) {
		if (!srcFile.exists()) {
			return false;
		}

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			destFile.getParentFile().mkdirs();
			destFile.createNewFile();

			bis = new BufferedInputStream(new FileInputStream(srcFile));
			bos = new BufferedOutputStream(new FileOutputStream(destFile));

			int size;
			byte[] temp = new byte[1024];
			while ((size = bis.read(temp, 0, temp.length)) != -1) {
				bos.write(temp, 0, size);
			}

			bos.flush();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bos = null;
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				bis = null;
			}
		}
		return false;
	}



	public static final void saveBitmapToFile(Bitmap bitmap, String savePath) {
		try {
			File f = new File(savePath);

			f.createNewFile();

			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bitmap.compress(Bitmap.CompressFormat.PNG, 0, fOut);
			try {
				fOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 获得指定文件的byte数组
	 */
	public static byte[] getCodeByFilePath(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			if(file.exists()){
				FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
				byte[] b = new byte[1024];
				int n;
				while ((n = fis.read(b)) != -1) {
					bos.write(b, 0, n);
				}
				fis.close();
				bos.close();
				buffer = bos.toByteArray();

				return buffer;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	public static byte[] getCodeByFile(File file) {
		byte[] buffer = null;
		try {
			if(file.exists()){
				FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
				byte[] b = new byte[1024];
				int n;
				while ((n = fis.read(b)) != -1) {
					bos.write(b, 0, n);
				}
				fis.close();
				bos.close();
				buffer = bos.toByteArray();

				return buffer;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}

	/**
	 * 根据byte数组，生成文�?
	 */
	public static void saveBytesToFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static byte[] stringToByte(String code){
		int b_leng = 0;
		int cfgData_x = 0;
		int cfgData_t = code.length() / 2 + b_leng;
		byte[] b = new byte[cfgData_t];
		for (int x = b_leng; x < cfgData_t; x++) {
			String s = code.substring(cfgData_x, cfgData_x + 2);
			b[x] = (byte) Integer.parseInt(s, 16);
			cfgData_x = cfgData_x + 2;
			b_leng ++;
		}
		
		return b;
	}

	
	public static final void saveStringToFile(String value,String fileName){
		File file = new File(fileName);
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(value); 
			fw.flush();
			fw.close();
			
//			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true),"GB2312");
//			osw.write(fileName);
//			osw.flush();
//			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final String getStringByFile(String fileName){
		try {
			File file = new File(fileName);
			if(file.exists()){
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String str = null;
				while((str = reader.readLine())!= null){
					System.out.println(file.getName() + ":" + str);
					return str;
				}
				reader.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除文件夹及下面的子文件
	 * @param file
	 */
	public static void deleteFile(File deletedFile) {
		if (deletedFile.exists()) { // 判断文件是否存在
			if (deletedFile.isFile()) { // 判断是否是文�?
				deletedFile.delete(); // delete()方法 你应该知�?是删除的意�?;
			} else if (deletedFile.isDirectory()) { // 否则如果它是�?��目录
				File files[] = deletedFile.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文�?用这个方法进行迭�?
				}
			}
			//deletedFile.delete();
		}
	}
	
	public static boolean checkFileExist(String path){
		File file = new File(path);
		return file.exists();
	}
	
	/** 
	 * 复制assets中的文件到指定目录下 
	 * @param context 
	 * @param assetsFileName 
	 * @param targetPath 
	 * @return 
	 */
	public static void copyAssetData(Context context, String folderName, String fileName,String targetPath) { 
	    try { 
	        InputStream inputStream = context.getAssets().open(folderName + File.separator + fileName); 
	        File outFile = new File(targetPath, fileName);    

	        if(outFile.exists())
	        	return;
	        
	        FileOutputStream output = new FileOutputStream(outFile); 
	        byte[] buf = new byte[10240]; 
	        int count = 0; 
	        while ((count = inputStream.read(buf)) > 0) { 
	            output.write(buf, 0, count); 
	        } 
	        output.close(); 
	        inputStream.close(); 
	    } catch (IOException e) { 
	        e.printStackTrace(); 
	    } 
	} 
	 
	public static void copyAssetFolderToSd(Context context, String folderName, String targetPath){
		try {
			String[] fileliStrings = context.getAssets().list(folderName);
			for(String fileName : fileliStrings){
				copyAssetData(context,  folderName, fileName, targetPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void makedirs() {
		// 文件路径设置
		String parentPath = null;
		String fileName = Constants.FILE_NAME;

		// 存在SDCARD的时候，路径设置到SDCARD
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			parentPath = Environment.getExternalStorageDirectory().getPath() + File.separator + fileName;
			// 不存在SDCARD的时候，路径设置到ROM
		} else {
			parentPath = Environment.getDataDirectory().getPath() + "/data/" + fileName;
		}

		Settings.BASE_PATH = parentPath;

		Settings.TEMP_PATH = parentPath + "/temp";
		Settings.CONFIG_PATH = parentPath + "/config";
		Settings.CACHE_PATH = parentPath + "/cache";
		Settings.DEVICE_ICON_PATH = parentPath + File.separator + Constants.FILE_DEVICE_ICON;
		Settings.DEVICE_ALL_ICON_PATH = parentPath + File.separator + Constants.FILE_ALL_DEVICE_ICON;
		Settings.RECORD_PATH = parentPath + File.separator + Constants.RECORD;

		File fileBase = new File(Settings.BASE_PATH);
		fileBase.mkdirs();

		File fileTemp = new File(Settings.TEMP_PATH);
		fileTemp.mkdirs();

		File configTemp = new File(Settings.CONFIG_PATH);
		configTemp.mkdirs();

		File fileCahe = new File(Settings.CACHE_PATH);
		fileCahe.mkdirs();

		File file_DEVICE_ICON = new File(Settings.DEVICE_ICON_PATH);
		file_DEVICE_ICON.mkdirs();

		File file_DEVICE_ALL_ICON = new File(Settings.DEVICE_ALL_ICON_PATH);
		file_DEVICE_ALL_ICON.mkdirs();

		File file_DEVICE_ICON_PATH = new File(Settings.DEVICE_ICON_PATH, ".nomedia");
		file_DEVICE_ICON_PATH.mkdirs();

		File file_DEVICE_ALL_ICON_PATH = new File(Settings.DEVICE_ALL_ICON_PATH, ".nomedia");
		file_DEVICE_ALL_ICON_PATH.mkdirs();
		
		File file_RECORD_PATH = new File(Settings.RECORD_PATH);
		file_RECORD_PATH.mkdirs();
	}
}
