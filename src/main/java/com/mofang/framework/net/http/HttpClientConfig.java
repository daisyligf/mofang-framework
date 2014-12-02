package com.mofang.framework.net.http;

/**
 * 
 * @author zhaodx
 *
 */
public class HttpClientConfig
{
	private int maxTotal = 100;
	private String host = "localhost";
	private int port = 80;
	private String charset = "utf-8";
	private int connTimeout = 30000;
	private int socketTimeout = 30000;
	private int defaultKeepAliveTimeout = 30000;
	private int checkIdleInitialDelay = 5000;
	private int checkIdlePeriod = 5000;
	private int closeIdleTimeout = 30000;
	
	public int getMaxTotal()
	{
		return maxTotal;
	}
	
	public void setMaxTotal(int maxTotal)
	{
		this.maxTotal = maxTotal;
	}
	
	public String getHost()
	{
		return host;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public String getCharset() 
	{
		return charset;
	}
	
	public void setCharset(String charset) 
	{
		this.charset = charset;
	}
	
	public int getConnTimeout()
	{
		return connTimeout;
	}
	
	public void setConnTimeout(int connTimeout)
	{
		this.connTimeout = connTimeout;
	}
	
	public int getSocketTimeout() 
	{
		return socketTimeout;
	}
	
	public void setSocketTimeout(int socketTimeout)
	{
		this.socketTimeout = socketTimeout;
	}
	
	public int getDefaultKeepAliveTimeout()
	{
		return defaultKeepAliveTimeout;
	}
	
	public void setDefaultKeepAliveTimeout(int defaultKeepAliveTimeout)
	{
		this.defaultKeepAliveTimeout = defaultKeepAliveTimeout;
	}

	public int getCheckIdleInitialDelay()
	{
		return checkIdleInitialDelay;
	}

	public void setCheckIdleInitialDelay(int checkIdleInitialDelay)
	{
		this.checkIdleInitialDelay = checkIdleInitialDelay;
	}

	public int getCheckIdlePeriod()
	{
		return checkIdlePeriod;
	}

	public void setCheckIdlePeriod(int checkIdlePeriod)
	{
		this.checkIdlePeriod = checkIdlePeriod;
	}

	public int getCloseIdleTimeout()
	{
		return closeIdleTimeout;
	}

	public void setCloseIdleTimeout(int closeIdleTimeout)
	{
		this.closeIdleTimeout = closeIdleTimeout;
	}
}