package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.parser.pql.Parameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class SelectAOBuilder extends AbstractOperatorBuilder {

	private static final String PREDICATE = "PREDICATE";
	private PredicateParameter predicateParameter = new PredicateParameter(
			PREDICATE, REQUIREMENT.MANDATORY);

	public SelectAOBuilder() {
		setParameters(predicateParameter);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 1);

		ILogicalOperator inputOp = inputOps.get(0);
		SDFAttributeList schema = inputOp.getOutputSchema();

		SelectAO selectAO = new SelectAO(predicateParameter.getValue());
		selectAO.subscribeToSource(inputOp, 0, 0, schema);

		return selectAO;
	}

}
