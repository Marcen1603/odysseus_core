package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;


@LogicalOperator(name="Stream",maxInputPorts=0, minInputPorts=0,category={LogicalOperatorCategory.SOURCE}, doc="Integrate a view.")
public class StreamAO extends AbstractLogicalOperator {
	
	private static final long serialVersionUID = 1L;
	private Resource streamname;

	public StreamAO(){
		// we need this
	}
	
	public StreamAO(Resource name){
		super();
		this.streamname = name;
	}
	
	public StreamAO(StreamAO streamAO) {
		super(streamAO);
		this.streamname = streamAO.streamname;
	}

	@Override
	public StreamAO clone() {
		return new StreamAO(this);
	}

	
	public Resource getStreamname() {
		return streamname;
	}

	@Parameter(name="Source", type=StringParameter.class, optional=false, possibleValues="__DD_SOURCES")
	public void setSource(String streamname) {
		this.streamname = new Resource(streamname);
	}

	@Override
	public String toString() {
		return "StreamAO@"+hashCode()+" "+streamname;
	}
	

}
