package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;

/**
 * {@link AbstractQueryBuildParameter} which provides an
 * {@link IBufferPlacementStrategy} for creating the query.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterBufferPlacementStrategy extends
		AbstractQueryBuildParameter<IBufferPlacementStrategy> {

	/**
	 * Creates a ParameterBufferPlacementStrategy.
	 * 
	 * @param object
	 *            {@link IBufferPlacementStrategy} for creating the query.
	 */
	public ParameterBufferPlacementStrategy(IBufferPlacementStrategy object) {
		super(object);
	}
}
