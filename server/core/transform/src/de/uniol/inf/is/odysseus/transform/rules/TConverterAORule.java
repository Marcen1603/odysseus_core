package de.uniol.inf.is.odysseus.transform.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ConverterAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ConverterPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TConverterAORule extends AbstractTransformationRule<ConverterAO> {

	static final private Logger LOG = LoggerFactory.getLogger(TConverterAORule.class);

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ConverterAO operator, TransformationConfiguration config) throws RuleException {

		if (operator.isUpdateMeta()) {
			if (getTimestampAOAsFather(operator) == null) {
				insertTimestampAO(operator, operator.getDateFormat());
			}
		}

		OptionMap options = new OptionMap(operator.getOptionMap());

		IStreamObjectDataHandler<?> outputDataHandler = DataHandlerRegistry
				.getStreamObjectDataHandler(operator.getOutputDataHandler(), operator.getOutputSchema());
		if (outputDataHandler == null) {
			LOG.error("No output data handler {} found.", operator.getOutputDataHandler());
			throw new TransformationException("No data handler " + operator.getOutputDataHandler() + " found.");
		}

		IStreamObjectDataHandler<?> inputDataHandler = DataHandlerRegistry
				.getStreamObjectDataHandler(operator.getInputDataHandler(), operator.getInputSchema());
		if (inputDataHandler == null) {
			LOG.error("No input data handler {} found.", operator.getInputDataHandler());
			throw new TransformationException("No data handler " + operator.getInputDataHandler() + " found.");
		}

		IProtocolHandler<?> protocolHandler = ProtocolHandlerRegistry.getInstance(operator.getProtocolHandler(),
				ITransportDirection.IN, IAccessPattern.PULL, options, outputDataHandler);

		if (protocolHandler == null) {
			LOG.error("No protocol handler {} found.", operator.getProtocolHandler());
			throw new TransformationException("No protocol handler " + operator.getProtocolHandler() + " found.");
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		ConverterPO converter = new ConverterPO(inputDataHandler, outputDataHandler, protocolHandler,
				operator.getOptionMap());
		defaultExecute(operator, converter, config, true, true);
	}

	@Override
	public boolean isExecutable(ConverterAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ConverterAO --> ConverterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ConverterAO> getConditionClass() {
		return ConverterAO.class;
	}

}
