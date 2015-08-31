package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCCSVFileSourceRule<T extends CSVFileSource> extends AbstractRule<CSVFileSource> {

	public AbstractCCSVFileSourceRule(String name) {
		super(name);
	}

	public boolean isExecutable(CSVFileSource logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof CSVFileSource) {

			CSVFileSource operator = (CSVFileSource) logicalOperator;
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

		}

		return false;
	}



	public void analyseOperator(CSVFileSource logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		CSVFileSource csvFileSource = (CSVFileSource) logicalOperator;

		transformationInformation
				.addDataHandler(csvFileSource.getDataHandler());
		transformationInformation.addProtocolHandler(csvFileSource
				.getProtocolHandler());
		transformationInformation.addTransportHandler(csvFileSource
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