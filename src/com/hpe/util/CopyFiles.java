package com.hpe.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author Cao JianLin
 * @date : 2017-09-248 09:09:39 
 * CopyFiles.java 拷贝文件（夹）
 */

public class CopyFiles {
	
	/**
	 * copy(File originalFile, File goalFile) 拷贝文件（夹）到指定文件（夹）
	 * @param originalFile
	 * @param goalFile
	 * @throws Exception
	 */
	public static void copy(File originalFile, File goalFile) throws Exception { 
		byte[] b = new byte[1024]; 
		int a; 
		FileInputStream fis; 
		FileOutputStream fos; 
		if (originalFile.isDirectory()) { 
			String filepath = originalFile.getAbsolutePath(); 
			filepath=filepath.replaceAll("\\\\", "/"); 
			String toFilepath = goalFile.getAbsolutePath(); 
			toFilepath=toFilepath.replaceAll("\\\\", "/"); 
			int lastIndexOf = filepath.lastIndexOf("/"); 
			toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length()); 
			File copy=new File(toFilepath); 
			//复制文件夹 
			if (!copy.exists()) { 
				copy.mkdir(); 
			} 
			//遍历文件夹 
			for (File f : originalFile.listFiles()) { 
				copy(f, copy);
			} 
		} 
		else { 
			if (goalFile.isDirectory()) { 
				String filepath = originalFile.getAbsolutePath(); 
				filepath=filepath.replaceAll("\\\\", "/"); 
				String toFilepath = goalFile.getAbsolutePath(); 
				toFilepath=toFilepath.replaceAll("\\\\", "/"); 
				int lastIndexOf = filepath.lastIndexOf("/"); 
				toFilepath = toFilepath + filepath.substring(lastIndexOf ,filepath.length()); 

				//写文件 
				File newFile = new File(toFilepath); 
				fis = new FileInputStream(originalFile); 
				fos = new FileOutputStream(newFile); 
				while ((a = fis.read(b)) != -1) { 
					fos.write(b, 0, a); 
				} 
			}
			else { 
				//写文件 
				fis = new FileInputStream(originalFile); 
				fos = new FileOutputStream(goalFile); 
					while ((a = fis.read(b)) != -1) { 
						fos.write(b, 0, a);
					} 
			} 
		}
	}
}
