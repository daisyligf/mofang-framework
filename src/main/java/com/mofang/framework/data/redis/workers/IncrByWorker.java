package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class IncrByWorker implements RedisWorker<Long>
{
	private String key;
	private Long increment;
	
	public IncrByWorker(String key, Long increment)
	{
		this.key = key;
		this.increment = increment;
	}

	@Override
	public Long execute(Jedis jedis) throws Exception
	{
		return jedis.incrBy(key, increment);
	}
}