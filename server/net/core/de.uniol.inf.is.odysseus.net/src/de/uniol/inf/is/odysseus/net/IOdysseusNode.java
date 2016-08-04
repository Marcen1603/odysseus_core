package de.uniol.inf.is.odysseus.net;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.base.Optional;

public interface IOdysseusNode extends Serializable {

	public OdysseusNodeID getID();
	public String getName();
	
	public void addProperty( String key, String value );
	public void removeProperty( String key );
	public Optional<String> getProperty( String key );
	public boolean existsProperty( String key );
	public Collection<String> getProperyKeys();
	
	public boolean isLocal();
}
