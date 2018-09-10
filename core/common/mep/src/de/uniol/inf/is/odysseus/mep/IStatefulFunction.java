package de.uniol.inf.is.odysseus.mep;

import java.io.Serializable;

/**
 * A marker interface for functions that provide a state
 * 
 * @author Marco Grawunder
 *
 */
public interface IStatefulFunction {
	
	/**
	 * Retieve the current state from the function
	 * @return
	 */
	Serializable getState();
	
	/**
	 * Set the current state to the function
	 * @param state
	 */
	void setState(Serializable state);
	
	

}
