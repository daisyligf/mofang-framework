package com.mofang.framework.data.mysql.core.meta;

import java.util.List;

/**
 * 
 * @author zhaodx
 *
 */
public class TableMetaData
{
	private String tableName;
	private String primaryKey;
	private String autoIncrementKey;
	private List<TableColumnMetaData> columns;
	
	public String getTableName()
	{
		return tableName;
	}
	
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
	
	public String getPrimaryKey()
	{
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) 
	{
		this.primaryKey = primaryKey;
	}

	public String getAutoIncrementKey() 
	{
		return autoIncrementKey;
	}

	public void setAutoIncrementKey(String autoIncrementKey) 
	{
		this.autoIncrementKey = autoIncrementKey;
	}

	public List<TableColumnMetaData> getColumns()
	{
		return columns;
	}
	
	public void setColumns(List<TableColumnMetaData> columns)
	{
		this.columns = columns;
	}
}