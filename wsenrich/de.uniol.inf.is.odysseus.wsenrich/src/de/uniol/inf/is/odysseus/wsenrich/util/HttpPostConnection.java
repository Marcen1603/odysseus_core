package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.wsenrich.logicaloperator.WSEnrichAO;

public class HttpPostConnection implements IConnectionForWebservices {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(HttpPostConnection.class);
	
	/**
	 * Static Variable for XML Content Type
	 */
	private static final String XML_CONTENT = "text/xml";

	/**
	 * Static Variable for Text Content Type
	 */
	private static final String TEXT_CONTENT = "application/x-www-form-urlencoded";
	
	/**
	 * Static Variable "Content-Type"
	 */
	private static final String CONTENT_TYPE = "Content-Type";
	
	/**
	 * Static Variable "Content-Encoding
	 */
	private static final String CONTENT_ENCODING = "Content-Encoding";
	
	/**
	 * The Url for the Http-Request
	 */
	private String url;
	
	/**
	 * The Post Data
	 */
	private String argument;
	
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
	public void setArguments(String value) {
		this.argument = value;
	}
	
	@Override
	public String getArguments() {
		return this.argument;
	}
		
	@Override
	public void connect(String charset, String contentType) {
		try {
			this.httpClient = new DefaultHttpClient();
			this.httpPost = new HttpPost(url);
			this.httpPost.addHeader(CONTENT_ENCODING, charset);
			if(contentType.equals(WSEnrichAO.POST_WITH_ARGUMENTS)) {
				this.httpPost.addHeader(CONTENT_TYPE, TEXT_CONTENT);
			} else if (contentType.equals(WSEnrichAO.POST_WITH_DOCUMENT)) {
				this.httpPost.addHeader(CONTENT_TYPE, XML_CONTENT);
			}
			this.httpPost.setEntity(new StringEntity(argument));	
			this.response = this.httpClient.execute(httpPost);
		} catch (IOException e) {
			logger.error("Error while connecting to the specified Url. Cause: {}", e.getMessage());
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
	
	@Override
	public String getName() {
		return "POST";
	}
	
	@Override
	public HttpPostConnection createInstance() {
		return new HttpPostConnection();
	}
}
