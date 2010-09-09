package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UnionAOBuilder extends AbstractOperatorBuilder {

	public UnionAOBuilder() {
		super(2, 2);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new UnionAO();
	}

	@Override
	protected boolean internalValidation() {
		ILogicalOperator firstInput = getInputOperator(0);
		ILogicalOperator secondInput = getInputOperator(1);
		SDFAttributeList firstSchema = firstInput.getOutputSchema();
		SDFAttributeList secondSchema = secondInput.getOutputSchema();
		if (!firstSchema.compatibleTo(secondSchema)) {
			throw new IllegalArgumentException("incompatible schemas for union");
		}

		return true;
	}
}
