package com.mofang.framework.web.server.conf;

/**
 * 
 * @author zhaodx
 *
 */
public class ChannelConfig
{
	private int backlog = 102400;
	private int soTimeout = 30000;
	private int connTimeout = 10000;
	private boolean reuseAddr = true;
	private boolean keepAlive = true;
	
	public int getBacklog()
	{
		return backlog;
	}
	
	public void setBacklog(int backlog)
	{
		this.backlog = backlog;
	}
	
	public int getSoTimeout() 
	{
		return soTimeout;
	}
	
	public void setSoTimeout(int soTimeout)
	{
		this.soTimeout = soTimeout;
	}
	
	public int getConnTimeout() 
	{
		return connTimeout;
	}
	
	public void setConnTimeout(int connTimeout)
	{
		this.connTimeout = connTimeout;
	}
	
	public boolean isReuseAddr()
	{
		return reuseAddr;
	}
	
	public void setReuseAddr(boolean reuseAddr) 
	{
		this.reuseAddr = reuseAddr;
	}
	
	public boolean isKeepAlive()
	{
		return keepAlive;
	}
	
	public void setKeepAlive(boolean keepAlive)
	{
		this.keepAlive = keepAlive;
	}
}