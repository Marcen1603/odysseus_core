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
	 * XML-Part of the Http-Post-Request
	 */
	private String xmlPart;
	
	/**
	 * Arguments as argument&value
	 */
	private List<NameValuePair> arguments;
	
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
	public HttpPostConnection(String url, List<Option> arguments) {
		
		this.url = url;
		this.xmlPart = null;
		this.arguments = castOptionsListToNameValuePair(arguments);
		this.httpClient = new DefaultHttpClient();
		this.httpPost = new HttpPost(url);
		this.connect();
		
	}
	
	/**
	 * Constructor for the Http Post Connection with a Document, for
	 * example a XML Document.
	 * Connects automatically to the given Url
	 * @param url the url for the request
	 * @param document the document which will be added in the Http Body
	 */
	public HttpPostConnection(String url, String document) {
		
		this.url = url;
		this.xmlPart = document;
		this.arguments = null;
		this.httpClient = new DefaultHttpClient();
		this.httpPost = new HttpPost(url);
		this.connect();
	}
	
	
	
	@Override
	public void connect() {
		
		if(this.xmlPart == null) {
			connectWithArguments();	
			
		} else if (this.arguments == null) {
			connectWithDocument();
			
		} else throw new IllegalParameterException(
				"There are no arguments or XML-Part defined. " +
				"Set arguments or XML Dokument and try again.");

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
	private void connectWithArguments() {
		
		try {
			
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
	
	//TODO: Eventuell mit Interface arbeiten, damit nicht nur XML übergeben werden kann???
	/**
	 * Opens the Http Post Connection with a Document, 
	 * not Key-Value-Pairs
	 */
	private void connectWithDocument() {
		
		try {
			
			this.httpPost.setEntity(new StringEntity(xmlPart));
			this.response = this.httpClient.execute(httpPost);
			
		} catch (ClientProtocolException e) {
			
			// TODO 
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		}
		
	}

}
