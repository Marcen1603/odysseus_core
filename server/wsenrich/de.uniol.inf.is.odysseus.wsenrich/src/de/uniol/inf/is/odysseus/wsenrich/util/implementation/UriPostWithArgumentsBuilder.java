package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IRequestBuilder;

public class UriPostWithArgumentsBuilder implements IRequestBuilder {
	
	/**
	 * Static variable for the Url-Prefix is "http://"
	 */
	private static final String URLPREFIX = "http://";
	
	/**
	 * Static variable for replacing Spaces is "+"
	 */
	private static final char BLANKDELMITTER = '+';
	
	/**
	 * Static variable for Spaces
	 */
	private static final char BLANK = ' ';
	
	/**
	 * Static variable for the Splitting of Keys and Values is "="
	 */
	private static final char KEYVALUEDELMITER = '=';
	
	/**
	 * Static variable for the Argumentdelmiter is "&"
	 */
	private static final char ARGUMENTDELMITER = '&';
	
	/**
	 * The static part of the Url before arguments
	 */
	private String url;
	
	/**
	 * The arguments as Key-Value-Pairs
	 */
	List<Option> arguments;
	
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
	public UriPostWithArgumentsBuilder() {
		this.url = "";
		this.uri = new StringBuffer();
		this.postData = new StringBuffer();
	}
	
	/**
	 * Builds the static part of the url
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
	
	/**
	 * Adds Key Value Parameter to the Uri
	 */
	private void addParameters() {
		if(!this.postData.equals("")) {
			this.postData.delete(0, this.postData.length());
		}
		//Adds the arguments
		for(Option argument : this.arguments) {
		//replace whitespaces if present
		String name = argument.getName().replace(BLANK, BLANKDELMITTER);
		String value = (""+argument.getValue()).replace(BLANK, BLANKDELMITTER);	
		this.postData.append(name + KEYVALUEDELMITER
				+ value + ARGUMENTDELMITER);
		}
		this.postData.deleteCharAt(this.postData.length()-1);
	}	
	
	@Override
	public String getPostData() {
		return this.postData.toString();
	}
	
	@Override
	public void setPostData(String doc) {
		//Nothing to do
	}
	
	@Override
	public String getName() {
		return "POST_ARGUMENTS";
	}
	
	@Override
	public UriPostWithArgumentsBuilder createInstance() {
		return new UriPostWithArgumentsBuilder();
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
		return this.arguments;
	}
		
	@Override
	public void setArguments(List<Option> arguments) {
		this.arguments = arguments;
	}

	@Override
	public void buildUri() {
		this.buildUrl();
		this.addParameters();
	}
	
	@Override
	public String getUri() {
		return this.uri.toString();
	}
}

