package de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.parameter;

import de.uniol.inf.is.odysseus.base.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.AbstractQueryBuildParameter;

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
