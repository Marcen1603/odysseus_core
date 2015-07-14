package de.uniol.inf.is.odysseus.net;

import com.google.common.collect.ImmutableCollection;

public interface INodeManager {

	public void addNode( INode node );
	public void removeNode( INode node );
	public ImmutableCollection<INode> getNodes();
	
	public void addListener( INodeManagerListener listener );
	public void removeListener( INodeManagerListener listener );
	
}
