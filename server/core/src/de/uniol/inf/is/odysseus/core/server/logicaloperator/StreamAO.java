package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.internal.RegistryBinder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "Stream", maxInputPorts = 0, minInputPorts = 0, category = { LogicalOperatorCategory.SOURCE }, doc = "Integrate a view.")
public class StreamAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	
	private Resource streamname;
	private String nodeName;
	private List<SDFAttribute> attributes;
	private SDFSchema schema;
	private AccessAO inputStream;
	
	@SuppressWarnings("rawtypes")
	private Class<? extends IStreamObject> type;

	public StreamAO() {
		// we need this
	}

	public StreamAO(Resource name) {
		super();
		this.streamname = name;
		
		setName(this.streamname.getResourceName());
	}

	public StreamAO(StreamAO streamAO) {
		super(streamAO);

		this.inputStream = streamAO.inputStream;
		this.streamname = streamAO.streamname;
		if (streamAO.attributes != null) {
			this.attributes = new ArrayList<>(streamAO.attributes);
		}
		this.nodeName = streamAO.nodeName;

		setName(streamname.getResourceName());

		this.schema = streamAO.schema;
		this.type = streamAO.type;
	}


	@Override
	public StreamAO clone() {
		return new StreamAO(this);
	}

	public Resource getStreamname() {
		return streamname;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (schema != null) {
			return schema;
		}
		return SDFSchemaFactory.createNewSchema(getStreamname().toString(), type, attributes);
	}

	@Parameter(name = "Source", type = SourceParameter.class, optional = true, possibleValues = "__DD_SOURCES", possibleValuesAreDynamic = true)
	public void setSource(AccessAO inputStream) {
		if (inputStream == null) {
			this.schema = null;
			this.streamname = null;
			this.inputStream = null;
		} else {
			this.schema = inputStream.getOutputSchema();
			this.streamname = inputStream.getAccessAOName();
			this.inputStream = inputStream;
		}
	}
	
	public AccessAO getSource() {
		return inputStream;
	}

	@Parameter(name = "SourceName", type = ResourceParameter.class, optional = true, possibleValues = "__DD_SOURCES", possibleValuesAreDynamic = true)
	public void setSourceName(Resource resource) {
		this.streamname = resource;
		setName(streamname.getResourceName());
	}
	
	public Resource getSourceName() {
		return streamname;
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema", isList = true, optional = true, doc = "The output schema.")
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	// @Parameter(type = StringParameter.class, name = "DataHandler", optional =
	// false, possibleValues = "getDataHandlerValues", doc =
	// "The name of the datahandler to use, e.g. Tuple or Document.")
	@Parameter(type = StringParameter.class, name = "DataHandler", optional = true, doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	public void setDataHandler(String dataHandler) {
		type = RegistryBinder.getDataHandlerRegistry().getCreatedType(dataHandler);
	}
	
	public String getDataHandler() {
		return type != null ? type.getName() : null;
	}

	public List<String> getDataHandlerValues() {
		return RegistryBinder.getDataHandlerRegistry().getStreamableDataHandlerNames();
	}

	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return "StreamAO@" + hashCode() + " " + streamname;
	}

	@Parameter(name = "Node", type = StringParameter.class, optional = true)
	public void setNode(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNode() {
		return nodeName;
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (schema != null) {
			if (type != null) {
				isValid = false;
				addError(
						"Stream cannot be combined with DataHandler");
			}
			if (attributes != null) {
				isValid = false;
				addError(
						"Stream cannot be combined with schema");
			}
		}
		if (schema == null) {
			if (type == null) {
				isValid = false;
				addError(
						"DataHandler _or_ stream must be given");
			}
			if (attributes == null) {
				isValid = false;
				addError(
						"Schema _or_ stream must be given");
			}
			if (streamname == null) {
				isValid = false;
				addError(
						"Streamname _or_ stream must be given");
			}
		}

		return isValid;
	}
	
	@Override
	public boolean isSinkOperator() {
		return false;
	}
}
