package de.uniol.inf.is.odysseus.codegenerator;

import de.uniol.inf.is.odysseus.codegenerator.modell.TransformationParameter;

/**
 * interface for the code analyse component
 * 
 * @param parameter
 */
public interface ICAnalyse {

	/**
	 * start query analyse
	 * 
	 * @param parameter
	 */
	public void startQueryTransformation(TransformationParameter parameter);

}
