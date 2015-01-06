package com.mofang.framework.net.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author zhaodx
 *
 */
public class HttpClientSender
{
	public static String get(CloseableHttpClient client, String url) throws Exception
	{
		CloseableHttpResponse response = null;
		HttpGet action = null;
		try
		{
			action = buildHttpGet(url);
			if(null == action)
				return null;
			response = client.execute(action, new BasicHttpContext());
			HttpEntity entity = response.getEntity();
            if (null == entity)
            	return null;
            
            byte[] bytes = EntityUtils.toByteArray(entity);
            return new String(bytes);
		}
		catch(Exception e)
		{
			action.abort();
			return null;
		}
		finally
		{
			if(null != response)
			{
				try
				{
					response.close();
				}
				catch (IOException e) 
				{}
			}
		}
	}
	
	public static String post(CloseableHttpClient client, String url, String postData) throws Exception
	{
		CloseableHttpResponse response = null;
		HttpPost action = null;
		try
		{
			action = buildHttpPost(url, postData);
			if(null == action)
				return null;

			response = client.execute(action, new BasicHttpContext());
			HttpEntity entity = response.getEntity();
            if (null == entity)
            	return null;
            
            byte[] bytes = EntityUtils.toByteArray(entity);
            return new String(bytes);
		}
		catch(Exception e)
		{
			action.abort();
			return null;
		}
		finally
		{
			if(null != response)
			{
				try
				{
					response.close();
				}
				catch (IOException e) 
				{}
			}
		}
	}
	
	public static String post(CloseableHttpClient client, String url, String postData, String contentType) throws Exception
	{
		CloseableHttpResponse response = null;
		HttpPost action = null;
		try
		{
			action = buildHttpPost(url, postData, contentType);
			if(null == action)
				return null;

			response = client.execute(action, new BasicHttpContext());
			HttpEntity entity = response.getEntity();
            if (null == entity)
            	return null;
            
            byte[] bytes = EntityUtils.toByteArray(entity);
            return new String(bytes);
		}
		catch(Exception e)
		{
			action.abort();
			return null;
		}
		finally
		{
			if(null != response)
			{
				try
				{
					response.close();
				}
				catch (IOException e) 
				{}
			}
		}
	}
	
	private static HttpGet buildHttpGet(String url)
	{
		try
		{
			HttpGet get = new HttpGet(url);
			HttpVersion version = HttpVersion.HTTP_1_1;
			ProtocolVersion protocolVersion = new ProtocolVersion(version.getProtocol(), version.getMajor(), version.getMinor());
			get.setProtocolVersion(protocolVersion);
			get.addHeader(HTTP.CONN_KEEP_ALIVE, "true");
			return get;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	private static HttpPost buildHttpPost(String url, String postData)
	{
		try
		{
			HttpPost post = new HttpPost(url);
			HttpVersion version = HttpVersion.HTTP_1_1;
			ProtocolVersion protocolVersion = new ProtocolVersion(version.getProtocol(), version.getMajor(), version.getMinor());
			post.setProtocolVersion(protocolVersion);
			post.addHeader(HTTP.CONN_KEEP_ALIVE, "true");
			HttpEntity entity = new StringEntity(postData, "UTF-8");
			post.setEntity(entity);
			return post;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	private static HttpPost buildHttpPost(String url, String postData, String contentType)
	{
		try
		{
			HttpPost post = new HttpPost(url);
			HttpVersion version = HttpVersion.HTTP_1_1;
			ProtocolVersion protocolVersion = new ProtocolVersion(version.getProtocol(), version.getMajor(), version.getMinor());
			post.setProtocolVersion(protocolVersion);
			post.addHeader(HTTP.CONN_KEEP_ALIVE, "true");
			post.addHeader(HTTP.CONTENT_TYPE, contentType);
			HttpEntity entity = new StringEntity(postData, "UTF-8");
			post.setEntity(entity);
			return post;
		}
		catch(Exception e)
		{
			return null;
		}
	}
}