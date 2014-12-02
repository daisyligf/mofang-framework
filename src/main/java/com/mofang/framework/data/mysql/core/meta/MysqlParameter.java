package com.mofang.framework.data.mysql.core.meta;

import java.util.List;

/**
 * SQL执行参数
 * @author zhaodx
 *
 */
public class MysqlParameter
{
	private String sql;
	private List<Object> params;
	
	public MysqlParameter(String sql, List<Object> params)
	{
		this.sql = sql;
		this.params = params;
	}
	
	public String getSql()
	{
		return this.sql;
	}
	
	public List<Object> getParams()
	{
		return this.params;
	}
}