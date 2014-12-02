package com.mofang.framework.web.server.action;

import com.mofang.framework.web.server.reactor.context.RequestContext;

/**
 * 
 * @author zhaodx
 *
 */
public interface ActionResolve
{
	public String getAction(RequestContext context) throws Exception;
}