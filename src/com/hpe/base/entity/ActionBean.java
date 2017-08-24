package com.hpe.base.entity;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

public class ActionBean {

	private Class<?> actionClass;
	
	private String bean;
	
    private Method actionMethod;
    
    private Map<String,Map<String,Type>> methodParamsAndType;
    public ActionBean(Class<?> actionClass, Method actionMethod,ClassAssist classAssist) {
        this.actionClass = actionClass;
        this.actionMethod = actionMethod;
        this.methodParamsAndType = classAssist.getMethodParamsAndType(this.actionMethod.getName(),this.actionMethod);
    }

    public Class<?> getActionClass() {
        return actionClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

	public void setBean(String bean) {
		this.bean = bean;
	}

	public String getBean() {
		return bean;
	}

	public Map<String,Map<String,Type>> getMethodParamsAndType() {
		return methodParamsAndType;
	}

	public void setMethodParamsAndType(Map<String,Map<String,Type>> methodParamsAndType) {
		this.methodParamsAndType = methodParamsAndType;
	}
}
