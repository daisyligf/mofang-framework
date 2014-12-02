package com.mofang.framework.web.server.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;

import com.mofang.framework.web.server.action.ActionResolve;
import com.mofang.framework.web.server.main.WebServerType;
import com.mofang.framework.web.server.reactor.parse.PostDataParserType;

/**
 * 
 * @author zhaodx
 *
 */
public class ServerHandlerFactory
{
	public static ChannelInboundHandlerAdapter getInstance(WebServerType serverType, PostDataParserType parserType, ActionResolve actionResolve)
	{
		ChannelInboundHandlerAdapter handler = null;
		switch(serverType)
		{
			case Http:
				handler = new HttpServerHandler(parserType, actionResolve);
				break;
			case WebSocket:
				handler = new WebSocketServerHandler(parserType, actionResolve);
				break;
		}
		return handler;
	}
}