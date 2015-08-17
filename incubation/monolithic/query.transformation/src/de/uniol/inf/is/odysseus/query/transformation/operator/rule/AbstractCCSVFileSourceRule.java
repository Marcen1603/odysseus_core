package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

public abstract class AbstractCCSVFileSourceRule extends AbstractRule{

	
	public AbstractCCSVFileSourceRule(String name, String targetPlatform) {
		super(name,targetPlatform );
	}


	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof AbstractAccessAO){
			
			AbstractAccessAO operator = (AbstractAccessAO) logicalOperator;
			if (operator.getWrapper() != null) {
				if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
					return true;
				}
				if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator.getWrapper())) {
					return true;
				}
			}
		}
	
		return false;
	}


	@Override
	public Class<?> getConditionClass() {
		return AbstractAccessAO.class;
	}


}