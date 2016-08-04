package de.uniol.inf.is.odysseus.net;

import java.io.Serializable;
import java.util.UUID;

import com.google.common.base.Preconditions;

public final class OdysseusNodeID implements Comparable<OdysseusNodeID>, Serializable {

	private static final long serialVersionUID = -3068104616795173918L;
	
	private final UUID uuid;
	
	OdysseusNodeID(UUID uuid) {
		Preconditions.checkNotNull(uuid, "uuid must not be null!");

		this.uuid = uuid;
	}
	
	public static OdysseusNodeID fromString( String text ) {
		UUID uuid = UUID.fromString(text);
		return new OdysseusNodeID(uuid);
	}
	
	public static OdysseusNodeID generateNew() {
		return new OdysseusNodeID(UUID.randomUUID());
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

	@Override
	public int compareTo(OdysseusNodeID other) {
		return uuid.compareTo(other.uuid);
	}

}
