package com.mofang.framework.web.server.logger;

/**
 * 
 * @author zhaodx
 *
 */
public interface WebServerLogger
{
	public void info(WebServerLoggerEntity entity);
	
	public void warning(WebServerLoggerEntity entity);
	
	public void error(WebServerLoggerEntity entity);
}