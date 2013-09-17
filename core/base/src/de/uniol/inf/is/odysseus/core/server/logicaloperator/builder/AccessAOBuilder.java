/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

/**
 * This Operatorbuilder is used to create a new AccessAO.
 * 
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public class AccessAOBuilder extends AbstractOperatorBuilder {

	static Logger logger = LoggerFactory.getLogger(AccessAOBuilder.class);

	private static final long serialVersionUID = 2682090172449918821L;

	private final StringParameter source = new StringParameter("source",
			REQUIREMENT.MANDATORY);
	private final StringParameter wrapper = new StringParameter("WRAPPER",
			REQUIREMENT.MANDATORY);
	private final StringParameter dataHandler = new StringParameter(
			"DATAHANDLER", REQUIREMENT.OPTIONAL);

	private final StringParameter transportHandler = new StringParameter(
			"transport", REQUIREMENT.OPTIONAL);
	private final StringParameter protocolHandler = new StringParameter(
			"protocol", REQUIREMENT.OPTIONAL);
	private final ListParameter<String> options = new ListParameter<String>(
			"OPTIONS_OLD", REQUIREMENT.OPTIONAL, new StringParameter());
	private final ListParameter<Option> options2 = new ListParameter<Option>(
			"OPTIONS", REQUIREMENT.OPTIONAL, new OptionParameter("OPTION",
					REQUIREMENT.MANDATORY));
	private final ListParameter<String> inputSchema = new ListParameter<String>(
			"INPUTSCHEMA", REQUIREMENT.OPTIONAL, new StringParameter());
	private final StringParameter dateFormat = new StringParameter(
			"dateFormat", REQUIREMENT.OPTIONAL);
	private final ListParameter<SDFAttribute> outputschema = new ListParameter<SDFAttribute>(
			"SCHEMA", REQUIREMENT.OPTIONAL, new CreateSDFAttributeParameter(
					"ATTRIBUTE", REQUIREMENT.MANDATORY));

	public AccessAOBuilder() {
		super("ACCESS", 0, 0);

		addParameters(source, outputschema,  options,
				options2, inputSchema, dataHandler, transportHandler, protocolHandler,
				wrapper, dateFormat);
		protocolHandler.setPossibleValues(ProtocolHandlerRegistry
				.getHandlerNames());
		transportHandler.setPossibleValues(TransportHandlerRegistry
				.getHandlerNames());
		dataHandler.setPossibleValues(DataHandlerRegistry.getHandlerNames());
		wrapper.setPossibleValues(WrapperRegistry.getWrapperNames());

	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected ILogicalOperator createOperatorInternal() {

		String wrapperName = wrapper.getValue();

		HashMap<String, String> optionsMap = new HashMap<String, String>();

		String sourcename = source.getValue();

		if (options.hasValue()) {
			for (final String item : options.getValue()) {
				final String[] option = item.split(":");
				if (option.length == 2) {
					optionsMap.put(option[0].toLowerCase(),
							item.substring(option[0].length() + 1));
				} else {
					optionsMap.put(option[0].toLowerCase(), "");
				}
			}
		}

		if (options2.hasValue()) {
			for (Option option : options2.getValue()) {
				optionsMap.put(option.getName().toLowerCase(),
						option.getValue());
			}
		}

		AccessAO ao = new AccessAO(wrapperName, optionsMap);
		ao.setName(sourcename);
		ao.setAccessAOName(new Resource(getCaller().getUser(), sourcename));

		String dhandlerText =  dataHandler.hasValue()? dataHandler.getValue():null;
		Class<? extends IStreamObject> type = DataHandlerRegistry.getCreatedType(dhandlerText);
		if (type == null){
			type = Tuple.class;
		}
		
		if (outputschema.hasValue()) {
			List<SDFAttribute> s2 = new ArrayList<>();
			// Add source name to attributes
			for (SDFAttribute a : outputschema.getValue()) {
				s2.add(new SDFAttribute(sourcename, a.getAttributeName(), a));
			}
			
			SDFSchema schema = new SDFSchema(sourcename, type, s2);
			ao.setOutputSchema(schema);
		}else{
			ao.setOutputSchema(new SDFSchema(sourcename,type,null));
		}

		if (inputSchema.hasValue()) {
			ao.setInputSchema(inputSchema.getValue());
		}
		if (dataHandler.hasValue()) {
			ao.setDataHandler(dataHandler.getValue());
		} else {
			ao.setDataHandler(null);
		}
		if (transportHandler.hasValue()) {
			ao.setTransportHandler(transportHandler.getValue());
		}
		if (protocolHandler.hasValue()) {
			ao.setProtocolHandler(protocolHandler.getValue());
		}

		String df = dateFormat.hasValue() ? dateFormat.getValue() : null;
		ao.setDateFormat(df);
		ILogicalOperator op = ao;
		return op;
	}


	@Override
	protected void insertParameterInfos(ILogicalOperator op) {
		// op is timestampao here (instead of accessao)
		if (!op.getSubscribedToSource().isEmpty()) {
			super.insertParameterInfos(op.getSubscribedToSource().iterator()
					.next().getTarget());
		}
	}

	@Override
	protected boolean internalValidation() {

		if (options.hasValue() && options2.hasValue()) {
			addError(new IllegalArgumentException(
					"Only one kind of options is allowed!"));
			return false;
		}

		if (this.outputschema.hasValue() && this.inputSchema.hasValue()) {
			if (this.outputschema.getValue().size() != this.inputSchema
					.getValue().size()) {
				addError(new IllegalArgumentException(
						"For each attribute there must be at least one reader in the input schema"));
				return false;
			}
		}

		if (this.wrapper.hasValue()) {
			if (!WrapperRegistry.containsWrapper(this.wrapper.getValue())) {
				addError(new IllegalParameterException("Wrapper "
						+ this.wrapper.getValue() + " is unknown"));
				return false;
			}
		}
		return true;
	}

	@Override
	public AccessAOBuilder cleanCopy() {
		return new AccessAOBuilder();
	}

	@Override
	public void setDataDictionary(IDataDictionary dataDictionary) {
		((CreateSDFAttributeParameter) ((outputschema).getSingleParameter()))
				.setDataDictionary(dataDictionary);
		super.setDataDictionary(dataDictionary);
	}

}
