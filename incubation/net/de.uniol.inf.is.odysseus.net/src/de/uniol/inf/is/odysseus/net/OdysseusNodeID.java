package de.uniol.inf.is.odysseus.net;

import java.util.UUID;

import com.google.common.base.Preconditions;

public final class OdysseusNodeID {

	private final UUID uuid;
	
	public OdysseusNodeID(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		this.uuid = uuid;
	}
	
	@Override
	public String toString() {
		return uuid.toString();
	}	
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof OdysseusNodeID)) {
			return false;
		}
		if( obj == this ) {
			return true;
		}
		
		OdysseusNodeID other = (OdysseusNodeID)obj;
		return this.uuid.equals(other.uuid);
	}
	
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}
}
