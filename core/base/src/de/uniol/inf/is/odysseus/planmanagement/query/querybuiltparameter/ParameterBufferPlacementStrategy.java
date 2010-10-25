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

	private String name;

	/**
	 * Creates a ParameterBufferPlacementStrategy.
	 * 
	 * @param object
	 *            {@link IBufferPlacementStrategy} for creating the query.
	 */
	public ParameterBufferPlacementStrategy(IBufferPlacementStrategy object) {
		super(object);
	}
	
	public ParameterBufferPlacementStrategy(){
		super(null);
	}
	
	public ParameterBufferPlacementStrategy(String name){
		super(null);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return super.toString()+" name "+name;
	}
	
}
