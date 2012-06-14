/** Copyright [2011] [The Odysseus Team]
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

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.USAGE;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

/**
 * This Operatorbuilder is used to create a new AccessAO.
 * 
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public class AccessAOBuilder extends AbstractOperatorBuilder {

	static Logger logger = LoggerFactory.getLogger(AccessAOBuilder.class);

	private static final long serialVersionUID = 2682090172449918821L;
	
	private final IntegerParameter port = new IntegerParameter("PORT",
			REQUIREMENT.OPTIONAL, USAGE.DEPRECATED);
	private final StringParameter type = new StringParameter("TYPE",
			REQUIREMENT.OPTIONAL, USAGE.DEPRECATED);
	private final StringParameter host = new StringParameter("HOST",
			REQUIREMENT.OPTIONAL, USAGE.DEPRECATED);
	private final ListParameter<SDFAttribute> attributes = new ListParameter<SDFAttribute>(
			"SCHEMA", REQUIREMENT.OPTIONAL, new CreateSDFAttributeParameter(
					"ATTRIBUTE", REQUIREMENT.MANDATORY, getDataDictionary()));
	private final ListParameter<String> inputSchema = new ListParameter<String>(
			"INPUTSCHEMA", REQUIREMENT.OPTIONAL, new StringParameter());
	private final ListParameter<String> options = new ListParameter<String>(
			"OPTIONS", REQUIREMENT.OPTIONAL, new StringParameter());
	private final StringParameter adapter = new StringParameter("ADAPTER",
			REQUIREMENT.OPTIONAL, USAGE.DEPRECATED);
	private final StringParameter input = new StringParameter("INPUT",
			REQUIREMENT.OPTIONAL, USAGE.DEPRECATED);
	private final StringParameter transformer = new StringParameter(
			"transformer", REQUIREMENT.OPTIONAL, USAGE.DEPRECATED);

	private final StringParameter accessConnectionHandler = new StringParameter(
			"AccessConnectionHandler", REQUIREMENT.OPTIONAL, USAGE.DEPRECATED);

	// TODO: These should be the only parameter in future
	// Make mandatory
	private final StringParameter wrapper = new StringParameter("WRAPPER",
			REQUIREMENT.OPTIONAL);
	private final StringParameter sourceName = new StringParameter("SOURCE",
			REQUIREMENT.MANDATORY);
	private final StringParameter dataHandler = new StringParameter(
			"DATAHANDLER", REQUIREMENT.OPTIONAL);
	private final StringParameter objectHandler = new StringParameter(
			"OBJECTHANDLER", REQUIREMENT.OPTIONAL);
	private final StringParameter inputDataHandler = new StringParameter(
			"InputDataHandler", REQUIREMENT.OPTIONAL);
	private final StringParameter transportHandler = new StringParameter(
			"transport", REQUIREMENT.OPTIONAL);
	private final StringParameter protocolHandler = new StringParameter(
			"protocol", REQUIREMENT.OPTIONAL);

	public AccessAOBuilder() {
		super(0, 0);
		addParameters(sourceName, host, port, attributes, type, options,
				inputSchema, adapter, input, transformer, dataHandler,
				objectHandler, inputDataHandler, accessConnectionHandler,
				transportHandler, protocolHandler, wrapper);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		String sourceName = this.sourceName.getValue();
		if (getDataDictionary().containsViewOrStream(sourceName, getCaller())) {
			try {
				return getDataDictionary().getViewOrStream(sourceName,
						getCaller());
			} catch (DataDictionaryException e) {
				throw new QueryParseException(e.getMessage());
			}
		}
		ILogicalOperator ao = createNewAccessAO(sourceName);

		return ao;
	}

	private ILogicalOperator createNewAccessAO(String sourceName) {

		String wrapperName = adapter.hasValue() ? adapter.getValue() : type
				.getValue();
		wrapperName = wrapper.hasValue() ? wrapper.getValue():wrapperName;
		
		SDFSchema schema = new SDFSchema(sourceName, attributes.getValue());
		HashMap<String, String> optionsMap = new HashMap<String, String>();

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
		getDataDictionary().addSourceType(sourceName, "RelationalStreaming");
		getDataDictionary().addEntitySchema(sourceName, schema, getCaller());

		AccessAO ao = new AccessAO(sourceName, wrapperName, optionsMap);
		ao.setOutputSchema(schema);

		if (host.hasValue()) {
			ao.setHost(host.getValue());
		}
		if (port.hasValue()) {
			ao.setPort(port.getValue());
		}
		if (inputSchema.hasValue()) {
			ao.setInputSchema(inputSchema.getValue());
		}
		if (input.hasValue()) {
			ao.setInput(input.getValue());
		}
		if (transformer.hasValue()) {
			ao.setTransformer(transformer.getValue());
		}
		if (dataHandler.hasValue()) {
			ao.setDataHandler(dataHandler.getValue());
		}
		if (objectHandler.hasValue()) {
			ao.setObjectHandler(objectHandler.getValue());
		}
		if (inputDataHandler.hasValue()) {
			ao.setInputDataHandler(inputDataHandler.getValue());
		}
		if (accessConnectionHandler.hasValue()) {
			ao.setAccessConnectionHandler(accessConnectionHandler.getValue());
		}
		if (transportHandler.hasValue()) {
			ao.setTransportHandler(transportHandler.getValue());
		}
		if (protocolHandler.hasValue()) {
			ao.setProtocolHandler(protocolHandler.getValue());
		}

		ILogicalOperator op = addTimestampAO(ao);
		return op;
	}

	private static ILogicalOperator addTimestampAO(ILogicalOperator operator) {
		TimestampAO timestampAO = new TimestampAO();
		for (SDFAttribute attr : operator.getOutputSchema()) {
			if (SDFDatatype.START_TIMESTAMP.toString().equalsIgnoreCase(
					attr.getDatatype().getURI())) {
				timestampAO.setStartTimestamp(attr);
			}

			if (SDFDatatype.END_TIMESTAMP.toString().equalsIgnoreCase(
					attr.getDatatype().getURI())) {
				timestampAO.setEndTimestamp(attr);
			}
		}

		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		return timestampAO;
	}

	@Override
	protected boolean internalValidation() {
		String sourceName = this.sourceName.getValue();

		if (getDataDictionary().containsViewOrStream(sourceName, getCaller())) {
			if (host.hasValue() || type.hasValue() || port.hasValue()
					|| attributes.hasValue() || options.hasValue()
					|| inputSchema.hasValue() || adapter.hasValue()
					|| input.hasValue() || transformer.hasValue()
					|| dataHandler.hasValue() || objectHandler.hasValue()
					|| inputDataHandler.hasValue()
					|| accessConnectionHandler.hasValue()
					|| transportHandler.hasValue()
					|| protocolHandler.hasValue()) {
				addError(new IllegalArgumentException("view " + sourceName
						+ " already exists. Use one only parameter source for an existing source."));
				return false;
			}
		} else {
			if (type.hasValue() && adapter.hasValue() && wrapper.hasValue()) {
				addError(new IllegalArgumentException(
						"to much information for the creation of source "
								+ sourceName + ". expecting wrapper OR type OR adapter."));
				return false;
			}
			if (!type.hasValue() && !adapter.hasValue() && !wrapper.hasValue()) {
				addError(new IllegalArgumentException(
						"to less information for the creation of source "
								+ sourceName + ". expecting wrapper, type or adapter."));
				return false;
			}
		}

		if (this.attributes.hasValue() && this.inputSchema.hasValue()) {
			if (this.attributes.getValue().size() != this.inputSchema
					.getValue().size()) {
				addError(new IllegalArgumentException(
						"For each attribute there must be at least one reader in the input schema"));
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
		((CreateSDFAttributeParameter) ((attributes).getSingleParameter()))
				.setDataDictionary(dataDictionary);
		super.setDataDictionary(dataDictionary);
	}
}
