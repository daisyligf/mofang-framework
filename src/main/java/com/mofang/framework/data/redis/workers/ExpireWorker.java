package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class ExpireWorker implements RedisWorker<Boolean>
{
	private String key;
	private int seconds;
	
	public ExpireWorker(String key, int seconds)
	{
		this.key = key;
		this.seconds = seconds;
	}
	
	@Override
	public Boolean execute(Jedis jedis) throws Exception
	{
		jedis.expire(key, seconds);
		return true;
	}
}