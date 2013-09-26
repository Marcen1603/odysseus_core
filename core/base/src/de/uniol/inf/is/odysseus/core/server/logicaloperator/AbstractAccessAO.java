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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

abstract public class AbstractAccessAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5423444612698319659L;

	private Resource accessAOName;
	private String wrapper;
	private String dataHandler;
	private String protocolHandler;
	private String transportHandler;
	final private Map<String, String> optionsMap = new HashMap<>();

	private String dateFormat;
	private List<SDFAttribute> attributes;
	private List<String> inputSchema = null;

	public AbstractAccessAO(AbstractLogicalOperator po) {
		super(po);
	}

	public AbstractAccessAO() {
		super();
	}

	public AbstractAccessAO(AbstractAccessAO po) {
		super(po);
		wrapper = po.wrapper;
		optionsMap.putAll(po.optionsMap);
		inputSchema = po.inputSchema;
		dataHandler = po.dataHandler;
		protocolHandler = po.protocolHandler;
		transportHandler = po.transportHandler;
		accessAOName = po.accessAOName;
		if (po.attributes != null) {
			this.attributes = new ArrayList<>(po.attributes);
		}
	}

	public AbstractAccessAO(Resource name, String wrapper,
			String transportHandler, String protocolHandler,
			String dataHandler, Map<String, String> optionsMap) {
		setAccessAOName(name);
		this.wrapper = wrapper;
		this.transportHandler = transportHandler;
		this.protocolHandler = protocolHandler;
		this.dataHandler = dataHandler;
		this.optionsMap.putAll(optionsMap);
	}

	@Parameter(type = ResourceParameter.class, name = "source", optional = false, doc = "The name of the sourcetype to create.")
	public void setAccessAOName(Resource name) {
		super.setName(name.getResourceName());
		this.accessAOName = name;
	}

	public Resource getAccessAOName() {
		return accessAOName;
	}

	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}

	public String getWrapper() {
		return wrapper;
	}

	@Parameter(type = StringParameter.class, name = "DataHandler", optional = true, possibleValues="getDataHandlerValues", doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}

	public List<String> getDataHandlerValues(){
		return DataHandlerRegistry.getStreamableDataHandlerNames();
	}
	
	public String getDataHandler() {
		return dataHandler;
	}

	public void setTransportHandler(String transportHandler) {
		this.transportHandler = transportHandler;
	}

	public String getTransportHandler() {
		return transportHandler;
	}

	public void setProtocolHandler(String protocolHandler) {
		this.protocolHandler = protocolHandler;
	}

	public String getProtocolHandler() {
		return protocolHandler;
	}

	public void setOptions(List<Option> value) {
		this.optionsMap.clear();
		for (Option option : value) {
			optionsMap.put(option.getName().toLowerCase(), option.getValue());
		}
	}

	protected void addOption(String key, String value) {
		optionsMap.put(key, value);
	}

	public void setOptionMap(Map<String, String> options) {
		this.optionsMap.clear();
		this.optionsMap.putAll(options);
	}

	public Map<String, String> getOptionsMap() {
		return optionsMap;
	}

	@Parameter(type = StringParameter.class, name = "inputSchema", isList = true, optional = true, doc = "A list of data types describing the input format. Must be compatible with output schema!")
	public void setInputSchema(List<String> inputSchema) {
		this.inputSchema = inputSchema;
	}

	public List<String> getInputSchema() {
		return inputSchema;
	}

	@Parameter(type = StringParameter.class, name = "dateFormat", optional = true, doc = "The date format used.")
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema", isList = true, optional = true, doc = "The output schema.")
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema schema = null;

		@SuppressWarnings("rawtypes")
		Class<? extends IStreamObject> type = DataHandlerRegistry
				.getCreatedType(dataHandler);
		if (type == null) {
			type = Tuple.class;
		}

		if (attributes != null && attributes.size() > 0) {
			List<SDFAttribute> s2 = new ArrayList<>();
			// Add source name to attributes
			for (SDFAttribute a : attributes) {
				s2.add(new SDFAttribute(getName(), a.getAttributeName(), a));
			}

			schema = new SDFSchema(getName(), type, s2);
		} else {
			schema = new SDFSchema(getName(), type, null);
		}

		return schema;
	}

	@Override
	public String toString() {
		return getName() + " (" + this.wrapper + ")";
	}

	@Override
	public boolean isAllPhysicalInputSet() {
		return true;
	}

	@Override
	public boolean isValid() {

		if (this.inputSchema != null) {
			if (this.attributes.size() != this.inputSchema.size()) {
				addError(new IllegalArgumentException(
						"For each attribute there must be at least one reader in the input schema"));
				return false;
			}
		}

		if (!WrapperRegistry.containsWrapper(this.wrapper)) {
			addError(new IllegalParameterException("Wrapper " + this.wrapper
					+ " is unknown"));
			return false;
		}

		return true;
	}
	
}
