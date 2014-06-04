package de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.LoadBalancingPunctuation;

/**
 * A {@link LoadBalancingSynchronizerAO} transfers the elements only from the
 * first (earliest) port until on both ports (first and second/earliest and
 * latest) {@link LoadBalancingPunctuation}s marking the end of a load balancing
 * process received. If that happens, only elements from the second (latest)
 * port are transferred and the earliest port will be closed.
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

}