package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;


/**
 * {@link IQueryBuildSetting} which provides a priority for the query.
 * 
 * @author Wolf Bauer
 * 
 */
public  final class ParameterPriority extends Setting<Integer> implements IQueryBuildSetting<Integer> {

	/**
	 * Creates a ParameterPriority.
	 * 
	 * @param value
	 *            Priority of the query.
	 */
	public ParameterPriority(Integer value) {
		super(value);
	}
}
