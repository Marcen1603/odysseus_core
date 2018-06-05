package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.wsenrich.logicaloperator.WSEnrichAO;

public class HttpPostConnection extends AbstractConnectionForWebservices {

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
	 * Constructor for the Http Post Connecton with arguments Connects
	 * automatically to the given Url
	 *
	 * @param url
	 *            the url for the request
	 * @param arguments
	 *            the arguments which will be added in the Http Body
	 */
	public HttpPostConnection() {
		// Needed for ConnectionForWebservicesRegistry
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
	public void connect(String charset, String method, String contentType) throws ClientProtocolException, IOException {
		this.httpClient = new DefaultHttpClient();
		this.httpClient.getParams().setParameter("http.connection.stalecheck", true);
		this.httpPost = new HttpPost(url);
		this.httpPost.addHeader(CONTENT_ENCODING, charset);
		if (contentType != null && contentType.length() > 0) {
			this.httpPost.addHeader(CONTENT_TYPE, contentType);
		} else {
			if (method.equals(WSEnrichAO.POST_WITH_ARGUMENTS)) {
				this.httpPost.addHeader(CONTENT_TYPE, TEXT_CONTENT);
			} else if (method.equals(WSEnrichAO.POST_WITH_DOCUMENT)) {
				this.httpPost.addHeader(CONTENT_TYPE, XML_CONTENT);
			}
		}
		for (Option o : getHeader()) {
			this.httpPost.addHeader(o.getName(), o.getValue());
		}
		this.httpPost.setEntity(new StringEntity(argument));
		if (logger.isTraceEnabled()) {
			logger.trace(httpPost.toString());
			for (Header h : httpPost.getAllHeaders())
				logger.trace(h + "");
		}
		this.response = this.httpClient.execute(httpPost);

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
	public String getName() {
		return "POST";
	}

	@Override
	public HttpPostConnection createInstance() {
		return new HttpPostConnection();
	}
}
