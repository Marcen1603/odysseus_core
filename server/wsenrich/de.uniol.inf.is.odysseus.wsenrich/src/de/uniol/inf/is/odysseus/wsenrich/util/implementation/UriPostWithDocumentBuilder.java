package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IRequestBuilder;

public class UriPostWithDocumentBuilder implements IRequestBuilder {

	/**
	 * Static variable for the Url-Prefix is "http://"
	 */
	private static final String URLPREFIX = "http://";
	
	/**
	 * The static part of the Url before arguments
	 */
	private String url;
	
	/**
	 * The Data for the Http-Post
	 */
	private StringBuffer postData;
	
	/**
	 * The resulting Url
	 */
	private StringBuffer uri;
	
	/**
	 * Default Constructor for the UriPostBuilder
	 */
	public UriPostWithDocumentBuilder() {
		this.url = "";
		this.uri = new StringBuffer();
		this.postData = new StringBuffer();
	}
	
	/**
	 * Builds the static part of the uri
	 */
	private void buildUrl() {
		if(!this.uri.equals("")) {
			this.uri.delete(0, this.uri.length());
		}
		//Adds the URL Suffix if its not present in url
		if(!this.url.contains(URLPREFIX)) {	
			this.uri.append(URLPREFIX + this.url);
		} else {
			this.uri.append(this.url);
		}
	}
	
	@Override
	public String getPostData() {
		return this.postData.toString();
	}
	
	@Override
	public void setPostData(String doc) {
		if(!this.postData.equals("")) {
			this.postData.delete(0, this.postData.length());
		}
		this.postData.append(doc); 
	}
	
	@Override
	public String getName() {
		return "POST_DOCUMENT";
	}
	
	@Override
	public UriPostWithDocumentBuilder createInstance() {
		return new UriPostWithDocumentBuilder();
	}

	@Override
	public String getUrlPrefix() {
		return this.url;
	}

	@Override
	public void setUrlPrefix(String urlPrefix) {
		this.url = urlPrefix;
		
	}

	@Override
	public String getUrlSuffix() {
		return "";  
	}

	@Override
	public void setUrlSuffix(String urlSuffix) {
		//Nothing to do
	}

	@Override
	public List<Option> getArguments() {
		return null;
	}
	
	@Override
	public void setArguments(List<Option> arguments) {
		//Nothing to do
	}

	@Override
	public void buildUri() {
		this.buildUrl();
	}
	
	@Override
	public String getUri() {
		return this.uri.toString();
	}
}

