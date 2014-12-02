package com.mofang.framework.data.redis;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import redis.clients.jedis.JedisPool;

/**
 * 
 * @author zhaodx
 *
 */
public class RedisExecutor
{
	private final static ExecutorService Executor = Executors.newCachedThreadPool();
	private JedisPool pool;
	
	public void setJedisPool(JedisPool pool)
	{
		this.pool = pool;
	}
	
	public <T> T execute(RedisWorker<T> worker) throws Exception
	{
		return RedisHelper.execute(pool, worker);
	}
	
	public <T> Future<T> invokeExecute(RedisWorker<T> worker) throws Exception
	{
		Callable<T> task = new RedisInvokeExecute<T>(pool, worker);
		return Executor.submit(task);
	}
	
	class RedisInvokeExecute<T> implements Callable<T>
	{
		private JedisPool pool;
		private RedisWorker<T> worker;
		
		public RedisInvokeExecute(JedisPool pool, RedisWorker<T> worker)
		{
			this.pool = pool;
			this.worker = worker;
		}
		
		@Override
		public T call() throws Exception
		{
			return RedisHelper.execute(pool, worker);
		}
	}
}