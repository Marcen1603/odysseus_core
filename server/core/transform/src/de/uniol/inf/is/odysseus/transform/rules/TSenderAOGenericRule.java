/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.transform.rules;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule for generic sender operator
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TSenderAOGenericRule extends AbstractTransformationRule<AbstractSenderAO> {

	static Logger LOG = LoggerFactory.getLogger(TSenderAOGenericRule.class);
	static final InfoService INFOSERVICE = InfoServiceFactory.getInfoService(TSenderAOGenericRule.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(AbstractSenderAO operator, TransformationConfiguration config) throws RuleException {
		Resource senderPOName = operator.getSinkname();

		OptionMap options = new OptionMap(operator.getOptionsMap());

		IStreamObjectDataHandler<?> dataHandler = getDataHandler(operator);
		if (dataHandler == null) {
			LOG.error("No data handler {} found.", operator.getDataHandler());
			throw new TransformationException("No data handler " + operator.getDataHandler() + " found.");
		}

		if (operator.isWriteMetaData()) {
			IMetaAttribute metaAttribute;
			// for standalone version
			if (operator.getOutputSchema() != null) {
				metaAttribute = MetadataRegistry
						.getMetadataType(operator.getOutputSchema().getMetaAttributeNames());
				
			} else {
				metaAttribute = MetadataRegistry
						.getMetadataType(operator.getInputSchema(0).getMetaAttributeNames());
			}
			dataHandler.setMetaAttribute(metaAttribute);
		}

		IProtocolHandler<?> protocolHandler = getProtocolHandler(operator, dataHandler, options);
		if (protocolHandler == null) {
			LOG.error("No protocol handler {} found.", operator.getProtocolHandler());
			throw new TransformationException("No protocol handler " + operator.getProtocolHandler() + " found.");
		}
		protocolHandler.setSchema(dataHandler.getSchema());

		if (!operator.getTransportHandler().equalsIgnoreCase("NONE")) {
			ITransportHandler transportHandler = getTransportHandler(operator, protocolHandler, options);
			if (transportHandler == null) {
				LOG.error("No transport handler {} found.", operator.getTransportHandler());
				throw new TransformationException("No transport handler " + operator.getTransportHandler() + " found.");
			}
		}

		ISink<?> senderPO = this.getSenderPO(protocolHandler, operator);
		if (!config.isVirtualTransformation()) {
			getDataDictionary().putSinkplan(senderPOName, senderPO);
			if (!getDataDictionary().containsSink(senderPOName, getCaller())) {
				getDataDictionary().addSink(senderPOName, new LogicalPlan(operator), getCaller());
			}
		}

		List<String> unusedOptions = options.getUnreadOptions();
		if (!unusedOptions.isEmpty()) {
			INFOSERVICE.warning("The following options where not used in translation " + unusedOptions);
		}

		defaultExecute(operator, senderPO, config, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(AbstractSenderAO operator, TransformationConfiguration config) {
		if (operator.isAllPhysicalInputSet()) {
			if (getDataDictionary().getSinkplan(operator.getSinkname()) == null) {
				if (operator.getWrapper() != null) {
					if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
						return true;
					}
					if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator.getWrapper())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "AbstractSenderAO (generic) --> SenderPO";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SENDER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public Class<? super AbstractSenderAO> getConditionClass() {
		return AbstractSenderAO.class;
	}

	/**
	 * Get the transport handler based on the configuration of the operator
	 * 
	 * @param operator
	 *            The {@link AbstractSenderAO}
	 * @param protocolHandler
	 *            the current protocol handler
	 * @return The transport handler
	 */
	private static ITransportHandler getTransportHandler(AbstractSenderAO operator, IProtocolHandler<?> protocolHandler,
			OptionMap options) {
		ITransportHandler transportHandler = null;
		if (operator.getTransportHandler() != null) {
			transportHandler = TransportHandlerRegistry.getInstance(operator.getTransportHandler(), protocolHandler,
					options);
		}
		return transportHandler;
	}

	/**
	 * Get the protocol handler based on the configuration of the operator
	 * 
	 * @param operator
	 *            The {@link AbstractSenderAO}
	 * @param dataHandler
	 *            The current data handler
	 * @return The protocol handler
	 */
	private static IProtocolHandler<?> getProtocolHandler(AbstractSenderAO operator,
			IStreamObjectDataHandler<?> dataHandler, OptionMap options) {
		IProtocolHandler<?> protocolHandler = null;
		if (operator.getProtocolHandler() != null) {
			if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
				protocolHandler = ProtocolHandlerRegistry.getInstance(operator.getProtocolHandler(),
						ITransportDirection.OUT, IAccessPattern.PULL, options, dataHandler);
			} else {
				protocolHandler = ProtocolHandlerRegistry.getInstance(operator.getProtocolHandler(),
						ITransportDirection.OUT, IAccessPattern.PUSH, options, dataHandler);
			}
		}
		return protocolHandler;
	}

	/**
	 * Get the data handler based on the configuration of the operator
	 * 
	 * @param operator
	 *            The {@link AbstractSenderAO}
	 * @return The data handler
	 */
	private static IStreamObjectDataHandler<?> getDataHandler(AbstractSenderAO operator) {
		IDataHandler<?> dataHandler = null;
		String dataHandlerText = operator.getDataHandler();
		if (dataHandlerText == null) {
			if (operator.getInputSchema(0) != null) {
				dataHandlerText = operator.getInputSchema(0).getType().getSimpleName();
			}
		}
		if (dataHandlerText != null) {
			if (operator.getOutputSchema() != null) {
				dataHandler = DataHandlerRegistry.getDataHandler(dataHandlerText, operator.getOutputSchema());
			}
		}
		if (dataHandler != null) {
			if (!(dataHandler instanceof IStreamObjectDataHandler)) {
				throw new IllegalArgumentException("DataHandler " + dataHandlerText + " cannot be used!");
			}
			return (IStreamObjectDataHandler<?>) dataHandler;
		}
		return null;
	}

	/**
	 * Instantiates a new SenderPO. <br />
	 * <br />
	 * Override this method to use other implementations for the SenderPO.
	 * 
	 * @param protocolHandler
	 *            The protocol handler based on the configuration
	 * @param senderAO
	 *            The logical operator
	 * @return A new {@link SenderPO} implementation
	 */
	// @SuppressWarnings("static-method")
	protected SenderPO<?> getSenderPO(IProtocolHandler<?> protocolHandler, AbstractSenderAO senderAO) {
		return new SenderPO<>(protocolHandler);
	}

}