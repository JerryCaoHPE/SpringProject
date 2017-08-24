package com.hpe.util;

import java.util.Date;

public class PrintManager{
	private static final int MESSAGETOTLELEHGTH = 100;
	private static final String MESSAGESYMBOL = "*";
	private static final String MESSAGEROWSYMBOL = " ";

	public static void print(String message) {
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        
		@SuppressWarnings("deprecation")
		String dateString = new Date().toGMTString();
		
		System.out.println( dateString + " " + className +  "." + methodName + " line:" +lineNumber);
		System.out.println( "INFO:" + message);
	}
	
	public static void printAsSymbol(String message)
	{
		message = getMessageRow(message);
		int messageLehgth = getLength(message);
		int symbolLehgth = getSymbolLength(messageLehgth);
		String symbolMessage = getSymbol(MESSAGETOTLELEHGTH, MESSAGESYMBOL);

		System.out.println(symbolMessage);
		System.out.print(getSymbol(symbolLehgth, MESSAGEROWSYMBOL));
		System.out.println(message);
		System.out.println(symbolMessage);
	}
	
	
	private static String getMessageRow(String message)
	{
		message = MESSAGESYMBOL+" " + message +" " + MESSAGESYMBOL;
		
		return message;
	}

	private static String getSymbol(int symbolNum, String symbol) {
		String tempSymbol = "";

		for (int i = 0; i < symbolNum; i++) {
			tempSymbol += symbol;
		}

		return tempSymbol;
	}

	private static int getSymbolLength(int messageLehgth) {

		return (MESSAGETOTLELEHGTH - messageLehgth - 2) / 2;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5
	 * 
	 * @param String
	 *            s 需要得到长度的字符串
	 * @return int 得到的字符串长度
	 */
	private static int getLength(String s) {
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < s.length(); i++) {
			String temp = s.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return (valueLength%2)==0?valueLength:(valueLength+1);
	}
}
