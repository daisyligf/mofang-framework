package com.mofang.framework.net.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 
 * @author zhaodx
 *
 */
public class HttpRequester
{
    private String url;
    private String method = "GET";
    private String postData;
    private String charset = "utf-8";
    private String userAgent = HttpHeaderProperty.UserAgent;
    private String accept = HttpHeaderProperty.Accept;
    private String contentType = HttpHeaderProperty.ApplicationContextType;
    private String acceptCharset = HttpHeaderProperty.AcceptCharset;
    private String acceptEncoding = HttpHeaderProperty.AcceptEncoding;
    private String acceptLanguage = HttpHeaderProperty.AcceptLanguage;
    private String cacheControl = HttpHeaderProperty.CacheControl;
    private int connTimeout = 30000;
    private int readTimeout = 30000;
    private String cookie;
    private Proxy proxy;
    private Map<String, String> headers;
    
    public HttpRequester(String url)
    {
    	this.url = url;
    }
    
    public String getUrl()
    {
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getPostData()
	{
		return postData;
	}

	public void setPostData(String postData)
	{
		this.postData = postData;
	}

	public String getCharset()
	{
		return charset;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	public String getUserAgent() 
	{
		return userAgent;
	}

	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}

	public String getCookie()
	{
		return cookie;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	public String getAccept()
	{
		return accept;
	}

	public void setAccept(String accept)
	{
		this.accept = accept;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getAcceptCharset()
	{
		return acceptCharset;
	}

	public void setAcceptCharset(String acceptCharset) 
	{
		this.acceptCharset = acceptCharset;
	}

	public String getAcceptEncoding() 
	{
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding)
	{
		this.acceptEncoding = acceptEncoding;
	}

	public String getAcceptLanguage()
	{
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage)
	{
		this.acceptLanguage = acceptLanguage;
	}

	public String getCacheControl()
	{
		return cacheControl;
	}

	public void setCacheControl(String cacheControl)
	{
		this.cacheControl = cacheControl;
	}

	public int getConnTimeout()
	{
		return connTimeout;
	}

	public void setConnTimeout(int connTimeout) 
	{
		this.connTimeout = connTimeout;
	}

	public int getReadTimeout()
	{
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout)
	{
		this.readTimeout = readTimeout;
	}

	public Proxy getProxy()
	{
		return proxy;
	}

	public void setProxy(Proxy proxy) 
	{
		this.proxy = proxy;
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	public void setHeaders(Map<String, String> headers)
	{
		this.headers = headers;
	}
	
	public String execute(int retry)
    {
    	String htmlCont;
    	while (retry > 0) 
    	{
    		htmlCont = execute();
    		if(null != htmlCont)
    			return htmlCont;
    		
    		retry--;
			try 
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
    	
    	return null;
    }

	private String execute()
    {
    	HttpURLConnection conn = null;
    	InputStream is = null;
    	try 
    	{
    		HttpURLConnection.setFollowRedirects(true);
    		if(null != proxy)
    			conn = (HttpURLConnection)new URL(url).openConnection(proxy);
    		else
    			conn = (HttpURLConnection)new URL(url).openConnection();
    		conn.setDoInput(true);
    		conn.setDoOutput(true);
    		conn.setRequestMethod(method);
    		conn.setConnectTimeout(connTimeout);
    		conn.setReadTimeout(readTimeout);
    		conn.setUseCaches(false);
    		conn.setRequestProperty("User-Agent", userAgent);
    		conn.setRequestProperty("Accpet", accept);
    		conn.setRequestProperty("Accept-Charset", acceptCharset);
    		conn.setRequestProperty("Accept-Encoding", acceptEncoding);
    		conn.setRequestProperty("Accept-Language", acceptLanguage);
    		conn.setRequestProperty("Content-Type", contentType);
    		conn.setRequestProperty("Cache-Control", cacheControl);
    		
    		if(null != cookie && !"".equals(cookie))
    			conn.setRequestProperty("Cookie", cookie);
    		
    		if(null != headers && headers.size() > 0)
    		{
    			Iterator<String> iterator = headers.keySet().iterator();
    			String headerKey;
    			while(iterator.hasNext())
    			{
    				headerKey = iterator.next();
    				conn.setRequestProperty(headerKey, headers.get(headerKey));
    			}
    		}
    		
    		if(null != postData && !"".equals(postData))
    		{
	    		OutputStream ops = conn.getOutputStream(); 
	    		ops.write(postData.getBytes(charset));
	    		ops.close();
    		}
    		
    		conn.connect();
    		boolean isGzip = false;
    		String contentEncoding = conn.getHeaderField("Content-Encoding");
    		if(null != contentEncoding && contentEncoding.toLowerCase().equals("gzip"))
    			isGzip = true;
    		
    		is = conn.getInputStream();
    		if(isGzip)
    			return readFromGzipStream(is);
    		else
    			return readFromInputStream(is);
		} 
    	catch (Exception e) 
    	{
			return null;
		}
    	finally
    	{
    		if(conn != null)
    			conn.disconnect();
    	}
    }
	
	private String readFromGzipStream(InputStream is)
	{
		GZIPInputStream gzip = null;
		ByteArrayOutputStream baos = null;
		int receLen = 10240;
		int readLen = 0;
		byte[] buf = new byte[receLen];
		try
		{
			gzip = new GZIPInputStream(is);
			baos = new ByteArrayOutputStream();
			while ((readLen = gzip.read(buf)) != -1)
			{
				baos.write(buf, 0, readLen);
			}
			byte[] b = baos.toByteArray();
			baos.flush();
			return new String(b, charset);
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				if(null != baos)
					baos.close();
				if(null != gzip)
					gzip.close();
			}
			catch(Exception e)
			{}
		}
	}
	
	private String readFromInputStream(InputStream is)
	{
		int receLen = 10240;
		int readLen = 0;
		byte[] receBytes = new byte[receLen];
		byte[] readBytes;
		StringBuilder strText = new StringBuilder();
		try
		{
			while((readLen = is.read(receBytes, 0, receLen)) > 0)
			{
				readBytes = new byte[readLen];
				System.arraycopy(receBytes, 0, readBytes, 0, readLen);
				strText.append(new String(readBytes, "utf-8"));
			}

			return strText.toString();
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				if(null != is)
					is.close();
			}
			catch(Exception e)
			{}
		}
	}
}