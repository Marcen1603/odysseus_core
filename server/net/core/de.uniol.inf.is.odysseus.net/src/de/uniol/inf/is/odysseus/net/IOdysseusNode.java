package de.uniol.inf.is.odysseus.net;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.base.Optional;

public interface IOdysseusNode extends Serializable {

	OdysseusNodeID getID();
	String getName();

	void addProperty( String key, String value );
	void removeProperty( String key );
	Optional<String> getProperty( String key );
	boolean existsProperty( String key );
	Collection<String> getProperyKeys();

	boolean isLocal();
	String toString(boolean verbose);
}
