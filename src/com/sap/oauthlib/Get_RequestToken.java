/*-------------------------------------------------------------------------\
| Title:  12Sprints Java code samples                                     |
| Author: Anthony Wood                                                     |
| Date:   08/03/2010                                                       |
|                                                                          |
| Gets a Request Token for a consumer application that's been registered   |
| on StreamWork. The OAUTH_CONSUMER_KEY and OAUTH_CONSUMER_SECRET          |  
| constants must be defined in OAuthHelper.java.                           |                                                                       
\-------------------------------------------------------------------------*/


package com.sap.oauthlib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.net.Proxy;

public class Get_RequestToken {
	
	private static DateFormat expiresFormat = new SimpleDateFormat("E, dd-MMM-yyyy k:m:s 'GMT'", Locale.US);
	public static String Run(OAuthHelper oauthHelper, boolean Use_proxy) throws Exception
    {
    	String RequestToken_URL = "";
		final String TITLE = "Get_RequestToken";    	
        //String result = TITLE + "\r\n" +   "----------------" + "\r\n";
		String result = "";
		HttpURLConnection HttpSessionToken = null;
        InputStream response = null;
               
        try
        {                                	
            RequestToken_URL += oauthHelper.getRequestTokenUrl();
        	
            if (Use_proxy){
            /*	ProxyHelper proxyHelper = new ProxyHelper();
            	SocketAddress address = new InetSocketAddress(proxyHelper.getProxyServer(), proxyHelper.getProxyPort());
                Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
                HttpSessionToken = (HttpURLConnection) new URL (RequestToken_URL).openConnection(proxy);
                HttpURLConnection.setFollowRedirects(true);
                HttpSessionToken.setRequestProperty("Proxy-Authorization", "Basic " + new sun.misc.BASE64Encoder().encode((proxyHelper.getProxyUsername() + ":" + 

proxyHelper.getProxyPassword()).getBytes("UTF-8")));*/
            }
            else{
            	HttpSessionToken = (HttpURLConnection) new URL (RequestToken_URL).openConnection();
            }
        	        	
            HttpSessionToken.setRequestMethod("POST");
            HttpSessionToken.setRequestProperty("Accept", "application/x-www-form-urlencoded");            
			response = (InputStream)HttpSessionToken.getContent();            
            //result += TITLE + " response: " + String.valueOf(HttpSessionToken.getResponseCode()) + "\r\n\r\n"; 
            
            String stringResponse = stringifyInputStream(response);
            
            String[] params = stringResponse.split("&");  
            Map<String, String> mapResponse = new HashMap<String, String>();
            for (String param : params)  
            {
            	String name = param.split("=")[0];
            	String value = param.split("=")[1];
            	mapResponse.put(name, value);  
            }
            
            System.out.println("The Request Token is " + mapResponse.get("oauth_token"));
            System.out.println("The Request Token Secret is " + mapResponse.get("oauth_token_secret"));
            oauthHelper.setOauthRequestSecret(mapResponse.get("oauth_token_secret"));
            oauthHelper.setOauthRequestToken(mapResponse.get("oauth_token"));
            
            //result += stringResponse + "\r\n";	
            //result += "Go authorize your app at https://beta.12sprints.com/oauth/authorize?oauth_token=" + mapResponse.get("oauth_token") + "\r\n";
            result = "https://beta.12sprints.com/oauth/authorize?oauth_token=" + mapResponse.get("oauth_token");
            System.out.println("result " + result); 
            response.close();

            /*result += "Logon cookies:" + "\r\n";							
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
	        }  */      		        
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
