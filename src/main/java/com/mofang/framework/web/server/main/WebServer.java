package com.mofang.framework.web.server.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Iterator;
import java.util.Map;

import com.mofang.framework.web.server.action.ActionResolve;
import com.mofang.framework.web.server.conf.ChannelConfig;
import com.mofang.framework.web.server.conf.IdleConfig;
import com.mofang.framework.web.server.logger.WebServerLogger;
import com.mofang.framework.web.server.reactor.parse.PostDataParserType;
import com.mofang.framework.web.server.reactor.proxy.ActionExecutor;
import com.mofang.framework.web.server.reactor.ActionDispatcher;
import com.mofang.framework.web.server.reactor.ActionLoader;
import com.mofang.framework.web.server.reactor.ActionLoaderFactory;

/**
 * 
 * @author zhaodx
 *
 */
public class WebServer 
{
	private int port;
	private String scanPackagePath;
	private PostDataParserType parserType;
	private Class<?> closeHandlerClass;
	private ActionResolve websocketActionResolve;
	private ActionResolve httpActionResolve;
	private WebServerLogger serverLogger;
	
	private ChannelConfig channelConfig;
	private IdleConfig idleConfig;
	
	public WebServer(int port, PostDataParserType parserType)
	{ 
		this.port = port;
		this.parserType = parserType;
	}
	
	public void setScanPackagePath(String scanPackagePath) 
	{
		this.scanPackagePath = scanPackagePath;
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

	public void setChannelConfig(ChannelConfig channelConfig)
	{
		this.channelConfig = channelConfig;
	}

	public void setIdleConfig(IdleConfig idleConfig)
	{
		this.idleConfig = idleConfig;
	}
	
	public void setWebServerLogger(WebServerLogger serverLogger)
	{
		this.serverLogger = serverLogger;
	}

	public void start() throws Exception
	{
		initActionMap();
		
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap server = new ServerBootstrap();
		server.group(bossGroup, workerGroup);
		server.channel(NioServerSocketChannel.class);
		///配置channel
		if(null != channelConfig)
		{
			server.option(ChannelOption.SO_BACKLOG, channelConfig.getBacklog());
			server.option(ChannelOption.SO_TIMEOUT, channelConfig.getSoTimeout());
			server.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, channelConfig.getConnTimeout());
			server.option(ChannelOption.SO_REUSEADDR,  channelConfig.isReuseAddr());
			server.option(ChannelOption.SO_KEEPALIVE, channelConfig.isKeepAlive());
		}
		WebServerInitializer initializer = new WebServerInitializer(parserType);
		initializer.setWebSocketCloseHandlerClass(closeHandlerClass);
		initializer.setWebSocketActionResolve(websocketActionResolve);
		initializer.setHttpActionResolve(httpActionResolve);
		initializer.setIdleConfig(idleConfig);
		initializer.setWebServerLogger(serverLogger);
		server.childHandler(initializer);
		
		Channel channel = null;
		ChannelFuture future = null;
		try
		{
			future = server.bind(port);
			future = future.sync();
			channel = future.channel();
			channel.closeFuture().sync();
		}
		finally
		{
			bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
		}
	}
	
	private void initActionMap() throws Exception
	{
		if(null == scanPackagePath || "".equals(scanPackagePath))
			throw new Exception("scanPackagePath can not be null.");
		
		ActionLoader loader = ActionLoaderFactory.getActionLoader();
		if(null == loader)
			throw new Exception("action load type dosen't set");
		
		ActionDispatcher dispatcher = ActionDispatcher.getInstance();
		Map<String, ActionExecutor> actionMap = loader.load(scanPackagePath);
		if(null == actionMap || actionMap.size() == 0)
			throw new Exception("action map can not be null.");
		
		Iterator<String> iterator = actionMap.keySet().iterator();
		String url;
		ActionExecutor executor;
		while(iterator.hasNext())
		{
			url = iterator.next();
			executor = actionMap.get(url);
			dispatcher.regiester(url, executor);
		}
	}
}