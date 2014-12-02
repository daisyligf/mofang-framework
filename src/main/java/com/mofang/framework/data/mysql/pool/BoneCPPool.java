package com.mofang.framework.data.mysql.pool;

import java.sql.Connection;

import com.jolbox.bonecp.BoneCP;

/**
 * 
 * @author zhaodx
 *
 */
public class BoneCPPool implements MysqlPool
{
	private BoneCP pool;
	
	public BoneCPPool(BoneCP pool)
	{
		this.pool = pool;
	}
	
	@Override
	public Connection getConnection() throws Exception
	{
		return pool.getConnection();
	}
}