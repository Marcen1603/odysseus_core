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
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFTimeUnit;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AccessAOSourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

abstract public class AbstractAccessAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5423444612698319659L;

	private Resource accessAOResource;
	private String wrapper;
	private String dataHandler;
	private String protocolHandler;
	private String transportHandler;
	final private Map<String, String> optionsMap = new HashMap<>();

	private String dateFormat;
	private List<SDFAttribute> attributes;
	private List<String> inputSchema = null;
	private long maxTimeToWaitForNewEventMS;

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
		accessAOResource = po.accessAOResource;
		if (po.attributes != null) {
			this.attributes = new ArrayList<>(po.attributes);
		}
		this.maxTimeToWaitForNewEventMS = po.maxTimeToWaitForNewEventMS;
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

	@Parameter(type = AccessAOSourceParameter.class, name = "source", optional = false, doc = "The name of the sourcetype to create.")
	public void setAccessAOName(Resource name) {
		super.setName(name.getResourceName());
		this.accessAOResource = name;
	}

	public Resource getAccessAOName() {
		return accessAOResource;
	}

	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}

	public String getWrapper() {
		return wrapper;
	}

	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}

	public List<String> getDataHandlerValues() {
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

	@Parameter(type = LongParameter.class, name = "MaxTimeToWaitForNewEventMS", optional = true, isList = false, doc = "For access. Max time to wait for a new element before calling done. Typically used when the input stream has an end")
	public void setMaxTimeToWaitForNewEventMS(long maxTimeToWaitForNewEventMS) {
		this.maxTimeToWaitForNewEventMS = maxTimeToWaitForNewEventMS;
	}
	
	public long getMaxTimeToWaitForNewEventMS() {
		return maxTimeToWaitForNewEventMS;
	}
	
	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	public void setOptions(List<Option> value) {
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

	@Parameter(type = StringParameter.class, name = "inputSchema", isList = true, optional = true, possibleValues = "__DD_DATATYPES", possibleValuesAreDynamic = true, doc = "A list of data types describing the input format. Must be compatible with output schema!")
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
		Map<String, SDFConstraint> constraints = new HashMap<>();
		
		@SuppressWarnings("rawtypes")
		Class<? extends IStreamObject> type = DataHandlerRegistry
				.getCreatedType(dataHandler);
		if (type == null) {
			type = Tuple.class;
		}
		
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		
		if (optionsMap.containsKey(SDFConstraint.BASE_TIME_UNIT)){
			String unit = optionsMap.get(SDFConstraint.BASE_TIME_UNIT);
			TimeUnit btu = TimeUnit.valueOf(unit);
			if (btu != null){
				timeUnit = btu;
			}else{
				throw new IllegalParameterException("Illegal time unit "+unit);
			}
		}
		constraints.put(SDFConstraint.BASE_TIME_UNIT, new SDFConstraint(SDFConstraint.BASE_TIME_UNIT,timeUnit));

		if (attributes != null && attributes.size() > 0) {
			List<SDFAttribute> s2 = new ArrayList<>();
			// Add source name to attributes
			for (SDFAttribute a : attributes) {
				SDFAttribute newAttr;
				if (a.getDatatype() == SDFDatatype.START_TIMESTAMP || a.getDatatype() == SDFDatatype.START_TIMESTAMP_STRING){
					List<SDFConstraint> c = new ArrayList<>();
					c.add(new SDFConstraint(SDFConstraint.BASE_TIME_UNIT, timeUnit));
					SDFUnit unit = new SDFTimeUnit(timeUnit.toString());
					newAttr = new SDFAttribute(getName(), a.getAttributeName(), a, unit, c);
				}else{
					newAttr = new SDFAttribute(getName(), a.getAttributeName(), a);
				}
				s2.add(newAttr);
			}
			
			schema = new SDFSchema(getName(), type, constraints, s2);
		} else {
			schema = new SDFSchema(getName(), type, constraints, null);
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

		if (accessAOResource.isMarked() && this.attributes != null){
			addError(new IllegalArgumentException("Source "+accessAOResource+" already defined!"));
			return false;
		}
		
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

	protected String buildString(List<String> elements, String sep) {
		StringBuffer v = new StringBuffer();
		for (String s : elements) {
			v.append(s).append(sep);
		}
		return v.substring(0, v.length() - 1);
	}

	@Override
	public boolean isSinkOperator() {
		return false;
	}
}
