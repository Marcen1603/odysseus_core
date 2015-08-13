package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.java.operator.JavaWindowOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.IOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;

public class TSlidingAdvanceTimeWindowTIPORule extends AbstractRule{


	public TSlidingAdvanceTimeWindowTIPORule(){
		super("TSlidingAdvanceTimeWindowTIPO", "Java");
	}
	
	public IOperator getOperatorTransformation() {
		return new JavaWindowOperator();
	}


	@Override
	public Class<? super AbstractWindowAO> getConditionClass() {
		return AbstractWindowAO.class;
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
	
		if(logicalOperator instanceof AbstractWindowAO){
		
			AbstractWindowAO operator = (AbstractWindowAO) logicalOperator;
			switch (operator.getWindowType()) {
			case TIME:
				if (operator.getWindowSlide() == null && operator.getWindowAdvance() != null) {
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
