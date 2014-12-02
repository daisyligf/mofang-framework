package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class SetWorker implements RedisWorker<Boolean>
{
	private String key;
	private String value;
	
	public SetWorker(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public Boolean execute(Jedis jedis) throws Exception
	{
		jedis.set(key, value);
		return true;
	}
}