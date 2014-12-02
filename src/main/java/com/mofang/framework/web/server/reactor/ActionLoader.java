package com.mofang.framework.web.server.reactor;

import java.util.Map;

import com.mofang.framework.web.server.reactor.proxy.ActionExecutor;

/**
 * 
 * @author zhaodx
 *
 */
public interface ActionLoader
{
	public Map<String, ActionExecutor> load(String path) throws Exception;
}