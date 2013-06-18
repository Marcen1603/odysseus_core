package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpGetConnection implements IConnectionForWebservices {
	
	/**
	 * The Uri for the Http-Request
	 */
	private String uri;
	
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
	 * Constructor connects automatically to the
	 * given Uri 
	 * @param uri the uri for the request
	 */
	public HttpGetConnection() {
		//Needed for the ConnectionForWebserviceRegistery
	}
	
	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Override
	public void setArgument(Object value) {

	}
	
	@Override
	public Object getArgument() {
		return null;
	}
	
	@Override
	public void connect() {
		
		try {
			this.httpClient = new DefaultHttpClient();
			this.httpGet = new HttpGet(uri);
			this.response = this.httpClient.execute(httpGet);
			
		} catch (ClientProtocolException e) {
			
			// TODO 
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
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
		
		return this.uri;
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
