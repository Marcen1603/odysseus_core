package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCSelectAORule<T extends SelectAO> extends AbstractCOperatorRule<SelectAO> {

	public AbstractCSelectAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(SelectAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {

				return true;
	
	}


	@Override
	public void analyseOperator(SelectAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		IPredicate<?> predicate = logicalOperator.getPredicate();

		String predicateValue = predicate.toString();
		IExpression<?> mepExpression = MEP.getInstance().parse(predicateValue);

		Map<String, IExpression<?>> mepFunctions = getAllMEPFunctions(mepExpression);

		transformationInformation.addMEPFunction(mepFunctions);
	}

}
