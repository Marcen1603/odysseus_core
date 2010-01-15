package de.uniol.inf.is.odysseus.base;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class LogicalSubscription extends Subscription<ILogicalOperator> implements Serializable{

	private static final long serialVersionUID = 678442733825703258L;
		
	public LogicalSubscription(ILogicalOperator target, int sinkPort,
			int sourcePort, SDFAttributeList schema) {
		super(target, sinkPort, sourcePort, schema);
	}
		
	//ACHTUNG. NICHT ÄNDERN!
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
		
}
