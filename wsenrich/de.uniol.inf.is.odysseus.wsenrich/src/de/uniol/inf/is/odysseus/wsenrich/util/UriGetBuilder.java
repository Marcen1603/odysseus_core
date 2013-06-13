package de.uniol.inf.is.odysseus.wsenrich.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;

public class UriGetBuilder implements IRequestBuilder {
	
	/**
	 * Static variable for the Argumentdelmiter is "&"
	 */
	private static final char ARGUMENTDELMITER = '&';
	
	/**
	 * Static variable for the Splitter between url and arguments is "?"
	 */
	private static final char URLDELMITER = '?';
	
	/**
	 * Static variable for the Splitting of Keys and Values is "="
	 */
	private static final char KEYVALUEDELMITER = '=';
	
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
	 * The static part of the Url before arguments
	 */
	private String url;
	
	/**
	 * The static part of the Url after arguments
	 */
	private String urlsuffix;
	
	/**
	 * The arguments as Key-Value-Pairs
	 */
	List<Option> arguments;
	
	/**
	 * The resulting Url
	 */
	private StringBuffer uri;
	
	
	public UriGetBuilder() {
		this.url = "";
		this.arguments = null;
		this.urlsuffix = "";
		this.uri = new StringBuffer();
		
	}
	
	/**
	 * Konstructor for building an Uri for the given Parameters
	 * @param url the static part of the url before arguments
	 * @param arguments die arguments as Key-Value-Parts
	 * @param urlsuffix the static part of the url after arguments,
	 * can be null or ""
	 * 
	 */
	public UriGetBuilder(String url, Object value, String urlsuffix ) {
		
		this.url = url;
		this.arguments = (List<Option>) arguments;
		this.urlsuffix = urlsuffix;
		this.uri = new StringBuffer();
		buildUrlBeforeArguments();
		addParameters();
		buildUrlAfterArguments();
				
	}
	
	/**
	 * Builds the static part of the uri before arguments
	 */
	private void buildUrlBeforeArguments() {
		
		//Adds the URL Suffix if its not present in url
		if(!this.url.contains(URLPREFIX)) {
			
			this.uri.append(URLPREFIX + this.url + URLDELMITER);
			
		} else {
			
			this.uri.append(this.url + URLDELMITER);
			
		}
	}
	
	/**
	 * Adds Parameter to the uri
	 */
	private void addParameters() {
		
		//Adds the arguments
		for(Option argument : arguments) {
				
			//replaces whitespaces if present
			String name = argument.getName().replace(BLANK, BLANKDELMITTER);
			String value = argument.getValue().replace(BLANK, BLANKDELMITTER);
				
			this.uri.append(name + KEYVALUEDELMITER
				+ value + ARGUMENTDELMITER);
				
				
		}
		//remove the last argumentdelmitter
		this.uri.deleteCharAt(this.uri.length()-1);
	}
	
	/**
	 * Adds the static part of the uri after arguments
	 */
	private void buildUrlAfterArguments() {
		
		//adds the urlsuffix if present
		if(this.urlsuffix != null || !this.urlsuffix.equals("")) {
			
			this.uri.append(this.urlsuffix);
		}
	}
	
	@Override
	public String getUri() {
		
		return this.uri.toString();
	}
	
	@Override
	public String getPostData() {
		return null;
	}

}
