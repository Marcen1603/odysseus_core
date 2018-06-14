package de.uniol.inf.is.odysseus.net;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;

public interface IOdysseusNodeManager {

	public void addNode( IOdysseusNode node );
	public void removeNode( IOdysseusNode node );
	public ImmutableCollection<IOdysseusNode> getNodes();
	public ImmutableCollection<OdysseusNodeID> getNodeIDs();
	public boolean existsNode(IOdysseusNode node);
	public boolean existsNode(OdysseusNodeID nodeID);
	public Optional<IOdysseusNode> getNode(OdysseusNodeID nodeID);
	
	public IOdysseusNode getLocalNode() throws OdysseusNetException;
	public boolean isLocalNode(OdysseusNodeID nodeID);
	
	public void addListener( IOdysseusNodeManagerListener listener );
	public void removeListener( IOdysseusNodeManagerListener listener );
	
}
