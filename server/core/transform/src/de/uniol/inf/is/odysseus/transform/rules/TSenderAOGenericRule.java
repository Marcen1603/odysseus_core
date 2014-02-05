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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.SenderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule for generic sender operator
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TSenderAOGenericRule extends
		AbstractTransformationRule<AbstractSenderAO> {
	static Logger LOG = LoggerFactory.getLogger(TSenderAOGenericRule.class);

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
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(AbstractSenderAO operator,
			TransformationConfiguration config) throws RuleException {
		Resource senderPOName = operator.getSinkname();

		IDataHandler<?> dataHandler = getDataHandler(operator);
		if (dataHandler == null) {
			LOG.error("No data handler {} found.", operator.getDataHandler());
			throw new TransformationException("No data handler "
					+ operator.getDataHandler() + " found.");
		}

		IProtocolHandler<?> protocolHandler = getProtocolHandler(operator,
				dataHandler);
		if (protocolHandler == null) {
			LOG.error("No protocol handler {} found.",
					operator.getProtocolHandler());
			throw new TransformationException("No protocol handler "
					+ operator.getProtocolHandler() + " found.");
		}

		ITransportHandler transportHandler = getTransportHandler(operator,
				protocolHandler);
		if (transportHandler == null) {
			LOG.error("No transport handler {} found.",
					operator.getTransportHandler());
			throw new TransformationException("No transport handler "
					+ operator.getTransportHandler() + " found.");
		}
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ISink<?> senderPO = new SenderPO(protocolHandler);
		if (!config.isVirtualTransformation()) {
			getDataDictionary().putSinkplan(senderPOName, senderPO);
			if (!getDataDictionary().containsSink(senderPOName, getCaller())) {
				getDataDictionary()
						.addSink(senderPOName, operator, getCaller());
			}
		}
		defaultExecute(operator, senderPO, config, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(AbstractSenderAO operator,
			TransformationConfiguration config) {
		if (operator.isAllPhysicalInputSet()) {
			if (getDataDictionary().getSinkplan(operator.getSinkname()) == null) {
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
	private ITransportHandler getTransportHandler(AbstractSenderAO operator,
			IProtocolHandler<?> protocolHandler) {
		ITransportHandler transportHandler = null;
		if (operator.getTransportHandler() != null) {
			transportHandler = TransportHandlerRegistry.getInstance(
					operator.getTransportHandler(), protocolHandler,
					operator.getOptionsMap());
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
	private IProtocolHandler<?> getProtocolHandler(AbstractSenderAO operator,
			IDataHandler<?> dataHandler) {
		IProtocolHandler<?> protocolHandler = null;
		if (operator.getProtocolHandler() != null) {
			if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
				protocolHandler = ProtocolHandlerRegistry.getInstance(
						operator.getProtocolHandler(), ITransportDirection.OUT,
						IAccessPattern.PULL, operator.getOptionsMap(),
						dataHandler);
			} else {
				protocolHandler = ProtocolHandlerRegistry.getInstance(
						operator.getProtocolHandler(), ITransportDirection.OUT,
						IAccessPattern.PUSH, operator.getOptionsMap(),
						dataHandler);
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
	private IDataHandler<?> getDataHandler(AbstractSenderAO operator) {
		IDataHandler<?> dataHandler = null;
		if (operator.getDataHandler() != null) {
			if (operator.getOutputSchema() != null) {
				dataHandler = DataHandlerRegistry.getDataHandler(
						operator.getDataHandler(), operator.getOutputSchema());
			}

		}
		return dataHandler;
	}

}
