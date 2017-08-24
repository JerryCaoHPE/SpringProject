package com.hpe.base.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.hpe.util.PrintManager;

public class LoadeConfig {

	private static final Properties config = new Properties();

	public static void initLoadeConfig() { 
		try {
			InputStream in = new BufferedInputStream (new FileInputStream( LoadeConfig.class.getResource("").getPath()+"base_config.properties"));
			config.load(in);
			in.close();
			PrintManager.printAsSymbol("初始化LoadeConfig完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		
		String value = config.getProperty(key).toString();
		
		if(value==null)
		{
			PrintManager.print("加载配置出错:未找到基础配置中关于"+ key +"的配置！");
		}
		return value;
	}
	
	public static void set(String key,String value) {
		config.setProperty(key,value);
	}
}
