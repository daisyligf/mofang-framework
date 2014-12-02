package com.mofang.framework.web.server.action.impl;

import com.mofang.framework.web.server.action.ActionResolve;
import com.mofang.framework.web.server.reactor.context.RequestContext;

/**
 * 
 * @author zhaodx
 *
 */
public abstract class AbstractActionResolve implements ActionResolve
{
	@Override
	public String getAction(RequestContext context) throws Exception
	{
		String postData = context.getPostData();
		if(null == postData || "".equals(postData))
			return null;
		
		return resolveAction(postData);
	}
	
	protected abstract String resolveAction(String content) throws Exception; 
}