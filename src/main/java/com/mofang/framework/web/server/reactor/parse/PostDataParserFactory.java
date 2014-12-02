package com.mofang.framework.web.server.reactor.parse;

import com.mofang.framework.web.server.reactor.parse.impl.JsonPostDataParser;
import com.mofang.framework.web.server.reactor.parse.PostDataParserType;
import com.mofang.framework.web.server.reactor.parse.impl.TextPostDataParser;
import com.mofang.framework.web.server.reactor.parse.impl.XmlPostDataParser;

/**
 * 
 * @author zhaodx
 *
 */
public class PostDataParserFactory
{
	public static PostDataParser getInstance(PostDataParserType type)
	{
		PostDataParser parser = null;
		switch(type)
		{
			case Text:
				parser = new TextPostDataParser();
				break;
			case Json:
				parser = new JsonPostDataParser();
				break;
			case Xml:
				parser = new XmlPostDataParser();
				break;
		}
		return parser;
	}
}