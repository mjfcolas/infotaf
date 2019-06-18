package com.googleToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotaf.common.exceptions.BusinessException;
import com.infotaf.restapi.config.AppConfig;
import com.infotaf.taffilemanager.TafFileManager;

/**
 * Classe pour mettre à jour le token d'accès à l'API google
 * Inspiré de http://chariotsolutions.com/blog/post/sending-mail-via-gmail-javamail/
 * @author emmanuel
 *
 */
public class GoogleToken{

	private static final Logger logger = LoggerFactory.getLogger(TafFileManager.class);
	private static String TOKEN_URL = "https://www.googleapis.com/oauth2/v4/token";
	
	private String oauthClientId;
	private String oauthSecret;
	private String refreshToken;
	private String accessToken = null;
	private long tokenExpires = 0;
	
	public GoogleToken(String oauthClientId, String oauthSecret, String refreshToken){
		this.setOauthClientId(oauthClientId);
		this.setOauthSecret(oauthSecret);
		this.setRefreshToken(refreshToken);
	}
	
	public GoogleToken(){
		String oauthClientId = AppConfig.mailProp.getProperty("mail.oautch.clientid");
		String oauthSecret = AppConfig.mailProp.getProperty("mail.oauth.secret");
		String refreshToken = AppConfig.mailProp.getProperty("mail.oauth.refreshtoken");
		this.setOauthClientId(oauthClientId);
		this.setOauthSecret(oauthSecret);
		this.setRefreshToken(refreshToken);
	}
	
	public String getAccessToken() throws BusinessException {
	    if(System.currentTimeMillis() > tokenExpires) {
	    	//Réinitialisation du token
	    	accessToken = null;
	        try {
	        	//Création de la requète pour récupérer le token
	            String request = "client_id="+URLEncoder.encode(oauthClientId, "UTF-8")
	                    +"&client_secret="+URLEncoder.encode(oauthSecret, "UTF-8")
	                    +"&refresh_token="+URLEncoder.encode(refreshToken, "UTF-8")
	                    +"&grant_type=refresh_token";
	            HttpURLConnection conn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
	            conn.setDoOutput(true);
	            conn.setRequestMethod("POST");
	            PrintWriter out = new PrintWriter(conn.getOutputStream());
	            out.print(request); // note: println causes error
	            out.flush();
	            out.close();
	            logger.debug(request);
	            conn.connect();
	            try {
	                HashMap<String,Object> result;
	                result = new ObjectMapper().readValue(conn.getInputStream(), new TypeReference<HashMap<String,Object>>() {});
	                accessToken = (String) result.get("access_token");
	                tokenExpires = System.currentTimeMillis()+(((Number)result.get("expires_in")).intValue()*1000);
	            } catch (IOException e) {
	                String line;
	                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	                while((line = in.readLine()) != null) {
	                    logger.error(line);
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    if(accessToken == null || accessToken.equals("")){
	    	throw new BusinessException("Impossible de déterminer le token");
	    }
	    return accessToken;
	}

	public String getOauthClientId() {
		return oauthClientId;
	}

	public void setOauthClientId(String oauthClientId) {
		this.oauthClientId = oauthClientId;
	}

	public String getOauthSecret() {
		return oauthSecret;
	}

	public void setOauthSecret(String oauthSecret) {
		this.oauthSecret = oauthSecret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
