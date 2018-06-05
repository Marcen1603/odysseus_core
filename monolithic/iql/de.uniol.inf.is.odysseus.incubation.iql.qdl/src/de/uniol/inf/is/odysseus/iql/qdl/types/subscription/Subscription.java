package de.uniol.inf.is.odysseus.iql.qdl.types.subscription;

public class Subscription {
	
	private final Subscriber sink;
	private final Subscribable source;

	private final int sourceOutPort;
	private final int sinkInPort;

	public Subscription(Subscribable source, Subscriber sink, int sourceOutPort, int sinkInPort) {
		this.sink = sink;
		this.source = source;
		this.sourceOutPort = sourceOutPort;
		this.sinkInPort = sinkInPort;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Subscription) {
			Subscription subs = (Subscription) obj;
			return this.source == subs.source && this.sink == subs.sink && this.sourceOutPort == subs.sourceOutPort && this.sinkInPort == subs.sinkInPort;
		} else {
			return false;
		}
	}

	public Subscriber getSink() {
		return sink;
	}

	public int getSourceOutPort() {
		return sourceOutPort;
	}

	public Subscribable getSource() {
		return source;
	}

	public int getSinkInPort() {
		return sinkInPort;
	}
	
	


}
