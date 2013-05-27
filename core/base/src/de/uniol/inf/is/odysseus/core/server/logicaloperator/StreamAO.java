package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

public class StreamAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;
	private String streamname;

	public StreamAO(){
		// we need this
	}
	
	public StreamAO(String name){
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

	
	public String getStreamname() {
		return streamname;
	}

	@Parameter(name="source", type=StringParameter.class)
	public void setStreamname(String streamname) {
		this.streamname = streamname;
	}

	

}
