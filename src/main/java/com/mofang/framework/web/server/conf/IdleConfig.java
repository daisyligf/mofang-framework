package com.mofang.framework.web.server.conf;

/**
 * 
 * @author zhaodx
 *
 */
public class IdleConfig
{
	private long readIdleTime;
	private long writeIdleTime;
	private long allIdleTime;
	
	public long getReadIdleTime()
	{
		return readIdleTime;
	}
	
	public void setReadIdleTime(long readIdleTime)
	{
		this.readIdleTime = readIdleTime;
	}
	
	public long getWriteIdleTime() 
	{
		return writeIdleTime;
	}
	
	public void setWriteIdleTime(long writeIdleTime)
	{
		this.writeIdleTime = writeIdleTime;
	}
	
	public long getAllIdleTime()
	{
		return allIdleTime;
	}
	
	public void setAllIdleTime(long allIdleTime) 
	{
		this.allIdleTime = allIdleTime;
	}
}