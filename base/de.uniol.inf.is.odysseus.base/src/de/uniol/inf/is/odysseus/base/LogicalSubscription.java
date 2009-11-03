package de.uniol.inf.is.odysseus.base;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class LogicalSubscription extends Subscription<ILogicalOperator> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 678442733825703258L;
	private SDFAttributeList inputSchema;

	public LogicalSubscription(ILogicalOperator target, int sinkPort,
			int sourcePort) {
		super(target, sinkPort, sourcePort);
	}
	
	public LogicalSubscription(ILogicalOperator target, int sinkPort,
			int sourcePort, SDFAttributeList inputSchema) {
		super(target, sinkPort, sourcePort);
		this.inputSchema = inputSchema;
	}

	public void setInputSchema(SDFAttributeList inputSchema) {
		this.inputSchema = inputSchema;
	}
	
	public SDFAttributeList getInputSchema() {
		return inputSchema;
	}
		
}
