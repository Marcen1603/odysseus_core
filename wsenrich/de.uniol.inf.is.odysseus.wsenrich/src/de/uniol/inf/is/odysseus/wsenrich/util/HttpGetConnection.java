package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpGetConnection implements IConnectionForWebservices {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(HttpGetConnection.class);
	
	/**
	 * The Uri for the Http-Request
	 */
	private String url;
	
	/**
	 * The client-side Http client
	 */
	private DefaultHttpClient httpClient;
	
	/**
	 * The Http-Get-Method
	 */
	private HttpGet httpGet;
	
	/**
	 * The Response of the Request
	 */
	private HttpResponse response;
	
	/**
	 * Constructor for a Http Get Request. 
	 */
	public HttpGetConnection() {
		//Needed for the ConnectionForWebserviceRegistery
	}
	
	@Override
	public void setUri(String uri) {
		this.url = uri;
	}
	
	@Override
	public void setArgument(String value) {
		//Nothing to do
	}
	
	@Override
	public String getArgument() {
		return null;
	}
	
	@Override 
	public void connect(String charset, String contentType) {
		//Nothing to do with contentType ia a Http Get Connection
		try {
			this.httpClient = new DefaultHttpClient();
			this.httpGet = new HttpGet(url);
			this.httpGet.addHeader("Content-Encoding", charset);
			this.response = this.httpClient.execute(httpGet);	
		} catch (IOException e) {
			logger.error("Error while connecting to the specified Url. Cause: {}", e.getMessage());
		} 
	}
	
	@Override
	public void closeConnection() {
		this.httpGet.releaseConnection();
	}
	
	@Override
	public String retrieveStatusLine() {
		return this.response.getStatusLine().toString();
	}

	@Override
	public HttpEntity retrieveBody() {
		HttpEntity entity = this.response.getEntity();
		return entity;
	}

	@Override
	public String getUri() {
		return this.url;
	}

	@Override
	public void addHeader(String argument, String value) {
		this.httpGet.addHeader(argument, value);	
	}
	
	@Override
	public String getName() {
		return "GET";
	}
	
	@Override
	public HttpGetConnection createInstance() {
		return new HttpGetConnection();
	}

}
