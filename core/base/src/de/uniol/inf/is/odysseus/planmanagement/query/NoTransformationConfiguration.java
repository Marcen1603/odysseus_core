/**
 * 
 */
package de.uniol.inf.is.odysseus.planmanagement.query;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;

/**
 * NoTransformationConfiguration is an {@link Exception} which occurs during
 * transformation of an query if no {@link TransformationConfiguration} is
 * established.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoTransformationConfiguration extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1842137531605713157L;

	/**
	 * Creates a new NoTransformationConfiguration. The Exception Message will
	 * be "No Transformation Configuration specified.".
	 */
	public NoTransformationConfiguration() {
		super("No Transformation Configuration specified.");
	}
}
