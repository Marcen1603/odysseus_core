package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
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
		// TODO Auto-generated method stub
		
	}

}
