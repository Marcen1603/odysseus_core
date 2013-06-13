package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;

public class HttpPostConnection implements IConnectionForWebservices {
	
	/**
	 * The Url for the Http-Request
	 */
	private String url;
	
	/**
	 * The Post Data
	 */
	private Object argument;
	
	/**
	 * The client-side Http client
	 */
	private DefaultHttpClient httpClient;
	
	/**
	 * Http-Post Method
	 */
	private HttpPost httpPost;
	
	/**
	 * The Response of the Request
	 */
	private HttpResponse response;
	
	/**
	 * Constructor for the Http Post Connecton with arguments
	 * Connects automatically to the given Url
	 * @param url the url for the request
	 * @param arguments the arguments which will be added in the Http Body
	 */
	public HttpPostConnection() {
		//Needed for ConnectionForWebservicesRegistry
	}
	
	@Override
	public void setUri(String url) {
		this.url = url;
	}
	
	@Override
	public void setArgument(Object value) {
		this.argument = value;
	}
	
	@Override
	public Object getArgument() {
		return this.argument;
	}
		
		
	@Override
	public void connect() {
		
		if(this.argument instanceof List) {
			
			@SuppressWarnings("unchecked")
			List<Option> temp = (List<Option>) argument;
			connectWithArguments(castOptionsListToNameValuePair(temp));
		} else if(this.argument == null) {
			 throw new IllegalParameterException(
					"There are no arguments or XML-Part defined. " +
					"Set arguments or XML Dokument and try again.");
		} else {
			String document = (String) argument;
			connectWithDocument(document);
		} 

	}

	@Override
	public void closeConnection() {
		
		this.httpPost.releaseConnection();
	}

	@Override
	public String retrieveStatusLine() {
		
		return response.getStatusLine().toString();
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
		
		this.httpPost.addHeader(argument, value);

	}
	
	/**
	 * Cast the List<Option> from Odysseus to List<NameValuePair>
	 * to send it in the Http Post Body
	 * @param arguments the arguments to cast to an NameValuePair
	 * @return the NameValuePair List
	 */
	private List<NameValuePair> castOptionsListToNameValuePair(List<Option> arguments) {
		
		List<NameValuePair> castedList = new ArrayList<NameValuePair>();
		
		for(Option argument : arguments) {
			
			castedList.add(new BasicNameValuePair(argument.getName(), argument.getValue()));	
		}
		return castedList;
	}
	
	/**
	 * Opens the Http Post Connection with Arguments
	 * Arguments will be added in the Http Body
	 */
	private void connectWithArguments(List<NameValuePair> arguments) {
		
		try {
			this.httpClient = new DefaultHttpClient();
			this.httpPost = new HttpPost(url);
			this.httpPost.setEntity(new UrlEncodedFormEntity(arguments));			
			this.response = this.httpClient.execute(httpPost);
			
		} catch (ClientProtocolException e) {
			
			// TODO 
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Opens the Http Post Connection with a Document, 
	 * not Key-Value-Pairs
	 */
	private void connectWithDocument(String document) {
		
		try {
			this.httpClient = new DefaultHttpClient();
			this.httpPost = new HttpPost(url);
			this.httpPost.setEntity(new StringEntity(document));
			this.response = this.httpClient.execute(httpPost);
			
		} catch (ClientProtocolException e) {
			
			// TODO 
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String getName() {
		return "POST";
	}
	
	@Override
	public HttpPostConnection createInstance() {
		return new HttpPostConnection();
	}

}
