package de.uniol.inf.is.odysseus.codegenerator.rcp.model;

import java.util.Map;

/**
 * interface for the special options
 * 
 * @author MarcPreuschaft
 *
 */
public interface IRcpOptionComposite {
	
	/**
	 * get all values for the special options
	 * @return
	 */
	public Map<String,String> getInput();
	
	/**
	 * check if the paranet composite is disposed
	 * @return
	 */
	public boolean isDisposed();

}