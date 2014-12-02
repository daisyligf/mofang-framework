package com.mofang.framework.web.server.reactor.parse.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import com.mofang.framework.web.server.reactor.parse.PostDataParser;

/**
 * 
 * @author zhaodx
 *
 */
public class JsonPostDataParser implements PostDataParser
{
	@Override
	public Map<String, String> parse(String postData) throws Exception
	{
		if(null == postData || "".equals(postData))
			return null;
	
		Map<String, String> paramMap = new HashMap<String, String>();
		JSONObject json = new JSONObject(postData);
		Iterator<?> iterator = json.keys();
		while(iterator.hasNext())
		{
			String key = (String) iterator.next();
			paramMap.put(key, json.optString(key));
		}
		return paramMap;
	}
}
