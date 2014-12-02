package com.mofang.framework.data.redis.workers;

import redis.clients.jedis.Jedis;

import com.mofang.framework.data.redis.RedisWorker;

/**
 * 
 * @author zhaodx
 *
 */
public class GetWorker implements RedisWorker<String>
{
	private String key;
	
	public GetWorker(String key)
	{
		this.key = key;
	}
	
	@Override
	public String execute(Jedis jedis) throws Exception 
	{
		return jedis.get(key);
	}
}