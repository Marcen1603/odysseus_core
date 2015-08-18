package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.transformation.modell.TransformationInformation;

public abstract class AbstractCCSVFileSinkRule extends AbstractRule {
	
	public AbstractCCSVFileSinkRule(String name, String targetPlatform) {
		super(name,targetPlatform );
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
	
	
	public void analyseOperator(ILogicalOperator logicalOperator,TransformationInformation transformationInformation){
		
		CSVFileSink csvFileSinkOP = (CSVFileSink) logicalOperator;
		
		
		transformationInformation.addDataHandler(csvFileSinkOP.getDataHandler());
		transformationInformation.addProtocolHandler(csvFileSinkOP.getProtocolHandler());
		transformationInformation.addTransportHandler(csvFileSinkOP.getTransportHandler());
		
	}
	
	@Override
	public void addOperatorConfiguration(ILogicalOperator logicalOperator,
			TransformationInformation transformationInformation) {
		
		CSVFileSink csvfileSink = (CSVFileSink) logicalOperator;
		Map<String,String> optionMap =  new HashMap<String,String>();
		optionMap = csvfileSink.getOptionsMap();
		
		transformationInformation.addOperatorConfiguration(logicalOperator, optionMap);
	}
	
}
