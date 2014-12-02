package com.mofang.framework.data.mysql.core.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author zhaodx
 *
 */
public class ResultData
{
	private boolean executeResult = false;
	private Object autoIncrementResult;
	private List<RowData> queryResult = new ArrayList<RowData>();
	
	public boolean getExecuteResult() 
	{
		return executeResult;
	}

	public void setExecuteResult(boolean executeResult)
	{
		this.executeResult = executeResult;
	}

	public Object getAutoIncrementResult()
	{
		return autoIncrementResult;
	}

	public void setAutoIncrementResult(Object autoIncrementResult)
	{
		this.autoIncrementResult = autoIncrementResult;
	}

	public void addRowData(RowData entry)
	{
		queryResult.add(entry);
	}
	
	public List<RowData> getQueryResult()
	{
		return queryResult;
	}
}