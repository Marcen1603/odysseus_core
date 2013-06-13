package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;

public class UriPostBuilder implements IRequestBuilder {

	
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
	Object arguments;
	
	/**
	 * The Data for the Http-Post
	 */
	private StringBuffer postData;
	
	/**
	 * The resulting Url
	 */
	private StringBuffer uri;
	
	
	public UriPostBuilder() {
		this.url = "";
		this.postData = null;
		this.arguments = null;
		this.uri = null;
	}
	
	/**
	 * Constructor for building an Uri for the given Parameters
	 * @param url the static part of the url before arguments
	 * @param arguments die arguments as Key-Value-Parts
	 * @param urlsuffix the static part of the url after arguments,
	 * can be null or ""
	 * 
	 */
	public UriPostBuilder(String url, Object value, String urlsuffix ) {
		
		this.url = url;
		this.postData = new StringBuffer();
		this.arguments = value;
		this.uri = new StringBuffer();
		buildUrl();
		addParameters();

				
	}
	
	/**
	 * Builds the static part of the uri before arguments
	 */
	private void buildUrl() {
		
		//Adds the URL Suffix if its not present in url
		if(!this.url.contains(URLPREFIX)) {
			
			this.uri.append(URLPREFIX + this.url);
			
		} else {
			
			this.uri.append(this.url);
		}
	}
	
	/**
	 * Adds Parameter to the uri
	 */
	@SuppressWarnings("unchecked")
	private void addParameters() {
		
		if(this.arguments instanceof List) {
			
			List<Option> temp = (List<Option>) arguments;
			
			//Adds the arguments
			for(Option argument : temp) {
				
			//replaces whitespaces if present
			String name = argument.getName().replace(BLANK, BLANKDELMITTER);
			String value = argument.getValue().replace(BLANK, BLANKDELMITTER);
				
			this.postData.append(name + KEYVALUEDELMITER
					+ value + ARGUMENTDELMITER);
			
			}
			//remove the last argumentdelmitter
			this.uri.deleteCharAt(this.uri.length()-1);
							
		} else {
			this.postData.append(arguments);
		}
			
			
		
	}
	
	/**
	 * @return The builded Uri for the given Parameters
	 */
	public String getUri() {
		return this.uri.toString();
	}
	
	
	@Override
	public String getPostData() {
		return this.postData.toString();
	}

}

