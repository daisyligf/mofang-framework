package com.mofang.framework.data.mysql.core.meta;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author zhaodx
 *
 */
public class RowData
{
	private SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Object[] items;
	private int count;
	private int idx = 0;
	
	public RowData(int count)
	{
		this.count = count;
		this.items = new Object[count];
	}
	
	public void add(Object item) throws Exception
	{
		if(idx >= count)
			throw new Exception("out of entry range");
		
		items[idx++] = item;
	}
	
	public Object get(int index) throws Exception
	{
		checkBound(index);
		return items[index];
	}
	
	public String getString(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : items[index].toString();
	}
	
	public Integer getInteger(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : (Integer)items[index];
	}
	
	public Long getLong(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : (Long)items[index];
	}
	
	public Double getDouble(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : (Double)items[index];
	}
	
	public Boolean getBoolean(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : (Boolean)items[index];
	}
	
	public Float getFloat(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : (Float)items[index];
	}
	
	public Date getDate(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : DateFormat.parse(items[index].toString());
	}
	
	public Byte getByte(int index) throws Exception
	{	
		checkBound(index);
		return items[index] == null ? null : Byte.valueOf(items[index].toString());
	}
	
	public Short getShort(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : Short.valueOf(items[index].toString());
	}
	
	public Character getChar(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : Character.valueOf(items[index].toString().charAt(0));
	}
	
	public byte[] getBytes(int index) throws Exception
	{
		checkBound(index);
		return items[index] == null ? null : ((byte[])items[index]);
	}
	
	private void checkBound(int index) throws Exception
	{
		if(index < 0 || index >= count)
			throw new Exception("out of entry range");
	}
}