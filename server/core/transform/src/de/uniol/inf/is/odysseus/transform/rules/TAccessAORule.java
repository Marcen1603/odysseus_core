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

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.engine.TransformationExecutor;
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
	static InfoService infoService = InfoServiceFactory.getInfoService(TAccessAORule.class);

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(AbstractAccessAO operator, TransformationConfiguration config) throws RuleException {

		ISource<?> inputPO = null;

		if (!config.isVirtualTransformation()) {
			inputPO = getDataDictionary().getAccessPO(operator.getAccessAOName());
		}

		IAccessAO other = getDataDictionary().getAccessAO(operator.getAccessAOName(), getCaller());
		if (other != null && !other.isSemanticallyEqual(operator)){
			throw new TransformationException("Duplicate AccessOperator with name "+operator.getAccessAOName()+"!");
		}

		if (inputPO == null) {
			inputPO = createInputPO(operator, config);
		} else {

			// Find cases where accessao is used with wrong name (--> same name but different parameters)

			if (!config.hasOption("NO_METADATA")) {
				Class<? extends IMetaAttribute>[] opMT = operator.getLocalMetaAttribute().getClasses();
				List<String> acMT = inputPO.getOutputSchema().getMetaAttributeNames();

				if (!MetadataRegistry.isSame(opMT, acMT)) {
					throw new TransformationException(
							"The source " + operator.getName() + " is already defined with meta data " + acMT);
				}
			}
		}
		processMetaData(operator, config, inputPO);

		defaultExecute(operator, inputPO, config, true, true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ISource<?> createInputPO(AbstractAccessAO operator, TransformationConfiguration config) {
		ISource<?> inputPO;
		if (operator.getTransportHandler() == null) {
			throw new IllegalArgumentException("This kind of access operator is no longer supported!");
		}

		OptionMap options = new OptionMap(operator.getOptionsMap());

		IStreamObjectDataHandler<?> dataHandler = processDataHandler(operator);

		IProtocolHandler<?> protocolHandler = processProtocolHandler(operator, config, options, dataHandler);

		processTransportHandler(operator, config, options, dataHandler, protocolHandler);

		if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
			inputPO = new AccessPO(protocolHandler, operator.getMaxTimeToWaitForNewEventMS(), operator.readMetaData());
		} else {
			inputPO = new ReceiverPO(protocolHandler, operator.readMetaData());
		}

		processOptions(inputPO, options);
		if (!config.isVirtualTransformation()) {
			putAccessOps(operator, inputPO);
		}
		List<String> unusedOptions = options.getUnreadOptions();
		if (unusedOptions.size() > 0) {
			infoService.warning("The following options where not used in translation " + unusedOptions);
		}
		return inputPO;
	}

	private void processMetaData(AbstractAccessAO operator, TransformationConfiguration config, ISource<?> inputPO) {
		if (inputPO instanceof IMetadataInitializer) {
			// New: do no create meta data creation and update, if operator
			// already read the meta data from the source
			if (!config.hasOption("NO_METADATA") && !operator.readMetaData()) {

				IMetaAttribute type = operator.getLocalMetaAttribute();
				if (type == null) {
					type = MetadataRegistry.getMetadataType(config.getDefaultMetaTypeSet());
				}
				((IMetadataInitializer<?, ?>) inputPO).setMetadataType(type);

				TimestampAO tsAO = getTimestampAOAsFather(operator);
				Class<? extends IMetaAttribute> toC = ITimeInterval.class;
				if (MetadataRegistry.contains(type.getClasses(), toC) && tsAO == null) {
					tsAO = insertTimestampAO(operator, operator.getDateFormat());
				}

			} else {
				if (config.hasOption("NO_METADATA")) {
					((IMetadataInitializer<?, ?>) inputPO).setMetadataType(null);
				}
			}

		} else {
			TimestampAO tsAO = getTimestampAOAsFather(operator);
			if (tsAO == null) {
				tsAO = insertTimestampAO(operator, operator.getDateFormat());
			}
		}
	}



	private void processTransportHandler(AbstractAccessAO operator, TransformationConfiguration config,
			OptionMap options, IStreamObjectDataHandler<?> dataHandler, IProtocolHandler<?> protocolHandler) {
		if (!operator.getTransportHandler().equalsIgnoreCase("NONE")) {
			ITransportHandler transportHandler = getTransportHandler(operator, protocolHandler, options);
			if (transportHandler == null) {
				LOG.error("No transport handler {} found.", operator.getTransportHandler());
				throw new TransformationException("No transport handler " + operator.getTransportHandler() + " found.");
			}

			transportHandler.setExecutor((IExecutor) config.getOption(IServerExecutor.class.getName()));

			// if the transport handler provides a schema this must be used
			// instead of the user defined schema
			// In some cases the transport handler needs to know the
			// schema
			if (dataHandler != null && transportHandler.getSchema() == null) {
				transportHandler.setSchema(dataHandler.getSchema());
			}

		}
	}

	private IProtocolHandler<?> processProtocolHandler(AbstractAccessAO operator, TransformationConfiguration config,
			OptionMap options, IStreamObjectDataHandler<?> dataHandler) {
		IProtocolHandler<?> protocolHandler = getProtocolHandler(operator, dataHandler, options);
		if (protocolHandler == null) {
			LOG.error("No protocol handler {} found.", operator.getProtocolHandler());
			throw new TransformationException("No protocol handler " + operator.getProtocolHandler() + " found.");
		}
		protocolHandler.setExecutor((IExecutor) config.getOption(IServerExecutor.class.getName()));

		if (dataHandler != null) {
			protocolHandler.setSchema(dataHandler.getSchema());
		}
		return protocolHandler;
	}

	private IStreamObjectDataHandler<?> processDataHandler(AbstractAccessAO operator) {
		IStreamObjectDataHandler<?> dataHandler = getDataHandler(operator);
		if (dataHandler == null) {
			LOG.error("No data handler {} found.", operator.getDataHandler());
			throw new TransformationException("No data handler " + operator.getDataHandler() + " found.");
		}

		if (operator.readMetaData()) {
			IMetaAttribute metaAttribute = MetadataRegistry
					.getMetadataType(operator.getOutputSchema().getMetaAttributeNames());
			dataHandler.setMetaAttribute(metaAttribute);
		}
		return dataHandler;
	}

	private void processOptions(ISource<?> inputPO, OptionMap options) {
		if (options.containsKey("scheduler.delay")) {
			if (inputPO instanceof IIterableSource) {
				((IIterableSource<?>) inputPO).setDelay(options.getLong("scheduler.delay", -1));
			}
		}
		if (options.containsKey("scheduler.yieldrate")) {
			if (inputPO instanceof IIterableSource) {
				((IIterableSource<?>) inputPO).setYieldRate(options.getInt("scheduler.yieldrate", -1));
			}
		}
		if (options.containsKey("scheduler.yieldnanos")) {
			if (inputPO instanceof IIterableSource) {
				((IIterableSource<?>) inputPO).setYieldDurationNanos(options.getInt("scheduler.yieldnanos", -1));
			}
		}
	}

	@Override
	public boolean isExecutable(AbstractAccessAO operator, TransformationConfiguration config) {
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

	private ITransportHandler getTransportHandler(AbstractAccessAO operator, IProtocolHandler<?> protocolHandler,
			OptionMap options) {
		ITransportHandler transportHandler = null;
		if (operator.getTransportHandler() != null) {
			transportHandler = TransformationExecutor.getTransportHandlerRegistry().getInstance(operator.getTransportHandler(), protocolHandler,
					options);
			if (transportHandler == null){
				throw new TransformationException("Cannot create transport handler "+operator.getTransportHandler());
			}
			if (transportHandler.getSchema() != null){
				operator.setOverWrittenSchema(transportHandler.getSchema());
			}
		}
		return transportHandler;
	}

	private IProtocolHandler<?> getProtocolHandler(AbstractAccessAO operator, IStreamObjectDataHandler<?> dataHandler,
			OptionMap options) {
		IProtocolHandler<?> protocolHandler = null;
		if (operator.getProtocolHandler() != null) {
			if (Constants.GENERIC_PULL.equalsIgnoreCase(operator.getWrapper())) {
				protocolHandler = getDataDictionary().getProtocolHandlerRegistry(getCaller()).getInstance(operator.getProtocolHandler(),
						ITransportDirection.IN, IAccessPattern.PULL, options, dataHandler);
			} else {
				protocolHandler = getDataDictionary().getProtocolHandlerRegistry(getCaller()).getInstance(operator.getProtocolHandler(),
						ITransportDirection.IN, IAccessPattern.PUSH, options, dataHandler);
			}
			if (protocolHandler != null && protocolHandler.getSchema() != null){
				operator.setOverWrittenSchema(protocolHandler.getSchema());
			}
		}
		return protocolHandler;
	}

	private IStreamObjectDataHandler<?> getDataHandler(AbstractAccessAO operator) {
		IDataHandler<?> dataHandler = null;
		if (operator.getDataHandler() != null) {
			if (operator.getInputSchema() != null) {
				List<SDFDatatype> dtList = new LinkedList<SDFDatatype>();
				for (String dt : operator.getInputSchema()) {
					dtList.add(getDataDictionary().getDatatype(dt));
				}
				dataHandler = getDataDictionary().getDataHandlerRegistry(getCaller()).getDataHandler(operator.getDataHandler(), dtList);
			} else if (operator.getOutputSchema() != null) {
				dataHandler = getDataDictionary().getDataHandlerRegistry(getCaller()).getDataHandler(operator.getDataHandler(), operator.getOutputSchema());
			} else {
				dataHandler = getDataDictionary().getDataHandlerRegistry(getCaller()).getDataHandler(operator.getDataHandler(), operator.getOutputSchema());
			}
		}
		if (dataHandler != null) {
			if (!(dataHandler instanceof IStreamObjectDataHandler)) {
				throw new IllegalArgumentException("DataHandler " + operator.getDataHandler() + " cannot be used!");
			}
			return (IStreamObjectDataHandler<?>) dataHandler;
		}
		return null;
	}

}
