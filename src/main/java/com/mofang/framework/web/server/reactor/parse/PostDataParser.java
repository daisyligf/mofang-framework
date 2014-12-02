package com.mofang.framework.web.server.reactor.parse;

import java.util.Map;

/**
 * 
 * @author zhaodx
 *
 */
public interface PostDataParser
{
	Map<String, String> parse(String postData) throws Exception;
}