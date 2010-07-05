package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UnionAOBuilder extends AbstractOperatorBuilder {

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 2);

		UnionAO union = new UnionAO();
		ILogicalOperator firstInput = inputOps.get(0);
		ILogicalOperator secondInput = inputOps.get(1);
		SDFAttributeList firstSchema = firstInput.getOutputSchema();
		SDFAttributeList secondSchema = secondInput.getOutputSchema();
		if (!firstSchema.compatibleTo(secondSchema)) {
			throw new IllegalArgumentException("incompatible schemas for union");
		}

		return union;
	}
}
