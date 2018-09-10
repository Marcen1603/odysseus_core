package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IRequestBuilder;

public class UriGetBuilder implements IRequestBuilder {

	/**
	 * Static variable for the Argumentdelmiter is "&"
	 */
	private static final char ARGUMENTDELMITER = '&';

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
	private List<Option> arguments;

	/**
	 * The resulting Url
	 */
	private StringBuffer uri;

	/**
	 * Default Constructor for the UriGetBuilder
	 */
	public UriGetBuilder() {
		this.url = "";
		this.uri = new StringBuffer();
	}

	/**
	 * Builds the static part of the uri before arguments
	 */
	private void buildUrlBeforeArguments() {
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
	 * Adds Parameter to the uri
	 */
	private void addParameters() {
		//Add the arguments
		if (!uri.toString().contains("?")){
			this.uri.append("?");
		}
		for(Option argument : this.arguments) {
			//replaces whitespaces if present
			String name = argument.getName().replace(BLANK, BLANKDELMITTER);
			String value = (""+argument.getValue()).replace(BLANK, BLANKDELMITTER);
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
		if(this.urlsuffix != null) {
			this.uri.append(this.urlsuffix);
		}
	}

	@Override
	public String getUri() {
		return this.uri.toString();
	}

	@Override
	public String getPostData() {
		return "";   //No Post Data in a Http Get
	}

	@Override
	public String getName() {
		return "GET";
	}

	@Override
	public UriGetBuilder createInstance() {
		return new UriGetBuilder();
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
		return this.urlsuffix;
	}

	@Override
	public void setUrlSuffix(String urlSuffix) {
		this.urlsuffix = urlSuffix;
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
		this.buildUrlBeforeArguments();
		this.addParameters();
		this.buildUrlAfterArguments();
	}

	@Override
	public void setPostData(String doc) {
		//Nothing to do
	}
}
