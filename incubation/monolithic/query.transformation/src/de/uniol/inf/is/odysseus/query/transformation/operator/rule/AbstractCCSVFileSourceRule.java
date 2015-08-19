package de.uniol.inf.is.odysseus.query.transformation.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.transformation.modell.QueryAnalyseInformation;

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


	public void analyseOperator(ILogicalOperator logicalOperator,QueryAnalyseInformation transformationInformation){
		
		CSVFileSource csvFileSource = (CSVFileSource) logicalOperator;
		
		transformationInformation.addDataHandler(csvFileSource.getDataHandler());
		transformationInformation.addProtocolHandler(csvFileSource.getProtocolHandler());
		transformationInformation.addTransportHandler(csvFileSource.getTransportHandler());
		
	}
	
	@Override
	public void addOperatorConfiguration(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation) {
		
		CSVFileSource csvFileSourceOP = (CSVFileSource) logicalOperator;
		Map<String,String> optionMap =  new HashMap<String,String>();
		optionMap = csvFileSourceOP.getOptionsMap();
		
	
		transformationInformation.addOperatorConfiguration(logicalOperator, optionMap);
	}

}