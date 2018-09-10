package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.internal.RegistryBinder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CONVERTER", doc="This operator can be used to transform element with other protocol handler, e.g. read a complete document from a server and then parse this document with csv or xml", category={LogicalOperatorCategory.TRANSFORM})
public class ConverterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -2630448144140835791L;
	private String protocolHandler;
	private String inputDataHandler;
	private String outputDataHandler;
	private String dateFormat;
	private String source;
	private boolean updateMeta = true;
	final private OptionMap optionsMap = new OptionMap();
	private final List<Option> options = Lists.newArrayList();
	private List<SDFAttribute> outputAttributes;

	public ConverterAO() {
		super();
	}

	public ConverterAO(ConverterAO converterAO) {
		super(converterAO);
		this.protocolHandler = converterAO.protocolHandler;
		this.inputDataHandler = converterAO.inputDataHandler;
		this.outputDataHandler = converterAO.outputDataHandler;
		this.optionsMap.addAll(converterAO.optionsMap);
		this.options.addAll(converterAO.options);
		this.outputAttributes = converterAO.outputAttributes;
		this.source = converterAO.source;
		this.updateMeta = converterAO.updateMeta;
	}

	@Parameter(name = "protocol", type = StringParameter.class, doc = "Protocol handler to use.")
	public void setProtocolHandler(String protocolHandler) {
		this.protocolHandler = protocolHandler;
	}

	public String getProtocolHandler() {
		return protocolHandler;
	}

	@Parameter(name = "inputDataHandler", type = StringParameter.class, doc = "Datahandler to use as input (e.g. format deliefered from preceeding operator)")
	public void setInputDataHandler(String inputDataHandler) {
		this.inputDataHandler = inputDataHandler;
	}

	public String getInputDataHandler() {
		return inputDataHandler;
	}

	@Parameter(name = "outputDataHandler", type = StringParameter.class, doc = "Datahandler to use for creation of elements.")
	public void setOutputDataHandler(String outputDataHandler) {
		this.outputDataHandler = outputDataHandler;
	}

	public String getOutputDataHandler() {
		return outputDataHandler;
	}

	@Parameter(name = "options", isList = true, type = OptionParameter.class, optional = true, doc = "Additional options. See help doc for further information")
	public void setOptions(List<Option> ops) {
		for (Option option : ops) {
			optionsMap.setOption(option.getName().toLowerCase(), option.getValue());
		}
		options.addAll(ops);
	}
	
	public List<Option> getOptions() {
		return options;
	}

	public void setOptionMap(OptionMap ops) {
		optionsMap.clear();
		optionsMap.addAll(ops);
	}	
	
	public OptionMap getOptionMap() {
		return optionsMap;
	}

    @Parameter(name = "schema", type = CreateSDFAttributeParameter.class, isList = true, optional = true, doc = "The output schema of this operator")
	public void setOutputAttributes(List<SDFAttribute> outputSchema) {
		this.outputAttributes = outputSchema;
	}
	
	public List<SDFAttribute> getOutputAttributes() {
		return outputAttributes;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (pos > 0) {
			throw new IllegalArgumentException(
					"This operator has only one input!");
		}
		
		@SuppressWarnings("rawtypes")
		Class<? extends IStreamObject> type = RegistryBinder.getDataHandlerRegistry().getCreatedType(outputDataHandler);
		if (type == null){
			type = Tuple.class;
		}
		
		String name = "";
		if (source != null){
			name = source;
		}else if (getInputSchema() != null){
			name = getInputSchema().getURI();
		}
		SDFSchema inputSchema = getInputSchema();
		if (inputSchema != null){
			return SDFSchemaFactory.createNewSchema(name, (Class<? extends IStreamObject<?>>) type, outputAttributes, inputSchema);
		}
		return SDFSchemaFactory.createNewSchema(name, type, outputAttributes);
	}

	@Parameter(name = "dateFormat", type = StringParameter.class, optional = true, doc = "Format used if schema contains (Start|End)TimestampString")
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	@Parameter(name = "source", type = StringParameter.class, optional = true, doc = "Overwrite source name")
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getSource() {
		return source;
	}
	
	public boolean isUpdateMeta() {
		return updateMeta;
	}

	@Parameter(name="updateMeta", type = BooleanParameter.class, optional = true, doc="If set to false, existing meta data will not be touched.")
	public void setUpdateMeta(boolean updateMeta) {
		this.updateMeta = updateMeta;
	}

	@Override
	public ConverterAO clone() {
		return new ConverterAO(this);
	}

}
