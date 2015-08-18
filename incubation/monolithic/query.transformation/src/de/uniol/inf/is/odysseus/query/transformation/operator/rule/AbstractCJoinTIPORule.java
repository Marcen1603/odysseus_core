package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.query.transformation.modell.TransformationInformation;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;

public abstract class AbstractCJoinTIPORule extends AbstractRule{
	
	public AbstractCJoinTIPORule(String name, String targetPlatform) {
		super(name,targetPlatform );
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if(logicalOperator instanceof JoinAO){
			
			
			return true;
		}
		return false;
	}

	@Override
	public Class<?> getConditionClass() {
		return JoinTIPO.class;
	}

	
	@Override
	public void analyseOperator(ILogicalOperator logicalOperator,
			TransformationInformation transformationInformation) {
	
		JoinAO joinAO = (JoinAO) logicalOperator;

		String areaName = "TIJoinSA";		
		
		if (joinAO.getSweepAreaName() != null){
			areaName = joinAO.getSweepAreaName();
		}
		
		transformationInformation.addSweepArea(areaName);
		
		IPredicate<?> predicate = joinAO.getPredicate();
		String predicateValue = predicate.toString();
		IExpression<?> mepExpression  = MEP.getInstance().parse(predicateValue);
		
		transformationInformation.addMEPFunction(mepExpression);
	}

}
