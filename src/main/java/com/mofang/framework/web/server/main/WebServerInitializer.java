package com.mofang.framework.web.server.main;

import java.util.concurrent.TimeUnit;

import com.mofang.framework.web.server.action.ActionResolve;
import com.mofang.framework.web.server.conf.IdleConfig;
import com.mofang.framework.web.server.handler.WebServerHandler;
import com.mofang.framework.web.server.logger.WebServerLogger;
import com.mofang.framework.web.server.reactor.parse.PostDataParserType;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author zhaodx
 *
 */
public class WebServerInitializer extends ChannelInitializer<SocketChannel>
{
	private PostDataParserType parserType;
	private Class<?> closeHandlerClass;
	private ActionResolve websocketActionResolve;
	private ActionResolve httpActionResolve;
	private IdleConfig idleConfig;
	private WebServerLogger serverLogger;
	
	public WebServerInitializer(PostDataParserType parserType)
	{
		this.parserType = parserType;
	}
	
	public void setWebSocketCloseHandlerClass(Class<?> handlerClass)
	{
		this.closeHandlerClass = handlerClass;
	}
	
	public void setWebSocketActionResolve(ActionResolve actionResolve)
	{
		this.websocketActionResolve = actionResolve;
	}
	
	public void setHttpActionResolve(ActionResolve actionResolve)
	{
		this.httpActionResolve = actionResolve;
	}

	public void setIdleConfig(IdleConfig idleConfig) 
	{
		this.idleConfig = idleConfig;
	}
	
	public void setWebServerLogger(WebServerLogger serverLogger)
	{
		this.serverLogger = serverLogger;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("codec", new HttpServerCodec());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		if(null != idleConfig)
		{
			long readIdleTime = idleConfig.getReadIdleTime();
			long writeIdleTime = idleConfig.getWriteIdleTime();
			long allIdleTime = idleConfig.getAllIdleTime();
			IdleStateHandler idleStateHandler = new IdleStateHandler(readIdleTime, writeIdleTime, allIdleTime, TimeUnit.MILLISECONDS);
			pipeline.addLast("idleState", idleStateHandler);
		}
		
		ChannelInboundHandlerAdapter handler = new WebServerHandler(parserType);
		if(null != closeHandlerClass)
			((WebServerHandler)handler).setWebSocketCloseHandlerClass(closeHandlerClass);
		if(null != websocketActionResolve)
			((WebServerHandler)handler).setWebSocketActionResolve(websocketActionResolve);
		if(null != httpActionResolve)
			((WebServerHandler)handler).setHttpActionResolve(httpActionResolve);
		if(null != serverLogger)
			((WebServerHandler)handler).setWebServerLogger(serverLogger);
		pipeline.addLast("handler", handler);
	}
}