package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCCSVFileSinkRule<T extends CSVFileSink> extends AbstractRule<CSVFileSink> {

	public AbstractCCSVFileSinkRule(String name) {
		super(name);
	}

	public boolean isExecutable(CSVFileSink logicalOperator,
			TransformationConfiguration transformationConfiguration) {

		if (logicalOperator instanceof AbstractSenderAO) {

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



	public void analyseOperator(AbstractSenderAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		AbstractSenderAO abstractSenderAO = (AbstractSenderAO) logicalOperator;

		transformationInformation.addDataHandler(abstractSenderAO
				.getDataHandler());
		transformationInformation.addProtocolHandler(abstractSenderAO
				.getProtocolHandler());
		transformationInformation.addTransportHandler(abstractSenderAO
				.getTransportHandler());

	}

	@Override
	public void addOperatorConfiguration(CSVFileSink logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		AbstractSenderAO abstractSenderAO = (AbstractSenderAO) logicalOperator;

		Map<String, String> optionMap = new HashMap<String, String>();

		optionMap = abstractSenderAO.getOptionsMap();

		transformationInformation.addOperatorConfiguration(logicalOperator,
				optionMap);
	}

}
