package de.uniol.inf.is.odysseus.p2p.superpeer;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;

public abstract class AbstractSuperPeer implements IPeer{
	
	public abstract void startNetwork();

	@Override
	public abstract void startPeer();

	@Override
	public abstract void stopPeer();
	

}
