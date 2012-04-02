package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * This operator can reduce traffic. It lets an event pass if its different than the last event 
 * @author Marco Grawunder
 *
 */
@LogicalOperator(name="CHANGEDETECT", minInputPorts=1, maxInputPorts=1)
public class ChangeDetectAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9042464546094886480L;

	public ChangeDetectAO(AbstractLogicalOperator po) {
		super(po);
	}

	public ChangeDetectAO() {
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ChangeDetectAO(this);
	}

}
