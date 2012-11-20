package de.offis.client;

import java.io.Serializable;

/**
 * Scai Data Model for a Link.
 *
 * @author Alexander Funk
 * 
 */
public class ScaiLink implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6293617559914963249L;
	
	private String destinationName;
	private String sourceName;
	
	@SuppressWarnings("unused")
	private ScaiLink() {
		// Serializable
	}
	
	public ScaiLink(String sourceName, String destName) {
		this.destinationName = destName;
		this.sourceName = sourceName;
	}
	
	public String getDestinationName() {
		return destinationName;
	}
	
	public String getSourceName() {
		return sourceName;
	}
}
