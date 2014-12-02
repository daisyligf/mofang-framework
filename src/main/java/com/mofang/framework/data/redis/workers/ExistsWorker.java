package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class ExistsWorker implements RedisWorker<Boolean>
{
	private String key;
	
	public ExistsWorker(String key)
	{
		this.key = key;
	}
	
	@Override
	public Boolean execute(Jedis jedis) throws Exception
	{
		return jedis.exists(key);
	}
}