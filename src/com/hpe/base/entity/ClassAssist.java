package com.hpe.base.entity;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;


public class ClassAssist {
	private Class<?> clazz;
	private CtClass ctClass;
	private ClassPool pool = ClassPool.getDefault();
	
	public ClassAssist(Class<?> clazz) {
		super();
		this.clazz = clazz;
		pool.insertClassPath(new ClassClassPath(this.clazz));
	};
	
	/**
	 * 使用javaassist的反射方法获取方法的参数名
	 * @param methodName
	 * @return
	 */
	public Map<String,Class<?>> getMethodParamNames(Method method) {
		try {
			if(this.ctClass==null){
				try {
					this.ctClass = pool.get(this.clazz.getName());
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
			CtMethod ctMethod = this.ctClass.getDeclaredMethod(method.getName());
	
			MethodInfo methodInfo = ctMethod.getMethodInfo(); 
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

			Map<String,Class<?>> paramNames = new HashMap<String, Class<?>>();
			int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
			Class<?>[] types = method.getParameterTypes();
			for (int i = 0; i < types.length; i++)
			{
				paramNames.put(attr.variableName(i + pos),types[i]);
			}
			return paramNames;
		} catch (NotFoundException e) {
			System.err.println(method);
			e.printStackTrace();
			return null;
		}
	}    
	
	/**
	 * 使用javaassist的反射方法获取方法的参数名
	 * @param methodName
	 * @return
	 */
	public  Map<String,Map<String,Type>> getMethodParamsAndType(String methodName,Method actionMethod) {
		try {
			if(this.ctClass==null){
				try {
					this.ctClass = pool.get(this.clazz.getName());
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
			}
			CtMethod ctMethod = this.ctClass.getDeclaredMethod(methodName);
	
			Type[] paraTypes = actionMethod.getGenericParameterTypes();
			
			MethodInfo methodInfo = ctMethod.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);

			Map<String,Map<String,Type>> paramNames = new LinkedHashMap<String,Map<String,Type>>();
			int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
			for (int i = 0; i < ctMethod.getParameterTypes().length; i++)
			{
				String paramName = attr.variableName(i + pos);
				Class<?> parameterType = actionMethod.getParameterTypes()[i];
				
				Map<String,Type> parameterTypeMap = new LinkedHashMap<String,Type>();
				if(Collection.class.isAssignableFrom(parameterType))
				{
					String genericType = actionMethod.getGenericParameterTypes()[i].toString();
					int startIndex = genericType.indexOf('<');
					int endIndex =genericType.indexOf('>');
					String genericClassName = genericType.substring(startIndex+1,endIndex);
					Class<?> genericClass= Class.forName(genericClassName);
					parameterTypeMap.put("genericClass", genericClass);
				}
				parameterTypeMap.put("parameterType", parameterType);
				parameterTypeMap.put("paraType", paraTypes[i]);
				
				paramNames.put(paramName, parameterTypeMap);
			}
			return paramNames;
		} catch (Exception e) {
			System.err.println(actionMethod);
			e.printStackTrace();
			return null;
		}
	} 
}
