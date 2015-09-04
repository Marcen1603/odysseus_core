package de.uniol.inf.is.odysseus.iql.qdl.types;

import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator;

public class IQLSubscription {
	
	private final IQDLOperator target;
	private final IQDLOperator source;

	private final int sourceOutPort;
	private final int sinkInPort;

	public IQLSubscription(IQDLOperator source, IQDLOperator target, int sourceOutPort, int sinkInPort) {
		this.target = target;
		this.source = source;
		this.sourceOutPort = sourceOutPort;
		this.sinkInPort = sinkInPort;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IQLSubscription) {
			IQLSubscription subs = (IQLSubscription) obj;
			return this.source == subs.source && this.target == subs.target && this.sourceOutPort == subs.sourceOutPort && this.sinkInPort == subs.sinkInPort;
		} else {
			return false;
		}
	}

	public IQDLOperator getTarget() {
		return target;
	}

	public int getSourceOutPort() {
		return sourceOutPort;
	}

	public IQDLOperator getSource() {
		return source;
	}

	public int getSinkInPort() {
		return sinkInPort;
	}
	
	


}
