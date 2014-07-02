package de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingPunctuation;

/**
 * A {@link LoadBalancingTestSenderAO} sends up all incoming tuples as they come
 * in and in addition sends {@link LoadBalancingPunctuation} in adjustable
 * intervals. This operator is for testing purpose to test the
 * load-balancing-synchronization.
 * 
 * When starting using this operator, it will wait {@value startSyncAfter}
 * tuples until a {@link LoadBalancingPunctuation} to start the synchronization
 * will be send. Then it will wait {@value stopSyncAfter} tuples until it will
 * send another {@link LoadBalancingPunctuation}. Then it will wait {@value
 * pauseBetween} tuples + {@value startSyncAfter} tuples until the next {@value
 * LoadBalancingPunctuation} will be send.
 * 
 * (startSyncAfter tuples waiting) -> (send {@link LoadBalancingPunctuation} to
 * start sync) -> (wait stopSyncAfter tuples) -> (send
 * {@link LoadBalancingPunctuation} to stop sync) -> (wait pauseBetween tuples)
 * -> (start from beginning)
 * 
 * @author Tobias Brandt
 * 
 */
@Deprecated
@LogicalOperator(category = { LogicalOperatorCategory.TEST }, doc = "Sends load balancing start- and stop-puctuations in periodic distance.", maxInputPorts = 1, minInputPorts = 1, name = "LoadBalancingTestSenderAO")
public class LoadBalancingTestSenderAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -3549054227734753568L;

	private long startSyncAfter;
	private long stopSyncAfter;
	private long pauseBetween;
	private boolean writeToLog;
	private String destinationPeerId;

	public String getDestinationPeerId() {
		return destinationPeerId;
	}

	@Parameter(type = StringParameter.class, name = "messageDestinationPeer", optional = true, doc = "If set, LoadBalancing Messages will be send to destination Peer (name)")
	public void setDestinationPeerId(String destinationPeerId) {
		this.destinationPeerId = destinationPeerId;
	}

	/**
	 * Standard-Constructor with all adjustable values set to 10 and logging
	 * enabled
	 */
	public LoadBalancingTestSenderAO() {
		super();
		writeToLog = true;
	}

	/**
	 * Copy-constructor to create a new {@link LoadBalancingTestSenderAO} with
	 * the same configuration. Caution: Does not copy the internal state!
	 * 
	 * @param other
	 *            The {@link LoadBalancingTestSenderAO} to be copied.
	 */
	public LoadBalancingTestSenderAO(LoadBalancingTestSenderAO other) {
		super(other);

		startSyncAfter = other.startSyncAfter;
		stopSyncAfter = other.stopSyncAfter;
		pauseBetween = other.pauseBetween;
		writeToLog = other.writeToLog;
		destinationPeerId = other.destinationPeerId;
	}

	/**
	 * 
	 * @return The value with the number of tuples after which the
	 *         {@link LoadBalancingPunctuation} to start the sync will be send.
	 */
	public long getStartSyncAfter() {
		return this.startSyncAfter;
	}

	/**
	 * 
	 * @return The value with the number of tuples after which the
	 *         {@link LoadBalancingPunctuation} to stop the sync will be send.
	 */
	public long getStopAfter() {
		return this.stopSyncAfter;
	}

	/**
	 * 
	 * @return The value with the number of tuples after which the process
	 *         restarts (additional waiting after the sync stopped)
	 */
	public long getPauseBetween() {
		return this.pauseBetween;
	}

	/**
	 * 
	 * @return true, if operator should write to log if a
	 *         {@link LoadBalancingPunctuation} is send
	 */
	public boolean getWriteToLog() {
		return this.writeToLog;
	}

	/**
	 * 
	 * @param numberOfTuples
	 *            The value with the number of tuples after which the
	 *            {@link LoadBalancingPunctuation} to start the sync will be
	 *            send.
	 */
	@Parameter(type = IntegerParameter.class, name = "startSyncAfter", optional = false, doc = "Number of tuples to wait before a LoadBalancingPunctuation to start will be send")
	public void setStartSyncAfter(long numberOfTuples) {
		this.startSyncAfter = numberOfTuples;
	}

	/**
	 * 
	 * @param numberOfTuples
	 *            The value with the number of tuples after which the
	 *            {@link LoadBalancingPunctuation} to stop the sync will be
	 *            send.
	 */
	@Parameter(type = IntegerParameter.class, name = "stopSyncAfter", optional = false, doc = "Number of tuples to wait between start and stop LoadBalancingPunctuation")
	public void setStopSyncAfter(long numberOfTuples) {
		this.stopSyncAfter = numberOfTuples;
	}

	/**
	 * 
	 * @param numberOfTuples
	 *            The value with the number of tuples after which the process
	 *            restarts (additional waiting after the sync stopped)
	 */
	@Parameter(type = IntegerParameter.class, name = "pauseBetween", optional = false, doc = "Number of tuples to pause before a new round")
	public void setPauseBetween(long numberOfTuples) {
		this.pauseBetween = numberOfTuples;
	}

	/**
	 * 
	 * @param writeToLog
	 *            Set to true, if operator should write to log when a
	 *            {@link LoadBalancingPunctuation} is send
	 */
	@Parameter(type = BooleanParameter.class, name = "writeToLog", optional = true, doc = "Write to log if a LoadBalancingPunctuation was send")
	public void setWriteToLog(boolean writeToLog) {
		this.writeToLog = writeToLog;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LoadBalancingTestSenderAO(this);
	}
}
