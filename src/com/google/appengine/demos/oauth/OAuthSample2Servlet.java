package com.google.appengine.demos.oauth;

import java.io.IOException;
import javax.servlet.http.*;

import com.sap.oauthlib.Get_AccessToken;
import com.sap.oauthlib.Make_TestOauthRequest;
import com.sap.oauthlib.OAuthHelper;

@SuppressWarnings("serial")
public class OAuthSample2Servlet extends HttpServlet {
	
	 private static final String USER_INFO_CONTAINER_KEY = "userinfocontainer";

	 
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		 String oauth_token = req.getParameter("oauth_token");
		    String oauth_verifier = req.getParameter("oauth_verifier");
		    System.out.println("oauth_verifier " + oauth_verifier);
		    System.out.println("oauth_token " + oauth_token);
		resp.setContentType("text/plain");
		resp.getWriter().println("oauth_token " + oauth_token);
		
		OAuthHelper oAuthHelper1 = 
	        (OAuthHelper) req.getSession().getAttribute(USER_INFO_CONTAINER_KEY);
		oAuthHelper1.setOauthVerifier(oauth_verifier);
		resp.getWriter().println("oauth_secret key " + oAuthHelper1.getOauthAccessTokenSecret());
		resp.getWriter().println("oauth_ key " + oAuthHelper1.getOauthAccessToken());
     	
    	final Boolean USE_PROXY = false;
    	
        String s = "";
        String s1 = "";
        try
        { 
        s = Get_AccessToken.Run(oAuthHelper1, false);
        resp.getWriter().println("response get access token" + s);
        String delims = "[=&]+"; // so the delimiters are:  + - * / ^ space
        String[] tokens = s.split(delims);
        resp.getWriter().println("t1 " + tokens[0]);
        resp.getWriter().println("t2 " + tokens[1]);
        resp.getWriter().println("t3 " + tokens[2]);
        resp.getWriter().println("t4 " + tokens[3]);
        oAuthHelper1.setOauthAccessToken(tokens[1]);
        oAuthHelper1.setOauthAccessTokenSecret(tokens[3]);

        s1 = Make_TestOauthRequest.Run(oAuthHelper1, false);
        System.out.println(s);    
        resp.getWriter().println("response activity list" + s1);
        //Go and run the next sample.
        System.out.println("You now know how to make API calls with OAuth.");
        } catch (Exception Ex)
        {
        	Ex.printStackTrace();
        }
        
       /* OAuthHelper oAuthHelper = new OAuthHelper();
        
        if ( CONSUMER_KEY == null || CONSUMER_SECRET == null || ACCESS_TOKEN == null) {
        	System.out.println( " You need to add your key and secret to run this application");
        } else {
	        oAuthHelper.setOauthConsumerKey(CONSUMER_KEY);
	        oAuthHelper.setOauthConsumerSecret(CONSUMER_SECRET);
	        oAuthHelper.setOauthAccessToken(ACCESS_TOKEN);
	        oAuthHelper.setOauthAccessTokenSecret(ACCESS_TOKEN_SECRET);
	           
	        try
	        { 
	        s = Make_TestOauthRequest.Run(oAuthHelper1, false);
	        System.out.println(s);       
	        //Go and run the next sample.
	        System.out.println("You now know how to make API calls with OAuth.");
	        } catch (Exception Ex)
	        {
	        	Ex.printStackTrace();
	        }
	   }*/
	}
}
