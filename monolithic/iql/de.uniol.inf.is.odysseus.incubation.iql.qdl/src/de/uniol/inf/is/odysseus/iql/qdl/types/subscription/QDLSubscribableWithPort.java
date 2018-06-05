package de.uniol.inf.is.odysseus.iql.qdl.types.subscription;


public class QDLSubscribableWithPort {
	private final int port;
	private final Subscribable source;
	
	public QDLSubscribableWithPort(int port,Subscribable source) {
		this.port = port;
		this.source = source;
	}
	
	public QDLSubscribableWithPort(Subscribable source,int port) {
		this.port = port;
		this.source = source;
	}


	public int getPort() {
		return port;
	}

	public Subscribable getSource() {
		return source;
	}

	
	
	
}
