package de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;

/**
 * A {@link LoadBalancingSynchronizerAO} transfers the elements only from the
 * first (earliest) port until both ports retrieve same elements or a given
 * threshold-time elapses.
 * 
 * @author Michael Brand
 */
@LogicalOperator(category = { LogicalOperatorCategory.PROCESSING }, doc = "Transfers elements from one input port determined by load balancing punctuations", maxInputPorts = 2, minInputPorts = 1, name = "LoadBalancingSynchronizer")
public class LoadBalancingSynchronizerAO extends BinaryLogicalOp {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 7546316063206366721L;

	/**
	 * The threshold for the synchronization process.
	 */
	private TimeValueItem threshold;

	/**
	 * Constructs a new {@link LoadBalancingSynchronizerAO}.
	 * 
	 * @see BinaryLogicalOp#BinaryLogicalOp()
	 */
	public LoadBalancingSynchronizerAO() {

		super();

	}

	/**
	 * Constructs a new {@link LoadBalancingSynchronizerAO} as a copy of an
	 * existing one.
	 * 
	 * @param syncAO
	 *            The {@link LoadBalancingSynchronizerAO} to be copied.
	 * @see BinaryLogicalOp#BinaryLogicalOp(AbstractLogicalOperator)
	 */
	public LoadBalancingSynchronizerAO(LoadBalancingSynchronizerAO syncAO) {

		super(syncAO);

	}

	@Override
	public AbstractLogicalOperator clone() {

		return new LoadBalancingSynchronizerAO(this);

	}

	/**
	 * Gets the threshold for the synchronization process.
	 * 
	 * @return Value and unit of the time-span, the synchronization process
	 *         shall last at maximum.
	 */
	public TimeValueItem getThreshold() {

		return this.threshold;

	}

	/**
	 * Sets the threshold for the synchronization process.
	 */
	@Parameter(type = TimeParameter.class, name = "threshold", optional = true)
	public void setThreshold(TimeValueItem threshold) {

		this.threshold = threshold;

	}

}