package com.hpe.base.annotation;

import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.hpe.base.util.LoadeConfig;
import com.hpe.util.StringUtils;
import com.hpe.base.entity.Entity;
import com.hpe.util.PrintManager;


public class AnnotationHelper {
	
	public static List<Class<? extends Entity>>  getClassListByAnnotation(Class<? extends Annotation> annotation, String basePath){
		
		List<Class<? extends Entity>> classes = new ArrayList<Class<? extends Entity>>();
		
		try{
			String[] packageNames = basePath.trim().split(",");
			for (String packageName : packageNames) {
				Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
				while (urls.hasMoreElements()) {
					URL url = urls.nextElement();
					if (url != null) {
						String protocol = url.getProtocol();
						if (protocol.equals("file")) {
							String packagePath = url.getPath();
							packagePath = URLDecoder.decode(packagePath,LoadeConfig.get("BASE_CHARSET"));
							addClassByAnnotation(classes,packageName,packagePath,annotation);
						}
						else if(protocol.equals("jar")) {
							protocolIsJar();
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return classes;
	}
	
	private static void protocolIsJar() {
		
	}

	private static void addClassByAnnotation(List<Class<? extends Entity>> classes, String packageName, String packagePath, Class<? extends Annotation> annotation) {
		try {
			File[] files = getClassFiles( packagePath );
			
			for (File file : files) 
			{
				String fileName = file.getName();
				if( file.isFile())
				{
					String className = getClassName(packageName, fileName);
					
					Class<? extends Entity> clazz = loadClass(className, false);
					if (clazz.isAnnotationPresent(annotation)) {
						classes.add(clazz);
						PrintManager.print(file.getName());
					}
				}
				else
				{
					String subPackagePath = getSubPackagePath(packagePath, fileName);
					String subPackageName = getSubPackageName(packageName, fileName);
					addClassByAnnotation(classes, subPackagePath, subPackageName, annotation);
				}
			}
		} catch (NullPointerException e) {
			PrintManager.print("加载文件失败：" + e.getMessage());
		}
	}
	
	private static String getSubPackagePath(String packagePath, String filePath) {
		String subPackagePath = filePath;
		if (StringUtils.isNotEmpty(packagePath)) {
			subPackagePath = packagePath + "/" + subPackagePath;
		}
		return subPackagePath;
	}

	private static String getSubPackageName(String packageName, String filePath) {
		String subPackageName = filePath;
		if (StringUtils.isNotEmpty(packageName)) {
			subPackageName = packageName + "." + subPackageName;
		}
		return subPackageName;
	}
	
	private static String getClassName(String packageName, String fileName) {
		String className = fileName.substring(0, fileName.lastIndexOf("."));
		if (StringUtils.isNotEmpty(packageName)) {
			className = packageName + "." + className;
		}
		return className;
	}
	
	
	/** 
	 * 
	 * 加载类 
	 */
	public static Class<? extends Entity> loadClass(String className, boolean isInitialized) {
		Class<? extends Entity> cls = null;
		try {
			cls = (Class<? extends Entity>) Class.forName(className, isInitialized, getClassLoader());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return cls;
	}
	
	/**
	 * 加载指定目录下的Class文件
	 */
	private static File[] getClassFiles(String packagePath) {
		return new File(packagePath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isFile() && file.getName().endsWith(".class"))
						|| file.isDirectory();
			}
		});
	}


	/** 获取类加载器 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
}
