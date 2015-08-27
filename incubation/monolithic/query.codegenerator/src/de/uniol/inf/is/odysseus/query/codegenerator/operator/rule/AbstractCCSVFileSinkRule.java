package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCCSVFileSinkRule extends AbstractRule {
	
	public AbstractCCSVFileSinkRule(String name) {
		super(name);
	}

	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
		if(logicalOperator instanceof AbstractSenderAO){
			
			AbstractSenderAO operator = (AbstractSenderAO) logicalOperator;
			
				if (operator.getWrapper() != null) {
						if (Constants.GENERIC_PULL.equalsIgnoreCase(operator
								.getWrapper())) {
							return true;
						}
						if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator
								.getWrapper())) {
							return true;
						}
				}
	
			return false;
			
		}
		
		return false;
	}

	public Class<?> getConditionClass() {
		return AbstractSenderAO.class;
	}
	
	
	public void analyseOperator(ILogicalOperator logicalOperator,QueryAnalyseInformation transformationInformation){
		
		AbstractSenderAO abstractSenderAO =(AbstractSenderAO)logicalOperator;

		transformationInformation.addDataHandler(abstractSenderAO.getDataHandler());
		transformationInformation.addProtocolHandler(abstractSenderAO.getProtocolHandler());
		transformationInformation.addTransportHandler(abstractSenderAO.getTransportHandler());
		
	}
	
	@Override
	public void addOperatorConfiguration(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation) {
		
		AbstractSenderAO abstractSenderAO =(AbstractSenderAO)logicalOperator;
		

		Map<String,String> optionMap =  new HashMap<String,String>();
		
	
		optionMap = abstractSenderAO.getOptionsMap();
		
		transformationInformation.addOperatorConfiguration(logicalOperator, optionMap);
	}
	
}
