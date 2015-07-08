package de.uniol.inf.is.odysseus.wsenrich.util.interfaces;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;

public interface IRequestBuilder {
	
	/**
	 * @return the static part of the url before arguments
	 */
	public String getUrlPrefix();
	
	/**
	 * Setter for the static part of the url before arguments
	 * @param urlPrefix the urlprefix
	 */
	public void setUrlPrefix(String urlPrefix);
	
	/**
	 * @return the static part of the url after arguments
	 */
	public String getUrlSuffix();
	
	/**
	 * Setter for the static part of the url after arguments
	 * @param urlSuffix the urlsuffix
	 */
	public void setUrlSuffix(String urlSuffix);
	
	/**
	 * @return the Arguments
	 */
	public List<Option> getArguments();
	
	/**
	 * Setter for the arguments
	 * @param arguments the list of key-value-pairs which will be written in the http-body
	 */
	public void setArguments(List<Option> arguments);
	
	/**
	 * @return The Post-Data as a String representation
	 */
	public String getPostData();
	
	/**
	 * Setter for the Post data
	 * @param doc the post data e.g an xml-doc which will be written in the http-body
	 */
	public void setPostData(String doc);
	
	/**
	 * builds the url for the given parameters
	 */
	public void buildUri();
	
	/**
	 * @return The builded Uri for the given Parameters
	 */
	public String getUri();
	
	/**
	 * @return The name of the Uri Builder
	 */
	public String getName();
	
	/**
	 * Creates a instance of a class who implements this class
	 */
	public IRequestBuilder createInstance();
}
