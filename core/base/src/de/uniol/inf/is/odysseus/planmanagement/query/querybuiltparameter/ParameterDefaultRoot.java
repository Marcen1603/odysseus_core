package de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.configuration.Setting;

/**
 * {@link IQueryBuildSetting} which provides a physical root for the
 * physical plan of a query.
 * 
 * @author Wolf Bauer
 * 
 */
public final class ParameterDefaultRoot extends Setting<IPhysicalOperator> implements
		IQueryBuildSetting<IPhysicalOperator> {

	private int port;

	/**
	 * Creates a ParameterDefaultRoot.
	 * 
	 * @param value
	 *            physical root for the physical plan of a query.
	 */
	public ParameterDefaultRoot(IPhysicalOperator value) {
		super(value);
		this.port = 0;
	}

	public ParameterDefaultRoot(IPhysicalOperator value, int sinkInPort) {
		super(value);
		this.port = sinkInPort;
	}
	
	@Override
	public IPhysicalOperator getValue() {
		return super.getValue();
	}
	
	public int getPort() {
		return port;
	};
}
