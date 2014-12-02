package com.mofang.framework.data.redis.pool;

import redis.clients.jedis.JedisPool;

/**
 * 
 * @author zhaodx
 *
 */
public class RedisPoolProvider
{
	public static JedisPool getRedisPool(RedisPoolConfig config) throws Exception
	{
		try
		{
			return new JedisPool(config.getConfig(), config.getHost(), config.getPort(), config.getTimeout());
		}
		catch(Exception e)
		{
			throw e;
		}
	}
}