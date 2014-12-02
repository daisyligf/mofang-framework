package com.mofang.framework.web.server.reactor.context;

import java.net.SocketAddress;
import java.util.Map;

/**
 * 
 * @author zhaodx
 *
 */
public interface RequestContext
{
	String getRequestUrl();
	
	String getParameters(String key);
	
	Map<String, String> getParamMap();
	
	SocketAddress getRemoteAddress();

	String getPostData();
}