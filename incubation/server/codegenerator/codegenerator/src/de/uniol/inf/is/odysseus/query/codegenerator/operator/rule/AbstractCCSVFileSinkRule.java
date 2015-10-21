package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCCSVFileSinkRule<T extends CSVFileSink> extends AbstractCOperatorRule<CSVFileSink> {

	public AbstractCCSVFileSinkRule(String name) {
		super(name);
	}

	public boolean isExecutable(CSVFileSink logicalOperator,
			TransformationConfiguration transformationConfiguration) {
	
			
			if (logicalOperator.getWrapper() != null) {
				if (Constants.GENERIC_PULL.equalsIgnoreCase(logicalOperator
						.getWrapper())) {
					return true;
				}
				if (Constants.GENERIC_PUSH.equalsIgnoreCase(logicalOperator
						.getWrapper())) {
					return true;
				}
			}

			return false;

	
	}


	@Override
	public void analyseOperator(CSVFileSink logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		transformationInformation.addDataHandler(logicalOperator
				.getDataHandler());
		transformationInformation.addProtocolHandler(logicalOperator
				.getProtocolHandler());
		transformationInformation.addTransportHandler(logicalOperator
				.getTransportHandler());

	}

	@Override
	public void addOperatorConfiguration(CSVFileSink logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		Map<String, String> optionMap = new HashMap<String, String>();

		optionMap = logicalOperator.getOptionsMap();

		transformationInformation.addOperatorConfiguration(logicalOperator,
				optionMap);
	}

}
