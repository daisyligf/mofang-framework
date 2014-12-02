package com.mofang.framework.web.server.reactor;

/**
 * 
 * @author zhaodx
 *
 */
public class ActionLoaderFactory
{
	public static ActionLoader getActionLoader()
	{
		return new AnnotationActionLoader();
	}
}