package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public abstract class AbstractCSlidingTimeWindowTIPORule extends AbstractRule{

	
	public AbstractCSlidingTimeWindowTIPORule(String name, String targetPlatform) {
		super(name,targetPlatform );
	}
	
	@Override
	public Class<?> getConditionClass() {
		return AbstractWindowAO.class;
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
	
		if(logicalOperator instanceof AbstractWindowAO){
		
			AbstractWindowAO operator = (AbstractWindowAO) logicalOperator;
			switch (operator.getWindowType()) {
				case TIME:
					if (operator.getWindowSlide() == null && operator.getWindowAdvance() == null) {
						return true;
					} 
					return false;
				default:
					return false;
			}
		
		}
		return false;
	}


}


