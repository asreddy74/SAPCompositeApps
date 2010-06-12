/*-------------------------------------------------------------------------\
| Title:  12Sprints Java code samples                                     |
| Date:   08/03/2010                                                       |
|                                                                          |
| In this sample, the steps for configuring OAuth to work with OAuth are:  |  
| 1. Register you application at https://streamwork.com/oauth_clients      |
| 2. Run Get_RequestToken.java to get a request token from StreamWork       |
| 3. Authorize the application at                                          | 
|	https://streamwork.com/oauth/authorize?oauth_token=<request_token>,    |
|	where <request_token> is the key that is issued after Step 2.          |
| 4. Run Get_AccessToken.java to exchange the request token for an access  |
|	token.                                                                 |
| 5. Optionally, run Make_TestOauthRequest.java to make a Test Request to  |
|   StreamWork using OAuth.                                                |
|                                                                          |
| Once you've been issued an access token, all additional API calls to     |
| StreamWork must include the following parameters:                        |  
|   * oauth_consumer_key -- the key that is issued when you register the   |
|		application                                                        |
|   * oauth_token -- the access token that is issued in Step 4.            |
|	* oauth_signature_method -- must always be "PLAINTEXT"                 |
|	* oauth_signature -- the concatenation of the consumer secret that is  |
|	 	issued	when you register the application and the access and the   |
|	 	access token secret when you are issued the access token,          |
|	 	separated by an escaped ampersand (%26)                            |
|	* oauth_timestamp -- the time of the request, in UNIX time format      |
|	* oauth_nonce -- a random string                                       |
|	* oauth_version -- must always be "1.0"                                |
|                                                                          |                                                                          
\-------------------------------------------------------------------------*/

package com.sap.oauthlib;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;

public class OAuthHelper implements Serializable {

    private String oauthRequestTokenUrl = "";
	private String oauthConsumerKey = ""; //the key that's issued when a consumer application is registered
    private String oauthConsumerSecret = ""; //the secret that's issued when a consumer application is registered
    private String oauthRequestToken = ""; //the key that's issued when Get_RequestToken.java is run
    private String oauthRequestSecret = ""; //the secret that's issued when Get_RequestToken.java is run
    private String oauthVerifier = ""; //the verifier that's issued when a consumer application is authorized on StreamWork
    private String oauthAccessTokenUrl = "";
    private String oauthAccessToken = ""; //the key that's issued when Get_AccessToken.java is run
    private String oauthAccessTokenSecret = ""; //the secret that's issued when Get_AccessToken.java is run
    private String oauthTestRequestUrl = "";
    
    public String getOauthRequestTokenUrl() {
		return oauthRequestTokenUrl;
	}

	public void setOauthRequestTokenUrl(String oauthRequestTokenUrl) {
		this.oauthRequestTokenUrl = oauthRequestTokenUrl;
	}

	public String getOauthConsumerKey() {
		return oauthConsumerKey;
	}

	public void setOauthConsumerKey(String oauthConsumerKey) {
		this.oauthConsumerKey = oauthConsumerKey;
	}

	public String getOauthConsumerSecret() {
		return oauthConsumerSecret;
	}

	public void setOauthConsumerSecret(String oauthConsumerSecret) {
		this.oauthConsumerSecret = oauthConsumerSecret;
	}

	public String getOauthRequestToken() {
		return oauthRequestToken;
	}

	public void setOauthRequestToken(String oauthRequestToken) {
		this.oauthRequestToken = oauthRequestToken;
	}

	public String getOauthRequestSecret() {
		return oauthRequestSecret;
	}

	public void setOauthRequestSecret(String oauthRequestSecret) {
		this.oauthRequestSecret = oauthRequestSecret;
	}

	public String getOauthVerifier() {
		return oauthVerifier;
	}

	public void setOauthVerifier(String oauthVerifier) {
		this.oauthVerifier = oauthVerifier;
	}

	public String getOauthAccessTokenUrl() {
		return oauthAccessTokenUrl;
	}

	public void setOauthAccessTokenUrl(String oauthAccessTokenUrl) {
		this.oauthAccessTokenUrl = oauthAccessTokenUrl;
	}

	public String getOauthAccessToken() {
		return oauthAccessToken;
	}

	public void setOauthAccessToken(String oauthAccessToken) {
		this.oauthAccessToken = oauthAccessToken;
	}

	public String getOauthAccessTokenSecret() {
		return oauthAccessTokenSecret;
	}

	public void setOauthAccessTokenSecret(String oauthAccessTokenSecret) {
		this.oauthAccessTokenSecret = oauthAccessTokenSecret;
	}

	public String getOauthTestRequestUrl() {
		return oauthTestRequestUrl;
	}

	public void setOauthTestRequestUrl(String oauthTestRequestUrl) {
		this.oauthTestRequestUrl = oauthTestRequestUrl;
	}

	
     
    /* Gets the required OAuth parameters when making a call to StreamWork */
    public String getOauthParameters(){
    	 
    	 String params = "?oauth_consumer_key=" + oauthConsumerKey +
    	 			"&oauth_token=" + oauthAccessToken + 
    	 			"&oauth_signature_method=PLAINTEXT" +
    	 			"&oauth_signature=" + oauthConsumerSecret + "%26" + oauthAccessTokenSecret +
    	 			"&oauth_timestamp=" + System.currentTimeMillis()/1000 +
    	 			"&oauth_nonce=" + getNonce() + 
    	 			"&oauth_version=1.0";
    	 
    	 return params;
    	 
     }
    
     /* Gets the Url and OAuth parameters used when getting a Request Token on StreamWork */
     public String getRequestTokenUrl(){
    	 
    	String Url = oauthRequestTokenUrl + "?oauth_consumer_key=" + oauthConsumerKey +
					"&oauth_signature_method=PLAINTEXT" +
					"&oauth_signature=" + oauthConsumerSecret + "%26" +
					"&oauth_timestamp=" + System.currentTimeMillis()/1000 +
					"&oauth_nonce=" + getNonce() + 
					"&oauth_version=1.0" +
					"&oauth_callback=oob";
    	 
    	 return Url;
     }
     
     /* Gets the Url and OAuth parameters used when getting an Access Token on StreamWork */
     public String getAccessTokenUrl(){
    	 
    	 String Url = oauthAccessTokenUrl + "?oauth_consumer_key=" + oauthConsumerKey +
							"&oauth_token=" + oauthRequestToken + 
							"&oauth_signature_method=PLAINTEXT" +
				            "&oauth_signature=" + oauthConsumerSecret + "%26" + oauthRequestSecret +
				            "&oauth_timestamp=" + System.currentTimeMillis()/1000 +
				            "&oauth_nonce=" + getNonce() +
				            "&oauth_version=1.0" +
				            "&oauth_verifier=" + oauthVerifier;
    	 
    	 return Url;
     }
     
     /* Gets the Url and OAuth parameters used when making a Test Request on StreamWork */
     public String getTestRequestUrl(){
    	 
    	 String Url = oauthTestRequestUrl +  "?oauth_consumer_key=" + oauthConsumerKey +
							"&oauth_token=" + oauthAccessToken + 
							"&oauth_signature_method=PLAINTEXT" +
							"&oauth_signature=" + oauthConsumerSecret + "%26" + oauthAccessTokenSecret +
							"&oauth_timestamp=" + System.currentTimeMillis()/1000 +
							"&oauth_nonce=" + getNonce() + 
							"&oauth_version=1.0";
    	 
    	 return Url;
     }
  
     /* Gets a random string for the oauth_nonce parameter */
     private String getNonce(){
    	 SecureRandom random = new SecureRandom();
    	 return new BigInteger(130, random).toString(32);
     }
}
