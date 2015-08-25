package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;

public abstract class AbstractCSlidingTimeWindowTIPORule extends AbstractRule{

	
	public AbstractCSlidingTimeWindowTIPORule(String name) {
		super(name);
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

	
	public void analyseOperator(ILogicalOperator logicalOperator,QueryAnalyseInformation transformationInformation){
		
		AccessAO accessAO = (AccessAO)logicalOperator;
	
		transformationInformation.addDataHandler(accessAO.getDataHandler());
		transformationInformation.addProtocolHandler(accessAO.getProtocolHandler());
		transformationInformation.addTransportHandler(accessAO.getTransportHandler());
		
	}

	
}


