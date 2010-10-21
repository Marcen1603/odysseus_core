package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.planmanagement.IBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.IExecutionSetting;

/**
 * {@link IQueryBuildSetting} which provides an
 * {@link IBufferPlacementStrategy} for creating the query.
 * 
 * @author Wolf Bauer
 * 
 */
public class ParameterBufferPlacementStrategy extends Setting<IBufferPlacementStrategy> implements
		IQueryBuildSetting<IBufferPlacementStrategy>, IExecutionSetting<IBufferPlacementStrategy> {

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
