package de.uniol.inf.is.odysseus.net;

import com.google.common.collect.ImmutableCollection;

public interface IOdysseusNodeManager {

	public void addNode( IOdysseusNode node );
	public void removeNode( IOdysseusNode node );
	public ImmutableCollection<IOdysseusNode> getNodes();
	public boolean existsNode(IOdysseusNode node);
	
	public void addListener( IOdysseusNodeManagerListener listener );
	public void removeListener( IOdysseusNodeManagerListener listener );
	
}
