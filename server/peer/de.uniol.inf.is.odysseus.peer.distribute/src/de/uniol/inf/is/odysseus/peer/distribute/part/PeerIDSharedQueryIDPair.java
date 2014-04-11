package de.uniol.inf.is.odysseus.peer.distribute.part;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

public class PeerIDSharedQueryIDPair {
	
	public final PeerID pid;
	public final ID sharedQueryID;
	
	public PeerIDSharedQueryIDPair( PeerID pid, ID sharedQueryID ) {
		this.pid = pid;
		this.sharedQueryID = sharedQueryID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof PeerIDSharedQueryIDPair ) ) {
			return false;
		}
		
		if( obj == this ) {
			return true;
		}
		
		PeerIDSharedQueryIDPair other = (PeerIDSharedQueryIDPair)obj;
		
		return other.pid.equals(this.pid) && other.sharedQueryID.equals(this.sharedQueryID);
	}

	@Override
	public int hashCode() {
		return pid.hashCode() + 31 * sharedQueryID.hashCode();
	}
}