package de.uniol.inf.is.odysseus.logicaloperator.builder;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UnionAOBuilder extends AbstractOperatorBuilder {

	public UnionAOBuilder() {
		super(2, 999);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		return new UnionAO();
	}

	@Override
	protected boolean internalValidation() {
		int count = getInputOperatorCount();
		ILogicalOperator firstInput = getInputOperator(0);
		for (int i=1;i<count;i++){
			ILogicalOperator currentInput = getInputOperator(i);	
			validateSchemata(firstInput, currentInput);
		}
		return true;
	}

	protected void validateSchemata(ILogicalOperator firstInput, ILogicalOperator secondInput) {
		SDFAttributeList firstSchema = firstInput.getOutputSchema();
		SDFAttributeList secondSchema = secondInput.getOutputSchema();
		if (!firstSchema.compatibleTo(secondSchema)) {
			throw new IllegalArgumentException("incompatible schemas for union");
		}
	}
}
