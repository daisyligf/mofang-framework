package com.mofang.framework.data.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 
 * @author zhaodx
 *
 */
public class RedisHelper
{
	public static <T> T execute(JedisPool pool, RedisWorker<T> worker) throws Exception
	{
		if(null == pool)
			return null;
		
		Jedis jedis = null;
		try
		{
			jedis = pool.getResource();
			return worker.execute(jedis);
		}
		catch (Exception e)
		{
			///销毁jedis对象
			pool.returnBrokenResource(jedis);
			throw e;
		}
		finally
		{
			if(jedis != null)
				pool.returnResource(jedis);
		}
	}
}
