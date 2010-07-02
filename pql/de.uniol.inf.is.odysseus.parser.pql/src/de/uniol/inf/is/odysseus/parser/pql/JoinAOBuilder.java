package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.parser.pql.Parameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JoinAOBuilder extends AbstractOperatorBuilder {

	private static final String PREDICATE = "PREDICATE";
	private PredicateParameter predicateParameter = new PredicateParameter(
			PREDICATE, REQUIREMENT.MANDATORY);

	public JoinAOBuilder() {
		setParameters(predicateParameter);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 2);

		ILogicalOperator inputOp1 = inputOps.get(0);
		ILogicalOperator inputOp2 = inputOps.get(1);
		SDFAttributeList schema1 = inputOp1.getOutputSchema();
		SDFAttributeList schema2 = inputOp2.getOutputSchema();

		JoinAO joinAO = new JoinAO();
		joinAO.setPredicate(predicateParameter.getValue());

		joinAO.subscribeToSource(inputOp1, 0, 0, schema1);
		joinAO.subscribeToSource(inputOp2, 0, 0, schema2);

		return joinAO;
	}

}
