package com.google.appengine.demos.oauth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

import com.sap.oauthlib.Get_RequestToken;
import com.sap.oauthlib.OAuthHelper;

@SuppressWarnings("serial")
public class OAuthSample1Servlet extends HttpServlet {
	 private static final String USER_INFO_CONTAINER_KEY = "userinfocontainer";
	 
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		final String CONSUMER_KEY = "g73JiUxHGBCOJ4qmNAK1";
    	final String CONSUMER_SECRET = "zO41fhEXNYdDNtRTcX7RnJS2xO5j3cHiIzZWSHgW";
    	final String REQUEST_TOKEN_URL = "https://streamwork.com/oauth/request_token";
    	final String ACCESS_TOKEN_URL = "https://streamwork.com/oauth/access_token";
    	
  
    	final Boolean USE_PROXY = false;
    	
        String s = "";
        OAuthHelper oAuthHelper = new OAuthHelper();
        
        if ( CONSUMER_KEY == null || CONSUMER_SECRET == null) {
        	System.out.println( " You need to add your key and secret to run this application");
        } else {
	        oAuthHelper.setOauthConsumerKey(CONSUMER_KEY);
	        oAuthHelper.setOauthConsumerSecret(CONSUMER_SECRET);
	        oAuthHelper.setOauthRequestTokenUrl(REQUEST_TOKEN_URL);
	        oAuthHelper.setOauthAccessTokenUrl(ACCESS_TOKEN_URL);
	        req.getSession().setAttribute(USER_INFO_CONTAINER_KEY, oAuthHelper);
	             
	        try
	        { 
	        s = Get_RequestToken.Run(oAuthHelper, false);
	        System.out.println(s);       
	        //Go and run the next sample.
	        //System.out.println("When you have authorized the application run the DesktopGetAccessToken.java sample file and use the variables above for the RequestToken and RequestTokenSecret\n\r");
	        String location = s;
	        
	        resp.setContentType("text/html");
	        PrintWriter out = resp.getWriter();
	        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	                                            "Transitional//EN\">\n" +
	                    "<HTML>\n" +
	                    "<HEAD><TITLE>Hello WWW</TITLE></HEAD>\n" +
	                    "<BODY>\n" +
	                    "<h>\n" + "oAuth Sample App - click link to trigger call" +
	                    "</h>\n" +
	                    "<A href=" + location + ">" + location + "</A>" +
	                    "</BODY></HTML>");

	        
	        /*ServletContext sc = getServletContext();
	        RequestDispatcher rd = sc.getRequestDispatcher(location);
	        rd.forward(req, resp);*/
	        
	        } catch (Exception Ex)
	        {
	        	Ex.printStackTrace();
	        }
	   }
	}
}
