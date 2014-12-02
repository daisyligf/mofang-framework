package com.mofang.framework.data.mysql.core.criterion.operand;

import com.mofang.framework.data.mysql.core.criterion.type.SortType;

/**
 * 
 * @author zhaodx
 *
 */
public class OrderByEntry
{
	private String columnName;
	private SortType sortType;
	
	public OrderByEntry(String columnName, SortType sortType)
	{
		this.columnName = columnName;
		this.sortType = sortType;
	}
	
	public String getColumnName()
	{
		return columnName;
	}
	
	public SortType getSortType()
	{
		return sortType;
	}
}