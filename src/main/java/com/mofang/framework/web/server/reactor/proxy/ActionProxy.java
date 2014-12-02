package com.mofang.framework.web.server.reactor.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 
 * @author zhaodx
 *
 */
public class ActionProxy 
{
	private final static ActionProxy PROXY = new ActionProxy();
	
	private ActionProxy()
	{}
	
	public static ActionProxy getInstance()
	{
		return PROXY;
	}

	public ActionExecutor getProxy(ActionExecutor executor)
	{
		InvocationHandler handler = new ActionInvocationHandler(executor);
		Class<?> cls = executor.getClass();
		ClassLoader loader = cls.getClassLoader();
		Class<?>[] interfaces = cls.getInterfaces();
		if(null == interfaces || interfaces.length == 0)
		{
			Class<?> superCls = cls.getSuperclass();
			if(null != superCls)
				interfaces = superCls.getInterfaces();
		}
		return (ActionExecutor)Proxy.newProxyInstance(loader,  interfaces, handler);
	}
}