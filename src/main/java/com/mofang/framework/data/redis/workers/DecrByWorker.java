package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class DecrByWorker implements RedisWorker<Long>
{
	private String key;
	private Long decrement;
	
	public DecrByWorker(String key, Long decrement)
	{
		this.key = key;
		this.decrement = decrement;
	}
	
	@Override
	public Long execute(Jedis jedis) throws Exception
	{
		return jedis.decrBy(key, decrement);
	}
}