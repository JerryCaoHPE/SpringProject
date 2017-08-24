package com.hpe.base;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.hpe.util.PrintManager;
import com.hpe.util.StringUtils;
import com.hpe.base.action.ActionHelper;
import com.hpe.base.entity.ActionBean;
import com.hpe.base.exception.HandleMethodReturnException;
import com.hpe.base.exception.LoadeMathodParamsException;
import com.hpe.base.util.LoadeConfig;
import com.hpe.base.util.SpringContextUtil;

public class Dispatcher implements Filter{
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain  filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		httpServletRequest.setCharacterEncoding(LoadeConfig.get("BASE_CHARSET"));
		
		// 获取请求方法
		String currentRequestMethod = httpServletRequest.getMethod();
		// 获取请求路径
		String currentRequestPath = getRequestPath(httpServletRequest);
		
		PrintManager.print("请求方法："+currentRequestMethod);
		PrintManager.print("请求路径："+currentRequestPath);
		PrintManager.print("请求参数："+new String(JSON.toJSONString(request.getParameterMap(), true).getBytes("iso-8859-1"), "utf-8")+"");
		
		// 静态资源处理
		if (LoadeConfig.get("FacadeServlet").equals(currentRequestPath))
		{
			filterChain.doFilter(request, response);
		}	
		else // 其他请求
		{
			boolean isMapped = actionHandler(httpServletRequest, httpServletResponse, filterChain,  currentRequestPath);
			
			// 如果映射失败就执行默认的操作
			if (!isMapped)
			{
				filterChain.doFilter(request, response);
			}
		}
	}
	
	/**
	 * 处理请求
	 * @param request
	 * @param response
	 * @param filterChain
	 * @param requestPath 当前请求路径
	 */
	private boolean actionHandler(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String requestPath) {
		
		//  从 Action 映射中获得请求的ActionBean
		ActionBean actionBean = ActionHelper.getActionMap().get(requestPath);
		
		// 获得请求的方法并设置其可访问
		Method actionMethod = actionBean.getActionMethod();;
		actionMethod.setAccessible(true);
		
		// 得到action实例
		Object actionInstance = SpringContextUtil.getBean(actionBean.getBean());
		
		Object[] actionMethodParams = null; // 待填充参数列表
		Object actionMethodResult = null; // 执行方法的结果
		
		try{
			actionMethodParams = createActionMethodParamList(request, response, actionBean);
			
			actionMethodResult = actionMethod.invoke(actionInstance, actionMethodParams);
			//SessionFactory.commitTransaction();
			
			handleActionMethodReturn(request, response, filterChain, actionMethodResult);
			
			return true;
		}
		catch(LoadeMathodParamsException e)
		{
			
		}catch (IllegalArgumentException e) {
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (HandleMethodReturnException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	private void handleActionMethodReturn(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain,
			Object actionMethodResult) throws HandleMethodReturnException{
		// TODO Auto-generated method stub
		
	}

	private Object[] createActionMethodParamList(HttpServletRequest request,HttpServletResponse response, ActionBean actionBean) throws LoadeMathodParamsException{
		// TODO Auto-generated method stub
		return null;
	}

	// 得到请求路径
	public static String getRequestPath(HttpServletRequest request)
	{
		String servletPath = request.getServletPath();
		String pathInfo = StringUtils.defaultIfEmpty(request.getPathInfo(),"DefaultPath");
		return servletPath + pathInfo;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		
		/**
		 * 加载配置文件
		 */
		LoadeConfig.initLoadeConfig();
		
		/**
		 * 初始化时加载所有带@Action注解的类
		 */
		ActionHelper.initActionHelper();
	}
}
