package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "Stream", maxInputPorts = 0, minInputPorts = 0, category = { LogicalOperatorCategory.SOURCE }, doc = "Integrate a view.")
public class StreamAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	private Resource streamname;
	private String nodeName;
	private List<SDFAttribute> attributes;
	@SuppressWarnings("rawtypes")
	private Class<? extends IStreamObject> type;
	private SDFSchema schema;

	public StreamAO() {
		// we need this
	}

	public StreamAO(Resource name) {
		super();
		this.streamname = name;
		setName(this.streamname.getResourceName());
		addParameterInfo("SOURCENAME", "'" + streamname.getResourceName() + "'");
	}

	public StreamAO(StreamAO streamAO) {
		super(streamAO);

		this.streamname = streamAO.streamname;
		if (streamAO.attributes != null) {
			this.attributes = new ArrayList<>(streamAO.attributes);
		}
		this.nodeName = streamAO.nodeName;

		setName(streamname.getResourceName());
		addParameterInfo("SOURCENAME", "'" + streamname.getResourceName() + "'");

		if (!Strings.isNullOrEmpty(this.nodeName)) {
			addParameterInfo("NODE", "'" + this.nodeName + "'");
		}
		this.schema = streamAO.schema;
		this.type = streamAO.type;
	}

	@Override
	@Parameter(name = "Name", type = StringParameter.class, optional = true, doc = "Name of the operator (e.g. for visulization).")
	public void setName(String name) {
		super.setName(name);
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
		return new SDFSchema(getStreamname().toString(), type, attributes);
	}

	@Parameter(name = "Source", type = SourceParameter.class, optional = true, possibleValues = "__DD_SOURCES", possibleValuesAreDynamic = true)
	public void setSource(AccessAO inputStream) {
		if( inputStream == null ) {
			this.schema = null;
		} else {
			this.schema = inputStream.getOutputSchema();
			setSourceName(inputStream.getAccessAOName());
		}
	}

	@Parameter(name = "SourceName", type = ResourceParameter.class, optional = true, possibleValues = "__DD_SOURCES", possibleValuesAreDynamic = true)
	public void setSourceName(Resource resource) {
		this.streamname = resource;
		setName(streamname.getResourceName());
		
		addParameterInfo("SOURCENAME", "'" + streamname.getResourceName() + "'");
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema", isList = true, optional = true, doc = "The output schema.")
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
		
		if( attributes == null || attributes.isEmpty() ) {
			removeParameterInfo("SCHEMA");
		} else {
			addParameterInfo("SCHEMA", schemaToString(attributes));
		}
	}

//	@Parameter(type = StringParameter.class, name = "DataHandler", optional = false, possibleValues = "getDataHandlerValues", doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	@Parameter(type = StringParameter.class, name = "DataHandler", optional = false, doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	public void setDataHandler(String dataHandler) {
		type = DataHandlerRegistry.getCreatedType(dataHandler);
		if( !Strings.isNullOrEmpty(dataHandler)) {
			addParameterInfo("DATAHANDLER", "'" + dataHandler + "'");
		} else {
			removeParameterInfo("DATAHANDLER");
		}
	}
	
	public List<String> getDataHandlerValues() {
		return DataHandlerRegistry.getStreamableDataHandlerNames();
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

		if (!Strings.isNullOrEmpty(this.nodeName)) {
			addParameterInfo("NODE", "'" + nodeName + "'");
		} else {
			removeParameterInfo("NODE");
		}
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
				addError(new IllegalParameterException(
						"Stream cannot be combined with type"));
			}
			if (attributes != null) {
				isValid = false;
				addError(new IllegalParameterException(
						"Stream cannot be combined with schema"));
			}
		}
		if (schema == null) {
			if (type == null) {
				isValid = false;
				addError(new IllegalParameterException(
						"Type or stream must be given"));
			}
			if (attributes == null) {
				isValid = false;
				addError(new IllegalParameterException(
						"Schema or stream must be given"));
			}
			if (streamname == null) {
				isValid = false;
				addError(new IllegalParameterException(
						"Streamname or stream must be given"));
			}
		}

		return isValid;
	}
	
	private static String schemaToString(List<SDFAttribute> outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		final SDFAttribute[] attributes = outputSchema.toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			final SDFAttribute attribute = attributes[i];
			sb.append("[");
			if( !Strings.isNullOrEmpty(attribute.getSourceName())) {
				sb.append("'").append(attribute.getSourceName()).append("',");
			}
			sb.append("'").append(attribute.getAttributeName());
			sb.append("', '");
			sb.append(attribute.getDatatype().getURI());
			sb.append("']");
			if (i < attributes.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
