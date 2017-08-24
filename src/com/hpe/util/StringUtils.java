package com.hpe.util;

import com.hpe.base.util.LoadeConfig;

public class StringUtils {
	
	public static String defaultIfEmpty(String value, String key)
	{
		if (value == null || value.trim().equals("") || value.trim().equalsIgnoreCase("null")) {
			value= LoadeConfig.get(key);
		}
		
		return value;
	}

	public static boolean isNotEmpty(String str) {
		if(str == null || str.trim().equals("") || str.trim().equalsIgnoreCase("null"))
			return false;
		return true;
	}
}
