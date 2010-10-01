package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;


/**
 * {@link AbstractQueryBuildParameter} which provides a priority for the query.
 * 
 * @author Wolf Bauer
 * 
 */
public  final class ParameterPriority extends AbstractQueryBuildParameter<Integer> {

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
