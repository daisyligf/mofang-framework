package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class DeleteWorker implements RedisWorker<Boolean>
{
	private String key;
	
	public DeleteWorker(String key)
	{
		this.key = key;
	}

	@Override
	public Boolean execute(Jedis jedis) throws Exception
	{
		jedis.del(key);
		return true;
	}
}