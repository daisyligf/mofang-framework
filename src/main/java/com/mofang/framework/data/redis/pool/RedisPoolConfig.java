package com.mofang.framework.data.redis.pool;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author zhaodx
 *
 */
public class RedisPoolConfig 
{
	private String host;
	private int port;
	private int timeout;
	private JedisPoolConfig config;
	
	public String getHost() 
	{
		return host;
	}
	
	public void setHost(String host) 
	{
		this.host = host;
	}
	
	public int getPort() 
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public int getTimeout() 
	{
		return timeout;
	}
	
	public void setTimeout(int timeout) 
	{
		this.timeout = timeout;
	}
	
	public JedisPoolConfig getConfig() 
	{
		return config;
	}
	
	public void setConfig(JedisPoolConfig config) 
	{
		this.config = config;
	}
}
