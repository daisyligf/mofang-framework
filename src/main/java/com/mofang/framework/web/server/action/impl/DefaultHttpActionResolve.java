package com.mofang.framework.web.server.action.impl;

import com.mofang.framework.web.server.action.ActionResolve;
import com.mofang.framework.web.server.reactor.context.HttpRequestContext;
import com.mofang.framework.web.server.reactor.context.RequestContext;

/**
 * 
 * @author zhaodx
 *
 */
public class DefaultHttpActionResolve implements ActionResolve
{
	@Override
	public String getAction(RequestContext context) throws Exception
	{
		HttpRequestContext ctx = (HttpRequestContext)context;
		String url = ctx.getRequestUrl();
		if(null == url || "".equals(url))
			return null;
		
		String action = url;
		int pos = action.indexOf("?");
		if(pos > 0)
			action = action.substring(0, pos);
		
		if(action.startsWith("/"))
			action = action.substring(1, action.length());
		return action;
	}
}