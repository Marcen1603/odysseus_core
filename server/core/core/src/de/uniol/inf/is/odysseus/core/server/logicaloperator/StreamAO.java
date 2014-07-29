package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.SourceParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "Stream", maxInputPorts = 0, minInputPorts = 0, category = { LogicalOperatorCategory.SOURCE }, doc = "Integrate a view.")
public class StreamAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	private Resource streamname;
	private SDFSchema schema;
	private String nodeName;

	public StreamAO() {
		// we need this
	}

	public StreamAO(Resource name) {
		super();
		this.streamname = name;
		setName(this.streamname.getResourceName());
		addParameterInfo("SOURCE", "'" + streamname.getResourceName() + "'");
	}

	public StreamAO(StreamAO streamAO) {
		super(streamAO);
		
		this.streamname = streamAO.streamname;
		this.schema = streamAO.schema;
		this.nodeName = streamAO.nodeName;
		
		setName(streamname.getResourceName());
		addParameterInfo("SOURCE", "'" + streamname.getResourceName() + "'");
		
		if( !Strings.isNullOrEmpty(this.nodeName)) {
			addParameterInfo("NODE", "'" + this.nodeName + "'");
		}
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
		return schema;
	}

	@Parameter(name = "Source", type = SourceParameter.class, optional = false, possibleValues = "__DD_SOURCES", possibleValuesAreDynamic = true)
	public void setSource(AccessAO inputStream) {
		this.schema = inputStream.getOutputSchema();
		setSourceName(inputStream.getAccessAOName());
	}
	
	public void setSourceName( Resource resource ) {
		this.streamname = resource;
		setName(streamname.getResourceName());
		addParameterInfo("SOURCE", "'" + streamname.getResourceName() + "'");
	}

	@Override
	public String toString() {
		return "StreamAO@" + hashCode() + " " + streamname;
	}
	
	@Parameter(name = "Node", type = StringParameter.class, optional = true)	
	public void setNode( String nodeName ) {
		this.nodeName = nodeName;
		
		if( !Strings.isNullOrEmpty(this.nodeName)) {
			addParameterInfo("NODE", "'" + nodeName + "'");
		} else {
			removeParameterInfo("NODE");
		}
	}
	
	public String getNode() {
		return nodeName;
	}
}
