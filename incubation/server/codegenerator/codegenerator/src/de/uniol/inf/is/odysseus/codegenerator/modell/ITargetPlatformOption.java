package de.uniol.inf.is.odysseus.codegenerator.modell;

/**
 * Interface for the special targetOption 
 * 
 * @author MarcPreuschaft
 *
 */
public interface ITargetPlatformOption {

	/**
	 * this function parse the special 
	 * option from transformationParameter
	 * 
	 * @param parameter
	 */
	public void parse(TransformationParameter parameter);
}
