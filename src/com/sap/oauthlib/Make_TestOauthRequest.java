package com.sap.oauthlib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Make_TestOauthRequest {
	
	private static DateFormat expiresFormat = new SimpleDateFormat("E, dd-MMM-yyyy k:m:s 'GMT'", Locale.US);
	public static String Run(OAuthHelper oauthHelper, boolean Use_proxy) throws Exception
    {
    	String TestRequest_Url = "";
		final String TITLE = "Make_TestOauthRequest";    	
        String result = TITLE + "\r\n" +   "----------------" + "\r\n";
        HttpURLConnection HttpSessionToken = null;
        InputStream response = null;
        
        try
        {   
        	TestRequest_Url = "https://streamwork.com/v1/activities";
			TestRequest_Url += oauthHelper.getTestRequestUrl();
        	        	
        	if (Use_proxy){
        	/*	ProxyHelper proxyHelper = new ProxyHelper();
            	SocketAddress address = new InetSocketAddress(proxyHelper.getProxyServer(), proxyHelper.getProxyPort());
                Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
                HttpSessionToken = (HttpURLConnection) new URL (TestRequest_Url).openConnection(proxy);
                HttpURLConnection.setFollowRedirects(true);
                HttpSessionToken.setRequestProperty("Proxy-Authorization", "Basic " + new sun.misc.BASE64Encoder().encode((proxyHelper.getProxyUsername() + ":" + 

proxyHelper.getProxyPassword()).getBytes("UTF-8")));*/
        	}
        	else{
        		HttpSessionToken = (HttpURLConnection)new URL(TestRequest_Url).openConnection();	
        	}
        	        													
			HttpSessionToken.setRequestMethod("GET"); 
			HttpSessionToken.setDoOutput(true);			
			HttpSessionToken.setRequestProperty("Accept", "application/xml");
			
			response = (InputStream)HttpSessionToken.getContent();            
            result += TITLE + " response: " + String.valueOf(HttpSessionToken.getResponseCode()) + "\r\n\r\n";            				        
	        result += stringifyInputStream(response) + "\r\n\r\n";            

			result += "Logon cookies:" + "\r\n";							
			Map<String, List<String>> headers = HttpSessionToken.getHeaderFields();
            List<String> cookies = headers.get("Set-Cookie");            
            if (cookies == null)
	        {
	            result += "None" + "\r\n";	            
	        } 
	        else 
	        {
	        	for (int i = 0; i < cookies.size(); i++) 
	            {	            	
	                if (!expired(cookies.get(i)))
	                {
	                	result += "- " + getName(cookies.get(i)) + "\r\n";
	                }
	            }
	        }        		        
        	response.close();
        	return result;
        }
        catch (Exception Ex)
        {
            result = TITLE + " encountered an error: \r\n" + Ex.getMessage() + "\r\n";
			if (HttpSessionToken != null)
			{
			    response = HttpSessionToken.getErrorStream(); 
				if (response != null)
				{
					result += stringifyInputStream(response);
				}
			}            
            return result;
        }
    }
    
    private static String getName(String cookie)
    {    	
    	return cookie.substring(0,cookie.indexOf('='));    	
    }
    private static boolean expired(String cookie) throws ParseException
    {
    	try
    	{    		
    		if (cookie.indexOf("expires=") != -1)
    		{    			    			
    			Date Expiry = expiresFormat.parse(cookie.substring(cookie.indexOf("expires=") + "expires=".length()));    			
    			return new Date().after(Expiry);
    		}
    		return false;
    	}
    	catch (ParseException Ex)
    	{
    		return false;
    	}    	
    	
    }
    public static String stringifyInputStream(InputStream inputStream) throws IOException 
    {
        int ichar;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(;;) {
        	ichar = inputStream.read();
        	if(ichar < 0) {
        		break;
        	}
        	baos.write(ichar);
        }
        
        return baos.toString("UTF-8");
    }
	
}
