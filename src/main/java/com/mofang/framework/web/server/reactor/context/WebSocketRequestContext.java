package com.mofang.framework.web.server.reactor.context;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.mofang.framework.web.server.reactor.parse.PostDataParser;
import com.mofang.framework.web.server.reactor.parse.PostDataParserFactory;
import com.mofang.framework.web.server.reactor.parse.PostDataParserType;

/**
 * 
 * @author zhaodx
 *
 */
public class WebSocketRequestContext implements RequestContext
{
	private Map<String, String> paramMap = new HashMap<String, String>();
	private WebSocketFrame frame;
	private SocketAddress remoteAddress;
	private Charset charset = Charset.forName("UTF-8");
	private PostDataParserType parserType;
	private String postData;
	private String requestUrl;
	private ChannelHandlerContext channelContext;
	
	public WebSocketRequestContext(WebSocketFrame frame, PostDataParserType type) throws Exception
	{
		this.frame = frame;
		if(null == frame)
			throw new Exception("web server receive empty frame.");
		
		this.parserType = type;
		initParam();
	}
	
	public void setRequestUrl(String requestUrl)
	{
		this.requestUrl = requestUrl;
	}
	
	public String getRequestUrl()
	{
		return this.requestUrl;
	}
	
	public String getParameters(String key)
	{
		return paramMap.get(key);
	}
	
	public Map<String, String> getParamMap() 
	{
		return paramMap;
	}
	
	public WebSocketFrame getFrame()
	{
		return this.frame;
	}

	public SocketAddress getRemoteAddress()
	{
		return remoteAddress;
	}

	public void setRemoteAddress(SocketAddress remoteAddress)
	{
		this.remoteAddress = remoteAddress;
	}
	
	public void setCharset(Charset charset)
	{
		this.charset = charset;
	}
	
	public String getPostData()
	{
		return this.postData;
	}
	
	public ChannelHandlerContext getChannelContext()
	{
		return channelContext;
	}

	public void setChannelContext(ChannelHandlerContext channelContext) 
	{
		this.channelContext = channelContext;
	}

	private void initParam() throws Exception
	{
		try 
		{	
			if(frame instanceof TextWebSocketFrame)
			{
				TextWebSocketFrame txtFrame = (TextWebSocketFrame)frame;
				this.postData = txtFrame.content().toString(charset);
				PostDataParser parser = PostDataParserFactory.getInstance(parserType);
	            if(null != parser)
	            {
		            	Map<String, String> postDataMap = parser.parse(postData);
		            	if(null != postDataMap)
		            		paramMap.putAll(postDataMap);
	            }
			}
		}
		catch (Exception e)
		{
			throw new Exception("web server parser data throw an error. message:" + e.getMessage());
		}
	}
}