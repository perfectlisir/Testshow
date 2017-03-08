package com.example.administrator.testshow.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

//sd卡工具类
public class SDCardUtils {
	// 判断SDCard是否挂载
	// Environment.MEDIA_MOUNTED,表示SDCard已经挂载
	// Environment.getExternalStorageState()，获得当前SDCard的挂载状态
	public static boolean isMounted() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return true;
		}
		return false;
	}

	// 获得SDCard 的路径,storage/sdcard
	public static String getSDCardPath() {
		String path = null;
		if (isMounted()) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return path;
	}

	// 获得SDCard 的大小,单位，兆
	public static long getSize() {
		if (isMounted()) {
			// statFs,用来计算文件系统存储空间大小的工具类
			// 参数为SDCard 的根目录
			StatFs stat = new StatFs(getSDCardPath());

			// 获得块的数量
			long count = stat.getBlockCountLong();
			// 获得块的大小,单位字节
			long size = stat.getBlockSizeLong();

			return count * size / 1024 / 1024;
		}
		return 0;
	}

	// 获得SDCard的可用空间大小
	public static long getAvailableSize() {
		if (isMounted()) {
			StatFs stat = new StatFs(getSDCardPath());
			// 获得可用的块的数量
			long count = stat.getAvailableBlocksLong();
			long size = stat.getBlockSizeLong();
			return count * size / 1024 / 1024;
		}
		return 0;
	}

	// 讲数据存储到SDCard中
	// data,存储的数据
	// dir.存储的路径
	// fileName,存储 的文件名
	public static boolean saveDataToSDCard(byte[] data, String dir,
			String fileName) {
		boolean flag = false;
		if (isMounted()) {
			// 首先判断当前文件夹是否存在，如果不存在，创建
			String path = getSDCardPath() + File.separator + dir;
			File file = new File(path);
			// 表示文件夹不存在
			if (!file.exists()) {
				file.mkdirs();
			}

			// 文件夹创建成功，之后存储数据
			file = new File(path + File.separator + fileName);
			BufferedOutputStream bos = null;
			try {
				bos = new BufferedOutputStream(new FileOutputStream(file));
				bos.write(data);
				bos.flush();

				flag = true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					if (bos != null)
						bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
		return flag;
	}

	// 读取SDCArd的数据

	public static byte[] getDataFromSDCard(String dir, String fileName) {
		byte[] b = null;

		if (isMounted()) {
			String path = getSDCardPath() + File.separator + dir
					+ File.separator + fileName;
			File file = new File(path);
			if (file.exists()) {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					BufferedInputStream bis = new BufferedInputStream(
							new FileInputStream(file));
					byte[] buf = new byte[1024 * 8];
					int n = 0;
					while ((n = bis.read(buf)) != -1) {
						baos.write(buf, 0, n);
						baos.flush();
					}

					b = baos.toByteArray();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return b;
	}

	// 根据类型获得公共路径
	public static String getPublicPath(String type) {
		if (isMounted()) {
			return Environment.getExternalStoragePublicDirectory(type)
					.getAbsolutePath();
		}
		return null;
	}

	// 将数据写入公共路径下
	public static boolean saveDataToSDCardPublic(byte[] data, String type,
			String fileName) {
		boolean flag = false;
		if (isMounted()) {
			// 获得公共的路径
			File file = new File(getPublicPath(type));
			if (!file.exists()) {
				file.mkdirs();
			}

			try {
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(new File(file, fileName)));
				bos.write(data);
				bos.flush();

				flag = true;
				bos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}

	// 获得私有的路径
	public static String getPrivatePath(Context context, String type) {
		if (isMounted()) {
			return context.getExternalFilesDir(type).getAbsolutePath();
		}
		return null;
	}

	// 将数据保存到私有的路径下
	public static boolean saveDataToSDCardPrivate(byte[] data, Context context,
			String type, String fileName) {
		boolean flag = false;
		if (isMounted()) {
			File file = new File(getPrivatePath(context, type));
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(new File(file, fileName)));
				bos.write(data);
				bos.flush();

				flag = true;
				bos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flag;
	}
}
