package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class DecrWorker implements RedisWorker<Long>
{
	private String key;
	
	public DecrWorker(String key)
	{
		this.key = key;
	}
	
	@Override
	public Long execute(Jedis jedis) throws Exception
	{
		return jedis.decr(key);
	}
}