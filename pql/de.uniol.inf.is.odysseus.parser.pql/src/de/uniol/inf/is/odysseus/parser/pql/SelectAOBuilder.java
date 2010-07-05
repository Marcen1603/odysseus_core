package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;

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

		SelectAO selectAO = new SelectAO();
		selectAO.setPredicate(predicateParameter.getValue());

		return selectAO;
	}

}
