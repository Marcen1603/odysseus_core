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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFTimeUnit;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.internal.RegistryBinder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AccessAOSourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.MetaAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

abstract public class AbstractAccessAO extends AbstractLogicalOperator implements IAccessAO {

	private static final long serialVersionUID = -5423444612698319659L;

	private Resource accessAOResource;
	private String wrapper;
	private String dataHandler;
	private String protocolHandler = "none";
	private String transportHandler;

	private final OptionMap optionsMap = new OptionMap();
	private List<Option> optionsList;

	private String dateFormat;
	private Map<Integer, List<SDFAttribute>> outputSchema = new HashMap<Integer, List<SDFAttribute>>();
	private List<String> inputSchema = null;
	private long maxTimeToWaitForNewEventMS;

	private IMetaAttribute localMetaAttribute;

	private boolean readMetaData;
	private boolean overWriteSchemaSourceName = true;
	private SDFSchema overWrittenSchema = null;
	
	// TODO: magic default values
	private long realTimeDelay = 1000;
	private long applicationTimeDelay = 900;

	public AbstractAccessAO(AbstractLogicalOperator po) {
		super(po);
	}

	public AbstractAccessAO() {
		super();
	}

	public AbstractAccessAO(AbstractAccessAO ao) {
		super(ao);
		wrapper = ao.wrapper;
		optionsMap.addAll(ao.optionsMap);
		if (ao.optionsList != null) {
			this.optionsList = new ArrayList<>(ao.optionsList);
		}
		inputSchema = ao.inputSchema;
		dataHandler = ao.dataHandler;
		protocolHandler = ao.protocolHandler;
		transportHandler = ao.transportHandler;
		accessAOResource = ao.accessAOResource;
		this.outputSchema.putAll(ao.outputSchema);
		this.maxTimeToWaitForNewEventMS = ao.maxTimeToWaitForNewEventMS;
		this.dateFormat = ao.dateFormat;
		this.localMetaAttribute = ao.localMetaAttribute;
		this.readMetaData = ao.readMetaData;
		this.overWriteSchemaSourceName = ao.overWriteSchemaSourceName;
		this.overWrittenSchema = ao.overWrittenSchema;
		this.realTimeDelay = ao.realTimeDelay;
		this.applicationTimeDelay = ao.applicationTimeDelay;
	}

	public AbstractAccessAO(Resource name, String wrapper, String transportHandler, String protocolHandler,
			String dataHandler, OptionMap optionsMap) {
		setAccessAOName(name);
		this.wrapper = wrapper;
		this.transportHandler = transportHandler;
		this.protocolHandler = protocolHandler;
		this.dataHandler = dataHandler;
		if (optionsMap != null) {
			this.optionsMap.addAll(optionsMap);
		}
	}

	@Parameter(type = AccessAOSourceParameter.class, name = "source", optional = false, doc = "The name of the sourcetype to create.")
	public void setAccessAOName(Resource name) {
		super.setName(name.getResourceName());
		this.accessAOResource = name;
	}

	@Override
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
		return RegistryBinder.getDataHandlerRegistry().getStreamableDataHandlerNames();
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

	public boolean isOverWriteSchemaSourceName() {
		return overWriteSchemaSourceName;
	}

	@Parameter(type = BooleanParameter.class, optional = true, isList = false, doc = "Output schema typically contains source name in attributes. Sometime this is not wanted. Set to false to avoid overwriting.")
	public void setOverWriteSchemaSourceName(boolean overWriteSchemaSourceName) {
		this.overWriteSchemaSourceName = overWriteSchemaSourceName;
	}

	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	public void setOptions(List<Option> value) {
		for (Option option : value) {
			optionsMap.setOption(option.getName().toLowerCase(), option.getValue());
		}
		optionsList = value;
	}

	public List<Option> getOptions() {
		return optionsList;
	}

	protected void addOption(String key, String value) {
		optionsMap.overwriteOption(key, value);
	}

	protected String getOption(String key) {
		return optionsMap.get(key);
	}

	public void setOptionMap(OptionMap options) {
		this.optionsMap.clear();
		this.optionsMap.addAll(options);
	}

	public OptionMap getOptionsMap() {
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
		this.outputSchema.put(0, attributes);
	}

	public List<SDFAttribute> getAttributes() {
		return outputSchema.get(0);
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema1", isList = true, optional = true, doc = "The output schema for port 1.")
	public void setAttributes1(List<SDFAttribute> attributes) {
		this.outputSchema.put(1, attributes);
	}

	public List<SDFAttribute> getAttributes1() {
		return outputSchema.get(1);
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema2", isList = true, optional = true, doc = "The output schema for port 2.")
	public void setAttributes2(List<SDFAttribute> attributes) {
		this.outputSchema.put(2, attributes);
	}

	public List<SDFAttribute> getAttributes2() {
		return outputSchema.get(2);
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema3", isList = true, optional = true, doc = "The output schema for port 3.")
	public void setAttributes3(List<SDFAttribute> attributes) {
		this.outputSchema.put(3, attributes);
	}

	public List<SDFAttribute> getAttributes3() {
		return outputSchema.get(3);
	}

	@Parameter(type = MetaAttributeParameter.class, name = "metaAttribute", isList = false, optional = true, possibleValues = "getMetadataTypes", doc = "If set, this value overwrites the meta data created from this source.")
	public void setLocalMetaAttribute(IMetaAttribute metaAttribute) {
		this.localMetaAttribute = metaAttribute;
	}

	public List<String> getMetadataTypes() {
		return new ArrayList<String>(MetadataRegistry.getNames());
	}

	public IMetaAttribute getLocalMetaAttribute() {
		return localMetaAttribute;
	}

	@Parameter(type = BooleanParameter.class, name = "readMetadata", optional = true, isList = false, doc = "If the source provides meta data, use this flag to enable reading of meta data.")
	public void setReadMetaData(boolean readMetaData) {
		this.readMetaData = readMetaData;
	}

	public boolean readMetaData() {
		return readMetaData;
	}
	
	
	@Parameter(type = BooleanParameter.class, name = "outOfOrder", optional = true, isList = false, doc = "The system needs to know if the input is ordered by timestamps. Set to true if this is not the case!")
	public void setOutOfOrder(boolean outOfOrder){
		optionsMap.setOption(SDFConstraint.STRICT_ORDER, !outOfOrder);
	}


	public long getRealTimeDelay() {
		return realTimeDelay;
	}
	
	@Parameter(type = LongParameter.class, optional = true, isList = false, doc = "For out of order. How long should be waited for new elements.")
	public void setRealTimeDelay(long realTimeDelay) {
		this.realTimeDelay = realTimeDelay;
	}

	public long getApplicationTimeDelay() {
		return applicationTimeDelay;
	}

	@Parameter(type = LongParameter.class, optional = true, isList = false, doc = "For out of order. After waiting some realTimeDelay, what time should be added to application time.")
	public void setApplicationTimeDelay(long applicationTimeDelay) {
		this.applicationTimeDelay = applicationTimeDelay;
	}
	
	@Override
	public boolean isSemanticallyEqual(IAccessAO operator) {
		if (!(operator instanceof AbstractAccessAO)) {
			return false;
		}
		AbstractAccessAO other = (AbstractAccessAO) operator;

		if (!Objects.equals(this.accessAOResource, other.accessAOResource)) {
			return false;
		}

		if (!Objects.equals(this.wrapper, other.wrapper)) {
			return false;
		}
		if (!Objects.equals(this.dataHandler, other.dataHandler)) {
			return false;
		}
		if (!Objects.equals(this.protocolHandler, other.protocolHandler)) {
			return false;
		}
		if (!Objects.equals(this.transportHandler, other.transportHandler)) {
			return false;
		}
		if (!Objects.equals(this.optionsMap, other.optionsMap)) {
			return false;
		}

		if (!Objects.equals(this.optionsList, other.optionsList)) {
			return false;
		}

		if (!Objects.equals(this.dateFormat, other.dateFormat)) {
			return false;
		}

		if (!Objects.equals(this.outputSchema, other.outputSchema)) {
			return false;
		}
		if (!Objects.equals(this.inputSchema, other.inputSchema)) {
			return false;
		}

		if (!Objects.equals(this.maxTimeToWaitForNewEventMS, other.maxTimeToWaitForNewEventMS)) {
			return false;
		}

		if (!Objects.equals(this.localMetaAttribute, other.localMetaAttribute)) {
			return false;
		}
		if (!Objects.equals(this.readMetaData, other.readMetaData)) {
			return false;
		}

		if (!Objects.equals(this.overWriteSchemaSourceName, other.overWriteSchemaSourceName)) {
			return false;
		}
		if (!Objects.equals(this.overWrittenSchema, other.overWrittenSchema)) {
			return false;
		}		
		if (!Objects.equals(this.realTimeDelay, other.realTimeDelay)) {
			return false;
		}
		if (!Objects.equals(this.applicationTimeDelay, other.applicationTimeDelay)) {
			return false;
		}
		return true;
	}

	// needed for PQL-Generator
	public boolean getReadMetaData() {
		return readMetaData();
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		SDFSchema schema = null;
		Map<String, SDFConstraint> constraints = new HashMap<>();

		@SuppressWarnings("rawtypes")
		Class<? extends IStreamObject> type = RegistryBinder.getDataHandlerRegistry().getCreatedType(dataHandler);
		if (type == null) {
			type = Tuple.class;
		}

		// TODO: Move more things from TAccessAORule here ... if possible

		OptionMap options = new OptionMap(optionsMap);

		@SuppressWarnings("rawtypes")
		IProtocolHandler ph = RegistryBinder.getProtocolHandlerRegistry().getIProtocolHandlerClass(protocolHandler);
		
		if (ph == null) {
			throw new IllegalArgumentException("Protocol handler "+protocolHandler+" not found!");
		}

		if (getOverWrittenSchema() == null && ph != null) {
			setOverWrittenSchema(ph.getSchema());
		}
		// For cases where the schema depends on the options, create a real
		// instance of transport handler
		if (getOverWrittenSchema() == null) {
			ITransportHandler th =  RegistryBinder.getTransportHandlerRegistry().getInstance(transportHandler, ph, options);
			setOverWrittenSchema(th != null ? th.getSchema() : null);
		}
		TimeUnit timeUnit = TimeUnit.MILLISECONDS;

		if (optionsMap.containsKey(SDFConstraint.BASE_TIME_UNIT)) {
			String unit = optionsMap.get(SDFConstraint.BASE_TIME_UNIT);
			TimeUnit btu = TimeUnit.valueOf(unit);
			if (btu != null) {
				timeUnit = btu;
			} else {
				throw new IllegalParameterException("Illegal time unit " + unit);
			}
		}
		constraints.put(SDFConstraint.BASE_TIME_UNIT, new SDFConstraint(SDFConstraint.BASE_TIME_UNIT, timeUnit));

		if (dateFormat != null) {
			constraints.put(SDFConstraint.DATE_FORMAT, new SDFConstraint(SDFConstraint.DATE_FORMAT, dateFormat));
		}

		boolean strictOrder = true;
		if (optionsMap.containsKey(SDFConstraint.STRICT_ORDER)) {
			String sorder = optionsMap.get(SDFConstraint.STRICT_ORDER);
			strictOrder = Boolean.parseBoolean(sorder);
		}

		List<SDFAttribute> attributes = getOverWrittenSchema() != null ? getOverWrittenSchema().getAttributes()
				: outputSchema.get(pos);
		List<SDFAttribute> s2 = new ArrayList<>();
		if (attributes != null && attributes.size() > 0) {
			// Add source name to attributes
			for (SDFAttribute a : attributes) {
				SDFAttribute newAttr;
				String sName = overWriteSchemaSourceName ? getName() : a.getSourceName();
				if (a.getDatatype() == SDFDatatype.START_TIMESTAMP
						|| a.getDatatype() == SDFDatatype.START_TIMESTAMP_STRING) {
					List<SDFConstraint> c = new ArrayList<>();
					c.add(new SDFConstraint(SDFConstraint.BASE_TIME_UNIT, timeUnit));
					SDFUnit unit = new SDFTimeUnit(timeUnit.toString());

					newAttr = new SDFAttribute(sName, a.getAttributeName(), a, unit, c);
				} else {
					newAttr = new SDFAttribute(sName, a.getAttributeName(), a);
				}
				s2.add(newAttr);
			}
		}
		schema = SDFSchemaFactory.createNewSchema(getName(), type, s2);
		schema = SDFSchemaFactory.createNewWithContraints(constraints, schema);
		schema = SDFSchemaFactory.createNewWithOutOfOrder(!strictOrder, schema);

		// Add meta attributes. If is set in operator, this overwrites other
		// options
		IMetaAttribute metaAttribute = localMetaAttribute != null ? localMetaAttribute : getMetaAttribute();

		if (metaAttribute != null) {
			List<SDFMetaSchema> metaSchema = metaAttribute.getSchema();
			schema = SDFSchemaFactory.createNewWithMetaSchema(schema, metaSchema);
			// Keep meta attribute!
			this.localMetaAttribute = metaAttribute;
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

		if (accessAOResource.isMarked() && this.outputSchema.size() > 0) {
			addError("Source " + accessAOResource + " already defined!");
			return false;
		}

		if (this.inputSchema != null) {
			if (this.outputSchema.get(0).size() != this.inputSchema.size()) {
				addError("For each attribute there must be at least one reader in the input schema");
				return false;
			}
		}

		if (!WrapperRegistry.containsWrapper(this.wrapper)) {
			addError("Wrapper " + this.wrapper + " is unknown");
			return false;
		}

		return true;
	}

	protected String buildString(List<?> elements, String sep) {
		StringBuffer v = new StringBuffer();
		for (Object s : elements) {
			v.append(s).append(sep);
		}
		return v.substring(0, v.length() - 1);
	}

	@Override
	public boolean isSinkOperator() {
		return false;
	}

	/**
	 * @return the overWrittenSchema
	 */
	public SDFSchema getOverWrittenSchema() {
		return overWrittenSchema;
	}

	/**
	 * @param overWrittenSchema
	 *            the overWrittenSchema to set
	 */
	public void setOverWrittenSchema(SDFSchema overWrittenSchema) {
		this.overWrittenSchema = overWrittenSchema;
	}


}
