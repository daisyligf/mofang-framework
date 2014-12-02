package com.mofang.framework.net.http;

import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * 
 * @author zhaodx
 *
 */
public class HttpClientProvider
{
	private PoolingHttpClientConnectionManager connManager;
	private HttpClientConfig config;
	
	public HttpClientProvider(HttpClientConfig config)
	{
		this.config = config;
		initConnectionManager();
	}
	
	private void initConnectionManager()
	{
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setCharset(Charset.forName(config.getCharset()))
				.build();
		
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(config.getMaxTotal());
		connManager.setDefaultMaxPerRoute(config.getMaxTotal());
		HttpHost host = new HttpHost(config.getHost(), config.getPort());
		connManager.setMaxPerRoute(new HttpRoute(host), config.getMaxTotal());
		connManager.setConnectionConfig(host, connectionConfig);
		
		checkIdle();
	}
	
	/**
	 * 定时清楚过期和闲置连接
	 */
	private void checkIdle()
	{
		Runnable r = new IdleConnectionMonitor(connManager);
		int initalDelay = config.getCheckIdleInitialDelay();
		int period = config.getCheckIdlePeriod();
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(r, initalDelay, period, TimeUnit.MILLISECONDS);
	}
	
	public CloseableHttpClient getHttpClient()
	{
		HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connManager);
		
		RequestConfig requestConfig = RequestConfig.custom()
	    	.setConnectTimeout(config.getConnTimeout())
	        .setSocketTimeout(config.getSocketTimeout())
	        .build();
		
		return clientBuilder
			.setKeepAliveStrategy(new KeepAliveStrategy())
			.setDefaultRequestConfig(requestConfig)
			.build();
	}
	
	/**
	 * 
	 * @author zhaodx
	 *
	 */
	private class KeepAliveStrategy implements ConnectionKeepAliveStrategy
	{
		@Override
		public long getKeepAliveDuration(HttpResponse response, HttpContext context)
		{
	        HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
	        while (it.hasNext()) 
	        {
	            HeaderElement he = it.nextElement();
	            String param = he.getName();
	            String value = he.getValue();
	            if (value != null && param.equalsIgnoreCase("timeout"))
	            {
	                try 
	                {
	                    return Long.parseLong(value) * 1000;
	                }
	                catch(NumberFormatException ignore) 
	                {
	                	return config.getDefaultKeepAliveTimeout();
	                }
	            }
	        }
	        return config.getDefaultKeepAliveTimeout();
		}
	}
	
	private class IdleConnectionMonitor implements Runnable
	{
		private PoolingHttpClientConnectionManager connectionManager;

        public IdleConnectionMonitor(PoolingHttpClientConnectionManager connectionManager)
        {
        	super();
            this.connectionManager = connectionManager;
        }

        @Override
        public void run() 
        {
            // Close expired connections
            connectionManager.closeExpiredConnections();
            // Optionally, close connections
            // that have been idle longer than 30 SECONDS
            connectionManager.closeIdleConnections(config.getCloseIdleTimeout(), TimeUnit.MILLISECONDS);
        }
    }
}
