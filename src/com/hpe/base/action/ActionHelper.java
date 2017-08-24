package com.hpe.base.action;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.hpe.base.annotation.AnnotationHelper;
import com.hpe.base.annotation.RequestMapping;
import com.hpe.base.entity.*;
import com.hpe.base.util.LoadeConfig;
import com.hpe.util.PrintManager;


/**
 * 用于遍历带有@Action注解的类
 * @author caoj
 *
 */
public class ActionHelper{
	
	private static final Map<String,ActionBean> actionMap = new HashMap<String,ActionBean>();
	
	/**
	 * 初始化时扫描注解得到所有带有@Action注解的类并获取类中的方法以及其参数列表
	 */
	public static void  initActionHelper() {
		List<Class<? extends Entity>> actionList = AnnotationHelper.getClassListByAnnotation(Action.class, LoadeConfig.get("actionBasePaths"));
		
		if(actionList!=null&&!actionList.isEmpty()){
			for (Class<?> actionClass : actionList) {
				Method[] actionMethods = actionClass.getDeclaredMethods();
				ClassAssist classAssist = new ClassAssist(actionClass);
				
				if(actionMethods!=null&&actionMethods.length!=0)
				{
					ScannActionMethodsToMap(actionClass, actionMethods, classAssist);
				}
				
				
			}
		}
		PrintManager.printAsSymbol("初始化ActionHelper完成");
	}

	
	/**
	 * 对扫描得到的集合进行处理
	 * 获取并遍历类中所有的方法（不包括父类中的方法）
	 * @param actionList
	 */
	private static void ScannActionMethodsToMap(Class<?> actionClass, Method[] actionMethods, ClassAssist classAssist) {
		for (Method actionMethod : actionMethods) {
			String requestPath = "/" + actionMethod.getName();
			String classRequestMappingStr = "";
			
			ActionBean actionBean = new ActionBean(actionClass,actionMethod,classAssist);
			
			/**
			 * 获取类@RequestMapping注解中的请求路径
			 */
			if (actionClass.isAnnotationPresent(RequestMapping.class)) {
				RequestMapping rm = actionClass.getAnnotation(RequestMapping.class);
				String value = rm.value();
				if (!value.equals("/")) {
					classRequestMappingStr = value;
				}
			}
			
			/**
			 * 通过类@Controller注解得到actionBean名字
			 */
			if(actionClass.getAnnotation(Controller.class)!=null){
				String beanName = actionClass.getAnnotation(Controller.class).value();
                if("".equals(beanName.trim())){
                	beanName = actionClass.getSimpleName().substring(0,1).toLowerCase()+actionClass.getSimpleName().substring(1);
                }
                actionBean.setBean(beanName);
            }
			
			/**
			 * 访问路径 classRequestMappingStr + requestPath
			 * actionBean中包含类、方法以及方法的所有参数信息
			 */
			actionMap.put(classRequestMappingStr + requestPath, actionBean);
		}
	}
	
	
	public static Map<String, ActionBean> getActionMap(){
		return actionMap;
	}
}
