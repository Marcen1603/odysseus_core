package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.Utils;

public abstract class AbstractCJoinTIPORule<T extends JoinAO> extends AbstractCIntervalRule<JoinAO> {

	public AbstractCJoinTIPORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(JoinAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
	
			if (super.isExecutable(logicalOperator, transformationConfiguration)) {
				return !(logicalOperator instanceof LeftJoinAO);
			}
			return false;
	

	}



	@Override
	public void analyseOperator(JoinAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		JoinAO joinAO = (JoinAO) logicalOperator;

		String areaName = "TIJoinSA";

		if (joinAO.getSweepAreaName() != null) {
			areaName = joinAO.getSweepAreaName();
		}

		transformationInformation.addSweepArea(areaName);

		IPredicate<?> predicate = joinAO.getPredicate();
		String predicateValue = predicate.toString();
		IExpression<?> mepExpression = MEP.getInstance().parse(predicateValue);

		Map<String, IExpression<?>> mepFunctions = Utils
				.getAllMEPFunctions(mepExpression);

		transformationInformation.addMEPFunction(mepFunctions);
	}

}
