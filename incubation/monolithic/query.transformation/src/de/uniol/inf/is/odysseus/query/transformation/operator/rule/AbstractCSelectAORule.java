package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCSelectAORule extends AbstractRule{

	public AbstractCSelectAORule(String name, String targetPlatform) {
		super(name,targetPlatform );
	}
	
	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if(logicalOperator instanceof SelectAO){
			return true;
		}else{
			return false;
		}
	}


	@Override
	public Class<?> getConditionClass() {
		return SelectAO.class;
	}



}
