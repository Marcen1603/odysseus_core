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
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This rule handles all generic access operator implementations
 * 
 * @author Marco Grawunder
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TAccessAORule extends AbstractTransformationRule<AbstractAccessAO> {
	static Logger LOG = LoggerFactory.getLogger(TAccessAORule.class);

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(AbstractAccessAO operator,
			TransformationConfiguration config) throws RuleException {

		if (!hasTimestampAOAsFather(operator)) {
			insertTimestampAO(operator, operator.getDateFormat());
		}

		ISource accessPO = null;

		if (!config.isVirtualTransformation()) {
			accessPO = getDataDictionary().getAccessAO(
					operator.getAccessAOName());
		}

		if (accessPO == null) {

			OptionMap options = new OptionMap(operator.getOptionsMap());
			
			if (operator.getTransportHandler() != null) {
				IDataHandler<?> dataHandler = getDataHandler(operator);
				if (dataHandler == null) {
					LOG.error("No data handler {} found.",
							operator.getDataHandler());
					throw new TransformationException("No data handler "
							+ operator.getDataHandler() + " found.");
				}

				IProtocolHandler<?> protocolHandler = getProtocolHandler(
						operator, dataHandler, options);
				if (protocolHandler == null) {
					LOG.error("No protocol handler {} found.",
							operator.getProtocolHandler());
					throw new TransformationException("No protocol handler "
							+ operator.getProtocolHandler() + " found.");
				}

				ITransportHandler transportHandler = getTransportHandler(
						operator, protocolHandler, options);
				if (transportHandler == null) {
					LOG.error("No transport handler {} found.",
							operator.getTransportHandler());
					throw new TransformationException("No transport handler "
							+ operator.getTransportHandler() + " found.");
				}
				
			
				// In some cases the transport handler needs to know the schema
				if (dataHandler != null){
					transportHandler.setSchema(dataHandler.getSchema());
				}

				if (Constants.GENERIC_PULL.equalsIgnoreCase(operator
						.getWrapper())) {
					accessPO = new AccessPO(protocolHandler, operator.getMaxTimeToWaitForNewEventMS());
				} else {
					accessPO = new ReceiverPO(protocolHandler);
				}

			} else {
				throw new IllegalArgumentException(
						"This kind of access operator is no longer supported!");
			}
			if (options.containsKey("scheduler.delay")) {
				if (accessPO instanceof IIterableSource) {
					((IIterableSource) accessPO).setDelay(Long
							.parseLong(operator.getOptionsMap().get(
									"scheduler.delay")));
				}
			}
			if (options.containsKey("scheduler.yieldrate")) {
				if (accessPO instanceof IIterableSource) {
					((IIterableSource) accessPO).setYieldRate(Integer
							.parseInt(operator.getOptionsMap().get(
									"scheduler.yieldrate")));
				}
			}
			if (options.containsKey("scheduler.yieldnanos")) {
				if (accessPO instanceof IIterableSource) {
					((IIterableSource) accessPO).setYieldDurationNanos(Integer
							.parseInt(operator.getOptionsMap().get(
									"scheduler.yieldnanos")));
				}
			}
			if (!config.isVirtualTransformation()) {
				getDataDictionary().putAccessAO(operator.getAccessAOName(),
						accessPO);
			}
			List<String> unusedOptions = options.getUnreadOptions();
			LOG.warn("The following options where not used in translation "+unusedOptions);

		}else{
			if (operator.getWrapper() != null){
				//throw new TransformationException("Multiple definiton of source with name "+operator.getAccessAOName());
				InfoService.warning("Potential problem? Multiple definition of source with name "+operator.getAccessAOName());
			}
		}
		defaultExecute(operator, accessPO, config, true, true);
	}

	@Override
	public boolean isExecutable(AbstractAccessAO operator,
			TransformationConfiguration config) {
		if (operator.getWrapper() != null) {
			if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
				return true;
			}
			if (Constants.GENERIC_PUSH.equalsIgnoreCase(operator.getWrapper())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO (generic) --> AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<? super AbstractAccessAO> getConditionClass() {
		return AbstractAccessAO.class;
	}

	private ITransportHandler getTransportHandler(AbstractAccessAO operator,
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		ITransportHandler transportHandler = null;
		if (operator.getTransportHandler() != null) {
			transportHandler = TransportHandlerRegistry.getInstance(
					operator.getTransportHandler(), protocolHandler,
					options);
		}
		return transportHandler;
	}

	private IProtocolHandler<?> getProtocolHandler(AbstractAccessAO operator,
			IDataHandler<?> dataHandler, OptionMap options) {
		IProtocolHandler<?> protocolHandler = null;
		if (operator.getProtocolHandler() != null) {
			if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
				protocolHandler = ProtocolHandlerRegistry.getInstance(
						operator.getProtocolHandler(), ITransportDirection.IN,
						IAccessPattern.PULL, options,
						dataHandler);
			} else {
				protocolHandler = ProtocolHandlerRegistry.getInstance(
						operator.getProtocolHandler(), ITransportDirection.IN,
						IAccessPattern.PUSH, options,
						dataHandler);
			}
		}
		return protocolHandler;
	}

	private IDataHandler<?> getDataHandler(AbstractAccessAO operator) {
		IDataHandler<?> dataHandler = null;
		if (operator.getDataHandler() != null) {
			if (operator.getInputSchema() != null) {
				dataHandler = DataHandlerRegistry.getDataHandler(
						operator.getDataHandler(), operator.getInputSchema());
			} else if (operator.getOutputSchema() != null) {
				dataHandler = DataHandlerRegistry.getDataHandler(
						operator.getDataHandler(), operator.getOutputSchema());
			} else {
				dataHandler = DataHandlerRegistry.getDataHandler(
						operator.getDataHandler(), operator.getOutputSchema());
			}
		}

		return dataHandler;
	}

}
