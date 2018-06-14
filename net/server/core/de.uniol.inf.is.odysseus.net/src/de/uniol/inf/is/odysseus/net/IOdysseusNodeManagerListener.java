package de.uniol.inf.is.odysseus.net;

public interface IOdysseusNodeManagerListener {

	public void nodeAdded( IOdysseusNodeManager sender, IOdysseusNode node );
	public void nodeRemoved( IOdysseusNodeManager sender, IOdysseusNode node );
	
}
