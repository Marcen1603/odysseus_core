package de.uniol.inf.is.odysseus.codegenerator.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.codegenerator.model.QueryAnalyseInformation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

/**
 * abstract rule for the CSVFileSource
 * 
 * @author MarcPreuschaft
 *
 * @param <T>
 */
public abstract class AbstractCCSVFileSourceRule<T extends CSVFileSource> extends AbstractCOperatorRule<CSVFileSource> {

	public AbstractCCSVFileSourceRule(String name) {
		super(name);
	}

	public boolean isExecutable(CSVFileSource logicalOperator,
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
	public void analyseOperator(CSVFileSource logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		//add detected transportHandler, protocolHandler and dataHandler
		transformationInformation
				.addDataHandler(logicalOperator.getDataHandler());
		transformationInformation.addProtocolHandler(logicalOperator
				.getProtocolHandler());
		transformationInformation.addTransportHandler(logicalOperator
				.getTransportHandler());

		transformationInformation.addIterableSource(logicalOperator);

	}

	@Override
	public void addOperatorConfiguration(CSVFileSource logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		CSVFileSource csvFileSourceOP = (CSVFileSource) logicalOperator;
		Map<String, String> optionMap = new HashMap<String, String>();
		optionMap = csvFileSourceOP.getOptionsMap();

		transformationInformation.addOperatorConfiguration(logicalOperator,
				optionMap);

	}

}