package de.uniol.inf.is.odysseus.net;

public interface INodeManagerListener {

	public void nodeAdded( INodeManager sender, INode node );
	public void nodeRemoved( INodeManager sender, INode node );
	
}
