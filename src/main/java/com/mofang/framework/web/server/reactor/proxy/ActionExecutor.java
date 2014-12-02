package com.mofang.framework.web.server.reactor.proxy;

import com.mofang.framework.web.server.reactor.context.RequestContext;

/**
 * action interface 
 * @author milozhao
 *
 */
public interface ActionExecutor
{
	public String execute(RequestContext context);
}