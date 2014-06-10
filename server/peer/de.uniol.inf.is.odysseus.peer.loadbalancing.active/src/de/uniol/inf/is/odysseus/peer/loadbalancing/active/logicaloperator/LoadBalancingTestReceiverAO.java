package de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingPunctuation;

/**
 * A {@link LoadBalancingTestReceiverAO} listens for
 * {@link LoadBalancingPunctuation}s and can log them and filter them, so they
 * won't be sent to the next operator.
 * 
 * @author Tobias Brandt
 * 
 */
@LogicalOperator(category = { LogicalOperatorCategory.TEST }, doc = "Receives incoming LoadBalancingPuctuations and gives options to filter them and write them to the log.", maxInputPorts = 1, minInputPorts = 1, name = "LoadBalancingTestReceiverAO")
public class LoadBalancingTestReceiverAO extends UnaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -2897044452611686405L;

	private boolean filterLoadBalancingPunctuations;
	private boolean writeToLog;

	/**
	 * Standard-constructor which creates a {@link LoadBalancingTestReceiverAO}
	 * that filters the {@link LoadBalancingPunctuation}s and writes them to log
	 */
	public LoadBalancingTestReceiverAO() {
		filterLoadBalancingPunctuations = true;
		writeToLog = true;
	}

	/**
	 * Copies an existing {@link LoadBalancingTestReceiverAO} with it's
	 * configuration
	 * 
	 * @param other
	 *            {@link LoadBalancingTestReceiverAO} to be copied
	 */
	public LoadBalancingTestReceiverAO(LoadBalancingTestReceiverAO other) {
		super(other);
		this.filterLoadBalancingPunctuations = other
				.getFilterLoadBalancingPuctuations();
		this.writeToLog = other.getWriteToLog();
	}

	/**
	 * Clones this {@link LoadBalancingTestReceiverAO} and returns the clone
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new LoadBalancingTestReceiverAO(this);
	}

	/**
	 * 
	 * @return true, if this operator filters the
	 *         {@link LoadBalancingPunctuation}s so they won't we send to the
	 *         next operator
	 */
	public boolean getFilterLoadBalancingPuctuations() {
		return filterLoadBalancingPunctuations;
	}

	/**
	 * 
	 * @return true, if this operator writes the received
	 *         {@link LoadBalancingPunctuation}s to the log (in debug log
	 *         level)
	 */
	public boolean getWriteToLog() {
		return writeToLog;
	}

	/**
	 * 
	 * @param filterLoadBalancingPuctuations
	 *            Set to true if incoming {@link LoadBalancingPunctuation}s
	 *            should be filtered so that they won't be send to the next
	 *            operator false, if you want to send the
	 *            {@link LoadBalancingPunctuation}s further
	 */
	@Parameter(type = BooleanParameter.class, name = "filterLoadBalancingPuctuations", optional = false)
	public void setFilterLoadBalancingPuctuations(
			boolean filterLoadBalancingPuctuations) {
		this.filterLoadBalancingPunctuations = filterLoadBalancingPuctuations;
	}

	/**
	 * 
	 * @param writeToLog
	 *            Set to true if incoming {@link LoadBalancingPunctuation}s
	 *            should be logged
	 *            false if not
	 */
	@Parameter(type = BooleanParameter.class, name = "writeToLog", optional = false)
	public void setWriteToLog(boolean writeToLog) {
		this.writeToLog = writeToLog;
	}

}
