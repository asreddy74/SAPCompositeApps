/*-------------------------------------------------------------------------\
| Title:  12Sprints Java code samples                                     |
| Author: Anthony Wood                                                     |
| Date:   08/03/2010                                                       |
|                                                                          |
| Gets an Access Token for a consumer application that's been registered   |
| on StreamWork. The following constants must be defined in                |               
| OAuthHelper.java.                                                        |
|                                                                          |
|  * OAUTH_CONSUMER_KEY                                                    |
|  * OAUTH_REQUEST_TOKEN                                                   |
|  * OAUTH_CONSUMER_SECRET                                                 |
|  * OAUTH_REQUEST_SECRET                                                  |
|  * OAUTH_VERIFIER                                                        |
|                                                                          |                                                                       
\-------------------------------------------------------------------------*/

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

public class Get_AccessToken {
	
	private static DateFormat expiresFormat = new SimpleDateFormat("E, dd-MMM-yyyy k:m:s 'GMT'", Locale.US);
	public static String Run(OAuthHelper oauthHelper, boolean Use_proxy) throws Exception
	{
    	String AccessToken_URL = "";
		final String TITLE = "Get_AccessToken";    	
        //String result = TITLE + "\r\n" +   "----------------" + "\r\n";
		String result = "";
		HttpURLConnection HttpAccessToken = null;
        InputStream response = null;
        
        
        try
        {                                	
			AccessToken_URL += oauthHelper.getAccessTokenUrl();
        	
			if (Use_proxy){
			/*	ProxyHelper proxyHelper = new ProxyHelper();
            	SocketAddress address = new InetSocketAddress(proxyHelper.getProxyServer(), proxyHelper.getProxyPort());
                Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
                HttpAccessToken = (HttpURLConnection) new URL (AccessToken_URL).openConnection(proxy);
                HttpURLConnection.setFollowRedirects(true);
                HttpAccessToken.setRequestProperty("Proxy-Authorization", "Basic " + new sun.misc.BASE64Encoder().encode((proxyHelper.getProxyUsername() + ":" + 

proxyHelper.getProxyPassword()).getBytes("UTF-8")));*/
			}
			else{
				HttpAccessToken = (HttpURLConnection)new URL(AccessToken_URL).openConnection();	
			}
        	
			HttpAccessToken.setRequestMethod("POST"); ////This is actually a [POST] request, in spite of the class name
			HttpAccessToken.setDoOutput(true);			
			HttpAccessToken.setRequestProperty("Accept", "application/x-www-form-urlencoded");
			response = (InputStream)HttpAccessToken.getContent();            
            //result += TITLE + " response: " + String.valueOf(HttpAccessToken.getResponseCode()) + "\r\n\r\n";            				        
	        result = stringifyInputStream(response);           

			//result += "Logon cookies:" + "\r\n";							
			Map<String, List<String>> headers = HttpAccessToken.getHeaderFields();
            List<String> cookies = headers.get("Set-Cookie");            
            if (cookies == null)
	        {
	            //result += "None" + "\r\n";	            
	        } 
	        else 
	        {
	        	for (int i = 0; i < cookies.size(); i++) 
	            {	            	
	                if (!expired(cookies.get(i)))
	                {
	                	//result += "- " + getName(cookies.get(i)) + "\r\n";
	                }
	            }
	        }        		        
        	response.close();
        	return result;
        }
        catch (Exception Ex)
        {
            result = TITLE + " encountered an error: \r\n" + Ex.getMessage() + "\r\n";
			if (HttpAccessToken != null)
			{
			    response = HttpAccessToken.getErrorStream(); 
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
