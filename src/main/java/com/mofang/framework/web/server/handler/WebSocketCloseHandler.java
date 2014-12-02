package com.mofang.framework.web.server.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author zhaodx
 *
 */
public interface WebSocketCloseHandler
{
	void handle(ChannelHandlerContext context);
}