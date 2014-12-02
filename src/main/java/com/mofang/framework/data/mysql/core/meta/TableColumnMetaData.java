package com.mofang.framework.data.mysql.core.meta;

/**
 * 
 * @author zhaodx
 *
 */
public class TableColumnMetaData
{
	private String fieldName;
	private Class<?> fieldType;
	private Object fieldValue;
	private String columnName;
	private String columnType;
	private boolean isPrimaryKey;
	private boolean isAutoIncrementKey;
	
	public String getFieldName()
	{
		return fieldName;
	}
	
	public void setFieldName(String fieldName) 
	{
		this.fieldName = fieldName;
	}
	
	public Class<?> getFieldType()
	{
		return fieldType;
	}
	
	public void setFieldType(Class<?> fieldType)
	{
		this.fieldType = fieldType;
	}
	
	public Object getFieldValue()
	{
		return fieldValue;
	}
	
	public void setFieldValue(Object fieldValue)
	{
		this.fieldValue = fieldValue;
	}
	
	public String getColumnName()
	{
		return columnName;
	}
	
	public void setColumnName(String columnName)
	{
		this.columnName = columnName;
	}
	
	public String getColumnType()
	{
		return columnType;
	}
	
	public void setColumnType(String columnType)
	{
		this.columnType = columnType;
	}
	
	public boolean isPrimaryKey()
	{
		return isPrimaryKey;
	}
	
	public void setPrimaryKey(boolean isPrimaryKey)
	{
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isAutoIncrementKey()
	{
		return isAutoIncrementKey;
	}

	public void setAutoIncrementKey(boolean isAutoIncrementKey)
	{
		this.isAutoIncrementKey = isAutoIncrementKey;
	}
}
