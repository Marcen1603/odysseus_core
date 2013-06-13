package de.uniol.inf.is.odysseus.wsenrich.util;

public interface IRequestBuilder {
	
	/**
	 * @return The builded Uri for the given Parameters
	 */
	public String getUri();
	
	/**
	 * @return The Post-Data as a String representation
	 */
	public String getPostData();
	
}
