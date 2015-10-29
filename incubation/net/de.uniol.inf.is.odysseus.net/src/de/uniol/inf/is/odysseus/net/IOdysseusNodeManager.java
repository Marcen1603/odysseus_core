package de.uniol.inf.is.odysseus.net;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IOdysseusNodeManager {

	public void addNode( IOdysseusNode node );
	public void removeNode( IOdysseusNode node );
	public ImmutableCollection<IOdysseusNode> getNodes();
	public boolean existsNode(IOdysseusNode node);
	public Optional<IOdysseusNode> getNode(OdysseusNodeID nodeID);
	
	public void addListener( IOdysseusNodeManagerListener listener );
	public void removeListener( IOdysseusNodeManagerListener listener );
	
}
