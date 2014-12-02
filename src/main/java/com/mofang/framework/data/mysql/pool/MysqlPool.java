package com.mofang.framework.data.mysql.pool;

import java.sql.Connection;

/**
 * 
 * @author zhaodx
 *
 */
public interface MysqlPool
{
	public Connection getConnection() throws Exception;
}