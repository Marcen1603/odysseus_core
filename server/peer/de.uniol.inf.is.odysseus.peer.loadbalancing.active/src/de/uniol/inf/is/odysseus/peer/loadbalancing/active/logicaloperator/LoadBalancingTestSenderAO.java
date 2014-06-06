package de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

/**
 * 
 * @author Tobias Brandt
 *
 */
@LogicalOperator(category = { LogicalOperatorCategory.TEST }, doc = "Sends load balancing start- and stop-puctuations in periodic distance.", maxInputPorts = 1, minInputPorts = 1, name = "LoadBalancingTestSenderAO")
public class LoadBalancingTestSenderAO extends JxtaSenderAO {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -3549054227734753568L;

	private long startSyncAfter;
	private long stopSyncAfter;
	private long pauseBetween;
	
	public LoadBalancingTestSenderAO() {
		
	}
	
	public LoadBalancingTestSenderAO(LoadBalancingTestSenderAO other) {
		super(other);
		
		startSyncAfter = other.startSyncAfter;
		stopSyncAfter = other.stopSyncAfter;
		pauseBetween = other.pauseBetween;
	}
	
	public long getStartSyncAfter() {
		return this.startSyncAfter;
	}

	public long getStopAfter() {
		return this.stopSyncAfter;
	}

	public long getPauseBetween() {
		return this.pauseBetween;
	}
	
	@Parameter(type = IntegerParameter.class, name = "startSyncAfter", optional = false)
	public void setStartSyncAfter(long numberOfTuples) {
		this.startSyncAfter = numberOfTuples;
	}
	
	@Parameter(type = IntegerParameter.class, name = "stopSyncAfter", optional = false)
	public void stopSyncAfter(long numberOfTuples) {
		this.stopSyncAfter = numberOfTuples;
	}
	
	@Parameter(type = IntegerParameter.class, name = "pauseBetween", optional = false)
	public void setPauseBetween(long numberOfTuples) {
		this.pauseBetween = numberOfTuples;
	}
}
