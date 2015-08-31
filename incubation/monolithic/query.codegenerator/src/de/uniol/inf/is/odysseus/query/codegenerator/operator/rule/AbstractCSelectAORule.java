package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.Utils;

public abstract class AbstractCSelectAORule<T extends SelectAO> extends AbstractRule<SelectAO> {

	public AbstractCSelectAORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(SelectAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if (logicalOperator instanceof SelectAO) {
			return true;
		} else {
			return false;
		}
	}


	public void analyseOperator(SelectAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		SelectAO selectAO = (SelectAO) logicalOperator;
		IPredicate<?> predicate = selectAO.getPredicate();

		String predicateValue = predicate.toString();
		IExpression<?> mepExpression = MEP.getInstance().parse(predicateValue);

		Map<String, IExpression<?>> mepFunctions = Utils
				.getAllMEPFunctions(mepExpression);

		transformationInformation.addMEPFunction(mepFunctions);
	}

}
